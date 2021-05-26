package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.a_serverSide.exception.ServiceException;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.a_serverSide.service.MessageService;
import com.nure.ua.a_serverSide.service.UserService;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.dataPack.DataPackImpl;
import com.nure.ua.exchangeData.response.Response;
import com.nure.ua.exchangeData.session.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        DataPackImpl responsePack = new DataPackImpl();

        Integer idSender = request.data.get("sender", Integer.class);
        Integer idReceiver = request.data.get("receiver", Integer.class);
        Integer quoteMessageId = request.data.get("quote", Integer.class);
        String content = request.data.get("content", String.class);

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

            responsePack.setCommand(CMD);
            responsePack.getArgs().put("message", message);

            List<Session> sessions = ClientContainer.getSessionsOfUser(receiver);
            sessions.addAll(ClientContainer.getSessionsOfUser(sender));
            sessions = sessions.stream().distinct().collect(Collectors.toList());

            System.out.println(sessions.size());

            for (Session s : sessions) {
                sendResponse(new Response(responsePack, s));
            }

        } catch (ServiceException ex) {
            responsePack.setFailState("Server error: " + ex.getMessage());
            sendResponse(new Response(responsePack, session));
        }
    }
}
