package com.nure.ua.exchangeData.dataPack;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DataPackImpl implements DataPack {
    private String command;
    private final Map<String, Object> args;

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public DataPackImpl() {
        this.command = "";
        args = new HashMap<>();
    }

    public void setFailState(String message) {
        command = failStateMsg;
        args.put("ex_message", message);
    }

    @Override
    public boolean isFailState() {
        return command.equals(failStateMsg);
    }

    @Override
    public <T> T get(String key, Type type) {
        @SuppressWarnings("unchecked")
        T res = (T) args.get(key);
        return res;
    }

    @Override
    public boolean has(String key) {
        return args.containsKey(key);
    }


    public <T> void put(String key, T value) {
        args.put(key, value);
    }

    @Override
    public String getCommand() {
        return command;
    }
}


