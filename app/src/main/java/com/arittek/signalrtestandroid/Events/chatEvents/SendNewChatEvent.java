package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.NewChatModel;

public class SendNewChatEvent {
    public NewChatModel sendMessageModel;
    public String eventType;

    public SendNewChatEvent(NewChatModel sendMessageModel, String eventType) {
        this.sendMessageModel = sendMessageModel;
        this.eventType = eventType;
    }
}
