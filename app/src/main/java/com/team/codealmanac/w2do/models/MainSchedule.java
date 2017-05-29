package com.team.codealmanac.w2do.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Choi Jaeung on 2017-01-04.
 */

public class MainSchedule {
    public long _ID;
    public long date;
    public String content;
    public int check_state;

    public MainSchedule(long _ID, long date, String content, int check_state) {
        this._ID = _ID;
        this.date = date;
        this.content = content;
        this.check_state = check_state;
    }
}
