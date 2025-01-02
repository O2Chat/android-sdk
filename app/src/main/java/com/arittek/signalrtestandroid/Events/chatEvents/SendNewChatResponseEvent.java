package com.arittek.signalrtestandroid.Events.chatEvents;

import com.arittek.signalrtestandroid.model.chat.ConversationByUID;

import java.util.ArrayList;

public class SendNewChatResponseEvent {
    public ArrayList<ConversationByUID> sendMessageModel;
    public String eventType;

    public SendNewChatResponseEvent(ArrayList<ConversationByUID>  sendMessageModel, String eventType) {
        this.sendMessageModel = sendMessageModel;
        this.eventType = eventType;
    }
}