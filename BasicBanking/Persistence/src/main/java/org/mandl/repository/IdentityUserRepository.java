package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mandl.identity.IdentityUser;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class IdentityUserRepository implements org.mandl.repositories.IdentityUserRepository {

    private final SessionFactory sessionFactory;

    @Inject
    public IdentityUserRepository(SessionFactory sessionFactory) {
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

    public List<IdentityUser> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM IdentityUser", IdentityUser.class).list();
        }
    }

    public IdentityUser findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM IdentityUser WHERE username = :username", IdentityUser.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
    }

    public void close() {
        sessionFactory.close();
    }
}
