package com.example.mobile.model;

public class DogWeight {
    private int dogId;
    private double weight;

    public DogWeight(){}

    public DogWeight(int dogId, double weight) {
        this.dogId = dogId;
        this.weight = weight;
    }

    public int getDogId() {
        return dogId;
    }

    public void setDogId(int dogId) {
        this.dogId = dogId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
