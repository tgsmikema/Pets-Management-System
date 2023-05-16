package com.example.mobile.model;

public class InvokeRequest {
    private int dogId;
    private int scaleId;

    public InvokeRequest() {}

    public InvokeRequest(int dogId, int scaleId) {
        this.dogId = dogId;
        this.scaleId = scaleId;
    }

    public int getDogId() {
        return dogId;
    }

    public void setDogId(int dogId) {
        this.dogId = dogId;
    }

    public int getScaleId() {
        return scaleId;
    }

    public void setScaleId(int scaleId) {
        this.scaleId = scaleId;
    }
}
