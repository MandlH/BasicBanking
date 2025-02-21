package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mandl.entities.Transaction;
import org.mandl.entities.TransactionType;
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
    public List<Transaction> getUserTransactions(TransactionType type, UUID bankAccountId) {
        return session.createQuery(
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
    }

    @Override
    public List<Transaction> getUserTransactions(UUID bankAccountId) {
        return session.createQuery(
                        """
                        SELECT t
                        FROM Transaction t
                        LEFT JOIN FETCH t.bankAccountFrom baf
                        LEFT JOIN FETCH t.bankAccountTo bat
                        WHERE
                            (baf.id = :bankAccountId) AND (baf.owner.id = :ownerId)
                            OR
                            (bat.id = :bankAccountId) AND (bat.owner.id = :ownerId)
                       """,
                        Transaction.class)
                .setParameter("bankAccountId", bankAccountId)
                .setParameter("ownerId", context.getUserId())
                .getResultList();
    }

    @Override
    public void update(Transaction entity) {
        throw new UnsupportedOperationException("Transactions can not be updated");
    }
}

