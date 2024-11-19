package com.example.signalrtestandroid.model.chat;

public class SyncChatRecieveResponse {
    public QueueConversationModel conversation;
    public String eventType;

    public SyncChatRecieveResponse(QueueConversationModel conversation, String eventType) {
        this.conversation = conversation;
        this.eventType = eventType;
    }
}
