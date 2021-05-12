package com.nure.ua.a_serverSide;

import com.nure.ua.exchangeData.Session;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientContainer {
    private static int counter = 0;
    private final List<ClientSession> clients;


    public List<ClientSession> getClients() {
        return clients;
    }

    public Optional<ClientSession> getClientBySessionId(int id) {
        return clients.stream().filter(x -> x.session.getId() == id).findFirst();
    }

    public List<ClientSession> getClientsByUserId(int id) {
        return clients.stream().filter(x -> (x.session.getUser() != null && x.session.getUser().getId() == id)).collect(Collectors.toList());
    }

    public ClientContainer() {
        clients = new ArrayList<>();
    }

    public void addClient(Socket client) throws IOException {
        clients.add(new ClientSession(client, new Session(null, ++counter)));
    }
}
