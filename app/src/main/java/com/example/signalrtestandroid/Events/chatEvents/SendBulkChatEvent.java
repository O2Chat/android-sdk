package com.example.signalrtestandroid.Events.chatEvents;

import com.example.signalrtestandroid.model.chat.NewChatModel;

import java.util.ArrayList;

public class SendBulkChatEvent {
    public ArrayList<NewChatModel> newChatModels;
    public String eventType;

    public SendBulkChatEvent(ArrayList<NewChatModel> newChatModels, String eventType) {
        this.newChatModels = newChatModels;
        this.eventType = eventType;
    }
}
