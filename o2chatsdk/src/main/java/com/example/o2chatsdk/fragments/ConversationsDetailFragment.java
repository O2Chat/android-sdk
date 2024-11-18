package com.example.o2chatsdk.fragments;

import static com.example.o2chatsdk.commons.Constants.COLOR_CODE_KEY;
import static com.example.o2chatsdk.commons.Constants.HIDE_TOOLBAR;
import static com.example.o2chatsdk.commons.Constants.NAME_KEY;
import static com.example.o2chatsdk.commons.Constants.NAME_LETTER_KEY;
import static com.example.o2chatsdk.commons.Constants.SOURCE_KEY;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.o2chatsdk.Events.appEvents.CustomerFeedBackEvent;
import com.example.o2chatsdk.Events.appEvents.FileDeleteEvent;
import com.example.o2chatsdk.Events.appEvents.MessageEventFileDownload;
import com.example.o2chatsdk.Events.appEvents.MessageEventForResendImage;
import com.example.o2chatsdk.Events.appEvents.ReadReceiptUpdateEvent;
import com.example.o2chatsdk.Events.appEvents.SendReceiptEvent;
import com.example.o2chatsdk.Events.chatEvents.AssignedChatEvent;
import com.example.o2chatsdk.Events.chatEvents.ConversationEvent;
import com.example.o2chatsdk.Events.appEvents.MessageEvent;
import com.example.o2chatsdk.Events.chatEvents.ConversationStatusListenerEvent;
import com.example.o2chatsdk.Events.chatEvents.ReceiveMessageEvent;
import com.example.o2chatsdk.Events.chatEvents.SendBulkChatEvent;
import com.example.o2chatsdk.Events.chatEvents.SendChatEvent;
import com.example.o2chatsdk.Events.chatEvents.SendFileAfterPreview;
import com.example.o2chatsdk.Events.chatEvents.SendNewChatEvent;
import com.example.o2chatsdk.Events.chatEvents.SendNewChatResponseEvent;
import com.example.o2chatsdk.Events.chatEvents.SendTypingIndicatorResponse;
import com.example.o2chatsdk.Events.chatEvents.TopicSelectEvent;
import com.example.o2chatsdk.O2ChatConfig;
import com.example.o2chatsdk.activities.MainActivityChat;
import com.example.o2chatsdk.activities.SelectFilePreviewActivity;
import com.example.o2chatsdk.adapters.ConversationsByUIListAdapter;
import com.example.o2chatsdk.adapters.ConversationsListingDetailAdapter;
import com.example.o2chatsdk.adapters.FileDataClass;
import com.example.o2chatsdk.adapters.SelectedFilesListAdapter;
import com.example.o2chatsdk.adapters.TopicListAdapter;
import com.example.o2chatsdk.commons.Common;
import com.example.o2chatsdk.commons.Constants;
import com.example.o2chatsdk.commons.FileUtil;
import com.example.o2chatsdk.commons.ImageCompressor;
import com.example.o2chatsdk.commons.PaginationScrollListener;
import com.example.o2chatsdk.commons.PermissionHelper;
import com.example.o2chatsdk.commons.Utils;
import com.example.o2chatsdk.localDB.AppDatabase;
import com.example.o2chatsdk.localDB.entity.ConversationDetail;
import com.example.o2chatsdk.localDB.entity.ConversationDetailFile;
import com.example.o2chatsdk.localDB.entity.NewChatEntity;
import com.example.o2chatsdk.model.chat.AssignChatListener;
import com.example.o2chatsdk.model.chat.Conversation;
import com.example.o2chatsdk.model.chat.ConversationByUID;
import com.example.o2chatsdk.model.chat.ConversationStatusListenerDataModel;
import com.example.o2chatsdk.model.chat.EventAddTopicMessage;
import com.example.o2chatsdk.model.chat.FilesData;
import com.example.o2chatsdk.model.chat.GetTopicsByConversationModel;
import com.example.o2chatsdk.model.chat.NewChatModel;
import com.example.o2chatsdk.model.chat.NewChatRecieveResponse;
import com.example.o2chatsdk.model.chat.OnErrorData;
import com.example.o2chatsdk.model.chat.ReadReceiptRequest;
import com.example.o2chatsdk.model.chat.ResolveFeedBackRequest;
import com.example.o2chatsdk.model.chat.SendMessageModel;
import com.example.o2chatsdk.model.chat.SyncChatRecieveResponse;
import com.example.o2chatsdk.model.chat.TopicModelResponse;
import com.example.o2chatsdk.model.chat.TypingIndicatorListenerModel;
import com.example.o2chatsdk.model.chat.UploadFilesData;
import com.example.o2chatsdk.model.chat.selectedFilePreviewData;
import com.example.o2chatsdk.retrofit.ApiClient;
import com.example.o2chatsdk.retrofit.WebResponse;
import com.example.o2chatsdk.retrofit.WebResponse2;
import com.example.o2chatsdk.retrofit.WebResponseBusinessHour;
import com.download.library.DownloadImpl;
import com.download.library.DownloadListenerAdapter;
import com.download.library.Extra;
import com.example.o2chatsdk.R;
import com.example.o2chatsdk.databinding.FragmentConversationsDetailBinding;
import com.example.o2chatsdk.commons.KeyboardVisibilityUtils;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

//import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */

public class ConversationsDetailFragment extends Fragment{

    ScheduledExecutorService scheduler;
    ArrayList<ConversationByUID> conversationByUIDArrayListIdZero;
    Handler handlerResend;
    boolean isTopicSelect,isTopicSecondTimeSelect = false;

    String topicMessage = "";
    String topicName = "";
    int topicID ;
    AppDatabase dbchat ;
    Handler handlerBulk;
    boolean isLocalChatLoaded = false;
    public static boolean isCalledFromPreviewActivity = false;
    public static boolean isCalledFromPreviewActivityBack = false;
    public static SendFileAfterPreview sendFileAfterPreview ;
    private final int CAPTURE_PICTURE_FROM_CAMERA = 2;
    private final int PICK_IMAGE_FOR_SELECT = 3;
    public String customerName = "";
    public String customerCNIC = "";
    public String customerMobileNumber = "";
    public String customerEmail = "";
    public long agentId = 0 ;
    public long cusId = 0;
    public long groupId = 0;
    public String mCurrentPhotoPath = "";
    public String channelId ;
    Timer timer,timer2,timer1;
    ArrayList<UploadFilesData> uploadFilesData;
    ArrayList<FileDataClass> filesNames = null;
    ArrayList<UploadFilesDataModel> fileUploadArrayList = null;
    File fileTemp = null;
    Handler handler = null;
    Runnable myRunnable = null;
    int pageNumber = 1;
    int pageSize = 15;
    int totalPages = 1;
    //init layout manager for list
    LinearLayoutManager mLayoutManager;
    FragmentConversationsDetailBinding fragmentConversationsBinding;
    Common common;
    String conversationByUID = "";
    String tempChatId = "";
    int addedConversationPos = -1;
    Context mContext;
    ConversationsByUIListAdapter conversationsListAdapter = null;
    SelectedFilesListAdapter selectedFilesListAdapter = null;
    ArrayList<ConversationByUID> conversationArrayList ;
    ArrayList<GetTopicsByConversationModel> topicArrayList ;
    ConversationsListingDetailAdapter conversationsListingDetailAdapter = null;
    Conversation conversation = null;
    private String businessHoursMessage = "";
    String temppChatIdWelcomeMessage = "";
    String temppChatIdWithoutTopic = "";
    String tempChatIdTopicWelcomeMsg = "";
    private String exitingCustomText = "";
    private boolean isOnline = false;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isFileAttach = false;
    ArrayList<selectedFilePreviewData> arrayListData;
    boolean isSatisfied = false;
    private String outputFilePath;
    MediaRecorder recorder;
    boolean isRecording = false;

    private Thread timerThread;
    private MediaPlayer mediaPlayer;
    Runnable updateTimeRunnable;
    String audioDuration = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().post(new MessageEvent(HIDE_TOOLBAR));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().post(new MessageEvent(HIDE_TOOLBAR));
        mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentConversationsBinding = FragmentConversationsDetailBinding.inflate(getLayoutInflater());
        View view = fragmentConversationsBinding.getRoot();


        dbchat = AppDatabase.getAppDatabase(getContext().getApplicationContext());
        common = new Common();
        filesNames = new ArrayList<>();
        fileUploadArrayList = new ArrayList<>();
        uploadFilesData = new ArrayList<>();
        conversationArrayList = new ArrayList<>();
        topicArrayList = new ArrayList<>();
        conversation = common.getConversationData(mContext);

        // Install the EmojiProvider
        EmojiManager.install(new GoogleEmojiProvider());

        final EmojiPopup popup = EmojiPopup.Builder.fromRootView(view).build(fragmentConversationsBinding.edtMessage);

//        O2ChatConfig config = O2ChatConfig.getInstance(getContext());
//        customerName = config.getFirstName()+""+config.getLastName();
//        customerCNIC = config.getCnic();
//        customerMobileNumber = config.getPhone();
//        customerEmail = config.getEmail();
//        conversationByUID = common.getConversationUUId(mContext);

        customerName = common.getFirstName(mContext);
        customerCNIC = common.getCustomerCnic(mContext);
        customerMobileNumber = common.getCustomerMobileNumber(mContext);
        customerEmail = common.getCustomerEmail(mContext);
        conversationByUID = common.getConversationUUId(mContext);

        if (common!=null){
            cusId = common.getCustomerID(mContext);
        }

        if (getArguments()!=null){
            Bundle bundle = getArguments();
            if(bundle.containsKey(NAME_LETTER_KEY) && bundle.containsKey(NAME_KEY) && bundle.containsKey(SOURCE_KEY) && bundle.containsKey(COLOR_CODE_KEY) && bundle.containsKey(Constants.CONVERSATION_BY_UID_KEY)){
                if (bundle.getString(NAME_LETTER_KEY)!=null){
                    fragmentConversationsBinding.txtNameFirstLetter.setText(bundle.getString(NAME_LETTER_KEY));
                }

                if (bundle.getString(Constants.CHANNEL_ID)!=null){
                    channelId = bundle.getString(Constants.CHANNEL_ID);
                }
                if (bundle.getString(COLOR_CODE_KEY)!=null){
                    if(!bundle.getString(COLOR_CODE_KEY).isEmpty()){
                        int colorCodeBg = Integer.parseInt(bundle.getString(COLOR_CODE_KEY));
                        fragmentConversationsBinding.ivFirstName.setColorFilter(colorCodeBg);
                    }
                }
            }
        }

        //get locally
        fragmentConversationsBinding.orgName.setText(common.getDisplayName(mContext));
        fragmentConversationsBinding.tvCustomText.setText(getString(R.string.lbl_reply_desc));
        exitingCustomText = getString(R.string.lbl_reply_desc);

        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        fragmentConversationsBinding.rvConversations.setLayoutManager(mLayoutManager);

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setAlignItems(AlignItems.STRETCH);
        fragmentConversationsBinding.recycleTopicChannel.setLayoutManager(flexboxLayoutManager);

        loadLocalMessages();

        // getConversationByUID(isLocalChatLoaded,pageNumber,pageSize,common.getConversationUUId(mContext),cusId,false);

        fragmentConversationsBinding.ivImageMenu.setOnClickListener(v -> {

            if(!handleBackPress())
            {
                EventBus.getDefault().post(new MessageEvent("SwitchToConversationList"));

            }

        });

        fragmentConversationsBinding.rvConversations.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = getCurrentItem();
                    if (position==0){
                        fragmentConversationsBinding.jumptoBottom.setVisibility(View.GONE);
                    }else if (position>1){
                        fragmentConversationsBinding.jumptoBottom.setVisibility(View.VISIBLE);
                    }
                    if (!conversationArrayList.isEmpty() && conversationsListAdapter.getItemCount()>0){
                        for (int i = 0; i < conversationsListAdapter.getItemCount(); i++) {
                            ConversationByUID conversationByUID1 = conversationArrayList.get(i);
                            if (conversationByUID1!=null){
                                if (!conversationByUID1.isFromWidget && !conversationByUID1.isSeen && !conversationByUID1.type.equalsIgnoreCase("system")){
                                    readReceiptInvoke(conversationByUID1.id);
                                }
                            }
                        }
                    }
                }
            }
        });

        fragmentConversationsBinding.ivAudioRecord.setOnClickListener(view113 -> {

                // Check and request RECORD_AUDIO permission
                ArrayList<String> permissionList = new ArrayList<>();
              //  permissionList.add(Manifest.permission.RECORD_AUDIO);
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                PermissionHelper.grantMultiplePermissions(getContext(), permissionList, new PermissionHelper.PermissionInterface() {
                    @Override
                    public void onSuccess() {
                        // Assuming fragmentConversationsBinding is of type FragmentConversationsBinding
                        if (fragmentConversationsBinding.lnAudioRecording.getVisibility() == View.VISIBLE) {
                            // The layout is visible, handle it accordingly
                            fragmentConversationsBinding.lnAudioRecording.setVisibility(View.GONE);
                        } else {
                            // The layout is not visible, handle it accordingly
                            startRecording();
                            fragmentConversationsBinding.lnAudioRecording.setVisibility(View.VISIBLE);

                            fragmentConversationsBinding.edtMessage.setEnabled(false);
                        }
                    }

                    @Override
                    public void onError() {
                        fragmentConversationsBinding.edtMessage.setEnabled(true);
                    }
                });
        });

        fragmentConversationsBinding.ivStop.setOnClickListener(view114 -> {
            fragmentConversationsBinding.ivStop.setVisibility(View.GONE);
            fragmentConversationsBinding.ivPlay.setVisibility(View.VISIBLE);
            stopRecording();
            stopRecording();
            audioDuration = getDuration(outputFilePath);
            stopPlaying();
        });

        fragmentConversationsBinding.ivPlay.setOnClickListener(view116 -> {
            fragmentConversationsBinding.ivStop.setVisibility(View.VISIBLE);
            fragmentConversationsBinding.ivPlay.setVisibility(View.GONE);
            startPlaying();
        });

        fragmentConversationsBinding.ivDelete.setOnClickListener(view115 -> {
            fragmentConversationsBinding.lnAudioRecording.setVisibility(View.GONE);
            //remove recording delete recording
            fragmentConversationsBinding.ivStop.setVisibility(View.VISIBLE);
            fragmentConversationsBinding.ivPlay.setVisibility(View.GONE);
            stopRecording();
            stopPlaying();
            fragmentConversationsBinding.timerTextView.setText("00:00");
            outputFilePath = "";

            fragmentConversationsBinding.edtMessage.setEnabled(true);
        });

        fragmentConversationsBinding.sendMessage.setOnClickListener(v -> {
            if (!fragmentConversationsBinding.edtMessage.getText().toString().isEmpty()) {
                //temporary comment this condition works when there is
                // no topicArrayList then and welcome message
                // is coming from server side we will show that into chat but we send when user send message
                if (common.getIsResolved(mContext) && topicArrayList.isEmpty() && !businessHoursMessage.isEmpty()){
                    isTopicSecondTimeSelect = false;
                    sendNewChat("","welcomeMessage", businessHoursMessage, uploadFilesData,arrayListData, temppChatIdWelcomeMessage,false);
                }
                tempChatId = UUID.randomUUID().toString();
                sendNewChat("","text",fragmentConversationsBinding.edtMessage.getText().toString(),uploadFilesData,arrayListData,tempChatId,false);
                fragmentConversationsBinding.edtMessage.setText("");
            }else{
                Toast.makeText(getActivity(), "Please type a message", Toast.LENGTH_SHORT).show();
            }

        });

        fragmentConversationsBinding.ivSendAuidoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopRecording();
                stopPlaying();
                fragmentConversationsBinding.timerTextView.setText("00:00"); // Reset timer display
                fragmentConversationsBinding.lnAudioRecording.setVisibility(View.GONE);
                tempChatId = UUID.randomUUID().toString();
                arrayListData = new ArrayList<>();
                // Extract file information

                if(outputFilePath != null && !outputFilePath.isEmpty()) {
                    File file = new File(outputFilePath);
                    // If using local file path, use: URI uri = new File(mediaPath).toURI();
                    String fileUri = file.toURI().toString();
                    String fileName = file.getName();
                    String fileType = "";

                    String result = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                    String finalresult = result.replace(".", "");
                    fileType = "application/" + finalresult;
                    String tempChatID = UUID.randomUUID().toString();
                    String base64Str = "";
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            base64Str = common.convertFileToBase64(file.getAbsolutePath());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    fileUploadArrayList.add(new UploadFilesDataModel(tempChatID, base64Str, file.getName(), fileType, conversationByUID,""));

                    filesNames.add(new FileDataClass(file.getName(), common.getFolderSizeLabel(file), fileType, fileUri, tempChatID));
                    selectedFilePreviewData selectedPreviewData = new selectedFilePreviewData(fileType, fileUri, fileName, common.getFolderSizeLabel(file), tempChatID,"",getDuration(outputFilePath));
                    arrayListData.add(selectedPreviewData);

                    addAudioMessageToList(conversation, arrayListData,fileUploadArrayList);

                    fragmentConversationsBinding.layoutAudioRecord.setVisibility(View.VISIBLE);
                    fragmentConversationsBinding.rlSendMessage.setVisibility(View.GONE);

                    fragmentConversationsBinding.ivPlay.setVisibility(View.GONE);
                    fragmentConversationsBinding.ivStop.setVisibility(View.VISIBLE);

                    fragmentConversationsBinding.edtMessage.setEnabled(true);

                }
            }
        });

        fragmentConversationsBinding.layoutImageUpload.setOnClickListener(v -> {
            isFileAttach = false;
            uploadImageDialog(getContext());
        });

        fragmentConversationsBinding.layoutFileAttach.setOnClickListener(v -> {
            isFileAttach = true;
            MainActivityChat.isPause = false;
            MainActivityChat.isLoadChat = true;
            storagePermission(false,true,null);

        });

        fragmentConversationsBinding.ivSmile.setOnClickListener(v -> {
            fragmentConversationsBinding.ivSmile.setVisibility(View.GONE);
            fragmentConversationsBinding.ivKeyboard.setVisibility(View.VISIBLE);
            popup.toggle();
        });

        fragmentConversationsBinding.ivKeyboard.setOnClickListener(v -> {
            fragmentConversationsBinding.ivKeyboard.setVisibility(View.GONE);
            fragmentConversationsBinding.ivSmile.setVisibility(View.VISIBLE);
            popup.dismiss();
        });

        //init layout manager for main list
        LinearLayoutManager layoutManagerList = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        fragmentConversationsBinding.rvConversationList.setLayoutManager(layoutManagerList);

//        KeyboardVisibilityEvent.setEventListener(getActivity(), isOpen -> {
//            if (isOpen){
//                fragmentConversationsBinding.ivSmile.setVisibility(View.GONE);
//                fragmentConversationsBinding.ivKeyboard.setVisibility(View.VISIBLE);
//            } else{
//                fragmentConversationsBinding.ivSmile.setVisibility(View.VISIBLE);
//                fragmentConversationsBinding.ivKeyboard.setVisibility(View.GONE);
//            }
//        });

        // Set keyboard visibility listener
        KeyboardVisibilityUtils.setKeyboardVisibilityListener(getActivity(), isOpen -> {
            if (isOpen){
                fragmentConversationsBinding.ivSmile.setVisibility(View.GONE);
                fragmentConversationsBinding.ivKeyboard.setVisibility(View.VISIBLE);
            } else{
                fragmentConversationsBinding.ivSmile.setVisibility(View.VISIBLE);
                fragmentConversationsBinding.ivKeyboard.setVisibility(View.GONE);
            }
        });

        fragmentConversationsBinding.ivMenuTopic.setOnClickListener(view12 -> {
            if (!topicArrayList.isEmpty()){
                LoadTopics();
            }else{
                Toast.makeText(mContext, "Topic not found", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentConversationsBinding.rvConversations.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                // check weather is last page or not
                if (pageNumber<=totalPages){
                    pageNumber++;
                    isLoading = true;
                    fragmentConversationsBinding.loadMoreProgress.setVisibility(View.VISIBLE);
                    //api for all conversations
                    getConversationByUID(isLocalChatLoaded,pageNumber,pageSize,common.getConversationUUId(mContext),cusId,false);
                } else {
                    isLoading = false;
                    isLastPage = true;
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        fragmentConversationsBinding.jumptoBottom.setOnClickListener(view1 -> {
            scrollToBottom();
        });

        fragmentConversationsBinding.ivAudioRecord.setOnClickListener(view113 -> {

                // Check and request RECORD_AUDIO permission
                ArrayList<String> permissionList = new ArrayList<>();
                permissionList.add(Manifest.permission.RECORD_AUDIO);
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                PermissionHelper.grantMultiplePermissions(getContext(), permissionList, new PermissionHelper.PermissionInterface() {
                    @Override
                    public void onSuccess() {
                        // Assuming fragmentConversationsBinding is of type FragmentConversationsBinding
                        if (fragmentConversationsBinding.lnAudioRecording.getVisibility() == View.VISIBLE) {
                            // The layout is visible, handle it accordingly
                            fragmentConversationsBinding.lnAudioRecording.setVisibility(View.GONE);
                        } else {
                            // The layout is not visible, handle it accordingly
                            startRecording();
                            fragmentConversationsBinding.lnAudioRecording.setVisibility(View.VISIBLE);

                            fragmentConversationsBinding.edtMessage.setEnabled(false);
                        }
                    }

                    @Override
                    public void onError() {
                        fragmentConversationsBinding.edtMessage.setEnabled(true);
                    }
                });
        });


        fragmentConversationsBinding.edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // user is typing: reset already started timer (if existing)

                if(fragmentConversationsBinding.edtMessage.getText().toString().isEmpty())
                {
                    fragmentConversationsBinding.layoutAudioRecord.setVisibility(View.VISIBLE);
                    fragmentConversationsBinding.rlSendMessage.setVisibility(View.GONE);

                }else{
                    fragmentConversationsBinding.layoutAudioRecord.setVisibility(View.GONE);
                    fragmentConversationsBinding.rlSendMessage.setVisibility(View.VISIBLE);
                }

                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                        TypingIndicatorListenerModel typingIndicatorListenerModel = new TypingIndicatorListenerModel();
                        typingIndicatorListenerModel.conversationUId = conversationByUID;
                        typingIndicatorListenerModel.name = customerName;
                        typingIndicatorListenerModel.id = Integer.parseInt(String.valueOf(cusId));
                        typingIndicatorListenerModel.isFromWidget = true;
                        typingIndicatorListenerModel.callerAppType = 3;
                        EventBus.getDefault().post(new SendTypingIndicatorResponse(typingIndicatorListenerModel, "IndicateTyping"));
                    }
                }, 600); // 600ms delay before the timer executes the „run“ method from TimerTask
            }
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                resendSchedulerStart();
            }
        }, 500);

        return view;
    }

    public boolean handleBackPress() {
        if (fragmentConversationsBinding.lbTopicChannel.getVisibility() == View.VISIBLE) {
            hideAndShowTopicMenu(true);
            return true; // Back press handled
        }
        return false; // Not handled, let activity decide
    }

    private void startRecording() {

        outputFilePath = getOutputFilePath();

        // below method is used to initialize
        // the media recorder class
        recorder = new MediaRecorder();
        // below method is used to set the audio
        // source which we are using a mic.
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // the output format of the audio.
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        recorder.setOutputFile(outputFilePath);
        // audio encoder for our recorded audio.
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        // below method is used to set the
        // output file location for our recorded audio
        try {
            // below method will prepare
            // our audio recorder class
            recorder.prepare();
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
        // start method will start
        // the audio recording.
        recorder.start();
        isRecording = true;
        timerThread = new Thread(() -> {
            int elapsedSeconds = 0;
            while (true) {
                try {
                    Thread.sleep(1000); // Update timer every second
                    elapsedSeconds++;

                    // ... (update timer UI on main thread)
                    int finalElapsedSeconds = elapsedSeconds;
                    getActivity().runOnUiThread(() -> {
                        if (fragmentConversationsBinding.timerTextView  != null) {
                            fragmentConversationsBinding.timerTextView.setText(String.format("%02d:%02d", finalElapsedSeconds / 60, finalElapsedSeconds % 60));
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        timerThread.start();
    }

    private String getOutputFilePath() {
        // Define your logic to generate a unique output file path for the recording
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String filename = "recording_" + timestampFormat.format(new Date()) + ".mp3"; // Or your preferred extension

        // Get the external storage directory for recordings (consider permissions)
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        storageDir.mkdirs(); // Create the directory if it doesn't exist

        return new File(storageDir, filename).getAbsolutePath();
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                if (getActivity() != null) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    isRecording = false;  // Set recording state to false
                    // Update UI to indicate recording stopped state
                }

                // Stop timer updates (optional)
                if (timerThread != null) {
                    timerThread.interrupt();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                // Handle potential exception if recorder is not in a valid state
            }
        }
    }

    private String getDuration(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration(); // duration in milliseconds
            mediaPlayer.release();

            int minutes = (duration / 1000) / 60;
            int seconds = (duration / 1000) % 60;
            return  minutes + ":" + String.format("%02d", seconds);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "00:00";
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(outputFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            updateTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    int duration = mediaPlayer.getDuration();
                    fragmentConversationsBinding.timerTextView.setText(formatTime(duration));
                    int remainingTime = mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition();
                    fragmentConversationsBinding.timerTextView.setText(formatTime(remainingTime));
                    if (mediaPlayer.isPlaying()) {
                        handler.postDelayed(this, 1000);
                    }
                }
            };

            handler.post(updateTimeRunnable);
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                stopPlaying();
                fragmentConversationsBinding.timerTextView.setText("00:00");
                fragmentConversationsBinding.ivStop.setVisibility(View.GONE);
                fragmentConversationsBinding.ivPlay.setVisibility(View.VISIBLE);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {

        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (handler != null)
            {
                handler.removeCallbacks(updateTimeRunnable);
                //fragmentConversationsBinding.timerTextView.setText("00:00");
            }

        }
        catch (Exception e){

        }
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private int getCurrentItem(){
        return ((LinearLayoutManager)fragmentConversationsBinding.rvConversations.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    private void loadLocalMessages(){
        if (!dbchat.conversationDetailDao().getAllConversation(conversationByUID).isEmpty()){
            conversationArrayList = getConversationLocally();
            conversationsListAdapter = new ConversationsByUIListAdapter(mContext,conversationArrayList);
            fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);

            isLocalChatLoaded = true;
        }else{
            isLocalChatLoaded = false;
        }
    }

    public void addAudioMessageToList(Conversation conversation, ArrayList<selectedFilePreviewData> selectedFilePreviewData, ArrayList<UploadFilesDataModel> partArrayList) {

        NewChatModel sendMessageModel = new NewChatModel();
        sendMessageModel.agentId = agentId;
        sendMessageModel.tempChatId = selectedFilePreviewData.get(0).getTempChatID();
        sendMessageModel.conversationUId = common.getConversationUUId(mContext);
        sendMessageModel.connectionId = common.getConnectionID(mContext);
        sendMessageModel.customerId = cusId;
        sendMessageModel.groupId = 0;
        sendMessageModel.conversationType = "multimedia";
        sendMessageModel.message = selectedFilePreviewData.get(0).getFileName();
        sendMessageModel.contactNo = customerMobileNumber;
        sendMessageModel.name = customerName;
        sendMessageModel.cnic = customerCNIC;
        sendMessageModel.emailaddress = customerEmail;
        sendMessageModel.source = "Mobile_Android";
        sendMessageModel.isFromWidget = true;
        sendMessageModel.type = "file";
        sendMessageModel.channelid = channelId;
        sendMessageModel.isWelcome = false;
        sendMessageModel.notifyMessage = "";
        sendMessageModel.caption = selectedFilePreviewData.get(0).getMessage();
        sendMessageModel.fileUrl = selectedFilePreviewData.get(0).getFileUri();
        sendMessageModel.documentName = selectedFilePreviewData.get(0).getFileName();
        sendMessageModel.documentorignalname = selectedFilePreviewData.get(0).getFileName();
        sendMessageModel.documentType = selectedFilePreviewData.get(0).getFileType();
        sendMessageModel.callerAppType = 3;
        sendMessageModel.timestamp = getTimeStamp();
        sendMessageModel.voiceDuration = selectedFilePreviewData.get(0).getVoiceDuration();
        addTempItemToList(sendMessageModel, true);

        //upload api call here
        uploadFiles(partArrayList);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (common!=null){
            cusId = common.getCustomerID(mContext);
        }

        EventBus.getDefault().post(new MessageEvent(HIDE_TOOLBAR));
        if (isCalledFromPreviewActivity){
            isCalledFromPreviewActivity = false;
            if(sendFileAfterPreview!=null) {
                if (sendFileAfterPreview.eventType.equalsIgnoreCase("SendFileAfterPreview")) {
                    arrayListData = sendFileAfterPreview.receivedList;
                    if (arrayListData.size() > 0) {
                        for (int i = 0; i < arrayListData.size(); i++) {
                            NewChatModel sendMessageModel = new NewChatModel();
                            sendMessageModel.agentId = agentId;
                            sendMessageModel.tempChatId = arrayListData.get(i).getTempChatID();
                            sendMessageModel.conversationUId = common.getConversationUUId(mContext);
                            sendMessageModel.connectionId = common.getConnectionID(mContext);
                            sendMessageModel.customerId = cusId;
                            sendMessageModel.groupId = 0;
                            sendMessageModel.conversationType = "multimedia";
                            sendMessageModel.message = arrayListData.get(i).getFileName();
                            sendMessageModel.contactNo = customerMobileNumber;
                            sendMessageModel.name = customerName;
                            sendMessageModel.cnic = customerCNIC;
                            sendMessageModel.emailaddress = customerEmail;
                            sendMessageModel.source = "Mobile_Android";
                            sendMessageModel.isFromWidget = true;
                            sendMessageModel.type = "file";
                            sendMessageModel.channelid = channelId;
                            sendMessageModel.isWelcome = false;
                            sendMessageModel.notifyMessage = "";
                            sendMessageModel.caption = arrayListData.get(i).getMessage();
                            sendMessageModel.fileUrl = arrayListData.get(i).getFileUri();
                            sendMessageModel.documentName = arrayListData.get(i).getFileName();
                            sendMessageModel.documentorignalname = arrayListData.get(i).getFileName();
                            sendMessageModel.documentType = arrayListData.get(i).getFileType();
                            sendMessageModel.callerAppType = 3;
                            sendMessageModel.timestamp = getTimeStamp();
                            addTempItemToList(sendMessageModel, true);
                        }
                    }

                     for (int i=0;i<arrayListData.size();i++){
                        for (int j=0;j<fileUploadArrayList.size();j++){
                            if (fileUploadArrayList.get(j).getTempChatID().equalsIgnoreCase(arrayListData.get(i).getTempChatID())){
                               UploadFilesDataModel uploadFilesData1 = new UploadFilesDataModel();
                                uploadFilesData1.setCaption(arrayListData.get(i).getMessage());
                                uploadFilesData1.setTempChatID(fileUploadArrayList.get(j).getTempChatID());
                                uploadFilesData1.setFile(fileUploadArrayList.get(j).getFile());
                                uploadFilesData1.setConversationUId(fileUploadArrayList.get(j).getConversationUId());
                                uploadFilesData1.setFileName(fileUploadArrayList.get(j).getFileName());
                                uploadFilesData1.setContentType(fileUploadArrayList.get(j).getContentType());
                                fileUploadArrayList.set(j,uploadFilesData1);
                            }
                        }
                    }
                    uploadFiles(fileUploadArrayList);
                }
            }

        }else if (isCalledFromPreviewActivityBack){
            isCalledFromPreviewActivityBack = false;
            if(sendFileAfterPreview!=null) {
                sendFileAfterPreview = null;
            }
            if (!fileUploadArrayList.isEmpty()){
                fileUploadArrayList.clear();
            }
            if (!filesNames.isEmpty()){
                filesNames.clear();
            }
        }

    }

    public void getConversationByUID(boolean isLocallyLoaded,int pageNumber,int pageSize,String conversationByUUID,long customerId,boolean isCalledFromAssigned) {
        if (!isLocallyLoaded){
            fragmentConversationsBinding.loadMoreProgress.setVisibility(View.VISIBLE);
        }
        new ApiClient(mContext).getWebService().getConversationByUID(pageNumber,pageSize,conversationByUUID,customerId,customerEmail).enqueue(new Callback<WebResponse2<ArrayList<ConversationByUID>>>() {
            @Override
            public void onResponse(Call<WebResponse2<ArrayList<ConversationByUID>>> call, Response<WebResponse2<ArrayList<ConversationByUID>>> response) {
                if(response.code()==200){
                    if(response.body().getResult().size()>0){

                        if (response != null && response.body() != null
                                && response.body().getResult() != null
                                && !response.body().getResult().isEmpty()
                                && response.body().getResult().get(0).conversationUid != null
                                && !response.body().getResult().get(0).conversationUid.isEmpty()) {

                            common.saveConversationUUId(mContext, response.body().getResult().get(0).conversationUid);
                            common.saveCustomerId(mContext, response.body().getResult().get(0).customerId);
                            conversationByUID = response.body().getResult().get(0).conversationUid;
                            cusId = response.body().getResult().get(0).customerId;

                            EventBus.getDefault().post(new com.example.o2chatsdk.Events.appEvents.MessageEvent( "startSignalR"));

                        } else {
                            String uuid = UUID.randomUUID().toString();
                            common.saveConversationUUId(mContext, uuid);
                            conversationByUID = uuid;
                        }

                        totalPages = response.body().getTotalPages();
                        if(pageNumber==1){
                            if(response.body().getResult().size()>0) {
                                saveConversationsDataIntoDB(false,pageNumber,response.body().getResult());
                            }
                        }else {
                            // check weather is last page or not
                            if (pageNumber <totalPages) {
                                //show loader here
                                if (!isLocallyLoaded){
                                    fragmentConversationsBinding.loadMoreProgress.setVisibility(View.VISIBLE);
                                }
                            } else {
                                isLastPage = true;
                            }
                            isLoading = false;
                            if(response.body().getResult().size()>0){
                                saveConversationsDataIntoDB(false,pageNumber,response.body().getResult());
                            }
                        }
                        if (!conversationArrayList.isEmpty() && conversationsListAdapter.getItemCount()>0){
                            for (int i = 0; i < conversationsListAdapter.getItemCount(); i++) {
                                ConversationByUID conversationByUID1 = conversationArrayList.get(i);
                                if (conversationByUID1!=null){
                                    if (!conversationByUID1.isFromWidget && !conversationByUID1.isSeen && !conversationByUID1.type.equalsIgnoreCase("system")){
                                        readReceiptInvoke(conversationByUID1.id);
                                    }
                                }
                            }
                        }
                    }else{
                        getIsValidBusinessHours();
                        String uuid = UUID.randomUUID().toString();
                        common.saveConversationUUId(mContext, uuid);
                        conversationByUID = uuid;
                    }
                } else{
                    if(response.code()==401){
//                        switch to login
                    }
                }
                if (isAdded()){
                    if(getActivity()!=null){
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() ->
                                fragmentConversationsBinding.loadMoreProgress.setVisibility(View.GONE)
                        , 500);
            }

            @Override
            public void onFailure(Call<WebResponse2<ArrayList<ConversationByUID>>> call, Throwable t) {
                if (isAdded()){
                    if(getActivity()!=null){
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
                if (t.getMessage().contains("Failed to connect")){
                    EventBus.getDefault().post(new ReLoadConversationEvent("Reconnecting"));
                }

                String uuid = UUID.randomUUID().toString();
                common.saveConversationUUId(mContext, uuid);
                conversationByUID = uuid;
            }
        });
    }

    public void uploadFiles(ArrayList<UploadFilesDataModel> partArrayList){

        if (getActivity().getWindow() != null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        new ApiClient(mContext).getWebService().uploadFiles(partArrayList).enqueue(new Callback<WebResponse<ArrayList<UploadFilesData>>>() {
            @Override
            public void onResponse(Call<WebResponse<ArrayList<UploadFilesData>>> call, Response<WebResponse<ArrayList<UploadFilesData>>> response) {
                if (response!=null)
                {
                    if(response.code()==200)
                    {
                        if(response.isSuccessful() && response.body()!=null)
                        {
                            if(response.body().getResult()!=null) {
                                uploadFilesData = response.body().getResult();
                                if(uploadFilesData.size()>0) {
                                    sendNewChat("", "file", fragmentConversationsBinding.edtMessage.getText().toString().trim(), uploadFilesData,arrayListData, "", false);
                                    fragmentConversationsBinding.edtMessage.setText("");
                                }
                            }
                        }
                    }else{
                        if (response.code()==401){
                        }else{

                        }
                    }

                }
                if(getActivity().getWindow()!=null){
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
            @Override
            public void onFailure(Call<WebResponse<ArrayList<UploadFilesData>>> call, Throwable t) {
                if(getActivity().getWindow()!=null){
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                if (t.getMessage().contains("Failed to connect")){
                    EventBus.getDefault().post(new ReLoadConversationEvent("Reconnecting"));
                }
                if (arrayListData.size()>0){
                    for(int i=0;i<arrayListData.size();i++){
                        if (isContainConversationByUIdTemp(conversationArrayList,arrayListData.get(i).getTempChatID())){
                            uploadFilesData = new ArrayList<>();
                            if (filesNames!=null){
                                filesNames.clear();
                            }
                            if(!fileUploadArrayList.isEmpty()){
                                fileUploadArrayList.clear();
                            }
                            if (!conversationArrayList.isEmpty()){
                                ConversationByUID conversationByUID1  = conversationArrayList.get(addedConversationPos);
                                conversationByUID1.isRecieved = false;
                                conversationByUID1.isFailed = true;
                                conversationByUID1.isShowLocalFiles = true;
                                saveAndUpdateTempItemToDB(false,conversationByUID1);
                                conversationArrayList.set(addedConversationPos,conversationByUID1);
                                conversationsListAdapter.notifyItemChanged(addedConversationPos);
                            }
                        }
                    }
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void hideSelectedFilesLayout(){
        if(!filesNames.isEmpty()){
            filesNames.clear();
        }
        if(!fileUploadArrayList.isEmpty()){
            fileUploadArrayList.clear();
        }
    }
    public void scrollToBottom(){
        fragmentConversationsBinding.jumptoBottom.setVisibility(View.GONE);
        fragmentConversationsBinding.rvConversations.postDelayed(() -> fragmentConversationsBinding.rvConversations.scrollToPosition(0), 200);
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
    public void onMessageEvent(EventAddTopicMessage event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("AddMessageToList")){
                if (!event.message.equalsIgnoreCase("")){
                    // addItemToListWelcomeMessage(event.message,common.getDisplayName(mContext));
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AssignedChatEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("AssignChatToAnother")){
                if (event.assignChatListener.conversationUId.equalsIgnoreCase(common.getConversationUUId(mContext))){
                    fragmentConversationsBinding.layoutTyping.setVisibility(View.GONE);

                    //Remove item because it assigned to another agent by admin side
                    if(conversationsListAdapter!=null && !conversationArrayList.isEmpty() && fragmentConversationsBinding!=null) {
                        if (isContainAssignChat(conversationArrayList, event.assignChatListener)) {
                            conversationArrayList.add(fragmentConversationsBinding.rvConversations.getAdapter().getItemCount(), getConversationFromAssignListener(event.assignChatListener));
                            conversationsListAdapter.notifyItemInserted(fragmentConversationsBinding.rvConversations.getAdapter().getItemCount());
                            scrollToBottom();
                        } else {
                            conversationArrayList.add(getConversationFromAssignListener(event.assignChatListener));
                            conversationsListAdapter = new ConversationsByUIListAdapter(mContext, conversationArrayList);
                            fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
                        }
                    }
                }else{
                    fragmentConversationsBinding.layoutTyping.setVisibility(View.VISIBLE);
                }
                handler = new Handler();
                myRunnable = () -> {
                    EventBus.getDefault().post(new MessageEvent("SwitchToConversationList"));
                };
                handler.postDelayed(myRunnable, 1500);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FileDeleteEvent event) {
        if (!event.eventType.isEmpty()) {
            if (event.eventType.equalsIgnoreCase("DeleteFile")) {
                if(event.position!=-1){
                    if(!filesNames.isEmpty() && !fileUploadArrayList.isEmpty()){
                        filesNames.remove(event.position);
                        fileUploadArrayList.remove(event.position);
                        if(selectedFilesListAdapter!=null){
                            selectedFilesListAdapter.notifyItemRemoved(event.position);
                        }
                        if(filesNames.isEmpty() && fileUploadArrayList.isEmpty()){
                            hideSelectedFilesLayout();
                        }
                    }

                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventFileDownload event) {
        if (!event.eventType.isEmpty()) {
            if (event.eventType.equalsIgnoreCase("FileDownload")) {
                storagePermission(event);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEventForResendImage event) {
        if (!event.eventType.isEmpty()) {
            if (event.eventType.equalsIgnoreCase("UploadResendImage")) {
                NewChatModel sendMessageModel = new NewChatModel();
                sendMessageModel.agentId = agentId;
                sendMessageModel.tempChatId = event.tempChatID;
                sendMessageModel.conversationUId = common.getConversationUUId(mContext);
                sendMessageModel.connectionId = common.getConnectionID(mContext);
                sendMessageModel.customerId = cusId;
                sendMessageModel.groupId = groupId ;
                sendMessageModel.conversationType = "multimedia";
                sendMessageModel.message = event.fileName;
                sendMessageModel.contactNo = customerMobileNumber;
                sendMessageModel.name = customerName ;
                sendMessageModel.cnic = customerCNIC;
                sendMessageModel.emailaddress = customerEmail;
                sendMessageModel.source = "Mobile_Android" ;
                sendMessageModel.isFromWidget = true ;
                sendMessageModel.type = "file";
                sendMessageModel.channelid = channelId;
                sendMessageModel.isWelcome = false;
                sendMessageModel.notifyMessage = "";
                sendMessageModel.caption = event.caption;
                sendMessageModel.fileUrl = "";

                for (int j=0;j<event.multipartBodyPart.size();j++){
                        if (event.multipartBodyPart.get(j).getTempChatID().equalsIgnoreCase(event.tempChatID)){
                            UploadFilesDataModel uploadFilesData1 = new UploadFilesDataModel();
                            uploadFilesData1.setCaption(event.caption);
                            uploadFilesData1.setTempChatID(event.multipartBodyPart.get(j).getTempChatID());
                            uploadFilesData1.setFile(event.multipartBodyPart.get(j).getFile());
                            uploadFilesData1.setConversationUId(event.multipartBodyPart.get(j).getConversationUId());
                            uploadFilesData1.setFileName(event.multipartBodyPart.get(j).getFileName());
                            uploadFilesData1.setContentType(event.multipartBodyPart.get(j).getContentType());
                            event.multipartBodyPart.set(j,uploadFilesData1);
                        }
                }
                uploadFiles(event.multipartBodyPart);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (!event.eventType.isEmpty()) {
            if (event.eventType.equalsIgnoreCase("UpdateNameOfOrganization")) {
                fragmentConversationsBinding.orgName.setText(common.getDisplayName(mContext));
                fragmentConversationsBinding.tvCustomText.setText(getString(R.string.lbl_reply_desc));
                exitingCustomText = getString(R.string.lbl_reply_desc);
            }

            if (event.eventType.equalsIgnoreCase("LoadConversationList")) {
                getConversationByUID(isLocalChatLoaded,pageNumber,pageSize,common.getConversationUUId(mContext),cusId,false);
            }

            if (event.eventType.equalsIgnoreCase("LoadLocalChatWhenActivityResume")) {
                if (!dbchat.conversationDetailDao().getAllConversation(conversationByUID).isEmpty()){
                    conversationArrayList = getConversationLocally();
                    conversationsListAdapter = new ConversationsByUIListAdapter(mContext,conversationArrayList);
                    fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
                    isLocalChatLoaded = true;
                }else{
                    isLocalChatLoaded = false;
                }
            }

            if (event.eventType.equalsIgnoreCase("StartSyncResendMessage")){
                resendSchedulerStart();
            }

        }
    }

    public void addWelcomeMessage(){
        //hide topic menu here
        hideAndShowTopicMenu(true);
        if (!businessHoursMessage.isEmpty()){
            temppChatIdWelcomeMessage = UUID.randomUUID().toString();
            addItemToListWelcomeMessage(temppChatIdWelcomeMessage,businessHoursMessage,common.getDisplayName(mContext),true);
        }
    }

    public void LoadTopics(){
        if (topicArrayList.isEmpty()){
            fragmentConversationsBinding.ivMenuTopic.setVisibility(View.GONE);
            fragmentConversationsBinding.lbTopicChannel.setVisibility(View.GONE);
            fragmentConversationsBinding.layoutTyping.setVisibility(View.VISIBLE);

        }else {
            if (!checkLastMessageTopicSelect("Please select your desired option from menu to chat with our representative.")){
                tempChatIdTopicWelcomeMsg = UUID.randomUUID().toString();
                addItemToListWelcomeMessage(tempChatIdTopicWelcomeMsg,"Please select your desired option from menu to chat with our representative.",common.getDisplayName(mContext),false);
            }
            fragmentConversationsBinding.lbTopicChannel.setVisibility(View.VISIBLE);
            fragmentConversationsBinding.layoutTyping.setVisibility(View.GONE);
            TopicListAdapter topicChannelListAdapter = new TopicListAdapter(mContext,topicArrayList);
            fragmentConversationsBinding.recycleTopicChannel.setAdapter(topicChannelListAdapter);
        }
    }

    public void hideAndShowTopicMenu(boolean isHide){
        if (isHide){
            fragmentConversationsBinding.lbTopicChannel.setVisibility(View.GONE);
            fragmentConversationsBinding.layoutTyping.setVisibility(View.VISIBLE);
            // Hide menu here
            fragmentConversationsBinding.ivMenuTopic.setVisibility(View.GONE);
        }else{
            fragmentConversationsBinding.lbTopicChannel.setVisibility(View.GONE);
            fragmentConversationsBinding.layoutTyping.setVisibility(View.VISIBLE);
            // show menu here
            fragmentConversationsBinding.ivMenuTopic.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReceiveMessageEvent event) {
        if (!event.eventType.isEmpty()) {
            fragmentConversationsBinding.ivSendMessage.setVisibility(View.VISIBLE);
            fragmentConversationsBinding.progressSend.setVisibility(View.GONE);
            if (uploadFilesData!=null){
                if(!uploadFilesData.isEmpty()){
                    uploadFilesData.clear();
                }
            }
            if (event.eventType.equalsIgnoreCase("ReceiveMessage")) {
                if (!event.recieveMessage.type.equalsIgnoreCase("welcomeMessage")){
                    if(conversationByUID.equalsIgnoreCase(event.recieveMessage.conversationUid)){
                        if(conversationsListAdapter!=null && !conversationArrayList.isEmpty() && fragmentConversationsBinding!=null){
                            if (conversationByUID.equalsIgnoreCase(event.recieveMessage.conversationUid)){
                                if (!event.recieveMessage.tempChatId.equalsIgnoreCase("")){
                                    if (isContainConversationByUIdTemp(conversationArrayList,event.recieveMessage.tempChatId)){
                                        ConversationByUID conversationByUID1  = common.getConversationFromReceiveMsg(event.recieveMessage);
                                        conversationByUID1.isUpdateStatus = true;
                                        saveAndUpdateTempItemToDB(false,conversationByUID1);
                                        conversationArrayList.set(addedConversationPos,conversationByUID1);
                                        conversationsListAdapter.notifyItemChanged(addedConversationPos);
                                        scrollToBottom();

                                    }else{
                                        ConversationByUID conversationByUID1 = common.getConversationFromReceiveMsg(event.recieveMessage);
                                        conversationByUID1.isUpdateStatus = true;
                                        saveAndUpdateTempItemToDB(true,conversationByUID1);
                                        conversationArrayList.add(0, conversationByUID1);
                                        conversationsListAdapter.notifyItemInserted(0);
                                        scrollToBottom();

                                        fragmentConversationsBinding.lbTopicChannel.setVisibility(View.GONE);
                                        fragmentConversationsBinding.ivMenuTopic.setVisibility(View.VISIBLE);
                                        fragmentConversationsBinding.layoutTyping.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    ConversationByUID conversationByUID1 = common.getConversationFromReceiveMsg(event.recieveMessage);
                                    conversationByUID1.isUpdateStatus = true;
                                    saveAndUpdateTempItemToDB(true,conversationByUID1);
                                    conversationsListAdapter.notifyItemInserted(0);
                                    scrollToBottom();
                                }
                            }else{
                                ConversationByUID conversationByUID1 = common.getConversationFromReceiveMsg(event.recieveMessage);
                                conversationByUID1.isUpdateStatus = true;
                                saveAndUpdateTempItemToDB(true,conversationByUID1);
                                conversationsListAdapter = new ConversationsByUIListAdapter(mContext,conversationArrayList);
                                fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
                            }
                        }else{
                            ConversationByUID conversationByUID1 = common.getConversationFromReceiveMsg(event.recieveMessage);
                            conversationByUID1.isUpdateStatus = true;
                            saveAndUpdateTempItemToDB(true,conversationByUID1);
                            conversationsListAdapter = new ConversationsByUIListAdapter(mContext,conversationArrayList);
                            fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
                        }
                        if (!event.recieveMessage.isFromWidget && !event.recieveMessage.isSeen && !event.recieveMessage.type.equalsIgnoreCase("system")){
                            readReceiptInvoke(event.recieveMessage.id);
                        }
                    }
                } else{
                    ConversationByUID conversationByUID1 = common.getConversationFromReceiveMsg(event.recieveMessage);
                    conversationByUID1.isUpdateStatus = true;
                    if (conversationByUID1.id!=0){
                        saveAndUpdateTempItemToDB(false,conversationByUID1);
                    }else{
                        saveAndUpdateTempItemToDB(true,conversationByUID1);
                    }
                }

                if (isTopicSelect && !topicArrayList.isEmpty()) {
                    isTopicSelect = false;
                    if (common.getIsResolved(mContext) && !checkLastMessageIsBusinessHour("Please select your desired option from menu to chat with our representative.")){
                        sendNewChat("","welcomeMessage", "Please select your desired option from menu to chat with our representative.", uploadFilesData,arrayListData, tempChatIdTopicWelcomeMsg,true);
                    }
                    if (!isTopicSecondTimeSelect){
                        isTopicSecondTimeSelect = false;
                        handlerBulk = new Handler(Looper.getMainLooper());
                        //do your work here
                        handlerBulk.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendNewChat("","text", topicName, uploadFilesData,arrayListData, UUID.randomUUID().toString(),true);
                            }
                        }, 500);
                        handlerBulk = new Handler(Looper.getMainLooper());
                        //do your work here
                        handlerBulk.postDelayed(this::sendWelcomeMessageTopic, 1000);
                    } else{
                        handlerBulk = new Handler(Looper.getMainLooper());
                        //do your work here
                        handlerBulk.postDelayed(this::sendWelcomeMessageTopic, 1000);
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReadReceiptUpdateEvent event) {
        if (!event.eventType.isEmpty()) {
            fragmentConversationsBinding.ivSendMessage.setVisibility(View.VISIBLE);
            fragmentConversationsBinding.progressSend.setVisibility(View.GONE);
            if(!uploadFilesData.isEmpty()){
                uploadFilesData.clear();
            }
            if (event.eventType.equalsIgnoreCase("UpdateReceiptEvent")) {
                if(conversationByUID.equalsIgnoreCase(event.readReceiptRequest.conversationUId)){
                    if(conversationsListAdapter!=null && !conversationArrayList.isEmpty() && fragmentConversationsBinding!=null){
                        if (conversationByUID.equalsIgnoreCase(event.readReceiptRequest.conversationUId)){
                            if (event.readReceiptRequest.conversationDetailId != 0){
                                if (getConversationById(conversationArrayList,event.readReceiptRequest.conversationDetailId)!=null){
                                    ConversationByUID conversationByUID1 = getConversationFromReadReceipt(getConversationById(conversationArrayList,event.readReceiptRequest.conversationDetailId));
                                    if (conversationByUID1.isFromWidget && !conversationByUID1.isSeen && conversationByUID1!=null){
                                        conversationByUID1.isUpdateStatus = true;
                                        conversationByUID1.isSeen = event.readReceiptRequest.isSeen;
                                        conversationArrayList.set(addedConversationPos,conversationByUID1);
                                        conversationsListAdapter.notifyItemChanged(addedConversationPos);
                                        saveAndUpdateTempItemToDB(true,conversationByUID1);
                                        //scrollToBottom();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnErrorData event) {
        if (!event.eventType.isEmpty()) {
            if (event.eventType.equalsIgnoreCase("OnErrorMessage")) {
                if (event.recieveMessage!=null){
                    if(event.recieveMessage!=null && !event.recieveMessage.isEmpty()){
                        Toast.makeText(mContext, event.recieveMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    public ConversationByUID getConversationFromResolveListener(ConversationStatusListenerDataModel conversationStatusListenerDataModel,ResolveFeedBackRequest feedBackRequest,boolean isFeedback){

        ConversationByUID conversationByUID = new ConversationByUID();
        conversationByUID.conversationType = "text";
        conversationByUID.type = "system";
        conversationByUID.conversationUid = conversationStatusListenerDataModel.conversationUid;
        conversationByUID.toUserId = 0;
        conversationByUID.customerName = "";
        conversationByUID.sender = "";
        conversationByUID.agentId = 0;
        conversationByUID.customerId = common.getCustomerID(mContext);
        conversationByUID.content = conversationStatusListenerDataModel.notifyMessage;
        conversationByUID.files = new ArrayList<>();
        conversationByUID.fromUserId = 0;
        conversationByUID.isFromWidget = false;
        conversationByUID.isFeedback = isFeedback;
        conversationByUID.id = conversationStatusListenerDataModel.conversationId;
        conversationByUID.isPrivate = false;
        conversationByUID.groupId = -1;
        conversationByUID.groupName = "";
        conversationByUID.receiver = "";
        conversationByUID.pageId = "";
        conversationByUID.pageName = "";
        conversationByUID.tiggerevent = 0;
        conversationByUID.timestamp = conversationStatusListenerDataModel.timestamp;
        conversationByUID.isResolved = conversationStatusListenerDataModel.isResolved;
        conversationByUID.rating = String.valueOf(feedBackRequest.rating);
        conversationByUID.feedback = feedBackRequest.feedback;

        return conversationByUID;
    }

    public ConversationByUID getConversationFromAssignListener(AssignChatListener assignChatListener){
        ConversationByUID conversationByUID = new ConversationByUID();
        conversationByUID.conversationType = "text";
        conversationByUID.type = "system";
        conversationByUID.conversationUid = assignChatListener.conversationUId;
        conversationByUID.toUserId = 0;
        conversationByUID.customerName = "";
        conversationByUID.sender = "";
        conversationByUID.agentId = 0;
        conversationByUID.customerId = assignChatListener.customerId;
        conversationByUID.content = assignChatListener.notifyMessage;
        conversationByUID.files = new ArrayList<>();
        conversationByUID.fromUserId = 0;
        conversationByUID.isFromWidget = false;
        conversationByUID.isPrivate = false;
        conversationByUID.groupId = assignChatListener.groupId;
        conversationByUID.groupName = assignChatListener.groupName;
        conversationByUID.timestamp = assignChatListener.timestamp;
        conversationByUID.receiver = "";
        conversationByUID.pageId = "";
        conversationByUID.pageName = "";
        conversationByUID.tiggerevent = 0;

        return conversationByUID;
    }

    public ConversationByUID addSendMessageTemp(NewChatModel recieveMessage){

        if (recieveMessage.type.equalsIgnoreCase("file")){
            ArrayList<FilesData> arrayList = new ArrayList<>();
            ConversationByUID conversation = new ConversationByUID();
            conversation.type = recieveMessage.type;
            conversation.conversationType = recieveMessage.type.equalsIgnoreCase("file") ? "multimedia" : "text";
            conversation.conversationUid = recieveMessage.conversationUId;
            conversation.toUserId = 0;
            conversation.customerName = recieveMessage.name;
            conversation.sender = "";
            conversation.agentId = recieveMessage.agentId;
            conversation.customerId = recieveMessage.customerId;
            conversation.content = recieveMessage.message;
            FileDataClass fileDataClass = filesNames.get(0);
            FilesData filesData = new FilesData();
            filesData.url = recieveMessage.fileUrl;
            filesData.type = recieveMessage.documentType;
            filesData.documentName = recieveMessage.documentName;
            filesData.tempChatId = recieveMessage.tempChatId;
            filesData.messagedId = 0;
            filesData.isLocalFile = true;
            arrayList.add(filesData);
            conversation.files = arrayList;
            conversation.fromUserId = 0;
            conversation.isFromWidget = recieveMessage.isFromWidget;
            conversation.isPrivate = false;
            conversation.groupId = recieveMessage.groupId;
            conversation.groupName = "";
            conversation.timestamp = recieveMessage.timestamp;
            conversation.receiver = "";
            conversation.pageId = "";
            conversation.voiceDuration = recieveMessage.voiceDuration;
            conversation.caption = recieveMessage.caption;
            conversation.pageName = "";
            conversation.tiggerevent = 0;
            conversation.fileLocalUri = fileDataClass.url;
            conversation.tempChatId = recieveMessage.tempChatId;
            conversation.isNotNewChat = false;
            conversation.isUpdateStatus = false;
            conversation.isShowLocalFiles = true;
            conversation.isWelcome = recieveMessage.isWelcome;
            conversation.voiceDuration = recieveMessage.voiceDuration;
            fragmentConversationsBinding.ivSendMessage.setVisibility(View.VISIBLE);
            fragmentConversationsBinding.progressSend.setVisibility(View.GONE);
            return conversation;
        }else{
            ConversationByUID conversation = new ConversationByUID();
            conversation.conversationType = recieveMessage.type.equalsIgnoreCase("file") ? "multimedia" : "text";
            conversation.type = recieveMessage.type;
            conversation.conversationUid = recieveMessage.conversationUId;
            conversation.toUserId = 0;
            conversation.customerName = recieveMessage.name;
            conversation.sender = "";
            conversation.agentId = recieveMessage.agentId;
            conversation.customerId = recieveMessage.customerId;
            conversation.content = recieveMessage.message;
            conversation.fromUserId = 0;
            conversation.isFromWidget = recieveMessage.isFromWidget;
            conversation.isPrivate = false;
            conversation.groupId = recieveMessage.groupId;
            conversation.groupName = "";
            conversation.timestamp = recieveMessage.timestamp;
            conversation.receiver = "";
            conversation.pageId = "";
            conversation.pageName = "";
            conversation.caption = recieveMessage.caption;
            conversation.tiggerevent = 0;
            conversation.voiceDuration = recieveMessage.voiceDuration;
            conversation.isWelcome = recieveMessage.isWelcome;
            conversation.tempChatId = recieveMessage.tempChatId;
            conversation.isNotNewChat = false;
            conversation.isUpdateStatus = false;
            conversation.isShowLocalFiles = false;

            return conversation;
        }
    }

    public ConversationByUID getConversationFromReadReceipt(ConversationByUID recieveMessage)
    {
        ConversationByUID conversationByUID = new ConversationByUID();
        conversationByUID.conversationType = recieveMessage.type.equalsIgnoreCase("file") ? "multimedia" : "text";
        conversationByUID.type = recieveMessage.type;
        conversationByUID.conversationUid = recieveMessage.conversationUid;
        conversationByUID.toUserId = recieveMessage.toUserId;
        conversationByUID.customerName = recieveMessage.customerName;
        conversationByUID.caption = recieveMessage.caption;
        conversationByUID.sender = recieveMessage.sender;
        conversationByUID.agentId = recieveMessage.agentId;
        conversationByUID.customerId = recieveMessage.customerId;
        conversationByUID.content = recieveMessage.content;
        conversationByUID.files = recieveMessage.files;
        conversationByUID.fromUserId = recieveMessage.fromUserId;
        conversationByUID.isFromWidget = recieveMessage.isFromWidget;
        conversationByUID.isPrivate = recieveMessage.isPrivate;
        conversationByUID.groupId = recieveMessage.groupId;
        conversationByUID.groupName = recieveMessage.groupName;
        conversationByUID.timestamp = recieveMessage.timestamp;
        conversationByUID.receiver = recieveMessage.receiver;
        conversationByUID.pageId = recieveMessage.pageId;
        conversationByUID.pageName = recieveMessage.pageName;
        conversationByUID.tiggerevent = recieveMessage.tiggerevent;
        conversationByUID.isUpdateStatus = false;
        conversationByUID.isNotNewChat = true;
        conversationByUID.isSeen = false;
        conversationByUID.id = recieveMessage.id;
        return conversationByUID;
    }

    public boolean isContainAssignChat(ArrayList<ConversationByUID> conversationArrayList, AssignChatListener assignChatListener){
        for (int i=0;i<conversationArrayList.size();i++){
            if(conversationArrayList.get(i).customerId == assignChatListener.customerId){
                addedConversationPos = i;
                return true;
            }
        }
        return false;
    }
    public ConversationByUID getConversationById(ArrayList<ConversationByUID> conversationArrayList, long chatId){
        for (int i=0;i<conversationArrayList.size();i++){
            if(conversationArrayList.get(i).id == chatId){
                addedConversationPos = i;
                return conversationArrayList.get(i);
            }
        }
        return null;
    }

    public boolean isContainConversationByUIdTemp(ArrayList<ConversationByUID> conversationArrayList, String tempChatId){
        for (int i=0;i<conversationArrayList.size();i++){
            if(conversationArrayList.get(i).tempChatId.equalsIgnoreCase(tempChatId)){
                addedConversationPos = i;
                return true;
            }
        }
        return false;
    }

    public boolean isContainConversationByID(ArrayList<ConversationByUID> conversationArrayList, long conversationID){
        for (int i=0;i<conversationArrayList.size();i++){
            if(conversationArrayList.get(i).id == conversationID){
                addedConversationPos = i;
                return true;
            }
        }
        return false;
    }

    public void readReceiptInvoke(long ConversationDetailId){
        ReadReceiptRequest readRequestModel =  new ReadReceiptRequest();
        readRequestModel.callerAppType = 3;
        readRequestModel.id = cusId;
        readRequestModel.isSeen = true;
        readRequestModel.conversationDetailId = ConversationDetailId;
        readRequestModel.conversationUId = conversationByUID;
        if (conversationArrayList!=null && !conversationArrayList.isEmpty()){
            if (isContainConversationByID(conversationArrayList,readRequestModel.conversationDetailId)){
                ConversationByUID conversationByUID1 = conversationArrayList.get(addedConversationPos);
                if (conversationByUID1!=null){
                    conversationByUID1.isSeen = true;
                    conversationArrayList.set(addedConversationPos,conversationByUID1);
                    saveAndUpdateTempItemToDB(true,conversationByUID1);
                }
            }
        }
        EventBus.getDefault().post(new SendReceiptEvent("ReadReceiptEvent",readRequestModel));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scheduler!=null){
            scheduler.shutdownNow();
            scheduler = null;
        }
        MainActivityChat.isPause = false;
        if (!temppChatIdWelcomeMessage.isEmpty()){
            if (dbchat.conversationDetailDao().getConversationWelcomeMsgIdIsZeroTempID(temppChatIdWelcomeMessage)!=null){
                dbchat.conversationDetailDao().deleteWelcomeMsgByTempChatID(temppChatIdWelcomeMessage);
            }
        }
        EventBus.getDefault().post(new MessageEvent("ShowToolbar"));
    }

    public String getTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date());
    }

    public void sendNewChat(String caption, String type, String txtMessage, ArrayList<UploadFilesData> uploadFilesData, ArrayList<selectedFilePreviewData> selectedFilePreviewData, String tempChatID, boolean isTopic){

        if (common!=null){
            common.setIsResolved(mContext,false);
        }
        if (type.equalsIgnoreCase("file")){
            if(uploadFilesData.size()>0){
                for (int i=0;i<uploadFilesData.size();i++){
                    NewChatModel sendMessageModel = new NewChatModel();
                    sendMessageModel.agentId = agentId;
                    sendMessageModel.tempChatId = uploadFilesData.get(i).tempChatId;
                    sendMessageModel.conversationUId = common.getConversationUUId(mContext);
                    sendMessageModel.connectionId = common.getConnectionID(mContext);
                    sendMessageModel.customerId = cusId ;
                    sendMessageModel.groupId = isTopic ? groupId : 0 ;
                    sendMessageModel.contactNo = customerMobileNumber;
                    sendMessageModel.name = customerName;
                    sendMessageModel.cnic = customerCNIC;
                    sendMessageModel.conversationType = type.equalsIgnoreCase("file") ? "multimedia" : "text";
                    sendMessageModel.emailaddress = customerEmail;
                    sendMessageModel.message = uploadFilesData.get(i).documentOrignalName;
                    sendMessageModel.documentName = uploadFilesData.get(i).documentName;
                    sendMessageModel.documentorignalname = uploadFilesData.get(i).documentOrignalName ;
                    sendMessageModel.documentType = uploadFilesData.get(i).documentType ;
                    sendMessageModel.source = "Mobile_Android" ;
                    sendMessageModel.isFromWidget = true ;
                    sendMessageModel.type = type;
                    sendMessageModel.channelid = channelId;
                    sendMessageModel.notifyMessage = "";
                    sendMessageModel.caption = uploadFilesData.get(i).caption;
                    sendMessageModel.mobileToken = common.getFcmToken(mContext);
                    sendMessageModel.timezone = common.getTimeZone();
                    sendMessageModel.timestamp = getTimeStamp();
                    sendMessageModel.topicId = topicID;
                    sendMessageModel.topicMessage = topicMessage;
                    sendMessageModel.callerAppType = 3;
                    if (dbchat!=null){
                        dbchat.conversationDetailDao().insertNewChat(getNewChatModel(sendMessageModel,isTopic));
                    }
                    if (filesNames!=null){
                        filesNames.clear();
                    }
                    if(!fileUploadArrayList.isEmpty()){
                        fileUploadArrayList.clear();
                    }
                    EventBus.getDefault().post(new SendNewChatEvent(sendMessageModel,"SendNewChatMessage"));
                }
            }
        }else {
            NewChatModel sendMessageModel = new NewChatModel();
            sendMessageModel.agentId = agentId;
            sendMessageModel.tempChatId = tempChatID;
            sendMessageModel.conversationUId = common.getConversationUUId(mContext);
            sendMessageModel.connectionId = common.getConnectionID(mContext);
            sendMessageModel.customerId = cusId;
            sendMessageModel.groupId = isTopic ? groupId : 0 ;
            sendMessageModel.conversationType = type.equalsIgnoreCase("file") ? "multimedia" : "text";
            sendMessageModel.contactNo = customerMobileNumber;
            sendMessageModel.name = customerName ;
            sendMessageModel.cnic = customerCNIC;
            sendMessageModel.emailaddress = customerEmail;
            sendMessageModel.message = txtMessage;
            sendMessageModel.documentName = "";
            sendMessageModel.documentorignalname = "";
            sendMessageModel.documentType = "" ;
            sendMessageModel.source = "Mobile_Android" ;
            sendMessageModel.isFromWidget = true;
            sendMessageModel.type = type;
            sendMessageModel.channelid = common.getChannelID(mContext);
            sendMessageModel.notifyMessage = "";
            sendMessageModel.caption = "";
            sendMessageModel.mobileToken = common.getFcmToken(mContext);
            sendMessageModel.timezone = common.getTimeZone();
            sendMessageModel.timestamp = getTimeStamp();
            sendMessageModel.callerAppType = 3;
            sendMessageModel.topicId = topicID;
            sendMessageModel.topicMessage = topicMessage;
            if (type.equalsIgnoreCase("welcomeMessage")){
                sendMessageModel.isWelcome = true;
            }else{
                sendMessageModel.isWelcome = false;
            }
            EventBus.getDefault().post(new SendNewChatEvent(sendMessageModel, "SendNewChatMessage"));
            if (!type.equalsIgnoreCase("welcomeMessage")){
                addTempItemToList(sendMessageModel,true);
                //adding new chat message to local db to check either item send of not
            }
            dbchat.conversationDetailDao().insertNewChat(getNewChatModel(sendMessageModel,isTopic));
            // start scheduler forat resend message after 10 second
            //resendSchedulerStart();
        }
    }

    public NewChatEntity getNewChatModel(NewChatModel newChatModel,boolean isTopic){
        NewChatEntity newChatEntity = new NewChatEntity();
        newChatEntity.agentId = newChatModel.agentId;
        newChatEntity.caption = newChatModel.caption;
        newChatEntity.tempChatId = newChatModel.tempChatId;
        newChatEntity.conversationType = newChatModel.conversationType;
        newChatEntity.conversationUId = newChatModel.conversationUId;
        newChatEntity.connectionId = newChatModel.connectionId;
        newChatEntity.channelid = newChatModel.channelid;
        newChatEntity.cnic = newChatModel.cnic;
        newChatEntity.contactNo = newChatModel.contactNo;
        newChatEntity.customerId = newChatModel.customerId;
        newChatEntity.documentName = newChatModel.documentName;
        newChatEntity.documentOrignalname = newChatModel.documentorignalname;
        newChatEntity.documentType = newChatModel.documentType;
        newChatEntity.emailaddress = newChatModel.emailaddress;
        newChatEntity.isRecieved = false;
        newChatEntity.isFromWidget = newChatModel.isFromWidget;
        newChatEntity.mobileToken = newChatModel.mobileToken;
        newChatEntity.name = newChatModel.name;
        newChatEntity.isResolved = newChatModel.isResolved;
        newChatEntity.isWelcome = newChatModel.isWelcome;
        newChatEntity.notifyMessage = newChatModel.notifyMessage;
        newChatEntity.source = newChatModel.source;
        newChatEntity.timezone = newChatModel.timezone;
        newChatEntity.type = newChatModel.type;
        newChatEntity.message = newChatModel.message;
        newChatEntity.groupId = isTopic ? newChatModel.groupId : 0;
        newChatEntity.fileUri = newChatModel.fileUrl;
        newChatEntity.timeStamp = newChatModel.timestamp;
        newChatEntity.topicId = newChatModel.topicId;
        newChatEntity.topicMessage = newChatModel.topicMessage;
        return newChatEntity;
    }

    public void addTempItemToList(NewChatModel sendMessageModel,boolean isAddedToDB){
        if (conversationArrayList.isEmpty()) {
            conversationArrayList.add(addSendMessageTemp(sendMessageModel));
            conversationsListAdapter = new ConversationsByUIListAdapter(mContext, conversationArrayList);
            fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
            if (isAddedToDB){
                saveAndUpdateTempItemToDB(false,addSendMessageTemp(sendMessageModel));
            }
        }else{
            conversationArrayList.add(0, addSendMessageTemp(sendMessageModel));
            conversationsListAdapter.notifyItemInserted(0);
            scrollToBottom();

            if (isAddedToDB) {
                saveAndUpdateTempItemToDB(false, addSendMessageTemp(sendMessageModel));
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
        conversationData.setCreatedOn(common.covertTimeStampToDate(conversationByUID1.timestamp));
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
                        dbchat.conversationDetailDao().updateConversationByID(conversationByUID1.isSeen,
                                conversationByUID1.id,
                                conversationByUID1.customerId,
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
                                    conversationByUID1.voiceDuration
                            );
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
                        conversationDetailFile.setmessagedId(conversationByUID1.files.get(i).messagedId);
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
                        dbchat.conversationDetailDao().updateConversationByTempChatID(conversationByUID1.isSeen,
                                conversationByUID1.id,
                                conversationByUID1.customerId,
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
                            int position =i;
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

    private void addItemToListWelcomeMessage(String temppChatId,String message,String displayName,boolean isAddedToDB){
        NewChatModel sendMessageModel = new NewChatModel();
        sendMessageModel.agentId = agentId;
        sendMessageModel.tempChatId = temppChatId;
        sendMessageModel.conversationUId = common.getConversationUUId(mContext);;
        sendMessageModel.connectionId = "";
        sendMessageModel.customerId = cusId ;
        sendMessageModel.groupId = groupId ;
        sendMessageModel.contactNo = customerMobileNumber;
        sendMessageModel.name = displayName ;
        sendMessageModel.cnic = customerCNIC;
        sendMessageModel.emailaddress = customerEmail;
        sendMessageModel.message = message;
        sendMessageModel.documentName = "";
        sendMessageModel.documentorignalname = "";
        sendMessageModel.documentType = "" ;
        sendMessageModel.source = "Mobile_Android" ;
        sendMessageModel.isFromWidget = false ;
        sendMessageModel.type = "welcomeMessage";
        sendMessageModel.channelid = channelId;
        sendMessageModel.notifyMessage = "";
        sendMessageModel.mobileToken = common.getFcmToken(mContext);
        sendMessageModel.timezone = "UTC" ;
        sendMessageModel.timestamp = getTimeStamp() ;
        sendMessageModel.topicId = topicID ;
        sendMessageModel.topicMessage = topicMessage ;
        sendMessageModel.isWelcome = true ;
        addTempItemToList(sendMessageModel,isAddedToDB);
    }

    public void sendMessage(String caption,Conversation conversation,String type,String txtMessage,ArrayList<UploadFilesData> uploadFilesData, ArrayList<selectedFilePreviewData> arrayListData){
        if (type.equalsIgnoreCase("file")){
            if(uploadFilesData.size()>0){
                    SendMessageModel sendMessageModel = new SendMessageModel();
                    sendMessageModel.conversationDetailId = conversation.id;
                    sendMessageModel.tempChatId = conversation.tempChatId;
                    sendMessageModel.agentId = conversation.agentId;
                    sendMessageModel.conversationUid = conversation.conversationUid;
                    sendMessageModel.conversationId = conversation.conversationUid;
                    sendMessageModel.customerId = conversation.customerId;
                    sendMessageModel.message = conversation.files.get(0).documentOriginalName;
                    sendMessageModel.receiverConnectionId = common.getConnectionID(mContext);
                    sendMessageModel.receiverName = conversation.customerName;
                    sendMessageModel.isFromWidget = true;
                    sendMessageModel.type = type;
                    sendMessageModel.groupId = conversation.groupId;
                    sendMessageModel.timestamp = conversation.timestamp;
                    sendMessageModel.conversationType = type.equalsIgnoreCase("file") ? "multimedia" : "text";
                    sendMessageModel.documentName = conversation.files.get(0).documentName;
                    sendMessageModel.documentOrignalname = conversation.files.get(0).documentOriginalName;
                    sendMessageModel.documentType = conversation.files.get(0).type;
                    sendMessageModel.fileUrl = conversation.files.get(0).url;
                    sendMessageModel.icon = "";
                    sendMessageModel.pageId = "";
                    sendMessageModel.pageName = "";
                    sendMessageModel.caption = caption;
                    sendMessageModel.timezone = "UTC";
                    EventBus.getDefault().post(new SendChatEvent(sendMessageModel, "SendNewMessage"));
            }
        }else{
            SendMessageModel sendMessageModel = new SendMessageModel();
            sendMessageModel.conversationDetailId = conversation.id;
            sendMessageModel.customerId = conversation.customerId ;
            sendMessageModel.tempChatId = conversation.tempChatId;
            sendMessageModel.agentId = conversation.agentId;
            sendMessageModel.conversationUid = conversation.conversationUid ;
            sendMessageModel.conversationId = conversation.conversationUid ;
            sendMessageModel.message = txtMessage;
            sendMessageModel.receiverConnectionId =  common.getConnectionID(mContext);
            sendMessageModel.receiverName = conversation.customerName;
            sendMessageModel.isFromWidget = true ;
            sendMessageModel.type = type;
            sendMessageModel.groupId = conversation.groupId;
            sendMessageModel.conversationType = type.equalsIgnoreCase("file") ? "multimedia" : "text";
            sendMessageModel.documentOrignalname = "" ;
            sendMessageModel.documentName = "";
            sendMessageModel.documentType = "" ;
            sendMessageModel.timestamp = conversation.timestamp;
            sendMessageModel.icon = "" ;
            sendMessageModel.pageId = "" ;
            sendMessageModel.pageName = "" ;
            sendMessageModel.caption = caption ;
            sendMessageModel.timezone = "UTC" ;
            EventBus.getDefault().post(new SendChatEvent(sendMessageModel,"SendNewMessage"));
        }
    }
    private void uploadImageDialog(final Context context) {
        Dialog dialog = new Dialog(context);
        View newUserView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        newUserView = inflater.inflate(R.layout.upload_img_dialoge, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newUserView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_border_rectangle));
        dialog.setContentView(newUserView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        //show dialog
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        ImageView btnSelectImg = newUserView.findViewById(R.id.btnSelectImg);
        ImageView btnCaptureImg = newUserView.findViewById(R.id.btnCaptureImg);
        ImageView btnCancel = newUserView.findViewById(R.id.btnCancel);

        btnSelectImg.setOnClickListener(view -> {
            // select image from gallery
            storagePermission(true,false,dialog);
        });

        btnCaptureImg.setOnClickListener(view -> {
            // open camera
            storagePermission(false,false,dialog);
        });

        btnCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();

    }

    private void storagePermission(MessageEventFileDownload messageEventFileDownload){
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        PermissionHelper.grantMultiplePermissions(getActivity(), permissionList, new PermissionHelper.PermissionInterface() {
            @Override
            public void onSuccess() {
                if (!messageEventFileDownload.files.isEmpty()){
                    if (!messageEventFileDownload.files.get(0).url.isEmpty()) {
                        String rootPath = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/O2Chat/";
                        File root = new File(rootPath);
                        if (!root.exists()) {
                            root.mkdirs();
                        }
                        File f = new File(rootPath + messageEventFileDownload.documentName);
                        if (f.exists()) {
                            //open file here
                            try {
                                common.openFile(mContext,f);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            if (messageEventFileDownload.files.get(0).isLocalFile){
                                ConversationByUID conversationByUID = messageEventFileDownload.conversationByUID;
                                if (messageEventFileDownload.conversationByUID!=null){
                                    conversationByUID.isShowLocalFiles = true;
                                    if(conversationsListAdapter!=null && messageEventFileDownload.position!=-1 && conversationsListAdapter.getItemCount()>0){
                                        conversationsListAdapter.notifyItemChanged(messageEventFileDownload.position,conversationByUID);
                                    }
                                }
                            }else{
                                ConversationByUID conversationByUID = messageEventFileDownload.conversationByUID;
                                DownloadImpl.getInstance(mContext)
                                        .url(messageEventFileDownload.files.get(0).url).target(f)
                                        .enqueue(new DownloadListenerAdapter() {
                                            @Override
                                            public void onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, Extra extra) {
                                                super.onStart(url, userAgent, contentDisposition, mimetype, contentLength, extra);
                                                if (messageEventFileDownload.conversationByUID!=null){
                                                    conversationByUID.isDownloading = true;
                                                    conversationByUID.isShowLocalFiles = false;
                                                    if(conversationsListAdapter!=null && messageEventFileDownload.position!=-1 && conversationsListAdapter.getItemCount()>0){
                                                        conversationsListAdapter.notifyItemChanged(messageEventFileDownload.position,conversationByUID);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onProgress(String url, long downloaded, long length, long usedTime) {
                                                super.onProgress(url, downloaded, length, usedTime);
                                                //Log.i("TAG", " progress:" + downloaded + " url:" + url);
                                            }

                                            @Override
                                            public boolean onResult(Throwable throwable, Uri path, String url, Extra extra) {

                                                handler = new Handler();
                                                myRunnable = () -> {
                                                    // Things to be done
                                                    try {
                                                        if (messageEventFileDownload.conversationByUID!=null){
                                                            conversationByUID.isDownloading = false;
                                                            if(conversationsListAdapter!=null && messageEventFileDownload.position!=-1 && conversationsListAdapter.getItemCount()>0){
                                                                conversationsListAdapter.notifyItemChanged(messageEventFileDownload.position,conversationByUID);
                                                            }
                                                        }
                                                        if(common!=null){
                                                            common.openFile(mContext,new File(path.getPath()));
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                };
                                                handler.postDelayed(myRunnable, 1200);
                                                //Toast.makeText(mContext, "File Saved", Toast.LENGTH_SHORT).show();
                                                return super.onResult(throwable, path, url, extra);
                                            }
                                        });
                            }
                        }
                    }
                }

            }

            @Override
            public void onError() {
                storagePermission(messageEventFileDownload);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new MessageEvent("ShowToolbar"));
        if(handler!=null){
            handler.removeCallbacks(myRunnable);
        }
        if(handlerBulk!=null){
            handlerBulk.removeCallbacksAndMessages(null);
        }
    }

    private void storagePermission(boolean openGalleryStatus, boolean isFileAttach, Dialog dialog) {
        ArrayList<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.CAMERA);
        PermissionHelper.grantMultiplePermissions(getActivity(), permissionList, new PermissionHelper.PermissionInterface() {
            @Override
            public void onSuccess() {
                if (isFileAttach){
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    String [] mimeTypes = {"image/*","application/msword","application/doc","application/docx", "application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, PICK_IMAGE_FOR_SELECT);
                }
                else if (openGalleryStatus) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    String[] mimeTypes = {"image/*"};
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, PICK_IMAGE_FOR_SELECT);
                    if(dialog!=null&& dialog.isShowing()){
                        dialog.dismiss();
                    }

                } else {
                    if(dialog!=null&& dialog.isShowing()){
                        dialog.dismiss();
                    }
                    dispatchTakePictureIntent(CAPTURE_PICTURE_FROM_CAMERA);
                }

            }

            @Override
            public void onError() {

            }
        });
    }

    private void dispatchTakePictureIntent(int requearCode) {
        if (mContext!=null){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI;
                    if (Build.VERSION.SDK_INT >= 24) {
                        photoURI = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", photoFile);
                    }else{
                        photoURI = Uri.fromFile(photoFile);
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(takePictureIntent, requearCode);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fileUri="";
        String fileName="";
        String fileType="";
        selectedFilePreviewData selectedPreviewData = null;
        arrayListData = new ArrayList<>();
        switch (requestCode) {
            case 3:
                boolean isFileSizeExceed = false;


                if (resultCode == -1) {
                    // Checking whether data is null or not
                    if (data != null) {
                        // Checking for selection multiple files or single.
                        if (data.getClipData() != null) {
                            // Getting the length of data and logging up the logs using index
                            for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                                // Getting the URIs of the selected files and logging them into logcat at debug level
                                Uri uri = data.getClipData().getItemAt(index).getUri();
                                if (mContext.getContentResolver().getType(uri).equalsIgnoreCase("image/jpeg") ||
                                        mContext.getContentResolver().getType(uri).equalsIgnoreCase("image/jpg") ||
                                        mContext.getContentResolver().getType(uri).equalsIgnoreCase("image/png")) {
                                    File compressedImageFile = null;
//                                    try {
                                        try {
                                            fileTemp = FileUtil.from(mContext, uri);
                                            Log.d("file", "File...:::: uti - " + fileTemp.getPath() + " file -" + fileTemp + " : " + fileTemp.exists());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        if (FileUtil.getFileTypeFromUri(mContext,uri)!=null){
                                            fileType = FileUtil.getFileTypeFromUri(mContext,uri);
                                        }else{
                                            String result = fileTemp.getAbsolutePath().substring(fileTemp.getAbsolutePath().lastIndexOf("."));
                                            String finalresult= result.replace(".", "");
                                            fileType = "image/"+finalresult;
                                        }
                                        String tempChatID = UUID.randomUUID().toString();

//                                        compressedImageFile =  isFileAttach ? fileTemp : new Compressor(getContext()).compressToFile(fileTemp);
                                        try {
                                            ImageCompressor imageCompressor = new ImageCompressor();
                                            compressedImageFile = isFileAttach ? fileTemp : imageCompressor.compressImageFile(getContext(), fileTemp);
                                            // Use the compressedImageFile as needed
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                         filesNames.add(new FileDataClass(compressedImageFile.getName(),common.getFolderSizeLabel(compressedImageFile),FileUtil.getFileTypeFromUri(mContext,uri),uri.toString(),tempChatID));
                                        String base64Str = "";
                                        try {
                                            base64Str  = common.convertFileToBase64(compressedImageFile.getAbsolutePath());
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                        fileUploadArrayList.add(new UploadFilesDataModel(tempChatID,base64Str,compressedImageFile.getName(),fileType,conversationByUID,""));

                                        fileUri = uri.toString();
                                        fileName = compressedImageFile.getName();

                                        selectedPreviewData = new selectedFilePreviewData(fileType,fileUri,fileName,common.getFolderSizeLabel(fileTemp),tempChatID,"");

//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                                } else {
                                    if (Utils.isCheckFileSize(uri, mContext)) {
                                        File compressedImageFile = null;
                                        try {
                                            fileTemp = FileUtil.from(mContext, uri);
                                            Log.d("file", "File...:::: uti - " + fileTemp.getPath() + " file -" + fileTemp + " : " + fileTemp.exists());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if (FileUtil.getFileTypeFromUri(mContext,uri)!=null){
                                            fileType = FileUtil.getFileTypeFromUri(mContext,uri);
                                        }else{
                                            String result = fileTemp.getAbsolutePath().substring(fileTemp.getAbsolutePath().lastIndexOf("."));
                                            String finalresult= result.replace(".", "");
                                            fileType = "application/"+finalresult;
                                        }
                                        String tempChatID = UUID.randomUUID().toString();
                                        String base64Str = "";
                                        try {
                                            base64Str  = common.convertFileToBase64(fileTemp.getAbsolutePath());
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                        fileUploadArrayList.add(new UploadFilesDataModel(tempChatID,base64Str,fileTemp.getName(),fileType,conversationByUID,""));
                                        filesNames.add(new FileDataClass(fileTemp.getName(),common.getFolderSizeLabel(fileTemp),fileType,uri.toString(),tempChatID));
                                        fileUri = uri.toString();
                                        fileName = fileTemp.getName();

                                        selectedPreviewData = new selectedFilePreviewData(fileType,fileUri,fileName,common.getFolderSizeLabel(fileTemp),tempChatID,"");

                                    } else {
                                        Toast.makeText(mContext, "File size exceeded from 2.5 mb", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                arrayListData.add(selectedPreviewData);
                            }
                        } else {
                            // Getting the URI of the selected file and logging into logcat at debug level
                            Uri uri = data.getData();
                            if (mContext.getContentResolver().getType(uri).equalsIgnoreCase("image/jpeg") ||
                                    mContext.getContentResolver().getType(uri).equalsIgnoreCase("image/jpg") ||
                                    mContext.getContentResolver().getType(uri).equalsIgnoreCase("image/png")) {
                                //uris.add(uri);
                                File compressedImageFile = null;
                                ImageCompressor imageCompressor = new ImageCompressor();
//                                try {
                                    try {
                                        fileTemp = FileUtil.from(mContext, uri);
                                        Log.d("file", "File...:::: uti - " + fileTemp.getPath() + " file -" + fileTemp + " : " + fileTemp.exists());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if (FileUtil.getFileTypeFromUri(mContext,uri)!=null){
                                        fileType = FileUtil.getFileTypeFromUri(mContext,uri);
                                    }else{
                                        String result = fileTemp.getAbsolutePath().substring(fileTemp.getAbsolutePath().lastIndexOf("."));
                                        String finalresult= result.replace(".", "");
                                        fileType = "image/"+finalresult;
                                    }
//                                    compressedImageFile =  isFileAttach ? fileTemp : new Compressor(getContext()).compressToFile(fileTemp);
                                    try {
                                         compressedImageFile = isFileAttach ? fileTemp : imageCompressor.compressImageFile(getContext(), fileTemp);
                                        // Use the compressedImageFile as needed
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    String tempChatID = UUID.randomUUID().toString();
                                    String base64Str = "";
                                    try {
                                        base64Str  = common.convertFileToBase64(compressedImageFile.getAbsolutePath());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    fileUploadArrayList.add(new UploadFilesDataModel(tempChatID,base64Str,compressedImageFile.getName(),fileType,conversationByUID,""));
                                    filesNames.add(new FileDataClass(compressedImageFile.getName(),common.getFolderSizeLabel(compressedImageFile),FileUtil.getFileTypeFromUri(mContext,uri),uri.toString(),tempChatID));
                                    fileUri = uri.toString();
                                    fileName = compressedImageFile.getName();
                                    selectedPreviewData = new selectedFilePreviewData(fileType,fileUri,fileName,common.getFolderSizeLabel(fileTemp),tempChatID,"");
                                    arrayListData.add(selectedPreviewData);

//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                            } else {
                                if (Utils.isCheckFileSize(uri, mContext)) {
                                    try {
                                        fileTemp = FileUtil.from(mContext, uri);
                                        Log.d("file", "File...:::: uti - " + fileTemp.getPath() + " file -" + fileTemp + " : " + fileTemp.exists());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (FileUtil.getFileTypeFromUri(mContext,uri)!=null){
                                        fileType = FileUtil.getFileTypeFromUri(mContext,uri);
                                    }else{
                                        String result = fileTemp.getAbsolutePath().substring(fileTemp.getAbsolutePath().lastIndexOf("."));
                                        String finalresult= result.replace(".", "");
                                        fileType = "application/"+finalresult;
                                    }
                                    String tempChatID = UUID.randomUUID().toString();
                                    String base64Str = "";
                                    try {
                                        base64Str  = common.convertFileToBase64(fileTemp.getAbsolutePath());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    fileUploadArrayList.add(new UploadFilesDataModel(tempChatID,base64Str,fileTemp.getName(),fileType,conversationByUID,""));
                                    filesNames.add(new FileDataClass(fileTemp.getName(),common.getFolderSizeLabel(fileTemp),fileType,uri.toString(),tempChatID));
                                    fileUri = uri.toString();
                                    fileName = fileTemp.getName();

                                    selectedPreviewData = new selectedFilePreviewData(fileType,fileUri,fileName,common.getFolderSizeLabel(fileTemp),tempChatID,"");

                                    arrayListData.add(selectedPreviewData);
                                } else {
                                    Toast.makeText(mContext, "File size exceeded from 2.5 mb", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        if (!fileUploadArrayList.isEmpty()) {
                            Intent intent = new Intent(mContext, SelectFilePreviewActivity.class);
                            intent.putParcelableArrayListExtra("arrayListData", arrayListData);
                            startActivity(intent);
                        }else{
                            Toast.makeText(mContext, "No file found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case 2:
                // Opening Camera
                if (requestCode == CAPTURE_PICTURE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
                    if(!mCurrentPhotoPath.isEmpty()) {
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri;
                        if (Utils.isFileLessThan2MB(new File(mCurrentPhotoPath))){
                            contentUri = Uri.fromFile(new File(mCurrentPhotoPath));
                        }else{
                            contentUri = Uri.fromFile(common.compressImage(mCurrentPhotoPath,mContext));
                        }
                        mediaScanIntent.setData(contentUri);
                        if (mContext != null && isAdded()) {
                            mContext.sendBroadcast(mediaScanIntent);
                        }
                        File compressedImageFile = null;
                        try {
                            fileTemp = FileUtil.from(mContext, contentUri);
                            Log.d("file", "File...:::: uti - " + fileTemp.getPath() + " file -" + fileTemp + " : " + fileTemp.exists());

                            if (FileUtil.getFileTypeFromUri(mContext,contentUri)!=null){
                                fileType = FileUtil.getFileTypeFromUri(mContext,contentUri);
                            }else{
                                String result = fileTemp.getAbsolutePath().substring(fileTemp.getAbsolutePath().lastIndexOf("."));
                                String finalresult= result.replace(".", "");
                                fileType = "image/"+finalresult;
                            }

//                            compressedImageFile = new Compressor(getContext()).compressToFile(fileTemp);

                            ImageCompressor imageCompressor = new ImageCompressor();
                            try {
                                compressedImageFile = imageCompressor.compressImageFile(getContext(), fileTemp);
                                // Use the compressedImageFile as needed
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String tempChatID = UUID.randomUUID().toString();
                            String base64Str = "";
                            try {
                                base64Str  = common.convertFileToBase64(compressedImageFile.getAbsolutePath());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            fileUploadArrayList.add(new UploadFilesDataModel(tempChatID,base64Str,compressedImageFile.getName(),fileType,conversationByUID,""));
                            filesNames.add(new FileDataClass(compressedImageFile.getName(),common.getFolderSizeLabel(compressedImageFile),fileType,contentUri.toString(),tempChatID));
                            fileUri = contentUri.toString();
                            fileName = compressedImageFile.getName();

                            selectedPreviewData = new selectedFilePreviewData(fileType,fileUri,fileName,common.getFolderSizeLabel(fileTemp),tempChatID,"");
                            arrayListData.add(selectedPreviewData);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!fileUploadArrayList.isEmpty()) {
                            Intent intent = new Intent(mContext, SelectFilePreviewActivity.class);
                            intent.putExtra("arrayListData", arrayListData);
                            startActivity(intent);
                        }else{
                            Toast.makeText(mContext, "No file found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                if(mContext!=null) {
                    // Create Dialoge.dialoge(mContext, "You haven't picked Image");
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NewChatRecieveResponse event) {
        if(event!=null) {
            if (event.eventType.equalsIgnoreCase("CallSendPrivateAfterNewChat")) {
//              stop resend scheduler when new chat response called
                if (dbchat!=null){
                    dbchat.conversationDetailDao().deleteNewChatByTempChatID(event.conversation.tempChatId);
                }

                tempChatId = event.conversation.tempChatId;
                conversationByUID = event.conversation.conversationUid;
                customerName = event.conversation.customerName;
                agentId = event.conversation.agentId;
                cusId = event.conversation.customerId;
                common.saveCustomerId(mContext,event.conversation.customerId);
                groupId = event.conversation.groupId;
                if (!uploadFilesData.isEmpty()){
                    sendMessage(event.conversation.caption,event.conversation,"file",event.conversation.content,uploadFilesData,arrayListData);
                }else{
                    sendMessage("",event.conversation,event.conversation.type,event.conversation.content,uploadFilesData,arrayListData);
                    fragmentConversationsBinding.edtMessage.setText("");
                }
            }
        }
    }

    public void sendWelcomeMessageTopic(){
        if (!topicMessage.isEmpty()){
            String tempChatId = UUID.randomUUID().toString();
            addItemToListWelcomeMessage(tempChatId,topicMessage,common.getDisplayName(mContext),true);
            sendNewChat("","welcomeMessage", topicMessage, uploadFilesData,arrayListData,tempChatId,true);
            topicMessage = "";
            topicID = 0;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReLoadConversationEvent event) {
        if(event!=null) {
            if (event.eventType.equalsIgnoreCase("ReloadConversationWhenConnect")) {
                //resendSchedulerStart();
                //api for all conversations
                getConversationByUID(isLocalChatLoaded,pageNumber,pageSize,common.getConversationUUId(mContext),cusId,false);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SyncChatRecieveResponse event) {
        if(event!=null) {
            if (event.eventType.equalsIgnoreCase("LoadSyncMessagesToLocalDB")) {
                if (!event.conversation.listConversationVM.isEmpty()){
                    for (int i = 0; i < event.conversation.listConversationVM.size(); i++) {
                        conversationArrayList.add(0,event.conversation.listConversationVM.get(i));
                        conversationsListAdapter.notifyItemInserted(0);
                    }
                    if(common!=null && mContext!=null){
                        common.saveLastConversationId(mContext,-1);
                    }
                    saveConversationsDataIntoDB(true,pageNumber,event.conversation.listConversationVM);
                    scrollToBottom();
                }
            }
        }
    }

    public ArrayList<ConversationByUID> getConversationLocally() {
        ArrayList<ConversationByUID> conversationByUIDArrayList = new ArrayList<>();
        conversationByUIDArrayListIdZero = new ArrayList<>();
        ArrayList<ConversationDetail> listOfStrings = new ArrayList<>(dbchat.conversationDetailDao().getAllConversation(conversationByUID).size());
        listOfStrings.addAll(dbchat.conversationDetailDao().getAllConversation(conversationByUID));
        if (!listOfStrings.isEmpty()){
            for (int i=0;i<listOfStrings.size();i++){
                ConversationByUID conversationByUID1 = new ConversationByUID();
                conversationByUID1.id = listOfStrings.get(i).id;
                conversationByUID1.isSeen = listOfStrings.get(i).isSeen;
                conversationByUID1.isFromWidget = listOfStrings.get(i).isFromWidget;
                conversationByUID1.type = listOfStrings.get(i).type;
                conversationByUID1.conversationUid = listOfStrings.get(i).conversationUid;
                conversationByUID1.isUpdateStatus = listOfStrings.get(i).isUpdateStatus;
                conversationByUID1.isWelcome = listOfStrings.get(i).isWelcome;
                conversationByUID1.isShowLocalFiles = listOfStrings.get(i).isShowLocalFiles;
                conversationByUID1.isNotNewChat = listOfStrings.get(i).isNotNewChat;
                conversationByUID1.isDownloading = listOfStrings.get(i).isDownloading;
                conversationByUID1.tempChatId = listOfStrings.get(i).tempChatId;
                conversationByUID1.agentId = listOfStrings.get(i).agentId;
                conversationByUID1.content = listOfStrings.get(i).content;
                conversationByUID1.conversationType = listOfStrings.get(i).conversationType;
                conversationByUID1.customerName = listOfStrings.get(i).customerName;
                conversationByUID1.customerEmail = listOfStrings.get(i).customerEmail;
                conversationByUID1.customerId = listOfStrings.get(i).customerId;
                conversationByUID1.forwardedTo = listOfStrings.get(i).forwardedTo;
                conversationByUID1.pageId = listOfStrings.get(i).pageId;
                conversationByUID1.pageName = listOfStrings.get(i).pageName;
                conversationByUID1.groupId = listOfStrings.get(i).groupId;
                conversationByUID1.fileLocalUri = listOfStrings.get(i).fileLocalUri;
                conversationByUID1.groupName = listOfStrings.get(i).groupName;
                conversationByUID1.isPrivate = listOfStrings.get(i).isPrivate;
                conversationByUID1.receiver = listOfStrings.get(i).receiver;
                conversationByUID1.sender = listOfStrings.get(i).sender;
                conversationByUID1.source = listOfStrings.get(i).source;
                conversationByUID1.toUserId = listOfStrings.get(i).toUserId;
                conversationByUID1.tiggerevent = listOfStrings.get(i).tiggerevent;
                conversationByUID1.timestamp = listOfStrings.get(i).timestamp;
                conversationByUID1.caption = listOfStrings.get(i).caption;
                conversationByUID1.rating = listOfStrings.get(i).rating;
                conversationByUID1.feedback = listOfStrings.get(i).feedback;
                conversationByUID1.isFailed = listOfStrings.get(i).isFailed;

                if (listOfStrings.get(i).type.equalsIgnoreCase("file")){
                    ArrayList<FilesData> filesdataArrayList = new ArrayList<>();
                    ArrayList<ConversationDetailFile> conversationDetailFiles;
                    if (listOfStrings.get(i).isNotNewChat){
                        conversationDetailFiles = new ArrayList<>(dbchat.conversationDetailDao().getConversationFilesData(conversationByUID,listOfStrings.get(i).tempChatId).size());
                        conversationDetailFiles.addAll(dbchat.conversationDetailDao().getConversationFilesData(conversationByUID,listOfStrings.get(i).tempChatId));
                    }else{
                        if (listOfStrings.get(i).id!=0){
                            conversationDetailFiles = new ArrayList<>(dbchat.conversationDetailDao().getConversationFilesData(conversationByUID,listOfStrings.get(i).id).size());
                            conversationDetailFiles.addAll(dbchat.conversationDetailDao().getConversationFilesData(conversationByUID,listOfStrings.get(i).id));
                        }else{
                            conversationDetailFiles = new ArrayList<>(dbchat.conversationDetailDao().getConversationFilesData(conversationByUID,listOfStrings.get(i).tempChatId).size());
                            conversationDetailFiles.addAll(dbchat.conversationDetailDao().getConversationFilesData(conversationByUID,listOfStrings.get(i).tempChatId));
                        }
                    }

                    if (!conversationDetailFiles.isEmpty()){
                        FilesData filesData = null;
                        for (int j=0;j<conversationDetailFiles.size();j++){
                            filesData = new FilesData();
                            filesData.url  = conversationDetailFiles.get(j).url;
                            filesData.documentName =  conversationDetailFiles.get(j).documentName;
                            filesData.icon = conversationDetailFiles.get(j).icon;
                            filesData.type = conversationDetailFiles.get(j).type;
                            filesdataArrayList.add(filesData);
                        }
                        conversationByUID1.files = filesdataArrayList;
                    }
                }
                conversationByUIDArrayList.add(conversationByUID1);
            }
        }
        return  conversationByUIDArrayList;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TopicSelectEvent event) {
        if(event!=null) {
            if (event.eventType.equalsIgnoreCase("TopicSelect")) {
                hideAndShowTopicMenu(false);
                isTopicSelect = true;
                topicMessage = event.getTopicsByChannelIDModel.message;
                topicName = event.getTopicsByChannelIDModel.name;
                topicID = event.getTopicsByChannelIDModel.topicId;
                if (!event.getTopicsByChannelIDModel.customText.isEmpty()){
                    fragmentConversationsBinding.tvCustomText.setText(event.getTopicsByChannelIDModel.customText);
                }else{
                    fragmentConversationsBinding.tvCustomText.setText(getString(R.string.lbl_reply_desc));
                }
                groupId = event.getTopicsByChannelIDModel.isGroupAssigned ? event.getTopicsByChannelIDModel.groupId : groupId;
                fragmentConversationsBinding.lbTopicChannel.setVisibility(View.GONE);
                fragmentConversationsBinding.layoutTyping.setVisibility(View.VISIBLE);

                if (common.getIsResolved(mContext)){
                    isTopicSecondTimeSelect = false;
                    sendNewChat("","welcomeMessage", businessHoursMessage, uploadFilesData,arrayListData, temppChatIdWelcomeMessage,true);
                }else{
                    isTopicSecondTimeSelect = true;
                    sendNewChat("","text", topicName, uploadFilesData,arrayListData, UUID.randomUUID().toString(),true);
                }
            }
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConversationEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("AddToList")){
                ArrayList<Conversation> conversationArrayList = new ArrayList<>();
                if (common.getConversationList(mContext)!=null && !common.getConversationList(mContext).isEmpty()){
                    for (int i=0;i<common.getConversationList(mContext).size();i++){
                        if (!common.getConversationList(mContext).get(i).conversationUid.equalsIgnoreCase(conversationByUID)){
                            Conversation conversation = common.getConversationList(mContext).get(i);
                            conversation.isNewMessageReceive = true;
                            conversationArrayList.add(conversation);
                        }else{
                            conversationArrayList.add(common.getConversationList(mContext).get(i));
                        }
                    }
                    if(!conversationArrayList.isEmpty() && conversationsListingDetailAdapter!=null){
                        conversationsListingDetailAdapter = new ConversationsListingDetailAdapter(mContext,conversationArrayList);
                        fragmentConversationsBinding.rvConversationList.setAdapter(conversationsListingDetailAdapter);
                    }

                }
            }
        }

    }

    public void getIsValidBusinessHours() {
        new ApiClient(mContext).getWebService().getValidBusinessHours(common.getUtcTime(),common.getCurrentDays()).enqueue(new Callback<WebResponseBusinessHour>() {
            @Override
            public void onResponse(Call<WebResponseBusinessHour> call, Response<WebResponseBusinessHour> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        businessHoursMessage = response.body().getBusinessHourModel().getMessage();
                        isOnline = response.body().getBusinessHourModel().isOnline;
                        getTopicsByEmail();
                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponseBusinessHour> call, Throwable t) {

                Log.d("Error",""+t.getMessage());
            }
        });
    }
    public boolean checkLastMessageTopicSelect(String lastMessage){
        if (!conversationArrayList.isEmpty()) {
            if (conversationArrayList.size()>0){
                try {
                    if (conversationArrayList.get(0).content.equalsIgnoreCase(lastMessage)){
                        return true;
                    }
                }
                catch ( IndexOutOfBoundsException e ) {
                    return false;
                }
            }
        }
        return false;
    }
    public boolean checkLastMessageIsBusinessHour(String lastMessage){
        if (!dbchat.conversationDetailDao().getLastConversationItem(conversationByUID).isEmpty()) {
            if (dbchat.conversationDetailDao().getLastConversationItem(conversationByUID).size()>0){
                if (dbchat.conversationDetailDao().getLastConversationItem(conversationByUID).get(0).content.equalsIgnoreCase(lastMessage)){
                    return  true;
                }
            }
        }
        return false;
    }

    public void getTopicsByEmail(){
        new ApiClient(mContext).getWebService().getTopicsByEmail(customerEmail).enqueue(new Callback<WebResponse<TopicModelResponse>>() {
            @Override
            public void onResponse(Call<WebResponse<TopicModelResponse>> call, Response<WebResponse<TopicModelResponse>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        //store conversation resolve status into the local prefrences
                        if (common!=null){
                            common.setIsResolved(mContext,response.body().getResult().status);
                        }
                        if (!response.body().getResult().topicList.isEmpty()){
                            //display welcome message based on conversation resolve status last message is not welcome message into the conversation
                            if (response.body().getResult().status && !checkLastMessageIsBusinessHour(businessHoursMessage)){
                                addWelcomeMessage();
                            }

                            topicArrayList = response.body().getResult().topicList;
                            if (response.body().getResult().status){
                                LoadTopics();
                            }else{
                                hideAndShowTopicMenu(false);
                            }

                        }else{
                            //also pass true to send this message to server
                            //display welcome message based on conversation resolve status last message is not welcome message into the conversation
                            if (response.body().getResult().status && !checkLastMessageIsBusinessHour(businessHoursMessage)){
                                addWelcomeMessage();
                            }
                        }
                    }else{
                        //display welcome message based on conversation resolve status last message is not welcome message into the conversation
                        if (common.getIsResolved(mContext) && !checkLastMessageIsBusinessHour(businessHoursMessage)){
                            addWelcomeMessage();
                        }
                        if(!topicArrayList.isEmpty()){
                            topicArrayList.clear();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<WebResponse<TopicModelResponse>> call, Throwable t) {

                Log.d("Error",""+t.getMessage());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendNewChatResponseEvent event) {
        if(event!=null) {
            if (event.eventType.equalsIgnoreCase("BulkMessageResponseListener")) {
                resendSchedulerStop();
                if (!event.sendMessageModel.isEmpty()) {
                    for (int i = 0; i < event.sendMessageModel.size(); i++) {
                        if (conversationByUID.equalsIgnoreCase(event.sendMessageModel.get(i).conversationUid)) {
                            if (conversationsListAdapter != null && !conversationArrayList.isEmpty() && fragmentConversationsBinding != null) {
                                if (!event.sendMessageModel.get(i).tempChatId.equalsIgnoreCase("")) {
                                    if (isContainConversationByUIdTemp(conversationArrayList, event.sendMessageModel.get(i).tempChatId)) {
                                        ConversationByUID conversationByUID1 = common.getConversationFromReceiveMsg(event.sendMessageModel.get(i));
                                        conversationByUID1.isUpdateStatus = true;
                                        saveAndUpdateTempItemToDB(false, conversationByUID1);
                                        conversationArrayList.set(addedConversationPos, conversationByUID1);
                                        conversationsListAdapter.notifyItemChanged(addedConversationPos);
                                        if (event.sendMessageModel.get(i).topicId!=0 &&
                                                !event.sendMessageModel.get(i).topicMessage.equalsIgnoreCase("")){
                                            handlerBulk = new Handler(Looper.getMainLooper());
                                            //do your work here
                                            handlerBulk.postDelayed(this::sendWelcomeMessageTopic, 1000);

                                            //send new chat for welcomeMessage
                                        }
                                    }
                                }
                            }
                        }
                    }
                    dbchat.conversationDetailDao().deleteAllNewChat();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendTypingIndicatorResponse event) {
        if(event!=null) {
            if (event.eventType.equalsIgnoreCase("TypingIndicatorListenerResponse")) {
                if (!event.typingIndicatorListenerModel.conversationUId.equalsIgnoreCase(conversationByUID)){
                    showTypingAnimation();
                }
            }
        }
    }

    public void showTypingAnimation(){

        // fade out view nicely
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,0.0f);
        alphaAnim.setDuration(3000);
        alphaAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation) {

                fragmentConversationsBinding.tvCustomText.setText("typing...");
                fragmentConversationsBinding.tvCustomText.animate().alpha(1.0f).setDuration(3000).start();
            }

            public void onAnimationEnd(Animation animation)
            {
                // make invisible when animation completes, you could also remove the view from the layout
                fragmentConversationsBinding.tvCustomText.setText(exitingCustomText);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fragmentConversationsBinding.tvCustomText.startAnimation(alphaAnim);
    }

    public void saveConversationsDataIntoDB(boolean isOnlySaved,int pagenum,ArrayList<ConversationByUID> conversationArrayList) {

        ConversationDetail conversationData = null;
        ConversationDetailFile conversationFilesData = null;
        for(int i=0; i<conversationArrayList.size(); i++) {
            conversationData = new ConversationDetail();
            conversationData.setId(conversationArrayList.get(i).id);
            conversationData.setCustomerId(conversationArrayList.get(i).customerId);
            conversationData.setConversationUid(conversationArrayList.get(i).conversationUid);
            conversationData.setCustomerEmail(conversationArrayList.get(i).customerEmail);
            conversationData.setTempChatId(conversationArrayList.get(i).tempChatId);
            conversationData.setToUserId(conversationArrayList.get(i).toUserId);
            conversationData.setGroupId(conversationArrayList.get(i).groupId);
            conversationData.setAgentId(conversationArrayList.get(i).agentId);
            conversationData.setContent(conversationArrayList.get(i).content);
            conversationData.setTimestamp(conversationArrayList.get(i).timestamp);
            conversationData.setSender(conversationArrayList.get(i).sender);
            conversationData.setReceiver(conversationArrayList.get(i).receiver);
            conversationData.setType(conversationArrayList.get(i).type);
            conversationData.setSource(conversationArrayList.get(i).source);
            conversationData.setGroupName(conversationArrayList.get(i).groupName);
            conversationData.setForwardedTo(conversationArrayList.get(i).forwardedTo);
            conversationData.setCustomerName(conversationArrayList.get(i).customerName);
            conversationData.setPrivate(conversationArrayList.get(i).isPrivate);
            conversationData.setFromWidget(conversationArrayList.get(i).isFromWidget);
            conversationData.setTiggerevent(conversationArrayList.get(i).tiggerevent);
            conversationData.setConversationType(conversationArrayList.get(i).conversationType);
            conversationData.setPageId(conversationArrayList.get(i).pageId);
            conversationData.setPageName(conversationArrayList.get(i).pageName);
            conversationData.setFileLocalUri(conversationArrayList.get(i).fileLocalUri);
            conversationData.setDownloading(conversationArrayList.get(i).isDownloading);
            conversationData.setUpdateStatus(conversationArrayList.get(i).isUpdateStatus);
            conversationData.setShowLocalFiles(false);
            conversationData.setCaption(conversationArrayList.get(i).caption);
            conversationData.setSeen(conversationArrayList.get(i).isSeen);
            conversationData.setCreatedOn(common.covertTimeStampToDate(conversationArrayList.get(i).timestamp));
            conversationData.setResolved(conversationArrayList.get(i).isResolved);
            conversationData.setIsRating(conversationArrayList.get(i).rating);
            conversationData.setIsRatingFeedback(conversationArrayList.get(i).feedback);

            if (!dbchat.conversationDetailDao().exists(conversationArrayList.get(i).id)) {
                long rowId = dbchat.conversationDetailDao().insertConversation(conversationData);
            } else{
                if (conversationArrayList.get(i).id!=0){
                    dbchat.conversationDetailDao().updateConversationByID(conversationArrayList.get(i).isSeen,
                            conversationArrayList.get(i).id,
                            conversationArrayList.get(i).customerId,
                            conversationArrayList.get(i).customerEmail,
                            conversationArrayList.get(i).tempChatId,
                            conversationArrayList.get(i).toUserId,
                            conversationArrayList.get(i).fromUserId,
                            conversationArrayList.get(i).groupId,
                            conversationArrayList.get(i).agentId,
                            conversationArrayList.get(i).content,
                            conversationArrayList.get(i).timestamp,
                            conversationArrayList.get(i).sender,
                            conversationArrayList.get(i).caption,
                            conversationArrayList.get(i).receiver,
                            conversationArrayList.get(i).type,
                            conversationArrayList.get(i).source,
                            conversationArrayList.get(i).groupName,
                            conversationArrayList.get(i).forwardedTo,
                            conversationArrayList.get(i).customerName,
                            conversationArrayList.get(i).conversationUid,
                            conversationArrayList.get(i).isPrivate,
                            conversationArrayList.get(i).isFromWidget,
                            conversationArrayList.get(i).tiggerevent,
                            conversationArrayList.get(i).conversationType,
                            conversationArrayList.get(i).pageId,
                            conversationArrayList.get(i).pageName,
                            conversationArrayList.get(i).fileLocalUri,
                            conversationArrayList.get(i).isDownloading,
                            conversationArrayList.get(i).isUpdateStatus,
                            conversationArrayList.get(i).isShowLocalFiles,
                            conversationArrayList.get(i).isWelcome,
                            conversationArrayList.get(i).isNotNewChat,
                            conversationArrayList.get(i).isFailed,
                            common.covertTimeStampToDate(conversationArrayList.get(i).timestamp),
                            conversationArrayList.get(i).isResolved,
                            conversationArrayList.get(i).rating,
                            conversationArrayList.get(i).feedback,
                            conversationArrayList.get(i).voiceDuration);
                }else{
                    dbchat.conversationDetailDao().updateConversationByTempChatID(conversationArrayList.get(i).isSeen,
                            conversationArrayList.get(i).id,
                            conversationArrayList.get(i).customerId,
                            conversationArrayList.get(i).customerEmail,
                            conversationArrayList.get(i).tempChatId,
                            conversationArrayList.get(i).toUserId,
                            conversationArrayList.get(i).fromUserId,
                            conversationArrayList.get(i).groupId,
                            conversationArrayList.get(i).agentId,
                            conversationArrayList.get(i).content,
                            conversationArrayList.get(i).timestamp,
                            conversationArrayList.get(i).sender,
                            conversationArrayList.get(i).caption,
                            conversationArrayList.get(i).receiver,
                            conversationArrayList.get(i).type,
                            conversationArrayList.get(i).source,
                            conversationArrayList.get(i).groupName,
                            conversationArrayList.get(i).forwardedTo,
                            conversationArrayList.get(i).customerName,
                            conversationArrayList.get(i).conversationUid,
                            conversationArrayList.get(i).isPrivate,
                            conversationArrayList.get(i).isFromWidget,
                            conversationArrayList.get(i).tiggerevent,
                            conversationArrayList.get(i).conversationType,
                            conversationArrayList.get(i).pageId,
                            conversationArrayList.get(i).pageName,
                            conversationArrayList.get(i).fileLocalUri,
                            conversationArrayList.get(i).isDownloading,
                            conversationArrayList.get(i).isUpdateStatus,
                            conversationArrayList.get(i).isShowLocalFiles,
                            conversationArrayList.get(i).isWelcome,
                            conversationArrayList.get(i).isNotNewChat,
                            conversationArrayList.get(i).isFailed,
                            common.covertTimeStampToDate(conversationArrayList.get(i).timestamp),
                            conversationArrayList.get(i).isResolved,
                            conversationArrayList.get(i).rating,
                            conversationArrayList.get(i).feedback,
                            conversationArrayList.get(i).voiceDuration);
                }
            }

            conversationFilesData = new ConversationDetailFile();
            if (conversationArrayList.get(i).type.equalsIgnoreCase("file")) {
                for (int j = 0; j < conversationArrayList.get(i).files.size(); j++) {
                    conversationFilesData.setmessagedId(conversationArrayList.get(i).id);
                    conversationFilesData.setConversationUid(conversationArrayList.get(i).conversationUid);
                    conversationFilesData.setCustomerId(conversationArrayList.get(i).customerId);
                    conversationFilesData.setUrl(conversationArrayList.get(i).files.get(j).url);
                    conversationFilesData.setType(conversationArrayList.get(i).files.get(j).type);
                    conversationFilesData.setIcon(conversationArrayList.get(i).files.get(j).icon);
                    conversationFilesData.setDocumentName(conversationArrayList.get(i).files.get(j).documentName);
                    conversationFilesData.setTempChatId(conversationArrayList.get(i).tempChatId);
                    if (!dbchat.conversationDetailDao().existsFile(conversationArrayList.get(i).id)){
                        dbchat.conversationDetailDao().insertConversationFileDate(conversationFilesData);
                    }else{
                        if (conversationArrayList.get(i).id!=0){
                            dbchat.conversationDetailDao().updateConversationFileByID(conversationArrayList.get(i).id,conversationFilesData.url,conversationFilesData.type,conversationFilesData.documentName,conversationFilesData.conversationUid,conversationFilesData.customerId,conversationFilesData.tempChatId);
                        }else{
                            dbchat.conversationDetailDao().updateConversationFileByTempChatID(conversationArrayList.get(i).id,conversationFilesData.url,conversationFilesData.type,conversationFilesData.documentName,conversationFilesData.conversationUid,conversationFilesData.customerId,conversationFilesData.tempChatId);
                        }

                    }
                }
            }
        }
        if (!isOnlySaved) {
            if (pagenum == 1) {
                fragmentConversationsBinding.loadMoreProgress.setVisibility(View.GONE);
                if (!dbchat.conversationDetailDao().getAllConversation(conversationByUID).isEmpty()) {
                    this.conversationArrayList = getConversationLocally();
                    conversationsListAdapter = new ConversationsByUIListAdapter(mContext, this.conversationArrayList);
                    fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
                    isLocalChatLoaded = true;
                } else {
                    isLocalChatLoaded = false;
                    this.conversationArrayList = conversationArrayList;
                    conversationsListAdapter = new ConversationsByUIListAdapter(mContext, this.conversationArrayList);
                    fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
                }
                scrollToBottom();
            } else {
                if (!this.conversationArrayList.isEmpty()) {
                    this.conversationArrayList.clear();
                }
                fragmentConversationsBinding.loadMoreProgress.setVisibility(View.GONE);
                if (!dbchat.conversationDetailDao().getAllConversation(conversationByUID).isEmpty()) {
                    this.conversationArrayList.addAll(getConversationLocally());
                    conversationsListAdapter.notifyDataSetChanged();
                    isLocalChatLoaded = true;
                } else {
                    this.conversationArrayList.addAll(conversationArrayList);
                    conversationsListAdapter.notifyDataSetChanged();
                    isLocalChatLoaded = false;
                }
            }
        }

        getIsValidBusinessHours();
    }

    public void sendBulkMessagesEvent()
    {
        if (dbchat!=null) {
            handlerBulk = new Handler(Looper.getMainLooper());
            handlerBulk.postDelayed(() -> {
                //do your work here
                MainActivityChat.isPause = false;
                if (!dbchat.conversationDetailDao().getAllUnSendChat(common.getConversationUUId(mContext), false).isEmpty()) {
                    EventBus.getDefault().post(new SendBulkChatEvent(getNewChatLocally(), "SendBulkChatMessages"));
                } else {
                    //resendSchedulerStop();
                }
            }, 200);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConversationStatusListenerEvent event) {
        if(event!=null){
            if(event.eventType.equalsIgnoreCase("ConversationStatusListener")){

                if(event.message.isResolved && conversationByUID.equalsIgnoreCase(event.message.conversationUid))
                {
                    showFeedBackDialog(event);
                }
            }
        }
    }

    public ArrayList<NewChatModel> getNewChatLocally(){
        ArrayList<NewChatModel> newChatModelArrayList = new ArrayList<>();
        ArrayList<NewChatEntity> listOfStrings = new ArrayList<>(dbchat.conversationDetailDao().getAllUnSendChat(conversationByUID,false).size());
        listOfStrings.addAll(dbchat.conversationDetailDao().getAllUnSendChat(conversationByUID,false));
        if (!listOfStrings.isEmpty()){
            for (int i=0;i<listOfStrings.size();i++) {
                NewChatModel sendMessageModel = new NewChatModel();
                sendMessageModel.agentId = listOfStrings.get(i).agentId;
                sendMessageModel.tempChatId = listOfStrings.get(i).tempChatId;
                sendMessageModel.conversationUId = listOfStrings.get(i).conversationUId;
                sendMessageModel.connectionId = listOfStrings.get(i).connectionId;
                sendMessageModel.customerId = listOfStrings.get(i).customerId ;
                sendMessageModel.groupId = listOfStrings.get(i).groupId ;
                sendMessageModel.conversationType = listOfStrings.get(i).conversationType;
                sendMessageModel.contactNo = listOfStrings.get(i).contactNo;
                sendMessageModel.name = listOfStrings.get(i).name ;
                sendMessageModel.cnic = listOfStrings.get(i).cnic;
                sendMessageModel.emailaddress = listOfStrings.get(i).emailaddress;
                sendMessageModel.message = listOfStrings.get(i).message;
                sendMessageModel.documentName = listOfStrings.get(i).documentName;
                sendMessageModel.documentorignalname = listOfStrings.get(i).documentOrignalname;
                sendMessageModel.documentType = listOfStrings.get(i).documentType;
                sendMessageModel.source = listOfStrings.get(i).source ;
                sendMessageModel.isFromWidget = listOfStrings.get(i).isFromWidget ;
                sendMessageModel.type = listOfStrings.get(i).type;
                sendMessageModel.channelid = listOfStrings.get(i).channelid;
                sendMessageModel.notifyMessage = listOfStrings.get(i).notifyMessage;
                sendMessageModel.caption = listOfStrings.get(i).caption;
                sendMessageModel.mobileToken = listOfStrings.get(i).mobileToken;
                sendMessageModel.timezone = listOfStrings.get(i).timezone;
                sendMessageModel.timestamp = listOfStrings.get(i).timeStamp;
                sendMessageModel.topicId = listOfStrings.get(i).topicId;
                sendMessageModel.topicMessage = listOfStrings.get(i).topicMessage;
                newChatModelArrayList.add(sendMessageModel);
            }
        }
        return newChatModelArrayList;
    }

    public void resendSchedulerStart(){
        scheduler = Executors.newScheduledThreadPool(10);
        scheduler.scheduleAtFixedRate(() -> {
            // Check for unsent messages and attempt to send them
            // Update the local database with the status of sent messages
            sendBulkMessagesEvent();
            if (timer != null) {
                timer.cancel();
            }
        }, 0, 14, TimeUnit.SECONDS); // Adjust the interval as needed
    }

    public void resendSchedulerStop(){
        if (handlerResend!=null){
            // When you need to cancel all your posted runnables just use:
            handlerResend.removeCallbacksAndMessages(null);
        }
    }

    private void showFeedBackDialog(ConversationStatusListenerEvent event) {

        Dialog bottomSheetDialog = new Dialog(getContext());
        View newUserView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        newUserView = inflater.inflate(R.layout.dialoge_rating_user, null);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newUserView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.round_border_rectangle));
        bottomSheetDialog.setContentView(newUserView);
        bottomSheetDialog.setCancelable(false);


        Button btnSubmit = bottomSheetDialog.findViewById(R.id.btnSubmit);
        Button btnSkipConversation = bottomSheetDialog.findViewById(R.id.btnSkip);
        RatingBar ratingBar = bottomSheetDialog.findViewById(R.id.simpleRatingBar);
        EditText editFeedBack = bottomSheetDialog.findViewById(R.id.editFeedBack);

        btnSubmit.setOnClickListener(v -> {
            if(common.isValidateConversationFeedBackDialog(getContext(),ratingBar)) {
                loadResolvedConversationBySkipOrSubmit(false,event,editFeedBack,ratingBar,bottomSheetDialog);
            }else{
                Toast.makeText(getActivity(), "Please rate before submit", Toast.LENGTH_SHORT).show();
            }
        });
        btnSkipConversation.setOnClickListener(v -> loadResolvedConversationBySkipOrSubmit(true,event,editFeedBack,ratingBar,bottomSheetDialog));
        bottomSheetDialog.show();
    }

    public void loadResolvedConversationBySkipOrSubmit(boolean isSkipClicked, ConversationStatusListenerEvent event, EditText editFeedback, RatingBar ratingBar, Dialog bottomSheetDialog) {
        // do your actual work here
        ResolveFeedBackRequest feedBackRequest = new ResolveFeedBackRequest();
        if(!isSkipClicked){
            feedBackRequest.conversationUid = conversationByUID;
            feedBackRequest.isSatisfied = isSatisfied;
            feedBackRequest.callerAppType = 3;
            feedBackRequest.feedback = !editFeedback.getText().toString().isEmpty() ? editFeedback.getText().toString() : "";
            feedBackRequest.rating = (int) ratingBar.getRating();
            EventBus.getDefault().post(new CustomerFeedBackEvent("ConversationFeedback",feedBackRequest));
        }
        if(common!=null && mContext!=null){
            common.setIsResolved(mContext,true);
        }
        if(conversationsListAdapter!=null && !conversationArrayList.isEmpty() && fragmentConversationsBinding!=null) {
            conversationArrayList.add(0, getConversationFromResolveListener(event.message,feedBackRequest,true));
            conversationsListAdapter.notifyItemInserted(0);
            saveAndUpdateTempItemToDB(true,getConversationFromResolveListener(event.message,feedBackRequest,true));
            scrollToBottom();
        }else{
            conversationArrayList.add(getConversationFromResolveListener(event.message,feedBackRequest,true));
            saveAndUpdateTempItemToDB(true,getConversationFromResolveListener(event.message,feedBackRequest,true));
            conversationsListAdapter = new ConversationsByUIListAdapter(mContext, conversationArrayList);
            fragmentConversationsBinding.rvConversations.setAdapter(conversationsListAdapter);
        }

        if (!topicArrayList.isEmpty()){
            if (!businessHoursMessage.isEmpty()){
                temppChatIdWelcomeMessage = UUID.randomUUID().toString();
                addItemToListWelcomeMessage(temppChatIdWelcomeMessage,businessHoursMessage,common.getDisplayName(mContext),true);
            }
            hideAndShowTopicMenu(true);
            LoadTopics();
        }

        if(bottomSheetDialog.isShowing()){
            bottomSheetDialog.dismiss();
        }
    }

}