package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.RecieveMessage;

public class ReceiveMessageEvent {
    public RecieveMessage recieveMessage;
    public String eventType;

    public ReceiveMessageEvent(RecieveMessage recieveMessage, String eventType) {
        this.recieveMessage = recieveMessage;
        this.eventType = eventType;
    }
}
