package com.nure.ua.clientSide;


import com.nure.ua.jsonData.GsonCreator;
import com.nure.ua.jsonData.response.Response;

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
                if(serverWord != null)
                    client.receiveData(GsonCreator.getInstance().fromJson(serverWord, Response.class));
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }
}

