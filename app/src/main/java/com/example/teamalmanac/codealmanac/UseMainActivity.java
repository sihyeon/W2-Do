package com.example.teamalmanac.codealmanac;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.view.WindowManager;

import com.example.teamalmanac.codealmanac.database.DataManager;

public class UseMainActivity extends AppCompatActivity {
    private final int PERMISSIONS_REQUEST = 1;
    //마지막 페이지에서 스와이핑하는걸 검출하기 위한 변수
    private int mPageScroll;
    //context를 담기 위한 변수
    private static Context mContext;
    private static Activity mActivity;
    //Fragment 수
    private static final int FRAGMENT_TOTAL_NUMBER = 4;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상태바 없앰
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.useactivity);
//        mContext = getApplicationContext();
        mActivity = this;
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(mListener);
        mViewPager.setCurrentItem(0);
    }

    public static Activity getUseActivity() {
        return mActivity;
    }

//  //  public static Context getContext() {
//        return mContext;
//    }


    private ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position == FRAGMENT_TOTAL_NUMBER - 1 && positionOffset == 0) {
                if (mPageScroll != 0) finish();
                mPageScroll++;
            } else {
                mPageScroll = 0;
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return UsepageFragment1.newInstance();
            } else if (position == 1) {
                return UsepageFragment2.newInstance();
            } else if (position == 2) {
                return UsepageFragment3.newInstance();
            } else {
                return UsepageFragment4.newInstance();
            }

        }
            @Override
            public int getCount() {
                // Show 4 total pages.
                return 4;
            }
        }
    }
