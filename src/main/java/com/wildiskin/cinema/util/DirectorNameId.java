package com.wildiskin.cinema.util;

public class DirectorNameId {

    private long id;

    private String name;

    public DirectorNameId() {
    }

    public DirectorNameId(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public DirectorNameId(String name) {
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
