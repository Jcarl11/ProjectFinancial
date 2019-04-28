package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Income;
import com.example.ratio.Enums.INCOME;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Utilities.DateTransform;
import com.example.ratio.Utilities.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO implements BaseDAO<Income> {
    private static final String TAG = "IncomeDAO";
    DateTransform dateTransform = new DateTransform();
    int result = 0;
    int defaultLimit = 50;
    boolean isSuccessful = false;
    ParseObject parseObject = null;

    @Override
    public int insert(Income objectEntity) {
        Log.d(TAG, "insert: Started...");
        int result = 0;
        ParseObject insertIncome = new ParseObject(PARSECLASS.INCOME.toString());
        insertIncome.put(INCOME.PARENT.toString(), objectEntity.getParent());
        insertIncome.put(INCOME.ATTACHMENTS.toString(), objectEntity.isAttachments());
        insertIncome.put(INCOME.TIMESTAMP.toString(), dateTransform.toISO8601Date(objectEntity.getTimestamp()));
        insertIncome.put(INCOME.AMOUNT.toString(), objectEntity.getAmount());
        insertIncome.put(INCOME.DESCRIPTION.toString(), objectEntity.getDescription());
        try {
            Log.d(TAG, "insert: Saving record...");
            insertIncome.save();
            result = 1;
            Log.d(TAG, "insert: Record saved!");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int insertAll(List<Income> objectList) {
        Log.d(TAG, "insertAll: Strated...");
        int result = 0;
        for(Income income : objectList) {
            result += insert(income);
        }
        Log.d(TAG, "insertAll: Result: " + result);
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - result));

        return result;
    }

    @Override
    public Income get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.INCOME.toString());
        try {
            Log.d(TAG, "get: Retriving object...");
            parseObject = query.get(objectId);
            Log.d(TAG, "get: Object retrieved: " + parseObject.getObjectId());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "get: Exception thrown: " + e.getMessage());
        }

        Income income = new Income();
        income.setObjectId(parseObject.getObjectId());
        income.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
        income.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
        income.setAmount(parseObject.getString(INCOME.AMOUNT.toString()));
        income.setAttachments(parseObject.getBoolean(INCOME.ATTACHMENTS.toString()));
        income.setDescription(parseObject.getString(INCOME.DESCRIPTION.toString()));
        income.setParent(parseObject.getString(INCOME.PARENT.toString()));
        income.setTimestamp(dateTransform.toDateString(parseObject.getDate(INCOME.TIMESTAMP.toString())));
        return income;
    }

    @Override
    public List<Income> getBulk(String sqlCommand) {
        Log.d(TAG, "getBulk: Started...");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit: " + defaultLimit);
        List<Income> incomeList = new ArrayList<>();
        ParseQuery<ParseObject> getbulk = ParseQuery.getQuery(PARSECLASS.INCOME.toString());
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving objects...");
            List<ParseObject> parseObjects = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseObject parseObject : parseObjects){
                Income income = new Income();
                income.setObjectId(parseObject.getObjectId());
                income.setCreatedAt(dateTransform.toDateString(parseObject.getCreatedAt()));
                income.setUpdatedAt(dateTransform.toDateString(parseObject.getUpdatedAt()));
                income.setAmount(parseObject.getString(INCOME.AMOUNT.toString()));
                income.setAttachments(parseObject.getBoolean(INCOME.ATTACHMENTS.toString()));
                income.setDescription(parseObject.getString(INCOME.DESCRIPTION.toString()));
                income.setParent(parseObject.getString(INCOME.PARENT.toString()));
                income.setTimestamp(dateTransform.toDateString(parseObject.getDate(INCOME.TIMESTAMP.toString())));
                incomeList.add(income);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return incomeList;
    }


    @Override
    public boolean update(Income newRecord) {
        Log.d(TAG, "update: Started...");
        isSuccessful = false;
        ParseObject incomeUpdate = new ParseObject(PARSECLASS.INCOME.toString());
        incomeUpdate.put(INCOME.TIMESTAMP.toString(), dateTransform.toISO8601Date(newRecord.getTimestamp()));
        incomeUpdate.put(INCOME.PARENT.toString(), newRecord.getParent());
        incomeUpdate.put(INCOME.DESCRIPTION.toString(), newRecord.getDescription());
        incomeUpdate.put(INCOME.ATTACHMENTS.toString(), newRecord.isAttachments());
        incomeUpdate.put(INCOME.AMOUNT.toString(), newRecord.getAmount());
        try {
            Log.d(TAG, "update: Saving record...");
            incomeUpdate.save();
            isSuccessful = true;
            Log.d(TAG, "update: Record saved!");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        return isSuccessful;
    }

    @Override
    public int delete(Income object) {
        Log.d(TAG, "delete: Started...");
        result = 0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.INCOME.toString());
        try {
            Log.d(TAG, "delete: Retrieving object...");
            parseObject = query.get(object.getObjectId());
            Log.d(TAG, "delete: Object retrieved");
            Log.d(TAG, "delete: Deleting object...");
            parseObject.delete();
            result = 1;
            Log.d(TAG, "delete: Record deleted...");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int deleteAll(List<Income> objectList) {
        Log.d(TAG, "deleteAll: Started...");
        int result = 0;
        for(Income income : objectList) {
            result += delete(income);
        }
        Log.d(TAG, "deleteAll: Result: " + result);
        Log.d(TAG, "deleteAll: Failed operations: " + String.valueOf(objectList.size() - result));

        return result;
    }
}
