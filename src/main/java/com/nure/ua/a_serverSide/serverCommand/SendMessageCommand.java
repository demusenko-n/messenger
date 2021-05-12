package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.exception.ServiceException;
import com.nure.ua.exchangeData.DataPack;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import com.nure.ua.exchangeData.Session;
import com.nure.ua.model.entity.Message;
import com.nure.ua.model.entity.User;
import com.nure.ua.service.MessageService;
import com.nure.ua.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

public class SendMessageCommand extends Command {
    private final UserService userService;
    private final MessageService messageService;
    private static final String CMD = "newmessage";
    public SendMessageCommand(ClientContainer container, UserService userService, MessageService messageService) {
        super(container);
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Request request, Session session) {
        DataPack responsePack = new DataPack();

        Integer idSender, idReceiver, quoteMessageId;
        String content;

        idSender = (Integer) request.data.args.get("sender");
        idReceiver = (Integer) request.data.args.get("receiver");
        quoteMessageId = (Integer) request.data.args.get("quote");
        content = (String) request.data.args.get("content");

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
            responsePack.args.put("message", message);

            List<Session> sessions = getSessionsOfUser(receiver);
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
