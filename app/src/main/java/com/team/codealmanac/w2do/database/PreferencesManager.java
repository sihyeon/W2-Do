package com.team.codealmanac.w2do.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.team.codealmanac.w2do.LockScreenActivity;

/**
 * Created by Choi Jaeung on 2017-01-05.
 */

public final class PreferencesManager {
    private static final String USERINFO_PREF = "user_info";
    private static final String USERINFO_NICKNAME = "nickname";

    private static final String LOCKSCREEN_PREF = "lock_screen";
    private static final String LOCKSCREEN_TYPE = "type";
    //nickname
    public static void setNickname(Context context, String nickname){
        context = context.getApplicationContext();
        SharedPreferences userInfoPref = context.getSharedPreferences(USERINFO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPref.edit();
        editor.putString(USERINFO_NICKNAME/* key */, nickname);
        editor.apply();
    }
    public static String getNickname(Context context){
        context = context.getApplicationContext();
        SharedPreferences userInfoPref = context.getSharedPreferences(USERINFO_PREF, Context.MODE_PRIVATE);
        return userInfoPref.getString(USERINFO_NICKNAME/*key*/, null/*default*/);
    }
    public static void deleteNickname(Context context){
        context = context.getApplicationContext();
        SharedPreferences userInfoPref = context.getSharedPreferences(USERINFO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPref.edit();
        editor.remove(USERINFO_NICKNAME);
        editor.apply();
    }

    public static void setLockScreenType(Context context, String TYPE){
        context = context.getApplicationContext();
        SharedPreferences lockScreenTypePref = context.getSharedPreferences(LOCKSCREEN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lockScreenTypePref.edit();
        editor.putString(LOCKSCREEN_TYPE/* key */, TYPE);
        editor.apply();
    }
    public static String getLockScreenType(Context context){
        context = context.getApplicationContext();
        SharedPreferences lockScreenTypePrefPref = context.getSharedPreferences(LOCKSCREEN_PREF, Context.MODE_PRIVATE);
        return lockScreenTypePrefPref.getString(LOCKSCREEN_TYPE/*key*/, LockScreenActivity.TYPE_MAINSCHEDULE/*default*/);
    }

}
