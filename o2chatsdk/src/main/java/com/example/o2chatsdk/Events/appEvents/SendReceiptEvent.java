package com.example.o2chatsdk.Events.appEvents;

import com.example.o2chatsdk.model.chat.ReadReceiptRequest;

public class SendReceiptEvent {
    public String eventType;
    public ReadReceiptRequest readReceiptRequest;


    public SendReceiptEvent(String eventType, ReadReceiptRequest readReceiptRequest) {
        this.eventType = eventType;
        this.readReceiptRequest = readReceiptRequest;
    }
}
