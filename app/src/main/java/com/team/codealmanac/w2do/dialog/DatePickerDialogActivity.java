package com.team.codealmanac.w2do.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.team.codealmanac.w2do.R;

import java.util.Calendar;

public class DatePickerDialogActivity extends Activity implements View.OnClickListener{
    private ViewPager act_date_picker_dialog_viewpager;
    private Button act_date_picker_dialog_okButton;
    private DatePicker act_date_picker_dialog_datePicker;
    private TimePicker act_date_picker_dialog_timePicker;

    private PagerAdapter mAdapter;

    private class PagerAdapter extends android.support.v4.view.PagerAdapter {
        protected LayoutInflater mInflater;

        public PagerAdapter(Context c) {
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = null;
            if(position == 0){
                v = mInflater.inflate(R.layout.activity_date_picker_dialog_datepicker, null);
                act_date_picker_dialog_datePicker = (DatePicker)v.findViewById(R.id.act_date_picker_dialog_datePicker);
            } else if(position == 1){
                v = mInflater.inflate(R.layout.activity_date_picker_dialog_timepicker, null);
                act_date_picker_dialog_timePicker = (TimePicker)v.findViewById(R.id.act_date_picker_dialog_timePicker);
            }
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView((View)object);
        }

        @Override
        public int getCount() {return 2;}

        @Override
        public boolean isViewFromObject(View view, Object object) {return view == object;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_date_picker_dialog);
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags  = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount  = 0.7f;

        act_date_picker_dialog_viewpager = (ViewPager) findViewById(R.id.act_date_picker_dialog_viewpager);

        mAdapter = new PagerAdapter(getApplicationContext());
        act_date_picker_dialog_viewpager.setAdapter(mAdapter);

        act_date_picker_dialog_okButton = (Button)findViewById(R.id.act_date_picker_dialog_okButton);
        act_date_picker_dialog_okButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.act_date_picker_dialog_okButton){
            Calendar calendar = Calendar.getInstance();
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                calendar.set(act_date_picker_dialog_datePicker.getYear(),
                        act_date_picker_dialog_datePicker.getMonth(),
                        act_date_picker_dialog_datePicker.getDayOfMonth(),
                        act_date_picker_dialog_timePicker.getHour(),
                        act_date_picker_dialog_timePicker.getMinute());
            } else {
                calendar.set(act_date_picker_dialog_datePicker.getYear(),
                        act_date_picker_dialog_datePicker.getMonth(),
                        act_date_picker_dialog_datePicker.getDayOfMonth(),
                        act_date_picker_dialog_timePicker.getCurrentHour(),
                        act_date_picker_dialog_timePicker.getCurrentMinute());
            }
            intent.putExtra("date", calendar.getTimeInMillis());
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
