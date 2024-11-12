package com.example.signalrtestandroid.model.chat;

import java.util.ArrayList;

public class ConversationByUID {
    public long id;
    public long customerId = 0;
    public String customerEmail = "";
    public String tempChatId = "" ;
    public long toUserId = 0;
    public long fromUserId = 0;
    public long groupId = 0;
    public long agentId = 0;
    public String content = "";
    public String timestamp = "";
    public String timestampMobile = "";
    public String sender = "";
    public String receiver = "";
    public String type = "";
    public String source = "";
    public String groupName = "";
    public long forwardedTo = 0;
    public String customerName = "";
    public String conversationUid = "";
    public int colorCode ;
    public boolean isPrivate;
    public boolean isFromWidget;
    public long tiggerevent = 0;
    public String conversationType = "text";
    public String pageId = "";
    public String pageName = "";
    public String fileLocalUri = "";
    public ArrayList<FilesData> files = new ArrayList<>();
    public boolean isDownloading;
    public boolean isUpdateStatus;
    public boolean isSeen;
    public String voiceDuration = "";
    public boolean isShowLocalFiles;
    public boolean isWelcome;
    public String caption = "" ;

    public boolean isNotNewChat;
    public boolean isRecieved;
    public boolean isFailed;
    public boolean isFeedback;
    public long topicId;
    public String topicMessage;
    public boolean isResolved;
    public String rating;
    public String feedback;
    public boolean isPlaying;

}
