package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.TypingIndicatorListenerModel;

public class SendTypingIndicatorResponse {
    public TypingIndicatorListenerModel typingIndicatorListenerModel;
    public String eventType;

    public SendTypingIndicatorResponse(TypingIndicatorListenerModel typingIndicatorListenerModel, String eventType) {
        this.typingIndicatorListenerModel = typingIndicatorListenerModel;
        this.eventType = eventType;
    }
}
