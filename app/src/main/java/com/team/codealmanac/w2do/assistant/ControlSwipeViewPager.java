package com.team.codealmanac.w2do.assistant;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Choi Jaeung on 2017-06-11.
 */

public class ControlSwipeViewPager extends ViewPager {
    private final String TAG = ControlSwipeViewPager.class.getSimpleName();

    private boolean isPaging;

    public ControlSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPaging = true;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (this.isPaging) {
                return super.onTouchEvent(event);
            }
        } catch (Exception e) {
            Log.e(TAG, "onTouchEvent: " + e);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPaging && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled() {
        //스크롤 풀기
        this.isPaging = true;
    }

    public void setPagingDisabled() {
        //스크롤 막기
        this.isPaging = false;
    }
}
