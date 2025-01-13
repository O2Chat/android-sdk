package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.SendMessageModel;

public class SendChatEvent {
    public SendMessageModel sendMessageModel;
    public String eventType;

    public SendChatEvent(SendMessageModel sendMessageModel, String eventType) {
        this.sendMessageModel = sendMessageModel;
        this.eventType = eventType;
    }
}
