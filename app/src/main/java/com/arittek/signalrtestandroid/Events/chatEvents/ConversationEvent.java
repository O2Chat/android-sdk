package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.Conversation;

public class ConversationEvent {
    public Conversation conversation;
    public String eventType;

    public ConversationEvent(Conversation conversation, String eventType) {
        this.conversation = conversation;
        this.eventType = eventType;
    }
}
