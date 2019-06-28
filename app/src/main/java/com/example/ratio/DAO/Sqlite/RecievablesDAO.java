package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Entities.Receivables;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.RECIEVABLES;
import com.example.ratio.HelperClasses.DateTransform;

import java.util.ArrayList;
import java.util.List;

public class RecievablesDAO implements BaseDAO<Receivables>, GetFromParent<Receivables>, NukeOperations<Receivables> {
    private static final String TAG = "RecievablesDAO";
    private DateTransform dateTransform = new DateTransform();
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public RecievablesDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }
    @Override
    public Receivables insert(Receivables objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(RECIEVABLES.PARENT.toString(), objectEntity.getParent());
        contentValues.put(RECIEVABLES.AMOUNT.toString(), objectEntity.getAmount());
        contentValues.put(RECIEVABLES.ATTACHMENTS.toString(), objectEntity.isAttachments());
        contentValues.put(RECIEVABLES.DESCRIPTION.toString(), objectEntity.getDescription());
        contentValues.put(RECIEVABLES.TIMESTAMP.toString(), objectEntity.getTimestamp());
        long result = sqLiteDatabase.insert(PARSECLASS.RECEIVABLES.toString(), null, contentValues);
        Log.d(TAG, "insert: Result: " + result);
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Receivables> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<Receivables> receivablesList = new ArrayList<>();
        for(Receivables receivable : objectList) {
            Receivables result = insert(receivable);
            if(result != null) {
                receivablesList.add(result);
            }
        }
        Log.d(TAG, "insertAll: Result: " + receivablesList.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - receivablesList.size()));
        return receivablesList.size();
    }

    @Override
    public Receivables get(String objectId) {
        return null;
    }

    @Override
    public List<Receivables> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        List<Receivables> receivablesList = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.RECEIVABLES.toString()), new String[]{});
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if(result <= 0) {
            return receivablesList;
        }

        while(cursor.moveToNext()) {
            Receivables receivable = new Receivables();
            receivable.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            receivable.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            receivable.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            receivable.setParent(cursor.getString(cursor.getColumnIndex(RECIEVABLES.PARENT.toString())));
            receivable.setDescription(cursor.getString(cursor.getColumnIndex(RECIEVABLES.DESCRIPTION.toString())));
            receivable.setAmount(cursor.getString(cursor.getColumnIndex(RECIEVABLES.AMOUNT.toString())));
            receivable.setAttachments(cursor.getInt(cursor.getColumnIndex(RECIEVABLES.ATTACHMENTS.toString())) == 1 ? true : false);
            receivable.setTimestamp(cursor.getString(cursor.getColumnIndex(RECIEVABLES.TIMESTAMP.toString())));
            receivablesList.add(receivable);
        }

        sqLiteDatabase.close();
        cursor.close();
        return receivablesList;
    }

    @Override
    public Receivables update(Receivables newRecord) {
        return null;
    }

    @Override
    public int delete(Receivables object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Receivables> objectList) {
        return 0;
    }

    @Override
    public List<Receivables> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        List<Receivables> receivablesList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ?",
                PARSECLASS.RECEIVABLES.toString(), RECIEVABLES.PARENT.toString()), new String[]{parentID});
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if(result <= 0) {
            return receivablesList;
        }

        while(cursor.moveToNext()) {
            Receivables receivable = new Receivables();
            receivable.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            receivable.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            receivable.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            receivable.setParent(cursor.getString(cursor.getColumnIndex(RECIEVABLES.PARENT.toString())));
            receivable.setDescription(cursor.getString(cursor.getColumnIndex(RECIEVABLES.DESCRIPTION.toString())));
            receivable.setAmount(cursor.getString(cursor.getColumnIndex(RECIEVABLES.AMOUNT.toString())));
            receivable.setAttachments(cursor.getInt(cursor.getColumnIndex(RECIEVABLES.ATTACHMENTS.toString())) == 1 ? true : false);
            receivable.setTimestamp(cursor.getString(cursor.getColumnIndex(RECIEVABLES.TIMESTAMP.toString())));
            receivablesList.add(receivable);
        }

        sqLiteDatabase.close();
        cursor.close();
        return receivablesList;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.RECEIVABLES.toString(), "1", null);

        sqLiteDatabase.close();
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }
}
