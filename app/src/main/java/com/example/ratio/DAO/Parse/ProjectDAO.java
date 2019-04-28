package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Services;
import com.example.ratio.Entities.Status;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT;
import com.example.ratio.Utilities.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements BaseDAO<Projects> {
    private static final String TAG = "ProjectDAO";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    int result = 0;
    int defaultLimit = 50;
    boolean isSuccessful = false;
    ParseObject parseObject = null;

    @Override
    public int insert(Projects objectEntity) {
        Log.d(TAG, "insert: Started...");
        result = 0;
        ParseObject parseObject = new ParseObject(PARSECLASS.PROJECT.toString());
        parseObject.put(PROJECT.PROJECT_CODE.toString(), objectEntity.getProjectCode());
        parseObject.put(PROJECT.PROJECT_TITLE.toString(), objectEntity.getProjectName());
        parseObject.put(PROJECT.PROJECT_OWNER.toString(), objectEntity.getProjectOwner());
        parseObject.put(PROJECT.TYPE.toString(), objectEntity.getProjectType().getObjectId());
        parseObject.put(PROJECT.SERVICES.toString(), objectEntity.getProjectServices().getObjectId());
        parseObject.put(PROJECT.SUBCATEGORY.toString(), objectEntity.getProjectServices().getObjectId());
        parseObject.put(PROJECT.STATUS.toString(), objectEntity.getProjectStatus().getObjectId());
        parseObject.put(PROJECT.DELETED.toString(), objectEntity.isDeleted());
        parseObject.put(PROJECT.Tags.toString(), objectEntity.getTags());
        try {
            Log.d(TAG, "insert: Saving record...");
            parseObject.save();
            result = 1;
            Log.d(TAG, "insert: Record saved");

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int insertAll(List<Projects> objectList) {
        Log.d(TAG, "insertAll: Started...");
        int res = 0;
        for(Projects projects : objectList){
            res += insert(projects);
        }
        Log.d(TAG, "insertAll: Result: " + String.valueOf(res));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - res));
        return res;
    }

    @Override
    public Projects get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
        ParseQuery<ParseObject> statusObj = ParseQuery.getQuery(PARSECLASS.STATUS.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Retrival finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception throw: " + e.getMessage());
        }

        Projects projects = new Projects();
        projects.setObjectId(parseObject.getObjectId());
        projects.setCreatedAt(dateFormat.format(parseObject.getCreatedAt()));
        projects.setCreatedAt(dateFormat.format(parseObject.getUpdatedAt()));
        projects.setProjectName(parseObject.getString(PROJECT.PROJECT_TITLE.toString()));
        projects.setProjectCode(parseObject.getString(PROJECT.PROJECT_CODE.toString()));
        projects.setProjectOwner(parseObject.getString(PROJECT.PROJECT_OWNER.toString()));
        projects.setProjectStatus(new Status(parseObject.getString(PROJECT.STATUS.toString())));
        projects.setProjectType(new ProjectType());
        projects.setProjectServices(new Services());
        projects.setProjectSubCategory(new Subcategory());
        return projects;
    }

    @Override
    public List<Projects> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + defaultLimit);
        List<Projects> projectsList = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving types...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Projects projects = new Projects();
                projects.setObjectId(parseObject.getObjectId());
                projects.setCreatedAt(dateFormat.format(parseObject.getCreatedAt()));
                projects.setUpdatedAt(dateFormat.format(parseObject.getUpdatedAt()));
                projectsList.add(projects);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return null;
    }


    @Override
    public boolean update(Projects newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject projectUpdate = new ParseObject(PARSECLASS.PROJECT.toString());
        return false;
    }

    @Override
    public int delete(Projects object) {
        Log.d(TAG, "delete: Started...");
        int result = 0;
        ParseQuery<ParseObject> deleteQuery = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
        try {
            Log.d(TAG, "delete: Retrieving object...");
            ParseObject parseObject = deleteQuery.get(object.getObjectId());
            Log.d(TAG, "delete: Object retrieved: " + parseObject.getObjectId());
            Log.d(TAG, "delete: Deleting object now...");
            parseObject.delete();
            result = 1;
            Log.d(TAG, "delete: Object deleted successfully");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int deleteAll(List<Projects> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for(Projects projects : objectList) {
            result += delete(projects);
        }

        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));
        return result;
    }
}
