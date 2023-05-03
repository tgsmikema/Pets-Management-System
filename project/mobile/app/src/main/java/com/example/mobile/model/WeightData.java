package com.example.mobile.model;

import androidx.annotation.NonNull;

public class WeightData {
    private String timeStamp;
    private int noOfDogsWeighted;
    private int noOfDogsUnweighted;

    public WeightData(){}

    public WeightData(String timeStamp, int noOfDogsWeighted, int noOfDogsUnweighted){
        this.timeStamp = timeStamp;
        this.noOfDogsWeighted = noOfDogsWeighted;
        this.noOfDogsUnweighted = noOfDogsUnweighted;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getNoOfDogsWeighted() {
        return noOfDogsWeighted;
    }

    public void setNoOfDogsWeighted(int noOfDogsWeighted) {
        this.noOfDogsWeighted = noOfDogsWeighted;
    }

    public int getNoOfDogsUnweighted() {
        return noOfDogsUnweighted;
    }

    public void setNoOfDogsUnweighted(int noOfDogsUnweighted) {
        this.noOfDogsUnweighted = noOfDogsUnweighted;
    }

    @NonNull
    @Override
    public String toString() {
        return timeStamp + " " + noOfDogsWeighted + " " + noOfDogsUnweighted;
    }
}
