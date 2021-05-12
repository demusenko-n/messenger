package com.nure.ua.a_serverSide.application;

import com.nure.ua.application.ApplicationContext;
import com.nure.ua.a_serverSide.Server;
import com.nure.ua.a_serverSide.serverCommand.ServerCommandContainer;

public class ServerApp {
    private static ServerCommandContainer commands;
    private static Server server;

    public static void setServer(Server server) {
        ServerApp.server = server;
    }

    public static ServerCommandContainer getCommands() {
        return commands;
    }

    public static void main(String[] args) throws Exception {
        new ApplicationContext().config();
        server.start();
    }

}