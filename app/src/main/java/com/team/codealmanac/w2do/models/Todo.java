package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-24.
 */
@IgnoreExtraProperties
public class Todo {
    public long today_sequence;
    public long folder_sequence;
    public boolean check_state;
    public String color;
    public String folder_name;
    public String content;
    public long start_date;
    public long end_date;
    public long alarm_date;
    public boolean alarm_recycle;
    public String sharing;
    public int latitude;
    public int longitude;
    public String memo;
    public boolean visible;

    public Todo() {}

    public Todo(long today_sequence, long date, String content, String folder_name) {
        this.today_sequence = today_sequence;
        this.start_date = date;
        this.end_date = date;
        this.content = content;
        this.folder_name = folder_name;
    }

    public Todo(long today_sequence, long folder_sequence, boolean check_state, String color, String folder_name, String content, long start_date, long end_date, long alarm_date, boolean alarm_recycle, String sharing, int latitude, int longitude, String memo, boolean visible) {
        this.today_sequence = today_sequence;
        this.folder_sequence = folder_sequence;
        this.check_state = check_state;
        this.color = color;
        this.folder_name = folder_name;
        this.content = content;
        this.start_date = start_date;
        this.end_date = end_date;
        this.alarm_date = alarm_date;
        this.alarm_recycle = alarm_recycle;
        this.sharing = sharing;
        this.latitude = latitude;
        this.longitude = longitude;
        this.memo = memo;
        this.visible = visible;
    }
}
