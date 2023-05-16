package com.example.mobile.model;

public class DogDetail {
    private int id;
    private String name;
    private String breed;
    private int centreId;
    private boolean isFlag;
    private boolean isAlert;
    private String lastCheckInTimeStamp;
    private double lastCheckInWeight;

    public  DogDetail(){}

    public DogDetail(int id, String name, String breed, int centreId, boolean isFlag, boolean isAlert, String lastCheckInTimeStamp, double lastCheckInWeight) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.centreId = centreId;
        this.isFlag = isFlag;
        this.isAlert = isAlert;
        this.lastCheckInTimeStamp = lastCheckInTimeStamp;
        this.lastCheckInWeight = lastCheckInWeight;
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

    public String getLastCheckInTimeStamp() {
        return lastCheckInTimeStamp;
    }

    public void setLastCheckInTimeStamp(String lastCheckInTimeStamp) {
        this.lastCheckInTimeStamp = lastCheckInTimeStamp;
    }

    public double getLastCheckInWeight() {
        return lastCheckInWeight;
    }

    public void setLastCheckInWeight(double lastCheckInWeight) {
        this.lastCheckInWeight = lastCheckInWeight;
    }
}
