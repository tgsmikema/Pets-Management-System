package com.example.mobile.model;

public class DogWeightHistory {
    private int id;
    private int dogId;
    private double dogWeight;
    private String timeStamp;

    public DogWeightHistory(){

    }

    public DogWeightHistory(int id, int dogId, double dogWeight, String timeStamp) {
        this.id = id;
        this.dogId = dogId;
        this.dogWeight = dogWeight;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDogId() {
        return dogId;
    }

    public void setDogId(int dogId) {
        this.dogId = dogId;
    }

    public double getDogWeight() {
        return dogWeight;
    }

    public void setDogWeight(double dogWeight) {
        this.dogWeight = dogWeight;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
