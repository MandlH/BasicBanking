package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.service.spi.ServiceException;
import org.mandl.entities.BankAccount;
import org.mandl.identity.IdentityUser;
import org.mandl.repositories.BankAccountRepository;
import org.mandl.unitOfWork.UnitOfWork;

import java.util.List;

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

    public List<BankAccount> getAllBankAccountsFromOwner() {
        String hql = "FROM BankAccount b WHERE b.owner.id = :ownerId AND b.isDeleted = false";
        var ownerId = context.getUserId();
        var bankAccounts = session
                .createQuery(hql, BankAccount.class)
                .setParameter("ownerId", ownerId)
                .getResultList();

        return bankAccounts;
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        IdentityUser identityUser = session.get(IdentityUser.class, bankAccount.getOwner().getId());
        bankAccount.setOwner(identityUser);
        save(bankAccount);
        return findById(bankAccount.getId());
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        var bankAccount = session.createQuery(
                        "FROM BankAccount b WHERE b.accountNumber = :accountNumber", BankAccount.class)
                .setParameter("accountNumber", accountNumber)
                .uniqueResult();

        return bankAccount;
    }
}
