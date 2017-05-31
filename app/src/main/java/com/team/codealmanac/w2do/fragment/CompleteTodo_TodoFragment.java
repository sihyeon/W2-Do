package com.team.codealmanac.w2do.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.codealmanac.w2do.R;

/**
 * Created by sihyeon on 2017-05-31.
 */

public class CompleteTodo_TodoFragment extends android.support.v4.app.Fragment {
    public CompleteTodo_TodoFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.tabfragment_todo_complete, container, false);
    }
}
