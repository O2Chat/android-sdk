package com.arittek.o2chatsdk.Events.chatEvents;

import com.arittek.o2chatsdk.model.chat.ConversationsCountModel;

public class AllConversationCountEvent {
    public ConversationsCountModel conversationsCountModel;
    public String eventType;

    public AllConversationCountEvent(ConversationsCountModel conversationsCountModel, String eventType) {
        this.conversationsCountModel = conversationsCountModel;
        this.eventType = eventType;
    }
}
