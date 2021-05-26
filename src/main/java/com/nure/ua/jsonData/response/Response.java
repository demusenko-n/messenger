package com.nure.ua.jsonData.response;

import com.nure.ua.jsonData.session.Session;
import com.nure.ua.jsonData.dataPack.DataPack;

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
