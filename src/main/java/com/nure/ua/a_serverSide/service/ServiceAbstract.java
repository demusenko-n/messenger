package com.nure.ua.service;

import com.nure.ua.model.ConnectionPool;
import com.nure.ua.model.entity.Entity;

public abstract class ServiceAbstract<T extends Entity>{
    private final ConnectionPool pool;
    protected ServiceAbstract(ConnectionPool pool) {
        this.pool = pool;
    }
    protected ConnectionPool getPool() {
        return pool;
    }
}
