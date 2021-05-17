package com.nure.ua.a_serverSide.service;

import com.nure.ua.a_serverSide.model.ConnectionPool;
import com.nure.ua.a_serverSide.model.entity.Entity;

public abstract class ServiceAbstract<T extends Entity>{
    private final ConnectionPool pool;
    protected ServiceAbstract(ConnectionPool pool) {
        this.pool = pool;
    }
    protected ConnectionPool getPool() {
        return pool;
    }
}
