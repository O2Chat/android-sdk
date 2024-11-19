package com.example.o2chatsdk.Events.chatEvents;

import com.example.o2chatsdk.model.chat.Conversation;

public class ConversationEvent {
    public Conversation conversation;
    public String eventType;

    public ConversationEvent(Conversation conversation, String eventType) {
        this.conversation = conversation;
        this.eventType = eventType;
    }
}
