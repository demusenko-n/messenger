package com.nure.ua.model;

import com.nure.ua.exception.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

public final class ConnectionPool {

    public static class ConnectionPoolElem implements AutoCloseable {
        private final Connection connection;
        private boolean isFree;

        public void disableAutoCommit() throws ConnectionException {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException ex) {
                throw new ConnectionException(ex.getMessage());
            }
        }

        public void commit() throws ConnectionException {
            try {
                connection.commit();
            } catch (SQLException ex) {
                throw new ConnectionException(ex.getMessage());
            }
        }

        public void rollback() throws ConnectionException {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new ConnectionException(ex.getMessage());
            }
        }

        public Connection getConnection() {
            return connection;
        }

        public ConnectionPoolElem(Connection connection) {
            this.connection = connection;
            isFree = true;
        }

        @Override
        public void close() throws ConnectionException {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new ConnectionException(ex.getMessage());
            }
            isFree = true;
        }

        private PreparedStatement prepareStatement(String sqlCommand, Object[] args) throws SQLException {
            var stmt = connection.prepareStatement(sqlCommand);
            System.out.print("(");
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null) {
                    System.out.print("null,");
                    stmt.setNull(i + 1, Types.NULL);
                } else if (args[i] instanceof LocalDateTime) {
                    stmt.setTimestamp(i+1, Timestamp.valueOf((LocalDateTime)args[i]));
                } else {

                    System.out.print(args[i].toString() + ",");
                    stmt.setString(i + 1, args[i].toString());
                }
            }
            System.out.println(")");
            return stmt;
        }

        public ResultSet executeSql(String sqlCommand, Object... args) throws ConnectionException {
            try {
                var stmt = prepareStatement(sqlCommand, args);
                return stmt.executeQuery();
            } catch (SQLException ex) {
                throw new ConnectionException(ex.getMessage());
            }
        }

        public void executeDml(String sqlCommand, Object... args) throws ConnectionException {
            try {
                var stmt = prepareStatement(sqlCommand, args);
                stmt.executeUpdate();
            } catch (SQLException ex) {
                throw new ConnectionException(ex.getMessage());
            }
        }
    }

    private final ConnectionPoolElem[] pool;


    public ConnectionPool(String url, String user, String password, int num) throws SQLException {
        pool = new ConnectionPoolElem[num];
        for (int i = 0; i < num; i++) {
            pool[i] = new ConnectionPoolElem(DriverManager.getConnection(url, user, password));
        }
    }

    public ConnectionPoolElem getFreeConnection() {
        while (true) {
            for (ConnectionPoolElem connectionPoolElem : pool) {
                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (connectionPoolElem) {
                    if (connectionPoolElem.isFree) {
                        connectionPoolElem.isFree = false;
                        return connectionPoolElem;
                    }
                }
            }
        }
    }
}
