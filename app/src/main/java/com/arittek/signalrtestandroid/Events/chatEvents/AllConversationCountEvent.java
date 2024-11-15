package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.ConversationsCountModel;

public class AllConversationCountEvent {
    public ConversationsCountModel conversationsCountModel;
    public String eventType;

    public AllConversationCountEvent(ConversationsCountModel conversationsCountModel, String eventType) {
        this.conversationsCountModel = conversationsCountModel;
        this.eventType = eventType;
    }
}
