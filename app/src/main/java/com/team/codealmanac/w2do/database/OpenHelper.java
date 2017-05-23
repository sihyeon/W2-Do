package com.team.codealmanac.w2do.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

class OpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public OpenHelper(Context context) {
        super(context, SQLContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        init(db);
    }

    private void init(SQLiteDatabase db){
        //삭제
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.TodoFolderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SQLContract.TodoEntry.TABLE_NAME);

        //생성
        db.execSQL(
                "CREATE TABLE " + SQLContract.TodoFolderEntry.TABLE_NAME + " ( " +
                        SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + " TEXT PRIMARY KEY, " +
                        SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE + " INTEGER UNIQUE NOT NULL, " +
                        SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " INTEGER DEFAULT 0 "
                        + " ) "
        );
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_NAME, "ALL");
        contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT, 0);
        contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE, 1);
        db.insert(SQLContract.TodoFolderEntry.TABLE_NAME, null, contentValues);

        db.execSQL(
                "CREATE TABLE " + SQLContract.TodoEntry.TABLE_NAME + " ( " +
                        SQLContract.TodoEntry._ID + "INTEGER PRIMARY KEY, " +
                        SQLContract.TodoEntry.COLUMN_NAME_SEQUENCE + " INTEGER UNIQUE NOT NULL, " +
                        SQLContract.TodoEntry.COLUMN_NAME_CHECK + " INTEGER CHECK(" + SQLContract.TodoEntry.COLUMN_NAME_CHECK + "= 0 OR " + SQLContract.TodoEntry.COLUMN_NAME_CHECK + " = 1) DEFAULT 0, " +
                        SQLContract.TodoEntry.COLUMN_NAME_COLOR + " INTEGER NOT NULL, " +
                        SQLContract.TodoEntry.COLUMN_NAME_FOLDER + " TEXT REFERENCES " + SQLContract.TodoFolderEntry.TABLE_NAME + "(" + SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + ")" + " on update cascade, " +
                        SQLContract.TodoEntry.COLUMN_NAME_CONTENT + " TEXT NOT NULL, " +
                        SQLContract.TodoEntry.COLUMN_NAME_START_DATE + " INTEGER NOT NULL, " +
                        SQLContract.TodoEntry.COLUMN_NAME_END_DATE + " INTEGER NOT NULL, " +
                        SQLContract.TodoEntry.COLUMN_NAME_ALARM + " INTEGER, " +
                        SQLContract.TodoEntry.COLUMN_NAME_LATITUDE + " REAL, " +
                        SQLContract.TodoEntry.COLUMN_NAME_LONGITUDE + " REAL, " +
                        SQLContract.TodoEntry.COLUMN_NAME_LOCATION + " TEXT, " +
                        SQLContract.TodoEntry.COLUMN_NAME_MEMO + " TEXT "
                        + " ) "
        );
    }
}
