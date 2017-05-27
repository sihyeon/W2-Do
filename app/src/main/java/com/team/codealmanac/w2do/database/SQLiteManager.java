package com.team.codealmanac.w2do.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

public class SQLiteManager extends SQLiteOpenHelper {
    private final String TAG = "SQLiteManager";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase sqliteDB;

    //    private static List<FolderSQLiteEventListener> mFolderListener;
//    private static List<TodoSQLiteEventListener> mTodoListener;
    private static FolderSQLiteEventListener mFolderListener;
    private static TodoSQLiteEventListener mTodoListener;

    private static SQLiteManager sInstance;

    public static synchronized SQLiteManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteManager(context.getApplicationContext());
        }
        return sInstance;
    }

    public SQLiteManager(Context context) {
        super(context, SQLContract.DATABASE_NAME, null, DATABASE_VERSION);
        sqliteDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void setFolderTodoListener(FolderSQLiteEventListener listener) {
        mFolderListener = listener;
    }

    public void setTodoListener(TodoSQLiteEventListener listener) {
        mTodoListener = listener;
    }

    public interface FolderSQLiteEventListener {
        void OnAddTodoFolder();

        void OnUpdateTodoFolder();
    }

    public interface TodoSQLiteEventListener {
        void OnAddTodo(Todo todo);
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
        sqliteDB.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_NAME, name);
            Cursor cursor = sqliteDB.query(SQLContract.TodoFolderEntry.TABLE_NAME, null, null, null, null, null, null);
            int sequence = cursor.getCount() + 1;
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE, sequence);
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT, 0);
            sqliteDB.insert(SQLContract.TodoFolderEntry.TABLE_NAME, null, contentValues);
            cursor.close();
            if (mFolderListener != null) {
                mFolderListener.OnAddTodoFolder();
            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error addTodoFolder: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
        //// TODO: 2017-05-24 현재 mFolderListener가 null인 이유는 add folder가 다른 액티비티(다이알로그)에서 불리기 때문임. 거기서 SQLiteManager를 따로 부르기 때문에 null일수 밖에 없음
    }

    public ArrayList<TodoFolder> getAllTodoFolder() {
        sqliteDB.beginTransaction();
        ArrayList<TodoFolder> tempArray = new ArrayList<>();
        try {
            Cursor cursor = sqliteDB.query(SQLContract.TodoFolderEntry.TABLE_NAME,
                    null, null, null, null, null, SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE);
            if (cursor.moveToFirst()) {
                do {
                    //name, sequence, todo_count
                    TodoFolder todofolder = new TodoFolder(cursor.getString(0), cursor.getLong(1), cursor.getLong(2));
                    tempArray.add(todofolder);
                } while (cursor.moveToNext());
            }
            cursor.close();
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error getAllTodoFolder: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
        return tempArray;
    }

    private int getCountInFolder(String folderName) {
        sqliteDB.beginTransaction();
        try {
            Cursor cursor = sqliteDB.query(SQLContract.TodoFolderEntry.TABLE_NAME,
                    new String[]{SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT},
                    SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + "=?", new String[]{folderName}, null, null, null);
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                cursor.close();
                return count;
            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error getCountInFolder: " + e);
            sqliteDB.endTransaction();
        }
        return 0;
    }

    public void incrementTodoCountInFolder(String folderName) {
        sqliteDB.beginTransaction();
        try {
            String updateSQL = "UPDATE " + SQLContract.TodoFolderEntry.TABLE_NAME +
                    " SET " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " = " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " + 1" +
                    " WHERE " + SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + " = '" + folderName + "'";
            sqliteDB.execSQL(updateSQL);
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error incrementTodoCountInFolder: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
    }

    public void addTodo(Todo todo) {
        todo.folder_sequence = getCountInFolder(todo.folder_name) + 1;
        sqliteDB.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_SEQUENCE, todo.folder_sequence);
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
            if (todo.memo != null)
                contentValues.put(SQLContract.TodoEntry.COLUMN_NAME_MEMO, todo.memo);

            if (sqliteDB.insert(SQLContract.TodoEntry.TABLE_NAME, null, contentValues) != -1) {
                String updateSQL = "UPDATE " + SQLContract.TodoFolderEntry.TABLE_NAME +
                        " SET " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " = " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " + 1" +
                        " WHERE " + SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + " = '" + todo.folder_name + "'";
                sqliteDB.execSQL(updateSQL);
//                incrementTodoCountInFolder(todo.folder_name);
            }
            if (mTodoListener != null && mFolderListener != null) {
                mTodoListener.OnAddTodo(todo);
                mFolderListener.OnUpdateTodoFolder();
            }
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error addTodo: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
    }

    public ArrayList<SimpleTodo> getSimpleTodo() {
        sqliteDB.beginTransaction();
        ArrayList<SimpleTodo> tempArray = new ArrayList<>();
        try {
            Cursor cursor = sqliteDB.query(SQLContract.TodoEntry.TABLE_NAME,
                    new String[]{SQLContract.TodoEntry._ID, SQLContract.TodoEntry.COLUMN_NAME_CHECK, SQLContract.TodoEntry.COLUMN_NAME_CONTENT},
                    SQLContract.TodoEntry.COLUMN_NAME_CHECK + "=?", new String[]{String.valueOf(0)}, null, null, null);

            if (!cursor.moveToFirst()) {
                return null;
            }
            do {
                //name, sequence, todo_count
                SimpleTodo simpleTodo = new SimpleTodo(cursor.getLong(0), cursor.getInt(1), cursor.getString(2));
                tempArray.add(simpleTodo);
            } while (cursor.moveToNext());
            cursor.close();
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error getSimpleTodo: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
        return tempArray;
    }


//    public void updateTodoButtonVisibility(String date, String visible) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(SQLContract.ToDoEntry.COLUMN_NAME_BUTTON_VISIBLE, visible);
//        sqliteDB.update(SQLContract.ToDoEntry.TABLE_NAME, contentValues,
//                SQLContract.ToDoEntry.COLUMN_NAME_DATE+"=?", new String[] {date});
//    }

    private void init(SQLiteDatabase db) {
        Log.d(TAG, "데이터베이스 초기화");

        db.beginTransaction();
        try{
            //삭제
            db.execSQL("DROP TABLE IF EXISTS " + SQLContract.TodoFolderEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SQLContract.TodoEntry.TABLE_NAME);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "init(): 삭제 에러 - " + e);
        } finally {
            db.endTransaction();
        }

        //테이블 생성
        db.beginTransaction();
        try{
            db.execSQL(
                    "CREATE TABLE " + SQLContract.TodoFolderEntry.TABLE_NAME + " ( " +
                            SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + " TEXT PRIMARY KEY, " +
                            SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE + " INTEGER UNIQUE NOT NULL, " +
                            SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " INTEGER DEFAULT 0 "
                            + " ) "
            );

            db.execSQL(
                    "CREATE TABLE " + SQLContract.TodoEntry.TABLE_NAME + " ( " +
                            SQLContract.TodoEntry._ID + " INTEGER PRIMARY KEY, " +
                            SQLContract.TodoEntry.COLUMN_NAME_SEQUENCE + " INTEGER UNIQUE NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_CHECK + " INTEGER CHECK(" + SQLContract.TodoEntry.COLUMN_NAME_CHECK + "= 0 OR " + SQLContract.TodoEntry.COLUMN_NAME_CHECK + " = 1) DEFAULT 0, " +
                            SQLContract.TodoEntry.COLUMN_NAME_COLOR + " INTEGER NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_FOLDER + " TEXT NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_CONTENT + " TEXT NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_START_DATE + " INTEGER NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_END_DATE + " INTEGER NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_ALARM + " INTEGER, " +
                            SQLContract.TodoEntry.COLUMN_NAME_LATITUDE + " REAL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_LONGITUDE + " REAL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_LOCATION + " TEXT, " +
                            SQLContract.TodoEntry.COLUMN_NAME_MEMO + " TEXT "
//                        "FOREIGN KEY(" + SQLContract.TodoEntry.COLUMN_NAME_FOLDER + ")" +
//                        " REFERENCES " + SQLContract.TodoFolderEntry.TABLE_NAME + "(" + SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + ")" +
//                        " ON DELETE CASCADE" +
//                        " ON UPDATE CASCADE"
                            + " ) "
            );
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "init(): 생성 에러 - " + e);
        } finally {
            db.endTransaction();
        }

        db.beginTransaction();
        try{
            //기본 폴더 생성
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_NAME, SQLContract.DEFUALT_FOLDER_NAME);
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT, 0);
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE, 1);
            db.insert(SQLContract.TodoFolderEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "init(): 기본폴더 데이터 insert 에러 - " + e);
        } finally {
            db.endTransaction();
        }
    }
}
