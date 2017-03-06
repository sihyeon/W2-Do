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


}
