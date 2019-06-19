package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.GetFromParent;
import com.example.ratio.Entities.Expenses;
import com.example.ratio.Enums.EXPENSES;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ExpensesDAO implements BaseDAO<Expenses>, GetFromParent<Expenses> {
    private static final String TAG = "ExpensesDAO";
    private DateTransform dateTransform = new DateTransform();
    private int result = 0;
    private int defaultLimit = 50;
    private boolean isSuccessful = false;
    private ParseObject parseObject = null;

    @Override
    public Expenses insert(Expenses objectEntity) {
        Log.d(TAG, "insert: Started...");
        ParseObject insertQuery = new ParseObject(PARSECLASS.EXPENSES.toString());
        insertQuery.put(EXPENSES.AMOUNT.toString(), objectEntity.getAmount());
        insertQuery.put(EXPENSES.ATTACHMENTS.toString(), objectEntity.isAttachments());
        insertQuery.put(EXPENSES.DESCRIPTION.toString(), objectEntity.getDescription());
        insertQuery.put(EXPENSES.PARENT.toString(), objectEntity.getParent());
        insertQuery.put(EXPENSES.TIMESTAMP.toString(), dateTransform.toISO8601Date(objectEntity.getTimestamp()));
        try {
            Log.d(TAG, "insert: Saving record...");
            insertQuery.save();
            Log.d(TAG, "insert: Record saved");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        Expenses expenses = new Expenses();
        expenses.setObjectId(insertQuery.getObjectId());
        expenses.setCreatedAt(dateTransform.toISO8601String(insertQuery.getCreatedAt()));
        expenses.setUpdatedAt(dateTransform.toISO8601String(insertQuery.getUpdatedAt()));
        expenses.setAmount(insertQuery.getString(EXPENSES.AMOUNT.toString()));
        expenses.setAttachments(insertQuery.getBoolean(EXPENSES.ATTACHMENTS.toString()));
        expenses.setDescription(insertQuery.getString(EXPENSES.DESCRIPTION.toString()));
        expenses.setParent(insertQuery.getString(EXPENSES.PARENT.toString()));
        expenses.setTimestamp(dateTransform.toISO8601String(insertQuery.getDate(EXPENSES.TIMESTAMP.toString())));
        return expenses;
    }

    @Override
    public int insertAll(List<Expenses> objectList) {
        Log.d(TAG, "insertAll: Started...");
        int result = 0;
        List<String> ids = new ArrayList<>();
        for(Expenses expenses : objectList){
            String id = insert(expenses).getObjectId();
            if(id != null) {
                ids.add(id);
            }
        }

        Log.d(TAG, "insertAll: Result: " + ids.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));
        return ids.size();
    }

    @Override
    public Expenses get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.EXPENSES.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Object retrieved: " + parseObject.getObjectId());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }
        Expenses expenses = new Expenses();
        expenses.setObjectId(parseObject.getObjectId());
        expenses.setCreatedAt(dateTransform.toISO8601String(parseObject.getCreatedAt()));
        expenses.setUpdatedAt(dateTransform.toISO8601String(parseObject.getUpdatedAt()));
        expenses.setAmount(parseObject.getString(EXPENSES.AMOUNT.toString()));
        expenses.setAttachments(parseObject.getBoolean(EXPENSES.ATTACHMENTS.toString()));
        expenses.setDescription(parseObject.getString(EXPENSES.DESCRIPTION.toString()));
        expenses.setParent(parseObject.getString(EXPENSES.PARENT.toString()));
        expenses.setTimestamp(dateTransform.toISO8601String(parseObject.getDate(EXPENSES.TIMESTAMP.toString())));
        return expenses;
    }

    @Override
    public List<Expenses> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Default limit: " + defaultLimit);
        List<Expenses> expensesList = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.EXPENSES.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving objects...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Expenses expenses = new Expenses();
                expenses.setObjectId(parseObject.getObjectId());
                expenses.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                expenses.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                expenses.setAmount(parseObject.getString(EXPENSES.AMOUNT.toString()));
                expenses.setAttachments(parseObject.getBoolean(EXPENSES.ATTACHMENTS.toString()));
                expenses.setDescription(parseObject.getString(EXPENSES.DESCRIPTION.toString()));
                expenses.setParent(parseObject.getString(EXPENSES.PARENT.toString()));
                expenses.setTimestamp(dateTransform.toISO8601String(parseObject.getDate(EXPENSES.TIMESTAMP.toString())));
                expensesList.add(expenses);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return expensesList;
    }

    @Override
    public Expenses update(Expenses newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject updateObject = new ParseObject(PARSECLASS.EXPENSES.toString());
        updateObject.put(EXPENSES.TIMESTAMP.toString(), dateTransform.toISO8601Date(newRecord.getTimestamp()));
        updateObject.put(EXPENSES.PARENT.toString(), newRecord.getParent());
        updateObject.put(EXPENSES.DESCRIPTION.toString(), newRecord.getDescription());
        updateObject.put(EXPENSES.ATTACHMENTS.toString(), newRecord.isAttachments());
        updateObject.put(EXPENSES.AMOUNT.toString(), newRecord.getAmount());
        try {
            Log.d(TAG, "update: Saving object...");
            updateObject.save();
            Log.d(TAG, "update: Save successful");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        Expenses expenses = new Expenses();
        expenses.setObjectId(updateObject.getObjectId());
        expenses.setCreatedAt(dateTransform.toISO8601String(updateObject.getCreatedAt()));
        expenses.setUpdatedAt(dateTransform.toISO8601String(updateObject.getUpdatedAt()));
        expenses.setAmount(updateObject.getString(EXPENSES.AMOUNT.toString()));
        expenses.setAttachments(updateObject.getBoolean(EXPENSES.ATTACHMENTS.toString()));
        expenses.setDescription(updateObject.getString(EXPENSES.DESCRIPTION.toString()));
        expenses.setParent(updateObject.getString(EXPENSES.PARENT.toString()));
        expenses.setTimestamp(dateTransform.toISO8601String(updateObject.getDate(EXPENSES.TIMESTAMP.toString())));
        return expenses;
    }

    @Override
    public int delete(Expenses object) {
        Log.d(TAG, "delete: Started...");
        int result = 0;
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.EXPENSES.toString());
        try {
            Log.d(TAG, "delete: Retrieving object...");
            parseObject = query.get(object.getObjectId());
            Log.d(TAG, "delete: Object retrieved");
            Log.d(TAG, "delete: Deleting now...");
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
    public int deleteAll(List<Expenses> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for(Expenses expenses : objectList) {
            result += delete(expenses);
        }
        Log.d(TAG, "deleteAll: Result: "+ result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));

        return result;
    }

    @Override
    public List<Expenses> getObjects(String parentID) {
        Log.d(TAG, "getObjects: Started...");
        List<Expenses> expensesList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.EXPENSES.toString());
        query.whereEqualTo(EXPENSES.PARENT.toString(), parentID);
        query.setLimit(1000);
        try {
            Log.d(TAG, "getObjects: Retrieving objects");
            List<ParseObject> parseObjects = query.find();
            Log.d(TAG, "getObjects: Retrieved objects: " + parseObjects.size());
            for(ParseObject parseObject : parseObjects){
                Expenses expenses = new Expenses();
                expenses.setObjectId(parseObject.getObjectId());
                expenses.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                expenses.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                expenses.setAmount(parseObject.getString(EXPENSES.AMOUNT.toString()));
                expenses.setAttachments(parseObject.getBoolean(EXPENSES.ATTACHMENTS.toString()));
                expenses.setDescription(parseObject.getString(EXPENSES.DESCRIPTION.toString()));
                expenses.setParent(parseObject.getString(EXPENSES.PARENT.toString()));
                expenses.setTimestamp(dateTransform.toISO8601String(parseObject.getDate(EXPENSES.TIMESTAMP.toString())));
                expensesList.add(expenses);
            }
        } catch (ParseException e) {
            Log.d(TAG, "getObjects: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return expensesList;
    }
}
