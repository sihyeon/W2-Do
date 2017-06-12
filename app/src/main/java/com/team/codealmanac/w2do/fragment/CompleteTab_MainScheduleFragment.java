package com.team.codealmanac.w2do.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.team.codealmanac.w2do.CompleteTabActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.CompleteAdapter;
import com.team.codealmanac.w2do.adapter.MainScheduleAdapter;

/**
 * Created by sihyeon on 2017-05-31.
 */

public class CompleteTab_MainScheduleFragment extends android.support.v4.app.Fragment {
    public CompleteAdapter mCompleteAdapter;
    private RecyclerView tabfragment_mainschedule_recyclerview;

    public CompleteTab_MainScheduleFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tabfragment_mainschedule_complete,container,false);
        tabfragment_mainschedule_recyclerview = (RecyclerView) view.findViewById(R.id.tabfragment_mainschedule_recyclerview);
        tabfragment_mainschedule_recyclerview.setHasFixedSize(true);
        tabfragment_mainschedule_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mCompleteAdapter = new CompleteAdapter(getContext(), CompleteAdapter.TYPE_MAINSCHEDULE);
        mCompleteAdapter.setOnCompleteAdapterListener((CompleteTabActivity)getContext());
        tabfragment_mainschedule_recyclerview.setAdapter(mCompleteAdapter);
        return view;
    }
}
