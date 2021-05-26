package com.nure.ua.serverSide.command;

import com.nure.ua.serverSide.exception.ServiceException;
import com.nure.ua.entity.User;
import com.nure.ua.serverSide.service.MessageService;
import com.nure.ua.serverSide.service.UserService;
import com.nure.ua.jsonData.dataPack.DataPackImpl;
import com.nure.ua.jsonData.Request;
import com.nure.ua.jsonData.response.Response;
import com.nure.ua.jsonData.session.Session;

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
        DataPackImpl dp = new DataPackImpl();
        System.out.println("signupcommand");
        String login = request.data.get("login", String.class);
        String password = request.data.get("password", String.class);
        String name = request.data.get("name", String.class);

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
                dp.setCommand(CMD);
                dp.getArgs().put("all_messages", messageService.getAllMessagesWithUser(newUser));
            }

        } catch (ServiceException ex) {
            dp.setFailState("Internal server error " + ex.getMessage());
        }

        sendResponse(new Response(dp, session));
    }
}
