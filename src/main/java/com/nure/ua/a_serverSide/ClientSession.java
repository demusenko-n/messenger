package com.nure.ua.a_serverSide;

import com.google.gson.Gson;
import com.nure.ua.a_serverSide.application.Server;
import com.nure.ua.a_serverSide.serverCommand.Command;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Response;
import com.nure.ua.exchangeData.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSession extends Thread {
    private final Socket client;
    private final Session session;
    private final PrintWriter outputStream;
    private final Gson gson;

    public Session getSession() {
        return session;
    }

    public ClientSession(Socket client, Session session) throws IOException {
        this.client = client;
        this.session = session;
        outputStream = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
        gson = new Gson();
    }

    @Override
    public void run() {

        try (client; outputStream;
             var in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            System.out.println("User connected");
            while (!client.isClosed()) {
                String fkJson = in.readLine();
                Request req = gson.fromJson(fkJson, Request.class);
                Command cmd = Server.getCommands().getCommand(req.data.command);
                cmd.execute(req, session);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("User disconnected");
            ClientContainer.getClients().remove(this);
        }

    }

    public synchronized void sendStr(String data) {
        outputStream.println(data);
    }

    public void sendResponse(Response response) {
        sendStr(gson.toJson(response));
    }
}
