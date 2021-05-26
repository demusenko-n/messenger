package com.nure.ua.exchangeData.dataPack;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class DataPackAdapter implements JsonDeserializer<DataPack>, JsonSerializer<DataPack> {
    @Override
    public DataPack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String cmd = json.get("cmd").getAsString();
        JsonObject map = json.get("map").getAsJsonObject();
        return new DataPackSerialized(cmd, map);
    }

    @Override
    public JsonElement serialize(DataPack dataPack, Type type, JsonSerializationContext context) {

        JsonObject result = new JsonObject();
        result.addProperty("cmd", dataPack.getCommand());

        if (dataPack instanceof DataPackSerialized) {
            result.add("map", ((DataPackSerialized) dataPack).map);
        } else {
            result.add("map", context.serialize(((DataPackImpl) dataPack).getArgs()));
        }


        return result;
    }
}