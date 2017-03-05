package com.team.codealmanac.w2do;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by sihyeon on 2017-01-15.
 */

public class SimpleInputActivity extends AppCompatActivity {

    private TextView headerText;
    private EditText todayedittext;
    private Button todayCancelBtn;
    private Button todaySaveBtn;
    private FontContract mFontContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_simpleinput);

        mFontContract = new FontContract(getApplication().getAssets());

        headerText = (TextView) findViewById(R.id.today_header_text);
        todayedittext = (EditText) findViewById(R.id.today_simple_edittext);
        todayCancelBtn = (Button) findViewById(R.id.today_cancel_btn);
        todaySaveBtn = (Button) findViewById(R.id.today_submit_btn);

        todayedittext.setTypeface(mFontContract.NahumSquareR_Regular());
        headerText.setTypeface(mFontContract.FranklinGothic_MediumCond());
        todayCancelBtn.setTypeface(mFontContract.NahumSquareR_Regular());
        todaySaveBtn.setTypeface(mFontContract.NahumSquareR_Regular());

        InputFilter[] FilterArray = new  InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(140);
        todayedittext.setFilters(FilterArray);

    }
}
