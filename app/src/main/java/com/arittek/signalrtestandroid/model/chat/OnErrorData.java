package com.arittek.signalrtestandroid.model.chat;

public class OnErrorData {
    public String recieveMessage;
    public String eventType;

    public OnErrorData(String recieveMessage, String eventType) {
        this.recieveMessage = recieveMessage;
        this.eventType = eventType;
    }
}
