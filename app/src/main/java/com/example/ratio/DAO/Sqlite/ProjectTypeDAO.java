package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.ProjectType;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT_TYPE;

import java.util.ArrayList;
import java.util.List;

public class ProjectTypeDAO implements BaseDAO<ProjectType>, NukeOperations<ProjectType> {
    private static final String TAG = "ProjectTypeDAO";
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;
    private int defaultLimit = 50;

    public ProjectTypeDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public ProjectType insert(ProjectType objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(PROJECT_TYPE.NAME.toString(), objectEntity.getName());
        contentValues.put(PROJECT_TYPE.OTHERS.toString(), objectEntity.isOthers() == true ? 1 : 0);
        long result = sqLiteDatabase.insert(PARSECLASS.PROJECT_TYPE.toString(),
                null, contentValues);
        Log.d(TAG, "insert: Result: " + result);
        sqLiteDatabase.close();
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<ProjectType> objectList) {
        Log.d(TAG, "insertAll: Started...");
        int result = 0;
        List<ProjectType> projectTypeList = new ArrayList<>();
        for (ProjectType projectType : objectList) {
            ProjectType res = insert(projectType);
            if (res != null) {
                projectTypeList.add(res);
            }
        }
        Log.d(TAG, "insertAll: Result: " + projectTypeList.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - projectTypeList.size()));
        return projectTypeList.size();
    }

    @Override
    public ProjectType get(String objectId) {
        Log.d(TAG, "get: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                String.format("SELECT * FROM %s WHERE %s = ?", PARSECLASS.PROJECT_TYPE.toString(), DEFAULTS.objectId.toString()),
                new String[]{objectId});
        int result = cursor.getCount();
        Log.d(TAG, "get: Result: " + result);
        if (result <= 0) {
            return new ProjectType();
        }
        ProjectType projectType = new ProjectType();
        projectType.setObjectId(cursor.getString(0));
        projectType.setCreatedAt(cursor.getString(1));
        projectType.setUpdatedAt(cursor.getString(2));
        projectType.setName(cursor.getString(3));
        projectType.setOthers(cursor.getInt(4) == 1 ? true : false);
        return projectType;
    }

    @Override
    public List<ProjectType> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        List<ProjectType> projectTypeList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.PROJECT_TYPE.toString()), null);
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if (result <= 0) {
            return projectTypeList;
        }

        while (cursor.moveToNext()) {
            ProjectType projectType = new ProjectType();
            projectType.setObjectId(cursor.getString(0));
            projectType.setCreatedAt(cursor.getString(1));
            projectType.setUpdatedAt(cursor.getString(2));
            projectType.setName(cursor.getString(3));
            projectType.setOthers(cursor.getInt(4) == 1 ? true : false);
            projectTypeList.add(projectType);
        }
        sqLiteDatabase.close();
        cursor.close();
        return projectTypeList;
    }


    @Override
    public ProjectType update(ProjectType newRecord) {
        Log.d(TAG, "update: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.updatedAt.toString(), newRecord.getUpdatedAt());
        contentValues.put(PROJECT_TYPE.NAME.toString(), newRecord.getName());
        contentValues.put(PROJECT_TYPE.OTHERS.toString(), newRecord.isOthers() == true ? 1 : 0);
        int result = sqLiteDatabase.update(PARSECLASS.PROJECT_TYPE.toString(),
                contentValues,
                String.format("%s = ?", DEFAULTS.objectId.toString()),
                new String[]{newRecord.getObjectId()});
        Log.d(TAG, "update: Result: " + result);
        sqLiteDatabase.close();
        return result <= 0 ? null : newRecord;
    }

    @Override
    public int delete(ProjectType object) {
        Log.d(TAG, "delete: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(PARSECLASS.PROJECT_TYPE.toString(),
                String.format("%s = ?", DEFAULTS.objectId.toString()),
                new String[]{object.getObjectId()});
        Log.d(TAG, "delete: Result: " + result);

        return result;
    }

    @Override
    public int deleteAll(List<ProjectType> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for (ProjectType projectType : objectList) {
            result += delete(projectType);
        }
        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));
        return result;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(PARSECLASS.PROJECT_TYPE.toString(), "1", null);
        Log.d(TAG, "deleteRows: Result: " + result);
        return result;
    }

    @Override
    public boolean dropTable() {
        //To implement
        return false;
    }
}
