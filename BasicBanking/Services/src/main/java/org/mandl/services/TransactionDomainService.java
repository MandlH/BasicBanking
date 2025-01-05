package org.mandl.services;

import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
import org.mandl.*;
import org.mandl.entities.BankAccount;
import org.mandl.entities.Transaction;
import org.mandl.mapper.TransactionMapper;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.repositories.RepositoryWrapper;
import org.mandl.repositories.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

final class TransactionDomainService
    extends BaseDomainService
        implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public TransactionDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.transactionRepository = repositoryWrapper.getTransactionRepository();
        this.bankAccountRepository = repositoryWrapper.getBankAccountRepository();
    }

    @Override
    public List<TransactionDto> getTransactions(UUID bankAccountId) {
        try {
            var transactions = transactionRepository.getUserTransactions(bankAccountId);
            var transactionsDto = transactions.stream()
                    .map(TransactionMapper.INSTANCE::domainToDto)
                    .toList();
            logger.info("Transactions retrieved for " + userContext.getUsername());
            return transactionsDto;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving Transactions", e);
        }
    }

    @Override
    public void createTransaction(TransactionDto transactionDto,
                                  String bankAccountNumberFrom,
                                  String bankAccountNumberTo) {
        try {
            var transaction = TransactionMapper.INSTANCE.dtoToDomain(transactionDto);

            if (bankAccountNumberFrom != null && bankAccountNumberFrom.equals(bankAccountNumberTo)) {
                throw new ServiceException("Source cannot be the same as the destination bank account number!");
            }

            repositoryWrapper.beginTransaction();
            switch (transactionDto.getTransactionType()) {
                case TRANSFER -> executeTransferTransaction(transaction, bankAccountNumberFrom, bankAccountNumberTo);
                case WITHDRAWAL -> executeWithdrawalTransaction(transaction, bankAccountNumberFrom);
                case DEPOSIT -> executeDepositTransaction(transaction, bankAccountNumberTo);
            }

            transactionRepository.save(transaction);
            repositoryWrapper.commitTransaction();
            logger.info(transaction.getTransactionType() + " transaction created by " + userContext.getUsername());
        } catch (ServiceException | IllegalArgumentException e) {
            repositoryWrapper.rollbackTransaction();
            throw e;
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Error occurred transaction could not be created.", e);
        }
    }

    private void executeDepositTransaction(Transaction transaction, String bankAccountNumberTo) {
        var bankAccountTo = loadOrValidateBankAccount(bankAccountNumberTo, true);
        validateTransactionAmount(transaction.getAmount(), true);

        bankAccountTo.deposit(transaction.getAmount());
        transaction.setBankAccountTo(bankAccountTo);
        bankAccountRepository.update(bankAccountTo);
    }

    private void executeWithdrawalTransaction(Transaction transaction, String bankAccountNumberFrom) {
        var bankAccountFrom = loadOrValidateBankAccount(bankAccountNumberFrom, true);
        validateTransactionAmount(transaction.getAmount(), false);

        bankAccountFrom.withdraw(transaction.getAmount());
        transaction.setBankAccountFrom(bankAccountFrom);
        bankAccountRepository.update(bankAccountFrom);
    }

    private void executeTransferTransaction(Transaction transaction,
                                            String bankAccountNumberFrom, String bankAccountNumberTo) {
        var bankAccountFrom = loadOrValidateBankAccount(bankAccountNumberFrom, true);
        var bankAccountTo = loadOrValidateBankAccount(bankAccountNumberTo, false);
        validateTransactionAmount(transaction.getAmount(), true);

        // A transfer is a withdrawal from source and a deposit to destination
        bankAccountFrom.withdraw(transaction.getAmount().negate());
        bankAccountTo.deposit(transaction.getAmount());

        transaction.setBankAccountFrom(bankAccountFrom);
        transaction.setBankAccountTo(bankAccountTo);

        bankAccountRepository.update(bankAccountFrom);
        bankAccountRepository.update(bankAccountTo);
    }

    private BankAccount loadOrValidateBankAccount(String bankAccountNumber, boolean isOwner) {
        var bankAccount = bankAccountRepository.findByAccountNumber(bankAccountNumber);

        if (bankAccount == null) {
            throw new ServiceException("Bank account does not exist: " + bankAccountNumber);
        }

        if (isOwner){
            if (!bankAccount.getOwner().getId().equals(userContext.getUserId())){
                throw new ServiceException("You do not have bank account with this Account No.: " + bankAccountNumber);
            }
        }

        return bankAccount;
    }

    private void validateTransactionAmount(BigDecimal amount, boolean isPositive) {
        if (amount == null) {
            throw new ServiceException("Transaction amount must have a value!");
        }

        if ((isPositive && amount.compareTo(BigDecimal.ZERO) <= 0) ||
                (!isPositive && amount.compareTo(BigDecimal.ZERO) >= 0)) {
            throw new ServiceException("Invalid transaction amount. Amount must be " + (isPositive ? "positive." : "negative."));
        }
    }
}