package com.example.teamalmanac.codealmanac.database;

import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

//DB 정의서
public final class SQLContract {
    public static final String DATABASE_NAME = "AlmanacDB.db";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String convertDateToString(Date date){
        return (new SimpleDateFormat(DATE_FORMAT)).format(date);
    }

    //사용자 이름 테이블
    public static abstract class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user_table";
        public static final String COLUMN_NAME_UUID = "user_uuid";
        public static final String COLUMN_NAME_NAME = "user_name";
    }

    public static abstract class FcmUserEntry implements BaseColumns{
        public static final String TABLE_NAME = "fcm_table";
        public static final String COLUMN_NAME_TOKEN = "fcm_token";
    }

    // 할일 테이블
    public static abstract class ToDoEntry implements BaseColumns{
        public static final String TABLE_NAME = "todo_table";
        public static final String COLUMN_NAME_TODO = "todo_text";
        public static final String COLUMN_NAME_DATE = "todo_date";
        public static final String COLUMN_NAME_BUTTON_VISIBLE = "todo_btvisible";
        public static final String COLUMN_NAME_SHOW = "todo_show";
    }

    public static abstract class MainScheduleEntry implements  BaseColumns{    //순서대로 insert 및 쿼리 해주세요.
        public static final String TABLE_NAME = "main_focus_table";
        public static final String COLUMN_NAME_MAIN_FOCUS = "main_focus_text";
        public static final String COLUMN_NAME_DATE = "main_focus_date";
//        public static final String COLUMN_NAME_BUTTON_VISIBLE = "main_focus_btvisible";
//        public static final String COLUMN_NAME_SHOW = "main_focus_show";
    }

    public static abstract class AppFolderEntry implements BaseColumns {
        public static final String TABLE_NAME = "app_folder_table";
        public static final String COLUMN_NAME_APP_NAME = "app_name";
        public static final String COLUMN_NAME_APP_PATH = "app_path";
    }
}
