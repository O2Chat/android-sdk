package com.example.o2chatsdk.Events.chatEvents;

import com.example.o2chatsdk.model.chat.ConversationStatusModel;

public class ConversationStatusEvent {
    public ConversationStatusModel conversationStatusModel;
    public String eventType;

    public ConversationStatusEvent(ConversationStatusModel conversationStatusModel, String eventType) {
        this.conversationStatusModel = conversationStatusModel;
        this.eventType = eventType;
    }
}
