package com.nure.ua.exchangeData;
import com.nure.ua.a_serverSide.model.entity.User;

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
}
