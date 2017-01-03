package com.example.teamalmanac.codealmanac;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sihyeon on 2016-12-09.
 */

public class UsepageFragment2 extends Fragment{

    private static UsepageFragment2 mFragment2;

    public UsepageFragment2(){

    }

    public static UsepageFragment2 getmFragment2(){
        return mFragment2;
    }

    public static UsepageFragment2 newInstance(){
        UsepageFragment2 fragment2 = new UsepageFragment2();
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFragment2 = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.usepage2, container, false);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
