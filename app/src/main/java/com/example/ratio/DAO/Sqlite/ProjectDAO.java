package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Entities.Projects;
import com.example.ratio.Entities.Services;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT;
import com.example.ratio.HelperClasses.DateTransform;

import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements BaseDAO<Projects>, NukeOperations<Projects> {
    private static final String TAG = "ProjectDAO";
    private DateTransform dateTransform = new DateTransform();
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public ProjectDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Projects insert(Projects objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(PROJECT.PROJECT_CODE.toString(), objectEntity.getProjectCode());
        contentValues.put(PROJECT.PROJECT_TITLE.toString(), objectEntity.getProjectName());
        contentValues.put(PROJECT.PROJECT_OWNER.toString(), objectEntity.getProjectOwner());
        contentValues.put(PROJECT.TYPE.toString(), objectEntity.getProjectType().getObjectId());
        contentValues.put(PROJECT.SERVICES.toString(), objectEntity.getProjectServices().getObjectId());
        contentValues.put(PROJECT.SUBCATEGORY.toString(), objectEntity.getProjectSubCategory().getObjectId());
        contentValues.put(PROJECT.DELETED.toString(), objectEntity.isDeleted());
        long result = sqLiteDatabase.insert(PARSECLASS.PROJECT.toString(), null, contentValues);
        sqLiteDatabase.close();
        Log.d(TAG, "insert: Result: " + result);
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Projects> objectList) {
        Log.d(TAG, "insertAll: Started..");
        List<String> idList = new ArrayList<>();
        for(Projects projects : objectList) {
            String id = insert(projects).getObjectId();
            if(id != null)
                idList.add(id);
        }
        Log.d(TAG, "insertAll: Result: " + idList.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - idList.size()));
        return idList.size();
    }

    @Override
    public Projects get(String objectId) {
        Log.d(TAG, "get: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Projects projects = new Projects();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ?",
                        PARSECLASS.PROJECT.toString(), DEFAULTS.objectId.toString()), new String[]{objectId});
        int result = cursor.getCount();
        Log.d(TAG, "get: Result: " + result);
        if(result <= 0) {
            return new Projects();
        }
        while(cursor.moveToNext()) {

            ProjectType projectType = new ProjectType();
            Services services = new Services();
            Subcategory subcategory = new Subcategory();
            projectType.setObjectId(cursor.getString(cursor.getColumnIndex(PROJECT.TYPE.toString())));
            services.setObjectId(cursor.getString(cursor.getColumnIndex(PROJECT.SERVICES.toString())));
            subcategory.setObjectId(cursor.getString(cursor.getColumnIndex(PROJECT.SUBCATEGORY.toString())));

            projects.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            projects.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            projects.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            projects.setProjectName(cursor.getString(cursor.getColumnIndex(PROJECT.PROJECT_TITLE.toString())));
            projects.setProjectCode(cursor.getString(cursor.getColumnIndex(PROJECT.PROJECT_CODE.toString())));
            projects.setProjectOwner(cursor.getString(cursor.getColumnIndex(PROJECT.PROJECT_OWNER.toString())));
            projects.setProjectType(projectType);
            projects.setProjectServices(services);
            projects.setProjectSubCategory(subcategory);
            projects.setDeleted(cursor.getInt(cursor.getColumnIndex(PROJECT.DELETED.toString())) == 1 ? true : false);
        }
        sqLiteDatabase.close();
        cursor.close();
        return projects;
    }

    @Override
    public List<Projects> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started..");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.PROJECT.toString()), null);
        List<Projects> projectsList = new ArrayList<>();
        int result = cursor.getCount();
        if ( result <= 0 ) {
            return projectsList;
        }

        while(cursor.moveToNext()) {
            Projects projects = new Projects();
            ProjectType projectType = new ProjectType();
            Services services = new Services();
            Subcategory subcategory = new Subcategory();
            projectType.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            services.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            subcategory.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));

            projects.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            projects.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            projects.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            projects.setProjectName(cursor.getString(cursor.getColumnIndex(PROJECT.PROJECT_TITLE.toString())));
            projects.setProjectCode(cursor.getString(cursor.getColumnIndex(PROJECT.PROJECT_CODE.toString())));
            projects.setProjectOwner(cursor.getString(cursor.getColumnIndex(PROJECT.PROJECT_OWNER.toString())));
            projects.setProjectType(projectType);
            projects.setProjectServices(services);
            projects.setProjectSubCategory(subcategory);
            projects.setDeleted(cursor.getInt(cursor.getColumnIndex(PROJECT.DELETED.toString())) == 1 ? true : false);
            projectsList.add(projects);
        }

        sqLiteDatabase.close();
        cursor.close();
        return projectsList;
    }


    @Override
    public Projects update(Projects newRecord) {
        return null;
    }

    @Override
    public int delete(Projects object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Projects> objectList) {
        return 0;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.PROJECT.toString(), "1", null);

        sqLiteDatabase.close();
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }
}
