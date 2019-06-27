package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Image;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.IMAGES;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.HelperClasses.DateTransform;

import java.util.ArrayList;
import java.util.List;

public class ImageDAO implements BaseDAO<Image>, GetFromParent<Image>, NukeOperations<Image> {
    private static final String TAG = "ImageDAO";
    private DateTransform dateTransform = new DateTransform();
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public ImageDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Image insert(Image objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(IMAGES.PARENT.toString(), objectEntity.getParent());
        contentValues.put(IMAGES.FILENAME.toString(), objectEntity.getFileName());
        contentValues.put(IMAGES.DELETED.toString(), objectEntity.isDeleted());
        contentValues.put(IMAGES.FILES.toString(), objectEntity.getFilePath());
        long result = sqLiteDatabase.insert(PARSECLASS.IMAGES.toString(), null, contentValues);
        sqLiteDatabase.close();
        Log.d(TAG, "insert: Result: " + result);
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Image> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> ids = new ArrayList<>();
        for(Image image : objectList) {
            String id = insert(image).getObjectId();
            if(id != null)
                ids.add(id);
        }
        Log.d(TAG, "insertAll: Result: " + ids.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));
        return ids.size();
    }

    @Override
    public Image get(String objectId) {
        return null;
    }

    @Override
    public List<Image> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.IMAGES.toString()), new String[]{});
        List<Image> imageList = new ArrayList<>();
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if ( result <= 0 ) {
            return imageList;
        }

        while(cursor.moveToNext()) {
            Image image = new Image();
            image.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            image.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            image.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            image.setFileName(cursor.getString(cursor.getColumnIndex(IMAGES.FILENAME.toString())));
            image.setDeleted(cursor.getInt(cursor.getColumnIndex(IMAGES.DELETED.toString())) == 1 ? true : false);
            image.setParent(cursor.getString(cursor.getColumnIndex(IMAGES.PARENT.toString())));
            image.setFilePath(cursor.getString(cursor.getColumnIndex(IMAGES.FILES.toString())));
            imageList.add(image);
        }

        sqLiteDatabase.close();
        cursor.close();

        return imageList;
    }

    @Override
    public Image update(Image newRecord) {
        return null;
    }

    @Override
    public int delete(Image object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Image> objectList) {
        return 0;
    }

    @Override
    public List<Image> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started..");
        List<Image> imageList = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                String.format("SELECT * FROM %s WHERE %s = ?", PARSECLASS.IMAGES.toString(), IMAGES.PARENT.toString()), new String[]{parentID});
        int result = cursor.getCount();
        Log.d(TAG, "getObjects: Result: " + result);
        if(result <= 0) {
            return imageList;
        }

        while(cursor.moveToNext()) {
            Image image = new Image();
            image.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            image.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            image.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            image.setFileName(cursor.getString(cursor.getColumnIndex(IMAGES.FILENAME.toString())));
            image.setFilePath(cursor.getString(cursor.getColumnIndex(IMAGES.FILES.toString())));
            image.setParent(cursor.getString(cursor.getColumnIndex(IMAGES.PARENT.toString())));
            image.setDeleted(cursor.getInt(cursor.getColumnIndex(IMAGES.DELETED.toString())) == 1 ? true : false);
            imageList.add(image);
        }
        sqLiteDatabase.close();
        cursor.close();
        return imageList;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.IMAGES.toString(), "1", null);

        sqLiteDatabase.close();
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }
}
