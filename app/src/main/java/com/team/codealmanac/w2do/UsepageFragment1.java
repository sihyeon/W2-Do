package com.team.codealmanac.w2do;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sihyeon on 2016-12-09.
 */

public class UsepageFragment1 extends Fragment{

    private static UsepageFragment1 mFragment;

    public UsepageFragment1(){

    }

    public static UsepageFragment1 getUsepageFragment1(){
        return mFragment;
    }

    public static UsepageFragment1 newInstance(){
        UsepageFragment1 fragment = new UsepageFragment1();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.use_page1, container, false);

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
