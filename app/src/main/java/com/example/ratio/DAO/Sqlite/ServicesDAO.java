package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.DAOFactory;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Services;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.SERVICES;

import java.util.ArrayList;
import java.util.List;

public class ServicesDAO implements BaseDAO<Services>, NukeOperations<Services> {
    private static final String TAG = "ServicesDAO";
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public ServicesDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Services insert(Services objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(SERVICES.NAME.toString(), objectEntity.getName());
        contentValues.put(SERVICES.OTHERS.toString(), objectEntity.isOthers());
        long result = sqLiteDatabase.insert(PARSECLASS.SERVICES.toString(), null, contentValues);
        Log.d(TAG, "insert: Result: " + result);
        sqLiteDatabase.close();
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Services> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<Services> servicesList = new ArrayList<>();
        int result = 0;
        for (Services services : objectList) {
            Services res = insert(services);
            if(res != null) {
                servicesList.add(res);
            }
        }
        Log.d(TAG, "insertAll: Result size: " + servicesList.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - servicesList.size()));
        return servicesList.size();
    }

    @Override
    public Services get(String objectId) {
        Log.d(TAG, "get: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ?",
                PARSECLASS.SERVICES.toString(), DEFAULTS.objectId.toString()), new String[]{objectId});
        int result = cursor.getCount();
        Log.d(TAG, "get: Result: " + result);
        Services services = new Services();
        if(result <= 0) {
            return services;
        }
        while (cursor.moveToNext()) {
            services.setObjectId(cursor.getString(0));
            services.setCreatedAt(cursor.getString(1));
            services.setUpdatedAt(cursor.getString(2));
            services.setName(cursor.getString(3));
            services.setOthers( cursor.getInt(4) == 1 ? true : false );
            return services;
        }
        sqLiteDatabase.close();
        cursor.close();
        return null;
    }

    @Override
    public List<Services> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        List<Services> servicesList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(
                String.format("SELECT * FROM %s", PARSECLASS.SERVICES.toString()), null);
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if (result <= 0) {
            return servicesList;
        }

        while(cursor.moveToNext()) {
            Services services = new Services();
            services.setObjectId(cursor.getString(0));
            services.setCreatedAt(cursor.getString(1));
            services.setUpdatedAt(cursor.getString(2));
            services.setName(cursor.getString(3));
            services.setOthers( cursor.getInt(4) == 1 ? true : false );
            servicesList.add(services);
        }
        sqLiteDatabase.close();
        cursor.close();
        return servicesList;
    }


    @Override
    public Services update(Services newRecord) {
        Log.d(TAG, "update: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.updatedAt.toString(), newRecord.getUpdatedAt());
        contentValues.put(SERVICES.NAME.toString(), newRecord.getName());
        contentValues.put(SERVICES.OTHERS.toString(), newRecord.isOthers() == true ? 1 : 0);
        int result = sqLiteDatabase.update(PARSECLASS.SERVICES.toString(),
                contentValues, String.format("%s = ?", DEFAULTS.objectId.toString()),
                new String[]{newRecord.getObjectId()});
        Log.d(TAG, "update: Result: " + result);
        sqLiteDatabase.close();
        return result <= 0 ? null : newRecord;
    }

    @Override
    public int delete(Services object) {
        Log.d(TAG, "delete: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(PARSECLASS.SERVICES.toString(),
                String.format("%s = ?", DEFAULTS.objectId.toString()),
                new String[]{object.getObjectId()});
        Log.d(TAG, "delete: Result: " + result);
        sqLiteDatabase.close();
        return result;
    }

    @Override
    public int deleteAll(List<Services> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for (Services services : objectList) {
            result += delete(services);
        }
        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));
        return result;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.SERVICES.toString(), "1", null);
        Log.d(TAG, "deleteRows: deleted records: " + deletedRows);
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }
}
