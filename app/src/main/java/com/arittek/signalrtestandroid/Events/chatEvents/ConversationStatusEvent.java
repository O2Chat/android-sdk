package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.ConversationStatusModel;

public class ConversationStatusEvent {
    public ConversationStatusModel conversationStatusModel;
    public String eventType;

    public ConversationStatusEvent(ConversationStatusModel conversationStatusModel, String eventType) {
        this.conversationStatusModel = conversationStatusModel;
        this.eventType = eventType;
    }
}
