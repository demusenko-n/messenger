package com.nure.ua.service;

import com.nure.ua.model.entity.User;
import com.nure.ua.model.exception.ServiceException;

public interface UserService {
    User getUserByLoginPassword(String login, String password) throws ServiceException;

    User getUserByLogin(String login) throws ServiceException;

    void createUser(User user) throws ServiceException;

    void removeUser(User user) throws ServiceException;

    void updateUser(User user) throws ServiceException;
}
