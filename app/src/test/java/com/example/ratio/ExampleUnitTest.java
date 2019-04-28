package com.example.ratio;

import android.util.Log;

import com.example.ratio.Utilities.DateTransform;
import com.example.ratio.Utilities.FileValidator;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = "ExampleUnitTest";
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void asd() {
        String extension = FilenameUtils.getExtension("joey.exe");
        assertEquals("exe", extension);
    }

    @Test
    public void fileValidatorTest(){
        FileValidator fileValidator = new FileValidator();
        boolean result = fileValidator.isImage("joey.jpeg");
        assertEquals(true, result);
    }
}