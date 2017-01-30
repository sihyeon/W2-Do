package com.team.codealmanac.w2do.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;

/**
 * Created by sihyeon on 2017-01-30.
 */

public class TimePickerTabFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_content_time_picker_layout,container,false);

    }
}
