package com.example.teamalmanac.codealmanac.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class DataManager {
    //싱글톤
//    private static DataManager singletonInstance = null;
//    public static DataManager getSingletonInstance(Context context){
//        if(singletonInstance == null) {
//            singletonInstance = new DataManager(context);
//        }
//        return singletonInstance;
//    }
//    public static DataManager getSingletonInstance(){
//        if(singletonInstance == null) {
//            if(MainActivity.getContext() != null){
//                singletonInstance = new DataManager(MainActivity.getContext());
//            } else if(TabActivity.getContext() != null) {
//                singletonInstance = new DataManager(TabActivity.getContext());
//            }
//        }
//        return singletonInstance;
//    }

    private SQLiteDatabase sqliteDB;

    public DataManager(Context context){
        SQLiteHelper helper = new SQLiteHelper(context);
        sqliteDB = helper.getWritableDatabase();
        if( sqliteDB == null) {
            helper.onCreate(sqliteDB);
        }
        // 디비를 재생성해야하면 이 코드의 주석을 해제하시오
//        helper = new SQLiteHelper(context);
    }
    public void setUserName(String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.UserEntry.COLUMN_NAME_NAME, name);
        if(getUserName() != null){
            sqliteDB.update(SQLContract.UserEntry.TABLE_NAME, contentValues, null, null);
        } else {
            sqliteDB.insert(SQLContract.UserEntry.TABLE_NAME, null, contentValues);
        }
    }

    public String getUserName() {
        Cursor cursor =  sqliteDB.query(SQLContract.UserEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        } else {
            return null;
        }
    }

    //투두
    public void setTodo(String todo, String date, Integer visible) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_TODO, todo);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_DATE, date);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE, visible.toString());
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_SHOW, "true");
        sqliteDB.insert(SQLContract.ToDoEntry.TABLE_NAME, null, contentValues);
    }

    public void setTodo(String todo, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_TODO, todo);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_DATE, date);
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE, String.valueOf(View.INVISIBLE));
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_SHOW, "true");
        sqliteDB.insert(SQLContract.ToDoEntry.TABLE_NAME, null, contentValues);
    }


    public void deleteTodoUsingDate(String date) {
        sqliteDB.delete(SQLContract.ToDoEntry.TABLE_NAME, SQLContract.ToDoEntry.COLUMN_NAME_DATE + "=?", new String[] {date});
    }

    public void updateTodoButtonVisibility(String date, String visible) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE, visible);
        sqliteDB.update(SQLContract.ToDoEntry.TABLE_NAME, contentValues,
                SQLContract.ToDoEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
    }

    public void updateTodoShowing(String date, Boolean bl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_SHOW, bl.toString());
        sqliteDB.update(SQLContract.ToDoEntry.TABLE_NAME, contentValues,
                SQLContract.ToDoEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
    }
//
//    public void updateMainFocusButtonVisibility(String date, String visible) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_BUTTON_VISIBLE, visible);
//         sqliteDB.update(SQLContract.MainScheduleEntry.TABLE_NAME, contentValues,
//                SQLContract.MainScheduleEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
//    }

}
