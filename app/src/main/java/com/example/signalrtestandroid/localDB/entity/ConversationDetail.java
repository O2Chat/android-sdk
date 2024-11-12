package com.example.signalrtestandroid.localDB.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.signalrtestandroid.commons.DateConverter;

import java.util.Date;

@Entity(tableName = "ConversationDetail")
public class ConversationDetail {

    @PrimaryKey(autoGenerate = true)
    public int column_id ;
    @ColumnInfo(name = "id")

    public long id;
    @ColumnInfo(name = "customerId")
    public long customerId = 0;
    @ColumnInfo(name = "customerEmail")
    public String customerEmail = "";
    @ColumnInfo(name = "tempChatId")
    public String tempChatId = "" ;
    @ColumnInfo(name = "toUserId")
    public long toUserId = 0;
    @ColumnInfo(name = "fromUserId")
    public long fromUserId = 0;
    @ColumnInfo(name = "groupId")
    public long groupId = 0;
    @ColumnInfo(name = "agentId")
    public long agentId = 0;
    @ColumnInfo(name = "content")
    public String content = "";
    @ColumnInfo(name = "timestamp")
    public String timestamp = "";
    @ColumnInfo(name = "sender")
    public String sender = "";
    @ColumnInfo(name = "caption")
    public String caption = "";
    @ColumnInfo(name = "receiver")
    public String receiver = "";
    @ColumnInfo(name = "type")
    public String type = "";
    @ColumnInfo(name = "source")
    public String source = "";
    @ColumnInfo(name = "groupName")
    public String groupName = "";
    @ColumnInfo(name = "forwardedTo")
    public long forwardedTo = 0;
    @ColumnInfo(name = "customerName")
    public String customerName = "";
    @ColumnInfo(name = "conversationUid")
    public String conversationUid = "";
    @ColumnInfo(name = "isPrivate")
    public boolean isPrivate;
    @ColumnInfo(name = "isFromWidget")
    public boolean isFromWidget;
    @ColumnInfo(name = "tiggerevent")
    public long tiggerevent = 0;
    @ColumnInfo(name = "conversationType")
    public String conversationType = "text";
    @ColumnInfo(name = "pageId")
    public String pageId = "";
    @ColumnInfo(name = "pageName")
    public String pageName = "";
    @ColumnInfo(name = "fileLocalUri")
    public String fileLocalUri = "";
    //public ArrayList<FilesData> files = new ArrayList<>();
    @ColumnInfo(name = "isDownloading")
    public boolean isDownloading;
    @ColumnInfo(name = "isUpdateStatus")
    public boolean isUpdateStatus;
    @ColumnInfo(name = "isSeen")
    public boolean isSeen;
    @ColumnInfo(name = "isShowLocalFiles")
    public boolean isShowLocalFiles;
    @ColumnInfo(name = "isWelcome")
    public boolean isWelcome;
    @ColumnInfo(name = "isNotNewChat")
    public boolean isNotNewChat;

    @ColumnInfo(name = "isRecieved")
    public boolean isRecieved;

    @ColumnInfo(name = "isFailed")
    public boolean isFailed;

    @ColumnInfo(name = "isResolved")
    public boolean isResolved = false;

    @ColumnInfo(name = "rating")
    public String rating;

    @ColumnInfo(name = "feedback")
    public String feedback;

    public String getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(String voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    @ColumnInfo(name = "voiceDuration")
    private String voiceDuration;


    public String getIsRatingFeedback() {
        return feedback;
    }

    public void setIsRatingFeedback(String feedback) {
        this.feedback = feedback;
    }

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "createdOn")
    public Date createdOn;

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public int getColumn_id() {
        return column_id;
    }

    public void setColumn_id(int column_id) {
        this.column_id = column_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getTempChatId() {
        return tempChatId;
    }

    public void setTempChatId(String tempChatId) {
        this.tempChatId = tempChatId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getForwardedTo() {
        return forwardedTo;
    }

    public void setForwardedTo(long forwardedTo) {
        this.forwardedTo = forwardedTo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getConversationUid() {
        return conversationUid;
    }

    public void setConversationUid(String conversationUid) {
        this.conversationUid = conversationUid;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isFromWidget() {
        return isFromWidget;
    }

    public void setFromWidget(boolean fromWidget) {
        isFromWidget = fromWidget;
    }

    public long getTiggerevent() {
        return tiggerevent;
    }

    public void setTiggerevent(long tiggerevent) {
        this.tiggerevent = tiggerevent;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getFileLocalUri() {
        return fileLocalUri;
    }

    public void setFileLocalUri(String fileLocalUri) {
        this.fileLocalUri = fileLocalUri;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public boolean isUpdateStatus() {
        return isUpdateStatus;
    }

    public void setUpdateStatus(boolean updateStatus) {
        isUpdateStatus = updateStatus;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public boolean isShowLocalFiles() {
        return isShowLocalFiles;
    }

    public void setShowLocalFiles(boolean showLocalFiles) {
        isShowLocalFiles = showLocalFiles;
    }

    public boolean isWelcome() {
        return isWelcome;
    }

    public void setWelcome(boolean welcome) {
        isWelcome = welcome;
    }

    public boolean isNotNewChat() {
        return isNotNewChat;
    }

    public void setNotNewChat(boolean notNewChat) {
        isNotNewChat = notNewChat;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isRecieved() {
        return isRecieved;
    }

    public void setRecieved(boolean recieved) {
        isRecieved = recieved;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }

    public boolean isResolved() {
        return isResolved;
    }

    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public String getIsRating() {
        return rating;
    }

    public void setIsRating(String rating) {
        this.rating = rating;
    }


}
