package com.example.signalrtestandroid.Events.appEvents;

import com.example.signalrtestandroid.model.chat.ReadReceiptRequest;

public class ReadReceiptUpdateEvent {

    public String eventType;
    public ReadReceiptRequest readReceiptRequest;

    public ReadReceiptUpdateEvent(String eventType, ReadReceiptRequest readReceiptRequest) {
        this.eventType = eventType;
        this.readReceiptRequest = readReceiptRequest;
    }
}
