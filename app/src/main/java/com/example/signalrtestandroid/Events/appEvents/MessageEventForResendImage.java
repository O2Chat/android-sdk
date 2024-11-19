package com.example.signalrtestandroid.Events.appEvents;

import com.example.signalrtestandroid.fragments.UploadFilesDataModel;

import java.util.ArrayList;

public class MessageEventForResendImage {
    public String eventType;
    public String tempChatID;
    public String fileName;
    public String caption;
    public ArrayList<UploadFilesDataModel> multipartBodyPart;

    public MessageEventForResendImage(String eventType, ArrayList<UploadFilesDataModel> multipartBodyPart, String tempChatID, String fileName, String caption) {
        this.eventType = eventType;
        this.multipartBodyPart = multipartBodyPart;
        this.tempChatID = tempChatID;
        this.caption = caption;
        this.fileName = fileName;
    }
}
