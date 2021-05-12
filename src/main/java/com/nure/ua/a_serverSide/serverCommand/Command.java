package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import com.nure.ua.exchangeData.Session;
import com.nure.ua.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Command {
    private ClientContainer container;

    protected Command(ClientContainer clients) {
        container = clients;
    }

    public abstract void execute(Request request, Session session);

    protected void sendResponse(Response response) {
        var clientSession = container.getClientBySessionId(response.session.getId());
        //clientSession.ifPresent(session -> session.sendData(convertToJson(response)));
        clientSession.ifPresent(session -> session.sendData("DATA"));
    }

    protected List<Session> getSessionsOfUser(User user) {
        return container.getClientsByUserId(user.getId()).stream().map(clientSession -> clientSession.session).collect(Collectors.toList());
    }
}
