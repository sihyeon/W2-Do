package com.team.codealmanac.w2do.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Choi Jaeung on 2017-01-10.
 */

@IgnoreExtraProperties
public class User {
    public String email;
    public String display_name;
    public String profile_image;

    public User() {
    }

    public User(String email, String display_name, String profile_image) {
        this.email = email;
        this.display_name = display_name;
        this.profile_image = profile_image;
    }
}
