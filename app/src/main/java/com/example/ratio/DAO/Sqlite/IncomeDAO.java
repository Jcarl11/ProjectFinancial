package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetAverage;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Income;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.INCOME;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.HelperClasses.DateTransform;

import java.util.ArrayList;
import java.util.List;

public class IncomeDAO implements BaseDAO<Income>, GetFromParent<Income>, NukeOperations<Income>, GetAverage<Income> {
    private static final String TAG = "IncomeDAO";
    private DateTransform dateTransform = new DateTransform();
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public IncomeDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Income insert(Income objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(INCOME.PARENT.toString(), objectEntity.getParent());
        contentValues.put(INCOME.AMOUNT.toString(), objectEntity.getAmount());
        contentValues.put(INCOME.ATTACHMENTS.toString(), objectEntity.isAttachments());
        contentValues.put(INCOME.DESCRIPTION.toString(), objectEntity.getDescription());
        contentValues.put(INCOME.TIMESTAMP.toString(), objectEntity.getTimestamp());
        long result = sqLiteDatabase.insert(PARSECLASS.INCOME.toString(), null, contentValues);
        Log.d(TAG, "insert: Result: " + result);
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Income> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<Income> incomeList = new ArrayList<>();
        for(Income income : objectList) {
            Income result = insert(income);
            if(result != null) {
                incomeList.add(result);
            }
        }
        Log.d(TAG, "insertAll: Result: " + incomeList.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - incomeList.size()));
        return incomeList.size();
    }

    @Override
    public Income get(String objectId) {
        return null;
    }

    @Override
    public List<Income> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        List<Income> incomeList = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.INCOME.toString()), new String[]{});
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if(result <= 0) {
            return incomeList;
        }

        while(cursor.moveToNext()) {
            Income income = new Income();
            income.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            income.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            income.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            income.setParent(cursor.getString(cursor.getColumnIndex(INCOME.PARENT.toString())));
            income.setDescription(cursor.getString(cursor.getColumnIndex(INCOME.DESCRIPTION.toString())));
            income.setAmount(cursor.getString(cursor.getColumnIndex(INCOME.AMOUNT.toString())));
            income.setAttachments(cursor.getInt(cursor.getColumnIndex(INCOME.ATTACHMENTS.toString())) == 1 ? true : false);
            income.setTimestamp(cursor.getString(cursor.getColumnIndex(INCOME.TIMESTAMP.toString())));
            incomeList.add(income);
        }

        sqLiteDatabase.close();
        cursor.close();
        return incomeList;
    }


    @Override
    public Income update(Income newRecord) {
        return null;
    }

    @Override
    public int delete(Income object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Income> objectList) {
        return 0;
    }

    @Override
    public List<Income> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        List<Income> incomeList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ?",
                PARSECLASS.INCOME.toString(), INCOME.PARENT.toString()), new String[]{parentID});
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if(result <= 0) {
            return incomeList;
        }

        while(cursor.moveToNext()) {
            Income income = new Income();
            income.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            income.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            income.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            income.setParent(cursor.getString(cursor.getColumnIndex(INCOME.PARENT.toString())));
            income.setDescription(cursor.getString(cursor.getColumnIndex(INCOME.DESCRIPTION.toString())));
            income.setAmount(cursor.getString(cursor.getColumnIndex(INCOME.AMOUNT.toString())));
            income.setAttachments(cursor.getInt(cursor.getColumnIndex(INCOME.ATTACHMENTS.toString())) == 1 ? true : false);
            income.setTimestamp(cursor.getString(cursor.getColumnIndex(INCOME.TIMESTAMP.toString())));
            incomeList.add(income);
        }

        sqLiteDatabase.close();
        cursor.close();
        return incomeList;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.INCOME.toString(), "1", null);

        sqLiteDatabase.close();
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }

    @Override
    public List<Income> getTopHighest(int limit) {
        Log.d(TAG, "getTopHighest: Started...");
        List<Income> incomeList = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT AVG(%s) [AMOUNT], %s, %s FROM %s GROUP BY PARENT ORDER BY AMOUNT DESC LIMIT %s"
        ,INCOME.AMOUNT.toString(), INCOME.PARENT.toString(), INCOME.TIMESTAMP.toString(), PARSECLASS.INCOME.toString(),String.valueOf(limit)), new String[]{});
        int result = cursor.getCount();
        Log.d(TAG, "getTopHighest: Result: " + result);
        if(result <= 0) {
            return incomeList;
        }

        while(cursor.moveToNext()) {
            Income income = new Income();
            income.setParent(cursor.getString(cursor.getColumnIndex(INCOME.PARENT.toString())));
            income.setAmount(cursor.getString(cursor.getColumnIndex(INCOME.AMOUNT.toString())));
            income.setTimestamp(cursor.getString(cursor.getColumnIndex(INCOME.TIMESTAMP.toString())));
            incomeList.add(income);
        }
        sqLiteDatabase.close();
        cursor.close();
        return incomeList;
    }
}
