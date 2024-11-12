package com.arittek.o2chatsdk.localDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "conversationDetailFile")
public class ConversationDetailFile {
    @PrimaryKey(autoGenerate = true)
    public int conversationFilesDataID;

    @ColumnInfo(name = "customerId")
    public long customerId = 0;

    @ColumnInfo(name = "messagedId")
    public long messagedId;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "conversationUid")
    public String conversationUid;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "icon")
    public String icon;

    @ColumnInfo(name = "documentName")
    public String documentName;

    @ColumnInfo(name = "tempChatId")
    public String tempChatId;

    public int getConversationFilesDataID() {
        return conversationFilesDataID;
    }

    public void setConversationFilesDataID(int conversationFilesDataID) {
        this.conversationFilesDataID = conversationFilesDataID;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getConversationUid() {
        return conversationUid;
    }

    public void setConversationUid(String conversationUid) {
        this.conversationUid = conversationUid;
    }

    public long getmessagedId() {
        return messagedId;
    }

    public void setmessagedId(long messagedId) {
        this.messagedId = messagedId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }


    public String getTempChatId() {
        return tempChatId;
    }

    public void setTempChatId(String tempChatId) {
        this.tempChatId = tempChatId;
    }
}
