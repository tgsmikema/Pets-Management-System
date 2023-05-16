package com.example.mobile.model;

public class DogEditRequest {
    private int id;
    private int centreId;
    private String name;
    private String breed;

    public DogEditRequest(){}

    public DogEditRequest(int id, int centreId, String name, String breed) {
        this.id = id;
        this.centreId = centreId;
        this.name = name;
        this.breed = breed;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
}
