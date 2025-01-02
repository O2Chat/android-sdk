package com.arittek.signalrtestandroid.Events.chatEvents;

public class FileResendEvent {

    public String message;
    public String tempChatId;
    public String conversationUUID;
    public String eventType;

    public FileResendEvent(String message,String tempChatId,String conversationUUID, String eventType) {
        this.message = message;
        this.tempChatId = tempChatId;
        this.conversationUUID = conversationUUID;
        this.eventType = eventType;
    }
}
