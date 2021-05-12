package com.nure.ua.a_serverSide.serverCommand;

import java.util.HashMap;

public class ServerCommandContainer {
    private final HashMap<String, Command> container;

    public ServerCommandContainer() {
        container = new HashMap<>();
    }

    public void addCommand(String key, Command value) {
        container.put(key, value);
    }

    public Command getCommand(String cmd) {
        return container.get(cmd);
    }

}
