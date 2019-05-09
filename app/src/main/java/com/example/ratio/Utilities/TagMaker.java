package com.example.ratio.Utilities;

import com.example.ratio.Entities.Entity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagMaker {
    private static final String TAG = "TagMaker";

    public TagMaker() {}

    public JSONArray createTags(String source) {
        String[] split = source.split(",");
        List<String> arrayForm = new ArrayList<>();
        for(String value : split) {
            arrayForm.add(value);
            String[] tags = value.split("\\s+");
            for(String val : tags) {
                if(!arrayForm.contains(val)) {
                    arrayForm.add(val);
                }
            }
        }
        System.out.println(arrayForm.toString());
        return new JSONArray(arrayForm);
    }

}
