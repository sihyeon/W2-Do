package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-21.
 */

@IgnoreExtraProperties
public class SimpleToday {
    public int sequence;
    public String content;
    public boolean check;

    public SimpleToday(){

    }

    public SimpleToday(int sequence, String content, boolean check) {
        this.sequence = sequence;
        this.content = content;
        this.check = check;
    }
}
