package com.nure.ua.serverSide.service;

import com.nure.ua.serverSide.repository.ConnectionPool;
import com.nure.ua.entity.Entity;

public abstract class ServiceAbstract<T extends Entity>{
    private final ConnectionPool pool;
    protected ServiceAbstract(ConnectionPool pool) {
        this.pool = pool;
    }
    protected ConnectionPool getPool() {
        return pool;
    }
}
