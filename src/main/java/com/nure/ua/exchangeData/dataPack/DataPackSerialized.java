package com.nure.ua.exchangeData.dataPack;

import com.google.gson.JsonObject;
import com.nure.ua.exchangeData.GsonCreator;

import java.lang.reflect.Type;

public class DataPackSerialized implements DataPack {
    public String command;
    public JsonObject map;
    public DataPackSerialized(String command, JsonObject map) {
        this.command = command;
        this.map = map;
    }

    @Override
    public <T> T get(String key, Type type) {
        return GsonCreator.getInstance().fromJson(map.get(key), type);
    }

    @Override
    public boolean has(String key) {
        return map.has(key);
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public boolean isFailState() {
        return command.equals(failStateMsg);
    }

}
