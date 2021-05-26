package com.nure.ua.jsonData.session;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nure.ua.entity.User;

import java.lang.reflect.Type;

public class SessionAdapter implements JsonDeserializer<Session>, JsonSerializer<Session> {
    @Override
    public Session deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!jsonElement.isJsonObject()) {
            throw new JsonParseException("jsonElement is not jsonObject, deserializing Session");
        }

        JsonObject json = jsonElement.getAsJsonObject();
        int id = json.get("id").getAsInt();
        User user = context.deserialize(json.get("user"), User.class);

        return new Session(user, id);
    }

    @Override
    public JsonElement serialize(Session session, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("id", session.getId());
        result.add("user", context.serialize(session.getUser()));
        return result;
    }
}
