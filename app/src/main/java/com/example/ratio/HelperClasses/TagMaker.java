package com.example.ratio.HelperClasses;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
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

    public JSONArray fromList(List<String> source) {
        JSONArray jsonArray = new JSONArray();
        for (String values : source) {
            jsonArray.put(values);
        }
        return jsonArray;
    }

    public ArrayList<String> toArray(JSONArray jsonArray) {
        ArrayList<String> values = new ArrayList<>();
        for (int x = 0 ; x < jsonArray.length() ; x++) {
            try {
                values.add(jsonArray.getString(x));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "toArray: Exception thrown: " + e.getMessage());
            }
        }
        return values;
    }

    public ArrayList<String> tagSplit(String source) {
        String[] tags = !TextUtils.isEmpty(source) ? source.split("\\s+") : null;
        if (tags == null) {
            return new ArrayList<String>();
        }
        ArrayList<String> tagsList = new ArrayList<>();
        for(String values : tags) {
            tagsList.add(values.toUpperCase());
        }
        return tagsList;
    }

}
