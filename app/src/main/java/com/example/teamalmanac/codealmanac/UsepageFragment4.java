package com.example.teamalmanac.codealmanac;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sihyeon on 2016-12-09.
 */

public class UsepageFragment4 extends Fragment{

    private static UsepageFragment4 mFragment4;

    public UsepageFragment4(){

    }

    public static UsepageFragment4 getmFragment4(){
        return mFragment4;
    }

    public static UsepageFragment4 newInstance(){
        UsepageFragment4 fragment = new UsepageFragment4();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFragment4 = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.usepage4, container, false);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
