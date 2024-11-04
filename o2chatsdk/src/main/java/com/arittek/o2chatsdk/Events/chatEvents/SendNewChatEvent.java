package com.arittek.o2chatsdk.Events.chatEvents;

import com.arittek.o2chatsdk.model.chat.NewChatModel;

public class SendNewChatEvent {
    public NewChatModel sendMessageModel;
    public String eventType;

    public SendNewChatEvent(NewChatModel sendMessageModel, String eventType) {
        this.sendMessageModel = sendMessageModel;
        this.eventType = eventType;
    }
}
