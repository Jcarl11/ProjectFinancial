package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetDistinct;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Status;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.STATUS;
import com.example.ratio.HelperClasses.DateTransform;

import java.util.ArrayList;
import java.util.List;

public class StatusDAO implements BaseDAO<Status>, NukeOperations<Status>, GetDistinct<Status> {

    private static final String TAG = "StatusDAO";
    private DateTransform dateTransform = new DateTransform();
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;
    public StatusDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Status insert(Status objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(STATUS.NAME.toString(), objectEntity.getName());
        contentValues.put(STATUS.PARENT.toString(), objectEntity.getParent());
        long result = sqLiteDatabase.insert(PARSECLASS.STATUS.toString(), null, contentValues);
        sqLiteDatabase.close();
        Log.d(TAG, "insert: Result: " + result);
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Status> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> operations = new ArrayList<>();
        for (Status status : objectList) {
            String ids = insert(status).getObjectId();
            if (ids != null) {
                operations.add(ids);
            }
        }

        Log.d(TAG, "insertAll: Result size: " + operations.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(operations.size() - objectList.size()));
        return operations.size();
    }

    @Override
    public Status get(String objectId) {
        Log.d(TAG, "get: Started..");
        Status status = new Status();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                String.format("SELECT * FROM STATUS WHERE objectId = %s", objectId),null);
        int result = cursor.getCount();
        Log.d(TAG, "get: Result: " + result);
        if (result <= 0) {
            return status;
        }
        while (cursor.moveToNext()) {
            status.setObjectId(cursor.getString(0));
            status.setCreatedAt(cursor.getString(1));
            status.setUpdatedAt(cursor.getString(2));
            status.setName(cursor.getString(3));
            status.setParent(cursor.getString(4));
        }

        cursor.close();
        sqLiteDatabase.close();
        return status;
    }
    @Override
    public List<Status> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.STATUS.toString()), null);
        List<Status> statusList = new ArrayList<>();
        int result = cursor.getCount();
        if ( result <= 0 ) {
            return statusList;
        }

        while(cursor.moveToNext()) {
            Status status = new Status();
            status.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            status.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            status.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            status.setName(cursor.getString(cursor.getColumnIndex(STATUS.NAME.toString())));
            status.setParent(cursor.getString(cursor.getColumnIndex(STATUS.PARENT.toString())));
            statusList.add(status);
        }

        sqLiteDatabase.close();
        cursor.close();

        return statusList;
    }

    @Override
    public Status update(Status newRecord) {
        Log.d(TAG, "update: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.updatedAt.toString(), newRecord.getUpdatedAt());
        contentValues.put(STATUS.NAME.toString(), newRecord.getName());
        contentValues.put(STATUS.PARENT.toString(), newRecord.getParent());
        int result = sqLiteDatabase.update(PARSECLASS.STATUS.toString(),
                contentValues, String.format("%s = ?", DEFAULTS.objectId.toString()), new String[]{newRecord.getObjectId()});
        Status status = new Status();
        if(result == 1) {
            status.setObjectId(newRecord.getObjectId());
            status.setCreatedAt(newRecord.getCreatedAt());
            status.setUpdatedAt(newRecord.getUpdatedAt());
            status.setName(newRecord.getName());
            status.setParent(newRecord.getParent());
            return status;
        }
        sqLiteDatabase.close();
        return status;
    }

    @Override
    public int delete(Status object) {
        Log.d(TAG, "delete: Started...");
        int result = sqLiteDatabase.delete(PARSECLASS.STATUS.toString(),
                String.format("%s = ?", DEFAULTS.objectId.toString()),
                new String[]{object.getObjectId()});
        sqLiteDatabase.close();
        Log.d(TAG, "delete: Result: " + result);
        return result;
    }

    @Override
    public int deleteAll(List<Status> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for (Status status : objectList) {
            result += delete(status);
        }
        Log.d(TAG, "deleteAll: Result size: " + result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));
        return result;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.STATUS.toString(), "1", null);

        sqLiteDatabase.close();
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }

    @Override
    public List<Status> getDistinct() {
        Log.d(TAG, "getBulk: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        List<Status> statuses = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT * FROM STATUS",null);
        int result = cursor.getCount();
        Log.d(TAG, "get: Result: " + result);
        if (result <= 0) {
            return statuses;
        }

        while (cursor.moveToNext()) {
            Status status = new Status();
            status.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            status.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            status.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            status.setName(cursor.getString(cursor.getColumnIndex(STATUS.NAME.toString())));
            status.setParent(cursor.getString(cursor.getColumnIndex(STATUS.PARENT.toString())));
            statuses.add(status);
        }


        cursor.close();
        sqLiteDatabase.close();

        return statuses;
    }
}
