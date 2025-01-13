package com.arittek.signalrtestandroid.model.chat;

import java.util.ArrayList;

public class RecieveMessage {

    public long id ;
    public long toUserId ;
    public long fromUserId ;
    public long groupId ;
    public String groupName ;
    public String  tempChatId = "" ;
    public String sender ;
    public String content ;
    public String type ;
    public long forwardedTo ;
    public long customerId ;
    public String customerName ;
    public long agentId ;
    public String receiver ;
    public String timestamp ;
    public String conversationUid ;
    public boolean isFromWidget ;
    public boolean isSeen;
    public boolean isPrivate ;
    public long tiggerevent ;
    public String pageId ;
    public String caption ;
    public String pageName ;
    public String voiceDuration ;
    public ArrayList<FilesData> files;
    
}
