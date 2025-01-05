package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
import org.mandl.BankAccountDto;
import org.mandl.BankAccountService;
import org.mandl.entities.BankAccount;
import org.mandl.exceptions.AuthenticationException;
import org.mandl.mapper.BankAccountMapper;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.util.List;

@ApplicationScoped
final class BankAccountDomainService
    extends BaseDomainService
        implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public BankAccountDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.bankAccountRepository = repositoryWrapper.getBankAccountRepository();
    }

    @Override
    public List<BankAccountDto> getAllBankAccounts() {
        try {
            List<BankAccount> bankAccounts = bankAccountRepository.getAllBankAccountsFromOwner();
            var bankAccountsDto = bankAccounts.stream()
                    .map(BankAccountMapper.INSTANCE::domainToDto)
                    .toList();
            logger.info("User " + userContext.getUsername() + " retrieved his bank accounts.");
            return bankAccountsDto;
        } catch (Exception e) {
            throw new ServiceException("Error occurred while retrieving bank accounts.", e);
        }
    }

    @Override
    public BankAccountDto createBankAccount(BankAccountDto bankAccountDto) {
        try {
            var bankAccount = BankAccountMapper.INSTANCE.dtoToDomain(bankAccountDto);

            var existingBankAccount = bankAccountRepository.findByAccountNumber(bankAccountDto.getAccountNumber());

            if (existingBankAccount != null) {
                throw new ServiceException("Bank account number already exists.");
            }

            repositoryWrapper.beginTransaction();
            bankAccountRepository.createBankAccount(bankAccount);
            repositoryWrapper.commitTransaction();

            logger.info("Bank account with number " + bankAccount.getMaskedAccountNumber() + " created by " + userContext.getUsername());
            return BankAccountMapper.INSTANCE.domainToDto(bankAccount);
        } catch (ServiceException | IllegalArgumentException e) {
            repositoryWrapper.rollbackTransaction();
            throw e;
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Error occurred while creating the bank account.", e);
        }
    }

    @Override
    public void deleteBankAccount(String accountNumber) {
        try {
            repositoryWrapper.beginTransaction();
            var bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);

            if (bankAccount == null) {
                throw new ServiceException("Bank account does not exist");
            }

            if (!bankAccount.getOwner().getId().equals(userContext.getUserId())){
                throw new AuthenticationException("User " + userContext.getUserId() + " tried to delete " + accountNumber);
            }

            bankAccountRepository.delete(bankAccount);
            repositoryWrapper.commitTransaction();
            logger.info("Bank account with number " + bankAccount.getMaskedAccountNumber() + " deleted by " + userContext.getUsername());
        } catch (ServiceException e) {
            repositoryWrapper.rollbackTransaction();
            throw e;
        } catch (AuthenticationException e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Bank account could not be deactivated.", e);
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Error occurred while deleting the bank account.", e);
        }
    }

    @Override
    public BankAccountDto getBankAccount(String accountNumber) {
        try {
            var bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);

            if (bankAccount == null) {
                throw new ServiceException("Bank account not found for account number: " + accountNumber);
            }

            logger.info("Retrieved bank account " + bankAccount.getMaskedAccountNumber() + " from " + userContext.getUsername());
            return BankAccountMapper.INSTANCE.domainToDto(bankAccount);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error occurred while retrieving the bank account.", e);
        }
    }
}
