package com.team.codealmanac.w2do.bean;

/**
 * Created by Choi Jaeung on 2017-01-04.
 */

public class MainScheduleBeen {
    private long _id;
    private String main_schedule;
    private String date;

    public MainScheduleBeen(long _id, String main_schedule, String date) {
        this._id = _id;
        this.main_schedule = main_schedule;
        this.date = date;
    }

    public long get_id() {
        return _id;
    }

    public String getMain_schedule() {
        return main_schedule;
    }

    public String getDate() {
        return date;
    }
}
