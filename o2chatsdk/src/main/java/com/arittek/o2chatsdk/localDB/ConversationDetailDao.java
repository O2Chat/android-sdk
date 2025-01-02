package com.arittek.o2chatsdk.localDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arittek.o2chatsdk.localDB.entity.ConversationDetail;
import com.arittek.o2chatsdk.localDB.entity.ConversationDetailFile;
import com.arittek.o2chatsdk.localDB.entity.NewChatEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface ConversationDetailDao {

    @Query("SELECT * FROM ConversationDetail WHERE conversationUid = :conversationUid ORDER BY createdOn DESC")
    List<ConversationDetail> getAllConversation(String conversationUid);

    @Query("SELECT * FROM conversationdetail WHERE conversationUid= :conversationUid ORDER BY createdOn DESC LIMIT 1")
    List<ConversationDetail> getLastConversationItem(String conversationUid);

    @Query("SELECT * FROM ConversationDetail WHERE conversationUid=:conversationUUID AND id=:conversationId AND tempChatId=:tempChatId")
    List<ConversationDetail> getAllConversationWhereIDIsZero(long conversationId,String tempChatId,String conversationUUID);

    @Query("SELECT * FROM ConversationDetail WHERE conversationUid=:conversationUUID AND id=:conversationId AND type=:type")
    List<ConversationDetail> getAllImageConversationWhereIDIsZero(long conversationId,String type,String conversationUUID);

    @Query("SELECT * FROM ConversationDetailFile WHERE conversationUid = :conversationUid AND messagedId= :messagedId")
    List<ConversationDetailFile> getConversationFilesData(String conversationUid,long messagedId);

    @Query("SELECT * FROM ConversationDetailFile WHERE conversationUid = :conversationUid AND messagedId= :messagedId AND tempChatId=:tempChatId")
    ConversationDetailFile getConversationFilesDataByTempId(String tempChatId,String conversationUid,long messagedId);

    @Query("SELECT * FROM ConversationDetail WHERE tempChatId=:tempChatId AND id =0")
    ConversationDetail getConversationWelcomeMsgIdIsZeroTempID(String tempChatId);


    @Query("SELECT * FROM ConversationDetailFile WHERE conversationUid = :conversationUid AND tempChatId= :tempChatId")
    List<ConversationDetailFile> getConversationFilesData(String conversationUid,String tempChatId);

    @Query("SELECT * FROM NewChatEntity WHERE isRecieved = :isRecieved  AND conversationUId= :conversationUId")
    List<NewChatEntity> getAllUnSendChat(String conversationUId,boolean isRecieved);

    @Query("SELECT EXISTS (SELECT 1 FROM ConversationDetail WHERE id = :id)")
    boolean exists(long id);

    @Query("SELECT EXISTS (SELECT 1 FROM ConversationDetail WHERE tempChatId = :tempChatId)")
    boolean exists(String tempChatId);

    @Query("SELECT EXISTS (SELECT 1 FROM ConversationDetail WHERE tempChatId = :tempChatId AND id= :id)")
    boolean exists(String tempChatId,long id);

    @Query("SELECT EXISTS (SELECT 1 FROM conversationDetailFile WHERE messagedId = :id)")
    boolean existsFile(long id);

    @Query("SELECT EXISTS (SELECT 1 FROM conversationDetailFile WHERE tempChatId = :tempChatId)")
    boolean existsFile(String tempChatId);
    @Query("UPDATE conversationdetail SET id = :id" +
            ",customerId = :customerId," +
            "customerEmail = :customerEmail" +
            ",tempChatId = :tempChatId," +
            "toUserId = :toUserId," +
            "fromUserId = :fromUserId," +
            "groupId = :groupId," +
            "isSeen = :isSeen," +
            "agentId =:agentId," +
            "content = :content," +
            "timestamp= :timestamp," +
            "sender= :sender," +
            "caption= :caption" +
            ",receiver=:receiver," +
            "type=:type," +
            "source=:source," +
            "groupName=:groupName," +
            "forwardedTo=:forwardedTo" +
            ",customerName=:customerName," +
            "conversationUid=:conversationUid," +
            "isPrivate=:isPrivate" +
            ",isFromWidget=:isFromWidget," +
            "tiggerevent=:tiggerevent," +
            "conversationType=:conversationType," +
            "pageId=:pageId," +
            "pageName=:pageName," +
            "fileLocalUri=:fileLocalUri" +
            ",isDownloading=:isDownloading," +
            "isUpdateStatus=:isUpdateStatus" +
            ",isShowLocalFiles=:isShowLocalFiles," +
            "isWelcome=:isWelcome," +
            "isNotNewChat=:isNotNewChat," +
            "isFailed=:isFailed ," +
            "createdOn=:createdOn," +
            "rating=:isRating," +
            "feedback=:isRatingFeedback," +
            "voiceDuration=:voiceduration, "+
            "isResolved=:isResolved WHERE id = :id")

    void updateConversationByID(boolean isSeen, long id, long customerId,
                                String customerEmail, String tempChatId, long toUserId,
                                long fromUserId, long groupId, long agentId, String content , String timestamp,
                                String sender, String caption, String receiver, String type, String source,
                                String groupName, long forwardedTo, String customerName, String conversationUid,
                                boolean isPrivate, boolean isFromWidget, long tiggerevent, String conversationType,
                                String pageId, String pageName, String fileLocalUri, boolean isDownloading, boolean isUpdateStatus,
                                boolean isShowLocalFiles, boolean isWelcome, boolean isNotNewChat, boolean isFailed, Date createdOn ,
                                boolean isResolved,String isRating,String isRatingFeedback,String voiceduration);

    @Query("UPDATE conversationdetail SET id = :id" +
            ",customerId = :customerId," +
            "customerEmail = :customerEmail" +
            ",tempChatId = :tempChatId," +
            "toUserId = :toUserId," +
            "fromUserId = :fromUserId," +
            "groupId = :groupId," +
            "isSeen = :isSeen," +
            "agentId =:agentId," +
            "content = :content," +
            "timestamp= :timestamp," +
            "sender= :sender," +
            "caption= :caption" +
            ",receiver=:receiver," +
            "type=:type," +
            "source=:source," +
            "groupName=:groupName," +
            "forwardedTo=:forwardedTo" +
            ",customerName=:customerName," +
            "conversationUid=:conversationUid," +
            "isPrivate=:isPrivate" +
            ",isFromWidget=:isFromWidget," +
            "tiggerevent=:tiggerevent," +
            "conversationType=:conversationType," +
            "pageId=:pageId," +
            "pageName=:pageName," +
            "fileLocalUri=:fileLocalUri" +
            ",isDownloading=:isDownloading," +
            "isUpdateStatus=:isUpdateStatus" +
            ",isShowLocalFiles=:isShowLocalFiles," +
            "isWelcome=:isWelcome," +
            "isNotNewChat=:isNotNewChat,"+
            "isFailed=:isFailed,"+
            "createdOn=:createdOn,"+
            "isResolved=:isResolved,"+
            "rating=:rating,"+
            "voiceDuration=:voiceduration,"+
            "feedback=:feedback  WHERE tempChatId = :tempChatId")

    void updateConversationByTempChatID(boolean isSeen, long id,long customerId,
                                String customerEmail,String tempChatId,long toUserId,
                                long fromUserId,long groupId,long agentId, String content ,String timestamp,
                                String sender,String caption,String receiver,String type,String source,
                                String groupName, long forwardedTo,String customerName,String conversationUid,
                                boolean isPrivate,boolean isFromWidget,long tiggerevent, String conversationType,
                                String pageId,String pageName,String fileLocalUri,boolean isDownloading,boolean isUpdateStatus,
                                boolean isShowLocalFiles,boolean isWelcome,boolean isNotNewChat,boolean isFailed,Date createdOn,
                                boolean isResolved,String rating,String feedback,String voiceduration);

    @Query("UPDATE NewChatEntity SET isRecieved = :isRecieved WHERE tempChatId = :tempChatId")
    void updateNewChatByTempChatID(boolean isRecieved,String tempChatId);

    @Query("UPDATE ConversationDetailFile SET messagedId = :id WHERE tempChatId = :tempChatId")
    void updateConversationFileData(long id,String tempChatId);

    @Query("UPDATE ConversationDetailFile SET type = :type,url = :url,documentName = :documentName,conversationUid = :conversationUid,customerId = :customerId,tempChatId=:tempChatId WHERE messagedId = :id")
    void updateConversationFileByID(long id,String url,String type, String documentName,String conversationUid,long customerId,String tempChatId);

    @Query("UPDATE ConversationDetailFile SET messagedId=:id,type = :type,url = :url,documentName = :documentName,conversationUid = :conversationUid,customerId = :customerId,tempChatId=:tempChatId WHERE tempChatId = :tempChatId")
    void updateConversationFileByTempChatID(long id,String url,String type, String documentName,String conversationUid,long customerId,String tempChatId);

    @Query("DELETE FROM ConversationDetail WHERE tempChatId=:tempChatId")
    void deleteWelcomeMsgByTempChatID(String tempChatId);

    @Query("DELETE FROM NewChatEntity WHERE tempChatId=:tempChatId")
    void deleteNewChatByTempChatID(String tempChatId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNewChat(NewChatEntity newChatEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertConversation(ConversationDetail conversationDetail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertConversationFileDate(ConversationDetailFile conversationDetailFile);

    @Query("DELETE FROM conversationDetail")
    void deleteAllConversation();

    @Query("DELETE FROM newchatentity")
    void deleteAllNewChat();

    @Query("DELETE FROM conversationDetailFile")
    void deleteConversationFilesData();
}
