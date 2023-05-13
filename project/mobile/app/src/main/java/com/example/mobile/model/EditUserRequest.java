package com.example.mobile.model;

public class EditUserRequest {

    private int userId;
    private String userType;

    public EditUserRequest(){

    }

    public EditUserRequest(int userId,String userType){
        this.userId = userId;
        this.userType = userType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
