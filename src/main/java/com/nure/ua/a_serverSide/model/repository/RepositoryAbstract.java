package com.nure.ua.model.repository;

import com.nure.ua.model.ConnectionPool;
import com.nure.ua.model.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class RepositoryAbstract<T extends Entity> implements Repository<T> {
    protected final ConnectionPool.ConnectionPoolElem con;

    protected RepositoryAbstract(ConnectionPool.ConnectionPoolElem con) {
        this.con = con;
    }

    protected abstract T readEntityFromSet(ResultSet set) throws SQLException;

    protected List<T> readListFromSet(ResultSet set) throws SQLException {
        List<T> entities = new ArrayList<>();
        while (set.next()) {
            entities.add(readEntityFromSet(set));
        }
        return entities;
    }

    protected T readEntityFromSetOrNull(ResultSet set) throws SQLException {
        return set.next() ? readEntityFromSet(set) : null;
    }
}
