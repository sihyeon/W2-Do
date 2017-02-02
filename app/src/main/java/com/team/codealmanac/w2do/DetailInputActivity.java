package com.team.codealmanac.w2do;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.team.codealmanac.w2do.fragment.DatePickerTabFragment;
import com.team.codealmanac.w2do.fragment.TimePickerTabFragment;


import java.util.Calendar;

import petrov.kristiyan.colorpicker.ColorPicker;

public class DetailInputActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    // 첫 번째 cardview items : 폴더 선택,내용입력,색상 설정
    private CardView act_detailInput_title_cardview;
    private Spinner act_detailInput_folder_spinner;
    private TextView act_detailInput_title_text;
    private Button act_detailInput_color_picker;
    private EditText act_detailInput_content_edittext;
    private TextView act_detailInput_folder_text;

    // 두 번째 cardview items : 캘린더,시간설정
    private CardView act_detailInput_calendar_cardview;
    private TextView act_detailInput_calendar_start_text;
    private TextView act_detailInput_calendar_end_text;
    private TextView act_detailInput_start_date_display;
    private TextView act_detailInput_end_date_display;
    private Button act_detailInput_calendar_allday_btn;
    private TextView act_detailInput_start_time_display;
    private TextView act_detailInput_end_time_display;
    private DatePickerDialog datePickerDialog;

    // 세 번째 cardview items: more detail buttons
    private CardView act_detailInput_more_detail_btn_cardview;
    private TextView act_detailInput_more_detail_title_text;
    private Button act_detailInput_more_detail_side_btn_gps;
    private Button act_detailInput_more_detail_side_btn_alarm;
    private Button act_detailInput_more_detail_side_btn_memo;
    private Button act_detailInput_more_detail_side_btn_share;

    static final int START_DATE_PICKER_ID = 00;
    static final int END_DATE_PICKER_ID = 01;
    static final int START_TIME_PICKER_ID = 10;
    static final int END_TIME_PICKER_ID = 11;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // first cardview items
        act_detailInput_title_cardview = (CardView)findViewById(R.id.act_detailInput_title_cardview);
        act_detailInput_title_text = (TextView)findViewById(R.id.act_detailInput_title_text);
        act_detailInput_color_picker = (Button)findViewById(R.id.act_detailInput_color_picker);
        act_detailInput_content_edittext = (EditText)findViewById(R.id.act_detailInput_content_edittext);
        act_detailInput_folder_text = (TextView)findViewById(R.id.act_detailInput_folder_text);
        act_detailInput_folder_spinner = (Spinner)findViewById(R.id.act_detailInput_folder_spinner);

        // second cardview items
        act_detailInput_calendar_cardview = (CardView)findViewById(R.id.act_detailInput_calendar_cardview);
        act_detailInput_calendar_start_text = (TextView)findViewById(R.id.act_detailInput_calendar_start_text);
        act_detailInput_calendar_end_text = (TextView)findViewById(R.id.act_detailInput_calendar_end_text);
        act_detailInput_start_date_display = (TextView)findViewById(R.id.act_detailInput_start_date_display);
        act_detailInput_end_date_display = (TextView)findViewById(R.id.act_detailInput_end_date_display);
        act_detailInput_start_time_display = (TextView)findViewById(R.id.act_detailInput_start_time_display);
        act_detailInput_end_time_display = (TextView)findViewById(R.id.act_detailInput_end_time_display);
        act_detailInput_calendar_allday_btn = (Button)findViewById(R.id.act_detailInput_calendar_allday_btn);

        //third cardview items
        act_detailInput_more_detail_btn_cardview = (CardView)findViewById(R.id.act_detailInput_more_detail_btn_cardview);
        act_detailInput_more_detail_title_text = (TextView)findViewById(R.id.act_detailInput_more_detail_title_text);
        act_detailInput_more_detail_side_btn_gps = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_gps);
        act_detailInput_more_detail_side_btn_alarm = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_alarm);
        act_detailInput_more_detail_side_btn_memo = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_memo);
        act_detailInput_more_detail_side_btn_gps = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_gps);
        act_detailInput_more_detail_side_btn_share = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_share);

        //OnClickListener 연결
        act_detailInput_color_picker.setOnClickListener(this);
        act_detailInput_start_date_display.setOnClickListener(this);
        act_detailInput_end_date_display.setOnClickListener(this);
        act_detailInput_start_time_display.setOnClickListener(this);
        act_detailInput_end_time_display.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_gps.setOnClickListener(this);

        // 폴더 선택 spinner adapter
        String[] Folder_Spinner_item = getResources().getStringArray(R.array.folder);
        ArrayAdapter<String> Folder_Spinner_Adapter = new ArrayAdapter<String>(
                this, R.layout.activity_detailinput_folder_spinner_textview, Folder_Spinner_item);
        Folder_Spinner_Adapter.setDropDownViewResource(R.layout.activity_detailinput_folder_spinner_textview);
        act_detailInput_folder_spinner.setAdapter(Folder_Spinner_Adapter);

    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id. act_detailInput_color_picker:
                final ColorPicker colorPicker = new ColorPicker(DetailInputActivity.this);
                // 다이얼로그 레이아웃 배경색 지정
                colorPicker.getDialogBaseLayout().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                // 팔레트 색상 지정
                colorPicker.setColors(
                        ContextCompat.getColor(getApplicationContext(),R.color.red),
                        ContextCompat.getColor(getApplicationContext(),R.color.pink),
                        ContextCompat.getColor(getApplicationContext(),R.color.purple),
                        ContextCompat.getColor(getApplicationContext(),R.color.deep_purple),
                        ContextCompat.getColor(getApplicationContext(),R.color.indigo),
                        ContextCompat.getColor(getApplicationContext(),R.color.blue),
                        ContextCompat.getColor(getApplicationContext(),R.color.light_blue),
                        ContextCompat.getColor(getApplicationContext(),R.color.cyan),
                        ContextCompat.getColor(getApplicationContext(),R.color.teal),
                        ContextCompat.getColor(getApplicationContext(),R.color.green),
                        ContextCompat.getColor(getApplicationContext(),R.color.light_green),
                        ContextCompat.getColor(getApplicationContext(),R.color.lime),
                        ContextCompat.getColor(getApplicationContext(),R.color.yellow),
                        ContextCompat.getColor(getApplicationContext(),R.color.amber),
                        ContextCompat.getColor(getApplicationContext(),R.color.orange),
                        ContextCompat.getColor(getApplicationContext(),R.color.deep_orange),
                        ContextCompat.getColor(getApplicationContext(),R.color.brown),
                        ContextCompat.getColor(getApplicationContext(),R.color.gray),
                        ContextCompat.getColor(getApplicationContext(),R.color.blue_grey));
                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        // put code
                        Log.d("position",""+position);
                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                }).addListenerButton("Cancel", new ColorPicker.OnButtonListener() {
                    @Override
                    public void onClick(View v, int position, int color) {
                        // put code
                    }
                }).disableDefaultButtons(true).setDefaultColorButton(Color.parseColor("#f84c44"))
                        .setColumns(5).setRoundColorButton(true).show();
                break;

            case R.id.act_detailInput_start_date_display:
                DialogFragment startDatePick = new DatePickerTabFragment();
                startDatePick.show(getSupportFragmentManager(),"start_date_pick");
                break;
            case R.id.act_detailInput_end_date_display:

                break;
            case R.id.act_detailInput_start_time_display:
                DialogFragment startTimePick = new TimePickerTabFragment();
                startTimePick.show(getSupportFragmentManager(),"start_time_pick");
                break;
            case R.id.act_detailInput_end_time_display:

                break;

            case R.id.act_detailInput_more_detail_side_btn_gps:
                act_detailInput_more_detail_side_btn_gps.setVisibility(View.GONE);

                break;
            default:
                break;
        }

    }

    //필요하면 수정해서 쓰고 안필요하면 지워도 됨
//    private void showStartDatePickerDialog(){
//        FragmentManager fm = getSupportFragmentManager();
//        DatePickerTabFragment dateFragment = DatePickerTabFragment.newInstance();
//        dateFragment.show(fm,"startdatepick");
//    }
//
//    private void showEndDatePickerDialog(){
//        FragmentManager fm = getSupportFragmentManager();
//        DatePickerTabFragment dateFragment = DatePickerTabFragment.newInstance();
//        dateFragment.show(fm,"enddatepick");
//    }
//
//    private void showStartTimePickerDialog(){
//        FragmentManager fm = getSupportFragmentManager();
//        TimePickerTabFragment timeFragment = TimePickerTabFragment.newInstance();
//        timeFragment.show(fm,"starttimepick");
//    }
//
//    private void showEndTimePickerDialog(){
//        FragmentManager fm = getSupportFragmentManager();
//        TimePickerTabFragment timeFragment = TimePickerTabFragment.newInstance();
//        timeFragment.show(fm,"endtimepick");
//    }

    // 날짜 선택 시 textview로 보내는 함수
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        act_detailInput_start_date_display.setText(year + "년" + (month + 1) + "월" + day + "일");
    }

    // 시간 선택 시 textview로 보내는 함수
    @Override
    public void onTimeSet(TimePicker view, int hourofDay, int minute){
        act_detailInput_start_time_display.setText(String.valueOf(hourofDay)+"시 "+ String.valueOf(minute) + "분");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_submit_btn) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

