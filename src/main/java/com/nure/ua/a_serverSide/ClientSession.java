package com.nure.ua.a_serverSide;

import com.nure.ua.exchangeData.Session;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSession implements AutoCloseable {
    public final Socket client;
    public final Session session;
    public final PrintWriter outputStream;

    public PrintWriter getOutputStream() {
        return outputStream;
    }

    public ClientSession(Socket client, Session session) throws IOException {
        this.client = client;
        this.session = session;

        outputStream = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);

        new Thread(new SocketProcessor(this)).start();
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
