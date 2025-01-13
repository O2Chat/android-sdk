package com.arittek.o2chatsdk.localDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "NewChatEntity")
public class NewChatEntity {

    @PrimaryKey(autoGenerate = true)
    public int column_id ;

    @ColumnInfo(name = "tempChatId")
    public String tempChatId;

    @ColumnInfo(name = "customerId")
    public long customerId;

    @ColumnInfo(name = "groupId")
    public long groupId;

    @ColumnInfo(name = "name")
    public String name ;

    @ColumnInfo(name = "contactNo")
    public String contactNo ;

    @ColumnInfo(name = "emailaddress")
    public String emailaddress;

    @ColumnInfo(name = "conversationType")
    public String conversationType;

    @ColumnInfo(name = "cnic")
    public String cnic;

    @ColumnInfo(name = "agentId")
    public long agentId ;

    @ColumnInfo(name = "message")
    public String message ;

    @ColumnInfo(name = "source")
    public String source;

    @ColumnInfo(name = "conversationUId")
    public String conversationUId;

    @ColumnInfo(name = "isResolved")
    public boolean isResolved;

    @ColumnInfo(name = "connectionId")
    public String connectionId ;

    @ColumnInfo(name = "isFromWidget")
    public boolean isFromWidget;

    @ColumnInfo(name = "ipAddress")
    public String ipAddress ;

    @ColumnInfo(name = "notifyMessage")
    public String notifyMessage ;

    @ColumnInfo(name = "channelid")
    public String channelid ;

    @ColumnInfo(name = "type")
    public String type ;

    @ColumnInfo(name = "documentOrignalname")
    public String documentOrignalname ;

    @ColumnInfo(name = "documentName")
    public String documentName ;

    @ColumnInfo(name = "documentType")
    public String documentType ;

    @ColumnInfo(name = "mobileToken")
    public String mobileToken ;

    @ColumnInfo(name = "timezone")
    public String timezone ;

    @ColumnInfo(name = "caption")
    public String caption ;

    @ColumnInfo(name = "isWelcome")
    public boolean isWelcome ;
    @ColumnInfo(name = "isRecieved")
    public boolean isRecieved ;
    @ColumnInfo(name = "fileUri")

    public String fileUri;
    @ColumnInfo(name = "timeStamp")
    public String timeStamp;

    @ColumnInfo(name = "topicId")
    public long topicId;

    @ColumnInfo(name = "topicMessage")
    public String topicMessage;
}
