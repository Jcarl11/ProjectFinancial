package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.CustomOperations;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.TagMaker;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements BaseDAO<Projects>, CustomOperations<Projects> {
    private static final String TAG = "ProjectDAO";
    private ProjectTypeDAO projectTypeBaseDAO = new ProjectTypeDAO();
    private ServicesDAO servicesBaseDAO = new ServicesDAO();
    private SubCategoryDAO subcategoryBaseDAO = new SubCategoryDAO();

    private DateTransform dateTransform = new DateTransform();
    private TagMaker tagMaker = new TagMaker();
    private int defaultLimit = 50;
    private ParseObject parseObject = null;


    @Override
    public Projects insert(Projects objectEntity) {
        Log.d(TAG, "insert: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.PROJECT.toString());
        parseObject.put(PROJECT.PROJECT_CODE.toString(), objectEntity.getProjectCode());
        parseObject.put(PROJECT.PROJECT_TITLE.toString(), objectEntity.getProjectName());
        parseObject.put(PROJECT.PROJECT_OWNER.toString(), objectEntity.getProjectOwner());
        parseObject.put(PROJECT.TYPE.toString(), objectEntity.getProjectType().getObjectId());
        parseObject.put(PROJECT.SERVICES.toString(), objectEntity.getProjectServices().getObjectId());
        parseObject.put(PROJECT.SUBCATEGORY.toString(), objectEntity.getProjectSubCategory().getObjectId());
        parseObject.put(PROJECT.DELETED.toString(), objectEntity.isDeleted());
        parseObject.put(PROJECT.Tags.toString(), tagMaker.fromList(objectEntity.getTags()));


        try {
            Log.d(TAG, "insert: Saving record...");
            parseObject.save();
            Log.d(TAG, "insert: Record saved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        Projects projects = new Projects();
        projects.setObjectId(parseObject.getObjectId());
        projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        projects.setProjectName(parseObject.getString(PROJECT.PROJECT_TITLE.toString()));
        projects.setProjectCode(parseObject.getString(PROJECT.PROJECT_CODE.toString()));
        projects.setProjectOwner(parseObject.getString(PROJECT.PROJECT_OWNER.toString()));
        projects.setProjectType(projectTypeBaseDAO.get(parseObject.getString(PROJECT.TYPE.toString())));
        projects.setProjectServices(servicesBaseDAO.get(parseObject.getString(PROJECT.SERVICES.toString())));
        projects.setProjectSubCategory(subcategoryBaseDAO.get(parseObject.getString(PROJECT.SUBCATEGORY.toString())));
        projects.setDeleted(parseObject.getBoolean(PROJECT.DELETED.toString()));
        projects.setTags(tagMaker.toArray(parseObject.getJSONArray(PROJECT.Tags.toString())));
        return projects;
    }

    @Override
    public int insertAll(List<Projects> objectList) {
        Log.d(TAG, "insertAll: Started...");
        int res = 0;
        List<String> ids = new ArrayList<>();
        for(Projects projects : objectList){
            String id = insert(projects).getObjectId();
            if(id != null) {
                ids.add(id);
            }
        }
        Log.d(TAG, "insertAll: Result: " + String.valueOf(ids.size()));
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));
        return ids.size();
    }

    @Override
    public Projects get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
            Log.d(TAG, "get: objectId: " + objectId);
            parseObject = query.get(objectId);
            if(parseObject == null) {
                return new Projects();
            }
            Log.d(TAG, "get: Retrieval finished");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception throw: " + e.getMessage());
        }

        Projects projects = new Projects();
        projects.setObjectId(parseObject.getObjectId());
        projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        projects.setProjectName(parseObject.getString(PROJECT.PROJECT_TITLE.toString()));
        projects.setProjectCode(parseObject.getString(PROJECT.PROJECT_CODE.toString()));
        projects.setProjectOwner(parseObject.getString(PROJECT.PROJECT_OWNER.toString()));
        projects.setProjectType(projectTypeBaseDAO.get(parseObject.getString(PROJECT.TYPE.toString())));
        projects.setProjectServices(servicesBaseDAO.get(parseObject.getString(PROJECT.SERVICES.toString())));
        projects.setProjectSubCategory(subcategoryBaseDAO.get(parseObject.getString(PROJECT.SUBCATEGORY.toString())));
        projects.setDeleted(parseObject.getBoolean(PROJECT.DELETED.toString()));
        projects.setTags(tagMaker.toArray(parseObject.getJSONArray(PROJECT.Tags.toString())));
        return projects;
    }

    @Override
    public List<Projects> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + defaultLimit);
        List<Projects> projectsList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
        query.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving projects...");
            List<ParseObject> command = query.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            Log.d(TAG, "getBulk: Size: " + command.size());
            /*if(command.size() <= 0 || command == null){
                projectsList.add(new Projects());
                return projectsList;
            }*/
            for(ParseObject parseObject : command){
                Projects projects = new Projects();
                projects.setObjectId(parseObject.getObjectId());
                projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
                projects.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
                projects.setProjectName(parseObject.getString(PROJECT.PROJECT_TITLE.toString()));
                projects.setProjectCode(parseObject.getString(PROJECT.PROJECT_CODE.toString()));
                projects.setProjectOwner(parseObject.getString(PROJECT.PROJECT_OWNER.toString()));
                projects.setProjectType(projectTypeBaseDAO.get(parseObject.getString(PROJECT.TYPE.toString())));
                projects.setProjectServices(servicesBaseDAO.get(parseObject.getString(PROJECT.SERVICES.toString())));
                projects.setProjectSubCategory(subcategoryBaseDAO.get(parseObject.getString(PROJECT.SUBCATEGORY.toString())));
                projects.setDeleted(parseObject.getBoolean(PROJECT.DELETED.toString()));
                projects.setTags(tagMaker.toArray(parseObject.getJSONArray(PROJECT.Tags.toString())));
                projectsList.add(projects);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return projectsList;
    }


    @Override
    public Projects update(Projects newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.PROJECT.toString());
        parseObject.put(PROJECT.PROJECT_CODE.toString(), newRecord.getProjectCode());
        parseObject.put(PROJECT.PROJECT_OWNER.toString(), newRecord.getProjectOwner());
        parseObject.put(PROJECT.PROJECT_TITLE.toString(), newRecord.getProjectName());
        parseObject.put(PROJECT.SERVICES.toString(), newRecord.getProjectServices().getObjectId());
        parseObject.put(PROJECT.TYPE.toString(), newRecord.getProjectType().getObjectId());
        parseObject.put(PROJECT.SUBCATEGORY.toString(), newRecord.getProjectSubCategory().getObjectId());
        parseObject.put(PROJECT.DELETED.toString(), newRecord.isDeleted());
        parseObject.put(PROJECT.Tags.toString(), newRecord.getTags());
        try {
            Log.d(TAG, "update: Saving object...");
            parseObject.save();
            Log.d(TAG, "update: Object saved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        Projects projects = new Projects();
        projects.setObjectId(parseObject.getObjectId());
        projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        projects.setProjectName(parseObject.getString(PROJECT.PROJECT_TITLE.toString()));
        projects.setProjectCode(parseObject.getString(PROJECT.PROJECT_CODE.toString()));
        projects.setProjectOwner(parseObject.getString(PROJECT.PROJECT_OWNER.toString()));
        projects.setProjectType(projectTypeBaseDAO.get(parseObject.getString(PROJECT.TYPE.toString())));
        projects.setProjectServices(servicesBaseDAO.get(parseObject.getString(PROJECT.SERVICES.toString())));
        projects.setProjectSubCategory(subcategoryBaseDAO.get(parseObject.getString(PROJECT.SUBCATEGORY.toString())));
        projects.setDeleted(parseObject.getBoolean(PROJECT.DELETED.toString()));
        projects.setTags(tagMaker.toArray(parseObject.getJSONArray(PROJECT.Tags.toString())));
        return projects;
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

    @Override
    public List<Projects> getObject(ParseQuery<ParseObject> customQuery) {
        Log.d(TAG, "getObject: Started...");
        List<Projects> projectsList = new ArrayList<>();
        if(customQuery.getClassName() == null) {
            Log.d(TAG, "getObject: Class not specified.");
            customQuery = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
        }

        try {
            Log.d(TAG, "getObject: Retriving objects...");
            List<ParseObject> objectList = customQuery.find();
            Log.d(TAG, "getObject: Objects retrieved: " + objectList.size());
            for(ParseObject parseObject : objectList) {
                Projects projects = new Projects();
                projects.setObjectId(parseObject.getObjectId());
                projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
                projects.setCreatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
                projects.setProjectName(parseObject.getString(PROJECT.PROJECT_TITLE.toString()));
                projects.setProjectCode(parseObject.getString(PROJECT.PROJECT_CODE.toString()));
                projects.setProjectOwner(parseObject.getString(PROJECT.PROJECT_OWNER.toString()));
                projects.setProjectType(projectTypeBaseDAO.get(parseObject.getString(PROJECT.TYPE.toString())));
                projects.setProjectServices(servicesBaseDAO.get(parseObject.getString(PROJECT.SERVICES.toString())));
                projects.setProjectSubCategory(subcategoryBaseDAO.get(parseObject.getString(PROJECT.SUBCATEGORY.toString())));
                projects.setDeleted(parseObject.getBoolean(PROJECT.DELETED.toString()));
                projects.setTags(tagMaker.toArray(parseObject.getJSONArray(PROJECT.Tags.toString())));
                projectsList.add(projects);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getObject: Exception thrown: " + e.getMessage());
        }

        return projectsList;
    }

    @Override
    public List<Projects> getProjectFromCode(String projectCode) {
        Log.d(TAG, "getProjectFromCode: Started...");
        List<Projects> projectsList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PROJECT.toString());
        query.whereEqualTo(PROJECT.PROJECT_CODE.toString(), projectCode);
        try {
            Log.d(TAG, "getProjectFromCode: Retrieving project...");
            List<ParseObject> parseObject = query.find();
            Log.d(TAG, "getProjectFromCode: Project retrieved: " + parseObject.size());
            for(ParseObject individuals : parseObject) {
                Projects projects = new Projects();
                projects.setObjectId(individuals.getObjectId());
                projects.setCreatedAt(dateTransform.toISO8601String(individuals.getCreatedAt()));
                projects.setCreatedAt(dateTransform.toISO8601String(individuals.getUpdatedAt()));
                projects.setProjectName(individuals.getString(PROJECT.PROJECT_TITLE.toString()));
                projects.setProjectCode(individuals.getString(PROJECT.PROJECT_CODE.toString()));
                projects.setProjectOwner(individuals.getString(PROJECT.PROJECT_OWNER.toString()));
                projects.setProjectType(projectTypeBaseDAO.get(individuals.getString(PROJECT.TYPE.toString())));
                projects.setProjectServices(servicesBaseDAO.get(individuals.getString(PROJECT.SERVICES.toString())));
                projects.setProjectSubCategory(subcategoryBaseDAO.get(individuals.getString(PROJECT.SUBCATEGORY.toString())));
                projects.setDeleted(individuals.getBoolean(PROJECT.DELETED.toString()));
                projects.setTags(tagMaker.toArray(individuals.getJSONArray(PROJECT.Tags.toString())));
                projectsList.add(projects);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getProjectFromCode: Exception thrown: " + e.getMessage());
        }
        return projectsList;
    }
}
