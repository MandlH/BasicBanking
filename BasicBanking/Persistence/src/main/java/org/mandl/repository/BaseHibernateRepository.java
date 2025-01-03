package org.mandl.repository;

import jakarta.inject.Inject;
import org.h2.engine.User;
import org.hibernate.Session;
import org.mandl.LoggingHandler;
import org.mandl.database.Context;
import org.mandl.entities.BaseEntity;
import org.mandl.repositories.BaseRepository;

import java.util.UUID;

public abstract class BaseHibernateRepository<T extends BaseEntity>
        implements BaseRepository<T> {

    protected final Session session;
    private final Class<T> entityClass;
    protected final LoggingHandler logger = LoggingHandler.getLogger(BaseRepository.class);

    protected abstract void VerifyEntityOwnerShip(T entity);

    @Inject
    protected Context context;

    protected BaseHibernateRepository(Session session, Class<T> entityClass) {
        this.session = session;
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        if (entity.getId() != null) {
            VerifyEntityOwnerShip(entity);
        }
        session.persist(entity);
    }

    public void update(T entity) {
        VerifyEntityOwnerShip(entity);
        session.merge(entity);
    }

    public void delete(T entity) {
        VerifyEntityOwnerShip(entity);
        session.remove(entity);
    }

    public T findById(UUID id) {
        var entity = session.get(entityClass, id);
        VerifyEntityOwnerShip(entity);
        return entity;
    }
}
