package com.arittek.o2chatsdk.Events.appEvents;

public class FileDeleteEvent {
    public int position;
    public String eventType;

    public FileDeleteEvent(int position, String eventType) {
        this.position = position;
        this.eventType = eventType;
    }
}
