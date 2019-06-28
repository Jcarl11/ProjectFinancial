package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.EXPENSES;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.HelperClasses.DateTransform;

import java.util.ArrayList;
import java.util.List;

public class ExpensesDAO implements BaseDAO<Expenses>, GetFromParent<Expenses>, NukeOperations<Expenses> {
    private static final String TAG = "ExpensesDAO";
    private DateTransform dateTransform = new DateTransform();
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public ExpensesDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Expenses insert(Expenses objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(EXPENSES.PARENT.toString(), objectEntity.getParent());
        contentValues.put(EXPENSES.AMOUNT.toString(), objectEntity.getAmount());
        contentValues.put(EXPENSES.ATTACHMENTS.toString(), objectEntity.isAttachments());
        contentValues.put(EXPENSES.DESCRIPTION.toString(), objectEntity.getDescription());
        contentValues.put(EXPENSES.TIMESTAMP.toString(), objectEntity.getTimestamp());
        long result = sqLiteDatabase.insert(PARSECLASS.EXPENSES.toString(), null, contentValues);
        Log.d(TAG, "insert: Result: " + result);
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Expenses> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<Expenses> expensesList = new ArrayList<>();
        for(Expenses expenses : objectList) {
            Expenses result = insert(expenses);
            if(result != null) {
                expensesList.add(result);
            }
        }
        Log.d(TAG, "insertAll: Result: " + expensesList.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - expensesList.size()));
        return expensesList.size();
    }

    @Override
    public Expenses get(String objectId) {
        return null;
    }

    @Override
    public List<Expenses> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        List<Expenses> expensesList = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.EXPENSES.toString()), new String[]{});
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if(result <= 0) {
            return expensesList;
        }

        while(cursor.moveToNext()) {
            Expenses expenses = new Expenses();
            expenses.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            expenses.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            expenses.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            expenses.setParent(cursor.getString(cursor.getColumnIndex(EXPENSES.PARENT.toString())));
            expenses.setDescription(cursor.getString(cursor.getColumnIndex(EXPENSES.DESCRIPTION.toString())));
            expenses.setAmount(cursor.getString(cursor.getColumnIndex(EXPENSES.AMOUNT.toString())));
            expenses.setAttachments(cursor.getInt(cursor.getColumnIndex(EXPENSES.ATTACHMENTS.toString())) == 1 ? true : false);
            expenses.setTimestamp(cursor.getString(cursor.getColumnIndex(EXPENSES.TIMESTAMP.toString())));
            expensesList.add(expenses);
        }

        sqLiteDatabase.close();
        cursor.close();
        return expensesList;
    }

    @Override
    public Expenses update(Expenses newRecord) {
        return null;
    }

    @Override
    public int delete(Expenses object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Expenses> objectList) {
        return 0;
    }

    @Override
    public List<Expenses> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        List<Expenses> expensesList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s WHERE %s = ?",
                PARSECLASS.EXPENSES.toString(), EXPENSES.PARENT.toString()), new String[]{parentID});
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if(result <= 0) {
            return expensesList;
        }

        while(cursor.moveToNext()) {
            Expenses income = new Expenses();
            income.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            income.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            income.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            income.setParent(cursor.getString(cursor.getColumnIndex(EXPENSES.PARENT.toString())));
            income.setDescription(cursor.getString(cursor.getColumnIndex(EXPENSES.DESCRIPTION.toString())));
            income.setAmount(cursor.getString(cursor.getColumnIndex(EXPENSES.AMOUNT.toString())));
            income.setAttachments(cursor.getInt(cursor.getColumnIndex(EXPENSES.ATTACHMENTS.toString())) == 1 ? true : false);
            income.setTimestamp(cursor.getString(cursor.getColumnIndex(EXPENSES.TIMESTAMP.toString())));
            expensesList.add(income);
        }

        sqLiteDatabase.close();
        cursor.close();
        return expensesList;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.EXPENSES.toString(), "1", null);

        sqLiteDatabase.close();
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }
}
