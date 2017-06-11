package com.team.codealmanac.w2do.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.CompleteTabActivity;
import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.CompleteAdapter;
import com.team.codealmanac.w2do.adapter.InFolderListAdapter;

/**
 * Created by sihyeon on 2017-05-31.
 */

public class CompleteTab_TodoFragment extends android.support.v4.app.Fragment {
    private RecyclerView tabfragment_todo_recyclerview;
    public CompleteAdapter mCompleteAdapter;
    public CompleteTab_TodoFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.tabfragment_todo_complete, container, false);
        tabfragment_todo_recyclerview = (RecyclerView)view.findViewById(R.id.tabfragment_todo_recyclerview);
        tabfragment_todo_recyclerview.setHasFixedSize(true);
        tabfragment_todo_recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mCompleteAdapter = new CompleteAdapter(getContext(), CompleteAdapter.TYPE_TODO);
        mCompleteAdapter.setOnCompleteAdapterListener((CompleteTabActivity)getContext());
        tabfragment_todo_recyclerview.setAdapter(mCompleteAdapter);
        return view;
    }
}
