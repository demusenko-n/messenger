package com.nure.ua.exchangeData.response;

import com.nure.ua.exchangeData.session.Session;
import com.nure.ua.exchangeData.dataPack.DataPack;

public class Response {

    private Session session;
    private DataPack data;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public DataPack getData() {
        return data;
    }

    public void setData(DataPack data) {
        this.data = data;
    }

    public Response(DataPack rdata, Session session) {
        this.data = rdata;
        this.session = session;
    }
}
