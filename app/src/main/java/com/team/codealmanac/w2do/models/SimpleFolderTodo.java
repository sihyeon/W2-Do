package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-21.
 */

@IgnoreExtraProperties
public class SimpleFolderTodo {
    public String key;
    public int folder_sequence;
    public String content;
    public boolean check_state;

    public SimpleFolderTodo(){}

    public SimpleFolderTodo(int folder_sequence, String content, boolean check_state) {
        this.folder_sequence = folder_sequence;
        this.content = content;
        this.check_state = check_state;
    }
}
