package com.nure.ua.serverSide.command;

import com.nure.ua.serverSide.exception.ServiceException;
import com.nure.ua.entity.User;
import com.nure.ua.serverSide.service.UserService;
import com.nure.ua.jsonData.Request;
import com.nure.ua.jsonData.dataPack.DataPackImpl;
import com.nure.ua.jsonData.response.Response;
import com.nure.ua.jsonData.session.Session;

public class FindUserCommand extends Command {
    private final UserService userService;
    private static final String CMD = "find_user";

    public FindUserCommand(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void execute(Request request, Session session) {
        DataPackImpl responsePack = new DataPackImpl();

        String login = request.data.get("login", String.class);

        if (login == null) {
            responsePack.setFailState("Not enough arguments");
            sendResponse(new Response(responsePack, session));
            return;
        }


        try {
            User user = userService.getUserByLogin(login);

            if (user == null) {
                responsePack.setFailState("User doesn't exist");
                sendResponse(new Response(responsePack, session));
                return;
            }

            responsePack.setCommand(CMD);
            responsePack.getArgs().put("user", user);
            
            sendResponse(new Response(responsePack, session));
        } catch (ServiceException ex) {
            responsePack.setFailState("Server error: " + ex.getMessage());
            sendResponse(new Response(responsePack, session));
        }
    }
}
