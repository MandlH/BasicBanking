package org.mandl.repository;

import org.hibernate.Session;
import java.util.UUID;

public abstract class BaseRepository<T> implements org.mandl.repositories.BaseRepository<T> {
    protected final Session session;
    private final Class<T> entityClass;

    protected BaseRepository(Session session, Class<T> entityClass) {
        this.session = session;
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        try {
            session.persist(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save entity.", e);
        }
    }

    public void update(T entity) {
        try {
            session.merge(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update entity.", e);
        }
    }

    public void delete(T entity) {
        try {
            session.remove(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete entity.", e);
        }
    }

    public T findById(UUID id) {
        try {
            return session.find(entityClass, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find entity by ID.", e);
        }
    }
}
