package com.arittek.signalrtestandroid.commons;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.arittek.signalrtestandroid.R;

import java.io.File;
import java.io.InputStream;

public class Utils {

    private static final int maxFileSize = 2621440;
    private static final int maxImageFileSize = 1000000;

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void createDialogLogout(final Context context, String textData, Common common) {

        Dialog newUserDialog = new Dialog(context);
        View newUserView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        newUserView = inflater.inflate(R.layout.log_out_dialoge_chat_app, null);
        newUserDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newUserView.setBackground(ContextCompat.getDrawable(context, R.drawable.round_border_rectangle));
        newUserDialog.setContentView(newUserView);
        newUserDialog.setCancelable(false);
        if (!((Activity) context).isFinishing()) {
            newUserDialog.show();
        }
        Button okbutton = newUserView.findViewById(R.id.ok_btn);
        TextView text = newUserView.findViewById(R.id.text);
        text.setText(textData);
        okbutton.setText("OK");

        okbutton.setOnClickListener(view -> {
            if (common!=null){
                common.deleteChatLocalDB(context);
            }

            ((Activity) context).finish();
            newUserDialog.dismiss();
        });
    }

    public static void customeTost(String msg, Activity context, View view) {
        if(view!= null){
            View layout = context.getLayoutInflater().inflate(R.layout.custom_toast,(ViewGroup) view.findViewById(R.id.custom_toast_container));
            TextView text = layout.findViewById(R.id.MyText);
            text.setText(msg);
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    public static boolean MaxSizeImage(int filesize) {
        boolean temp = false;
        // 2.5 mb
        temp = filesize > maxFileSize;
        return temp;
    }

    public static boolean MaxSizeImageCheck(int filesize) {
        boolean temp = false;
        // 1 mb
        temp = filesize > maxImageFileSize;
        return temp;
    }

    public static boolean isCheckFileSize(Uri uri, Context context){

        Boolean isFileSizeExceed= false;
        int dataSize=0;
        String scheme = uri.getScheme();
        if(scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            try {
                InputStream fileInputStream = context.getContentResolver().openInputStream(uri);
                dataSize = fileInputStream.available();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isFileSizeExceed= !Utils.MaxSizeImage(dataSize);
        }
        else if(scheme.equals(ContentResolver.SCHEME_FILE))
        {
            File f=null;
            String path = uri.getPath();
            try {
                f = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isFileSizeExceed = !Utils.MaxSizeImage(dataSize);
        }
        return isFileSizeExceed;
    }

    public static boolean isFileLessThan2MB(File file) {
        int maxFileSize = 2 * 1024 * 1024;
        Long l = file.length();
        String fileSize = l.toString();
        int finalFileSize = Integer.parseInt(fileSize);
        return finalFileSize <= maxFileSize;
    }

    public static boolean isCheckImageSize(Uri uri,Context context){

        Boolean isFileSizeExceed= false;
        int dataSize=0;
        String scheme = uri.getScheme();
        if(scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            try {
                InputStream fileInputStream = context.getContentResolver().openInputStream(uri);
                dataSize = fileInputStream.available();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isFileSizeExceed= !Utils.MaxSizeImageCheck(dataSize);
        }
        else if(scheme.equals(ContentResolver.SCHEME_FILE))
        {
            File f=null;
            String path = uri.getPath();
            try {
                f = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isFileSizeExceed = !Utils.MaxSizeImageCheck(dataSize);
        }
        return isFileSizeExceed;
    }


    public static void setupEdgeToEdge(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.getWindow().setDecorFitsSystemWindows(false);
        }

        WindowInsetsControllerCompat controller =
                ViewCompat.getWindowInsetsController(activity.getWindow().getDecorView());

        if (controller != null) {
            controller.setAppearanceLightStatusBars(true);
            controller.setAppearanceLightNavigationBars(true);
        }
    }


/*    public static void setupEdgeToEdge(Activity activity) {
        // Step 1: Enable edge-to-edge drawing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.getWindow().setDecorFitsSystemWindows(false);
        }

        // Step 2: Get the root view. Using android.R.id.content is a common way
        // to get the root of the Activity's content view.
        View rootView = activity.findViewById(android.R.id.content);
        if (rootView == null) {
            // Fallback if android.R.id.content is not suitable or found,
            // though it usually is for standard Activities.
            // You might need a specific root view ID from your layout XML.
            // Example: rootView = activity.findViewById(R.id.your_activity_root_layout);
            return; // Cannot proceed if no root view found
        }

        // Step 3: Apply window insets as padding to the root view
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply padding directly to the root view.
            // This assumes the root view should handle the entire system bar area.
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);

            // You might return windowInsets here if children views also need to consume parts.
            // For simple full-screen padding, returning CONSUMED is also an option:
            // return WindowInsetsCompat.CONSUMED;
            return windowInsets;
        });

        // Step 4: Adjust system bar icon appearance
        WindowInsetsControllerCompat insetsController = ViewCompat.getWindowInsetsController(activity.getWindow().getDecorView());
        if (insetsController != null) {
            insetsController.setAppearanceLightStatusBars(true); // Assuming light status bar background
            insetsController.setAppearanceLightNavigationBars(true); // Assuming light navigation bar background
            // Set to false for dark background
        }
    }*/

}
