package com.example.ratio.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class ImageCompressor {
    private static final String TAG = "ImageCompressor";
    private Context context;
    private static ImageCompressor instance = null;
    private ImageCompressor(){}
    public static ImageCompressor getInstance(){
        if(instance == null) {
            instance = new ImageCompressor();
        }
        return instance;
    }

    public File compressToFile(File original) {
        Log.d(TAG, "compressToFile: compressing...");
        Log.d(TAG, "compressToFile: Original Size: " + String.valueOf(original.length() / 1024) + "kb");
        File compressedImage = null;
        try {
            compressedImage = new Compressor(context).compressToFile(original);
            Log.d(TAG, "compressToFile: Compressed size: " + String.valueOf(compressedImage.length() / 1024) + "kb");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "compressToFile: Exception thrown: " + e.getMessage());
        }
        return compressedImage;
    }

    public File compressToFile(String imagePath){
        return compressToFile(new File(imagePath));
    }

    public Bitmap compressToBitmap(File original) {
        Log.d(TAG, "compressToBitmap: Started...");
        Log.d(TAG, "compressToBitmap: Orginal Size: "  + String.valueOf(original.length() / 1024) + "kb");
        Bitmap bitmap = null;
        try {
            bitmap = new Compressor(context).compressToBitmap(original);
            Log.d(TAG, "compressToBitmap: Compressed size: " + String.valueOf(bitmap.getByteCount() / 1024) + "kb" );
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "compressToBitmap: Exception thrown: " + e.getMessage());
        }
        return bitmap;
    }

    public Bitmap compressToBitmap(String imagePath) {
        return compressToBitmap(new File(imagePath));
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
