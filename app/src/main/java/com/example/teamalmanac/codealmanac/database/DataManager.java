package com.example.teamalmanac.codealmanac.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;


import java.util.ArrayList;

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

    public void setFcmUser(String token){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.FcmUserEntry.COLUMN_NAME_TOKEN, token);
        if(getFcmUser() != null) {
            sqliteDB.update(SQLContract.FcmUserEntry.TABLE_NAME, contentValues, null, null);
        } else {
            sqliteDB.insert(SQLContract.FcmUserEntry.TABLE_NAME, null, contentValues);
        }
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

    public ArrayList<TodoDataType> getShowingTodos() {
        Cursor cursor = sqliteDB.query(SQLContract.ToDoEntry.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<TodoDataType> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                //check is showing on lockscreen(app2)
                if(Boolean.valueOf(cursor.getString(4))) {
                    //todo, date
                    list.add(new TodoDataType(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                }
            } while(cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<TodoDataType> getTodoList(){
        ArrayList<TodoDataType> todoList = new ArrayList<>();
        Cursor cursor =  sqliteDB.query(SQLContract.ToDoEntry.TABLE_NAME, new String[]{SQLContract.MainFocusEntry._ID,
                SQLContract.ToDoEntry.COLUMN_NAME_TODO, SQLContract.ToDoEntry.COLUMN_NAME_DATE}, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do {
                //id, mainfocus, date
                todoList.add(new TodoDataType(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            } while(cursor.moveToNext());
        }
        return todoList;
    }

    public ArrayList<TodoDataType> selectionDateTodo(String date){
        ArrayList<TodoDataType> todoList = new ArrayList<>();
        Cursor cursor =  sqliteDB.query(SQLContract.ToDoEntry.TABLE_NAME, new String[]{SQLContract.ToDoEntry._ID,
                        SQLContract.ToDoEntry.COLUMN_NAME_TODO, SQLContract.ToDoEntry.COLUMN_NAME_DATE},
                SQLContract.ToDoEntry.COLUMN_NAME_DATE+"=?", new String[]{date}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                //id, mainfocus, date
                todoList.add(new TodoDataType(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            } while(cursor.moveToNext());
        }
        return todoList;
    }

    //메인포커스
    public void setMainFocus(String name, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, name);
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_DATE, date);
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_BUTTON_VISIBLE, String.valueOf(View.INVISIBLE));
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_SHOW, "true");

         sqliteDB.insert(SQLContract.MainFocusEntry.TABLE_NAME, null, contentValues);
    }

    public MainfocusDataType getMainFocusInfo() {
        Cursor cursor = sqliteDB.query(SQLContract.MainFocusEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToLast()){
            if(cursor.getString(4).equals("false")) return new MainfocusDataType();
            else return new MainfocusDataType(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        } else {
            return new MainfocusDataType();
        }
    }

    public void updateMainFocusButtonVisibility(String date, String visible) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_BUTTON_VISIBLE, visible);
         sqliteDB.update(SQLContract.MainFocusEntry.TABLE_NAME, contentValues,
                SQLContract.MainFocusEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
    }

    public ArrayList<MainfocusDataType> getMainFocusList() {
        ArrayList<MainfocusDataType> mainfocusList = new ArrayList<>();
        Cursor cursor = sqliteDB.query(SQLContract.MainFocusEntry.TABLE_NAME, new String[]{SQLContract.MainFocusEntry._ID,
                SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, SQLContract.MainFocusEntry.COLUMN_NAME_DATE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //id, mainfocus, date
                mainfocusList.add(new MainfocusDataType(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return mainfocusList;
    }
    public void deleteMainFocus(){
        Cursor cursor = sqliteDB.query(SQLContract.MainFocusEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToLast()) {
            if (cursor.getString(4).equals("true")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(SQLContract.MainFocusEntry.COLUMN_NAME_SHOW, "false");
                sqliteDB.update(SQLContract.MainFocusEntry.TABLE_NAME, contentValues,
                        SQLContract.MainFocusEntry._ID+"=?", new String[] {cursor.getString(0)});
            }
        }
    }

    public ArrayList<MainfocusDataType> selectionDateMainfocus(String date){
        ArrayList<MainfocusDataType> mainfocusList = new ArrayList<>();
        Cursor cursor =  sqliteDB.query(SQLContract.MainFocusEntry.TABLE_NAME, new String[]{SQLContract.MainFocusEntry._ID,
                        SQLContract.MainFocusEntry.COLUMN_NAME_MAIN_FOCUS, SQLContract.MainFocusEntry.COLUMN_NAME_DATE},
                SQLContract.MainFocusEntry.COLUMN_NAME_DATE+"=?", new String[]{date}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                //id, mainfocus, date
                mainfocusList.add(new MainfocusDataType(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            } while(cursor.moveToNext());
        }
        return mainfocusList;
    }

    //FCM
    public FcmUserDataType getFcmUser() {
        Cursor cursor = sqliteDB.query(SQLContract.FcmUserEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToLast()){
            //uuid, reg_id
            return new FcmUserDataType(cursor.getString(1));
        } else {
            return new FcmUserDataType();
        }
    }

    //AppFolder
    public void insertApp(String app_name, String app_path){
        Cursor cursor = sqliteDB.query(SQLContract.AppFolderEntry.TABLE_NAME, new String[]{SQLContract.AppFolderEntry._ID,
                SQLContract.AppFolderEntry.COLUMN_NAME_APP_NAME}, SQLContract.AppFolderEntry.COLUMN_NAME_APP_NAME+"=?", new String[]{app_name}, null, null, null);
        if (cursor.moveToFirst()) return;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.AppFolderEntry.COLUMN_NAME_APP_NAME, app_name);
        contentValues.put(SQLContract.AppFolderEntry.COLUMN_NAME_APP_PATH, app_path);
        sqliteDB.insert(SQLContract.AppFolderEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<AppFolderDataType> getAppFolderList(){
        ArrayList<AppFolderDataType> appList = new ArrayList<>();
        Cursor cursor = sqliteDB.query(SQLContract.AppFolderEntry.TABLE_NAME, new String[]{SQLContract.AppFolderEntry._ID,
                SQLContract.AppFolderEntry.COLUMN_NAME_APP_NAME, SQLContract.AppFolderEntry.COLUMN_NAME_APP_PATH}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                //id, mainfocus, date
                appList.add(new AppFolderDataType(cursor.getLong(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return appList;
    }
}
