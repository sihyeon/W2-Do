package com.team.codealmanac.w2do;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;
import com.team.codealmanac.w2do.MainActivity;

import static com.team.codealmanac.w2do.MainActivity.mContext;

public class NavSettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        SwitchPreference LockscreenPreference = (SwitchPreference)findPreference("pref_LockscreenService");
        LockscreenPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                boolean checked = ((SwitchPreference) preference).isChecked();
                    if(checked){
                        Toast.makeText(getActivity(),"잠금화면 서비스를 실행합니다.", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        Toast.makeText(getActivity(),"잠금화면 서비스를 해제합니다.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
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
                    return true;
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
                    return true;
                }
            }
        });
    }

}

