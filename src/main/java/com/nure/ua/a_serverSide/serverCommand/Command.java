package com.nure.ua.a_serverSide.serverCommand;

import com.nure.ua.a_serverSide.ClientContainer;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.response.Response;
import com.nure.ua.exchangeData.session.Session;

public abstract class Command {
    public abstract void execute(Request request, Session session);

    protected final void sendResponse(Response response) {
        var clientSession = ClientContainer.getClientBySessionId(response.getSession().getId());
        clientSession.ifPresent(session -> session.sendResponse(response));
    }

}
