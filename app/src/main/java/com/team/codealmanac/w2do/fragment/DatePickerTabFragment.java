package com.team.codealmanac.w2do.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;


// 상세 입력 페이지에서 일정의 날짜,시간을 입력할 viewpager fragment

public class DatePickerTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_content_date_picker_layout,container,false);

    }

}
