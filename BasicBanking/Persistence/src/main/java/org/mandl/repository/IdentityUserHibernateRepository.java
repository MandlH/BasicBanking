package org.mandl.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.mandl.database.Context;
import org.mandl.identity.IdentityUser;
import org.mandl.repositories.IdentityUserRepository;
import org.mandl.unitOfWork.UnitOfWork;

@ApplicationScoped
public class IdentityUserHibernateRepository
        extends BaseHibernateRepository<IdentityUser>
        implements IdentityUserRepository {

    private final Session session;

    @Inject
    public IdentityUserHibernateRepository(UnitOfWork unitOfWork) {
        super(unitOfWork.getSession(), IdentityUser.class);
        this.session = unitOfWork.getSession();
    }

    public IdentityUser getLoginUser(String username) {
        return session.createQuery(
                        "FROM IdentityUser WHERE username = :username", IdentityUser.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    @Override
    public void update(IdentityUser entity) {
        throw new UnsupportedOperationException("Transactions can not be updated");
    }
}
