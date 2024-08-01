package com.example.o2chatsdk.Events.chatEvents;


import com.example.o2chatsdk.model.chat.ConversationStatusListenerDataModel;

public class ConversationStatusListenerEvent {
    public ConversationStatusListenerDataModel message;
    public String eventType;

    public ConversationStatusListenerEvent(ConversationStatusListenerDataModel message, String eventType) {
        this.message = message;
        this.eventType = eventType;
    }
}