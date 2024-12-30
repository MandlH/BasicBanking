package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mandl.identity.IdentityUser;

@ApplicationScoped
public class IdentityUserRepository extends BaseRepository implements org.mandl.repositories.IdentityUserRepository {

    @Inject
    public IdentityUserRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public IdentityUser findByUsername(String username) {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM IdentityUser WHERE username = :username", IdentityUser.class)
                .setParameter("username", username)
                .uniqueResult();
        }
    }

    public void close() {
        getSessionFactory().close();
    }
}
