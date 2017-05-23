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

    //투두 테이블
    public static class TodoFolderEntry implements BaseColumns{
        public static final String TABLE_NAME = "todo_folder";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SEQUENCE = "sequence";
        public static final String COLUMN_NAME_TODO_COUNT = "todo_count";
    }

    public static class TodoEntry implements BaseColumns{
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_NAME_SEQUENCE = "folder_sequence";
        public static final String COLUMN_NAME_CHECK = "check_state";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_FOLDER = "folder_name";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
        public static final String COLUMN_NAME_ALARM = "alarm_date";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LOCATION = "location_name";
        public static final String COLUMN_NAME_MEMO = "memo";
    }
}
