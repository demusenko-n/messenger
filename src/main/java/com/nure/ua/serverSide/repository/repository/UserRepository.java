package com.nure.ua.serverSide.repository.repository;

import com.nure.ua.entity.User;
import com.nure.ua.serverSide.exception.RepositoryException;

import java.sql.SQLException;


public interface UserRepository extends Repository<User>{
    User getByLogin(String login) throws SQLException, RepositoryException;
}
