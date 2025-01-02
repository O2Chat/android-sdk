package com.arittek.signalrtestandroid.activities;

import android.content.Intent;
import android.os.Bundle;

import com.arittek.signalrtestandroid.commons.Common;
import com.example.signalrtestandroid.databinding.ActivitySplashBinding;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.ui.AppBarConfiguration;

public class SplashActivityCHat extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        launchExpenseModule();

    }

    private void launchExpenseModule() {
        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Common common = new Common();
            if (common.getIsLoggedIn(this)){
                Intent intent = new Intent(SplashActivityCHat.this, MainActivityChat.class);
                intent.putExtra("conversationByUID","");
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(SplashActivityCHat.this, MainActivityChat.class);
                intent.putExtra("conversationByUID","");
                startActivity(intent);
                finish();
            }

        }).start();
    }

}