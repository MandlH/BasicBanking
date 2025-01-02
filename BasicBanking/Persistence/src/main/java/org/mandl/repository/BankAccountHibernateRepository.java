package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.mandl.database.Context;
import org.mandl.entities.BankAccount;
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
        var bankAccounts = session
                .createQuery(hql, BankAccount.class)
                .setParameter("ownerId", ownerId)
                .getResultList();
        bankAccounts.forEach(this::VerifyEntityOwnerShip);
        return bankAccounts;
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
        VerifyEntityOwnerShip(bankaccount);
        delete(bankaccount);
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        var bankAccount = session.createQuery(
                        "FROM BankAccount b JOIN FETCH b.owner WHERE b.accountNumber = :accountNumber AND b.owner.id =: ownerId", BankAccount.class)
                .setParameter("accountNumber", accountNumber)
                .setParameter("ownerId", context.getUserId())
                .uniqueResult();
        VerifyEntityOwnerShip(bankAccount);
        return bankAccount;
    }

    @Override
    protected void VerifyEntityOwnerShip(BankAccount entity) {
        if (entity == null) {
            throw new IllegalStateException("Entity is null");
        }

        boolean isOwner = session.createQuery(
                        """
                        SELECT COUNT(b) > 0
                        FROM BankAccount b
                        WHERE b.id = :bankAccountId
                        AND b.owner.id = :userId
                        """,
                        Boolean.class)
                .setParameter("bankAccountId", entity.getId())
                .setParameter("userId", context.getUserId())
                .uniqueResult();

        if (!isOwner) {
            throw new IllegalStateException("User is not allowed to access this bank account");
        }
    }
}
