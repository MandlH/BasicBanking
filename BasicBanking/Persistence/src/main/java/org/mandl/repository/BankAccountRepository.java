package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.mandl.entities.BankAccount;
import org.mandl.identity.IdentityUser;
import org.mandl.unitOfWork.UnitOfWork;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class BankAccountRepository extends BaseRepository<BankAccount> implements org.mandl.repositories.BankAccountRepository {

    private final Session session;

    @Inject
    public BankAccountRepository(UnitOfWork unitOfWork) {
        super(unitOfWork.getSession(), BankAccount.class);
        this.session = unitOfWork.getSession();
    }

    public List<BankAccount> getAllBankAccountsByOwnerId(UUID ownerId) {
        try {
            String hql = "FROM BankAccount WHERE owner.id = :ownerId";
            return session
                    .createQuery(hql, BankAccount.class)
                    .setParameter("ownerId", ownerId)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch all bank accounts", e);
        }
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        IdentityUser identityUser = session.get(IdentityUser.class, bankAccount.getOwner().getId());
        if (identityUser == null) {
            throw new IllegalStateException("Owner not found in the session.");
        }
        bankAccount.setOwner(identityUser);
        save(bankAccount);
        return findById(bankAccount.getId());
    }
}
