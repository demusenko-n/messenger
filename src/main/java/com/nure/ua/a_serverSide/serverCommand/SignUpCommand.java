package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.exception.ServiceException;
import com.nure.ua.exchangeData.DataPack;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import com.nure.ua.exchangeData.Session;
import com.nure.ua.model.entity.User;
import com.nure.ua.service.MessageService;
import com.nure.ua.service.UserService;

public class SignUpCommand extends Command {
    private final UserService userService;
    private final MessageService messageService;
    private static final String CMD = "auth";

    public SignUpCommand(ClientContainer container, UserService userService, MessageService messageService) {
        super(container);
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Request request, Session session) {
        DataPack dp = new DataPack();

        String login = (String) request.data.args.get("LOGIN");
        String password = (String) request.data.args.get("PASSWORD");
        String name = (String) request.data.args.get("NAME");

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
                dp.args.put("user", newUser);
                dp.args.put("allmessages", messageService.getAllMessagesWithUser(newUser));
            }

        } catch (ServiceException ex) {
            dp.setFailState("Internal server error " + ex.getMessage());
        }

        sendResponse(new Response(dp, session));
    }
}
