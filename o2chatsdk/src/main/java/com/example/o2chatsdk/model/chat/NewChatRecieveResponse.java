package com.example.o2chatsdk.model.chat;

public class NewChatRecieveResponse {
    public Conversation conversation;
    public String eventType;

    public NewChatRecieveResponse(Conversation conversation, String eventType) {
        this.conversation = conversation;
        this.eventType = eventType;
    }
}
