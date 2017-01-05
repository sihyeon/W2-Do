package com.team.codealmanac.w2do.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Choi Jaeung on 2017-01-05.
 */

public class PreferencesManager {
    public static void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray temp = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            temp.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, temp.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public static void setIntegerArrayPref(Context context, String key, ArrayList<Integer> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray temp = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            temp.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, temp.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public static ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> dataArray = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray temp = new JSONArray(json);
                for (int i = 0; i < temp.length(); i++) {
                    String url = temp.optString(i);
                    dataArray.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataArray;
    }

    public static ArrayList<Integer> getIntegerArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<Integer> dataArray = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray temp = new JSONArray(json);
                for (int i = 0; i < temp.length(); i++) {
                    Integer url = temp.optInt(i);
                    dataArray.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataArray;
    }
}
