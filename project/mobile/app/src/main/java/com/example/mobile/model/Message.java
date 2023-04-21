package com.example.mobile.model;

public class Message {

    private int id;
    private String date;
    private String messageContent;
    private User toUser;
    private User fromUser;

    public Message(){

    }

    public Message(int id, String date,String messageContent,User toUser, User fromUser){
        this.id = id;
        this.date = date;
        this.messageContent = messageContent;
        this.toUser = toUser;
        this.fromUser = fromUser;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public User getToUser() {
        return toUser;
    }

    public User getFromUser() {
        return fromUser;
    }
}
