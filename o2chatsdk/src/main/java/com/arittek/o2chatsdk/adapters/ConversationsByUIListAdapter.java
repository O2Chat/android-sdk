package com.arittek.o2chatsdk.adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arittek.o2chatsdk.commons.O2ChatConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.arittek.o2chatsdk.Events.appEvents.MessageEventFileDownload;
import com.arittek.o2chatsdk.Events.chatEvents.FileResendEvent;
import com.arittek.o2chatsdk.commons.Common;
import com.arittek.o2chatsdk.commons.Utils;
import com.arittek.o2chatsdk.model.chat.ConversationByUID;
import com.arittek.o2chatsdk.R;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

public class ConversationsByUIListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mcontex;
    private final Common common;

    private final O2ChatConfig o2ChatConfig;

    private int viewTypeSenderMessage = 0;
    private int viewTypeReceiverMessage = 1;
    private int viewTypeSystemMessage = 2;
    private int viewTypeImageSenderMessage = 3;
    private int viewTypeImageReceiverMessage = 4;

    private final ArrayList<ConversationByUID> item_list;
    private int playingPosition = RecyclerView.NO_POSITION;

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Handler handler = new Handler();
    private Runnable updateSeekBarRunnable;
    private Runnable updateRunnable;

    public ConversationsByUIListAdapter(Context contex, ArrayList<ConversationByUID> item_list) {
        this.mcontex = contex;
        this.item_list = item_list;
        this.common = new Common();
        this.o2ChatConfig = new O2ChatConfig();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if(viewType==viewTypeSenderMessage) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_list_loginuser,parent,false);
            viewHolder = new com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolder(view);
        }
        else if (viewType==viewTypeReceiverMessage) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_list_nonusermessage,parent,false);
            viewHolder= new com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolder(view);
        }
        else if (viewType==viewTypeSystemMessage) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_list_system,parent,false);
            viewHolder= new com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolder(view);
        }
        else if (viewType==viewTypeImageSenderMessage) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_conversation_list_loginuser,parent,false);
            viewHolder= new com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile(view);
        }
        else if (viewType==viewTypeImageReceiverMessage) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_conversation_list_nonusermessage,parent,false);
            viewHolder= new com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile(view);
        }

        return viewHolder;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ConversationByUID conversation = item_list.get(position);
        if (holder.getItemViewType()==viewTypeImageSenderMessage || holder.getItemViewType() == viewTypeImageReceiverMessage) {
            if (conversation.type.equalsIgnoreCase("file"))
            {
                com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile viewHolderForChatImagesAndFile = (com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile) holder;
                if (conversation.files != null && conversation.files.size()>0) {

                    if(!conversation.files.get(0).url.isEmpty() && conversation.files.get(0).type!=null){
                        if (conversation.files.get(0).type.equalsIgnoreCase("zip") || conversation.files.get(0).type.equalsIgnoreCase("application/zip")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.zip);
                        }
                        else if (conversation.files.get(0).type.equalsIgnoreCase("rar") || conversation.files.get(0).type.equalsIgnoreCase("application/rar")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.rar);
                        }
                        else if (conversation.files.get(0).type.equalsIgnoreCase("7z") || conversation.files.get(0).type.equalsIgnoreCase("application/7z")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.sevenz);
                        }
                        else if (conversation.files.get(0).type.equalsIgnoreCase("txt") || conversation.files.get(0).type.equalsIgnoreCase("application/txt")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.txt);
                        }
                        else if (conversation.files.get(0).type.equalsIgnoreCase("pdf") || conversation.files.get(0).type.equalsIgnoreCase("application/pdf")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.pdf);
                        }
                        else if ( conversation.files.get(0).type.equalsIgnoreCase("msword")  || conversation.files.get(0).type.equalsIgnoreCase("application/msword") || conversation.files.get(0).type.equalsIgnoreCase("docx") || conversation.files.get(0).type.equalsIgnoreCase("application/docx") || conversation.files.get(0).type.equalsIgnoreCase("doc") || conversation.files.get(0).type.equalsIgnoreCase("application/doc") || conversation.files.get(0).type.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.doc);
                        }
                        else if (conversation.files.get(0).type.equalsIgnoreCase("xls") || conversation.files.get(0).type.equalsIgnoreCase("application/xls")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.xls);
                        }
                        else if (conversation.files.get(0).type.equalsIgnoreCase("xlsx") || conversation.files.get(0).type.equalsIgnoreCase("application/xlsx")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.xlsx);
                        }
                        else if (conversation.files.get(0).type.equalsIgnoreCase("csv") || conversation.files.get(0).type.equalsIgnoreCase("application/csv")){
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivMultimedia.setImageResource(R.drawable.xls);
                        }else if (conversation.files.get(0).type.equalsIgnoreCase("mp3") || conversation.files.get(0).type.equalsIgnoreCase("application/mp3")) {
                            viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnAudio.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.lnOtherFiles.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.GONE);
                            if (isPlaying && playingPosition == position) {
                                viewHolderForChatImagesAndFile.ivPlay.setImageResource(R.drawable.ic_play);
                            } else {
                                viewHolderForChatImagesAndFile.ivPlay.setImageResource(R.drawable.ic_pause);
                                ((com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile) holder).audioSeekBar.setProgress(0);
                            }
                            if (conversation.voiceDuration!=null && !conversation.voiceDuration.isEmpty()){
                                viewHolderForChatImagesAndFile.txtAudioTime.setText(!conversation.voiceDuration.isEmpty() ?  conversation.voiceDuration : "0:00");
                                viewHolderForChatImagesAndFile.txtAudioPlayTime.setText("0:00");
                            }
                        } else{
                            if(!conversation.files.get(0).url.isEmpty()){
                                viewHolderForChatImagesAndFile.layoutFileShow.setVisibility(View.GONE);
                                viewHolderForChatImagesAndFile.layoutImage.setVisibility(View.VISIBLE);
                                if (conversation.isShowLocalFiles){
                                    Uri myUri = Uri.parse(conversation.files.get(0).url);
                                    Glide.with(mcontex).load(myUri).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(viewHolderForChatImagesAndFile.image);
                                }else{
                                    Glide.with(mcontex).asBitmap().load(conversation.files.get(0).url).dontAnimate()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .into(viewHolderForChatImagesAndFile.image);

                                }
                            }
                        }

                        Glide.with(mcontex).load(R.drawable.cloudnew).dontAnimate().into(viewHolderForChatImagesAndFile.icSource);
                        viewHolderForChatImagesAndFile.textMessage.setText(conversation.content);
                    }
                    if (conversation.isDownloading){
                        viewHolderForChatImagesAndFile.icSource.setVisibility(View.VISIBLE);
                    }else{
                        viewHolderForChatImagesAndFile.icSource.setVisibility(View.GONE);
                    }

                    if (holder.getItemViewType()==viewTypeImageSenderMessage){
                        if (conversation.isNotNewChat){
                            if (conversation.isUpdateStatus){
                                if (conversation.isSeen){
                                    viewHolderForChatImagesAndFile.imageStatus.setVisibility(View.VISIBLE);
                                    viewHolderForChatImagesAndFile.imageStatus.setImageResource(R.drawable.read_receipt);
                                }else {
                                    viewHolderForChatImagesAndFile.imageStatus.setVisibility(View.VISIBLE);
                                    viewHolderForChatImagesAndFile.imageStatus.setImageResource(R.drawable.tick);
                                }
                            }else{
                                if (conversation.isSeen){
                                    viewHolderForChatImagesAndFile.imageStatus.setVisibility(View.VISIBLE);
                                    viewHolderForChatImagesAndFile.imageStatus.setImageResource(R.drawable.read_receipt);
                                }else {
                                    viewHolderForChatImagesAndFile.imageStatus.setVisibility(View.VISIBLE);
                                    viewHolderForChatImagesAndFile.imageStatus.setImageResource(R.drawable.tick);
                                }
                            }
                        }else{
                            if (conversation.isSeen){
                                viewHolderForChatImagesAndFile.imageStatus.setVisibility(View.VISIBLE);
                                viewHolderForChatImagesAndFile.imageStatus.setImageResource(R.drawable.read_receipt);
                            }else{
                                if (conversation.id !=0){
                                    viewHolderForChatImagesAndFile.imageStatus.setVisibility(View.VISIBLE);
                                    viewHolderForChatImagesAndFile.imageStatus.setImageResource(R.drawable.tick);
                                }else{
                                    viewHolderForChatImagesAndFile.imageStatus.setVisibility(View.VISIBLE);
                                    viewHolderForChatImagesAndFile.imageStatus.setImageResource(R.drawable.send_messagetime);
                                }
                            }

                        }
                    }
                }

                if (!conversation.timestamp.isEmpty()) {
                    viewHolderForChatImagesAndFile.txtTime.setText(common.covertTimeToLong(conversation.timestamp));
                    //viewHolderForChatImagesAndFile.txtTime.setReferenceTime(common.covertTimeToLong(conversation.timestamp));
//                      if (!viewHolderForChatImagesAndFile.txtTime.getText().toString().isEmpty()){
//                          if(viewHolderForChatImagesAndFile.txtTime.getText().toString().equalsIgnoreCase("In 0 min.")){
//                              viewHolderForChatImagesAndFile.txtTime.setText(R.string.lbl_jstnow);
//                          }
//                      }
                }

                if(conversation != null && !conversation.files.isEmpty())
                {
                    if (!conversation.files.get(0).type.equalsIgnoreCase("mp3") && !conversation.files.get(0).type.equalsIgnoreCase("application/mp3")){
                        viewHolderForChatImagesAndFile.itemView.setOnClickListener(v -> {
                            EventBus.getDefault().post(new MessageEventFileDownload("FileDownload", conversation.conversationUid, conversation.files.get(0).documentName, conversation.files, viewHolderForChatImagesAndFile.getAdapterPosition(), item_list.get(position)));
                        });
                    }
                }

                viewHolderForChatImagesAndFile.ivPlay.setOnClickListener(v -> {
                    if (isPlaying && playingPosition == position) {
                        ConversationByUID conversationByUID = item_list.get(position);
                        conversationByUID.isPlaying = false;
                        item_list.set(position,conversationByUID);
                        // Find and reset playback state for the previously playing item
                        stopPlayback();
                        viewHolderForChatImagesAndFile.ivPlay.setImageResource(R.drawable.ic_pause);
                        viewHolderForChatImagesAndFile.audioSeekBar.setProgress(0);
                        isPlaying = false;
                    } else {
                        isPlaying = true;
                        ConversationByUID conversationByUID = item_list.get(position);
                        conversationByUID.isPlaying = false;
                        item_list.set(position, conversationByUID);
                        try {
                            if (conversation.fileLocalUri.isEmpty()) {
                                if (conversation.isFromWidget) {
                                    startPlaybackFromWidget(viewHolderForChatImagesAndFile, viewHolderForChatImagesAndFile.audioSeekBar, conversation.files.get(0).url, conversation.conversationUid + "" + conversation.id, false);
                                } else {
                                    startPlayback(viewHolderForChatImagesAndFile, viewHolderForChatImagesAndFile.audioSeekBar, conversation.files.get(0).url);
                                }
                            } else {
                                if (conversation.isFromWidget) {
                                    startPlaybackFromWidget(viewHolderForChatImagesAndFile, viewHolderForChatImagesAndFile.audioSeekBar, conversation.fileLocalUri, conversation.conversationUid + "" + conversation.id, false);
                                } else {
                                    startPlayback(viewHolderForChatImagesAndFile, viewHolderForChatImagesAndFile.audioSeekBar, conversation.fileLocalUri);
                                }
                            }
                            viewHolderForChatImagesAndFile.ivPlay.setImageResource(R.drawable.ic_play);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Update UI for previously selected item if it exists
                    if (playingPosition != position) {
                        notifyItemChanged(playingPosition);
                    }
                    // Set the clicked item as selected
                    playingPosition = position;

                });

//                if (conversation.files.get(0).type.equalsIgnoreCase("image/jpeg") || conversation.files.get(0).type.equalsIgnoreCase("image/png") || conversation.files.get(0).type.equalsIgnoreCase("image/jpg")) {
                if (conversation.caption != null && !conversation.caption.isEmpty()) {
                    if(conversation.files.size()>0)
                    {
                        if (conversation.files.get(0).type.equalsIgnoreCase("image/jpeg") || conversation.files.get(0).type.equalsIgnoreCase("image/png") || conversation.files.get(0).type.equalsIgnoreCase("image/jpg")) {
                            viewHolderForChatImagesAndFile.layoutCaption.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.layoutFileCaption.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.txtCaption.setText(conversation.caption);
                        }else{
                            viewHolderForChatImagesAndFile.layoutCaption.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutFileCaption.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.txtFileCaption.setText(conversation.caption);
                        }
                    }
                } else {
                    viewHolderForChatImagesAndFile.layoutFileCaption.setVisibility(View.GONE);
                    viewHolderForChatImagesAndFile.txtFileCaption.setText("");
                    viewHolderForChatImagesAndFile.layoutCaption.setVisibility(View.GONE);
                    viewHolderForChatImagesAndFile.txtCaption.setText("");
                }

                if (conversation.isFailed){
                    if(conversation.files.get(0).type != null)
                    {
                        if (conversation.files.get(0).type.equalsIgnoreCase("image/jpeg") || conversation.files.get(0).type.equalsIgnoreCase("image/png") || conversation.files.get(0).type.equalsIgnoreCase("image/jpg")){
                            viewHolderForChatImagesAndFile.layoutError.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.imageError.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.ic_error.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.resendProgress.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.resendFileProgress.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.ivError.setVisibility(View.GONE);

                        }else{
                            viewHolderForChatImagesAndFile.ic_error.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.ivError.setVisibility(View.VISIBLE);
                            viewHolderForChatImagesAndFile.resendProgress.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.resendFileProgress.setVisibility(View.GONE);
                            viewHolderForChatImagesAndFile.layoutError.setVisibility(View.GONE);
                        }
                    }
                } else{
                    viewHolderForChatImagesAndFile.resendProgress.setVisibility(View.GONE);
                    viewHolderForChatImagesAndFile.layoutError.setVisibility(View.GONE);
                    viewHolderForChatImagesAndFile.ic_error.setVisibility(View.GONE);
                    viewHolderForChatImagesAndFile.ivError.setVisibility(View.GONE);
                    viewHolderForChatImagesAndFile.resendFileProgress.setVisibility(View.GONE);
                }

                viewHolderForChatImagesAndFile.imageError.setOnClickListener(view -> {
                    if (Utils.isNetworkAvailable(mcontex)){
                        viewHolderForChatImagesAndFile.txtFailed.setVisibility(View.GONE);
                        viewHolderForChatImagesAndFile.imageError.setVisibility(View.GONE);
                        viewHolderForChatImagesAndFile.resendProgress.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new FileResendEvent(conversation.caption,conversation.tempChatId,conversation.conversationUid,"FileResend"));
                    }else{
                        Toast.makeText(mcontex,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolderForChatImagesAndFile.ic_error.setOnClickListener(view -> {
                    if (Utils.isNetworkAvailable(mcontex)){
                        viewHolderForChatImagesAndFile.txtFailed.setVisibility(View.GONE);
                        viewHolderForChatImagesAndFile.imageError.setVisibility(View.GONE);
                        viewHolderForChatImagesAndFile.ivError.setVisibility(View.GONE);
                        viewHolderForChatImagesAndFile.resendProgress.setVisibility(View.GONE);
                        viewHolderForChatImagesAndFile.resendFileProgress.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new FileResendEvent(conversation.caption,conversation.tempChatId,conversation.conversationUid,"FileResend"));
                    }else{
                        Toast.makeText(mcontex,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else{
            com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolder vh = (com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolder) holder;
            if (!conversation.type.equalsIgnoreCase("system") && !conversation.type.equalsIgnoreCase("file")){
                if (conversation.content != null && !conversation.content.isEmpty()) {
                    vh.textMessage.setText(conversation.content);
                }
                if (!conversation.timestamp.isEmpty()) {
                    vh.txtTime.setText(""+common.covertTimeToLong(conversation.timestamp));
                }
                if (conversation.sender != null) {
                    if (conversation.isWelcome || conversation.type.equalsIgnoreCase("welcomeMessage")){
                        vh.txtName.setText(common.getOrganizationName(mcontex));
                        if(vh.txtNameFirstLetter!=null && !common.getOrganizationName(mcontex).isEmpty()){
                            vh.txtNameFirstLetter.setText(common.firstCharactorCapital(common.getOrganizationName(mcontex)));
                        }
                    }else{
                        vh.txtName.setText(conversation.sender);
                        if(vh.txtNameFirstLetter!=null){
                            vh.txtNameFirstLetter.setText(common.firstCharactorCapital(conversation.sender));
                        }
                    }
                }

                if (holder.getItemViewType() == viewTypeSenderMessage){
                    if (conversation.isNotNewChat){
                        if (conversation.isUpdateStatus){
                            if (conversation.isSeen){
                                vh.imageStatus.setVisibility(View.VISIBLE);
                                vh.imageStatus.setImageResource(R.drawable.read_receipt);
                            }else {
                                vh.imageStatus.setVisibility(View.VISIBLE);
                                vh.imageStatus.setImageResource(R.drawable.tick);
                            }
                        }
                    }else{
                        if (conversation.isSeen){
                            vh.imageStatus.setVisibility(View.VISIBLE);
                            vh.imageStatus.setImageResource(R.drawable.read_receipt);
                        }else{
                            if (conversation.id != 0){
                                vh.imageStatus.setVisibility(View.VISIBLE);
                                vh.imageStatus.setImageResource(R.drawable.tick);
                            }else{
                                vh.imageStatus.setVisibility(View.VISIBLE);
                                vh.imageStatus.setImageResource(R.drawable.send_messagetime);
                            }
                        }
                    }
                }
            } else{
                if (conversation.content != null && !conversation.content.isEmpty()) {
                    vh.textMessage.setText(conversation.content);
                }

                if (!conversation.timestamp.isEmpty()) {
                    vh.txtTime.setText(""+common.covertTimeToLong(conversation.timestamp));
                }
                if (conversation.rating != null && !conversation.rating.isEmpty()){
                    float rating= Float.parseFloat(conversation.rating);
                    if(rating != 0 ) {
                        vh.lnChatResloved.setVisibility(View.VISIBLE);
                        if(/*common.getFirstName(mcontex)*/o2ChatConfig.getFirstName(mcontex) != null) {
                            vh.txtUserName.setText(o2ChatConfig.getFirstName(mcontex)/*common.getFirstName(mcontex)*/);
                        }
                        if(conversation.feedback != null) {
                            vh.txtRatingFeedback.setText(conversation.feedback);
                            vh.library_normal_ratingbar.setRating(Float.parseFloat(conversation.rating));
                        }
                    }else{
                        vh.lnChatResloved.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(updateSeekBarRunnable);
            handler.removeCallbacks(updateRunnable);
        }
    }

    private void startPlayback(com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile viewHolderForChatImagesAndFile, SeekBar audioSeekBar, String audioUrl) {

        stopPlayback(); // Stop previous playback if any
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // MediaPlayer is prepared, now you can get the duration
                    int duration = mp.getDuration();
                    if (duration > 0) {
                        audioSeekBar.setMax(duration);
                        // Start playback
                        mp.start();
                        isPlaying = true;
                        // Update SeekBar progress
                        updateSeekBarRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer != null && isPlaying) {
                                    audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                                    handler.postDelayed(this, 100);
                                }
                            }
                        };
                        handler.post(updateSeekBarRunnable);
                        updateRunnable = new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                    viewHolderForChatImagesAndFile.txtAudioPlayTime.setText(String.format("%d:%02d", currentPosition / 60, currentPosition % 60));
                                    handler.postDelayed(this, 1000);
                                }
                            }
                        };
                        handler.post(updateRunnable);
                    } else {
                        Log.e(TAG, "Failed to retrieve duration. Duration is " + duration);
                    }
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Playback completed
                    isPlaying = false;
                    handler.removeCallbacks(updateSeekBarRunnable);
                    handler.removeCallbacks(updateRunnable);
                    audioSeekBar.setProgress(0);
                    notifyItemChanged(viewHolderForChatImagesAndFile.getAdapterPosition());
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(TAG, "MediaPlayer error. What: " + what + ", Extra: " + extra);
                    // Handle error
                    return false;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException occurred: " + e.getMessage());
        }

    }

    private void startPlaybackFromWidget(com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile viewHolderForChatImagesAndFile, SeekBar audioSeekBar, String audioUrl, String conversationUUIDandID, boolean isDuration) {
        stopPlayback(); // Stop previous playback if any
        String localFilePath = mcontex.getCacheDir() + "/"+conversationUUIDandID+".ogg"; // Ensure proper file naming and extension
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            // File already exists, get duration
            getAudioDuration(localFilePath, duration -> {
                audioSeekBar.setMax(duration);
                if (!isDuration){
                    playAudioFile(localFilePath, viewHolderForChatImagesAndFile, audioSeekBar);
                }else{
                    viewHolderForChatImagesAndFile.txtAudioTime.setText(common.formatTime(duration));
                }
            });
        } else {
            // File doesn't exist, download and play
            showLoader(viewHolderForChatImagesAndFile); // Show loader
            downloadFile(audioUrl, localFilePath, new com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.DownloadCallback() {
                @Override
                public void onDownloadComplete(String localPath) {
                    // File already exists, get duration
                    hideLoader(viewHolderForChatImagesAndFile);
                    getAudioDuration(localPath, duration -> {
                        audioSeekBar.setMax(duration);
                        if (!isDuration){
                            playAudioFile(localPath, viewHolderForChatImagesAndFile, audioSeekBar);
                        }else{
                            viewHolderForChatImagesAndFile.txtAudioTime.setText(common.formatTime(duration));
                        }
                    });
                }

                @Override
                public void onDownloadFailed(IOException e) {
                    Log.e(TAG, "Download failed: " + e.getMessage());
                    hideLoader(viewHolderForChatImagesAndFile);
                }
            });
        }
    }

    private void playAudioFile(String localFilePath, com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile viewHolderForChatImagesAndFile, SeekBar audioSeekBar) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(localFilePath);
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());

            mediaPlayer.setOnPreparedListener(mp -> {
                isPlaying = true;
                mediaPlayer.start();

                int duration = mediaPlayer.getDuration();
                audioSeekBar.setMax(duration);

                updateSeekBarRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null && isPlaying) {
                            audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                            handler.postDelayed(this, 100);
                        }
                    }
                };
                handler.post(updateSeekBarRunnable);
                updateRunnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                            viewHolderForChatImagesAndFile.txtAudioPlayTime.setText(String.format("%d:%02d", currentPosition / 60, currentPosition % 60));
                            handler.postDelayed(this, 1000);
                        }
                    }
                };
                handler.post(updateRunnable);
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                handler.removeCallbacks(updateSeekBarRunnable);
                handler.removeCallbacks(updateRunnable);
                audioSeekBar.setProgress(0);
                notifyItemChanged(viewHolderForChatImagesAndFile.getAdapterPosition());
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                return false;
            });

            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException occurred: " + e.getMessage());
        }
    }

    private void showLoader(com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile viewHolderForChatImagesAndFile) {
        ((Activity) mcontex).runOnUiThread(() -> {
            viewHolderForChatImagesAndFile.ivPlay.setVisibility(View.GONE);
            viewHolderForChatImagesAndFile.progressBarAudio.setVisibility(View.VISIBLE);
        });
    }

    private void hideLoader(com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.ViewHolderForChatImagesAndFile viewHolderForChatImagesAndFile) {
        ((Activity) mcontex).runOnUiThread(() -> {
            viewHolderForChatImagesAndFile.ivPlay.setVisibility(View.VISIBLE);
            viewHolderForChatImagesAndFile.progressBarAudio.setVisibility(View.GONE);
        });
    }

    interface DownloadCallback {
        void onDownloadComplete(String localPath);
        void onDownloadFailed(IOException e);
    }

    private void downloadFile(String fileUrl, String localPath, com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.DownloadCallback callback) {
        new Thread(() -> {
            try (InputStream input = new URL(fileUrl).openStream();
                 OutputStream output = new FileOutputStream(localPath)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                callback.onDownloadComplete(localPath);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onDownloadFailed(e);
            }
        }).start();
    }

    interface DurationCallback {
        void onDurationRetrieved(int duration);
    }

    private void getAudioDuration(String filePath, com.arittek.o2chatsdk.adapters.ConversationsByUIListAdapter.DurationCallback callback) {
        MediaPlayer tempMediaPlayer = new MediaPlayer();
        try {
            tempMediaPlayer.setDataSource(filePath);
            tempMediaPlayer.setOnPreparedListener(mp -> {
                int duration = mp.getDuration();
                mp.release(); // Release the temporary MediaPlayer
                callback.onDurationRetrieved(duration);
            });
            tempMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException occurred: " + e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return item_list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (item_list.get(position).type.equalsIgnoreCase("system")) {
            return viewTypeSystemMessage;
        }
        else if (item_list.get(position).type.equalsIgnoreCase("file")) {
            //if (item_list.get(position).toUserId == Integer.parseInt(common.getUserId(mcontex)) && !item_list.get(position).type.equalsIgnoreCase("system")) {
            if (item_list.get(position).isFromWidget && !item_list.get(position).type.equalsIgnoreCase("system")) {
                return viewTypeImageSenderMessage;
            }else{
                return viewTypeImageReceiverMessage;
            }
        }
        else if (item_list.get(position).isFromWidget && !item_list.get(position).type.equalsIgnoreCase("system") && !item_list.get(position).type.equalsIgnoreCase("welcomeMessage") ) {
            return viewTypeSenderMessage;
        }else{
            return viewTypeReceiverMessage;
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView  textMessage,txtName,txtNameFirstLetter,txtUserName,txtRatingFeedback;
        private ImageView imageStatus;
        private TextView txtTime;
        private LinearLayout lnChatResloved;
        private RatingBar library_normal_ratingbar;

        public ViewHolder(View view) {
            super(view);

            txtTime = view.findViewById(R.id.txtTime);
            textMessage = view.findViewById(R.id.textMessage);
            txtName = view.findViewById(R.id.txtName);
            txtNameFirstLetter = view.findViewById(R.id.txtNameFirstLetter);
            imageStatus = view.findViewById(R.id.imageStatus);

            // System Layout Use
            lnChatResloved = view.findViewById(R.id.lnChatResolved);
            txtUserName = view.findViewById(R.id.txtUserName);
            txtRatingFeedback = view.findViewById(R.id.txtRatingFeedback);
            library_normal_ratingbar = view.findViewById(R.id.library_normal_ratingbar);

        }
    }

    protected class ViewHolderForChatImagesAndFile extends RecyclerView.ViewHolder {

        private TextView textMessage;
        private TextView txtTime,txtCaption,txtFailed,txtFileCaption,txtAudioTime,txtAudioPlayTime;
        private ImageView image,ivMultimedia,icSource,imageStatus,imageError,ivError,ivPlay;
        private LinearLayout layoutFileShow,layoutImage,layoutCaption,layoutFileCaption;
        private LinearLayout layoutError,ic_error,lnAudio,lnOtherFiles,layoutBgFile;
        private ProgressBar resendProgress,resendFileProgress;
        private SeekBar audioSeekBar;
        private ProgressBar progressBarAudio;

        public ViewHolderForChatImagesAndFile(View view) {
            super(view);

            txtTime = view.findViewById(R.id.txtTime);
            textMessage = view.findViewById(R.id.txtMessage);
            image = view.findViewById(R.id.image);
            ivMultimedia = view.findViewById(R.id.ivMultimedia);
            lnOtherFiles = view.findViewById(R.id.lnOtherFiles);
            icSource = view.findViewById(R.id.icSource);
            layoutFileShow = view.findViewById(R.id.layoutFileShow);
            layoutImage = view.findViewById(R.id.layoutImage);
            imageStatus = view.findViewById(R.id.imageStatus);
            txtFileCaption = view.findViewById(R.id.txtFileCaption);
            layoutFileCaption = view.findViewById(R.id.layoutFileCaption);
            layoutCaption = view.findViewById(R.id.layoutCaption);
            txtCaption = view.findViewById(R.id.txtCaption);
            //layoutBgFile = view.findViewById(R.id.layoutBgFile);
            txtFailed = view.findViewById(R.id.txtFailed);
            layoutError = view.findViewById(R.id.layoutError);
            imageError = view.findViewById(R.id.imageError);
            resendProgress = view.findViewById(R.id.resendProgress);
            resendFileProgress = view.findViewById(R.id.resendFileProgress);
            ic_error = view.findViewById(R.id.ic_error);
            ivError = view.findViewById(R.id.ivError);

            audioSeekBar = view.findViewById(R.id.audioSeekBar);
            txtAudioTime = view.findViewById(R.id.txtAudioTime);
            lnOtherFiles = view.findViewById(R.id.lnOtherFiles);
            lnAudio = view.findViewById(R.id.lnAudio);
            ivPlay = view.findViewById(R.id.ivPlay);
            progressBarAudio = view.findViewById(R.id.progressBarAudio);
            txtAudioPlayTime = view.findViewById(R.id.txtAudioPlayTime);

        }
    }
}

