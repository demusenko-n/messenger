package com.nure.ua.model.repository;

import com.nure.ua.model.entity.Message;
import com.nure.ua.exception.RepositoryException;

import java.util.List;

public interface MessageRepository extends Repository<Message> {
    List<Message> getBySenderId(int id) throws RepositoryException;
    List<Message> getByReceiverId(int id) throws RepositoryException;
    List<Message> getBetweenTwoUsers(int idSender, int idReceiver) throws RepositoryException;
    List<Message> getAllMessagesByUser(int idUser) throws RepositoryException;
    List<Message> getByContent(String content) throws RepositoryException;
    List<Message> getThatContainsContent(String content) throws RepositoryException;
}
