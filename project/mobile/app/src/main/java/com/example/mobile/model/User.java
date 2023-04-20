package com.example.mobile.model;

public class User {

    private int id;
    private String userName;
    private String userType;
    private String firstName;
    private String lastName;
    private int centreId;
    private String token;

    public User(){

    }

    public User(String userName, String userType){
         this.userName = userName;
         this.userType = userType;
    }

    public int getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }


    public String getUserType() {
        return userType;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public int getCentreId() {
        return centreId;
    }


    public String getToken() {
        return token;
    }

}
