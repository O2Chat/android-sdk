package com.example.o2chatsdk.activities;

import static com.example.o2chatsdk.commons.Constants.COLOR_CODE_KEY;
import static com.example.o2chatsdk.commons.Constants.NAME_KEY;
import static com.example.o2chatsdk.commons.Constants.NAME_LETTER_KEY;
import static com.example.o2chatsdk.commons.Constants.SOURCE_KEY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.o2chatsdk.R;
import com.example.o2chatsdk.commons.O2ChatConfig;
import com.bumptech.glide.Glide;
import com.example.o2chatsdk.Events.appEvents.CustomerFeedBackEvent;
import com.example.o2chatsdk.Events.appEvents.MessageEvent;
import com.example.o2chatsdk.Events.appEvents.MessageEventForResendImage;
import com.example.o2chatsdk.Events.appEvents.ReadReceiptUpdateEvent;
import com.example.o2chatsdk.Events.appEvents.SendReceiptEvent;
import com.example.o2chatsdk.Events.chatEvents.ConversationStatusListenerEvent;
import com.example.o2chatsdk.Events.chatEvents.FileResendEvent;
import com.example.o2chatsdk.Events.chatEvents.ReceiveMessageEvent;
import com.example.o2chatsdk.Events.chatEvents.SendBulkChatEvent;
import com.example.o2chatsdk.Events.chatEvents.SendChatEvent;
import com.example.o2chatsdk.Events.chatEvents.SendFileAfterPreview;
import com.example.o2chatsdk.Events.chatEvents.SendNewChatEvent;
import com.example.o2chatsdk.Events.chatEvents.SendNewChatResponseEvent;
import com.example.o2chatsdk.Events.chatEvents.SendTypingIndicatorResponse;

import com.example.o2chatsdk.baseClasses.BaseActivity;
import com.example.o2chatsdk.commons.ConnectionService;
import com.example.o2chatsdk.commons.Constants;
import com.example.o2chatsdk.commons.FileUtil;
import com.example.o2chatsdk.commons.ImageCompressor;
import com.example.o2chatsdk.commons.PermissionHelper;
import com.example.o2chatsdk.commons.SignalRHelper;
import com.example.o2chatsdk.commons.Common;
import com.example.o2chatsdk.commons.Utils;
import com.example.o2chatsdk.fragments.ConversationsDetailFragment;
import com.example.o2chatsdk.fragments.ConversationsFragment;
import com.example.o2chatsdk.fragments.UploadFilesDataModel;
import com.example.o2chatsdk.fragments.ReLoadConversationEvent;
import com.example.o2chatsdk.localDB.AppDatabase;
import com.example.o2chatsdk.localDB.entity.ConversationDetail;
import com.example.o2chatsdk.localDB.entity.ConversationDetailFile;
import com.example.o2chatsdk.model.chat.BulkConversationModel;
import com.example.o2chatsdk.model.chat.Conversation;
import com.example.o2chatsdk.model.chat.ConversationByUID;
import com.example.o2chatsdk.model.chat.ConversationStatusListenerDataModel;
import com.example.o2chatsdk.model.chat.NewChatRecieveResponse;
import com.example.o2chatsdk.model.chat.OnErrorData;
import com.example.o2chatsdk.model.chat.OrganizationModelData;
import com.example.o2chatsdk.model.chat.QueueConversationModel;
import com.example.o2chatsdk.model.chat.ReadReceiptRequest;
import com.example.o2chatsdk.model.chat.RecieveMessage;
import com.example.o2chatsdk.model.chat.SyncChatRecieveResponse;
import com.example.o2chatsdk.model.chat.TypingIndicatorListenerModel;
import com.example.o2chatsdk.retrofit.ApiClient;
import com.example.o2chatsdk.retrofit.WebResponse;


import com.google.gson.Gson;
import com.microsoft.signalr.HubConnection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityChat extends BaseActivity implements ConnectionService.ConnectionServiceCallback {

    public static boolean isPause = false;
    public static boolean isLoadChat = false;
    public boolean isBulkMessageCalled = false;
    public static boolean isComingFromChat = false;
    Timer timer = null;
    Common common;
    O2ChatConfig o2ChatConfig;

    public boolean isAlreadyConnected = true;
    public boolean isSignalRConnected;
    public boolean isReconnecting = true ;
    SignalRHelper signalRHelper;
    HubConnection hubConnection;
    int agentId;
    public String channelId ;
    String isSuperAdmin = "";
    String selectMenu = "0";
    RelativeLayout menuClick,action_bar,rlUserProfile,layoutAllAssignCountMain,layoutAssignToMeCountMain,layoutResolvedCountMain,layoutNewCountMain ;

    private Context mContext;
    private LinearLayout LayoutReconnecting;
    private ImageView icSource;
    private TextView txtStatus;

    private String notificationId = "";
    Intent intent1 = null;
    AppDatabase  dbchat;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        EmojiManager.install(new GoogleEmojiProvider());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.setTurnScreenOn(true);
        } else {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        mContext = MainActivityChat.this;
        dbchat = AppDatabase.getAppDatabase(mContext.getApplicationContext());
        intent1 = getIntent();
        common = new Common();
        o2ChatConfig = new O2ChatConfig();


        common.saveBaseUrlChat(mContext,Constants.ChatHubUrl);
        ReplaceFragment(new ConversationsDetailFragment(), false, new Bundle(), true);
//      getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        //action_bar = findViewById(R.id.action_bar);
        menuClick = findViewById(R.id.menuClick);
        LayoutReconnecting = findViewById(R.id.LayoutReconnecting);
        icSource = findViewById(R.id.icSource);
        txtStatus = findViewById(R.id.txtStatus);
        Glide.with(mContext).load(R.drawable.connecting).into(icSource);
        channelId  = o2ChatConfig.getChannelID(mContext); //common.getChannelID(mContext);

        signalRHelper = new SignalRHelper();
//      getAccessTokenByChannelId("f26a33d9-5b2e-4227-a456-eab45924a1d3");
//      O2ChatConfig config = O2ChatConfig.getInstance(MainActivityChat.this);

        getAccessTokenByChannelId(channelId);

        if (!common.getUserId(this).isEmpty()) {
            agentId = Integer.parseInt(common.getUserId(this));
        }

        if (!common.getIsSuperAdmin(this).isEmpty()) {
            isSuperAdmin = common.getIsSuperAdmin(this);
        }

        if (!common.getIsSuperAdmin(this).isEmpty()) {
            if(intent1.getStringExtra(Constants.CONVERSATION_BY_UID_KEY)!=null && intent1.getExtras().containsKey(Constants.CONVERSATION_BY_UID_KEY)){
                notificationId = intent1.getStringExtra(Constants.CONVERSATION_BY_UID_KEY);
            }
        }

        common.saveNotificationCount(getApplicationContext(),"");

         startConnectionCheckService();
         if (Build.VERSION.SDK_INT >= 32) {
            notificationPermission();
         }
        if (common!=null && mContext!=null){
            common.setIsConversationOpen(mContext,true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
                notificationId = intent.getStringExtra(Constants.CONVERSATION_BY_UID_KEY);
                //switch to notification screen when notificationCategoryId is empty
            if (notificationId != null && !notificationId.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString(NAME_KEY, common.getConversationData(mContext).customerName);
                bundle.putString(NAME_LETTER_KEY, common.firstCharactorCapital(common.getConversationData(mContext).customerName));
                bundle.putString(SOURCE_KEY, common.getConversationData(mContext).source);
                bundle.putString(COLOR_CODE_KEY, "000000");
                bundle.putString(Constants.CONVERSATION_BY_UID_KEY, notificationId);
                bundle.putString(Constants.CHANNEL_ID, channelId);
                ReplaceFragmentWithoutClearBackStack(new ConversationsDetailFragment(), true, bundle, true);
            }
        }
    }

    private void notificationPermission() {

        PermissionHelper.grantPermission(this, Manifest.permission.POST_NOTIFICATIONS, new PermissionHelper.PermissionInterface() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                ReplaceFragment(new ConversationsFragment(), false, null, true);
            }
        });
    }

    public void startConnectionCheckService(){
        Intent intent = new Intent(this, ConnectionService.class);
        // Interval in seconds
        intent.putExtra(ConnectionService.TAG_INTERVAL, 3);
        // URL to ping
        intent.putExtra(ConnectionService.TAG_URL_PING, "http://www.google.com");
        // Name of the class that is calling this service
        intent.putExtra(ConnectionService.TAG_ACTIVITY_NAME, this.getClass().getName());
        // Starts the service
        startService(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
              //   mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        if (isLoadChat && !isPause){
            isLoadChat = false;
            EventBus.getDefault().post(new ReLoadConversationEvent("ReloadConversationWhenConnect"));
        }
        if(isPause){
            isPause = false;
            EventBus.getDefault().post(new MessageEvent( "LoadLocalChatWhenActivityResume"));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReLoadConversationEvent event) {
        if(event!=null) {
             if (event.eventType.equalsIgnoreCase("Reconnecting")){
                if(isReconnecting){
                    isReconnecting = false;
                    txtStatus.setText("Reconnecting..");
                    LayoutReconnecting.setVisibility(View.VISIBLE);
                    isAlreadyConnected = true;
                    EventBus.getDefault().post(new MessageEvent("StartSyncResendMessage"));

                }
            }
        }
    }


    public void stopIntervalOfConnection(){
        isSignalRConnected = true;
        if(timer!=null){
            timer.cancel();
        }
        runOnUiThread(() -> {
            if (!isReconnecting){
                if (mContext!=null){
                    isReconnecting = true;
                    txtStatus.setText("Connected");
                    checkQueueConversation(common.getCustomerID(getApplicationContext()),common.getLastConversationId(getApplicationContext()));
                    Glide.with(getApplicationContext()).load(R.drawable.wifi).into(icSource);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        //Do something after 100ms
                        LayoutReconnecting.setVisibility(View.GONE);
                        isAlreadyConnected = true;

                    }, 1000);
                }
            }
        });

    }

    public void setHubConnectionListeners(HubConnection hubConnection) {

        // list of messages
        hubConnection.on("ReceiveMessage", (message) -> {
                runOnUiThread(() -> {
                    if (message != null) {
                        common.saveLastConversationId(mContext,message.id);
                        Log.d("ReceiveMessage","-------->"+new Gson().toJson(message.files));
                        if (!message.tempChatId.isEmpty()){
                            saveAndUpdateTempItemToDB(false,common.getConversationFromReceiveMsg(message));
                        }else{
                            saveAndUpdateTempItemToDB(true,common.getConversationFromReceiveMsg(message));
                        }
                        EventBus.getDefault().post(new ReceiveMessageEvent(message, "ReceiveMessage"));
                    }
                });
        }, RecieveMessage.class);

        hubConnection.on("NewChatReceiver", (message) -> {
                runOnUiThread(() -> {
                    if (message != null) {
                        common.saveCustomerId(getApplicationContext(),message.customerId);
                        EventBus.getDefault().post(new NewChatRecieveResponse(message, "CallSendPrivateAfterNewChat"));
                    }
                });
            }, Conversation.class);

        hubConnection.on("SyncMessagesListener", (message) -> {
            runOnUiThread(() -> {
                if (message != null) {
                    EventBus.getDefault().post(new SyncChatRecieveResponse(message, "LoadSyncMessagesToLocalDB"));
                }
            });
        }, QueueConversationModel.class);

        // list of messages
        hubConnection.on("onError", (message) -> {
            runOnUiThread(() -> {
                if (message != null) {
                    EventBus.getDefault().post(new OnErrorData(message, "OnErrorMessage"));
                }
            });
        }, String.class);

        hubConnection.on("ForceLogoutListener", (message) -> {
            runOnUiThread(() -> {
                if (message != null) {
                    Utils.createDialogLogout(MainActivityChat.this, message,common);
                }
            });
        }, String.class);

        hubConnection.on("ReadReceiptListener", (message) -> {
            runOnUiThread(() -> {
                if (message != null) {
                    EventBus.getDefault().post(new ReadReceiptUpdateEvent( "UpdateReceiptEvent",message));
                }
            });
        }, ReadReceiptRequest.class);

        hubConnection.on("TypingIndicatorListener", (message) -> {
            runOnUiThread(() -> {
                if (message != null) {
                    EventBus.getDefault().post(new SendTypingIndicatorResponse(message, "TypingIndicatorListenerResponse"));
                }
            });
        }, TypingIndicatorListenerModel.class);

        hubConnection.on("BulkNewChatListener", (message) -> {
            runOnUiThread(() -> {
                if (message != null) {
                    EventBus.getDefault().post(new SendNewChatResponseEvent(message.conversationVMList, "BulkMessageResponseListener"));
                }
            });
        }, BulkConversationModel.class);

        // list of messages
        hubConnection.on("ConversationStatusListener", (message) -> {
            runOnUiThread(() -> {
                if (message != null) {
                    EventBus.getDefault().post(new ConversationStatusListenerEvent(message, "ConversationStatusListener"));
                }
            });
        }, ConversationStatusListenerDataModel.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendBulkChatEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("SendBulkChatMessages")){
                try {
                    if(hubConnection!=null) {
                         hubConnection.send("BulkNewChat",event.newChatModels);
                    }
                } catch (Exception e) {
                    if(e.getMessage().equalsIgnoreCase("The 'invoke' method cannot be called if the connection is not active.") || e.getMessage().equalsIgnoreCase("The 'send' method cannot be called if the connection is not active")|| e.getMessage().contains("is not active")){
                        if (hubConnection != null && signalRHelper != null) {
                            boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                            if(isSignalRConnect){
                                stopIntervalOfConnection();
                            }
                        }
                    }
                }
            }
        }
    }

    public void checkQueueConversation(long customerId,long conversationId){
        try {
            if(hubConnection!=null){
                hubConnection.send("SyncMessages",customerId,conversationId);
            }
        } catch (Exception e) {
            if(e.getMessage().equalsIgnoreCase("The 'invoke' method cannot be called if the connection is not active.") || e.getMessage().equalsIgnoreCase("The 'send' method cannot be called if the connection is not active")|| e.getMessage().contains("is not active")){
                if (hubConnection != null && signalRHelper != null) {
                    boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                    if(isSignalRConnect){
                        stopIntervalOfConnection();
                    }
                }
            }
        }
    }

    public void saveAndUpdateTempItemToDB(boolean isUpdateById,ConversationByUID conversationByUID1){

        ConversationDetail conversationData = new ConversationDetail();
        conversationData.setId(conversationByUID1.id);
        conversationData.setCustomerId(conversationByUID1.customerId);
        conversationData.setConversationUid(conversationByUID1.conversationUid);
        conversationData.setCustomerEmail(conversationByUID1.customerEmail);
        conversationData.setTempChatId(conversationByUID1.tempChatId);
        conversationData.setToUserId(conversationByUID1.toUserId);
        conversationData.setGroupId(conversationByUID1.groupId);
        conversationData.setAgentId(conversationByUID1.agentId);
        conversationData.setContent(conversationByUID1.content);
        conversationData.setTimestamp(conversationByUID1.timestamp);
        conversationData.setSender(conversationByUID1.sender);
        conversationData.setReceiver(conversationByUID1.receiver);
        conversationData.setType(conversationByUID1.type);
        conversationData.setSource(conversationByUID1.source);
        conversationData.setGroupName(conversationByUID1.groupName);
        conversationData.setForwardedTo(conversationByUID1.forwardedTo);
        conversationData.setCustomerName(conversationByUID1.customerName);
        conversationData.setPrivate(conversationByUID1.isPrivate);
        conversationData.setFromWidget(conversationByUID1.isFromWidget);
        conversationData.setTiggerevent(conversationByUID1.tiggerevent);
        conversationData.setConversationType(conversationByUID1.conversationType);
        conversationData.setPageId(conversationByUID1.pageId);
        conversationData.setPageName(conversationByUID1.pageName);
        conversationData.setFileLocalUri(conversationByUID1.fileLocalUri);
        conversationData.setDownloading(conversationByUID1.isDownloading);
        conversationData.setUpdateStatus(conversationByUID1.isUpdateStatus);
        conversationData.setShowLocalFiles(conversationByUID1.isShowLocalFiles);
        conversationData.setCaption(conversationByUID1.caption);
        conversationData.setSeen(conversationByUID1.isSeen);
        conversationData.setFailed(conversationByUID1.isFailed);
        conversationData.setCreatedOn(common.covertTimeStampToDate(conversationData.timestamp));
        conversationData.setResolved(conversationByUID1.isResolved);
        conversationData.setIsRating(conversationByUID1.rating);
        conversationData.setIsRatingFeedback(conversationByUID1.feedback);
        conversationData.setVoiceDuration(conversationByUID1.voiceDuration);

        if (isUpdateById){
            if (!dbchat.conversationDetailDao().exists(conversationData.id)) {
                dbchat.conversationDetailDao().insertConversation(conversationData);
            }else{
                if (conversationData.id!=0){
                    Executor myExecutor = Executors.newSingleThreadExecutor();
                    myExecutor.execute(() -> {
                        dbchat.conversationDetailDao().updateConversationByID(conversationByUID1.isSeen, conversationByUID1.id, conversationByUID1.customerId,
                                conversationByUID1.customerEmail,
                                conversationByUID1.tempChatId,
                                conversationByUID1.toUserId,
                                conversationByUID1.fromUserId,
                                conversationByUID1.groupId,
                                conversationByUID1.agentId,
                                conversationByUID1.content,
                                conversationByUID1.timestamp,
                                conversationByUID1.sender,
                                conversationByUID1.caption,
                                conversationByUID1.receiver,
                                conversationByUID1.type,
                                conversationByUID1.source,
                                conversationByUID1.groupName,
                                conversationByUID1.forwardedTo,
                                conversationByUID1.customerName,
                                conversationByUID1.conversationUid,
                                conversationByUID1.isPrivate,
                                conversationByUID1.isFromWidget,
                                conversationByUID1.tiggerevent,
                                conversationByUID1.conversationType,
                                conversationByUID1.pageId,
                                conversationByUID1.pageName,
                                conversationByUID1.fileLocalUri,
                                conversationByUID1.isDownloading,
                                conversationByUID1.isUpdateStatus,
                                conversationByUID1.isShowLocalFiles,
                                conversationByUID1.isWelcome,
                                conversationByUID1.isNotNewChat,
                                conversationByUID1.isFailed,
                                common.covertTimeStampToDate(conversationByUID1.timestamp),
                                conversationByUID1.isResolved,
                                conversationByUID1.rating,
                                conversationByUID1.feedback,
                                conversationByUID1.voiceDuration);
                    });
                }else{
                    if (!conversationByUID1.tempChatId.isEmpty()){
                        Executor myExecutor = Executors.newSingleThreadExecutor();
                        myExecutor.execute(() -> {
                            dbchat.conversationDetailDao().updateConversationByTempChatID(conversationByUID1.isSeen, conversationByUID1.id, conversationByUID1.customerId,
                                    conversationByUID1.customerEmail,
                                    conversationByUID1.tempChatId,
                                    conversationByUID1.toUserId,
                                    conversationByUID1.fromUserId,
                                    conversationByUID1.groupId,
                                    conversationByUID1.agentId,
                                    conversationByUID1.content,
                                    conversationByUID1.timestamp,
                                    conversationByUID1.sender,
                                    conversationByUID1.caption,
                                    conversationByUID1.receiver,
                                    conversationByUID1.type,
                                    conversationByUID1.source,
                                    conversationByUID1.groupName,
                                    conversationByUID1.forwardedTo,
                                    conversationByUID1.customerName,
                                    conversationByUID1.conversationUid,
                                    conversationByUID1.isPrivate,
                                    conversationByUID1.isFromWidget,
                                    conversationByUID1.tiggerevent,
                                    conversationByUID1.conversationType,
                                    conversationByUID1.pageId,
                                    conversationByUID1.pageName,
                                    conversationByUID1.fileLocalUri,
                                    conversationByUID1.isDownloading,
                                    conversationByUID1.isUpdateStatus,
                                    conversationByUID1.isShowLocalFiles,
                                    conversationByUID1.isWelcome,
                                    conversationByUID1.isNotNewChat,
                                    conversationByUID1.isFailed,
                                    common.covertTimeStampToDate(conversationByUID1.timestamp),
                                    conversationByUID1.isResolved,
                                    conversationByUID1.rating,
                                    conversationByUID1.feedback,
                                    conversationByUID1.voiceDuration);
                        });
                    }else{
                        dbchat.conversationDetailDao().insertConversation(conversationData);
                    }
                }
            }
            if (conversationData.type.equalsIgnoreCase("file")){
                if (!conversationByUID1.files.isEmpty()) {
                    for (int i=0;i<conversationByUID1.files.size();i++){
                        ConversationDetailFile conversationDetailFile = new ConversationDetailFile();
                        conversationDetailFile.setIcon(conversationByUID1.files.get(i).icon);
                        conversationDetailFile.setUrl(conversationByUID1.files.get(i).url);
                        conversationDetailFile.setCustomerId(conversationByUID1.customerId);
                        conversationDetailFile.setConversationUid(conversationByUID1.conversationUid);
                        conversationDetailFile.setType(conversationByUID1.files.get(i).type);
                        conversationDetailFile.setDocumentName(conversationByUID1.files.get(i).documentName);
                        conversationDetailFile.setTempChatId(conversationByUID1.files.get(i).tempChatId);
                        if (!dbchat.conversationDetailDao().existsFile(conversationByUID1.id)){
                            dbchat.conversationDetailDao().insertConversationFileDate(conversationDetailFile);
                        }else{
                            int position = i;
                            if (conversationByUID1.id!=0){
                                Executor myExecutor = Executors.newSingleThreadExecutor();
                                myExecutor.execute(() -> {
                                    dbchat.conversationDetailDao().updateConversationFileByID(conversationByUID1.id, conversationByUID1.files.get(position).url, conversationByUID1.files.get(position).type, conversationByUID1.files.get(position).documentName, conversationByUID1.conversationUid, conversationByUID1.customerId, conversationByUID1.tempChatId);
                                });
                            }else{
                                Executor myExecutor = Executors.newSingleThreadExecutor();
                                myExecutor.execute(() -> {
                                    dbchat.conversationDetailDao().updateConversationFileByTempChatID(conversationByUID1.id, conversationByUID1.files.get(position).url, conversationByUID1.files.get(position).type, conversationByUID1.files.get(position).documentName, conversationByUID1.conversationUid, conversationByUID1.customerId, conversationByUID1.tempChatId);
                                });
                            }
                        }
                    }
                }
            }
        }else{
            if (!dbchat.conversationDetailDao().exists(conversationData.tempChatId)) {
                long rowId = dbchat.conversationDetailDao().insertConversation(conversationData);
            }else{
                if (!conversationByUID1.tempChatId.isEmpty()){
                    Executor myExecutor = Executors.newSingleThreadExecutor();
                    myExecutor.execute(() -> {
                        dbchat.conversationDetailDao().updateConversationByTempChatID(conversationByUID1.isSeen,conversationByUID1.id,conversationByUID1.customerId,
                                conversationByUID1.customerEmail,
                                conversationByUID1.tempChatId,
                                conversationByUID1.toUserId,
                                conversationByUID1.fromUserId,
                                conversationByUID1.groupId,
                                conversationByUID1.agentId,
                                conversationByUID1.content,
                                conversationByUID1.timestamp,
                                conversationByUID1.sender,
                                conversationByUID1.caption,
                                conversationByUID1.receiver,
                                conversationByUID1.type,
                                conversationByUID1.source,
                                conversationByUID1.groupName,
                                conversationByUID1.forwardedTo,
                                conversationByUID1.customerName,
                                conversationByUID1.conversationUid,
                                conversationByUID1.isPrivate,
                                conversationByUID1.isFromWidget,
                                conversationByUID1.tiggerevent,
                                conversationByUID1.conversationType,
                                conversationByUID1.pageId,
                                conversationByUID1.pageName,
                                conversationByUID1.fileLocalUri,
                                conversationByUID1.isDownloading,
                                conversationByUID1.isUpdateStatus,
                                conversationByUID1.isShowLocalFiles,
                                conversationByUID1.isWelcome,
                                conversationByUID1.isNotNewChat,
                                conversationByUID1.isFailed,
                                common.covertTimeStampToDate(conversationByUID1.timestamp),
                                conversationByUID1.isResolved,
                                conversationByUID1.rating,
                                conversationByUID1.feedback,
                                conversationByUID1.voiceDuration);
                    });
                }else{
                    dbchat.conversationDetailDao().insertConversation(conversationData);
                }
            }
            if (conversationData.type.equalsIgnoreCase("file")){
                if (!conversationByUID1.files.isEmpty()) {
                    for (int i=0;i<conversationByUID1.files.size();i++){
                        ConversationDetailFile conversationDetailFile = new ConversationDetailFile();
                        conversationDetailFile.setIcon(conversationByUID1.files.get(i).icon);
                        conversationDetailFile.setUrl(conversationByUID1.files.get(i).url);
                        conversationDetailFile.setCustomerId(conversationByUID1.customerId);
                        conversationDetailFile.setConversationUid(conversationByUID1.conversationUid);
                        conversationDetailFile.setType(conversationByUID1.files.get(i).type);
                        conversationDetailFile.setDocumentName(conversationByUID1.files.get(i).documentName);
                        conversationDetailFile.setTempChatId(conversationByUID1.files.get(i).tempChatId);
                        if (!dbchat.conversationDetailDao().existsFile(conversationData.tempChatId)){
                            dbchat.conversationDetailDao().insertConversationFileDate(conversationDetailFile);
                        }else{
                            int position = i;
                            if (conversationByUID1.id!=0){
                                Executor myExecutor = Executors.newSingleThreadExecutor();
                                myExecutor.execute(() -> {
                                    dbchat.conversationDetailDao().updateConversationFileByID(conversationByUID1.id, conversationByUID1.files.get(position).url, conversationByUID1.files.get(position).type, conversationByUID1.files.get(position).documentName, conversationByUID1.conversationUid, conversationByUID1.customerId, conversationByUID1.tempChatId);
                                });
                            }else{
                                Executor myExecutor = Executors.newSingleThreadExecutor();
                                myExecutor.execute(() -> {
                                    dbchat.conversationDetailDao().updateConversationFileByTempChatID(conversationByUID1.id, conversationByUID1.files.get(position).url, conversationByUID1.files.get(position).type, conversationByUID1.files.get(position).documentName, conversationByUID1.conversationUid, conversationByUID1.customerId, conversationByUID1.tempChatId);
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendReceiptEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("ReadReceiptEvent")){
                try {
                    if(hubConnection!=null){
                        hubConnection.send("ReadReceipt",event.readReceiptRequest);
                    }
                } catch (Exception e) {
                    if(e.getMessage().equalsIgnoreCase("The 'invoke' method cannot be called if the connection is not active.") || e.getMessage().equalsIgnoreCase("The 'send' method cannot be called if the connection is not active")|| e.getMessage().contains("is not active")){
                        if (hubConnection != null && signalRHelper != null) {
                            boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                            if(isSignalRConnect){
                                stopIntervalOfConnection();
                            }
                        }
                    }
                }

            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FileResendEvent event) {
        if(event!=null){
            ArrayList<ConversationDetail> conversationDetailArrayList;
            if(event.eventType.equalsIgnoreCase("FileResend")){
                conversationDetailArrayList = new ArrayList<>();
                if (!dbchat.conversationDetailDao().getAllConversationWhereIDIsZero(0,event.tempChatId,event.conversationUUID).isEmpty()){
                    ArrayList<ConversationByUID> conversationByUIDArrayList = new ArrayList<>();
                    ArrayList<ConversationDetail> listOfStrings = new ArrayList<>(dbchat.conversationDetailDao().getAllConversationWhereIDIsZero(0,event.tempChatId,event.conversationUUID).size());
                    listOfStrings.addAll(dbchat.conversationDetailDao().getAllConversationWhereIDIsZero(0,event.tempChatId,event.conversationUUID));
                    String fileUri = listOfStrings.get(0).fileLocalUri;
                    String tempChatId = listOfStrings.get(0).tempChatId;
                    String fileName = "";
                    String type = "";
                    if (dbchat.conversationDetailDao().getConversationFilesDataByTempId(tempChatId,event.conversationUUID,0)!=null){
                        ConversationDetailFile conversationDetailFile = dbchat.conversationDetailDao().getConversationFilesDataByTempId(tempChatId,event.conversationUUID,0);
                        fileName = conversationDetailFile.documentName;
                        type = conversationDetailFile.type;
                    }
                    ArrayList<UploadFilesDataModel> fileUploadArrayList = new ArrayList<>();
                    Uri uri = Uri.parse(fileUri);
                    File compressedImageFile = null;
                    File fileTemp = null;
                    String fileType = "";
//                        try {
                            try {
                                fileTemp = FileUtil.from(mContext, uri);
                                Log.d("file", "File...:::: uti - " + fileTemp.getPath() + " file -" + fileTemp + " : " + fileTemp.exists());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            String typeFromPath = "";
                            if (mContext.getContentResolver().getType(uri)!=null){
                                typeFromPath = mContext.getContentResolver().getType(uri);
                            }else {
                                typeFromPath = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
                            }

                            if (typeFromPath.equalsIgnoreCase("jpeg") || typeFromPath.equalsIgnoreCase("jpg")
                                    || typeFromPath.equalsIgnoreCase("png")){
                                    if (FileUtil.getFileTypeFromUri(mContext,uri)!=null){
                                        fileType = FileUtil.getFileTypeFromUri(mContext,uri);
                                    }else{
                                        fileType = "image/"+typeFromPath;
                                    }

                                    String base64Str = "";
                                    try {
                                        base64Str  = common.convertFileToBase64(fileTemp.getAbsolutePath());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

//                                    compressedImageFile = new Compressor(mContext).compressToFile(fileTemp);
                                    try {
                                        ImageCompressor imageCompressor = new ImageCompressor();
                                        compressedImageFile = imageCompressor.compressImageFile(mContext, fileTemp);
                                        // Use the compressedImageFile as needed
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    fileUploadArrayList.add(new UploadFilesDataModel(tempChatId,base64Str,fileTemp.getName(),fileType, event.conversationUUID,listOfStrings.get(0).caption));
                                    fileName = compressedImageFile.getName();

                                }else{
                                    if (FileUtil.getFileTypeFromUri(mContext,uri)!=null){
                                        fileType = FileUtil.getFileTypeFromUri(mContext,uri);
                                    }else{
                                        fileType = "application/"+typeFromPath;
                                    }
                                    String base64Str = "";
                                    try {
                                        base64Str  = common.convertFileToBase64(fileTemp.getAbsolutePath());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    fileUploadArrayList.add(new UploadFilesDataModel(tempChatId,base64Str,fileTemp.getName(),fileType,event.conversationUUID,listOfStrings.get(0).caption));
                                    fileName = fileTemp.getName();
                                }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        if (fileUploadArrayList!=null && !fileUploadArrayList.isEmpty()){
                            EventBus.getDefault().post(new MessageEventForResendImage("UploadResendImage",fileUploadArrayList,tempChatId,fileName,listOfStrings.get(0).caption));
                        }
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)

    public void onMessageEvent(MessageEvent event) {
        if (!event.eventType.isEmpty()) {
            if (event.eventType.equalsIgnoreCase("startSignalR")) {
                if (hubConnection != null && signalRHelper != null) {
                    boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                    if(isSignalRConnect){
                        stopIntervalOfConnection();
                    }
                }
            }else if(event.eventType.equalsIgnoreCase("ShowToolbar")){
                   hideNshowToolbar(false);
            }else if (event.eventType.equalsIgnoreCase("HideToolbar")){
                   hideNshowToolbar(true);
            }else if (event.eventType.equalsIgnoreCase("SwitchToConversationList")){
                     finish();
            }else if (event.eventType.equalsIgnoreCase("isComingFromUserProfileDetailSwitch")){
                //ReplaceFragmentWithoutClearBackStack(new UserProfileFragment(),true,bundle,false);
            }
            else if (event.eventType.equalsIgnoreCase("Reload")){
                runOnUiThread(() -> {
                    if (txtStatus!=null){
                        txtStatus.setText("Connected");
                    }
                    if(icSource!=null){
                        Glide.with(mContext).load(R.drawable.wifi).into(icSource);
                    }
                    EventBus.getDefault().post(new ReLoadConversationEvent("ReloadConversationWhenConnect"));
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> {
                        //Do something after 100ms
                        LayoutReconnecting.setVisibility(View.GONE);
                        stopIntervalOfConnection();

                    }, 500);
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(hubConnection!=null){
            hubConnection.remove("onError");
            hubConnection.remove("ReceiveMessage");
            hubConnection.remove("NewChatReceiver");
            hubConnection.remove("ForceLogoutListener");
            hubConnection.remove("SyncMessagesListener");
            hubConnection.remove("ReadReceiptListener");
            hubConnection.remove("TypingIndicatorListener");
            hubConnection.remove("BulkNewChatListener");
            hubConnection.remove("ConversationStatusListener");
            hubConnection.stop();
        }
        if (common!=null && mContext!=null){
            common.setIsConversationOpen(mContext,false);
        }
        stopIntervalOfConnection();
        isComingFromChat = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendChatEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("SendNewMessage")){
                try {
                    if(hubConnection!=null){
                        hubConnection.send("SendPrivateMessage",event.sendMessageModel,true);
                    }
                } catch (Exception e) {
                    if(e.getMessage().equalsIgnoreCase("The 'invoke' method cannot be called if the connection is not active.") || e.getMessage().equalsIgnoreCase("The 'send' method cannot be called if the connection is not active")|| e.getMessage().contains("is not active")){
                        if (hubConnection != null && signalRHelper != null) {
                            boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                            if(isSignalRConnect){
                                stopIntervalOfConnection();
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendNewChatEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("SendNewChatMessage")){
                try {
                    if(hubConnection!=null) {
                        if(hubConnection!=null){
                            hubConnection.send("NewChat",event.sendMessageModel,true);
                        }
                    }
                } catch (Exception e) {
                    if(e.getMessage().equalsIgnoreCase("The 'invoke' method cannot be called if the connection is not active.") || e.getMessage().equalsIgnoreCase("The 'send' method cannot be called if the connection is not active")|| e.getMessage().contains("is not active")){
                        if (hubConnection != null && signalRHelper != null) {
                            boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                            if(isSignalRConnect){
                                stopIntervalOfConnection();
                            }
                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendTypingIndicatorResponse event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("IndicateTyping")){
                try {
                    if(hubConnection!=null) {
                        if(hubConnection!=null){
                            hubConnection.send("IndicateTyping",event.typingIndicatorListenerModel);
                        }
                    }

                } catch (Exception e) {
                    if(e.getMessage().equalsIgnoreCase("The 'invoke' method cannot be called if the connection is not active.") || e.getMessage().equalsIgnoreCase("The 'send' method cannot be called if the connection is not active")|| e.getMessage().contains("is not active")){
                        if (hubConnection != null && signalRHelper != null) {
                            boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                            if(isSignalRConnect){
                                stopIntervalOfConnection();
                            }
                        }
                    }
                }

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CustomerFeedBackEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("ConversationFeedback")){
                try {
                    if(hubConnection!=null) {
                        if(hubConnection!=null){
                            hubConnection.send("ConversationFeedback",event.resolveFeedBackRequest);
                        }
                    }
                } catch (Exception e) {
                    if(e.getMessage().equalsIgnoreCase("The 'invoke' method cannot be called if the connection is not active.") || e.getMessage().equalsIgnoreCase("The 'send' method cannot be called if the connection is not active")|| e.getMessage().contains("is not active")){
                        if (hubConnection != null && signalRHelper != null) {
                            boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                            if(isSignalRConnect){
                                stopIntervalOfConnection();
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragInstance = fm.findFragmentById(R.id.main_content);
        if ((fragInstance instanceof ConversationsDetailFragment)) {
            if (common!=null && mContext!=null){
                common.setIsConversationOpen(mContext,false);
            }


            boolean isHandled = ((ConversationsDetailFragment) fragInstance).handleBackPress();
            if (isHandled) {
                return; // Fragment handled the back press
            }
            else {
                finish();

            }
        }

    }

    public void hideNshowToolbar(boolean isHide){
//        if(isHide){
//            action_bar.setVisibility(View.GONE);
//        }else{
//            action_bar.setVisibility(View.GONE);
//        }

    }

    public void scheduleApiSignalRConnection() {
        int delay = 0; // delay for 0 sec.
        int period = 2500; // repeat every 10 sec.
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run() {
                //Call function
                if(!isSignalRConnected) {
                    //ping signal are every 2 second once connect then upcoming
                    // Reconnect if the connection was lost due to an error
                    if (hubConnection != null  && signalRHelper != null) {
                        boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                        if(isSignalRConnect){
                            stopIntervalOfConnection();
                        }
                    }
                }
            }
        }, delay, period);
    }

    @Override
    public void hasInternetConnection() {
        runOnUiThread(() -> {
            if (isAlreadyConnected){
                isAlreadyConnected = false;
                EventBus.getDefault().post(new MessageEvent("Reload"));
            }
        });
    }

    @Override
    public void hasNoInternetConnection() {
        runOnUiThread(() -> {
            isAlreadyConnected = true;
            EventBus.getDefault().post(new ReLoadConversationEvent("Reconnecting"));

        });
    }
    public void getAccessTokenByChannelId(String channelId){

        new ApiClient(mContext).getWebService().getAccessTokenByChannelId(channelId).enqueue(new Callback<WebResponse>() {
            @Override
            public void onResponse(Call<WebResponse> call, Response<WebResponse> response) {
                if (response.body()!=null){
                    if(response.isSuccessful()){
                        if (!response.body().getResult().toString().isEmpty()){

                            //create chathub connection
                            common.saveToken(MainActivityChat.this,response.body().getResult().toString());
                            hubConnection = signalRHelper.createChatHubConnection(response.body().getResult().toString(), mContext);

                            //set all chathub listners
                            setHubConnectionListeners(hubConnection);
                            //start signal R
                            if (hubConnection != null && signalRHelper != null) {
                                boolean isSignalRConnect =  signalRHelper.startSignalRHubClient(hubConnection, common.getCustomerID(mContext),common.getFcmToken(mContext),mContext, MainActivityChat.this);
                                if(isSignalRConnect){
                                    stopIntervalOfConnection();
                                }
                            }
                            hubConnection.onClosed(exception -> {
                                if (exception != null) {
                                    isSignalRConnected = false;
                                    scheduleApiSignalRConnection();
                                }
                            });
                            getOrganization();
                            EventBus.getDefault().post(new MessageEvent( "LoadConversationList"));

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponse> call, Throwable t) {
                EventBus.getDefault().post(new MessageEvent( "LoadConversationList"));

            }
        });
    }

    public void getOrganization() {

        new ApiClient(mContext).getWebService().getOrganization().enqueue(new Callback<WebResponse<OrganizationModelData>>() {
            @Override
            public void onResponse(Call<WebResponse<OrganizationModelData>> call, Response<WebResponse<OrganizationModelData>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        // OrganizationDetails organizationDetails = new OrganizationDetails();
                        // organizationDetails.orgName =  response.body().getResult().orgName;
                        // organizationDetails.displayname =  response.body().getResult().displayname;
                        common.saveOrganizationName(MainActivityChat.this,response.body().getResult().orgName);
                        common.saveDisplayName(MainActivityChat.this,response.body().getResult().displayname);
                        EventBus.getDefault().post(new MessageEvent( "UpdateNameOfOrganization"));
                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponse<OrganizationModelData>> call, Throwable t) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendFileAfterPreview event) {
        if(event!=null) {
            if (event.eventType.equalsIgnoreCase("SendFileAfterPreview")) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivityChat.isPause = false;
    }
}
