package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.ConversationStatusModel;

public class ConversationStatusEvent {
    public ConversationStatusModel conversationStatusModel;
    public String eventType;

    public ConversationStatusEvent(ConversationStatusModel conversationStatusModel, String eventType) {
        this.conversationStatusModel = conversationStatusModel;
        this.eventType = eventType;
    }
}
