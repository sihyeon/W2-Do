package com.team.codealmanac.w2do.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.team.codealmanac.w2do.fragment.DatePickerTabFragment;
import com.team.codealmanac.w2do.fragment.TimePickerTabFragment;


public class TabPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                DatePickerTabFragment datePickerTabFragment = new DatePickerTabFragment();
                return datePickerTabFragment;
            case 1:
                TimePickerTabFragment timePickerTabFragment = new TimePickerTabFragment();
                return timePickerTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}