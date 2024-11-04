package com.arittek.o2chatsdk.adapters;

import android.content.Context;
import android.net.Uri;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.arittek.o2chatsdk.activities.SelectFilePreviewActivity;
import com.arittek.o2chatsdk.model.chat.selectedFilePreviewData;
import com.arittek.o2chatsdk.R;

import java.util.ArrayList;

public class ViewPagerAdapterUriLoad extends PagerAdapter
{

    Context context;
    ArrayList<selectedFilePreviewData> selectedFilePreviewDataArrayList;
    ImageView selectedImageView,ivFileImage,ivSmile,ivKeyboard;
    LinearLayout layoutOtherFiles;
    RelativeLayout sendMessage,menuClick;
    TextView txtFileName,txtSize;
    EditText edtMessage;
    // PDFView pdfView;
    View layout;

    public ViewPagerAdapterUriLoad(Context context, ArrayList<selectedFilePreviewData> receivedList, SelectFilePreviewActivity selectFilePreviewActivity) {
        this.context = context;
        selectedFilePreviewDataArrayList = receivedList;
    }

    @Override
    public int getCount() {
        return selectedFilePreviewDataArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.multipule_images_item, null);

        // pdfView = layout.findViewById(R.id.pdfView);
        ivSmile = layout.findViewById(R.id.ivSmile);
        ivKeyboard = layout.findViewById(R.id.ivKeyboard);
        selectedImageView = layout.findViewById(R.id.selectedImageView);
        menuClick = layout.findViewById(R.id.menuClick);
        ivFileImage = layout.findViewById(R.id.ivFileImage);
        txtFileName = layout.findViewById(R.id.txtFileName);
        txtSize = layout.findViewById(R.id.txtSize);
        layoutOtherFiles = layout.findViewById(R.id.layoutOtherFiles);
        sendMessage = layout.findViewById(R.id.sendMessage);
        edtMessage = layout.findViewById(R.id.edtMessage);

        selectedFilePreviewDataArrayList.get(position).setMessage(selectedFilePreviewDataArrayList.get(position).getMessage());
        if(selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("image/jpeg") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("image/png") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("image/jpg")){
            //pdfView.setVisibility(View.GONE);
            selectedImageView.setVisibility(View.VISIBLE);
            layoutOtherFiles.setVisibility(View.GONE);

            Uri myUri = Uri.parse(selectedFilePreviewDataArrayList.get(position).getFileUri());
            selectedImageView.setImageURI(myUri);

        } else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/msword") ||selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("docx") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/docx") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("doc") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/doc")|| selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
            // pdfView.setVisibility(View.GONE);
            selectedImageView.setVisibility(View.GONE);
            layoutOtherFiles.setVisibility(View.VISIBLE);
            ivFileImage.setImageResource(R.drawable.doc);
            txtFileName.setText(selectedFilePreviewDataArrayList.get(position).getFileName());
            txtSize.setText(selectedFilePreviewDataArrayList.get(position).getFileSize());

        } else if(selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/pdf")){
            layoutOtherFiles.setVisibility(View.GONE);
            selectedImageView.setVisibility(View.VISIBLE);

            selectedImageView.setImageDrawable(context.getDrawable(R.drawable.pdf));

//            Uri pdfUri = Uri.parse(selectedFilePreviewDataArrayList.get(position).getFileUri());
//            try {
//                ParcelFileDescriptor fileDescriptor = context.getContentResolver().openFileDescriptor(pdfUri, "r");
//                if (fileDescriptor != null) {
//                    PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
//                    PdfRenderer.Page page = pdfRenderer.openPage(0);
//                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
//                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//                    selectedImageView.setImageBitmap(bitmap);
//                    page.close();
//                    pdfRenderer.close();
//                    fileDescriptor.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            // pdfView.setVisibility(View.VISIBLE);

//            pdfView.fromUri(Uri.parse(selectedFilePreviewDataArrayList.get(position).getFileUri()))
//                    .defaultPage(0)
//                    .onPageChange(this)
//                    .enableAnnotationRendering(true)
//                    .onLoad(this)
//                    .scrollHandle(new DefaultScrollHandle(context))
//                    .spacing(10) // in dp
//                    .onPageError(this)
//                    .load();

        } else {
            // pdfView.setVisibility(View.GONE);
            selectedImageView.setVisibility(View.GONE);
            layoutOtherFiles.setVisibility(View.VISIBLE);
            txtFileName.setText(selectedFilePreviewDataArrayList.get(position).getFileName());
            txtSize.setText(selectedFilePreviewDataArrayList.get(position).getFileSize());
            if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("zip") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/zip")){
                ivFileImage.setImageResource(R.drawable.zip);
            }
            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("rar") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/rar")){
                ivFileImage.setImageResource(R.drawable.rar);
            }
            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("7z") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/7z")){
                ivFileImage.setImageResource(R.drawable.sevenz);
            }
            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("txt") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/txt")){
                ivFileImage.setImageResource(R.drawable.txt);
            }

            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("xls") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/xls")){
                ivFileImage.setImageResource(R.drawable.xls);
            }
            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("csv") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/csv")){
                ivFileImage.setImageResource(R.drawable.xls);
            }
        }

        ((ViewPager) container).addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

//
//    @Override
//    public void loadComplete(int nbPages) {
//
//    }
//
//    @Override
//    public void onPageError(int page, Throwable t) {
//
//    }
//
//    @Override
//    public void onPageChanged(int page, int pageCount) {
//
//    }
}




//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Parcelable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.viewpager.widget.PagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//
//import com.example.o2chatsdk.activities.SelectFilePreviewActivity;
//import com.example.o2chatsdk.model.chat.selectedFilePreviewData;
//
//
//import java.util.ArrayList;
//
//public class ViewPagerAdapterUriLoad extends PagerAdapter
//{
//
//    Context context;
//    ArrayList<com.example.o2chatsdk.model.chat.selectedFilePreviewData> selectedFilePreviewDataArrayList;
//    ImageView selectedImageView,ivFileImage,ivSmile,ivKeyboard;
//    LinearLayout layoutOtherFiles;
//    RelativeLayout sendMessage,menuClick;
//    TextView txtFileName,txtSize;
//    EditText edtMessage;
//    // PDFView pdfView;
//    View layout;
//
//    public ViewPagerAdapterUriLoad(Context context, ArrayList<selectedFilePreviewData> receivedList, SelectFilePreviewActivity selectFilePreviewActivity) {
//        this.context = context;
//        selectedFilePreviewDataArrayList = receivedList;
//    }
//
//    @Override
//    public int getCount() {
//        return selectedFilePreviewDataArrayList.size();
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, final int position) {
//        // TODO Auto-generated method stub
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layout = inflater.inflate(com.example.signalrtestandroid.R.layout.multipule_images_item, null);
//
//        // pdfView = layout.findViewById(R.id.pdfView);
//        ivSmile = layout.findViewById(com.example.signalrtestandroid.R.id.ivSmile);
//        ivKeyboard = layout.findViewById(com.example.signalrtestandroid.R.id.ivKeyboard);
//        selectedImageView = layout.findViewById(com.example.signalrtestandroid.R.id.selectedImageView);
//        menuClick = layout.findViewById(com.example.signalrtestandroid.R.id.menuClick);
//        ivFileImage = layout.findViewById(com.example.signalrtestandroid.R.id.ivFileImage);
//        txtFileName = layout.findViewById(com.example.signalrtestandroid.R.id.txtFileName);
//        txtSize = layout.findViewById(com.example.signalrtestandroid.R.id.txtSize);
//        layoutOtherFiles = layout.findViewById(com.example.signalrtestandroid.R.id.layoutOtherFiles);
//        sendMessage = layout.findViewById(com.example.signalrtestandroid.R.id.sendMessage);
//        edtMessage = layout.findViewById(com.example.signalrtestandroid.R.id.edtMessage);
//
//        selectedFilePreviewDataArrayList.get(position).setMessage(selectedFilePreviewDataArrayList.get(position).getMessage());
//        if(selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("image/jpeg") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("image/png") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("image/jpg")){
//            //pdfView.setVisibility(View.GONE);
//            selectedImageView.setVisibility(View.VISIBLE);
//            layoutOtherFiles.setVisibility(View.GONE);
//
//            Uri myUri = Uri.parse(selectedFilePreviewDataArrayList.get(position).getFileUri());
//            selectedImageView.setImageURI(myUri);
//
//        } else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/msword") ||selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("docx") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/docx") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("doc") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/doc")|| selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
//            // pdfView.setVisibility(View.GONE);
//            selectedImageView.setVisibility(View.GONE);
//            layoutOtherFiles.setVisibility(View.VISIBLE);
//            ivFileImage.setImageResource(com.example.signalrtestandroid.R.drawable.doc);
//            txtFileName.setText(selectedFilePreviewDataArrayList.get(position).getFileName());
//            txtSize.setText(selectedFilePreviewDataArrayList.get(position).getFileSize());
//
//        } else if(selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/pdf")){
//            layoutOtherFiles.setVisibility(View.GONE);
//            selectedImageView.setVisibility(View.VISIBLE);
//
//            selectedImageView.setImageDrawable(context.getDrawable(com.example.signalrtestandroid.R.drawable.pdf));
//
//        } else {
//            // pdfView.setVisibility(View.GONE);
//            selectedImageView.setVisibility(View.GONE);
//            layoutOtherFiles.setVisibility(View.VISIBLE);
//            txtFileName.setText(selectedFilePreviewDataArrayList.get(position).getFileName());
//            txtSize.setText(selectedFilePreviewDataArrayList.get(position).getFileSize());
//            if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("zip") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/zip")){
//                ivFileImage.setImageResource(com.example.signalrtestandroid.R.drawable.zip);
//            }
//            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("rar") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/rar")){
//                ivFileImage.setImageResource(com.example.signalrtestandroid.R.drawable.rar);
//            }
//            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("7z") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/7z")){
//                ivFileImage.setImageResource(com.example.signalrtestandroid.R.drawable.sevenz);
//            }
//            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("txt") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/txt")){
//                ivFileImage.setImageResource(com.example.signalrtestandroid.R.drawable.txt);
//            }
//
//            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("xls") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/xls")){
//                ivFileImage.setImageResource(com.example.signalrtestandroid.R.drawable.xls);
//            }
//            else if (selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("csv") || selectedFilePreviewDataArrayList.get(position).getFileType().equalsIgnoreCase("application/csv")){
//                ivFileImage.setImageResource(com.example.signalrtestandroid.R.drawable.xls);
//            }
//        }
//
//        ((ViewPager) container).addView(layout);
//        return layout;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
//        ((ViewPager) arg0).removeView((View) arg2);
//    }
//
//    @Override
//    public boolean isViewFromObject(View arg0, Object arg1) {
//        return arg0 == ((View) arg1);
//    }
//
//    @Override
//    public Parcelable saveState() {
//        return null;
//    }
//
////
////    @Override
////    public void loadComplete(int nbPages) {
////
////    }
//
////    @Override
////    public void onPageError(int page, Throwable t) {
////
////    }
////
////    @Override
////    public void onPageChanged(int page, int pageCount) {
////
////    }
//}