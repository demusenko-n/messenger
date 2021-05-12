package com.nure.ua.exchangeData;

public class Response {
    public Session session;
    public DataPack data;

    public Response(DataPack rdata, Session session) {
        this.data = rdata;
        this.session = session;
    }
}

