package com.nure.ua.exchangeData.trash;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nure.ua.a_serverSide.model.entity.User;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {

        if (user == null) {
            return JsonNull.INSTANCE;
        }

        return new JsonPrimitive(user.getId() + " " + user.getName() + " " + user.getLogin() + " " + user.getPassword());
    }
    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonNull()) {
            return null;
        }
        String data = jsonElement.getAsString();
        List<String> parts = Arrays.asList(data.split(" "));

        return new User(Integer.parseInt(parts.get(0)), parts.get(1), parts.get(2), parts.get(3));
    }
}


