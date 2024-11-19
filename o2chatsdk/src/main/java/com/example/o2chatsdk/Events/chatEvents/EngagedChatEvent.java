package com.example.o2chatsdk.Events.chatEvents;

import com.example.o2chatsdk.model.chat.EngageListener;

public class EngagedChatEvent {
    public EngageListener engageListener;
    public String eventType;

    public EngagedChatEvent(EngageListener engageListener, String eventType) {
        this.engageListener = engageListener;
        this.eventType = eventType;
    }
}
