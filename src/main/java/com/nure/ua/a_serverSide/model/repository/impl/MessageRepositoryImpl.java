package com.nure.ua.a_serverSide.model.repository.impl;

import com.nure.ua.a_serverSide.model.ConnectionPool;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.a_serverSide.exception.ConnectionException;
import com.nure.ua.a_serverSide.exception.RepositoryException;
import com.nure.ua.a_serverSide.model.repository.MessageRepository;
import com.nure.ua.a_serverSide.model.repository.RepositoryAbstract;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageRepositoryImpl extends RepositoryAbstract<Message> implements MessageRepository {

    private static final String SELECT_ALL = "SELECT m.ID_MESSAGE as M_ID_MESSAGE," +
            "m.QUOTE_MESSAGE_ID as M_QUOTE_MESSAGE_ID," +
            "m.CONTENT as M_CONTENT," +
            "s.ID_USER as S_ID_USER," +
            "s.LOGIN as S_LOGIN," +
            "s.PASSWORD as S_PASSWORD," +
            "s.NAME as S_NAME," +
            "r.ID_USER as R_ID_USER," +
            "r.LOGIN as R_LOGIN," +
            "r.PASSWORD as R_PASSWORD," +
            "r.NAME as R_NAME," +
            "m.STATUS_UNREAD as M_STATUS_UNREAD," +
            "m.STATUS_EDITED as M_STATUS_EDITED," +
            "m.TIME as M_TIME " +
            "FROM MESSAGES m " +
            "INNER JOIN USERS s ON(m.SENDER_ID=s.ID_USER) " +
            "INNER JOIN USERS r ON(m.RECEIVER_ID=r.ID_USER) ";
    private static final String SELECT_BY_ID = SELECT_ALL +
            "WHERE m.ID_MESSAGE=?";
    private static final String SELECT_BY_RECEIVER_ID = SELECT_ALL +
            "WHERE m.RECEIVER_ID=?";
    private static final String SELECT_BY_USER_ID = SELECT_ALL +
            "WHERE m.RECEIVER_ID=? OR m.SENDER_ID=?";
    private static final String SELECT_BY_SENDER_ID_AND_RECEIVER_ID = SELECT_ALL +
            "WHERE m.SENDER_ID=? AND m.RECEIVER_ID=? OR m.SENDER_ID=? AND m.RECEIVER_ID=?";
    private static final String SELECT_BY_CONTENT = SELECT_ALL +
            "WHERE UPPER(m.CONTENT)=UPPER(?)";
    private static final String SELECT_THAT_CONTAINS = SELECT_ALL +
            "WHERE UPPER(m.CONTENT) LIKE '%'||UPPER(?)||'%'";
    private static final String DELETE_BY_ID = "DELETE FROM MESSAGES WHERE ID_MESSAGE=?";
    private static final String UPDATE_REMOVE_ALL_QUOTES = "UPDATE MESSAGES SET QUOTE_MESSAGE_ID = NULL " +
            "WHERE QUOTE_MESSAGE_ID=?";
    private static final String UPDATE = "UPDATE MESSAGES SET CONTENT=?,STATUS_UNREAD=?,STATUS_EDITED=? " +
            "WHERE ID_MESSAGE=?";
    private static final String INSERT_INTO = "INSERT INTO " +
            "MESSAGES(QUOTE_MESSAGE_ID,CONTENT,SENDER_ID,RECEIVER_ID,STATUS_UNREAD,STATUS_EDITED,TIME) " +
            "VALUES (?,?,?,?,?,?,?)";

    public MessageRepositoryImpl(ConnectionPool.ConnectionPoolElem con) {
        super(con);
    }

    @Override
    public List<Message> getBySenderId(int id) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_USER_ID, id)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Message> getByReceiverId(int id) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_RECEIVER_ID, id)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Message> getBetweenTwoUsers(int idFirst, int idSecond) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_SENDER_ID_AND_RECEIVER_ID, idFirst, idSecond, idSecond, idFirst)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Message> getAllMessagesByUser(int idUser) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_USER_ID, idUser, idUser)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Message> getByContent(String content) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_CONTENT, content)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Message> getThatContainsContent(String content) throws RepositoryException {
        try (var set = con.executeSql(SELECT_THAT_CONTAINS, content)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public List<Message> getAll() throws RepositoryException {
        try (var set = con.executeSql(SELECT_ALL)) {
            return readListFromSet(set);
        } catch (ConnectionException | SQLException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public Message getById(int id) throws RepositoryException {
        try (var set = con.executeSql(SELECT_BY_ID, id)) {
            return readEntityFromSetOrNull(set);
        } catch (SQLException | ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public void create(Message item) throws RepositoryException {
        try {
            con.executeSql(INSERT_INTO,
                    item.getQuoteMessageId(),
                    item.getContent(),
                    item.getSender().getId(),
                    item.getReceiver().getId(),
                    item.isUnread(),
                    item.isEdited(),
                    item.getTime());
        } catch (ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public void delete(Message item) throws RepositoryException {
        try {
            con.executeDml(UPDATE_REMOVE_ALL_QUOTES, item.getId());
            con.executeDml(DELETE_BY_ID, item.getId());
        } catch (ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    public void update(Message item) throws RepositoryException {
        try {
            con.executeDml(UPDATE, item.getContent(), item.isUnread(), item.isEdited(), item.getId());
        } catch (ConnectionException ex) {
            throw new RepositoryException(ex.getMessage());
        }
    }

    @Override
    protected Message readEntityFromSet(ResultSet set) throws SQLException {
        Integer quoteId = set.getInt("M_QUOTE_MESSAGE_ID");
        quoteId = set.wasNull() ? null : quoteId;

        return new Message(set.getInt("M_ID_MESSAGE"),
                quoteId,
                set.getString("M_CONTENT"),
                new User(set.getInt("S_ID_USER"),
                        set.getString("S_NAME"),
                        set.getString("S_LOGIN"),
                        set.getString("S_PASSWORD")),

                new User(set.getInt("R_ID_USER"),
                        set.getString("R_NAME"),
                        set.getString("R_LOGIN"),
                        set.getString("R_PASSWORD")),

                set.getInt("M_STATUS_UNREAD"),
                set.getInt("M_STATUS_EDITED"),
                set.getTimestamp("M_TIME").toLocalDateTime());
    }
}
