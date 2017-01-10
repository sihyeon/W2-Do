package com.team.codealmanac.w2do.models;

/**
 * Created by Choi Jaeung on 2017-01-04.
 */

public class MainSchedule {
    public long _id;
    public String main_schedule;
    public String date;

    public MainSchedule(){
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public MainSchedule(long _id, String main_schedule, String date) {
        this._id = _id;
        this.main_schedule = main_schedule;
        this.date = date;
    }
}
