package com.arittek.signalrtestandroid.Events.chatEvents;


import com.arittek.signalrtestandroid.model.chat.ConversationStatusListenerDataModel;

public class ConversationStatusListenerEvent {
    public ConversationStatusListenerDataModel message;
    public String eventType;

    public ConversationStatusListenerEvent(ConversationStatusListenerDataModel message, String eventType) {
        this.message = message;
        this.eventType = eventType;
    }
}
