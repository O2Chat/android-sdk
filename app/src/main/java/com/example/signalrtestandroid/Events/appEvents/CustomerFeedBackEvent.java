package com.example.signalrtestandroid.Events.appEvents;

import com.example.signalrtestandroid.model.chat.ResolveFeedBackRequest;

public class CustomerFeedBackEvent {

    public String eventType;
    public ResolveFeedBackRequest resolveFeedBackRequest;

    public CustomerFeedBackEvent(String eventType, ResolveFeedBackRequest resolveFeedBackRequest) {
        this.eventType = eventType;
        this.resolveFeedBackRequest = resolveFeedBackRequest;
    }
}
