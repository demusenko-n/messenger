package com.nure.ua.a_serverSide;

import com.nure.ua.a_serverSide.application.ServerApp;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.a_serverSide.serverCommand.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SocketProcessor implements Runnable {
    private final ClientSession clientSession;

    public SocketProcessor(ClientSession client) {
        this.clientSession = client;
    }

    @Override
    public void run() {
        try (clientSession.client;
             var in = new BufferedReader(new InputStreamReader(clientSession.client.getInputStream()))) {


            System.out.println("User connected");
            clientSession.sendData("Welcome");
            while (!clientSession.client.isClosed()) {

                String fuckingJsonOrWhatever = in.readLine();
                System.out.println(fuckingJsonOrWhatever);

                Request req = null;    //Request = somehowGetRequestFromJson(fuckingJsonOrWhatever);

                Command cmd = ServerApp.getCommands().getCommand(req.data.command);
                cmd.execute(req, clientSession.session);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("User disconnected");
        }

    }
}