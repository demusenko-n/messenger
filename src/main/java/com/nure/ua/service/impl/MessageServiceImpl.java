package com.nure.ua.service.impl;

import com.nure.ua.model.ConnectionPool;
import com.nure.ua.model.entity.Message;
import com.nure.ua.model.entity.User;
import com.nure.ua.model.exception.ConnectionException;
import com.nure.ua.model.exception.RepositoryException;
import com.nure.ua.model.exception.ServiceException;
import com.nure.ua.model.repository.impl.MessageRepositoryImpl;
import com.nure.ua.service.MessageService;
import com.nure.ua.service.ServiceAbstract;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MessageServiceImpl extends ServiceAbstract<Message> implements MessageService {

    public MessageServiceImpl(ConnectionPool pool) {
        super(pool);
    }

    @Override
    public void createMessage(Message entity) throws ServiceException {
        if (entity == null){
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            new MessageRepositoryImpl(con).create(entity);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public void updateMessage(Message entity) throws ServiceException {
        if (entity == null){
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            new MessageRepositoryImpl(con).update(entity);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public List<Message> getAllMessagesInDialogue(User first, User second) throws ServiceException {
        if (first == null || second == null){
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            return new MessageRepositoryImpl(con).getBetweenTwoUsers(first.getId(), second.getId());
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public Map<User, List<Message>> getAllMessagesWithUser(User user) throws ServiceException {
        if (user == null){
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            var repository = new MessageRepositoryImpl(con);
            List<Message> allMessages = repository.getAllMessagesByUser(user.getId());
            return allMessages.stream()
                    .collect(Collectors.groupingBy(m ->
                            m.getSender().getId() == user.getId() ? m.getReceiver() : m.getSender()));
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public void removeMessage(Message entity) throws ServiceException {
        if (entity == null){
            throw new ServiceException("argument was null");
        }
        try (var con = getPool().getFreeConnection()) {
            new MessageRepositoryImpl(con).delete(entity);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    @Override
    public Message getMessageById(int id) throws ServiceException {
        try (var con = getPool().getFreeConnection()) {
            return new MessageRepositoryImpl(con).getById(id);
        } catch (ConnectionException | RepositoryException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }
}
