package com.example.teamalmanac.codealmanac;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sihyeon on 2016-12-09.
 */

public class UsepageFragment3 extends Fragment{

    private static UsepageFragment3 mFragment3;

    public UsepageFragment3(){

    }

    public static UsepageFragment3 getmFragment3(){
        return mFragment3;
    }

    public static UsepageFragment3 newInstance(){
        UsepageFragment3 fragment = new UsepageFragment3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFragment3 = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.usepage3, container, false);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
