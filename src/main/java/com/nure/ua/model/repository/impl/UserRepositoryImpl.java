package com.nure.ua.model.repository.impl;

import com.nure.ua.model.ConnectionPool;
import com.nure.ua.model.entity.User;
import com.nure.ua.model.exception.ConnectionException;
import com.nure.ua.model.exception.RepositoryException;
import com.nure.ua.model.repository.RepositoryAbstract;
import com.nure.ua.model.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserRepositoryImpl extends RepositoryAbstract<User> implements UserRepository {
    private static final String INSERT_INTO = "INSERT INTO USERS(LOGIN, PASSWORD, NAME) VALUES (?,?,?)";
    private static final String DELETE_BY_ID = "DELETE FROM USERS WHERE ID_USER=?";
    private static final String UPDATE = "UPDATE USERS SET LOGIN=?,PASSWORD=?, NAME=? WHERE ID_USER=?";
    private static final String SELECT_BY_LOGIN = "SELECT * FROM USERS WHERE LOGIN=?";
    private static final String SELECT_ALL = "SELECT * FROM USERS";
    private static final String SELECT_BY_ID = "SELECT * FROM USERS WHERE ID_USER=?";

    public UserRepositoryImpl(ConnectionPool.ConnectionPoolElem con) {
        super(con);
    }

    @Override
    protected User readEntityFromSet(ResultSet set) throws SQLException {
        return new User(set.getInt("ID_USER"), set.getString("NAME"),
                set.getString("LOGIN"), set.getString("PASSWORD"));
    }

    @Override
    public List<User> getAll() throws RepositoryException {
        try (var set = con.executeSql(SELECT_ALL)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public User getById(int id) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_ID, id)) {
            return readEntityFromSetOrNull(set);
        } catch (SQLException | ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public void create(User item) throws RepositoryException {
        try {
            con.executeDml(INSERT_INTO, item.getLogin(), item.getName(), item.getPassword());
        } catch (ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public void delete(User item) throws RepositoryException {
        try {
            con.executeDml(DELETE_BY_ID, item.getId());
        } catch (ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public void update(User item) throws RepositoryException {
        try {
            con.executeDml(UPDATE, item.getLogin(), item.getPassword(), item.getName(), item.getId());
        } catch (ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public User getByLogin(String login) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_LOGIN, login)) {
            return readEntityFromSetOrNull(set);
        } catch (SQLException | ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }
}
