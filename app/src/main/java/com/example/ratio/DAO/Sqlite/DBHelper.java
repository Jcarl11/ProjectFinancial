package com.example.ratio.DAO.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PROJECT_TYPE;
import com.example.ratio.Enums.PROJECT_TYPE_SUBCATEGORY;
import com.example.ratio.Enums.SERVICES;
import com.example.ratio.Enums.STATUS;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "LocalStorage.db";
    private static final String objectId = DEFAULTS.objectId.toString();
    private static final String createdAt = DEFAULTS.createdAt.toString();
    private static final String updatedAt = DEFAULTS.updatedAt.toString();

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatus = String.format("CREATE TABLE IF NOT EXISTS %s" +
                "(%s character(10) PRIMARY KEY," +
                "%s datetime, " +
                "%s datetime, " +
                "%s varchar(255), " +
                "%s character(10))", PARSECLASS.STATUS.toString(),
                objectId, createdAt, updatedAt, STATUS.NAME.toString(), STATUS.PARENT.toString());

        String createServices = String.format("CREATE TABLE IF NOT EXISTS %s" +
                "(%s character(10) PRIMARY KEY," +
                "%s datetime, " +
                "%s datetime, " +
                "%s varchar(255), " +
                "%s integer)", PARSECLASS.SERVICES.toString(), objectId, createdAt, updatedAt,
                SERVICES.NAME.toString(), SERVICES.OTHERS.toString());

        String createProjectType = String.format("CREATE TABLE IF NOT EXISTS %s" +
                "(%s character(10) PRIMARY KEY, " +
                "%s datetime, " +
                "%s datetime, " +
                "%s varchar(255), " +
                "%s integer)", PARSECLASS.PROJECT_TYPE.toString(), objectId, createdAt, updatedAt,
                PROJECT_TYPE.NAME.toString(), PROJECT_TYPE.OTHERS.toString());

        String createSubcategory = String.format("CREATE TABLE IF NOT EXISTS %s" +
                        "(%s character(10) PRIMARY KEY, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s varchar(255), " +
                        "%s integer, " +
                        "%s varchar(255))", PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString(), objectId, createdAt, updatedAt,
                PROJECT_TYPE_SUBCATEGORY.NAME.toString(), PROJECT_TYPE_SUBCATEGORY.OTHERS.toString(),
                PROJECT_TYPE_SUBCATEGORY.PARENT.toString());

        db.execSQL(createStatus);
        db.execSQL(createServices);
        db.execSQL(createProjectType);
        db.execSQL(createSubcategory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 2) {
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY," +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s integer)", PARSECLASS.SERVICES.toString(), objectId, createdAt, updatedAt,
                    SERVICES.NAME.toString(), SERVICES.OTHERS.toString()));
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s integer)", PARSECLASS.PROJECT_TYPE.toString(), objectId, createdAt, updatedAt,
                    PROJECT_TYPE.NAME.toString(), PROJECT_TYPE.OTHERS.toString()));

        }
        if(oldVersion < 3) {
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s integer, " +
                            "%s varchar(255))", PARSECLASS.PROJECT_TYPE_SUBCATEGORY.toString(), objectId, createdAt, updatedAt,
                    PROJECT_TYPE_SUBCATEGORY.NAME.toString(), PROJECT_TYPE_SUBCATEGORY.OTHERS.toString(),
                    PROJECT_TYPE_SUBCATEGORY.PARENT.toString()));
        }
    }
}