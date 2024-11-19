package com.example.o2chatsdk.Events.chatEvents;

import com.example.o2chatsdk.model.chat.SendMessageModel;

public class SendChatEvent {
    public SendMessageModel sendMessageModel;
    public String eventType;

    public SendChatEvent(SendMessageModel sendMessageModel, String eventType) {
        this.sendMessageModel = sendMessageModel;
        this.eventType = eventType;
    }
}
