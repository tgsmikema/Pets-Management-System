package com.example.mobile.model;

public class MessageRequest
{
    private int fromUserId;
    private int toUserId;
    private String messageContent;

    public MessageRequest(){

    }

    public MessageRequest(int fromUserId,int toUserId,String messageContent){
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.messageContent = messageContent;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
