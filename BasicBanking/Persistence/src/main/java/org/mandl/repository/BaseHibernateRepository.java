package org.mandl.repository;

import org.hibernate.Session;
import org.mandl.LoggingHandler;
import org.mandl.repositories.BaseRepository;

import java.util.UUID;

public abstract class BaseHibernateRepository<T>
        implements BaseRepository<T> {

    protected final Session session;
    private final Class<T> entityClass;
    protected final LoggingHandler logger = LoggingHandler.getLogger(BaseRepository.class);

    protected BaseHibernateRepository(Session session, Class<T> entityClass) {
        this.session = session;
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        session.persist(entity);
    }

    public void update(T entity) {
        session.merge(entity);
    }

    public void delete(T entity) {
        session.remove(entity);
    }

    public T findById(UUID id) {
        return session.find(entityClass, id);
    }
}
