package com.example.ratio.DAO.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ratio.Enums.DEFAULTS;
import com.example.ratio.Enums.EXPENSES;
import com.example.ratio.Enums.IMAGES;
import com.example.ratio.Enums.INCOME;
import com.example.ratio.Enums.PARSECLASS;
import com.example.ratio.Enums.PDF;
import com.example.ratio.Enums.PROJECT;
import com.example.ratio.Enums.PROJECT_TYPE;
import com.example.ratio.Enums.PROJECT_TYPE_SUBCATEGORY;
import com.example.ratio.Enums.RECIEVABLES;
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
        super(context, DB_NAME, null, 6);
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

        String projects = String.format("CREATE TABLE IF NOT EXISTS %s" +
                        "(%s character(10) PRIMARY KEY, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s varchar(255), " +
                        "%s integer, " +
                        "%s varchar(255), " +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s varchar(255))", PARSECLASS.PROJECT.toString(), objectId, createdAt, updatedAt,
                PROJECT.PROJECT_CODE.toString(), PROJECT.DELETED.toString(),
                PROJECT.PROJECT_TITLE.toString(), PROJECT.PROJECT_OWNER.toString(), PROJECT.TYPE.toString(),
                PROJECT.SERVICES.toString(),PROJECT.SUBCATEGORY.toString());

        String createImages = String.format("CREATE TABLE IF NOT EXISTS %s" +
                        "(%s character(10) PRIMARY KEY, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s varchar(255), " +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s integer)", PARSECLASS.IMAGES.toString(), objectId, createdAt, updatedAt,
                IMAGES.PARENT.toString(), IMAGES.FILENAME.toString(), IMAGES.FILES.toString(), IMAGES.DELETED.toString());

        String createPdf = String.format("CREATE TABLE IF NOT EXISTS %s" +
                        "(%s character(10) PRIMARY KEY, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s varchar(255), " +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s integer)", PARSECLASS.PDF.toString(), objectId, createdAt, updatedAt,
                PDF.PARENT.toString(), PDF.FILENAME.toString(), PDF.FILES.toString(), PDF.DELETED.toString());
        String createIncome = String.format("CREATE TABLE IF NOT EXISTS %s" +
                        "(%s character(10) PRIMARY KEY, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s varchar(255), " +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s integer, " +
                        "%s datetime)", PARSECLASS.INCOME.toString(), objectId, createdAt, updatedAt,
                INCOME.PARENT.toString(), INCOME.DESCRIPTION.toString(), INCOME.AMOUNT.toString(),
                INCOME.ATTACHMENTS.toString(), INCOME.TIMESTAMP.toString());

        String createExpenses = String.format("CREATE TABLE IF NOT EXISTS %s" +
                        "(%s character(10) PRIMARY KEY, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s varchar(255), " +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s integer, " +
                        "%s datetime)", PARSECLASS.EXPENSES.toString(), objectId, createdAt, updatedAt,
                EXPENSES.PARENT.toString(), EXPENSES.DESCRIPTION.toString(), EXPENSES.AMOUNT.toString(),
                EXPENSES.ATTACHMENTS.toString(), EXPENSES.TIMESTAMP.toString());

        String createReceivables = String.format("CREATE TABLE IF NOT EXISTS %s" +
                        "(%s character(10) PRIMARY KEY, " +
                        "%s datetime, " +
                        "%s datetime, " +
                        "%s varchar(255), " +
                        "%s varchar(255)," +
                        "%s varchar(255)," +
                        "%s integer, " +
                        "%s datetime)", PARSECLASS.RECEIVABLES.toString(), objectId, createdAt, updatedAt,
                RECIEVABLES.PARENT.toString(), RECIEVABLES.DESCRIPTION.toString(), RECIEVABLES.AMOUNT.toString(),
                RECIEVABLES.ATTACHMENTS.toString(), RECIEVABLES.TIMESTAMP.toString());

        db.execSQL(createStatus);
        db.execSQL(createServices);
        db.execSQL(createProjectType);
        db.execSQL(createSubcategory);
        db.execSQL(projects);
        db.execSQL(createImages);
        db.execSQL(createPdf);
        db.execSQL(createIncome);
        db.execSQL(createExpenses);
        db.execSQL(createReceivables);
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
        if(oldVersion < 4) {
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s integer, " +
                            "%s varchar(255), " +
                            "%s varchar(255)," +
                            "%s varchar(255)," +
                            "%s varchar(255)," +
                            "%s varchar(255))", PARSECLASS.PROJECT.toString(), objectId, createdAt, updatedAt,
                    PROJECT.PROJECT_CODE.toString(), PROJECT.DELETED.toString(),
                    PROJECT.PROJECT_TITLE.toString(), PROJECT.PROJECT_OWNER.toString(), PROJECT.TYPE.toString(),
                    PROJECT.SERVICES.toString(),PROJECT.SUBCATEGORY.toString()));
        }
        if(oldVersion < 5) {
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s varchar(255)," +
                            "%s varchar(255)," +
                            "%s integer)", PARSECLASS.IMAGES.toString(), objectId, createdAt, updatedAt,
                    IMAGES.PARENT.toString(), IMAGES.FILENAME.toString(), IMAGES.FILES.toString(), IMAGES.DELETED.toString()));

            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s varchar(255)," +
                            "%s varchar(255)," +
                            "%s integer)", PARSECLASS.PDF.toString(), objectId, createdAt, updatedAt,
                    PDF.PARENT.toString(), PDF.FILENAME.toString(), PDF.FILES.toString(), PDF.DELETED.toString()));
        }

        if(oldVersion < 6) {
            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s varchar(255)," +
                            "%s varchar(255)," +
                            "%s integer, " +
                            "%s datetime)", PARSECLASS.INCOME.toString(), objectId, createdAt, updatedAt,
                    INCOME.PARENT.toString(), INCOME.DESCRIPTION.toString(), INCOME.AMOUNT.toString(),
                    INCOME.ATTACHMENTS.toString(), INCOME.TIMESTAMP.toString()));

            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s varchar(255)," +
                            "%s varchar(255)," +
                            "%s integer, " +
                            "%s datetime)", PARSECLASS.EXPENSES.toString(), objectId, createdAt, updatedAt,
                    EXPENSES.PARENT.toString(), EXPENSES.DESCRIPTION.toString(), EXPENSES.AMOUNT.toString(),
                    EXPENSES.ATTACHMENTS.toString(), EXPENSES.TIMESTAMP.toString()));

            db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s" +
                            "(%s character(10) PRIMARY KEY, " +
                            "%s datetime, " +
                            "%s datetime, " +
                            "%s varchar(255), " +
                            "%s varchar(255)," +
                            "%s varchar(255)," +
                            "%s integer, " +
                            "%s datetime)", PARSECLASS.RECEIVABLES.toString(), objectId, createdAt, updatedAt,
                    RECIEVABLES.PARENT.toString(), RECIEVABLES.DESCRIPTION.toString(), RECIEVABLES.AMOUNT.toString(),
                    RECIEVABLES.ATTACHMENTS.toString(), RECIEVABLES.TIMESTAMP.toString()));
        }
    }
}
