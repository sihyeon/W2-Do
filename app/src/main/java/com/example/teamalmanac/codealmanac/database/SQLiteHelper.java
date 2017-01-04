package com.example.teamalmanac.codealmanac.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, SQLContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        init(db);
    }

    private void init(SQLiteDatabase db){
        //삭제
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.FcmUserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.ToDoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.MainScheduleEntry.TABLE_NAME);

        //생성
        db.execSQL(
                "CREATE TABLE " + SQLContract.UserEntry.TABLE_NAME + " ( " +
                        SQLContract.UserEntry._ID + " INTEGER PRIMARY KEY, " +
                        SQLContract.UserEntry.COLUMN_NAME_NAME + " text " + " ) "
        );

        db.execSQL(
                "CREATE TABLE " + SQLContract.FcmUserEntry.TABLE_NAME + " ( " +
                        SQLContract.FcmUserEntry._ID + "INTEGER PRIMARY KEY, " +
                        SQLContract.FcmUserEntry.COLUMN_NAME_TOKEN + " text )"
        );

        db.execSQL("CREATE TABLE " + SQLContract.ToDoEntry.TABLE_NAME + " ( " +
                SQLContract.ToDoEntry._ID + " INTEGER PRIMARY KEY, " +
                SQLContract.ToDoEntry.COLUMN_NAME_TODO + " text , " +
                SQLContract.ToDoEntry.COLUMN_NAME_DATE + " text , " +
                SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE + " text , " +
                SQLContract.ToDoEntry.COLUMN_NAME_SHOW + " text " + " ) "
        );

        db.execSQL(
                "CREATE TABLE " + SQLContract.MainScheduleEntry.TABLE_NAME + " ( " +
                        SQLContract.MainScheduleEntry._ID + " INTEGER PRIMARY KEY, " +
                        SQLContract.MainScheduleEntry.COLUMN_NAME_MAIN_FOCUS + " text ," +
                        SQLContract.MainScheduleEntry.COLUMN_NAME_DATE + " text , " +
                        SQLContract.MainScheduleEntry.COLUMN_NAME_BUTTON_VISIBLE+ " text, " +
                        SQLContract.MainScheduleEntry.COLUMN_NAME_SHOW + " text " + " ) "
        );

        db.execSQL(
                "CREATE TABLE " + SQLContract.AppFolderEntry.TABLE_NAME + " ( " +
                        SQLContract.AppFolderEntry._ID + " INTEGER PRIMARY KEY, " +
                        SQLContract.AppFolderEntry.COLUMN_NAME_APP_NAME + " text ," +
                        SQLContract.AppFolderEntry.COLUMN_NAME_APP_PATH + " text " + " ) "
        );
    }
}
