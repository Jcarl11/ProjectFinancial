package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Recievables;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.RECIEVABLES;
import com.example.ratio.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecievablesDAO implements BaseDAO<Recievables> {
    private static final String TAG = "RecievablesDAO";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    int result = 0;
    int defaultLimit = 50;
    boolean isSuccessful = false;
    ParseObject parseObject = null;

    @Override
    public int insert(Recievables objectEntity) {
        Log.d(TAG, "insert: Started...");
        result = 0;
        ParseObject parseObject = new ParseObject(PARSECLASS.RECEIVABLES.toString());
        parseObject.put(RECIEVABLES.AMOUNT.toString(), objectEntity.getAmount());
        parseObject.put(RECIEVABLES.ATTACHMENTS.toString(), objectEntity.isAttachments());
        parseObject.put(RECIEVABLES.DESCRIPTION.toString(), objectEntity.getDescription());
        parseObject.put(RECIEVABLES.PARENT.toString(), objectEntity.getParent());
        parseObject.put(RECIEVABLES.TIMESTAMP.toString(), objectEntity.getTimestamp());
        try {
            Log.d(TAG, "insert: Saving...");
            parseObject.save();
            result = 1;
            Log.d(TAG, "insert: Done");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int insertAll(List<Recievables> objectList) {
        Log.d(TAG, "insertAll: Started...");
        int res = 0;
        for(Recievables recievables : objectList){
            res += insert(recievables);
        }

        Log.d(TAG, "insertAll: Result: " + String.valueOf(res));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }

    @Override
    public Recievables get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.RECEIVABLES.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Object retrieved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }
        Recievables recievables = new Recievables();
        recievables.setObjectId(parseObject.getObjectId());
        recievables.setCreatedAt(dateFormat.format(parseObject.getCreatedAt()));
        recievables.setUpdatedAt(dateFormat.format(parseObject.getUpdatedAt()));
        recievables.setAmount(parseObject.getString(RECIEVABLES.AMOUNT.toString()));
        recievables.setAttachments(parseObject.getBoolean(RECIEVABLES.ATTACHMENTS.toString()));
        recievables.setDescription(parseObject.getString(RECIEVABLES.DESCRIPTION.toString()));
        recievables.setParent(parseObject.getString(RECIEVABLES.PARENT.toString()));
        recievables.setTimestamp(parseObject.getString(RECIEVABLES.TIMESTAMP.toString()));
        return recievables;
    }

    @Override
    public List<Recievables> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<Recievables> recievables = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.RECEIVABLES.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving services...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Recievables recievable = new Recievables();
                recievable.setObjectId(parseObject.getObjectId());
                recievable.setCreatedAt(dateFormat.format(parseObject.getCreatedAt()));
                recievable.setUpdatedAt(dateFormat.format(parseObject.getUpdatedAt()));
                recievable.setAmount(parseObject.getString(RECIEVABLES.AMOUNT.toString()));
                recievable.setAttachments(parseObject.getBoolean(RECIEVABLES.ATTACHMENTS.toString()));
                recievable.setDescription(parseObject.getString(RECIEVABLES.DESCRIPTION.toString()));
                recievable.setParent(parseObject.getString(RECIEVABLES.PARENT.toString()));
                recievable.setTimestamp(parseObject.getString(RECIEVABLES.TIMESTAMP.toString()));
                recievables.add(recievable);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return recievables;
    }

    @Override
    public boolean update(Recievables newRecord) {
        Log.d(TAG, "update: Started...");
        isSuccessful = false;
        ParseObject parseObject = new ParseObject(PARSECLASS.RECEIVABLES.toString());
        parseObject.put(RECIEVABLES.AMOUNT.toString(), newRecord.getAmount());
        parseObject.put(RECIEVABLES.ATTACHMENTS.toString(), newRecord.isAttachments());
        parseObject.put(RECIEVABLES.DESCRIPTION.toString(), newRecord.getDescription());
        parseObject.put(RECIEVABLES.PARENT.toString(), newRecord.getParent());
        parseObject.put(RECIEVABLES.TIMESTAMP.toString(), newRecord.getTimestamp());
        try {
            Log.d(TAG, "update: Saving record...");
            parseObject.save();
            isSuccessful = true;
            Log.d(TAG, "update: Save finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }

        return isSuccessful;
    }

    @Override
    public int delete(Recievables object) {
        Log.d(TAG, "delete: Started...");
        result = 0;
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.SERVICES.toString());
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
    public int deleteAll(List<Recievables> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int res = 0;
        for(Recievables recievables : objectList){
            res += delete(recievables);
        }

        Log.d(TAG, "deleteAll: Result: " + String.valueOf(res));
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }
}
