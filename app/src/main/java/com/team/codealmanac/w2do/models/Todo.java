package com.team.codealmanac.w2do.models;

import android.support.annotation.NonNull;

import com.team.codealmanac.w2do.database.SQLContract;

import java.io.Serializable;

/**
 * Created by Choi Jaeung on 2017-01-24.
 */

public class Todo implements Serializable{
    public long _ID;
    public long folder_sequence;
    public int check_state = 0;
    public int color = -15690763;   //블루컬러
    public String folder_name;
    public String content;
    public long start_date;
    public long end_date;
    public long alarm_date = 0;
    public double latitude = 500;
    public double longitude = 500;
    public String location_name;
    public String memo;
    public long check_date;

    //TODO: Simple Input
    public Todo(long date, String content) {
        this.start_date = date;
        this.end_date = date;
        this.content = content;
        this.folder_name = SQLContract.DEFUALT_FOLDER_NAME;
    }

    // InFolder Simple Input
    public Todo(long date, String content, String folder) {
        this.start_date = date;
        this.end_date = date;
        this.content = content;
        this.folder_name = folder;
    }
    public Todo(int color, String folder_name, String content,
                long start_date, long end_date, long alarm_date, double latitude, double longitude, String location_name, String memo) {
        this.color = color;
        this.folder_name = folder_name;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.alarm_date = alarm_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location_name = location_name;
        this.memo = memo;
    }

    public Todo(long _ID, long folder_sequence, int check_state, int color, String folder_name, String content, long start_date, long end_date, long alarm_date, double latitude, double longitude, String location_name, String memo) {
        this._ID = _ID;
        this.folder_sequence = folder_sequence;
        this.check_state = check_state;
        this.color = color;
        this.folder_name = folder_name;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.alarm_date = alarm_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location_name = location_name;
        this.memo = memo;
    }

    public Todo(long _ID, long folder_sequence, int check_state, int color, String folder_name, String content, long start_date, long end_date, long alarm_date, double latitude, double longitude, String location_name, String memo, long check_date) {
        this._ID = _ID;
        this.folder_sequence = folder_sequence;
        this.check_state = check_state;
        this.color = color;
        this.folder_name = folder_name;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.alarm_date = alarm_date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location_name = location_name;
        this.memo = memo;
        this.check_date = check_date;
    }
}
