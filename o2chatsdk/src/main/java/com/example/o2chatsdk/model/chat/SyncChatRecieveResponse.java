package com.example.o2chatsdk.model.chat;

public class SyncChatRecieveResponse {
    public QueueConversationModel conversation;
    public String eventType;

    public SyncChatRecieveResponse(QueueConversationModel conversation, String eventType) {
        this.conversation = conversation;
        this.eventType = eventType;
    }
}
