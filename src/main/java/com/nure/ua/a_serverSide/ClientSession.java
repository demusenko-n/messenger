package com.nure.ua.a_serverSide;

import com.nure.ua.a_serverSide.application.Server;
import com.nure.ua.a_serverSide.serverCommand.Command;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSession extends Thread implements AutoCloseable  {
    public final Socket client;
    public final Session session;
    public final PrintWriter outputStream;

    public ClientSession(Socket client, Session session) throws IOException {
        this.client = client;
        this.session = session;

        outputStream = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
    }

    @Override
    public void run() {
        try (var in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            System.out.println("User connected");
            sendData("Welcome");
            while (!client.isClosed()) {

                String fuckingJsonOrWhatever = in.readLine();
                System.out.println(fuckingJsonOrWhatever);

                Request req = null;    //Request req = somehowGetRequestFromJson(fuckingJsonOrWhatever);

                Command cmd = Server.getCommands().getCommand(req.data.command);
                cmd.execute(req, session);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("User disconnected");
        }

    }

    public synchronized void sendData(String data) {
        outputStream.println(data);
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
        client.close();
    }
}
