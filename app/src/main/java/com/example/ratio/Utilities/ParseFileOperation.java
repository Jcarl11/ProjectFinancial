package com.example.ratio.Utilities;

import android.graphics.Bitmap;
import android.util.Log;

import com.parse.ParseFile;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class ParseFileOperation {
    private static final String TAG = "ParseFileOperation";
    FileValidator fileValidator = new FileValidator();

    public ParseFile fromByte(String filename, byte[] source) {
        return fileValidator.isImage(filename) == true ? new ParseFile(filename, source) : null;
    }

    public ParseFile fromFile(File source) {

        byte[] bytesArray = new byte[(int) source.length()];

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileInputStream.read(bytesArray);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "fromFile: FileNotFoundException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "fromFile: IOException: " + e.getMessage());
            e.printStackTrace();
        }
        return fromByte(source.getName(), bytesArray);
    }



}
