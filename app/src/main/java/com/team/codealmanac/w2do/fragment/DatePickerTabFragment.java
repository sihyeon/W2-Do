package com.team.codealmanac.w2do.fragment;


import android.content.Context;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;
import android.widget.DatePicker;
import android.widget.TextView;

import com.team.codealmanac.w2do.R;


// 상세 입력 페이지에서 일정의 날짜,시간을 입력할 viewpager fragment

public class DatePickerTabFragment extends Fragment {
    private DatePicker datePicker;
    private Calendar calendar;

    public DatePickerTabFragment(){

    }

    public static DatePickerTabFragment newInstance(){
        DatePickerTabFragment datePickerTabFragment = new DatePickerTabFragment();
        return datePickerTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_content_date_picker_layout,container,false);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("date picker onView", view.toString());
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
