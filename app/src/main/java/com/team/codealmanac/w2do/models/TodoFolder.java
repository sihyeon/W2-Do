package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-11.
 */

@IgnoreExtraProperties
public class TodoFolder {
    public long sequence;
    public String name;
    public long todo_count;

    public TodoFolder() {
    }

    public TodoFolder(long sequence, String name, long todo_count) {
        this.sequence = sequence;
        this.name = name;
        this.todo_count = todo_count;
    }
}
