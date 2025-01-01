package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.repositories.RepositoryWrapper;
import org.mandl.repositories.UnitOfWork;

@ApplicationScoped
@Default
public class HibernateRepositoryWrapper
        implements RepositoryWrapper {

    private final UnitOfWork unitOfWork;
    private final IdentityUserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @Inject
    public HibernateRepositoryWrapper(
            UnitOfWork unitOfWork,
            IdentityUserRepository userRepository,
            BankAccountRepository bankAccountRepository) {
        this.unitOfWork = unitOfWork;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void beginTransaction() {
        unitOfWork.beginTransaction();
    }

    public void commitTransaction() {
        unitOfWork.commit();
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
