package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.EngageListener;

public class EngagedChatEvent {
    public EngageListener engageListener;
    public String eventType;

    public EngagedChatEvent(EngageListener engageListener, String eventType) {
        this.engageListener = engageListener;
        this.eventType = eventType;
    }
}
