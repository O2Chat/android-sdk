package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.EngageListener;

public class EngagedChatEvent {
    public EngageListener engageListener;
    public String eventType;

    public EngagedChatEvent(EngageListener engageListener, String eventType) {
        this.engageListener = engageListener;
        this.eventType = eventType;
    }
}
