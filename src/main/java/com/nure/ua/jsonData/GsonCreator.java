package com.nure.ua.jsonData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nure.ua.entity.User;
import com.nure.ua.jsonData.dataPack.DataPack;
import com.nure.ua.jsonData.dataPack.DataPackAdapter;
import com.nure.ua.jsonData.session.Session;
import com.nure.ua.jsonData.session.SessionAdapter;
import com.nure.ua.jsonData.userAdapter.UserAdapter;

public class GsonCreator {
    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(DataPack.class, new DataPackAdapter())
            .registerTypeAdapter(Session.class, new SessionAdapter())
            .registerTypeAdapter(User.class, new UserAdapter())
            .create();

    public static Gson getInstance() {
        return gson;
    }
}
