package com.nure.ua.serverSide;

import com.nure.ua.serverSide.application.Server;
import com.nure.ua.serverSide.command.Command;
import com.nure.ua.jsonData.GsonCreator;
import com.nure.ua.jsonData.Request;
import com.nure.ua.jsonData.response.Response;
import com.nure.ua.jsonData.session.Session;

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

    public Session getSession() {
        return session;
    }

    public ClientSession(Socket client, Session session) throws IOException {
        this.client = client;
        this.session = session;
        outputStream = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
    }

    @Override
    public void run() {

        try (client; outputStream;
             var in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            System.out.println("User connected");
            while (!client.isClosed()) {
                String requestJson = in.readLine();

                Request req = GsonCreator.getInstance().fromJson(requestJson, Request.class);

                Command cmd = Server.getCommands().getCommand(req.data.getCommand());
                if (cmd == null) {
                    throw new IOException();
                }
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
        sendStr(GsonCreator.getInstance().toJson(response));
    }
}
