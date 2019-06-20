package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Image;
import com.example.ratio.Enums.IMAGES;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.ImageCompressor;
import com.example.ratio.HelperClasses.ImageUtils;
import com.example.ratio.HelperClasses.ParseFileOperation;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO implements BaseDAO<Image>, GetFromParent<Image> {
    private static final String TAG = "ImageDAO";
    private DateTransform dateTransform = new DateTransform();
    private ParseFileOperation parseFileOperation = new ParseFileOperation();
    private int defaultLimit = 50;
    private ImageUtils imageUtils = new ImageUtils();
    private ParseObject parseObject = null;
    private File downscaledImage = null;

    @Override
    public Image insert(Image objectEntity) {
        Log.d(TAG, "insert: Started...");
        Image image = new Image();
        ParseObject insert = new ParseObject(PARSECLASS.IMAGES.toString());
        insert.put(IMAGES.PARENT.toString(), objectEntity.getParent());
        insert.put(IMAGES.FILENAME.toString(), objectEntity.getFileName());
        File compressedImage = ImageCompressor.getInstance().compressToFile(new File(objectEntity.getFilePath()));
        downscaledImage = imageUtils.resize(compressedImage, 500);
        insert.put(IMAGES.FILES.toString(), parseFileOperation.fromFile(downscaledImage));
        insert.put(IMAGES.DELETED.toString(), false);
        try {
            Log.d(TAG, "insert: Saving...");
            insert.save();
            Log.d(TAG, "insert: Record saved...");
            image.setObjectId(insert.getObjectId());
            image.setCreatedAt(dateTransform.toDateString(insert.getCreatedAt()));
            image.setUpdatedAt(dateTransform.toDateString(insert.getUpdatedAt()));
            image.setParent(insert.getString(IMAGES.PARENT.toString()));
            image.setFileName(insert.getString(IMAGES.FILENAME.toString()));
            image.setDeleted(insert.getBoolean(IMAGES.DELETED.toString()));
            image.setFilePath(insert.getParseFile(IMAGES.FILES.toString()).getUrl());
            imageUtils.deleteFile(downscaledImage);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        return image;
    }

    @Override
    public int insertAll(List<Image> objectList) {
        Log.d(TAG, "insertAll: Started...");
        List<String> ids =  new ArrayList<>();
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
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.IMAGES.toString());
        try {
            Log.d(TAG, "get: retrieving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Record retrieved: " + parseObject.getObjectId());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }

        Image image = new Image();
        image.setObjectId(parseObject.getObjectId());
        image.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
        image.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
        image.setParent(parseObject.getString(IMAGES.PARENT.toString()));
        image.setFileName(parseObject.getString(IMAGES.FILENAME.toString()));
        image.setDeleted(parseObject.getBoolean(IMAGES.DELETED.toString()));
        image.setFilePath(parseObject.getParseFile(IMAGES.FILES.toString()).getUrl());
        return image;
    }

    @Override
    public List<Image> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        List<Image> imageList = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.IMAGES.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving objects...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Image image = new Image();
                image.setObjectId(parseObject.getObjectId());
                image.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                image.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                image.setParent(parseObject.getString(IMAGES.PARENT.toString()));
                image.setFileName(parseObject.getString(IMAGES.FILENAME.toString()));
                image.setDeleted(parseObject.getBoolean(IMAGES.DELETED.toString()));
                image.setFilePath(parseObject.getParseFile(IMAGES.FILES.toString()).getUrl());
                imageList.add(image);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return imageList;
    }

    @Override
    public Image update(Image newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject parseObject = new ParseObject(PARSECLASS.IMAGES.toString());
        parseObject.put(IMAGES.PARENT.toString(), newRecord.getParent());
        parseObject.put(IMAGES.FILENAME.toString(), newRecord.getFileName());
        parseObject.put(IMAGES.DELETED.toString(), newRecord.isDeleted());
        parseObject.put(IMAGES.FILES.toString(), parseFileOperation.fromFile(new File(newRecord.getFilePath())));
        try {
            Log.d(TAG, "update: Saving...");
            parseObject.save();
            Log.d(TAG, "update: Record saved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        Image image = new Image();
        image.setObjectId(parseObject.getObjectId());
        image.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
        image.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
        image.setParent(parseObject.getString(IMAGES.PARENT.toString()));
        image.setFileName(parseObject.getString(IMAGES.FILENAME.toString()));
        image.setDeleted(parseObject.getBoolean(IMAGES.DELETED.toString()));
        image.setFilePath(parseObject.getParseFile(IMAGES.FILES.toString()).getUrl());
        return image;
    }

    @Override
    public int delete(Image object) {
        Log.d(TAG, "delete: Started...");
        parseObject = null;
        int result = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.IMAGES.toString());
        try {
            Log.d(TAG, "delete: retrieving object...");
            parseObject = query.get(object.getObjectId());
            Log.d(TAG, "delete: Object Retrieved: " + parseObject.getObjectId());
            Log.d(TAG, "delete: Deleting object...");
            parseObject.delete();
            result = 1;
            Log.d(TAG, "delete: Object deleted");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int deleteAll(List<Image> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for(Image image : objectList) {
            result += delete(image);
        }

        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed Operations: " + String.valueOf(objectList.size() - result));
        return result;
    }

    @Override
    public List<Image> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started...");
        List<Image> imageList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.IMAGES.toString());
        query.whereEqualTo(IMAGES.PARENT.toString(), parentID);
        try {
            Log.d(TAG, "getBulk: Retrieving objects...");
            List<ParseObject> parseObjects = query.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Image image = new Image();
                image.setObjectId(parseObject.getObjectId());
                image.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                image.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                image.setParent(parseObject.getString(IMAGES.PARENT.toString()));
                image.setFileName(parseObject.getString(IMAGES.FILENAME.toString()));
                image.setDeleted(parseObject.getBoolean(IMAGES.DELETED.toString()));
                image.setFilePath(parseObject.getParseFile(IMAGES.FILES.toString()).getUrl());
                imageList.add(image);
            }
        } catch (ParseException e) {
            Log.d(TAG, "getObjects: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return imageList;
    }
}
