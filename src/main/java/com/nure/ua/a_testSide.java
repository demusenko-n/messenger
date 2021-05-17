package com.nure.ua;

import com.google.gson.Gson;
import com.nure.ua.exchangeData.DataPack;

public class a_testSide {
    public static void main(String[] args) throws Exception {
        DataPack dp = new DataPack();
        dp.getArgs().put("key", "value");
        dp.getArgs().put("number", 15);
        dp.command = "cmd";

        Gson gson = new Gson();
        String str = gson.toJson(dp);


        System.out.println("JSON: " + str);


        DataPack res = gson.fromJson(str, DataPack.class);


        System.out.println("JSON: " + gson.toJson(res));
    }
}
