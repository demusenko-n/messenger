package com.nure.ua.serverSide.service;

import com.nure.ua.entity.User;
import com.nure.ua.serverSide.exception.ServiceException;

public interface UserService {
    User getUserByLoginPassword(String login, String password) throws ServiceException;

    User getUserByLogin(String login) throws ServiceException;

    User getUserById(int id) throws ServiceException;

    void createUser(User user) throws ServiceException;

    void removeUser(User user) throws ServiceException;

    void updateUser(User user) throws ServiceException;
}
