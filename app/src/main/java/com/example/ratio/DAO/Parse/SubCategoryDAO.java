package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT_TYPE_SUBCATEGORY;
import com.example.ratio.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class SubCategoryDAO implements BaseDAO<Subcategory> {
    private static final String TAG = "SubCategoryDAO";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    int result = 0;
    boolean isSuccessful = false;
    int defaultLimit = 50;
    ParseObject parseObject = null;
    @Override
    public int insert(Subcategory objectEntity) {
        Log.d(TAG, "insert: started");
        result = 0;
        ParseObject insert = new ParseObject(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString());
        insert.put(PROJECT_TYPE_SUBCATEGORY.PARENT.toString(), objectEntity.getParent());
        insert.put(PROJECT_TYPE_SUBCATEGORY.NAME.toString(), objectEntity.getName());
        try {
            Log.d(TAG, "insert: Saving record...");
            insert.save();
            result = 1;
            Log.d(TAG, "insert: Save finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int insertAll(List<Subcategory> objectList) {
        Log.d(TAG, "insertAll: started");
        Log.d(TAG, "insertAll: objectList size: " + String.valueOf(objectList.size()));
        int res = 0;
        for(Subcategory subcategory : objectList){
            res += insert(subcategory);
        }
        Log.d(TAG, "insertAll: Result: " + String.valueOf(res));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }

    @Override
    public Subcategory get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Object retrieved: " + parseObject.getObjectId());

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }
        Subcategory subcategory = new Subcategory();
        subcategory.setObjectId(parseObject.getObjectId());
        subcategory.setCreatedAt(dateFormat.format(parseObject.getCreatedAt()));
        subcategory.setUpdatedAt(dateFormat.format(parseObject.getUpdatedAt()));
        subcategory.setName(parseObject.getString(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString()));
        subcategory.setParent(parseObject.getString(PROJECT_TYPE_SUBCATEGORY.PARENT.toString()));
        subcategory.setOthers(parseObject.getBoolean(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString()));
        return subcategory;
    }

    @Override
    public List<Subcategory> getBulk(@Nullable  String sqlCommand) { // this parameter can be nullable, Applicable only to Sqlite, Can also be used for limiting the number of result
        Log.d(TAG, "getBulk: Started");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<Subcategory> subcategories = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving subcategories...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject subcategory : parseObjects){
                Subcategory categories = new Subcategory();
                categories.setObjectId(subcategory.getObjectId());
                categories.setCreatedAt(dateFormat.format(subcategory.getCreatedAt()));
                categories.setUpdatedAt(dateFormat.format(subcategory.getUpdatedAt()));
                categories.setName(subcategory.getString(PROJECT_TYPE_SUBCATEGORY.NAME.toString()));
                categories.setParent(subcategory.getString(PROJECT_TYPE_SUBCATEGORY.PARENT.toString()));
                categories.setOthers(subcategory.getBoolean(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString()));
                subcategories.add(categories);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return subcategories;
    }

    @Override
    public boolean update(Subcategory newRecord) {
        Log.d(TAG, "update: Started...");
        isSuccessful = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString());
        try {
            Log.d(TAG, "update: Retrieving object");
            ParseObject parseObject = query.get(newRecord.getObjectId());
            parseObject.put(PROJECT_TYPE_SUBCATEGORY.NAME.toString(), newRecord.getName());
            parseObject.put(PROJECT_TYPE_SUBCATEGORY.PARENT.toString(), newRecord.getParent());
            parseObject.put(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString(), newRecord.isOthers());
            Log.d(TAG, "update: Updating record...");
            parseObject.save();
            isSuccessful = true;
            Log.d(TAG, "update: Update finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        return isSuccessful;
    }

    @Override
    public int delete(Subcategory object) {
        Log.d(TAG, "delete: Started...");
        result = -1;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString());
        try {
            Log.d(TAG, "delete: Retrieving object...");
            ParseObject parseObject = query.get(object.getObjectId());
            Log.d(TAG, "delete: Object retrieved");
            Log.d(TAG, "delete: Deleting object...");
            parseObject.delete();
            result = 1;
            Log.d(TAG, "delete: Object deleted succesfully");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int deleteAll(List<Subcategory> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int res = 0;
        for(Subcategory subcategory : objectList){
            res += delete(subcategory);
        }
        Log.d(TAG, "deleteAll: Result: " + String.valueOf(res));
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - res));

        return res;
    }
}
