package com.nure.ua.serverSide.application;

import com.nure.ua.serverSide.ClientContainer;
import com.nure.ua.serverSide.command.ServerCommandContainer;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static final ServerCommandContainer commands;

    static {
        commands = new ServerCommandContainer();
    }

    public static ServerCommandContainer getCommands() {
        return commands;
    }

    public static void main(String[] args) throws Exception {
        new ApplicationContext().config();
        new Server().run();
    }

    public void run() {
        try (var server = new ServerSocket(4004)) {
            System.out.println("Server is running.");
            while (!server.isClosed()) {
                ClientContainer.addClient(server.accept());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Server has been closed.");
        }
    }

}