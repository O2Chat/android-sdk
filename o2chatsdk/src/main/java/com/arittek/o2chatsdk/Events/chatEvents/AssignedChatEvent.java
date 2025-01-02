package com.arittek.o2chatsdk.Events.chatEvents;

import com.arittek.o2chatsdk.model.chat.AssignChatListener;

public class AssignedChatEvent {
    public AssignChatListener assignChatListener;
    public String eventType;

    public AssignedChatEvent(AssignChatListener assignChatListener, String eventType) {
        this.assignChatListener = assignChatListener;
        this.eventType = eventType;
    }
}
