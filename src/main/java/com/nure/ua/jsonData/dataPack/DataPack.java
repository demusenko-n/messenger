package com.nure.ua.jsonData.dataPack;

import java.lang.reflect.Type;

public interface DataPack {
    String failStateMsg = "ex";
    String getCommand();
    boolean isFailState();
    <T> T get(String key, Type type);
    boolean has(String key);
}
