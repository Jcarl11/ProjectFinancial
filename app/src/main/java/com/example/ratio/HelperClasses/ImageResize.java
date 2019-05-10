package com.example.ratio.HelperClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageResize {
    private static final String TAG = "ImageResize";
    private int targetWidth = 300;

    public File  resize(File original, int destinationWidth) {
        Bitmap bitmap = BitmapFactory.decodeFile(original.getAbsolutePath());
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        if(originalWidth > destinationWidth) {
            targetWidth = destinationWidth;
            int destinationHeight = originalHeight / (originalWidth / targetWidth);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Bitmap b2 = Bitmap.createScaledBitmap(bitmap, destinationWidth, destinationHeight, false);
            b2.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            File outputFile = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "temp.jpg");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                fileOutputStream.write(outputStream.toByteArray());
                fileOutputStream.close();
                return outputFile;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public File resize(String path, int destinationWidth) {
        return resize(new File(path), destinationWidth);
    }
}
