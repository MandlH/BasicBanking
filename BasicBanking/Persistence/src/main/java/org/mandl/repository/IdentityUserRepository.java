package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.mandl.entities.BankAccount;
import org.mandl.identity.IdentityUser;
import org.mandl.unitOfWork.UnitOfWork;

@ApplicationScoped
public class IdentityUserRepository extends BaseRepository<IdentityUser> implements org.mandl.repositories.IdentityUserRepository {

    private final Session session;

    @Inject
    public IdentityUserRepository(UnitOfWork unitOfWork) {
        super(unitOfWork.getSession(), IdentityUser.class);
        this.session = unitOfWork.getSession();
    }

    public IdentityUser findByUsername(String username) {
        try {
            return session.createQuery(
                "FROM IdentityUser WHERE username = :username", IdentityUser.class)
                .setParameter("username", username)
                .uniqueResult();
        } catch (Exception e) {
            return null;
        }
    }
}
