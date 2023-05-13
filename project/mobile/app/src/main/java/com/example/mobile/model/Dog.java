package com.example.mobile.model;

public class Dog {
    private int id;
    private String name;
    private String breed;

    private int centreId;
    private double lastCheckInWeight;
    private String lastCheckInTimeStamp;
    private boolean isFlag;
    private boolean isAlert;

    public Dog(){

    }

    public Dog(int id, String name, String breed, int centreId, double lastCheckInWeight, String lastCheckInTimeStamp, boolean isFlag, boolean isAlert) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.centreId = centreId;
        this.lastCheckInWeight = lastCheckInWeight;
        this.lastCheckInTimeStamp = lastCheckInTimeStamp;
        this.isFlag = isFlag;
        this.isAlert = isAlert;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCentreId() {
        return centreId;
    }

    public void setCentreId(int centreId) {
        this.centreId = centreId;
    }

    public double getLastCheckInWeight() {
        return lastCheckInWeight;
    }

    public void setLastCheckInWeight(double lastCheckInWeight) {
        this.lastCheckInWeight = lastCheckInWeight;
    }

    public String getLastCheckInTimeStamp() {
        return lastCheckInTimeStamp;
    }

    public void setLastCheckInTimeStamp(String lastCheckInTimeStamp) {
        this.lastCheckInTimeStamp = lastCheckInTimeStamp;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean flag) {
        isFlag = flag;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setAlert(boolean alert) {
        isAlert = alert;
    }
}
