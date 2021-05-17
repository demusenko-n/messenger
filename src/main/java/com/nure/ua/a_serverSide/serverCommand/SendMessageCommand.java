package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.a_serverSide.exception.ServiceException;
import com.nure.ua.exchangeData.DataPack;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import com.nure.ua.exchangeData.Session;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.a_serverSide.service.MessageService;
import com.nure.ua.a_serverSide.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

public class SendMessageCommand extends Command {
    private final UserService userService;
    private final MessageService messageService;
    private static final String CMD = "newmessage";
    public SendMessageCommand(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Request request, Session session) {
        DataPack responsePack = new DataPack();

        Integer idSender, idReceiver, quoteMessageId;
        String content;

        idSender = (Integer) request.data.getArgs().get("sender");
        idReceiver = (Integer) request.data.getArgs().get("receiver");
        quoteMessageId = (Integer) request.data.getArgs().get("quote");
        content = (String) request.data.getArgs().get("content");

        if (idSender == null || idReceiver == null || content == null) {
            responsePack.setFailState("Not enough arguments");
            sendResponse(new Response(responsePack, session));
            return;
        }


        try {
            User sender = userService.getUserById(idSender);
            User receiver = userService.getUserById(idReceiver);

            if (sender == null || receiver == null) {
                responsePack.setFailState("Sender and/or receiver do(es)n't exist");
                sendResponse(new Response(responsePack, session));
                return;
            }

            Message message = new Message(quoteMessageId, content, sender, receiver, 1, 0, LocalDateTime.now());
            messageService.createMessage(message);

            responsePack.command = CMD;
            responsePack.getArgs().put("message", message);

            List<Session> sessions = ClientContainer.getSessionsOfUser(receiver);
            sessions.add(session);

            for (Session s : sessions) {
                sendResponse(new Response(responsePack, s));
            }

        } catch (ServiceException ex) {
            responsePack.setFailState("Server error: " + ex.getMessage());
            sendResponse(new Response(responsePack, session));
        }
    }
}
