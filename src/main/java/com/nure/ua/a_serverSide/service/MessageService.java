package com.nure.ua.service;

import com.nure.ua.model.entity.Message;
import com.nure.ua.model.entity.User;
import com.nure.ua.a_serverSide.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface MessageService {
    void createMessage(Message message) throws ServiceException;
    void removeMessage(Message message) throws ServiceException;
    void updateMessage(Message message) throws ServiceException;

    List<Message> getAllMessagesInDialogue(User first, User second) throws ServiceException;
    Map<User, List<Message>> getAllMessagesWithUser(User user) throws ServiceException;
    Message getMessageById(int id) throws ServiceException;
}
