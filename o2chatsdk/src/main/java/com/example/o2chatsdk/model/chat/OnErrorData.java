package com.example.o2chatsdk.model.chat;

public class OnErrorData {
    public String recieveMessage;
    public String eventType;

    public OnErrorData(String recieveMessage, String eventType) {
        this.recieveMessage = recieveMessage;
        this.eventType = eventType;
    }
}