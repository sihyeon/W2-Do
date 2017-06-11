package com.team.codealmanac.w2do.database;

import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

//DB 정의서
public final class SQLContract {
    public static final String DATABASE_NAME = "What2do.db";
    public static String DEFUALT_FOLDER_NAME = "ALL";
    //투두 테이블
    static class TodoFolderEntry implements BaseColumns{
        static final String TABLE_NAME = "todo_folder";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_SEQUENCE = "sequence";
        static final String COLUMN_NAME_TODO_COUNT = "todo_count";
    }

    static class TodoEntry implements BaseColumns{
        static final String TABLE_NAME = "todo";
        static final String COLUMN_NAME_SEQUENCE = "folder_sequence";
        static final String COLUMN_NAME_CHECK = "check_state";
        static final String COLUMN_NAME_COLOR = "color";
        static final String COLUMN_NAME_FOLDER = "folder_name";
        static final String COLUMN_NAME_CONTENT = "content";
        static final String COLUMN_NAME_START_DATE = "start_date";
        static final String COLUMN_NAME_END_DATE = "end_date";
        static final String COLUMN_NAME_ALARM = "alarm_date";
        static final String COLUMN_NAME_LATITUDE = "latitude";
        static final String COLUMN_NAME_LONGITUDE = "longitude";
        static final String COLUMN_NAME_LOCATION = "location_name";
        static final String COLUMN_NAME_MEMO = "memo";
        static final String COLUMN_NAME_CHECK_DATE = "check_date";
    }

    static class MainScheduleEntry implements BaseColumns{
        static final String TABLE_NAME = "main_schedule";
        static final String COLUMN_NAME_DATE = "date";
        static final String COLUMN_NAME_CONTENT = "content";
        static final String COLUMN_NAME_CHECK_STATE = "check_state";
        static final String COLUMN_NAME_CHECK_DATE = "check_date";
    }
}
