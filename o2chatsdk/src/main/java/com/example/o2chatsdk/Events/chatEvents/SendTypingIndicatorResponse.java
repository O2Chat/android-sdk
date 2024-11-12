package com.example.o2chatsdk.Events.chatEvents;

import com.example.o2chatsdk.model.chat.TypingIndicatorListenerModel;

public class SendTypingIndicatorResponse {
    public TypingIndicatorListenerModel typingIndicatorListenerModel;
    public String eventType;

    public SendTypingIndicatorResponse(TypingIndicatorListenerModel typingIndicatorListenerModel, String eventType) {
        this.typingIndicatorListenerModel = typingIndicatorListenerModel;
        this.eventType = eventType;
    }
}
