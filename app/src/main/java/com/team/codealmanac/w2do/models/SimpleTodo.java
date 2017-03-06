package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-21.
 */

@IgnoreExtraProperties
public class SimpleTodo {
    public long today_sequence;
    public long date;
    public String content;
    public boolean visible;
    public boolean check_state;

    public SimpleTodo(){}

    public SimpleTodo(long today_sequence, long date, String content, boolean visible, boolean check_state) {
        this.today_sequence = today_sequence;
        this.date = date;
        this.content = content;
        this.visible = visible;
        this.check_state = check_state;
    }
}
