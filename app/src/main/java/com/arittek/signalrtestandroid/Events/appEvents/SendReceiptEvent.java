package com.arittek.signalrtestandroid.Events.appEvents;

import com.arittek.signalrtestandroid.model.chat.ReadReceiptRequest;

public class SendReceiptEvent {
    public String eventType;
    public ReadReceiptRequest readReceiptRequest;


    public SendReceiptEvent(String eventType, ReadReceiptRequest readReceiptRequest) {
        this.eventType = eventType;
        this.readReceiptRequest = readReceiptRequest;
    }
}
