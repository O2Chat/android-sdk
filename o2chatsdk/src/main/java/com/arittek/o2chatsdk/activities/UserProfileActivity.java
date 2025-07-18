package com.arittek.o2chatsdk.activities;

import static com.arittek.o2chatsdk.commons.Utils.setupEdgeToEdge;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.arittek.o2chatsdk.commons.Common;
import com.arittek.o2chatsdk.commons.Utils;
import com.arittek.o2chatsdk.R;
import com.arittek.o2chatsdk.databinding.ActivityProfileUpdateBinding;

public class UserProfileActivity extends AppCompatActivity {

    ActivityProfileUpdateBinding profileUpdateBinding;
    Common common;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileUpdateBinding = ActivityProfileUpdateBinding.inflate(getLayoutInflater());
        setContentView(profileUpdateBinding.getRoot());
        setupEdgeToEdge(this);

        common = new Common();

        if (Utils.isNetworkAvailable(UserProfileActivity.this)) {
            // getProfileData();
        } else {
            Utils.customeTost(getString(R.string.no_internet), UserProfileActivity.this, view);
        }

//        profileUpdateBinding.imgBackArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

//    public void getProfileData() {
//
//        if (UserProfileActivity.this.getWindow() != null) {
//            UserProfileActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        }
//
//        new ApiClient(UserProfileActivity.this).getWebService().getUserById("1").enqueue(new Callback<WebResponse<UserProfile>>() {
//            @Override
//            public void onResponse(Call<WebResponse<UserProfile>> call, Response<WebResponse<UserProfile>> response) {
//                if (response.body() != null) {
//                    if (response.body().isSuccess()) {
//
//                        profileUpdateBinding.txtUsername.setText(response.body().getResult().firstName+" "+response.body().getResult().lastName);
//                        profileUpdateBinding.txtEmailAddress.setText(response.body().getResult().contactEmail);
//                        profileUpdateBinding.txtMobileNo.setText(response.body().getResult().contactNo);
//
//                    } else {
//                        Toast.makeText(UserProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(UserProfileActivity.this, "Server Con,t Response", Toast.LENGTH_SHORT).show();
//                }
//
//                if (UserProfileActivity.this.getWindow() != null) {
//                    UserProfileActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WebResponse<UserProfile>> call, Throwable t) {
//                if (UserProfileActivity.this.getWindow() != null) {
//                    UserProfileActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                }
//                if (t.getMessage().contains("Failed to connect")) {
//                    Toast.makeText(UserProfileActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
}