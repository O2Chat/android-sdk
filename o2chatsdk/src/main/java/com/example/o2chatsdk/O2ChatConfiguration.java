package com.example.o2chatsdk;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.o2chatsdk.commons.Common;

public final class O2ChatConfiguration {

    private String domain;
    private String appId = "";
    private String appKey = "";
    private static final String PREFS_NAME = "o2chat_prefs";
    private static final String KEY_CHANNEL_ID = "channel_id";

    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CNIC = "cnic";
    private static final String KEY_PHONE = "phone";

    private static final String App_NAME = "app_name";
    private SharedPreferences sharedPreferences;
    private boolean responseExpectationEnabled = true;
    private boolean teamMemberInfoVisible = true;
    private boolean cameraCaptureEnabled = true;
    private boolean gallerySelectionEnabled = true;
    private boolean userEventsTrackingEnabled = true;

    private Context mContext;

    private static O2ChatConfiguration instance;

    // Private constructor to prevent direct instantiation
    private O2ChatConfiguration(@NonNull Context context, @NonNull String appId, @NonNull String appKey) {
        mContext = context;
        this.appId = appId.trim();
        this.appKey = appKey.trim();
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Public method to provide access to the instance
    public static synchronized O2ChatConfiguration getInstance(@NonNull Context context, @NonNull String appId, @NonNull String appKey) {
        if (instance == null) {
            instance = new O2ChatConfiguration(context, appId, appKey);
        }
        return instance;
    }

    // Method to reset the instance
    public static synchronized void resetInstance() {
        instance = null;
    }

    // Public method to provide access to the instance without creating new instance
    public static synchronized O2ChatConfiguration getInstance(@NonNull Context context) {
        if (instance == null) {
            throw new IllegalStateException("O2ChatConfig is not initialized, call getInstance(context, appId, appKey) first");
        }
        return instance;
    }

    public void setDomain(@Nullable String domain) {
        if (isValidDomain(domain)) {
            this.domain = domain.toLowerCase();
        }
    }

    private boolean isValidDomain(@Nullable String domain) {
        return domain != null && !domain.trim().isEmpty();
    }

    public String getChannelID() {
        return sharedPreferences.getString(KEY_CHANNEL_ID, "");
    }

    public String getAppName() {
        return sharedPreferences.getString(App_NAME, "");
    }

    public void setChannelID(@Nullable String channelID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CHANNEL_ID, channelID != null ? channelID : "");
        editor.apply(); // Use apply() for asynchronous saving
    }

    public void setAppName(@Nullable String appName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(App_NAME, appName != null ? appName : "");
        editor.apply(); // Use apply() for asynchronous saving
    }

    @Nullable
    public String getDomain() {
        return this.domain;
    }

    public String getAppId() {
        return this.appId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public boolean isTeamMemberInfoVisible() {
        return this.teamMemberInfoVisible;
    }

    public O2ChatConfiguration setTeamMemberInfoVisible(boolean visible) {
        this.teamMemberInfoVisible = visible;
        return this;
    }

    public boolean isResponseExpectationEnabled() {
        return this.responseExpectationEnabled;
    }

    public O2ChatConfiguration setResponseExpectationEnabled(boolean enabled) {
        this.responseExpectationEnabled = enabled;
        return this;
    }

    public boolean isCameraCaptureEnabled() {
        return this.cameraCaptureEnabled;
    }

    public O2ChatConfiguration setCameraCaptureEnabled(boolean enable) {
        this.cameraCaptureEnabled = enable;
        return this;
    }

    public boolean isGallerySelectionEnabled() {
        return this.gallerySelectionEnabled;
    }

    public O2ChatConfiguration setGallerySelectionEnabled(boolean enable) {
        this.gallerySelectionEnabled = enable;
        return this;
    }

    public boolean isUserEventsTrackingEnabled() {
        return this.userEventsTrackingEnabled;
    }

    public void setUserEventsTrackingEnabled(boolean enable) {
        this.userEventsTrackingEnabled = enable;
    }

    // User Detail
    public String getFirstName() {
        return sharedPreferences.getString(KEY_FIRST_NAME, "");
    }

    public String getLastName() {
        return sharedPreferences.getString(KEY_LAST_NAME, "");
    }

    public String getCnic() {
        return sharedPreferences.getString(KEY_CNIC, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public void setFirstName(@Nullable String firstName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, firstName != null ? firstName : "");
        editor.apply(); // Use apply() for asynchronous saving
    }

    public void setLastName(@Nullable String lastName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_NAME, lastName != null ? lastName : "");
        editor.apply(); // Use apply() for asynchronous saving
    }

    public void setEmail(@Nullable String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email != null ? email : "");
        editor.apply(); // Use apply() for asynchronous saving
    }

    public void setCnic(@Nullable String cnic) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CNIC, cnic != null ? cnic : "");
        editor.apply(); // Use apply() for asynchronous saving
    }

    public void setPhoneNumber(@Nullable String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PHONE, phone != null ? phone : "");
        editor.apply(); // Use apply() for asynchronous saving
    }

    // Clear all data
    public void clearAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data in SharedPreferences
        editor.apply(); // Use apply() for asynchronous saving
    }

    public void clearAllLocalDatabase() {
        new Common().deleteChatLocalDB(mContext);
    }
}