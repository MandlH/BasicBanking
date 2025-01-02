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
        var identityUser = session.createQuery(
                        "FROM IdentityUser WHERE username = :username", IdentityUser.class)
                .setParameter("username", username)
                .uniqueResult();
        return identityUser;
    }

    @Override
    public void update(IdentityUser entity) {
        throw new UnsupportedOperationException("Transactions can not be updated");
    }

    @Override
    protected void VerifyEntityOwnerShip(IdentityUser entity) {
        if (entity == null) {
            throw new IllegalStateException("Entity is null");
        }

        boolean isOwner = session.createQuery(
                        """
                        SELECT COUNT(i) > 0
                        FROM IdentityUser i
                        WHERE i.id = :identityUserId
                        AND i.id = :userId
                        """,
                        Boolean.class)
                .setParameter("identityUserId", entity.getId())
                .setParameter("userId", context.getUserId())
                .uniqueResult();

        if (!isOwner) {
            throw new IllegalStateException("User is not allowed to access this identity");
        }
    }
}
