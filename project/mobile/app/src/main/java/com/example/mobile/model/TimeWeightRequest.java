package com.example.mobile.model;

import com.google.gson.annotations.SerializedName;

public class TimeWeightRequest {
    @SerializedName("centerId")
    private int centerId;
    @SerializedName("minTimestamp")
    private String minTimestamp;
    @SerializedName("maxTimestamp")
    private String maxTimestamp;

    public TimeWeightRequest(){

    }

    public TimeWeightRequest(int centerId,String minTimestamp,String maxTimestamp){
        this.centerId = centerId;
        this.minTimestamp = minTimestamp;
        this.maxTimestamp = maxTimestamp;
    }

    public int getCenterId() {
        return centerId;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
    }

    public String getMinTimestamp() {
        return minTimestamp;
    }

    public void setMinTimestamp(String minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public String getMaxTimestamp() {
        return maxTimestamp;
    }

    public void setMaxTimestamp(String maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }
}
