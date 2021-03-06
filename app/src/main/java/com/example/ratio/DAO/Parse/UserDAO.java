package com.example.ratio.DAO.Parse;

import android.util.Log;

import com.example.ratio.DAO.BaseDAO;
import com.example.ratio.DAO.UserOperations;
import com.example.ratio.Entities.User;
import com.example.ratio.Enums.USERINFO;
import com.example.ratio.HelperClasses.Constant;
import com.example.ratio.HelperClasses.DateTransform;
import com.example.ratio.HelperClasses.Utility;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class UserDAO implements BaseDAO<User>, UserOperations<User> {
    private static final String TAG = "UserDAO";
    private DateTransform dateTransform;
    private ParseUser parseUser = null;
    private int result = 0;
    private int defaultLimit = 50;

    public UserDAO() {
        dateTransform = new DateTransform();
    }

    @Override
    public User insert(User objectEntity) {
        Log.d(TAG, "insert: inserting user " + objectEntity.getObjectId());
        User userEntity = new User();
        ParseUser register = new ParseUser();
        register.setEmail(objectEntity.getEmail());
        register.setUsername(objectEntity.getUsername());
        register.setPassword(objectEntity.getPassword());
        register.put(USERINFO.POSITION.toString(), Constant.PENDING);
        try {
            Log.d(TAG, "insert: inserting...");
            register.signUp();
            Log.d(TAG, "insert: Signup done");
            userEntity.setObjectId(register.getObjectId());
            userEntity.setCreatedAt(dateTransform.toISO8601String(register.getCreatedAt()));
            userEntity.setUpdatedAt(dateTransform.toISO8601String(register.getUpdatedAt()));
            userEntity.setEmail(register.getEmail());
            userEntity.setUsername(register.getUsername());
            Log.d(TAG, "insert: Logging out account");
            ParseUser.logOut();
            Log.d(TAG, "insert: logout done");
        } catch (ParseException e) {
            Log.d(TAG, "insert: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return userEntity;
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
        userEntity.setCreatedAt(dateTransform.toISO8601String(parseUser.getCreatedAt()));
        userEntity.setUpdatedAt(dateTransform.toISO8601String(parseUser.getUpdatedAt()));
        userEntity.setEmail(parseUser.getEmail());
        userEntity.setUsername(parseUser.getUsername());

        return userEntity;
    }

    @Override
    public List<User> getBulk(@Nullable String sqlCommand) { // this parameter can be nullable, Applicable only to Sqlite, Can also be used for limiting the number of result
        Log.d(TAG, "getBulk: started");
        defaultLimit = Utility.getInstance().checkIfInteger(sqlCommand) == true ? Integer.valueOf(sqlCommand) : 50; // check sqlCommand if integer then convert, if not assign default limit to 50(No change)
        Log.d(TAG, "getBulk: Limit set to " + String.valueOf(defaultLimit));
        List<User> userEntityList = new ArrayList<>();
        ParseQuery<ParseUser> getbulk = ParseUser.getQuery();
        getbulk.setLimit(defaultLimit);
        try {
            Log.d(TAG, "getBulk: Retrieving users...");
            List<ParseUser> userList = getbulk.find();
            Log.d(TAG, "getBulk: Retrieval finished");
            for(ParseUser users : userList){
                User user = new User();
                user.setObjectId(users.getObjectId());
                user.setCreatedAt(dateTransform.toISO8601String(parseUser.getCreatedAt()));
                user.setUpdatedAt(dateTransform.toISO8601String(parseUser.getUpdatedAt()));
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
    public User update(User newRecord) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if(!parseUser.getString(USERINFO.POSITION.toString()).equalsIgnoreCase(newRecord.getUserinfo().getPosition())) {
            Log.d(TAG, "update: Different");
            Log.d(TAG, "update: POSITION: " + parseUser.getString(USERINFO.POSITION.toString()));
            parseUser.put(USERINFO.POSITION.toString(), newRecord.getUserinfo().getPosition());
            try {
                Log.d(TAG, "update: Saving...");
                parseUser.save();
                Log.d(TAG, "update: Saved");
            } catch (ParseException e) {
                Log.d(TAG, "update: Exception thrown: " + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.d(TAG, "update: POS: " + parseUser.getString(USERINFO.POSITION.toString()));
        newRecord.setObjectId(ParseUser.getCurrentUser().getObjectId());
        newRecord.setCreatedAt(dateTransform.toISO8601String(ParseUser.getCurrentUser().getCreatedAt()));
        newRecord.setUpdatedAt(dateTransform.toISO8601String(ParseUser.getCurrentUser().getUpdatedAt()));
        newRecord.setEmail(ParseUser.getCurrentUser().getEmail());
        newRecord.setUsername(ParseUser.getCurrentUser().getUsername());
        return newRecord;
    }

    @Override
    public int delete(User object) {
        int result = 0;
        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            ParseUser parseUser = query.get(object.getObjectId());
            Log.d(TAG, "delete: Deleting...");
            parseUser.delete();
            Log.d(TAG, "delete: Deleted Successfully");
            result = 1;
        } catch (Exception e) {
            Log.d(TAG, "delete: Exception thrown: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
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

    @Override
    public User loginUser(User userObject) {
        Log.d(TAG, "loginUser: Started...");
        ParseUser user = null;
        User userEntity = null;
        try {
            Log.d(TAG, "loginUser: Logging in user...");
            user = ParseUser.logIn(userObject.getUsername(), userObject.getPassword());
            Log.d(TAG, "loginUser: User logged in: " + user.getObjectId());
            Log.d(TAG, "loginUser: User logged in: " + user.getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "loginUser: Exception thrown: " + e.getMessage());
        }
        if(user != null){
            userEntity = new User();
            userEntity.setObjectId(user.getObjectId());
            userEntity.setCreatedAt(dateTransform.toISO8601String(user.getCreatedAt()));
            userEntity.setUpdatedAt(dateTransform.toISO8601String(user.getUpdatedAt()));
            userEntity.setUsername(user.getUsername());
            userEntity.setEmail(user.getEmail());
        }
        return userEntity;
    }

    @Override
    public void logoutUser() {
        Log.d(TAG, "logoutUser: Logging out user...");
        ParseUser.logOut();
        Log.d(TAG, "logoutUser: Logout finished");
    }
}
