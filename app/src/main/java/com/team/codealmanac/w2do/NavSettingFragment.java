package com.team.codealmanac.w2do;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;


import static com.team.codealmanac.w2do.MainActivity.mContext;

public class NavSettingFragment extends PreferenceFragment {
    public static Context mPrefContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        final SwitchPreference LockscreenPreference = (SwitchPreference)findPreference("pref_LockscreenService");
        LockscreenPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (Boolean) newValue;
                if(value){
                    Toast.makeText(getActivity(), "잠금 화면 서비스를 실행합니다.", Toast.LENGTH_SHORT).show();
                    getActivity().startService(new Intent(getActivity(),W2DoService.class));
                } else {
                    Toast.makeText(getActivity(), "잠금 화면 서비스를 종료합니다.", Toast.LENGTH_SHORT).show();
                    getActivity().stopService(new Intent(getActivity(),W2DoService.class));
                }
                return true;
            }
        });

        SwitchPreference MorningPushPreference = (SwitchPreference)findPreference("pref_PushAlarm_morning");
        MorningPushPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean checked = ((SwitchPreference) preference).isChecked();
                if (checked) {
                    ((MainActivity)mContext).createNotification();
                    Toast.makeText(getActivity(), "아침 일정 서비스를 등록합니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else {

                    Toast.makeText(getActivity(), "아침 일정 서비스를 해제합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        SwitchPreference UpdatePreference = (SwitchPreference)findPreference("pref_AutoUpdate");
        UpdatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean checked = ((SwitchPreference) preference).isChecked();
                if (checked) {
                    Toast.makeText(getActivity(), "자동 업데이트 서비스를 실행합니다.", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(getActivity(), "자동 업데이트 서비스를 해제합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }
}

