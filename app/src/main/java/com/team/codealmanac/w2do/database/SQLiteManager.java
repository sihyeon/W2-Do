package com.team.codealmanac.w2do.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.team.codealmanac.w2do.models.MainSchedule;
import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

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

    public ArrayList<String> getAllTodoFolderName() {
        sqliteDB.beginTransaction();
        ArrayList<String> tempArray = new ArrayList<>();
        try {
            Cursor cursor = sqliteDB.query(SQLContract.TodoFolderEntry.TABLE_NAME,
                    new String[]{SQLContract.TodoFolderEntry.COLUMN_NAME_NAME}, null, null, null, null, SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE);
            if (cursor.moveToFirst()) {
                do {
                    //name, sequence, todo_count
                    String folderName = cursor.getString(0);
                    tempArray.add(folderName);
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

    private void incrementTodoCountInFolder(String folderName) {
        String updateSQL = "UPDATE " + SQLContract.TodoFolderEntry.TABLE_NAME +
                " SET " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " = " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " + 1" +
                " WHERE " + SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + " = '" + folderName + "'";
        sqliteDB.execSQL(updateSQL);
        if (!folderName.equals(SQLContract.DEFUALT_FOLDER_NAME)) {
            String defaultFolder = "UPDATE " + SQLContract.TodoFolderEntry.TABLE_NAME +
                    " SET " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " = " + SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " + 1" +
                    " WHERE " + SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + " = '" + SQLContract.DEFUALT_FOLDER_NAME + "'";
            sqliteDB.execSQL(defaultFolder);
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
                incrementTodoCountInFolder(todo.folder_name);
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

    public ArrayList<Todo> getTodoListInFolder(String folder) {
        ArrayList<Todo> tempArray = new ArrayList<>();
        sqliteDB.beginTransaction();
        try {
            Cursor cursor = sqliteDB.query(SQLContract.TodoEntry.TABLE_NAME,
                    null,
                    SQLContract.TodoEntry.COLUMN_NAME_FOLDER + "=?", new String[]{folder}, null, null, null);
            if (!cursor.moveToFirst()) {
                return null;
            }
            do {
                //name, sequence, todo_count
                Todo Todo = new Todo(cursor.getLong(0), cursor.getLong(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getString(5), cursor.getLong(6), cursor.getLong(7), cursor.getLong(8),
                        cursor.getDouble(9), cursor.getDouble(10), cursor.getString(11), cursor.getString(12));
                tempArray.add(Todo);
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

    public ArrayList<Todo> getAllTodoList() {
        ArrayList<Todo> tempArray = new ArrayList<>();
        sqliteDB.beginTransaction();
        try {
            Cursor cursor = sqliteDB.query(SQLContract.TodoEntry.TABLE_NAME,
                    null, null, null, null, null, null);
            if (!cursor.moveToFirst()) {
                return null;
            }
            do {
                //name, sequence, todo_count
                Todo Todo = new Todo(cursor.getLong(0), cursor.getLong(1), cursor.getInt(2), cursor.getInt(3),
                        cursor.getString(4), cursor.getString(5), cursor.getLong(6), cursor.getLong(7), cursor.getLong(8),
                        cursor.getDouble(9), cursor.getDouble(10), cursor.getString(11), cursor.getString(12));
                tempArray.add(Todo);
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

    public boolean setMainSchedule(String content) {
        sqliteDB.beginTransaction();
        try {
            Calendar calendar = Calendar.getInstance();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_DATE, calendar.getTimeInMillis());
            contentValues.put(SQLContract.MainScheduleEntry.COLUMN_NAME_CONTENT, content);
            sqliteDB.insert(SQLContract.MainScheduleEntry.TABLE_NAME, null, contentValues);
            sqliteDB.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error setMainSchedule: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
        return false;
    }

    public MainSchedule getMainSchedule() {
        sqliteDB.beginTransaction();
        try {
            Cursor cursor = sqliteDB.query(SQLContract.MainScheduleEntry.TABLE_NAME, null,
                    SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE + "=?", new String[]{String.valueOf(0)}, null, null, null);
            if (cursor.moveToFirst()) {
                sqliteDB.setTransactionSuccessful();
                return new MainSchedule(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3));
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(TAG, "Error getMainSchedule: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
        return null;
    }

    public String updateCheckInMainSchedule(long _ID) {
        sqliteDB.beginTransaction();
        try {
            int check_state;
            Cursor cursor = sqliteDB.query(SQLContract.MainScheduleEntry.TABLE_NAME,
                    new String[]{SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE},
                    SQLContract.MainScheduleEntry._ID + "=?", new String[]{String.valueOf(_ID)}, null, null, null);
            if (cursor.moveToFirst()) {
                check_state = cursor.getInt(0);
                String updateSQL;
                if(check_state == 0){
                    updateSQL = "UPDATE " + SQLContract.MainScheduleEntry.TABLE_NAME +
                            " SET " + SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE + " = " + 1 +
                            " WHERE " + SQLContract.MainScheduleEntry._ID + " = " + _ID;
                } else {
                    if(!sqliteDB.query(SQLContract.MainScheduleEntry.TABLE_NAME,
                            new String[]{SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE},
                            SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE + "=?", new String[]{String.valueOf(0)}, null, null, null).moveToFirst()){
                        updateSQL = "UPDATE " + SQLContract.MainScheduleEntry.TABLE_NAME +
                                " SET " + SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE + " = " + 0 +
                                " WHERE " + SQLContract.MainScheduleEntry._ID + " = " + _ID;
                    } else {
                        return "두개 이상의 메인스케줄을 활성화 할 수 없습니다.";
                    }
                }
                sqliteDB.execSQL(updateSQL);
            }
            cursor.close();
            sqliteDB.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error updateCheckInMainSchedule: " + e);
        } finally {
            sqliteDB.endTransaction();
        }
        return null;
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
        try {
            //삭제
            db.execSQL("DROP TABLE IF EXISTS " + SQLContract.TodoFolderEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SQLContract.TodoEntry.TABLE_NAME);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "init(): 삭제 에러 - " + e);
        } finally {
            db.endTransaction();
        }

        //테이블 생성
        db.beginTransaction();
        try {
            db.execSQL(
                    "CREATE TABLE " + SQLContract.TodoFolderEntry.TABLE_NAME + " ( " +
                            SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + " TEXT PRIMARY KEY, " +
                            SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE + " INTEGER UNIQUE NOT NULL, " +
                            SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT + " INTEGER DEFAULT 0 " +
                            " ) "
            );

            db.execSQL(
                    "CREATE TABLE " + SQLContract.TodoEntry.TABLE_NAME + " ( " +
                            SQLContract.TodoEntry._ID + " INTEGER PRIMARY KEY, " +
                            SQLContract.TodoEntry.COLUMN_NAME_SEQUENCE + " INTEGER NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_CHECK + " INTEGER CHECK(" + SQLContract.TodoEntry.COLUMN_NAME_CHECK + "= 0 OR " + SQLContract.TodoEntry.COLUMN_NAME_CHECK + " = 1) DEFAULT 0, " +
                            SQLContract.TodoEntry.COLUMN_NAME_COLOR + " INTEGER NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_FOLDER + " TEXT NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_CONTENT + " TEXT NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_START_DATE + " INTEGER NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_END_DATE + " INTEGER NOT NULL, " +
                            SQLContract.TodoEntry.COLUMN_NAME_ALARM + " INTEGER, " +
                            SQLContract.TodoEntry.COLUMN_NAME_LATITUDE + " REAL DEFAULT -1, " +
                            SQLContract.TodoEntry.COLUMN_NAME_LONGITUDE + " REAL DEFAULT -1, " +
                            SQLContract.TodoEntry.COLUMN_NAME_LOCATION + " TEXT, " +
                            SQLContract.TodoEntry.COLUMN_NAME_MEMO + " TEXT " +
//                        "FOREIGN KEY(" + SQLContract.TodoEntry.COLUMN_NAME_FOLDER + ")" +
//                        " REFERENCES " + SQLContract.TodoFolderEntry.TABLE_NAME + "(" + SQLContract.TodoFolderEntry.COLUMN_NAME_NAME + ")" +
//                        " ON DELETE CASCADE" +
//                        " ON UPDATE CASCADE"
                            " ) "
            );

            db.execSQL(
                    "CREATE TABLE " + SQLContract.MainScheduleEntry.TABLE_NAME + " ( " +
                            SQLContract.MainScheduleEntry._ID + " INTEGER PRIMARY KEY, " +
                            SQLContract.MainScheduleEntry.COLUMN_NAME_DATE + " INTEGER NOT NULL, " +
                            SQLContract.MainScheduleEntry.COLUMN_NAME_CONTENT + " TEXT NOT NULL, " +
                            SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE + " INTEGER CHECK(" + SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE + "= 0 OR " + SQLContract.MainScheduleEntry.COLUMN_NAME_CHECK_STATE + " = 1) DEFAULT 0 " +
                            " ) "
            );
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "init(): 생성 에러 - " + e);
        } finally {
            db.endTransaction();
        }

        db.beginTransaction();
        try {
            //기본 폴더 생성
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_NAME, SQLContract.DEFUALT_FOLDER_NAME);
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_TODO_COUNT, 0);
            contentValues.put(SQLContract.TodoFolderEntry.COLUMN_NAME_SEQUENCE, 1);
            db.insert(SQLContract.TodoFolderEntry.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "init(): 기본폴더 데이터 insert 에러 - " + e);
        } finally {
            db.endTransaction();
        }
    }
}
