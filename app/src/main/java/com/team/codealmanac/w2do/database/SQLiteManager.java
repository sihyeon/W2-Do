package com.team.codealmanac.w2do.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.team.codealmanac.w2do.models.MainSchedule;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class SQLiteManager {
    private SQLiteDatabase sqliteDB;

    public SQLiteManager(Context context){
        SQLiteHelper helper = new SQLiteHelper(context);
        sqliteDB = helper.getWritableDatabase();
        if( sqliteDB == null) {
            helper.onCreate(sqliteDB);
        }
        // 디비를 재생성해야하면 이 코드의 주석을 해제하시오
//        helper.onCreate(sqliteDB);
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

    //Today
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
//    public void updateMainFocusButtonVisibility(String input_date, String visible) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_BUTTON_VISIBLE, visible);
//         sqliteDB.update(SQLContract.MainScheduleEntry.TABLE_NAME, contentValues,
//                SQLContract.MainScheduleEntry.COLUMN_NAME_DATE+"=?", new String[] {input_date});
//    }


//    //메인스케줄
//    public void addMainSchedule(String mainSchedule){
//        String date = (new SimpleDateFormat("yyyy MM dd HH:mm ss")).format(new Date());
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_MAIN_SCHEDULE, mainSchedule);
//        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_DATE, date);
//        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_USED, 1);
//
//        sqliteDB.insert(SQLContract.MainScheduleEntry.TABLE_NAME, null, contentValues);
//    }
//    public MainSchedule getMainSchedule(){
//        Cursor cursor =  sqliteDB.query(SQLContract.MainScheduleEntry.TABLE_NAME, null,
//                SQLContract.MainScheduleEntry.COLUMN_NAME_USED + "=?", new String[]{"1"}, null, null, null);
//        if(cursor.moveToLast()) return new MainSchedule(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
//        else return null;
//    }

    public void unusedMainSchedule(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_USED, 0);

        sqliteDB.update(SQLContract.MainScheduleEntry.TABLE_NAME, contentValues,
                SQLContract.ToDoEntry._ID+"=(select MAX(_id) from " + SQLContract.MainScheduleEntry.TABLE_NAME + ")", null);
    }
}
