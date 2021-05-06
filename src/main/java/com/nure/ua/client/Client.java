package com.nure.ua.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

@SuppressWarnings("InfiniteLoopStatement")
public class Client extends Thread {
    static class InputConsoleHandler implements Runnable {
        Socket client;

        InputConsoleHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (var out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()), true);
                 var reader = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    String word = reader.readLine();
                    out.println(word);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    static class InputServerHandler implements Runnable {
        Socket client;

        InputServerHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (var in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                while (true) {
                    String serverWord = in.readLine();
                    if (serverWord != null) {
                        System.out.println(serverWord);
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        try (Socket clientSocket = new Socket(InetAddress.getByName(null), 4004)) {

            Thread inputConsoleThread = new Thread(new InputConsoleHandler(clientSocket));
            inputConsoleThread.start();

            new InputServerHandler(clientSocket).run();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Disconnected.");
        }
    }
}