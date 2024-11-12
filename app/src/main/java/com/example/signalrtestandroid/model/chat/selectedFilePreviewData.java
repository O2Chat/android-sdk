package com.example.signalrtestandroid.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class selectedFilePreviewData implements Parcelable {


    String fileType = "";
    String fileUri = "";
    String fileName = "";
    String fileSize = "";
    String message = "";
    String tempChatID = "";
    String voiceDuration = "";

    public String getTempChatID() {
        return tempChatID;
    }

    public void setTempChatID(String tempChatID) {
        this.tempChatID = tempChatID;
    }

    public String getFileType() {
        return fileType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileUri() {
        return fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(String voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public selectedFilePreviewData(String fileType, String fileUri, String fileName, String fileSize, String tempChatID, String message, String voiceDuration) {
        this.fileType = fileType;
        this.fileUri = fileUri;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.tempChatID = tempChatID;
        this.message = message;
        this.voiceDuration = voiceDuration;
    }

    public selectedFilePreviewData(String fileType, String fileUri, String fileName, String fileSize, String tempChatID, String message) {
        this.fileType = fileType;
        this.fileUri = fileUri;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.tempChatID = tempChatID;
        this.message = message;
    }

    // Constructor, getters, and setters

    // Implement Parcelable methods (writeToParcel, describeContents)

    public static final Parcelable.Creator<selectedFilePreviewData> CREATOR = new Parcelable.Creator<selectedFilePreviewData>() {
        @Override
        public selectedFilePreviewData createFromParcel(Parcel in) {
            return new selectedFilePreviewData(in);
        }

        @Override
        public selectedFilePreviewData[] newArray(int size) {
            return new selectedFilePreviewData[size];
        }
    };

    // Constructor for Parcelable
    private selectedFilePreviewData(Parcel in) {
        fileType = in.readString();
        fileUri = in.readString();
        fileName = in.readString();
        fileSize = in.readString();
        tempChatID = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileType);
        dest.writeString(fileUri);
        dest.writeString(fileName);
        dest.writeString(fileSize);
        dest.writeString(tempChatID);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}