package com.example.o2chatsdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.o2chatsdk.Events.chatEvents.SendFileAfterPreview;
import com.example.o2chatsdk.adapters.ViewPagerAdapterUriLoad;
import com.example.o2chatsdk.databinding.FragmentFilePreviewBinding;
import com.example.o2chatsdk.fragments.ConversationsDetailFragment;
import com.example.o2chatsdk.model.chat.selectedFilePreviewData;

import com.example.o2chatsdk.commons.KeyboardVisibilityUtils;
import com.vanniktech.emoji.EmojiPopup;
//import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;

public class SelectFilePreviewActivity extends AppCompatActivity {

    private FragmentFilePreviewBinding binding;
    int currentPage = 0;
    private int lastPosition = 0;
    Intent intent1;
    ArrayList<selectedFilePreviewData> receivedList;
    ViewPagerAdapterUriLoad mImageAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityChat.isPause = false;
        binding = FragmentFilePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EmojiPopup popup = EmojiPopup.Builder.fromRootView(binding.getRoot()).build(binding.edtMessage);

        if (getIntent()!=null){
            intent1 = getIntent();
            receivedList = intent1.getParcelableArrayListExtra("arrayListData");

            if(receivedList.size()>0)
            {
                uriLoadImageSlider(receivedList);
            }
        }

        binding.ivSmile.setOnClickListener(v -> {
            binding.ivSmile.setVisibility(View.GONE);
            binding.ivKeyboard.setVisibility(View.VISIBLE);
            popup.toggle();
        });

        binding.ivKeyboard.setOnClickListener(v -> {
            binding.ivKeyboard.setVisibility(View.GONE);
            binding.ivSmile.setVisibility(View.VISIBLE);
            popup.dismiss();
        });

//        KeyboardVisibilityEvent.setEventListener(SelectFilePreviewActivity.this, isOpen -> {
//            if (isOpen){
//                binding.ivSmile.setVisibility(View.GONE);
//                binding.ivKeyboard.setVisibility(View.VISIBLE);
//            } else{
//                binding.ivSmile.setVisibility(View.VISIBLE);
//                binding.ivKeyboard.setVisibility(View.GONE);
//            }
//        });

        // Set keyboard visibility listener
        KeyboardVisibilityUtils.setKeyboardVisibilityListener(this, isOpen -> {
            if (isOpen){
                binding.ivSmile.setVisibility(View.GONE);
                binding.ivKeyboard.setVisibility(View.VISIBLE);
            } else {
                binding.ivSmile.setVisibility(View.VISIBLE);
            }
        });

        binding.menuClick.setOnClickListener(v -> {
            ConversationsDetailFragment.sendFileAfterPreview = null;
            ConversationsDetailFragment.isCalledFromPreviewActivityBack = true;
            MainActivityChat.isPause = true;
            finish();
        });

        binding.ViewPagerSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // This method will be called when the ViewPager is scrolled
            }

            @Override
            public void onPageSelected(int position) {
                // This method will be called when a new page becomes selected

                if (lastPosition > position) {
                    // Left Swipe
                    receivedList.get(currentPage).setMessage(binding.edtMessage.getText().toString());
                    currentPage = binding.ViewPagerSlider.getCurrentItem();
                    slideViewPager(binding.ViewPagerSlider.getCurrentItem());
                }else if (lastPosition < position) {
                    //  Right Swipe
                    receivedList.get(currentPage).setMessage(binding.edtMessage.getText().toString());
                    currentPage = binding.ViewPagerSlider.getCurrentItem();
                    slideViewPager(binding.ViewPagerSlider.getCurrentItem());
                }
                lastPosition = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Called when the state of the ViewPager is changed (e.g., idle, dragging, settling)
            }
        });

        binding.sendMessage.setOnClickListener(v -> {

            String message = "";
            message = binding.edtMessage.getText().toString();
            receivedList.get(currentPage).setMessage(binding.edtMessage.getText().toString());
            ConversationsDetailFragment.isCalledFromPreviewActivity = true;
            SendFileAfterPreview sendFileAfterPreview = new SendFileAfterPreview(message,receivedList,"fileUri","","","SendFileAfterPreview");
            ConversationsDetailFragment.sendFileAfterPreview = sendFileAfterPreview;
            MainActivityChat.isPause = true;
            finish();

        });
    }

    public void uriLoadImageSlider(ArrayList<selectedFilePreviewData> receivedList) {

        binding.ViewPagerSlider.setVisibility(View.VISIBLE);

        if(receivedList.size()>0){
            mImageAdapter = new ViewPagerAdapterUriLoad(SelectFilePreviewActivity.this,receivedList,SelectFilePreviewActivity.this);
            try {
                binding.ViewPagerSlider.setAdapter(mImageAdapter);
            } catch (Exception e){e.printStackTrace();}
        }
    }

    private void slideViewPager(int selectCurrentPage) {

        if (currentPage >=0 &&  currentPage <= binding.ViewPagerSlider.getAdapter().getCount()) {
            if (receivedList.get(selectCurrentPage).getMessage() != null && !receivedList.get(selectCurrentPage).getMessage().isEmpty()) {
                    binding.edtMessage.setText(receivedList.get(selectCurrentPage).getMessage());
            }else{
                binding.edtMessage.setText("");
            }
        }
    }

//    @Override
//    public void onPageChanged(int page, int pageCount) {
//        Log.d("","index---->"+binding.ViewPagerSlider.getCurrentItem());
//
//    }
//
//    @Override
//    public void loadComplete(int nbPages) {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivityChat.isPause = false;
    }

//    @Override
//    public void onPageError(int page, Throwable t) {
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}