package org.mandl.core.persistence.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mandl.core.domain.identity.IdentityUser;
import org.mandl.core.persistence.database.DatabaseConnection;

import java.util.List;
import java.util.UUID;

public class IdentityUserRepository {

    private final SessionFactory sessionFactory = DatabaseConnection.getSessionFactory();

    public void save(IdentityUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    public IdentityUser findById(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(IdentityUser.class, id);
        }
    }

    public List<IdentityUser> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM org.mandl.core.domain.identity.IdentityUser", IdentityUser.class).list();
        }
    }

    public void update(IdentityUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user); // Use merge() for updates
            session.getTransaction().commit();
        }
    }

    public void delete(IdentityUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(user); // Use remove() for deletions
            session.getTransaction().commit();
        }
    }

    public void close() {
        sessionFactory.close();
    }
}
