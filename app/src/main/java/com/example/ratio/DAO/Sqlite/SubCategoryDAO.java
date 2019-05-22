package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Subcategory;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT_TYPE_SUBCATEGORY;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryDAO implements BaseDAO<Subcategory>, NukeOperations<Subcategory> {
    private static final String TAG = "SubCategoryDAO";
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public SubCategoryDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Subcategory insert(Subcategory objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(PROJECT_TYPE_SUBCATEGORY.NAME.toString(), objectEntity.getName());
        contentValues.put(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString(), objectEntity.isOthers() == true ? 1 : 0);
        contentValues.put(PROJECT_TYPE_SUBCATEGORY.PARENT.toString(), objectEntity.getParent());
        long result = sqLiteDatabase.insert(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString(), null, contentValues);
        Log.d(TAG, "insert: Result: " + result);
        sqLiteDatabase.close();
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Subcategory> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<Subcategory> subcategoryList = new ArrayList<>();
        for (Subcategory subcategory : objectList) {
            Subcategory result = insert(subcategory);
            if(result != null) {
                subcategoryList.add(result);
            }
        }
        Log.d(TAG, "insertAll: Result: " + subcategoryList.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - subcategoryList.size()));
        return subcategoryList.size();
    }

    @Override
    public Subcategory get(String objectId) {
        Log.d(TAG, "get: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ?",
                PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString(),
                DEFAULTS.objectId.toString()),
                new String[]{objectId});
        int result = cursor.getCount();
        Log.d(TAG, "get: Result size: " + result);
        if (result <= 0) {
            return new Subcategory();
        }

        Subcategory subcategory = new Subcategory();
        subcategory.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
        subcategory.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
        subcategory.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
        subcategory.setName(cursor.getString(cursor.getColumnIndex(PROJECT_TYPE_SUBCATEGORY.NAME.toString())));
        subcategory.setOthers(cursor.getInt(cursor.getColumnIndex(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString())) == 1 ? true : false);
        subcategory.setParent(cursor.getString(cursor.getColumnIndex(PROJECT_TYPE_SUBCATEGORY.PARENT.toString())));
        sqLiteDatabase.close();
        cursor.close();
        return subcategory;
    }

    @Override
    public List<Subcategory> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        List<Subcategory> subcategoryList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(
                String.format("SELECT * FROM %s", PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString()),
                null);
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result size: " + result);

        if (result <= 0 ){
            return subcategoryList;
        }
        while (cursor.moveToNext()) {
            Subcategory subcategory = new Subcategory();
            subcategory.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            subcategory.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            subcategory.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            subcategory.setName(cursor.getString(cursor.getColumnIndex(PROJECT_TYPE_SUBCATEGORY.NAME.toString())));
            subcategory.setOthers(cursor.getInt(cursor.getColumnIndex(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString())) == 1 ? true : false);
            subcategory.setParent(cursor.getString(cursor.getColumnIndex(PROJECT_TYPE_SUBCATEGORY.PARENT.toString())));
            subcategoryList.add(subcategory);
        }
        sqLiteDatabase.close();
        cursor.close();
        return subcategoryList;
    }

    @Override
    public Subcategory update(Subcategory newRecord) {
        Log.d(TAG, "update: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.updatedAt.toString(), newRecord.getUpdatedAt());
        contentValues.put(PROJECT_TYPE_SUBCATEGORY.NAME.toString(), newRecord.getName());
        contentValues.put(PROJECT_TYPE_SUBCATEGORY.OTHERS.toString(), newRecord.isOthers() == true ? 1 : 0);
        contentValues.put(PROJECT_TYPE_SUBCATEGORY.PARENT.toString(), newRecord.getParent());
        int result = sqLiteDatabase.update(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString(),
                contentValues, String.format("%s = ?", DEFAULTS.objectId.toString()),
                new String[]{newRecord.getObjectId()});
        Log.d(TAG, "update: Result: " + result);
        sqLiteDatabase.close();
        return result <= 0 ? null : newRecord;
    }

    @Override
    public int delete(Subcategory object) {
        Log.d(TAG, "delete: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString(),
                String.format("%s = ?", DEFAULTS.objectId.toString()),
                new String[]{object.getObjectId()});
        Log.d(TAG, "delete: Result: " + result);
        sqLiteDatabase.close();
        return result;
    }

    @Override
    public int deleteAll(List<Subcategory> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for (Subcategory subcategory : objectList) {
            result += delete(subcategory);
        }
        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));
        return result;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString(), "1", null);
        Log.d(TAG, "deleteRows: Result: " + result);
        return result;
    }

    @Override
    public boolean dropTable() {
        //To implement
        return false;
    }
}
