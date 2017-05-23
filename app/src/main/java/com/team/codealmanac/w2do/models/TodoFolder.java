package com.team.codealmanac.w2do.models;

/**
 * Created by Choi Jaeung on 2017-01-11.
 */

public class TodoFolder {
    public String name;
    public int sequence;
    public int todo_count;

    public TodoFolder() {
    }

    public TodoFolder(String name, int sequence, int todo_count) {
        this.name = name;
        this.sequence = sequence;
        this.todo_count = todo_count;
    }
}
