package com.example.signalrtestandroid.model.chat;

public class EventAddTopicMessage {
    public String eventType;
    public String message;

    public EventAddTopicMessage(String eventType, String message) {
        this.eventType = eventType;
        this.message = message;
    }
}
