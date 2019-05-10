package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.Income;
import com.example.ratio.Enums.INCOME;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class IncomeDAO implements BaseDAO<Income> {
    private static final String TAG = "IncomeDAO";
    private DateTransform dateTransform = new DateTransform();
    private int result = 0;
    private int defaultLimit = 50;
    private boolean isSuccessful = false;
    private ParseObject parseObject = null;

    @Override
    public Income insert(Income objectEntity) {
        Log.d(TAG, "insert: Started...");
        ParseObject insertIncome = new ParseObject(PARSECLASS.INCOME.toString());
        insertIncome.put(INCOME.PARENT.toString(), objectEntity.getParent());
        insertIncome.put(INCOME.ATTACHMENTS.toString(), objectEntity.isAttachments());
        insertIncome.put(INCOME.TIMESTAMP.toString(), dateTransform.toISO8601Date(objectEntity.getTimestamp()));
        insertIncome.put(INCOME.AMOUNT.toString(), objectEntity.getAmount());
        insertIncome.put(INCOME.DESCRIPTION.toString(), objectEntity.getDescription());
        try {
            Log.d(TAG, "insert: Saving record...");
            insertIncome.save();
            Log.d(TAG, "insert: Record saved!");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        Income income = new Income();
        income.setObjectId(insertIncome.getObjectId());
        income.setCreatedAt(dateTransform.toDateString(insertIncome.getCreatedAt()));
        income.setUpdatedAt(dateTransform.toDateString(insertIncome.getUpdatedAt()));
        income.setAmount(insertIncome.getString(INCOME.AMOUNT.toString()));
        income.setAttachments(insertIncome.getBoolean(INCOME.ATTACHMENTS.toString()));
        income.setDescription(insertIncome.getString(INCOME.DESCRIPTION.toString()));
        income.setParent(insertIncome.getString(INCOME.PARENT.toString()));
        income.setTimestamp(dateTransform.toDateString(insertIncome.getDate(INCOME.TIMESTAMP.toString())));
        return income;
    }

    @Override
    public int insertAll(List<Income> objectList) {
        Log.d(TAG, "insertAll: Strated...");
        List<String> ids = new ArrayList<>();
        for(Income income : objectList) {
            String id = insert(income).getObjectId();
            if(id != null) {
                ids.add(id);
            }
        }
        Log.d(TAG, "insertAll: Result: " + ids.size());
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(objectList.size() - ids.size()));

        return ids.size();
    }

    @Override
    public Income get(String objectId) {
        Log.d(TAG, "get: Started...");
        parseObject = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSECLASS.INCOME.toString());
        try {
            Log.d(TAG, "get: Retrieving object...");
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
    public Income update(Income newRecord) {
        Log.d(TAG, "update: Started...");
        ParseObject incomeUpdate = new ParseObject(PARSECLASS.INCOME.toString());
        incomeUpdate.put(INCOME.TIMESTAMP.toString(), dateTransform.toISO8601Date(newRecord.getTimestamp()));
        incomeUpdate.put(INCOME.PARENT.toString(), newRecord.getParent());
        incomeUpdate.put(INCOME.DESCRIPTION.toString(), newRecord.getDescription());
        incomeUpdate.put(INCOME.ATTACHMENTS.toString(), newRecord.isAttachments());
        incomeUpdate.put(INCOME.AMOUNT.toString(), newRecord.getAmount());
        try {
            Log.d(TAG, "update: Saving record...");
            incomeUpdate.save();
            Log.d(TAG, "update: Record saved!");
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "update: Exception thrown: " + e.getMessage());
        }
        Income income = new Income();
        income.setObjectId(incomeUpdate.getObjectId());
        income.setCreatedAt(dateTransform.toDateString(incomeUpdate.getCreatedAt()));
        income.setUpdatedAt(dateTransform.toDateString(incomeUpdate.getUpdatedAt()));
        income.setAmount(incomeUpdate.getString(INCOME.AMOUNT.toString()));
        income.setAttachments(incomeUpdate.getBoolean(INCOME.ATTACHMENTS.toString()));
        income.setDescription(incomeUpdate.getString(INCOME.DESCRIPTION.toString()));
        income.setParent(incomeUpdate.getString(INCOME.PARENT.toString()));
        income.setTimestamp(dateTransform.toDateString(incomeUpdate.getDate(INCOME.TIMESTAMP.toString())));
        return income;
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
