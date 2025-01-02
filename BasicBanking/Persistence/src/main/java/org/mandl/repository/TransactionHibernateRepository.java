package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.database.Context;
import org.mandl.entities.BankAccountType;
import org.mandl.entities.Transaction;
import org.mandl.entities.TransactionType;
import org.mandl.identity.IdentityUser;
import org.mandl.repositories.TransactionRepository;
import org.mandl.unitOfWork.UnitOfWork;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TransactionHibernateRepository
    extends BaseHibernateRepository<Transaction>
    implements TransactionRepository {

    @Inject
    protected TransactionHibernateRepository(UnitOfWork unitOfWork) {
        super(unitOfWork.getSession(), Transaction.class);
    }

    @Override
    public List<Transaction> getTransactions(TransactionType type, UUID bankAccountId) {
        var transactions = session.createQuery(
                        """
                        SELECT t
                        FROM Transaction t
                        JOIN FETCH t.bankAccountFrom baf
                        LEFT JOIN FETCH t.bankAccountTo bat
                        WHERE
                            (baf.id = :bankAccountId AND t.transactionType = :type)
                            OR
                            (bat.id = :bankAccountId AND t.transactionType = :type)
                        """,
                        Transaction.class)
                .setParameter("bankAccountId", bankAccountId)
                .setParameter("type", type)
                .getResultList();
        transactions.forEach(this::VerifyEntityOwnerShip);
        return transactions;
    }

    @Override
    public List<Transaction> getTransactions(UUID bankAccountId) {
        var transactions = session.createQuery(
                        """
                        SELECT t
                        FROM Transaction t
                        JOIN FETCH t.bankAccountFrom baf
                        LEFT JOIN FETCH t.bankAccountTo bat
                        WHERE
                            (baf.id = :bankAccountId)
                            OR
                            (bat.id = :bankAccountId)
                       """,
                        Transaction.class)
                .setParameter("bankAccountId", bankAccountId)
                .getResultList();
        transactions.forEach(this::VerifyEntityOwnerShip);
        return transactions;
    }

    @Override
    protected void VerifyEntityOwnerShip(Transaction entity) {
        if (entity == null) {
            throw new IllegalStateException("Entity is null");
        }

        boolean isOwner = session.createQuery(
                        """
                        SELECT COUNT(t) > 0
                        FROM Transaction t
                        JOIN t.bankAccountFrom baf
                        LEFT JOIN t.bankAccountTo bat
                        WHERE 
                            t.id = :transactionId
                            AND (
                                baf.owner.id = :userId
                                OR bat.owner.id = :userId
                            )
                        """,
                        Boolean.class)
                .setParameter("transactionId", entity.getId())
                .setParameter("userId", context.getUserId())
                .uniqueResult();

        if (!isOwner) {
            throw new IllegalStateException("User is not allowed to access this entity");
        }
    }
}

