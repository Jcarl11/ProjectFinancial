package com.example.ratio.HelperClasses;

import android.util.Log;
import com.parse.ParseFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ParseFileOperation {
    private static final String TAG = "ParseFileOperation";
    private FileValidator fileValidator = new FileValidator();

    public ParseFile fromByte(String filename, byte[] source) {
        return fileValidator.isImage(filename) || fileValidator.isFile(filename)
                ? new ParseFile(filename, source) : null;
    }

    public ParseFile fromFile(File source) {

        byte[] bytesArray = new byte[(int) source.length()];

        FileInputStream fileInputStream;
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
