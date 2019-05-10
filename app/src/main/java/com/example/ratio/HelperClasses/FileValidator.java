package com.example.ratio.HelperClasses;

import android.util.Log;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileValidator {
    private static final String TAG = "FileValidator";
    private static final List<String> ACCEPTABLE_IMAGE = new ArrayList<>(Arrays.asList("jpg","gif", "jpeg", "png"));
    private static final List<String> ACCEPTABLE_FILE = new ArrayList<>(Arrays.asList("pdf"));

    public boolean isImage(String path) {
        String extension = FilenameUtils.getExtension(path);

        return ACCEPTABLE_IMAGE.contains(extension.toLowerCase()) == true ? true : false;
    }

    public boolean isImage(File source) {
        String toPath = source.getAbsolutePath();
        Log.d(TAG, "isFile: Path: " + toPath);
        return isImage(toPath);
    }

    public boolean isFile(String path) {
        String extension = FilenameUtils.getExtension(path);
        Log.d(TAG, "isFile: File extension: " + extension);

        return ACCEPTABLE_FILE.contains(extension.toLowerCase()) == true ? true : false;
    }

    public boolean isFile(File source) {
        String path = source.getAbsolutePath();
        Log.d(TAG, "isFile: Path: " + path);
        return isFile(path);
    }
}
