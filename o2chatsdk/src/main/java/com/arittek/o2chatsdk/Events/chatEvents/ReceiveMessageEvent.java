package com.arittek.o2chatsdk.Events.chatEvents;

import com.arittek.o2chatsdk.model.chat.RecieveMessage;

public class ReceiveMessageEvent {
    public RecieveMessage recieveMessage;
    public String eventType;

    public ReceiveMessageEvent(RecieveMessage recieveMessage, String eventType) {
        this.recieveMessage = recieveMessage;
        this.eventType = eventType;
    }
}
