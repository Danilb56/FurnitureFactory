package com.example.furniture_factory.services;

import java.sql.Connection;
import java.util.List;

public abstract class Service<E> {
    protected final Connection connection;

    public Service(Connection connection) {
        this.connection = connection;
    }

    public abstract List<E> findAll();

    public abstract E findById(Long id);

    public abstract E update(E entity);

    public abstract void deleteById(Long id);

    public abstract E create(E e);
}
