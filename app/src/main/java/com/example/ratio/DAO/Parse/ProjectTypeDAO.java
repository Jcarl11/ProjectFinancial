package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT_TYPE;
import com.example.ratio.Utilities.DateTransform;
import com.example.ratio.Utilities.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProjectTypeDAO implements BaseDAO<ProjectType> {
    private static final String TAG = "ProjectTypeDAO";
    private DateTransform dateTransform = new DateTransform();
    private int result = 0;
    private int defaultLimit = 50;
    private boolean isSuccessful = false;
    private ParseObject parseObject = null;

    @Override
    public ProjectType insert(ProjectType objectEntity) {
        Log.d(TAG, "insert: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.PROJECT_TYPE.toString());
        parseObject.put(PROJECT_TYPE.NAME.toString(), objectEntity.getName());
        try {
            Log.d(TAG, "insert: Saving...");
            parseObject.save();
            Log.d(TAG, "insert: Done");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        ProjectType projectType = new ProjectType();
        projectType.setObjectId(parseObject.getObjectId());
        projectType.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        projectType.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        projectType.setName(parseObject.getString(PROJECT_TYPE.NAME.toString()));
        projectType.setOthers(parseObject.getBoolean(PROJECT_TYPE.OTHERS.toString()));
        return projectType;
    }

    @Override
    public int insertAll(List<ProjectType> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> ids = new ArrayList<>();
        for(ProjectType projectType : objectList){
            String id = insert(projectType).getObjectId();
            if(id != null)
                ids.add(id);
        }

        Log.d(TAG, "insertAll: Result: " + String.valueOf(ids.size()));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));
        return ids.size();
    }

    @Override
    public ProjectType get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE.toString());
        ProjectType projectType = new ProjectType();
        try {
            Log.d(TAG, "get: Retrieving object...");
            parseObject = query.get(objectId);
            if(parseObject == null) {
                return new ProjectType();
            }
            Log.d(TAG, "get: Object retrieved");
            projectType.setObjectId(parseObject.getObjectId());
            projectType.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
            projectType.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
            projectType.setName(parseObject.getString(PROJECT_TYPE.NAME.toString()));
            projectType.setOthers(parseObject.getBoolean(PROJECT_TYPE.OTHERS.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }


        return projectType;
    }

    @Override
    public List<ProjectType> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<ProjectType> projectTypeList = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.PROJECT_TYPE.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving types...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            if(parseObjects.size() <= 0 || parseObjects == null) {
                List<ProjectType> list = new ArrayList<>();
                list.add(new ProjectType());
                return list;
            }
            for(ParseObject parseObject : parseObjects){
                ProjectType projectType = new ProjectType();
                projectType.setObjectId(parseObject.getObjectId());
                projectType.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
                projectType.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
                projectType.setName(parseObject.getString(PROJECT_TYPE.NAME.toString()));
                projectType.setOthers(parseObject.getBoolean(PROJECT_TYPE.OTHERS.toString()));
                projectTypeList.add(projectType);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return projectTypeList;
    }


    @Override
    public ProjectType update(ProjectType newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.PROJECT_TYPE.toString());
        parseObject.put(PROJECT_TYPE.NAME.toString(), newRecord.getName());
        parseObject.put(PROJECT_TYPE.OTHERS.toString(), newRecord.isOthers());
        try {
            Log.d(TAG, "update: Saving record...");
            parseObject.save();
            Log.d(TAG, "update: Save finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        ProjectType projectType = new ProjectType();
        projectType.setObjectId(parseObject.getObjectId());
        projectType.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        projectType.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        projectType.setName(parseObject.getString(PROJECT_TYPE.NAME.toString()));
        projectType.setOthers(parseObject.getBoolean(PROJECT_TYPE.OTHERS.toString()));
        return projectType;
    }

    @Override
    public int delete(ProjectType object) {
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
    public int deleteAll(List<ProjectType> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int res = 0;
        for(ProjectType projectType : objectList){
            res += delete(projectType);
        }

        Log.d(TAG, "deleteAll: Result: " + String.valueOf(res));
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }
}
