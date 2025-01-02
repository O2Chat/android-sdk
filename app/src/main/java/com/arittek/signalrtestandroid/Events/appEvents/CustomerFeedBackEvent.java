package com.arittek.signalrtestandroid.Events.appEvents;

import com.arittek.signalrtestandroid.model.chat.ResolveFeedBackRequest;

public class CustomerFeedBackEvent {

    public String eventType;
    public ResolveFeedBackRequest resolveFeedBackRequest;

    public CustomerFeedBackEvent(String eventType, ResolveFeedBackRequest resolveFeedBackRequest) {
        this.eventType = eventType;
        this.resolveFeedBackRequest = resolveFeedBackRequest;
    }
}
