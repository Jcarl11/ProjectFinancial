package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Pdf;
import com.example.ratio.Enums.DATABASES;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PDF;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.ParseFileOperation;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class FileDAO implements BaseDAO<Pdf>, GetFromParent<Pdf> {

    private static final String TAG = "FileDAO";
    private DateTransform dateTransform = new DateTransform();
    private ParseFileOperation parseFileOperation = new ParseFileOperation();
    private int defaultLimit = 50;
    @Override
    public Pdf insert(Pdf objectEntity) {
        Log.d(TAG, "insert: Started...");
        Pdf pdf = new Pdf();
        ParseObject parseObject = new ParseObject(PARSECLASS.PDF.toString());
        parseObject.put(PDF.FILENAME.toString(), objectEntity.getFileName());
        parseObject.put(PDF.PARENT.toString(), objectEntity.getParent());
        parseObject.put(PDF.DELETED.toString(), false);
        parseObject.put(PDF.FILES.toString(), parseFileOperation.fromFile(new File(objectEntity.getFilePath())));
        try {
            Log.d(TAG, "insert: Saving record...");
            parseObject.save();
            Log.d(TAG, "insert: Record Saved");
            objectEntity.setObjectId(parseObject.getObjectId());
            objectEntity.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
            objectEntity.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
            objectEntity.setFilePath(parseObject.getParseFile(PDF.FILES.toString()).getUrl());
        } catch (ParseException e) {
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return objectEntity;
    }

    @Override
    public int insertAll(List<Pdf> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> ids = new ArrayList<>();
        for(Pdf pdf : objectList) {
            String id = insert(pdf).getObjectId();
            if(id != null) {
                ids.add(id);
            }
        }
        Log.d(TAG, "insertAll: Result: " + ids.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()) );
        return ids.size();
    }

    @Override
    public Pdf get(String objectId) {
        Log.d(TAG, "get: Started...");
        ParseObject parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PDF.toString());
        try {
            Log.d(TAG, "get: retrieving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Record retrieved: " + parseObject.getObjectId());
        } catch (ParseException e) {
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }

        Pdf pdf = new Pdf();
        pdf.setObjectId(parseObject.getObjectId());
        pdf.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
        pdf.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
        pdf.setParent(parseObject.getString(PDF.PARENT.toString()));
        pdf.setFileName(parseObject.getString(PDF.FILENAME.toString()));
        pdf.setDeleted(parseObject.getBoolean(PDF.DELETED.toString()));
        pdf.setFilePath(parseObject.getParseFile(PDF.FILES.toString()).getUrl());
        return pdf;
    }

    @Override
    public List<Pdf> getBulk(@Nullable String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        List<Pdf> pdfList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PDF.toString());
        query.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Fetching objects");
            List<ParseObject> parseObjectList = query.find();
            Log.d(TAG, "getBulk: PDF Files fetched: " + parseObjectList.size());
            for(ParseObject parseObject : parseObjectList) {
                Pdf pdf = new Pdf();
                pdf.setObjectId(parseObject.getObjectId());
                pdf.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                pdf.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                pdf.setParent(parseObject.getString(PDF.PARENT.toString()));
                pdf.setFileName(parseObject.getString(PDF.FILENAME.toString()));
                pdf.setDeleted(parseObject.getBoolean(PDF.DELETED.toString()));
                pdf.setFilePath(parseObject.getParseFile(PDF.FILES.toString()).getUrl());
                pdfList.add(pdf);
            }

        } catch (ParseException e) {
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage() );
            e.printStackTrace();
        }
        return pdfList;
    }

    @Override
    public Pdf update(Pdf newRecord) {
        Log.d(TAG, "update: Started..");
        ParseObject parseObject = new ParseObject(PARSECLASS.PDF.toString());
        parseObject.put(PDF.FILENAME.toString(), newRecord.getFileName());
        parseObject.put(PDF.PARENT.toString(), newRecord.getParent());
        parseObject.put(PDF.DELETED.toString(), false);
        parseObject.put(PDF.FILES.toString(), parseFileOperation.fromFile(new File(newRecord.getFilePath())));
        try {
            Log.d(TAG, "update: Updating record...");
            parseObject.save();
            Log.d(TAG, "update: Record updated");
            newRecord.setObjectId(parseObject.getObjectId());
            newRecord.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
            newRecord.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
            newRecord.setFilePath(parseObject.getParseFile(PDF.FILES.toString()).getUrl());
        } catch (ParseException e) {
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(Pdf object) {
        Log.d(TAG, "delete: Started...");
        ParseObject parseObject = null;
        int result = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PDF.toString());
        try {
            Log.d(TAG, "delete: retrieving object...");
            parseObject = query.get(object.getObjectId());
            Log.d(TAG, "delete: Object Retrieved: " + parseObject.getObjectId());
            Log.d(TAG, "delete: Deleting object...");
            parseObject.delete();
            result = 1;
            Log.d(TAG, "delete: Object deleted");
        } catch (ParseException e) {
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int deleteAll(List<Pdf> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for(Pdf pdf : objectList) {
            result += delete(pdf);
        }

        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed Operations: " + String.valueOf(objectList.size() - result));
        return result;
    }

    @Override
    public List<Pdf> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started...");
        List<Pdf> pdfList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.PDF.toString());
        query.whereEqualTo(PDF.PARENT.toString(), parentID);
        try {
            Log.d(TAG, "getObjects: Fetching objects");
            List<ParseObject> parseObjectList = query.find();
            Log.d(TAG, "getObjects: PDF Files fetched: " + parseObjectList.size());
            for(ParseObject parseObject : parseObjectList) {
                Pdf pdf = new Pdf();
                pdf.setObjectId(parseObject.getObjectId());
                pdf.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                pdf.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                pdf.setParent(parseObject.getString(PDF.PARENT.toString()));
                pdf.setFileName(parseObject.getString(PDF.FILENAME.toString()));
                pdf.setDeleted(parseObject.getBoolean(PDF.DELETED.toString()));
                pdf.setFilePath(parseObject.getParseFile(PDF.FILES.toString()).getUrl());
                pdfList.add(pdf);
            }

        } catch (ParseException e) {
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage() );
            e.printStackTrace();
        }
        return pdfList;
    }
}
