package com.nure.ua.serverSide.service;

import com.nure.ua.entity.Message;
import com.nure.ua.entity.User;
import com.nure.ua.serverSide.exception.ServiceException;

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
