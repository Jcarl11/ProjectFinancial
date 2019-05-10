package com.example.ratio;

import java.util.Random;

public class ImageAPI {
    private static final String TAG = "ImageAPI";
    private String BASE_URL = "https://picsum.photos/id/%s/%s/%s";
    private int defaultWidth = 300;
    private int defaultHeight = 175;
    private int lowerBound = 1014;
    private int upperBound = 1023;
    private Random randomizer = new Random();

    public String generateImage() {

        return String.format(BASE_URL, randomNumber(), defaultWidth, defaultHeight);
    }

    public String generateImage(int targetWidth, int targetHeight) {

        return String.format(BASE_URL, randomNumber(), targetWidth, targetHeight);
    }

    private int randomNumber() {
        return randomizer.nextInt(upperBound - lowerBound) + lowerBound;
    }
}
