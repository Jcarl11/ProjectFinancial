package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.RECIEVABLES;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class RecievablesDAO implements BaseDAO<Receivables> {
    private static final String TAG = "RecievablesDAO";
    private DateTransform dateTransform = new DateTransform();
    private int result = 0;
    private int defaultLimit = 50;
    private boolean isSuccessful = false;
    private ParseObject parseObject = null;

    @Override
    public Receivables insert(Receivables objectEntity) {
        Log.d(TAG, "insert: Started...");
        result = 0;
        ParseObject parseObject = new ParseObject(PARSECLASS.RECEIVABLES.toString());
        parseObject.put(RECIEVABLES.AMOUNT.toString(), objectEntity.getAmount());
        parseObject.put(RECIEVABLES.ATTACHMENTS.toString(), objectEntity.isAttachments());
        parseObject.put(RECIEVABLES.DESCRIPTION.toString(), objectEntity.getDescription());
        parseObject.put(RECIEVABLES.PARENT.toString(), objectEntity.getParent());
        parseObject.put(RECIEVABLES.TIMESTAMP.toString(), dateTransform.toISO8601Date(objectEntity.getTimestamp()));
        try {
            Log.d(TAG, "insert: Saving...");
            parseObject.save();
            result = 1;
            Log.d(TAG, "insert: Done");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        Receivables receivables = new Receivables();
        receivables.setObjectId(parseObject.getObjectId());
        receivables.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
        receivables.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
        receivables.setAmount(parseObject.getString(RECIEVABLES.AMOUNT.toString()));
        receivables.setAttachments(parseObject.getBoolean(RECIEVABLES.ATTACHMENTS.toString()));
        receivables.setDescription(parseObject.getString(RECIEVABLES.DESCRIPTION.toString()));
        receivables.setParent(parseObject.getString(RECIEVABLES.PARENT.toString()));
        receivables.setTimestamp(dateTransform.toDateString(parseObject.getDate(RECIEVABLES.TIMESTAMP.toString())));
        return receivables;
    }

    @Override
    public int insertAll(List<Receivables> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> ids = new ArrayList<>();
        for(Receivables receivables : objectList){
            String id = insert(receivables).getObjectId();
            if(id != null)
                ids.add(id);
        }

        Log.d(TAG, "insertAll: Result: " + String.valueOf(ids.size()));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));
        return ids.size();
    }

    @Override
    public Receivables get(String objectId) {
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
        Receivables receivables = new Receivables();
        receivables.setObjectId(parseObject.getObjectId());
        receivables.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
        receivables.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
        receivables.setAmount(parseObject.getString(RECIEVABLES.AMOUNT.toString()));
        receivables.setAttachments(parseObject.getBoolean(RECIEVABLES.ATTACHMENTS.toString()));
        receivables.setDescription(parseObject.getString(RECIEVABLES.DESCRIPTION.toString()));
        receivables.setParent(parseObject.getString(RECIEVABLES.PARENT.toString()));
        receivables.setTimestamp(dateTransform.toDateString(parseObject.getDate(RECIEVABLES.TIMESTAMP.toString())));
        return receivables;
    }

    @Override
    public List<Receivables> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<Receivables> recievables = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.RECEIVABLES.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving services...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Receivables receivables = new Receivables();
                receivables.setObjectId(parseObject.getObjectId());
                receivables.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                receivables.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                receivables.setAmount(parseObject.getString(RECIEVABLES.AMOUNT.toString()));
                receivables.setAttachments(parseObject.getBoolean(RECIEVABLES.ATTACHMENTS.toString()));
                receivables.setDescription(parseObject.getString(RECIEVABLES.DESCRIPTION.toString()));
                receivables.setParent(parseObject.getString(RECIEVABLES.PARENT.toString()));
                receivables.setTimestamp(dateTransform.toDateString(parseObject.getDate(RECIEVABLES.TIMESTAMP.toString())));
                recievables.add(receivables);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return recievables;
    }

    @Override
    public Receivables update(Receivables newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.RECEIVABLES.toString());
        parseObject.put(RECIEVABLES.AMOUNT.toString(), newRecord.getAmount());
        parseObject.put(RECIEVABLES.ATTACHMENTS.toString(), newRecord.isAttachments());
        parseObject.put(RECIEVABLES.DESCRIPTION.toString(), newRecord.getDescription());
        parseObject.put(RECIEVABLES.PARENT.toString(), newRecord.getParent());
        parseObject.put(RECIEVABLES.TIMESTAMP.toString(), newRecord.getTimestamp());
        try {
            Log.d(TAG, "update: Saving record...");
            parseObject.save();
            Log.d(TAG, "update: Save finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        Receivables receivables = new Receivables();
        receivables.setObjectId(parseObject.getObjectId());
        receivables.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
        receivables.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
        receivables.setAmount(parseObject.getString(RECIEVABLES.AMOUNT.toString()));
        receivables.setAttachments(parseObject.getBoolean(RECIEVABLES.ATTACHMENTS.toString()));
        receivables.setDescription(parseObject.getString(RECIEVABLES.DESCRIPTION.toString()));
        receivables.setParent(parseObject.getString(RECIEVABLES.PARENT.toString()));
        receivables.setTimestamp(dateTransform.toDateString(parseObject.getDate(RECIEVABLES.TIMESTAMP.toString())));
        return receivables;
    }

    @Override
    public int delete(Receivables object) {
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
    public int deleteAll(List<Receivables> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int res = 0;
        for(Receivables receivables : objectList){
            res += delete(receivables);
        }

        Log.d(TAG, "deleteAll: Result: " + String.valueOf(res));
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }
}
