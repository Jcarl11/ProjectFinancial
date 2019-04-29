package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.STATUS;
import com.example.ratio.Utilities.DateTransform;
import com.example.ratio.Utilities.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class StatusDAO implements BaseDAO<Status> {
    private static final String TAG = "StatusDAO";
    private DateTransform dateTransform = new DateTransform();
    private int result = 0;
    private int defaultLimit = 50;
    private boolean isSuccessful = false;
    private ParseObject parseObject = null;

    @Override
    public Status insert(Status objectEntity) {
        Log.d(TAG, "insert: Started...");
        result = 0;
        ParseObject parseObject = new ParseObject(PARSECLASS.STATUS.toString());
        parseObject.put(STATUS.NAME.toString(), objectEntity.getName());
        try {
            Log.d(TAG, "insert: Saving...");
            parseObject.save();
            result = 1;
            Log.d(TAG, "insert: Save done");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        Status status = new Status();
        status.setObjectId(parseObject.getObjectId());
        status.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        status.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        status.setName(parseObject.getString(STATUS.NAME.toString()));

        return status;
    }

    @Override
    public int insertAll(List<Status> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> operations = new ArrayList<>();
        for(Status status : objectList){
            String id = insert(status).getObjectId();
            if(id != null) {
                operations.add(id);
            }
        }
        Log.d(TAG, "insertAll: Result: " + String.valueOf(operations.size()));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - operations.size()));
        return operations.size();
    }

    @Override
    public Status get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.STATUS.toString());
        try {
            Log.d(TAG, "get: Retriving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Object retrieved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }

        Status status = new Status();
        status.setObjectId(parseObject.getObjectId());
        status.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        status.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        status.setName(parseObject.getString(STATUS.NAME.toString()));

        return status;
    }

    @Override
    public List<Status> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<Status> statuses = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.STATUS.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving subcategories...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Status status = new Status();
                status.setObjectId(parseObject.getObjectId());
                status.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
                status.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
                status.setName(parseObject.getString(STATUS.NAME.toString()));
                statuses.add(status);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return statuses;
    }

    @Override
    public Status update(Status newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.STATUS.toString());
        parseObject.put(STATUS.NAME.toString(), newRecord.getName());
        try {
            Log.d(TAG, "update: Saving record...");
            parseObject.save();
            Log.d(TAG, "update: Save finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        Status status = new Status();
        status.setObjectId(parseObject.getObjectId());
        status.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        status.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        status.setName(parseObject.getString(STATUS.NAME.toString()));

        return status;
    }

    @Override
    public int delete(Status object) {
        Log.d(TAG, "delete: Started...");
        result = 0;
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.STATUS.toString());
        try {
            Log.d(TAG, "delete: Retrieving object...");
            parseObject = query.get(object.getObjectId());
            Log.d(TAG, "delete: Object Retrieved");
            Log.d(TAG, "delete: Deleting...");
            parseObject.delete();
            Log.d(TAG, "delete: Object deleted");
            result = 1;

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int deleteAll(List<Status> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int res = 0;
        for(Status status : objectList){
            res += delete(status);
        }

        Log.d(TAG, "deleteAll: Result: " + String.valueOf(res));
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }
}
