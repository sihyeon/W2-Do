package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-24.
 */
@IgnoreExtraProperties
public class Todo {
    public long folder_sequence;
    public boolean check_state;
    public int color;
    public String folder_name;
    public String content;
    public long start_date;
    public long end_date;
    public long alarm_date;
    public String sharing;
    public double latitude;
    public double longitude;
    public String memo;

    public Todo() {}

    public Todo(long folder_sequence, long date, String content, String folder_name) {
        this.folder_sequence = folder_sequence;
        this.start_date = date;
        this.end_date = date;
        this.content = content;
        this.folder_name = folder_name;
    }

    public Todo(long folder_sequence, boolean check_state, int color, String folder_name, String content, long start_date, long end_date, long alarm_date, String sharing, double latitude, double longitude, String memo) {
        this.folder_sequence = folder_sequence;
        this.check_state = check_state;
        this.color = color;
        this.folder_name = folder_name;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.alarm_date = alarm_date;
        this.sharing = sharing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
    }
}
