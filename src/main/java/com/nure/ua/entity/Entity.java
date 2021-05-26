package com.nure.ua.entity;

public abstract class Entity {
    private int id;

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity && ((Entity) obj).id == id;
    }

    public Entity(int id) {
        this.id = id;
    }

    public Entity() {
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = this.id == -1 ? id : this.id;
    }
}
