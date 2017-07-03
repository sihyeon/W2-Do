package com.team.codealmanac.w2do;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team.codealmanac.w2do.adapter.CompleteAdapter;
import com.team.codealmanac.w2do.assistant.ControlSwipeViewPager;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.dialog.DeleteDialogFragment;
import com.team.codealmanac.w2do.fragment.CompleteTab_MainScheduleFragment;
import com.team.codealmanac.w2do.fragment.CompleteTab_TodoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sihyeon on 2017-05-30.
 */

public class CompleteTabActivity extends AppCompatActivity
        implements CompleteAdapter.CompleteAdapterListener, DeleteDialogFragment.DeleteDialogListener {

    private Toolbar act_complete_toolbar;
    private TabLayout act_complete_tablayout;
    private ControlSwipeViewPager act_complete_viewpager;

    private TextView act_complete_toolbar_title_tv;
    private ImageButton act_complete_toolbar_delete_btn;
    private String mAdapterType;

    private CompleteTab_MainScheduleFragment mCompleteTab_mainScheduleFragment;
    private CompleteTab_TodoFragment mCompleteTab_todoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completelist_tab);

        act_complete_toolbar = (Toolbar) findViewById(R.id.act_complete_toolbar);
        setSupportActionBar(act_complete_toolbar);
        FontContract font = new FontContract(getAssets());
        act_complete_viewpager = (ControlSwipeViewPager) findViewById(R.id.act_complete_viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mCompleteTab_mainScheduleFragment = new CompleteTab_MainScheduleFragment();
        mCompleteTab_todoFragment = new CompleteTab_TodoFragment();
        adapter.addFragment(mCompleteTab_mainScheduleFragment, "Main Schedule");
        adapter.addFragment(mCompleteTab_todoFragment, "To do");
        act_complete_viewpager.setAdapter(adapter);

        act_complete_tablayout = (TabLayout) findViewById(R.id.act_complete_tablayout);
        act_complete_tablayout.setupWithViewPager(act_complete_viewpager);

        ViewGroup vg = (ViewGroup) act_complete_tablayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(font.RobotoMedium());
                }
            }
        }

        ImageButton act_complete_toolbar_back_btn = (ImageButton) findViewById(R.id.act_complete_toolbar_back_btn);
        act_complete_toolbar_title_tv = (TextView) findViewById(R.id.act_complete_toolbar_title_tv);
        act_complete_toolbar_title_tv.setTypeface(font.NahumSquareB_Regular());

        act_complete_toolbar_delete_btn = (ImageButton) findViewById(R.id.act_complete_toolbar_delete_btn);

        act_complete_toolbar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (act_complete_toolbar_delete_btn.getVisibility() == View.VISIBLE) {
                    endMultiClick();
                    if (mAdapterType.equals(CompleteAdapter.TYPE_TODO))
                        mCompleteTab_todoFragment.mCompleteAdapter.updateList();
                    else mCompleteTab_mainScheduleFragment.mCompleteAdapter.updateList();
                } else {
                    finish();
                }
            }
        });
        act_complete_toolbar_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterType.equals(CompleteAdapter.TYPE_TODO))
                    DeleteDialogFragment.newInstance(DeleteDialogFragment.TYPE_TODO, null).show(getFragmentManager(), "delete_todo");
                else
                    DeleteDialogFragment.newInstance(DeleteDialogFragment.TYPE_MAINSCHEDULE, null).show(getFragmentManager(), "delete_todo");

            }
        });
    }

    private void endMultiClick() {
        act_complete_toolbar_delete_btn.setVisibility(View.GONE);
        act_complete_toolbar_title_tv.setText(getString(R.string.complete_activity_title));
        act_complete_viewpager.setPagingEnabled();
        LinearLayout tabStrip = ((LinearLayout)act_complete_tablayout.getChildAt(0));
        tabStrip.setEnabled(true);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(true);
        }
    }

    //[START CompleteAdapterListener]
    @Override
    public void onItemLongClick(String type) {
        mAdapterType = type;
        act_complete_toolbar_delete_btn.setVisibility(View.VISIBLE);
        act_complete_viewpager.setPagingDisabled();
        LinearLayout tabStrip = ((LinearLayout)act_complete_tablayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }
        if (mAdapterType.equals(CompleteAdapter.TYPE_TODO)){
            mCompleteTab_todoFragment.isLongClicked = true;
        }
    }

    @Override
    public void onMultiClick(int count) {
        act_complete_toolbar_title_tv.setText(count + "개");
    }
    //[END CompleteAdapterListener]

    //DeleteDialogListener
    @Override
    public void OnDelete() {
        endMultiClick();
        if (mAdapterType.equals(CompleteAdapter.TYPE_TODO)){
            mCompleteTab_todoFragment.mCompleteAdapter.deleteWithMulti();
            mCompleteTab_todoFragment.isLongClicked = false;
        }
        else mCompleteTab_mainScheduleFragment.mCompleteAdapter.deleteWithMulti();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
