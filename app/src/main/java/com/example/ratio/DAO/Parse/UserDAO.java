package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.PARSECLASS;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements BaseDAO<User> {
    private static final String TAG = "UserDAO";
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    ParseUser parseUser = null;
    int result = 0;
    int defaultLimit = 50;
    @Override
    public int insert(User objectEntity) {
        Log.d(TAG, "insert: inserting user " + objectEntity.getObjectId());
        result = -1;
        ParseUser register = new ParseUser();
        register.setEmail(objectEntity.getEmail());
        register.setUsername(objectEntity.getUsername());
        register.setPassword(objectEntity.getPassword());
        try {
            Log.d(TAG, "insert: inserting...");
            register.signUp();
            Log.d(TAG, "insert: Signup done");
            Log.d(TAG, "insert: Logging out account");
            ParseUser.logOut();
            Log.d(TAG, "insert: logout done");
            result = 1;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int insertAll(List<User> objectList) {
        Log.d(TAG, "insertAll: inserting bulk: " + String.valueOf(objectList.size()));
        result = 0;
        for(User user : objectList){
            ParseUser parseUser = new ParseUser();
            parseUser.setEmail(user.getEmail());
            parseUser.setUsername(user.getUsername());
            parseUser.setPassword(user.getPassword());
            try {
                Log.d(TAG, "insertAll: inserting...");
                parseUser.signUp();
                Log.d(TAG, "insertAll: account inserted");
                Log.d(TAG, "insertAll: Logging out...");
                ParseUser.logOut();
                result++;
                Log.d(TAG, "insertAll: Log out done");

            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "insertAll: Exception thrown: " + e.getMessage());
            }
        }

        int failedOperations = objectList.size() - result;
        Log.d(TAG, "insertAll: Failed operations: " + String.valueOf(failedOperations));
        return result;
    }

    @Override
    public User get(String objectId) {
        Log.d(TAG, "get: getting user " + objectId);
        parseUser = null;
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        try {
            Log.d(TAG, "get: Getting user...");
            parseUser = userQuery.get(objectId);
            Log.d(TAG, "get: User retrieved" + parseUser.getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        User userEntity = new User();
        userEntity.setObjectId(parseUser.getObjectId());
        userEntity.setCreatedAt(dateFormat.format(parseUser.getCreatedAt()));
        userEntity.setUpdatedAt(dateFormat.format(parseUser.getUpdatedAt()));
        userEntity.setEmail(parseUser.getEmail());
        userEntity.setUsername(parseUser.getUsername());

        return userEntity;
    }

    @Override
    public List<User> getBulk(String sqlCommand) { // SQL Command can be nullable, Applicable only to Sqlite, Can also be used for limiting the number of result
        Log.d(TAG, "getBulk: started");
        defaultLimit = checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50;
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<User> userEntityList = new ArrayList<>();
        ParseQuery<ParseUser> getbulk = ParseUser.getQuery();
        getbulk.setLimit(defaultLimit); // default limit of 50 rows
        try {
            Log.d(TAG, "getBulk: Retrieving users...");
            List<ParseUser> userList = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseUser users : userList){
                User user = new User();
                user.setObjectId(users.getObjectId());
                user.setCreatedAt(dateFormat.format(users.getCreatedAt()));
                user.setUpdatedAt(dateFormat.format(users.getUpdatedAt()));
                user.setEmail(users.getEmail());
                user.setUsername(users.getUsername());
                userEntityList.add(user);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "getBulk: Exception thrown: " + e.getMessage());
        }
        return userEntityList;
    }


    @Override
    public boolean update(User oldRecord, User newRecord) {
        boolean isSuccessful = false;
        if(newRecord.getEmail() != null){
            try {
                Log.d(TAG, "update: sending request...");
                ParseUser.requestPasswordReset(newRecord.getEmail());
                isSuccessful = true;
                Log.d(TAG, "update: change password request sent");
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TAG, "update: Exception thrown: " + e.getMessage());
            }
        }
        return isSuccessful;
    }

    @Override
    public int delete(User object) {
        try {
            throw new Exception("No implementation for this method");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteAll(List<User> objectList) {
        try {
            throw new Exception("No implementation for this method");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean checkIfInteger(String input){
        boolean isInteger = false;
        try{
            Integer.parseInt(input);
            isInteger = true;
        }catch(Exception ex){
            Log.d(TAG, "checkIfInteger: Exception thrown: " + ex.getMessage());
        }
        return isInteger;
    }
}
