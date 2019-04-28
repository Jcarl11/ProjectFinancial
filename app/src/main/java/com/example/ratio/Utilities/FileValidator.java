package com.example.ratio.Utilities;

import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class FileValidator {
    private static final String TAG = "FileValidator";


    public boolean isImage(String path) {
        String extension = FilenameUtils.getExtension(path);
        //Log.d(TAG, "isFile: Image extension: " + extension);
        if(extension.equalsIgnoreCase("png")) {
            return true;
        } else if(extension.equalsIgnoreCase("jpg")) {
            return true;
        } else if(extension.equalsIgnoreCase("gif")) {
            return true;
        } else if(extension.equalsIgnoreCase("jpeg")) {
            return true;
        }
        //Log.d(TAG, "isFile: Image format not valid");
        return false;
    }

    public boolean isImage(File source) {
        String toPath = source.getAbsolutePath();
        Log.d(TAG, "isFile: Path: " + toPath);
        return isImage(toPath);
    }

    public boolean isFile(String path) {
        String extension = FilenameUtils.getExtension(path);
        Log.d(TAG, "isFile: File extension: " + extension);
        if(extension.equalsIgnoreCase("pdf")) {
            return true;
        } //add more extensions if necessary

        Log.d(TAG, "isFile: File format not valid");
        return false;
    }

    public boolean isFile(File source) {
        String path = source.getAbsolutePath();
        Log.d(TAG, "isFile: Path: " + path);
        return isFile(path);
    }
}
