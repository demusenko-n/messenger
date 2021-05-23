package com.nure.ua.exchangeData;

import com.nure.ua.exchangeData.dataPack.DataPack;

public class Request {
    public DataPack data;

    public Request(DataPack pack) {
        data = pack;
    }
}