package com.example.mobile.model;

public class DogRegisterRequest {
    private String name;
    private String breed;
    private int centreId;

    public DogRegisterRequest(String name, String breed, int centreId) {
        this.name = name;
        this.breed = breed;
        this.centreId = centreId;
    }

    public DogRegisterRequest(){}

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

    public int getCentreId() {
        return centreId;
    }

    public void setCentreId(int centreId) {
        this.centreId = centreId;
    }
}
