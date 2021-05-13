package com.nure.ua.a_serverSide;

import com.nure.ua.exchangeData.Session;
import com.nure.ua.model.entity.User;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientContainer {
    private ClientContainer() {
    }

    public static List<Session> getSessionsOfUser(User user) {
        return ClientContainer.getClientsByUserId(user.getId()).stream().map(clientSession -> clientSession.session).collect(Collectors.toList());
    }

    private static int counter = 0;
    private static final List<ClientSession> clients;

    public static List<ClientSession> getClients() {
        return clients;
    }

    public static Optional<ClientSession> getClientBySessionId(int id) {
        return clients.stream().filter(x -> x.session.getId() == id).findFirst();
    }

    public static List<ClientSession> getClientsByUserId(int id) {
        return clients.stream().filter(x -> (x.session.getUser() != null && x.session.getUser().getId() == id)).collect(Collectors.toList());
    }
    static{
        clients = new ArrayList<>();
    }

    public static void addClient(Socket client) throws IOException {
        ClientSession session = new ClientSession(client, new Session(null, ++counter));
        session.start();
        clients.add(session);
    }
}
