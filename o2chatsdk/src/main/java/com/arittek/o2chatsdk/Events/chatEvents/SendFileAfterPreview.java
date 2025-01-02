package com.arittek.o2chatsdk.Events.chatEvents;

import com.arittek.o2chatsdk.model.chat.selectedFilePreviewData;

import java.util.ArrayList;

public class SendFileAfterPreview {

    public String fileMessage;
    public ArrayList<selectedFilePreviewData> receivedList;
    public String fileUri;
    public String fileType;
    public String fileName;
    public String eventType;


    public SendFileAfterPreview(String fileMessage, ArrayList<selectedFilePreviewData> filePreviewDataArrayList, String fileUri, String fileType, String fileName, String eventType) {
        this.fileMessage = fileMessage;
        this.receivedList = filePreviewDataArrayList;
        this.fileUri = fileUri;
        this.fileType = fileType;
        this.fileName = fileName;
        this.eventType = eventType;
    }
}
