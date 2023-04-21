package com.example.mobile.model;

public class Dog {
    private String id;
    private String name;
    private String breed;
    private String location;
    private int[] weightList;

    private String weight;
    private String date;
    private boolean flag;
    private boolean alert;

    public String getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean isAlert() {
        return alert;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getLocation() {
        return location;
    }

    public int[] getWeightList() {
        return weightList;
    }

    public String[] getDateList() {
        return dateList;
    }

    private String[] dateList;
    public Dog() {
    }

    public Dog(String id, String name, String breed, String weight, String date, boolean flag, boolean alert) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.date = date;
        this.flag = flag;
        this.alert = alert;
    }



}
