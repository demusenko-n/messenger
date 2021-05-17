package com.nure.ua.a_clientSide;


import com.google.gson.Gson;
import com.nure.ua.exchangeData.Response;

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
        Gson gson = new Gson();
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                String serverWord = in.readLine();
                client.receiveData(gson.fromJson(serverWord, Response.class));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}

