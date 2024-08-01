package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.ConversationsCountModel;

public class AllConversationCountEvent {
    public ConversationsCountModel conversationsCountModel;
    public String eventType;

    public AllConversationCountEvent(ConversationsCountModel conversationsCountModel, String eventType) {
        this.conversationsCountModel = conversationsCountModel;
        this.eventType = eventType;
    }
}
