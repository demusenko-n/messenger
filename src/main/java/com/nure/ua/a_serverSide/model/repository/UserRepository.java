package com.nure.ua.a_serverSide.model.repository;

import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.a_serverSide.exception.RepositoryException;

import java.sql.SQLException;


public interface UserRepository extends Repository<User>{
    User getByLogin(String login) throws SQLException, RepositoryException;
}
