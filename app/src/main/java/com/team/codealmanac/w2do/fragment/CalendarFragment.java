package com.team.codealmanac.w2do.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team.codealmanac.w2do.R;


/**
 * Created by sihyeon on 2017-01-09.
 */

public class CalendarFragment extends Fragment{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager)view.findViewById(R.id.frag_calendar_viewpager);
//        mViewPager.addView();
        mViewPager.setAdapter(mSectionsPagerAdapter);

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

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
