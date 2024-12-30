package org.mandl.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mandl.identity.IdentityUser;

import java.util.List;
import java.util.UUID;

public abstract class BaseRepository {

    private final SessionFactory sessionFactory;

    protected BaseRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(IdentityUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    public void update(IdentityUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    public void delete(IdentityUser user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        }
    }

    public IdentityUser findById(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(IdentityUser.class, id);
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
