package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.mandl.entities.BankAccount;
import org.mandl.entities.BankAccountType;
import org.mandl.identity.IdentityUser;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.unitOfWork.UnitOfWork;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BankAccountHibernateRepository
        extends BaseHibernateRepository<BankAccount>
        implements BankAccountRepository {

    private final Session session;

    @Inject
    public BankAccountHibernateRepository(UnitOfWork unitOfWork) {
        super(unitOfWork.getSession(), BankAccount.class);
        this.session = unitOfWork.getSession();
    }

    public List<BankAccount> getAllBankAccountsByOwnerId(UUID ownerId) {
        String hql = "FROM BankAccount WHERE owner.id = :ownerId";
        return session
                .createQuery(hql, BankAccount.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        IdentityUser identityUser = session.get(IdentityUser.class, bankAccount.getOwner().getId());
        if (identityUser == null) {
            throw new IllegalStateException("Owner not found for ID: " + bankAccount.getOwner().getId());
        }
        bankAccount.setOwner(identityUser);
        save(bankAccount);
        return findById(bankAccount.getId());
    }

    @Override
    public void deleteBankAccount(String accountNumber) {
        var bankaccount = findByAccountNumber(accountNumber);
        if (bankaccount == null) {
            throw new IllegalStateException("Account not found for ID: " + accountNumber);
        }
        delete(bankaccount);
    }

    private BankAccount findByAccountNumber(String accountNumber) {
        return session.createQuery(
                        "FROM BankAccount WHERE accountNumber = :accountNumber", BankAccount.class)
                .setParameter("accountNumber", accountNumber)
                .uniqueResult();
    }
}
