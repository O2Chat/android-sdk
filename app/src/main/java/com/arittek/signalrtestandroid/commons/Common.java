package com.arittek.signalrtestandroid.commons;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.arittek.signalrtestandroid.Events.appEvents.MessageEvent;
import com.arittek.signalrtestandroid.activities.MainActivityChat;
import com.arittek.signalrtestandroid.localDB.AppDatabase;
import com.arittek.signalrtestandroid.model.chat.Conversation;
import com.arittek.signalrtestandroid.model.chat.ConversationByUID;
import com.arittek.signalrtestandroid.model.chat.PermissionDataModel;
import com.arittek.signalrtestandroid.model.chat.RecieveMessage;
import com.arittek.signalrtestandroid.model.login.LoginResponseData;
import com.arittek.signalrtestandroid.retrofit.ApiClient;
import com.arittek.signalrtestandroid.retrofit.WebResponse;
import com.arittek.signalrtestandroid.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Common {

//        new Gson().toJson(commons.getAlertLoginBanner(LoginActivity.this))
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    // is isValidate Foreign Assets Wealth Statement
    public boolean isValidateConversationFeedBackDialog(Context applicationContext, RatingBar ratingBar)
    {
        if (ratingBar.getRating()==0) {
            return false;
        }
        return true;
    }

    public static void setEditTextFocus(Context context, View textFocus) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(textFocus, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isTextNullOrEmpty(String text) {
        if (text == null) {
            return true;
        }
        if (text.isEmpty()) {
            return true;
        }
        return text.trim().equalsIgnoreCase("");


    }

    public String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return  minutes + ":" + String.format("%02d", seconds);

    }

    public void setIsResolved(Context context,boolean isResolved) {
        SharedPreferences.Editor editor = context.getSharedPreferences("isResolvedPref", MODE_PRIVATE).edit();
        editor.putBoolean("isResolved", isResolved);
        editor.apply();
    }


    public boolean getIsResolved(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("isResolvedPref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isResolved",false);
    }
    public void saveLastConversationId(Context context, long saveConversation) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveLastConversationIdPref", MODE_PRIVATE).edit();
        editor.putLong("saveLastConversationId", saveConversation);
        editor.apply();
    }
    public Long getLastConversationId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveLastConversationIdPref", MODE_PRIVATE);
        return sharedPreferences.getLong(  "saveLastConversationId",0);
    }

    // save Current screen
    public void saveCurrentScreen(Context context, String saveConversation) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveCurrentScreenPref", MODE_PRIVATE).edit();
        editor.putString("saveCurrentScreen", saveConversation);
        editor.apply();
    }

    public String getCurrentScreen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveCurrentScreenPref", MODE_PRIVATE);
        return sharedPreferences.getString("saveCurrentScreen","");
    }


    // save Current screen
    public void saveConversationUID(Context context, String saveConversation) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveConversationUIDPref", MODE_PRIVATE).edit();
        editor.putString("saveConversationUID", saveConversation);
        editor.apply();
    }

    public String getConversationUID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveConversationUIDPref", MODE_PRIVATE);
        return sharedPreferences.getString("saveConversationUID","");
    }


    public void setIsConversationOpen(Context context, boolean setIsUnAssign) {
        SharedPreferences.Editor editor = context.getSharedPreferences("setIsConversationOpenPref", MODE_PRIVATE).edit();
        editor.putBoolean("setIsConversationOpen", setIsUnAssign);
        editor.apply();
    }
    public boolean getIsConversationOpen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setIsConversationOpenPref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("setIsConversationOpen",false);
    }


    // save Current screen
    public void setIsUnAssign(Context context, boolean setIsUnAssign) {
        SharedPreferences.Editor editor = context.getSharedPreferences("setIsUnAssignDPref", MODE_PRIVATE).edit();
        editor.putBoolean("setIsUnAssign", setIsUnAssign);
        editor.apply();
    }

    public boolean getIsUnAssign(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("setIsUnAssignDPref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("setIsUnAssign",false);
    }


    // saveUnAssignStatus
    public void saveUnAssignStatus(Context context, String saveConversation) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveUnAssignStatusPref", MODE_PRIVATE).edit();
        editor.putString("saveUnAssignStatus", saveConversation);
        editor.apply();
    }

    public String getUnAssignStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveUnAssignStatusPref", MODE_PRIVATE);
        return sharedPreferences.getString("saveUnAssignStatus","");
    }

    public String getSelectedMenu(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveSelectedMenuPrefrence", MODE_PRIVATE);
        return sharedPreferences.getString("saveSelectedMenu","");
    }

    public void saveSelectedMenu(Context context, String saveSelectedMenu) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveSelectedMenuPrefrence", MODE_PRIVATE).edit();
        editor.putString("saveSelectedMenu", saveSelectedMenu);
        editor.apply();
    }

    public void saveBaseUrlChat(Context context, String saveBaserl) {
        SharedPreferences.Editor editor = context.getSharedPreferences("getBaseUrlChatPref", MODE_PRIVATE).edit();
        editor.putString("getBaseUrlChat", saveBaserl);
        editor.apply();
    }

    public String getBaseUrlChat(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("getBaseUrlChatPref", MODE_PRIVATE);
        return sharedPreferences.getString("getBaseUrlChat","");
    }
    public String getConnectionID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveConnectionIdPref", MODE_PRIVATE);
        return sharedPreferences.getString("saveConnectionId","");
    }
    public void saveConnectionId(Context context, String saveConnectionId) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveConnectionIdPref", MODE_PRIVATE).edit();
        editor.putString("saveConnectionId", saveConnectionId);
        editor.apply();
    }
    public void saveCustomerId(Context context, long saveConnectionId) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveCustomerIdPref", MODE_PRIVATE).edit();
        editor.putLong("saveCustomerId", saveConnectionId);
        editor.apply();
    }

    public long getCustomerID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveCustomerIdPref", MODE_PRIVATE);
        return sharedPreferences.getLong("saveCustomerId",0);
    }
    // save conversation data on which user click
    public void saveConversation(Context context, String saveConversation) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveConversationPref", MODE_PRIVATE).edit();
        editor.putString("saveConversationData", saveConversation);
        editor.apply();
    }
    // get conversation data on user click
    public Conversation getConversationData(Context context) {
        Conversation conversation;
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveConversationPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saveConversationData", null);
        Type type = new TypeToken<Conversation>() {}.getType();
        conversation = gson.fromJson(json, type);
        return conversation;
    }

    public void saveFcmToken(Context context, String fcmToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences("FcmTokenString", MODE_PRIVATE).edit();
        editor.putString("fcm_tokenString", fcmToken);
        editor.apply();
    }

    public String getFcmToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("FcmTokenString", MODE_PRIVATE);
        String TaxFormString = prefs.getString("fcm_tokenString", "");
        return TaxFormString;
    }

    // get banner login list
    public ArrayList<Conversation> getConversationList(Context context) {
        ArrayList<Conversation> conversationArrayList;
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveConversationListPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saveConversationList", null);
        Type type = new TypeToken<ArrayList<Conversation>>() {}.getType();
        conversationArrayList = gson.fromJson(json, type);
        if (conversationArrayList == null) {
            conversationArrayList = new ArrayList<Conversation>();
        }
        return conversationArrayList;
    }

    // get  Topic Channel list



    public void saveConversationList(Context context, String deviceToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveConversationListPref", MODE_PRIVATE).edit();
        editor.putString("saveConversationList", deviceToken);
        editor.apply();
    }

    public void saveDisplayName(Context context, String displayName) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveDisplayNamePref", MODE_PRIVATE).edit();
        editor.putString("saveDisplayName", displayName);
        editor.apply();
    }
    public void saveOrganizationName(Context context, String orgName) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveOrganizationNamePref", MODE_PRIVATE).edit();
        editor.putString("saveOrganizationName", orgName);
        editor.apply();
    }

    public void saveDeviceToken(Context context, String deviceToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences("deviceTokenPref", MODE_PRIVATE).edit();
        editor.putString("deviceToken", deviceToken);
        editor.apply();
    }

    public String getDeviceToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("deviceTokenPref", MODE_PRIVATE);
        return sharedPreferences.getString("deviceToken","");
    }

    // saveUserData
    public void saveUserLoginData(Context context, String loginResponseData) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveUserLoginDataPref", MODE_PRIVATE).edit();
        editor.putString("saveUserLoginData", loginResponseData);
        editor.apply();
    }
    // saveUserData
    public void savePermission(Context context, String savePermission) {
        SharedPreferences.Editor editor = context.getSharedPreferences("savePermissionPref", MODE_PRIVATE).edit();
        editor.putString("savePermission", savePermission);
        editor.apply();
    }
    // get login UserData
    public PermissionDataModel getPermission(Context context) {
        PermissionDataModel permissionDataModel;
        SharedPreferences sharedPreferences = context.getSharedPreferences("savePermissionPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("savePermission", null);
        Type type = new TypeToken<PermissionDataModel>() {}.getType();
        permissionDataModel = gson.fromJson(json, type);
        return permissionDataModel;
    }


    // get login UserData
    public LoginResponseData getUserLoginDate(Context context) {
        LoginResponseData loginResponseData;
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveUserLoginDataPref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saveUserLoginData", null);
        Type type = new TypeToken<LoginResponseData>() {}.getType();
        loginResponseData = gson.fromJson(json, type);
        return loginResponseData;
    }

    // save Conversaction List
    public void saveConversationSource(Context context, String conversactionData) {
        SharedPreferences.Editor editor = context.getSharedPreferences("saveConversationSourcePrep", MODE_PRIVATE).edit();
        editor.putString("saveConversationSource", conversactionData);
        editor.apply();
    }

    // get is super admin
    public String getConversationSource(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveConversationSourcePrep", MODE_PRIVATE);
        return sharedPreferences.getString("saveConversationSource","");
    }



    // save is user loggin
    public void isLoggedIn(Context context,boolean userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences("isLoginUserPref", MODE_PRIVATE).edit();
        editor.putBoolean("isLoginUser", userId);
        editor.apply();
    }
    // check is user login
    public boolean getIsLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("isLoginUserPref", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoginUser",false);
    }
    // save is super admin
    public void saveIsSuperAdmin(Context context,String isSuperAdmin) {
        SharedPreferences.Editor editor = context.getSharedPreferences("isSuperAdminPref", MODE_PRIVATE).edit();
        editor.putString("isSuperAdmin", isSuperAdmin);
        editor.apply();
    }

    // get is super admin
    public String getIsSuperAdmin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("isSuperAdminPref", MODE_PRIVATE);
        return sharedPreferences.getString("isSuperAdmin","");
    }

    // save User ID
    public void saveUserId(Context context,String userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userIdPref", MODE_PRIVATE).edit();
        editor.putString("userId", userId);
        editor.apply();
    }
    // get User ID
    public String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("userIdPref", MODE_PRIVATE);
        return sharedPreferences.getString("userId","");
    }

    // saveToken
    public void saveToken(Context context,String saveToken) {
        SharedPreferences.Editor editor = context.getSharedPreferences("tokenauthPref", MODE_PRIVATE).edit();
        editor.putString("tokenSaved", saveToken);
        editor.apply();
    }

    public String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenauthPref", MODE_PRIVATE);
        return sharedPreferences.getString("tokenSaved","");
    }

    public String getOrganizationName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveOrganizationNamePref", MODE_PRIVATE);
        return sharedPreferences.getString("saveOrganizationName","");
    }
    public String getDisplayName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveDisplayNamePref", MODE_PRIVATE);
        return sharedPreferences.getString("saveDisplayName","");
    }

    public String firstCharactorCapital(String name){
        String captilizedString="";
        if(!name.isEmpty()){
            if(!name.trim().equals("")){
                captilizedString = name.substring(0,1).toUpperCase();
            }
        }
        return captilizedString;
    }

    public String covertTimeToLong(String dataDate) {
        String formattedDate = "";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("PST"));
            Date date = utcFormat.parse(dataDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YY h:mm a");
            System.out.println(dateFormat.format(date));
            formattedDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String convertFileToBase64(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        byte[] fileContent = Files.readAllBytes(path);

        // Encode the file content in Base64
        byte[] base64Encoded = Base64.getEncoder().encode(fileContent);

        // Convert the byte array to a string
        return new String(base64Encoded);
    }
    public Date covertTimeStampToDate(String dataDate) {
        Date inputDate = null;
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            inputDate = utcFormat.parse(dataDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate;
    }

    public String covertTimeToText(Context mcontext,String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("ConvTimeE", e.getMessage());
        }

        return convTime;
    }


    public File compressImage(String imagePath, Context context) {
         final float maxHeight = 1280.0f;
         final float maxWidth = 1280.0f;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        if (bmp != null) {
            bmp.recycle();
        }
        ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        File filepath = null;
        try {
            if(context!=null){
                filepath = createImageFile(context);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //new File(imageFilePath).delete();
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filepath;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }


    public File createImageFile(Context context) throws IOException {
        // Create an image file name
        File image = null;
        if(context!=null){
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";

            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static void initSSL(OkHttpClient.Builder httpClientBuilder, Context context) {
        SSLContext sslContext = null;
        try {
            sslContext = createCertificate(context.getResources().openRawResource(R.raw.meintaxi));
        } catch (CertificateException | IOException | KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(sslContext!=null){
            httpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), systemDefaultTrustManager());
        }
    }

    private static SSLContext createCertificate(InputStream trustedCertificateIS) throws CertificateException, IOException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try {
            ca = cf.generateCertificate(trustedCertificateIS);
        } finally {
            trustedCertificateIS.close();
        }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;

    }
    private static X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }


    public void openFile(Context context, File files) throws IOException {
        MainActivityChat.isPause = false;
        // Create URI
        File file = files;
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName()+ ".provider" ,file);
        Intent intent ;

        String  url = uri.toString().substring(uri.toString().lastIndexOf("."));
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.equalsIgnoreCase(".doc")){
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/msword");        }
        else if (url.equalsIgnoreCase(".docx")) {
            // Word document
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/msword");
        } else if(url.equalsIgnoreCase(".pdf")) {
            // PDF file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.equalsIgnoreCase(".ppt") || url.equalsIgnoreCase(".pptx")) {
            // Powerpoint file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.equalsIgnoreCase(".xls") || url.equalsIgnoreCase(".xlsx")) {
            // Excel file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.equalsIgnoreCase(".zip") || url.equalsIgnoreCase(".rar")) {
            // WAV audio file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.equalsIgnoreCase(".rtf")) {
            // RTF file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.equalsIgnoreCase(".wav") || url.equalsIgnoreCase(".mp3")) {
            // WAV audio file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.equalsIgnoreCase(".gif")) {
            // GIF file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/gif");
        } else if(url.equalsIgnoreCase(".jpg") || url.equalsIgnoreCase(".jpeg") || url.equalsIgnoreCase(".png")) {
            // JPG file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.equalsIgnoreCase(".txt")) {
            // Text file
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "text/plain");
        } else if(url.equalsIgnoreCase(".3gp") || url.equalsIgnoreCase(".mpg") || url.equalsIgnoreCase(".mpeg") || url.equalsIgnoreCase(".mpe") || url.equalsIgnoreCase(".mp4") || url.equalsIgnoreCase(".avi")) {
            // Video files
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context,"There is no any app available to open this file",Toast.LENGTH_SHORT).show();
        }
    }

    public String getFolderSizeLabel(File file) {
        double size = (double) getFolderSize(file) / 1000.0; // Get size and convert bytes into KB.
        if (size >= 1024) {
            return (size / 1024) + " MB";
        } else {
            return size + " KB";
        }
    }
    public static long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    public String getMimeType(Uri uri,Context context) {
        String mimeType = "";
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
    public void saveCustomerMobileNumber(Context context, String customerMobileNumber) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("saveCustomerMobileNumber", MODE_PRIVATE).edit();
            editor.putString("CustomerMobileNumber", customerMobileNumber);
            editor.apply();
        }
    }
    public String getCustomerMobileNumber(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("saveCustomerMobileNumber", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerMobileNumber", "");
        return TaxFormString;
    }

    public void saveFirstName(Context context, String firstName) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("CustomerNamePreference", MODE_PRIVATE).edit();
            editor.putString("CustomerName", firstName);
            editor.apply();
        }
    }
    public String getFirstName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("CustomerNamePreference", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerName", "Umair Abbasi");
        return TaxFormString;
    }

    public String getCustomerCnic(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("saveCnicNumber", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerCnicNumber", "1234567890123");
        return TaxFormString;
    }

    public String getCustomerEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("saveCustomerEmail", MODE_PRIVATE);
        String TaxFormString = prefs.getString("CustomerEmailAddress", "umair   @gmail.com");
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

    public void saveNotificationCount(Context context, String count) {
        if(context != null)
        {
            SharedPreferences.Editor editor = context.getSharedPreferences("ChatNotificationCountPref", MODE_PRIVATE).edit();
            editor.putString("ChatNotificationCount", count);
            editor.apply();
        }
    }

    public String getChatNotificationCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("ChatNotificationCountPref", MODE_PRIVATE);
        String TaxFormString = prefs.getString("ChatNotificationCount", "");
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
        String TaxFormString = prefs.getString("chnlId", "f26a33d9-5b2e-4227-a456-eab45924a1d3");
        return TaxFormString;
    }
    public void saveChannelID(Context context, String channelId) {
        if(context != null) {
            SharedPreferences.Editor editor = context.getSharedPreferences("chnlIdPref", MODE_PRIVATE).edit();
            editor.putString("chnlId", channelId);
            editor.apply();
        }
    }
    public void saveConversationUUId(Context context, String conversationUId) {
        if(context != null) {
            SharedPreferences.Editor editor = context.getSharedPreferences("saveConversationUIdPref", MODE_PRIVATE).edit();
            editor.putString("saveConversationUId", conversationUId);
            editor.apply();
        }
    }

    public String getConversationUUId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("saveConversationUIdPref", MODE_PRIVATE);
        String TaxFormString = prefs.getString("saveConversationUId", "");
//        String TaxFormString = prefs.getString("saveConversationUId", "15eb57eb-4801-4321-9aa7-80cd408d2385");
        return TaxFormString;
    }

    public void logoutUserChatData(Context context,int channelId){
        new ApiClient(context).getWebService().logoutApiForChat(channelId).enqueue(new Callback<WebResponse>() {
            @Override
            public void onResponse(Call<WebResponse> call, Response<WebResponse> response) {
                if (response.body()!=null){
                    if(response.isSuccessful()){
                        EventBus.getDefault().post(new MessageEvent( "LogoutChatCalled"));
                    }
                }else{
                    EventBus.getDefault().post(new MessageEvent( "LogoutChatNotCalled"));
                }

            }

            @Override
            public void onFailure(Call<WebResponse> call, Throwable t) {
                EventBus.getDefault().post(new MessageEvent( "LogoutChatNotCalled"));

            }
        });

    }

    public String getUtcTime()
    {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Format the `Date` object to a string in UTC
        String utcTime = utcDateFormat.format(new Date());
        String localDateFormatTime = localDateFormat.format(new Date());
        Log.d("LocaldateTime::",localDateFormatTime);
        Log.d("utcdateTime:",utcTime);

        return utcTime;
    }


    public  String getCurrentDays()
    {
        Calendar calendar = Calendar.getInstance();
        int todayDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        String[] weekdays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        Map<String, Integer> daysOfWeek = new HashMap<>();

        daysOfWeek.put("Monday", 0);
        daysOfWeek.put("Tuesday", 1);
        daysOfWeek.put("Wednesday", 2);
        daysOfWeek.put("Thursday", 3);
        daysOfWeek.put("Friday", 4);
        daysOfWeek.put("Saturday", 5);
        daysOfWeek.put("Sunday", 6);

        int payloadDayIndex = daysOfWeek.get(weekdays[todayDay]);

        return String.valueOf(payloadDayIndex);

    }

    public String getTimeZone()
    {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getDisplayName(false, TimeZone.LONG)+" - ("+timeZone.getDisplayName(false, TimeZone.SHORT)+")";
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

        }
    }

    public ConversationByUID getConversationFromReceiveMsg(RecieveMessage recieveMessage) {
        ConversationByUID conversationByUID = new ConversationByUID();
        conversationByUID.conversationType = recieveMessage.type.equalsIgnoreCase("file") ? "multimedia" : "text";
        conversationByUID.type = recieveMessage.type;
        conversationByUID.conversationUid = recieveMessage.conversationUid;
        conversationByUID.toUserId = recieveMessage.toUserId;
        conversationByUID.customerName = recieveMessage.customerName;
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
        conversationByUID.caption = recieveMessage.caption;
        conversationByUID.timestamp = recieveMessage.timestamp;
        conversationByUID.receiver = recieveMessage.receiver;
        conversationByUID.pageId = recieveMessage.pageId;
        conversationByUID.pageName = recieveMessage.pageName;
        conversationByUID.tiggerevent = recieveMessage.tiggerevent;
        conversationByUID.voiceDuration = recieveMessage.voiceDuration;
        conversationByUID.isUpdateStatus = false;
        conversationByUID.isNotNewChat = true;
        conversationByUID.isRecieved = true;
        conversationByUID.isFailed = false;
        conversationByUID.tempChatId = recieveMessage.tempChatId;
        conversationByUID.id = recieveMessage.id;
        return conversationByUID;
    }
    public ConversationByUID getConversationFromReceiveMsg(ConversationByUID recieveMessage)
    {
        ConversationByUID conversationByUID = new ConversationByUID();
        conversationByUID.conversationType = recieveMessage.type.equalsIgnoreCase("file") ? "multimedia" : "text";
        conversationByUID.type = recieveMessage.type;
        conversationByUID.conversationUid = recieveMessage.conversationUid;
        conversationByUID.toUserId = recieveMessage.toUserId;
        conversationByUID.customerName = recieveMessage.customerName;
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
        conversationByUID.caption = recieveMessage.caption;
        conversationByUID.timestamp = recieveMessage.timestamp;
        conversationByUID.receiver = recieveMessage.receiver;
        conversationByUID.pageId = recieveMessage.pageId;
        conversationByUID.pageName = recieveMessage.pageName;
        conversationByUID.tiggerevent = recieveMessage.tiggerevent;
        conversationByUID.isUpdateStatus = false;
        conversationByUID.isNotNewChat = true;
        conversationByUID.voiceDuration = recieveMessage.voiceDuration;
        conversationByUID.tempChatId = recieveMessage.tempChatId;
        conversationByUID.id = recieveMessage.id;
        return conversationByUID;
    }
    public MultipartBody.Part  createMultipartFromContentUri(Context context, Uri uri, String partName) throws FileNotFoundException {
        // Step 1: Retrieve the content from the URI.
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        if (parcelFileDescriptor == null) {
            throw new FileNotFoundException("Could not open ParcelFileDescriptor for URI: " + uri);
        }
        // Step 2: Create a RequestBody from the content.
        File file = new File(uri.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse(context.getContentResolver().getType(uri)), file);

        // Step 3: Add the RequestBody to a MultipartBody.
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }


}

