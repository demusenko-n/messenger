package com.nure.ua.a_clientSide;


import com.nure.ua.a_clientSide.application.Client;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerReader extends Thread {
    private final BufferedReader in;
    private final Client client;
    public ServerReader(BufferedReader reader, Client client) {
        in = reader;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                String serverWord = in.readLine();
                client.receiveData(serverWord);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}

