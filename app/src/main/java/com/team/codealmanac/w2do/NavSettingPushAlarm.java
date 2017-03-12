package com.team.codealmanac.w2do;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;

import com.team.codealmanac.w2do.AppCompatPreferenceActivity;
import com.team.codealmanac.w2do.NavSettingActivity;
import com.team.codealmanac.w2do.R;

/**
 * Created by sihyeon on 2017-03-12.
 */

public class NavSettingPushAlarm extends AppCompatPreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PushAlarmPreferenceFragment()).commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // PreferenceFragment 클래스 사용
    public static class PushAlarmPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_pushalarm);
        }
    }
}
