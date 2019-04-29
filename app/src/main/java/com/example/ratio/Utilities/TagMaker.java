package com.example.ratio.Utilities;

import com.example.ratio.Entities.Entity;

import org.json.JSONArray;

public class TagMaker {
    private static final String TAG = "TagMaker";
    public static final int COMMA = 1;
    public static final int SPACE = 2;
    private int delimiter = SPACE;

    public TagMaker() {}

    public TagMaker(int delimiter) {
        this.delimiter = delimiter;
    }

    public JSONArray createTags(Class<? extends Entity> source) {
        switch (delimiter) {
            case COMMA:

                break;
            case SPACE:
                break;
        }

        return null;
    }

}
