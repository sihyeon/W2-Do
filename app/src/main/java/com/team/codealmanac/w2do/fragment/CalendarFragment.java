package com.team.codealmanac.w2do.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.codealmanac.w2do.R;


/**
 * Created by sihyeon on 2017-01-09.
 */

public class CalendarFragment extends Fragment{

    public CalendarFragment() {
        // Required empty public constructor
    }
    public static CalendarFragment newInstance() {
        CalendarFragment calfragment = new CalendarFragment();
        return calfragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_checkbox_layout, container, false);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
