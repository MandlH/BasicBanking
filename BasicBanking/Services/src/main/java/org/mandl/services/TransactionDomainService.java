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
        implements TransactionService {

    private static final LoggingHandler logger = LoggingHandler.getLogger(BankAccountDomainService.class);
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
    public List<TransactionDto> getTransactions(TransactionTypeDto typeDto, UUID bankAccountId) {
        try {
            var transactionType = TransactionMapper.INSTANCE.dtoToDomain(typeDto);
            var transactions = transactionRepository.getTransactions(transactionType, bankAccountId);
            return transactions.stream()
                    .map(TransactionMapper.INSTANCE::domainToDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving Transactions", e);
        }
    }

    @Override
    public List<TransactionDto> getTransactions(UUID bankAccountId) {
        try {
            var transactions = transactionRepository.getTransactions(bankAccountId);
            return transactions.stream()
                    .map(TransactionMapper.INSTANCE::domainToDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving Transactions", e);
        }
    }

    @Override
    public void createTransaction(TransactionDto transactionDto, BankAccountDto bankAccountFromDto, BankAccountDto bankAccountToDto) {
        try {
            var transaction = TransactionMapper.INSTANCE.dtoToDomain(transactionDto);
            var bankAccountFrom = loadOrValidateBankAccount(bankAccountFromDto);
            var bankAccountTo = loadOrValidateBankAccount(bankAccountToDto);

            repositoryWrapper.beginTransaction();
            switch (transactionDto.getTransactionType()) {
                case TRANSFER -> executeTransferTransaction(transaction, bankAccountFrom, bankAccountTo);
                case WITHDRAWAL -> executeWithdrawalTransaction(transaction, bankAccountFrom);
                case DEPOSIT -> executeDepositTransaction(transaction, bankAccountTo);
            }
            repositoryWrapper.commitTransaction();

            repositoryWrapper.beginTransaction();
            transactionRepository.save(transaction);
            repositoryWrapper.commitTransaction();
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Transaction could not be created.", e);
        }
    }

    private BankAccount loadOrValidateBankAccount(BankAccountDto bankAccountDto) {
        if (bankAccountDto == null) {
            return null;
        }
        var bankAccount = bankAccountRepository.findByAccountNumber(bankAccountDto.getAccountNumber());
        if (bankAccount == null) {
            throw new ServiceException("Bank account does not exist: " + bankAccountDto.getAccountNumber());
        }
        return bankAccount;
    }

    private void executeDepositTransaction(Transaction transaction, BankAccount bankAccountTo) {
        validateBankAccountExists(bankAccountTo);
        validateTransactionAmount(transaction.getAmount(), true);

        var bankAccount = bankAccountRepository.findByAccountNumber(bankAccountTo.getAccountNumber());
        bankAccount.setBalance(bankAccount.getBalance().add(transaction.getAmount()));
        transaction.setBankAccountTo(bankAccount);
        bankAccountRepository.update(bankAccount);
    }

    private void executeWithdrawalTransaction(Transaction transaction, BankAccount bankAccountFrom) {
        validateBankAccountExists(bankAccountFrom);
        validateTransactionAmount(transaction.getAmount(), false);

        var bankAccount = bankAccountRepository.findByAccountNumber(bankAccountFrom.getAccountNumber());
        if (bankAccount == null) {
            throw new ServiceException("Bank account does not exist.");
        }

        BigDecimal newBalance = bankAccount.getBalance().add(transaction.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("Insufficient funds for withdrawal.");
        }

        bankAccount.setBalance(newBalance); // Withdrawal amount is negative
        transaction.setBankAccountFrom(bankAccount);
        bankAccountRepository.update(bankAccount);
    }

    private void executeTransferTransaction(Transaction transaction, BankAccount bankAccountFrom, BankAccount bankAccountTo) {
        validateBankAccountExists(bankAccountFrom);
        validateBankAccountExists(bankAccountTo);
        validateTransactionAmount(transaction.getAmount(), true);

        var sourceAccount = bankAccountRepository.findByAccountNumber(bankAccountFrom.getAccountNumber());
        var destinationAccount = bankAccountRepository.findByAccountNumber(bankAccountTo.getAccountNumber());

        if (sourceAccount == null || destinationAccount == null) {
            throw new ServiceException("One or both bank accounts do not exist.");
        }

        if (sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new ServiceException("Insufficient funds for transfer.");
        }

        BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
        BigDecimal newDestinationBalance = destinationAccount.getBalance().add(transaction.getAmount());

        sourceAccount.setBalance(newSourceBalance);
        destinationAccount.setBalance(newDestinationBalance);

        transaction.setBankAccountFrom(sourceAccount);
        transaction.setBankAccountTo(destinationAccount);

        bankAccountRepository.update(sourceAccount);
        bankAccountRepository.update(destinationAccount);
    }

    private void validateBankAccountExists(BankAccount bankAccount) {
        if (bankAccount == null || bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()) == null) {
            throw new ServiceException("Bank account does not exist.");
        }
    }

    private void validateTransactionAmount(BigDecimal amount, boolean isPositive) {
        if (amount == null) {
            throw new ServiceException("Transaction amount cannot be null.");
        }

        if ((isPositive && amount.compareTo(BigDecimal.ZERO) <= 0) ||
                (!isPositive && amount.compareTo(BigDecimal.ZERO) >= 0)) {
            throw new ServiceException("Invalid transaction amount. Amount must be " + (isPositive ? "positive." : "negative."));
        }
    }
}
