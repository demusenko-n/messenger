package com.nure.ua.a_serverSide.model.repository;

import com.nure.ua.a_serverSide.model.entity.Entity;
import com.nure.ua.a_serverSide.exception.RepositoryException;

import java.util.List;

public interface Repository<T extends Entity> {
    List<T> getAll() throws RepositoryException;
    T getById(int id) throws RepositoryException;

    void create(T item) throws RepositoryException;
    void delete(T item) throws RepositoryException;
    void update(T item) throws RepositoryException;
}
