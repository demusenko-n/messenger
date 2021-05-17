package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.exception.ServiceException;
import com.nure.ua.exchangeData.DataPack;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import com.nure.ua.exchangeData.Session;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.a_serverSide.service.MessageService;
import com.nure.ua.a_serverSide.service.UserService;

public class SignInCommand extends Command {
    private final UserService userService;
    private final MessageService messageService;
    private static final String CMD = "auth";

    public SignInCommand(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Request request, Session session) {
        DataPack dp = new DataPack();

        String login = (String) request.data.getArgs().get("login");
        String password = (String) request.data.getArgs().get("password");

        if (login == null || password == null) {
            dp.setFailState("Not enough arguments");
            sendResponse(new Response(dp, session));
            return;
        }

        try {
            User user = userService.getUserByLoginPassword(login, password);
            if (user == null) {
                dp.setFailState("Incorrect login or password");
            } else {
                dp.command = CMD;
                dp.getArgs().put("all_messages", messageService.getAllMessagesWithUser(user));
            }
            session.setUser(user);
        } catch (ServiceException ex) {
            dp.setFailState("Server error: " + ex.getMessage());
        }
        sendResponse(new Response(dp, session));
    }
}
