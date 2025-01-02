package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.AssignChatListener;

public class AssignedChatEvent {
    public AssignChatListener assignChatListener;
    public String eventType;

    public AssignedChatEvent(AssignChatListener assignChatListener, String eventType) {
        this.assignChatListener = assignChatListener;
        this.eventType = eventType;
    }
}
