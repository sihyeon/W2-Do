package com.team.codealmanac.w2do.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.team.codealmanac.w2do.DetailInputActivity;
import com.team.codealmanac.w2do.R;

import java.util.Calendar;


public class DatePickerTabFragment extends DialogFragment {
    static final int START_DATE_PICKER_ID = 00;
    static final int END_DATE_PICKER_ID = 01;
    static final int START_TIME_PICKER_ID = 10;
    static final int END_TIME_PICKER_ID = 11;
    int syear,smonth,sday;
    DatePickerDialog.OnDateSetListener start_dateListener,end_dateListener;

    public DatePickerTabFragment() {
        // nothing to see here, move along
    }

    public static DatePickerTabFragment newInstance(){

        DatePickerTabFragment datePickerTabFragment = new DatePickerTabFragment();
        return datePickerTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("date picker onView", view.toString());
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        syear = c.get(Calendar.YEAR);
        smonth = c.get(Calendar.MONTH);
        sday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)
                getActivity(), syear, smonth, sday);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,"DONE", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANDEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().dismiss();
            }
        });

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)
                getActivity(), syear, smonth, sday);
    }
}