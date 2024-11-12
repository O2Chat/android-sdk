package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.Conversation;

public class ConversationEvent {
    public Conversation conversation;
    public String eventType;

    public ConversationEvent(Conversation conversation, String eventType) {
        this.conversation = conversation;
        this.eventType = eventType;
    }
}
