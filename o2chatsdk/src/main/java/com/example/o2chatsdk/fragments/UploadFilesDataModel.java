package com.example.o2chatsdk.fragments;

public class UploadFilesDataModel {

    private  String tempChatID ;
    private String file;
    private String fileName;
    private String contentType;
    private String conversationUId;
    private String caption;

    public UploadFilesDataModel() {
    }

    public UploadFilesDataModel(String tempChatID, String file, String fileName, String contentType, String conversationUId, String caption) {
        this.tempChatID = tempChatID;
        this.file = file;
        this.fileName = fileName;
        this.contentType = contentType;
        this.conversationUId = conversationUId;
        this.caption = caption;
    }

    public String getTempChatID() {
        return tempChatID;
    }

    public void setTempChatID(String tempChatID) {
        this.tempChatID = tempChatID;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getConversationUId() {
        return conversationUId;
    }

    public void setConversationUId(String conversationUId) {
        this.conversationUId = conversationUId;
    }
}
