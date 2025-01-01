package org.mandl.repositories;

public interface UnitOfWork {
    void beginTransaction();
    void commit();
    void rollback();
    void close();
    void flush();
}
