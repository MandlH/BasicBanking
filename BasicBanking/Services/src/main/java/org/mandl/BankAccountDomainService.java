package org.mandl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.entities.BankAccount;
import org.mandl.mapper.BankAccountMapper;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.repositories.RepositoryWrapper;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
final class BankAccountDomainService implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final RepositoryWrapper repositoryWrapper;

    @Inject
    public BankAccountDomainService(RepositoryWrapper repositoryWrapper) {
        this.repositoryWrapper = repositoryWrapper;
        this.bankAccountRepository = repositoryWrapper.getBankAccountRepository();
    }

    @Override
    public List<BankAccountDto> getAllBankAccountsByOwnerId(UUID ownerId) {
        List<BankAccount> bankAccounts = bankAccountRepository.getAllBankAccountsByOwnerId(ownerId);
        return bankAccounts.stream()
                .map(BankAccountMapper.INSTANCE::domainToDto).toList();
    }

    @Override
    public BankAccountDto createBankAccount(BankAccountDto bankAccountDto) {
        repositoryWrapper.beginTransaction();
        BankAccount bankAccount = BankAccountMapper.INSTANCE.dtoToDomain(bankAccountDto);
        bankAccountRepository.createBankAccount(bankAccount);
        repositoryWrapper.commitTransaction();
        return BankAccountMapper.INSTANCE.domainToDto(bankAccountRepository.findById(bankAccount.getId()));
    }
}
