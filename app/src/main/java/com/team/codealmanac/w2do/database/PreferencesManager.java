package com.team.codealmanac.w2do.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-01-05.
 */

public final class PreferencesManager {
    //nickname
    public static void setNickname(Context context, String nickname){
        context = context.getApplicationContext();
        SharedPreferences userInfoPref = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPref.edit();
        editor.putString("nickname"/* key */, nickname);
        editor.apply();
    }
    public static String getNickname(Context context){
        context = context.getApplicationContext();
        SharedPreferences userInfoPref = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return userInfoPref.getString("nickname"/*key*/, null/*default*/);
    }
    public static void deleteNickname(Context context){
        context = context.getApplicationContext();
        SharedPreferences userInfoPref = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfoPref.edit();
        editor.remove("nickname");
        editor.apply();
    }

}
