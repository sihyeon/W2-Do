package com.team.codealmanac.w2do.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.codealmanac.w2do.MainActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.service.W2DoForeGroundService;
import com.team.codealmanac.w2do.assistant.GoogleAPIAssistant;
import com.team.codealmanac.w2do.database.PreferencesManager;
import com.team.codealmanac.w2do.dialog.OpensourceDialogActivity;

import static com.team.codealmanac.w2do.MainActivity.mContext;

public class NavSettingFragment extends PreferenceFragment {
    private PreferencesManager mPreferencesManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        mPreferencesManager = new PreferencesManager(getActivity());

        Preference logoutPreference = findPreference("pref_Logout");
        logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                GoogleAPIAssistant googleAPIAssistant = new GoogleAPIAssistant(getActivity(), (AppCompatActivity)getActivity());
//                GoogleAPIAssistant googleAPIAssistant = GoogleAPIAssistant.newInstance(getActivity().getApplicationContext(), (AppCompatActivity)getActivity());
                googleAPIAssistant.signOut();
                mPreferencesManager.deleteNickname();
                return true;
            }
        });

        Preference leavePreference = findPreference("pref_EndW2do");
        leavePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                database.child("user").child(auth.getCurrentUser().getUid()).removeValue();
                database.child("public_users").child(auth.getCurrentUser().getUid()).removeValue();
                database.child("nickname").child(auth.getCurrentUser().getUid()).removeValue();
                auth.signOut();
                GoogleAPIAssistant googleAPIAssistant = new GoogleAPIAssistant(getActivity(), (AppCompatActivity)getActivity());
                googleAPIAssistant.signOut();
                mPreferencesManager.deleteNickname();
                return true;
            }
        });

        final SwitchPreference lockScreenPreference = (SwitchPreference)findPreference("pref_LockscreenService");
        lockScreenPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (Boolean) newValue;
                if(value){
                    Toast.makeText(getActivity(), "잠금 화면 서비스를 실행합니다.", Toast.LENGTH_SHORT).show();
                    getActivity().startService(new Intent(getActivity().getApplicationContext(), W2DoForeGroundService.class));
                } else {
                    Toast.makeText(getActivity(), "잠금 화면 서비스를 종료합니다.", Toast.LENGTH_SHORT).show();
                    getActivity().stopService(new Intent(getActivity().getApplicationContext(), W2DoForeGroundService.class));
                }
                return true;
            }
        });

        SwitchPreference morningPushPreference = (SwitchPreference)findPreference("pref_PushAlarm_morning");
        morningPushPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
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

        Preference versionPreference = findPreference("pref_AppVersionInfo");
        versionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent AppstoreIntent = new Intent(Intent.ACTION_VIEW);
                AppstoreIntent.setData(Uri.parse("market://details?id=com.google.android.apps.maps" ));
                startActivity(AppstoreIntent);
                return true;
            }
        });

        PreferenceScreen openSourcePreferenceScreen = (PreferenceScreen) getPreferenceScreen().findPreference("pref_opensource");
        openSourcePreferenceScreen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent dialogIntent = new Intent(getActivity(),OpensourceDialogActivity.class);
                startActivity(dialogIntent);
                return true;
            }
        });
    }
}

