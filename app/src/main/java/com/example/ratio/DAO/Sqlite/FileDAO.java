package com.example.ratio.DAO.Sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ratio.ContextApplication;
import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.DAO.NukeOperations;
import com.example.ratio.Entities.Pdf;
import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PDF;
import com.example.ratio.HelperClasses.DateTransform;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class FileDAO implements BaseDAO<Pdf>, GetFromParent<Pdf>, NukeOperations<Pdf> {
    private static final String TAG = "FileDAO";
    private DateTransform dateTransform = new DateTransform();
    private SQLiteDatabase sqLiteDatabase = null;
    private DBHelper dbHelper = null;

    public FileDAO() {
        dbHelper = new DBHelper(ContextApplication.getContext());
    }

    @Override
    public Pdf insert(Pdf objectEntity) {
        Log.d(TAG, "insert: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DEFAULTS.objectId.toString(), objectEntity.getObjectId());
        contentValues.put(DEFAULTS.createdAt.toString(), objectEntity.getCreatedAt());
        contentValues.put(DEFAULTS.updatedAt.toString(), objectEntity.getUpdatedAt());
        contentValues.put(PDF.PARENT.toString(), objectEntity.getParent());
        contentValues.put(PDF.FILENAME.toString(), objectEntity.getFileName());
        contentValues.put(PDF.DELETED.toString(), objectEntity.isDeleted());
        contentValues.put(PDF.FILES.toString(), objectEntity.getFilePath());
        long result = sqLiteDatabase.insert(PARSECLASS.PDF.toString(), null, contentValues);
        sqLiteDatabase.close();
        Log.d(TAG, "insert: Result: " + result);
        return result <= 0 ? null : objectEntity;
    }

    @Override
    public int insertAll(List<Pdf> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> ids = new ArrayList<>();
        for(Pdf pdf : objectList) {
            String id = insert(pdf).getObjectId();
            if(id != null)
                ids.add(id);
        }
        Log.d(TAG, "insertAll: Result: " + ids.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));
        return ids.size();
    }

    @Override
    public Pdf get(String objectId) {
        return null;
    }

    @Override
    public List<Pdf> getBulk(@Nullable String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT * FROM %s", PARSECLASS.PDF.toString()), new String[]{});
        List<Pdf> pdfList = new ArrayList<>();
        int result = cursor.getCount();
        Log.d(TAG, "getBulk: Result: " + result);
        if ( result <= 0 ) {
            return pdfList;
        }

        while(cursor.moveToNext()) {
            Pdf pdf = new Pdf();
            pdf.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            pdf.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            pdf.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            pdf.setFileName(cursor.getString(cursor.getColumnIndex(PDF.FILENAME.toString())));
            pdf.setDeleted(cursor.getInt(cursor.getColumnIndex(PDF.DELETED.toString())) == 1 ? true : false);
            pdf.setParent(cursor.getString(cursor.getColumnIndex(PDF.PARENT.toString())));
            pdf.setFilePath(cursor.getString(cursor.getColumnIndex(PDF.FILES.toString())));
            pdfList.add(pdf);
        }

        sqLiteDatabase.close();
        cursor.close();

        return pdfList;
    }

    @Override
    public Pdf update(Pdf newRecord) {
        return null;
    }

    @Override
    public int delete(Pdf object) {
        return 0;
    }

    @Override
    public int deleteAll(List<Pdf> objectList) {
        return 0;
    }

    @Override
    public List<Pdf> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started..");
        List<Pdf> pdfList = new ArrayList<>();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                String.format("SELECT * FROM %s WHERE %s = ?", PARSECLASS.PDF.toString(), PDF.PARENT.toString()), new String[]{parentID});
        int result = cursor.getCount();
        Log.d(TAG, "getObjects: Result: " + result);
        if(result <= 0) {
            return pdfList;
        }

        while(cursor.moveToNext()) {
            Pdf pdf = new Pdf();
            pdf.setObjectId(cursor.getString(cursor.getColumnIndex(DEFAULTS.objectId.toString())));
            pdf.setCreatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.createdAt.toString())));
            pdf.setUpdatedAt(cursor.getString(cursor.getColumnIndex(DEFAULTS.updatedAt.toString())));
            pdf.setFileName(cursor.getString(cursor.getColumnIndex(PDF.FILENAME.toString())));
            pdf.setFilePath(cursor.getString(cursor.getColumnIndex(PDF.FILES.toString())));
            pdf.setParent(cursor.getString(cursor.getColumnIndex(PDF.PARENT.toString())));
            pdf.setDeleted(cursor.getInt(cursor.getColumnIndex(PDF.DELETED.toString())) == 1 ? true : false);
            pdfList.add(pdf);
        }
        sqLiteDatabase.close();
        cursor.close();
        return pdfList;
    }

    @Override
    public int deleteRows() {
        Log.d(TAG, "deleteRows: Started...");
        sqLiteDatabase = dbHelper.getWritableDatabase();
        int deletedRows = sqLiteDatabase.delete(PARSECLASS.PDF.toString(), "1", null);

        sqLiteDatabase.close();
        return deletedRows;
    }

    @Override
    public boolean dropTable() {
        return false;
    }
}
