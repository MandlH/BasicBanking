package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.UnitOfWork;

@ApplicationScoped
@Default
public class RepositoryWrapper implements org.mandl.repositories.RepositoryWrapper {

    private final UnitOfWork unitOfWork;
    private final IdentityUserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @Inject
    public RepositoryWrapper(
            UnitOfWork unitOfWork,
            IdentityUserRepository userRepository,
            BankAccountRepository bankAccountRepository) {
        this.unitOfWork = unitOfWork;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void beginTransaction() {
        try {
            unitOfWork.beginTransaction();
        } catch (Exception e) {
            throw new RuntimeException("Transaction failed", e);
        }
    }

    public void commitTransaction() {
        try {
            unitOfWork.commit();
        } catch (Exception e) {
            throw new RuntimeException("Transaction commit failed.", e);
        }
    }

    public void rollbackTransaction() {
        unitOfWork.rollback();
    }

    @Override
    public IdentityUserRepository getIdentityUserRepository() {
        return userRepository;
    }

    @Override
    public BankAccountRepository getBankAccountRepository() {
        return bankAccountRepository;
    }
}
