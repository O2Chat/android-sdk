package com.arittek.o2chatsdk.commons;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.arittek.o2chatsdk.localDB.AppDatabase;

public class O2ChatConfig {

    public void setTitleTextColor(Context context,String colorCode)
    {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("setTextColor", MODE_PRIVATE).edit();
            editor.putString("TitleTextColor", colorCode);
            editor.apply();
        }
    }
    public String getTitleTextColor(Context context)
    {

        SharedPreferences prefs = context.getSharedPreferences("setTextColor", MODE_PRIVATE);
        String TaxFormString = prefs.getString("TitleTextColor", "#FF000000");
        return TaxFormString;

    }

    public void setTitleBarColor(Context context,String colorCode)
    {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("setTitleColor", MODE_PRIVATE).edit();
            editor.putString("TitleColor", colorCode);
            editor.apply();
        }
    }
    public String getTitleBarColor(Context context)
    {

            SharedPreferences prefs = context.getSharedPreferences("setTitleColor", MODE_PRIVATE);
            String TaxFormString = prefs.getString("TitleColor", "#FFFFFFFF");
            return TaxFormString;

    }

    //get token for client
    public void saveFcmToken(Context context,String saveToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences("FcmTokenString", MODE_PRIVATE).edit();
        editor.putString("fcm_tokenString", saveToken);
        editor.apply();
    }

    public void saveCustomerMobileNumber(Context context, String customerMobileNumber) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("saveCustomerMobileNumber", MODE_PRIVATE).edit();
            editor.putString("CustomerMobileNumber", customerMobileNumber);
            editor.apply();
        }
    }

    public String getCustomerMobileNumber(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("saveCustomerMobileNumber", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerMobileNumber", "");
        return TaxFormString;
    }

    public void saveFirstName(Context context, String firstName)
    {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("CustomerNamePreference", MODE_PRIVATE).edit();
            editor.putString("CustomerName", firstName);
            editor.apply();
        }
    }

    public String getFirstName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CustomerNamePreference", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerName", "test o2chat acc");
        return TaxFormString;
    }



    public String getCustomerEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("saveCustomerEmail", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerEmailAddress", "chat321o2chat@gmail.com");
        return TaxFormString;
    }

    public void saveCustomerEmail(Context context, String firstName) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("saveCustomerEmail", MODE_PRIVATE).edit();
            editor.putString("CustomerEmailAddress", firstName);
            editor.apply();
        }
    }

    public String getCustomerCnic(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("saveCnicNumber", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerCnicNumber", "4677976454887");
        return TaxFormString;
    }
    public void saveCnicNumber(Context context, String firstName) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("saveCnicNumber", MODE_PRIVATE).edit();
            editor.putString("CustomerCnicNumber", firstName);
            editor.apply();
        }
    }

    public String getChannelID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("chnlIdPref", MODE_PRIVATE);
        String TaxFormString = prefs.getString("chnlId", "");
        return TaxFormString;
    }

    public void saveChannelID(Context context, String channelId) {
        if(context != null) {
            SharedPreferences.Editor editor = context.getSharedPreferences("chnlIdPref", MODE_PRIVATE).edit();
            editor.putString("chnlId", channelId);
            editor.apply();
        }
    }
    public void deleteChatLocalDB(Context context){
        if (context!=null){
            AppDatabase dbchat = AppDatabase.getAppDatabase(context.getApplicationContext());
            dbchat.conversationDetailDao().deleteAllConversation();
            dbchat.conversationDetailDao().deleteConversationFilesData();
            SharedPreferences pref = context.getSharedPreferences("isfirstTimeWelcomeMessage3",0);
            SharedPreferences.Editor editorfirsttime= pref.edit();
            editorfirsttime.putBoolean("firstTimeWelcomeMessage3", true);
            SharedPreferences prefWelcome = context.getSharedPreferences("isfirstTimeWelcomeMessage",0);
            SharedPreferences.Editor editorwelcome= prefWelcome.edit();
            editorwelcome.putBoolean("firstTimeWelcomeMessage", true);
            SharedPreferences prefWelcome3 = context.getSharedPreferences("isfirstTimeWelcomeMessage3",0);
            SharedPreferences.Editor editorwelcome3= prefWelcome3.edit();
            editorwelcome3.putBoolean("firstTimeWelcomeMessage3", true);
            new Common().saveConversationUID(context,"");
            new Common().saveCustomerId(context,0);

        }
    }

}
