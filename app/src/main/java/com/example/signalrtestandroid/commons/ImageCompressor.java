package com.example.signalrtestandroid.commons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ImageCompressor {

    public File compressImageFile(Context context, File file) throws IOException {
        // Decode the image file to a Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        // Compress the Bitmap
        Bitmap compressedBitmap = compressBitmap(bitmap, 512, 512, 90);

        // Save the compressed Bitmap to a new file
        File compressedFile = saveBitmapToFile(context, compressedBitmap);

        return compressedFile;
    }

    private Bitmap compressBitmap(Bitmap bitmap, int maxWidth, int maxHeight, int quality) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) maxWidth) / width;
        float scaleHeight = ((float) maxHeight) / height;

        float scaleFactor = Math.min(scaleWidth, scaleHeight);

        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
    }

    private File saveBitmapToFile(Context context, Bitmap bitmap) throws IOException {
        // Create a file to save the compressed image

        String uniqueFileName = "compressed_image_" + UUID.randomUUID().toString() + ".jpg";

        File file = new File(context.getCacheDir(), uniqueFileName);

        // Save the bitmap to the file
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
        fos.close();

        return file;
    }
}