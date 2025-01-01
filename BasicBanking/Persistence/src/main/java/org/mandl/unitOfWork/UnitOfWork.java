package org.mandl.unitOfWork;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@ApplicationScoped
public class UnitOfWork implements org.mandl.repositories.UnitOfWork {
    private final Session session;
    private Transaction transaction;

    @Inject
    public UnitOfWork(SessionFactory sessionFactory) {
        this.session = sessionFactory.openSession();
    }

    public Session getSession() {
        return session;
    }

    @Override
    public void beginTransaction() {
        transaction = session.beginTransaction();
    }

    @Override
    public void commit() {
        try {
            if (transaction != null && transaction.isActive()) {
                session.flush();
                transaction.commit();
            }
        } catch (Exception e) {
            rollback();
            throw new RuntimeException("Error committing transaction.", e);
        }
    }

    @Override
    public void rollback() {
        try {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error rolling back transaction.", e);
        }
    }

    @Override
    public void close() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    @Override
    public void flush() {
        try {
            session.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error flushing session.", e);
        }
    }
}
