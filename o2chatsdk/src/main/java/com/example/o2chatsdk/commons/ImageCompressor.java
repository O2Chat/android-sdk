package com.example.o2chatsdk.commons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
public class ImageCompressor {

    public File compressImageFile(Context context, File file) throws IOException {
        // Decode the image file with memory-efficient options
        Bitmap bitmap = decodeFileWithSampling(file);

        if (bitmap == null) {
            throw new IOException("Failed to decode file: " + file.getAbsolutePath());
        }

        // Compress the Bitmap
        Bitmap compressedBitmap = compressBitmap(bitmap, 512, 512);

        // Save the compressed Bitmap to a new file
        File compressedFile = saveBitmapToFile(context, compressedBitmap);

        // Recycle the bitmap to free up memory
        bitmap.recycle();
        compressedBitmap.recycle();

        return compressedFile;
    }

    private Bitmap decodeFileWithSampling(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // Load image dimensions only
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // Calculate sample size to reduce memory usage
        int sampleSize = calculateInSampleSize(options, 1024, 1024); // Limit to 1024x1024 for efficiency
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false; // Decode actual bitmap

        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int sampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / sampleSize) >= reqHeight && (halfWidth / sampleSize) >= reqWidth) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    private Bitmap compressBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
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
        // Generate a unique file name to avoid overwriting
        String uniqueFileName = "IMG_" + System.currentTimeMillis() + ".jpg";

        File file = new File(context.getCacheDir(), uniqueFileName);

        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
        fos.close();

        return file;
    }
}
/*

public class ImageCompressor {

    public File compressImageFile(Context context, File file) throws IOException {
        // Decode the image file to a Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        // Compress the Bitmap
        Bitmap compressedBitmap = compressBitmap(bitmap, 512, 512, 80);

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
}*/
