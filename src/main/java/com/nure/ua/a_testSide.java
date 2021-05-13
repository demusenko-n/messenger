package com.nure.ua;

import com.nure.ua.exchangeData.DataPack;
import com.nure.ua.exchangeData.Request;

public class a_testSide {
    public static void main(String[] args) throws Exception {
        Request req = new Request(new DataPack());
        req.data.getArgs().put("key", "value");

        //ObjectMapper mapper = new JsonMapper();
        //String reqAsString  = mapper.writeValueAsString(mapper);

//        System.out.println(reqAsString);
    }
}
