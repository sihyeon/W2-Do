package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-10.
 */

@IgnoreExtraProperties
public class User {
    public String email;
    public String user_name;
    public String last_login_date;
    public String user_profile_image;

    public User() {
    }

    public User(String email, String user_name, String last_login_date, String user_profile_image) {
        this.email = email;
        this.user_name = user_name;
        this.last_login_date = last_login_date;
        this.user_profile_image = user_profile_image;
    }
}
