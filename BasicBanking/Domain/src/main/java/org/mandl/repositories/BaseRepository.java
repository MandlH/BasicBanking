package org.mandl.repositories;

import java.util.UUID;

public interface BaseRepository<T> {
    void save(T entity);
    void delete(T entity);
    void update(T entity);
    T findById(UUID id);
    T merge(T entity);
}
