package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.NewChatModel;

public class SendNewChatEvent {
    public NewChatModel sendMessageModel;
    public String eventType;

    public SendNewChatEvent(NewChatModel sendMessageModel, String eventType) {
        this.sendMessageModel = sendMessageModel;
        this.eventType = eventType;
    }
}
