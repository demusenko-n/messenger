package com.nure.ua;

import com.google.gson.reflect.TypeToken;
import com.nure.ua.a_serverSide.model.entity.Message;
import com.nure.ua.a_serverSide.model.entity.User;
import com.nure.ua.exchangeData.GsonCreator;
import com.nure.ua.exchangeData.Request;
import com.nure.ua.exchangeData.dataPack.DataPackImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class a_testSide {
    public static void main(String[] args) throws Exception {
        HashMap<User, List<Message>> mes = new HashMap<>();
        List<Message> messages = new ArrayList<>();
        User user = new User(1,"name","log","pass");
        messages.add(new Message(1,null,"content",user, user, 1, 1, LocalDateTime.now()));
        mes.put(user, messages);


        DataPackImpl dp = new DataPackImpl();
        dp.put("all_messages", mes);

        dp.getArgs().put("key", "value");
        dp.getArgs().put("number", 15);
        dp.setCommand("cmd");


        Request req = new Request(dp);

        String str = GsonCreator.getInstance().toJson(req, Request.class);


        System.out.println("JSON: " + str);


        Request baseReq = GsonCreator.getInstance().fromJson(str, Request.class);

        mes = baseReq.data.get("all_messages", new TypeToken<Map<User, List<Message>>>() {}.getType());


    }
}
