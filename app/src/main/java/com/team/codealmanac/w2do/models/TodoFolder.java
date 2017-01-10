package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-11.
 */

@IgnoreExtraProperties
public class TodoFolder {
    public long sequence;
    public String key;
    public String name;

    public TodoFolder(long sequence, String key, String name) {
        this.sequence = sequence;
        this.key = key;
        this.name = name;
    }
}
