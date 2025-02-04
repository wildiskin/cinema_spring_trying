package com.wildiskin.cinema.util;

public class BookNameId {

    private long id;

    private String name;

    public BookNameId() {
    }

    public BookNameId(long id, String name) {
        this.name = name;
        this.id = id;
    }

    public BookNameId(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
