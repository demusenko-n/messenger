package com.nure.ua.serverSide.command;

import com.nure.ua.serverSide.ClientContainer;
import com.nure.ua.jsonData.Request;
import com.nure.ua.jsonData.response.Response;
import com.nure.ua.jsonData.session.Session;

public abstract class Command {
    public abstract void execute(Request request, Session session);

    protected final void sendResponse(Response response) {
        var clientSession = ClientContainer.getClientBySessionId(response.getSession().getId());
        clientSession.ifPresent(session -> session.sendResponse(response));
    }

}
