package com.team.codealmanac.w2do.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Choi Jaeung on 2017-01-04.
 */

@IgnoreExtraProperties
public class MainSchedule {
    public String content;          //내용
    public long input_date;       //입력한 날짜 밀리세컨드로

    public MainSchedule(){
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public MainSchedule(String content, long input_date) {
        this.content = content;
        this.input_date = input_date;
    }

    @Exclude
    public Map<String, Object> toVisibleMainSchedule(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", this.content);
        return result;
    }
}
