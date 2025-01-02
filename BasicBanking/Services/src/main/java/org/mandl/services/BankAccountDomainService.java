package org.mandl.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.service.spi.ServiceException;
import org.mandl.BankAccountDto;
import org.mandl.BankAccountService;
import org.mandl.LoggingHandler;
import org.mandl.entities.BankAccount;
import org.mandl.mapper.BankAccountMapper;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
final class BankAccountDomainService
        implements BankAccountService {
    private static final LoggingHandler logger = LoggingHandler.getLogger(BankAccountDomainService.class);
    private final BankAccountRepository bankAccountRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public BankAccountDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.bankAccountRepository = repositoryWrapper.getBankAccountRepository();
    }

    @Override
    public List<BankAccountDto> getAllBankAccountsByOwnerId(UUID ownerId) {
        try {
            List<BankAccount> bankAccounts = bankAccountRepository.getAllBankAccountsByOwnerId(ownerId);
            return bankAccounts.stream()
                    .map(BankAccountMapper.INSTANCE::domainToDto)
                    .toList();
        } catch (Exception e) {
            logger.error("Failed to fetch bank accounts for owner ID: " + ownerId, e);
            throw new ServiceException("Error retrieving bank accounts", e);
        }
    }

    @Override
    public BankAccountDto createBankAccount(BankAccountDto bankAccountDto) {
        try {
            repositoryWrapper.beginTransaction();
            var bankAccount = BankAccountMapper.INSTANCE.dtoToDomain(bankAccountDto);
            bankAccountRepository.createBankAccount(bankAccount);
            repositoryWrapper.commitTransaction();
            return BankAccountMapper.INSTANCE.domainToDto(bankAccount);
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            logger.error("Failed to create bank account for owner ID: " + bankAccountDto.getOwner().getId(), e);
            throw new ServiceException("Error creating bank account", e);
        }
    }

    @Override
    public void deleteBankAccount(String accountNumber) {
        try {
            repositoryWrapper.beginTransaction();
            bankAccountRepository.deleteBankAccount(accountNumber);
            repositoryWrapper.commitTransaction();
        } catch (Exception e) {
            repositoryWrapper.rollbackTransaction();
            throw new ServiceException("Error deleting bank account", e);
        }
    }

    @Override
    public BankAccountDto getBankAccount(UUID userId, String accountNumber) {
        try {
            var bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);

            if (bankAccount == null) {
                throw new ServiceException("Bank account not found for account number: " + accountNumber);
            }

            return BankAccountMapper.INSTANCE.domainToDto(bankAccount);
        } catch (Exception e) {
            throw new ServiceException("Error checking if bank account exists", e);
        }
    }
}
