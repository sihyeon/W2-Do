package com.team.codealmanac.w2do.models;

/**
 * Created by Choi Jaeung on 2017-05-23.
 */

public class SimpleTodo {
    public long _ID;
    public int check_state;
    public String content;

    public SimpleTodo(long _ID, int check_state, String content) {
        this._ID = _ID;
        this.check_state = check_state;
        this.content = content;
    }
}
