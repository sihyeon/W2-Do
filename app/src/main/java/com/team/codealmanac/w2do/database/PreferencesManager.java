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

    private static final String BACKGROUNDSEQUNCE_PREF = "select_backgrounds";
    private static final String BACKGROUNDSEQUNCE_KEY = "background_sequence";

    private Context mContext;
    public PreferencesManager(Context context){
        mContext = context.getApplicationContext();
    }
    //nickname
    public void setNickname(String nickname){
        SharedPreferences userInfoPref = mContext.getSharedPreferences(USERINFO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPref.edit();
        editor.putString(USERINFO_NICKNAME/* key */, nickname);
        editor.apply();
    }
    public String getNickname(){
        SharedPreferences userInfoPref = mContext.getSharedPreferences(USERINFO_PREF, Context.MODE_PRIVATE);
        return userInfoPref.getString(USERINFO_NICKNAME/*key*/, null/*default*/);
    }
    public void deleteNickname(){
        SharedPreferences userInfoPref = mContext.getSharedPreferences(USERINFO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPref.edit();
        editor.remove(USERINFO_NICKNAME);
        editor.apply();
    }

    public void setLockScreenType(String TYPE){
        SharedPreferences lockScreenTypePref = mContext.getSharedPreferences(LOCKSCREEN_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lockScreenTypePref.edit();
        editor.putString(LOCKSCREEN_TYPE/* key */, TYPE);
        editor.apply();
    }
    public String getLockScreenType(){
        SharedPreferences lockScreenTypePrefPref = mContext.getSharedPreferences(LOCKSCREEN_PREF, Context.MODE_PRIVATE);
        return lockScreenTypePrefPref.getString(LOCKSCREEN_TYPE/*key*/, LockScreenActivity.TYPE_MAINSCHEDULE/*default*/);
    }

    public void setBackGroundSequence(int sequence){
        SharedPreferences lockScreenTypePref = mContext.getSharedPreferences(BACKGROUNDSEQUNCE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lockScreenTypePref.edit();
        editor.putInt(BACKGROUNDSEQUNCE_KEY/* key */, sequence);
        editor.apply();
    }
    public int getBackGroundSquence(){
        SharedPreferences lockScreenTypePrefPref = mContext.getSharedPreferences(BACKGROUNDSEQUNCE_PREF, Context.MODE_PRIVATE);
        return lockScreenTypePrefPref.getInt(BACKGROUNDSEQUNCE_KEY/*key*/, 1/*default*/);
    }
}
