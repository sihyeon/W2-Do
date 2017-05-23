package com.team.codealmanac.w2do.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class SQLiteManager{
    private final String TAG = "SQLiteManager";
    private SQLiteDatabase sqliteDB;
    private static FolderSQLiteEventListener mFolderListener;
    public SQLiteManager(Context context) {
        OpenHelper helper = new OpenHelper(context);
        sqliteDB = helper.getWritableDatabase();
//        if (sqliteDB == null) {
//            helper.onCreate(sqliteDB);
//        }

        // 디비를 재생성해야하면 이 코드의 주석을 해제하시오
//        helper.onCreate(sqliteDB);
    }
    public void setFolderTodoListener(FolderSQLiteEventListener listener){
        Log.d(TAG, "call setFolderTodoListener: "+listener.toString());
        mFolderListener = listener;
    }
    public interface FolderSQLiteEventListener {
        void OnAddTodoFolder(int position);
    }

    public void viewDatabaseTable() {
        Cursor c = sqliteDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (true) {
                Log.d(TAG, "table name : " + c.getString(0));
                if (!c.moveToNext()) {
                    c.close();
                    break;
                }
            }
        }
    }

    public void addTodoFolder(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_NAME, name);
        Cursor cursor = sqliteDB.query(SQLContract.TodoFolderEntry.TABLE_NAME, null, null, null, null, null, null);
        int sequence = cursor.getCount() + 1;
        contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE, cursor.getCount() + 1);
        contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT, 0);
        sqliteDB.insert(SQLContract.TodoFolderEntry.TABLE_NAME, null, contentValues);
        cursor.close();
        if(mFolderListener != null){
            Log.d(TAG, "call OnAddTodoFoler");
            mFolderListener.OnAddTodoFolder(sequence);
        }
        //// TODO: 2017-05-24 현재 mFolderListener가 null인 이유는 add folder가 다른 액티비티(다이알로그)에서 불리기 때문임. 거기서 SQLiteManager를 따로 부르기 때문에 null일수 밖에 없음
    }

    public ArrayList<TodoFolder> getAllTodoFolder() {
        Cursor cursor = sqliteDB.query(SQLContract.TodoFolderEntry.TABLE_NAME,
                null, null, null, null, null, SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE);
        ArrayList<TodoFolder> tempArray = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                //name, sequence, todo_count
                TodoFolder todofolder = new TodoFolder(cursor.getString(0), cursor.getInt(1), cursor.getInt(2));
                tempArray.add(todofolder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tempArray;
    }

    public int getCountInFolder(String name) {
        Cursor cursor = sqliteDB.query(SQLContract.TodoFolderEntry.TABLE_NAME,
                new String[]{SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT},
                SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + "=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } else {
            cursor.close();
            return 0;
        }
    }

    public void addTodo(Todo todo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_SEQUENCE, getCountInFolder(todo.folder_name) + 1);
        contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_COLOR, todo.color);
        contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_FOLDER, todo.folder_name);
        contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_CONTENT, todo.content);
        contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_START_DATE, todo.start_date);
        contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_END_DATE, todo.end_date);
        contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_ALARM, todo.alarm_date);
        if (todo.location_name != null) {
            contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_LATITUDE, todo.latitude);
            contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_LONGITUDE, todo.longitude);
            contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_LOCATION, todo.location_name);
        }
        if (todo.memo != null) contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_MEMO, todo.memo);
        sqliteDB.insert(SQLContract.TodoEntry.TABLE_NAME, null, contentValues);
    }

    public ArrayList<SimpleTodo> getSimpleTodo() {
        Cursor cursor = sqliteDB.query(SQLContract.TodoEntry.TABLE_NAME,
                new String[]{SQLContract.TodoEntry._ID, SQLContract.TodoEntry.COLUMN_NAME_CHECK, SQLContract.TodoEntry.COLUMN_NAME_CONTENT},
                SQLContract.TodoEntry.COLUMN_NAME_CHECK + "=?", new String[]{String.valueOf(0)}, null, null, null);

        if (!cursor.moveToFirst()) {
            return null;
        }
        ArrayList<SimpleTodo> tempArray = new ArrayList<>();
        do {
            //name, sequence, todo_count
            SimpleTodo simpleTodo = new SimpleTodo(cursor.getLong(0), cursor.getInt(1), cursor.getString(2));
            tempArray.add(simpleTodo);
        } while (cursor.moveToNext());
        cursor.close();
        return tempArray;
    }


//    public void updateTodoButtonVisibility(String date, String visible) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE, visible);
//        sqliteDB.update(SQLContract.ToDoEntry.TABLE_NAME, contentValues,
//                SQLContract.ToDoEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
//    }
//
//    public void updateTodoShowing(String date, Boolean bl) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_SHOW, bl.toString());
//        sqliteDB.update(SQLContract.ToDoEntry.TABLE_NAME, contentValues,
//                SQLContract.ToDoEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
//    }
//
////
////    public void updateMainFocusButtonVisibility(String input_date, String visible) {
////        ContentValues contentValues = new ContentValues();
////        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_BUTTON_VISIBLE, visible);
////         sqliteDB.update(SQLContract.MainScheduleEntry.TABLE_NAME, contentValues,
////                SQLContract.MainScheduleEntry.COLUMN_NAME_DATE+"=?", new String[] {input_date});
////    }
//
//
////    //메인스케줄
////    public void addMainSchedule(String mainSchedule){
////        String date = (new SimpleDateFormat("yyyy MM dd HH:mm ss")).format(new Date());
////
////        ContentValues contentValues = new ContentValues();
////        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_MAIN_SCHEDULE, mainSchedule);
////        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_DATE, date);
////        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_USED, 1);
////
////        sqliteDB.insert(SQLContract.MainScheduleEntry.TABLE_NAME, null, contentValues);
////    }
////    public MainSchedule getMainSchedule(){
////        Cursor cursor =  sqliteDB.query(SQLContract.MainScheduleEntry.TABLE_NAME, null,
////                SQLContract.MainScheduleEntry.COLUMN_NAME_USED + "=?", new String[]{"1"}, null, null, null);
////        if(cursor.moveToLast()) return new MainSchedule(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
////        else return null;
////    }
//
//    public void unusedMainSchedule(){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_USED, 0);
//
//        sqliteDB.update(SQLContract.MainScheduleEntry.TABLE_NAME, contentValues,
//                SQLContract.ToDoEntry._ID+"=(select MAX(_id) from " + SQLContract.MainScheduleEntry.TABLE_NAME + ")", null);
//    }
}
