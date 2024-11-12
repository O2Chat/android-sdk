package com.example.o2chatsdk.Events.appEvents;

import com.example.o2chatsdk.model.chat.ResolveFeedBackRequest;

public class CustomerFeedBackEvent {

    public String eventType;
    public ResolveFeedBackRequest resolveFeedBackRequest;

    public CustomerFeedBackEvent(String eventType, ResolveFeedBackRequest resolveFeedBackRequest) {
        this.eventType = eventType;
        this.resolveFeedBackRequest = resolveFeedBackRequest;
    }
}
