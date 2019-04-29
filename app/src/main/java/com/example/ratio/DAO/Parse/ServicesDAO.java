package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Services;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.SERVICES;
import com.example.ratio.Utilities.DateTransform;
import com.example.ratio.Utilities.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ServicesDAO implements BaseDAO<Services> {
    private static final String TAG = "ServicesDAO";
    private DateTransform dateTransform = new DateTransform();
    private int result = 0;
    private int defaultLimit = 50;
    private boolean isSuccessful = false;
    private ParseObject parseObject = null;

    @Override
    public Services insert(Services objectEntity) {
        Log.d(TAG, "insert: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.SERVICES.toString());
        parseObject.put(SERVICES.NAME.toString(), objectEntity.getName());
        parseObject.put(SERVICES.OTHERS.toString(), objectEntity.isOthers());
        try {
            Log.d(TAG, "insert: Saving...");
            parseObject.save();
            Log.d(TAG, "insert: Done");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        Services services = new Services();
        services.setObjectId(parseObject.getObjectId());
        services.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        services.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        services.setName(parseObject.getString(SERVICES.NAME.toString()));
        services.setOthers(parseObject.getBoolean(SERVICES.OTHERS.toString()));
        return services;
    }

    @Override
    public int insertAll(List<Services> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> ids = new ArrayList<>();
        for(Services services : objectList){
            String id = insert(services).getObjectId();
            if(id != null){
                ids.add(id);
            }
        }

        Log.d(TAG, "insertAll: Result: " + String.valueOf(ids.size()));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));
        return ids.size();
    }

    @Override
    public Services get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.SERVICES.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Object retrieved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }
        Services services = new Services();
        services.setObjectId(parseObject.getObjectId());
        services.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        services.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        services.setName(parseObject.getString(SERVICES.NAME.toString()));
        services.setOthers(parseObject.getBoolean(SERVICES.OTHERS.toString()));
        return services;
    }

    @Override
    public List<Services> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<Services> services = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.SERVICES.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving services...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Services service = new Services();
                service.setObjectId(parseObject.getObjectId());
                service.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
                service.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
                service.setName(parseObject.getString(SERVICES.NAME.toString()));
                service.setOthers(parseObject.getBoolean(SERVICES.OTHERS.toString()));
                services.add(service);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return services;
    }


    @Override
    public Services update(Services newRecord) {
        Log.d(TAG, "update: Started...");
        isSuccessful = false;
        ParseObject parseObject = new ParseObject(PARSECLASS.SERVICES.toString());
        parseObject.put(SERVICES.NAME.toString(), newRecord.getName());
        parseObject.put(SERVICES.OTHERS.toString(), newRecord.isOthers());
        try {
            Log.d(TAG, "update: Saving record...");
            parseObject.save();
            isSuccessful = true;
            Log.d(TAG, "update: Save finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }

        Services services = new Services();
        services.setObjectId(parseObject.getObjectId());
        services.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        services.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        services.setName(parseObject.getString(SERVICES.NAME.toString()));
        services.setOthers(parseObject.getBoolean(SERVICES.OTHERS.toString()));
        return services;
    }

    @Override
    public int delete(Services object) {
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
    public int deleteAll(List<Services> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int res = 0;
        for(Services services : objectList){
            res += delete(services);
        }

        Log.d(TAG, "deleteAll: Result: " + String.valueOf(res));
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }
}
