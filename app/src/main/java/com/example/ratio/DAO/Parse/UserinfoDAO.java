package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.DAO.GetFromPosition;
import com.example.ratio.Entities.Userinfo;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.USERINFO;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class UserinfoDAO implements BaseDAO<Userinfo>, GetFromParent<Userinfo>, GetFromPosition<Userinfo> {
    private static final String TAG = "UserinfoDAO";
    private DateTransform dateTransform = new DateTransform();
    private int defaultLimit = 50;

    @Override
    public Userinfo insert(Userinfo objectEntity) {
        Log.d(TAG, "insert: Started...");
        Userinfo userinfo = new Userinfo();
        ParseObject parseObject = new ParseObject(PARSECLASS.USERINFO.toString());
        parseObject.put(USERINFO.PARENT.toString(), objectEntity.getParent());
        parseObject.put(USERINFO.FULLNAME.toString(), objectEntity.getFullname());
        parseObject.put(USERINFO.POSITION.toString(), objectEntity.getPosition());
        parseObject.put(USERINFO.STATUS.toString(), objectEntity.getStatus());
        parseObject.put(USERINFO.VERIFIED.toString(), objectEntity.isVerified());
        parseObject.put(USERINFO.EMAIL.toString(), objectEntity.getEmail());
        parseObject.put(USERINFO.USERNAME.toString(), objectEntity.getUsername());
        try {
            Log.d(TAG, "insert: Saving object...");
            parseObject.save();
            Log.d(TAG, "insert: Object Saved");
            userinfo.setObjectId(parseObject.getObjectId());
            userinfo.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
            userinfo.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
            return userinfo;
        } catch (ParseException e) {
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insertAll(List<Userinfo> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> userinfos = new ArrayList<>();
        for (Userinfo userinfo : objectList) {
            String id = insert(userinfo).getObjectId();
            if(id != null) {
                userinfos.add(id);
            }
        }
        Log.d(TAG, "insertAll: Result: " + userinfos.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - userinfos.size()));
        return userinfos.size();
    }

    @Override
    public Userinfo get(String objectId) {
        Log.d(TAG, "get: Started...");
        Userinfo userinfo = new Userinfo();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.USERINFO.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
            ParseObject parseObject = query.get(objectId);
            Log.d(TAG, "get: Object retrieved");
            userinfo.setObjectId(parseObject.getObjectId());
            userinfo.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
            userinfo.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
            userinfo.setEmail(parseObject.getString(USERINFO.EMAIL.toString()));
            userinfo.setFullname(parseObject.getString(USERINFO.FULLNAME.toString()));
            userinfo.setParent(parseObject.getString(USERINFO.PARENT.toString()));
            userinfo.setPosition(parseObject.getString(USERINFO.POSITION.toString()));
            userinfo.setStatus(parseObject.getString(USERINFO.STATUS.toString()));
            userinfo.setVerified(parseObject.getBoolean(USERINFO.VERIFIED.toString()));
            userinfo.setUsername(parseObject.getString(USERINFO.USERNAME.toString()));
            return userinfo;
        } catch (ParseException e) {
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Userinfo> getBulk(@Nullable String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        List<Userinfo> userinfos = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.USERINFO.toString());
        query.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving objects...");
            List<ParseObject> parseObjects = query.find();
            Log.d(TAG, "getBulk: Objects retrieved: " + parseObjects.size());
            for (ParseObject parseObject : parseObjects) {
                Userinfo userinfo = new Userinfo();
                userinfo.setObjectId(parseObject.getObjectId());
                userinfo.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                userinfo.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                userinfo.setEmail(parseObject.getString(USERINFO.EMAIL.toString()));
                userinfo.setFullname(parseObject.getString(USERINFO.FULLNAME.toString()));
                userinfo.setParent(parseObject.getString(USERINFO.PARENT.toString()));
                userinfo.setPosition(parseObject.getString(USERINFO.POSITION.toString()));
                userinfo.setStatus(parseObject.getString(USERINFO.STATUS.toString()));
                userinfo.setVerified(parseObject.getBoolean(USERINFO.VERIFIED.toString()));
                userinfo.setUsername(parseObject.getString(USERINFO.USERNAME.toString()));
                userinfos.add(userinfo);
            }
        } catch (ParseException e) {
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return userinfos;
    }

    @Override
    public Userinfo update(Userinfo newRecord) {
        Log.d(TAG, "update: Started...");
        Userinfo userinfo = new Userinfo();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.USERINFO.toString());
        try {
            Log.d(TAG, "update: Retrieving object...");
            ParseObject parseObject = query.get(newRecord.getObjectId());
            Log.d(TAG, "update: Object retrieved: " + parseObject.getObjectId());
            if(!parseObject.getString(USERINFO.FULLNAME.toString()).equals(newRecord.getFullname()))
                parseObject.put(USERINFO.FULLNAME.toString(), newRecord.getFullname());
            if(!parseObject.getString(USERINFO.POSITION.toString()).equals(newRecord.getPosition()))
                parseObject.put(USERINFO.POSITION.toString(), newRecord.getPosition());
            if(!parseObject.getString(USERINFO.STATUS.toString()).equals(newRecord.getStatus()))
                parseObject.put(USERINFO.STATUS.toString(), newRecord.getStatus());
            if(parseObject.getBoolean(USERINFO.VERIFIED.toString()) != newRecord.isVerified())
                parseObject.put(USERINFO.VERIFIED.toString(), newRecord.isVerified());
            if(!parseObject.getString(USERINFO.EMAIL.toString()).equals(newRecord.getEmail()))
                parseObject.put(USERINFO.EMAIL.toString(), newRecord.getEmail());
            if(!parseObject.getString(USERINFO.USERNAME.toString()).equals(newRecord.getUsername()))
                parseObject.put(USERINFO.USERNAME.toString(), newRecord.getUsername());

            parseObject.save();

            userinfo.setObjectId(parseObject.getObjectId());
            userinfo.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
            userinfo.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
            userinfo.setEmail(parseObject.getString(USERINFO.EMAIL.toString()));
            userinfo.setFullname(parseObject.getString(USERINFO.FULLNAME.toString()));
            userinfo.setParent(parseObject.getString(USERINFO.PARENT.toString()));
            userinfo.setPosition(parseObject.getString(USERINFO.POSITION.toString()));
            userinfo.setStatus(parseObject.getString(USERINFO.STATUS.toString()));
            userinfo.setVerified(parseObject.getBoolean(USERINFO.VERIFIED.toString()));
        } catch (ParseException e) {
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return userinfo;
    }

    @Override
    public int delete(Userinfo object) {
        Log.d(TAG, "delete: Started..");
        int result = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.USERINFO.toString());
        try {
            Log.d(TAG, "delete: Retrieving object...");
            ParseObject parseObject = query.get(object.getObjectId());
            Log.d(TAG, "delete: Object retrieved");
            Log.d(TAG, "delete: Deleting..");
            parseObject.put(USERINFO.STATUS.toString(), Constant.DELETED);
            parseObject.put(USERINFO.POSITION.toString(), Constant.PENDING);
            parseObject.save();
            result = 1;
            Log.d(TAG, "delete: Deleted");
        } catch (ParseException e) {
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteAll(List<Userinfo> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for (Userinfo userinfo : objectList) {
            result += delete(userinfo);
        }
        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));
        return result;
    }

    @Override
    public List<Userinfo> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started...");
        List<Userinfo> userinfos = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.USERINFO.toString());
        query.whereEqualTo(USERINFO.PARENT.toString(), parentID);
        try {
            Log.d(TAG, "getBulk: Retrieving objects...");
            List<ParseObject> parseObjects = query.find();
            Log.d(TAG, "getBulk: Objects retrieved: " + parseObjects.size());
            for (ParseObject parseObject : parseObjects) {
                Userinfo userinfo = new Userinfo();
                userinfo.setObjectId(parseObject.getObjectId());
                userinfo.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                userinfo.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                userinfo.setEmail(parseObject.getString(USERINFO.EMAIL.toString()));
                userinfo.setFullname(parseObject.getString(USERINFO.FULLNAME.toString()));
                userinfo.setParent(parseObject.getString(USERINFO.PARENT.toString()));
                userinfo.setPosition(parseObject.getString(USERINFO.POSITION.toString()));
                userinfo.setStatus(parseObject.getString(USERINFO.STATUS.toString()));
                userinfo.setVerified(parseObject.getBoolean(USERINFO.VERIFIED.toString()));
                userinfo.setUsername(parseObject.getString(USERINFO.USERNAME.toString()));
                userinfos.add(userinfo);
            }
        } catch (ParseException e) {
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return userinfos;
    }

    @Override
    public List<Userinfo> getUsers(String position) {
        Log.d(TAG, "getUsers: Started..");
        List<Userinfo> userinfos = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.USERINFO.toString());
        query.whereEqualTo(USERINFO.POSITION.toString(), position);
        try {
            Log.d(TAG, "getUsers: Retrieving objects...");
            List<ParseObject> parseObjects = query.find();
            Log.d(TAG, "getUsers: Objects retrieved: " + parseObjects.size());
            for (ParseObject parseObject : parseObjects) {
                Userinfo userinfo = new Userinfo();
                userinfo.setObjectId(parseObject.getObjectId());
                userinfo.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                userinfo.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                userinfo.setEmail(parseObject.getString(USERINFO.EMAIL.toString()));
                userinfo.setFullname(parseObject.getString(USERINFO.FULLNAME.toString()));
                userinfo.setParent(parseObject.getString(USERINFO.PARENT.toString()));
                userinfo.setPosition(parseObject.getString(USERINFO.POSITION.toString()));
                userinfo.setStatus(parseObject.getString(USERINFO.STATUS.toString()));
                userinfo.setVerified(parseObject.getBoolean(USERINFO.VERIFIED.toString()));
                userinfo.setUsername(parseObject.getString(USERINFO.USERNAME.toString()));
                userinfos.add(userinfo);
            }
        } catch (ParseException e) {
            Log.d(TAG, "getUsers: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return userinfos;
    }
}
