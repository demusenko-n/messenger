package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.exception.ServiceException;
import com.nure.ua.exchangeData.DataPack;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import com.nure.ua.exchangeData.Session;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.a_serverSide.service.MessageService;
import com.nure.ua.a_serverSide.service.UserService;

public class SignUpCommand extends Command {
    private final UserService userService;
    private final MessageService messageService;
    private static final String CMD = "auth";

    public SignUpCommand(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Request request, Session session) {
        DataPack dp = new DataPack();
        System.out.println("signupcommand");
        String login = (String) request.data.getArgs().get("login");
        String password = (String) request.data.getArgs().get("password");
        String name = (String) request.data.getArgs().get("name");

        if (login == null || password == null || name == null) {
            dp.setFailState("Not enough arguments");
            sendResponse(new Response(dp, session));
            return;
        }

        try {
            User existingUser = userService.getUserByLogin(login);

            if (existingUser != null) {
                dp.setFailState("User already exists");
                sendResponse(new Response(dp, session));
                return;
            }

            userService.createUser(new User(name, login, password));

            User newUser = userService.getUserByLoginPassword(login, password);

            if (newUser == null) {
                dp.setFailState("Server error, couldn't find new user");
            } else {
                session.setUser(newUser);
                dp.command = CMD;
                dp.getArgs().put("all_messages", messageService.getAllMessagesWithUser(newUser));
            }

        } catch (ServiceException ex) {
            dp.setFailState("Internal server error " + ex.getMessage());
        }

        sendResponse(new Response(dp, session));
    }
}
