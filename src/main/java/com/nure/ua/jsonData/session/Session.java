package com.nure.ua.jsonData.session;

import com.nure.ua.entity.User;

public class Session {
    private User user;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Session(User user, int id) {
        this.user = user;
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Session && ((Session)obj).getId() == id;
    }
}

