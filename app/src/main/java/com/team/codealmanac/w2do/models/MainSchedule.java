package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-04.
 */

@IgnoreExtraProperties
public class MainSchedule {
    public long sequence;
    public String key;
    public String content;
    public String input_date;
    public String visibility;

    public MainSchedule(){
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public MainSchedule(long sequence, String key, String content, String input_date, String visibility) {
        this.sequence = sequence;
        this.key = key;
        this.content = content;
        this.input_date = input_date;
        this.visibility = visibility;
    }
}
