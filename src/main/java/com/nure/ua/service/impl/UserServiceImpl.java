package com.nure.ua.service.impl;

import com.nure.ua.model.ConnectionPool;
import com.nure.ua.model.entity.User;
import com.nure.ua.exception.ConnectionException;
import com.nure.ua.exception.RepositoryException;
import com.nure.ua.exception.ServiceException;
import com.nure.ua.model.repository.impl.UserRepositoryImpl;
import com.nure.ua.service.ServiceAbstract;
import com.nure.ua.service.UserService;

import java.util.List;

public class UserServiceImpl extends ServiceAbstract<User> implements UserService {
    public UserServiceImpl(ConnectionPool pool) {
        super(pool);
    }

    @Override
    public void createUser(User entity) throws ServiceException {
        if (entity == null) {
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            new UserRepositoryImpl(con).create(entity);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public void updateUser(User entity) throws ServiceException {
        if (entity == null) {
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            new UserRepositoryImpl(con).update(entity);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public void removeUser(User entity) throws ServiceException {
        if(entity == null){
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            new UserRepositoryImpl(con).delete(entity);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    public List<User> getAll() throws ServiceException {
        try (var con = getPool().getFreeConnection()) {
            return new UserRepositoryImpl(con).getAll();
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    public User getById(int id) throws ServiceException {
        try (var con = getPool().getFreeConnection()) {
            return new UserRepositoryImpl(con).getById(id);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public User getUserByLoginPassword(String login, String password) throws ServiceException {
        try (var con = getPool().getFreeConnection()) {
            UserRepositoryImpl rep = new UserRepositoryImpl(con);
            User userWithLogin = rep.getByLogin(login);
            if (userWithLogin != null && userWithLogin.getPassword().equals(password)) {
                return userWithLogin;
            }
            return null;
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public User getUserByLogin(String login) throws ServiceException {
        try (var con = getPool().getFreeConnection()) {
            return new UserRepositoryImpl(con).getByLogin(login);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public User getUserById(int id) throws ServiceException {
        try (var con = getPool().getFreeConnection()) {
            return new UserRepositoryImpl(con).getById(id);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}
