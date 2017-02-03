package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-25.
 */
@IgnoreExtraProperties
public class User_NickName {
    public String nickname;

    public User_NickName() {
    }

    public User_NickName(String nickname) {
        this.nickname = nickname;
    }
}
