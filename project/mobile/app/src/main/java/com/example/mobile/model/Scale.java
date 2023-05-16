package com.example.mobile.model;

public class Scale {
    private int id;
    private int centreId;
    private String name;

    public Scale(){}

    public Scale(int id, int centreId, String name) {
        this.id = id;
        this.centreId = centreId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCentreId() {
        return centreId;
    }

    public void setCentreId(int centreId) {
        this.centreId = centreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
