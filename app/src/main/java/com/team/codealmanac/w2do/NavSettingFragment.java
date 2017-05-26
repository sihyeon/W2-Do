package com.team.codealmanac.w2do;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class NavSettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);
    }
}

