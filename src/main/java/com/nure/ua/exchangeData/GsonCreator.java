package com.nure.ua.exchangeData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.exchangeData.dataPack.DataPack;
import com.nure.ua.exchangeData.dataPack.DataPackAdapter;
import com.nure.ua.exchangeData.session.Session;
import com.nure.ua.exchangeData.session.SessionAdapter;
import com.nure.ua.exchangeData.trash.UserAdapter;

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
