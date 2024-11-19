package com.example.o2chatsdk.model.chat;

import com.google.gson.annotations.Expose;

public class GetTopicsByConversationModel {
    @Expose
    public int topicId;
    
    @Expose
    public String channelId;
    
    @Expose
    public int groupId;
    
    @Expose
    public String groupName;
    @Expose
    public int botId;
    @Expose
    public String name;
    @Expose
    public String message;
    @Expose
    public boolean isGroupAssigned;
    @Expose
    public int topicVisibility;
    @Expose
    public String customText;
    @Expose
    public String isTriggerBot;
    @Expose
    public int botSchedule;
    @Expose
    public boolean isActive;

}
