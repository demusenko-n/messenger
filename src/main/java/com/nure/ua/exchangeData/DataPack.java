package com.nure.ua.exchangeData;

import java.util.HashMap;
import java.util.Map;

public class DataPack {
    public String command;
    public Map<String, Object> args;

    public DataPack() {
        this.command = "";
        args = new HashMap<>();
    }

    public void setFailState(String message) {
        command = "exception";
        args.put("message", message);
    }
}
