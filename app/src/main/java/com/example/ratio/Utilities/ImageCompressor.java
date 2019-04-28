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

    public ImageCompressor(Context context) {
        this.context = context;
    }

    public File compressToFile(File original) {
        Log.d(TAG, "compressToFile: compressing...");
        File compressedImage = null;
        try {
            compressedImage = new Compressor(context).compressToFile(original);
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
        Bitmap bitmap = null;
        try {
            bitmap = new Compressor(context).compressToBitmap(original);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "compressToBitmap: Exception thrown: " + e.getMessage());
        }
        return bitmap;
    }

    public Bitmap compressToBitmap(String imagePath) {
        return compressToBitmap(new File(imagePath));
    }
}
