package com.team.codealmanac.w2do;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;

import android.util.Log;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.codealmanac.w2do.adapter.FolderSpinnerAdapter;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.dialog.DatePickerDialogActivity;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;

public class DetailInputActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback{
    private final String TAG = "DetailInputActivity";
    private final int GOOGLE_MAP_REQUEST_CODE = 1;
    private final int DATEPICKER_START_DATE_REQUEST_CODE = 2;
    private final int DATEPICKER_END_DATE_REQUEST_CODE = 3;
    private final int DATEPICKER_ALARM_REQUEST_CODE = 4;
    // 첫번째 cardview items : 할일 입력, 컬러 설정
    private EditText act_detailInput_todo_content_edt;
    private Button act_detailInput_todo_content_color_picker;
    private int mPickedColor;

    //두번째 cardview items : 폴더 선택
    private Spinner act_detailInput_folder_spinner;

    // cardview items : 캘린더,시간설정
    private TextView act_detailInput_calendar_start_title_text;
    private TextView act_detailInput_calendar_end_title_text;
    private TextView act_detailInput_calendar_start_date;
    private TextView act_detailInput_calendar_end_date;
    private ToggleButton act_detailInput_calendar_allday_btn;
    private long mStartDate;
    private long mEndDate;

    // cardview items : 지도
    private boolean isLocationButtonOneClick = true;
    private CardView act_detailInput_map_cardview;
    private TextView act_detailInput_map_location_guide_text;
    private TextView act_detailInput_map_location_text;
    private ImageButton act_detailInput_map_remove_btn;
    private MapFragment act_detailInput_googleMap_frag;
    private GoogleMap mGoogleMap;
    private LatLng mLocation;
    private String mLocationName;
    private Marker mMarker;

    // cardview items : 공유
//    private CardView act_detailInput_share_cardview;
//    private TextView act_detailInput_share_guide_text;
//    private RecyclerView act_detailInput_share_invitee_recyclerview;
//    private DetailInputInviteeAdapter mShareInviteeAdapter;

    // cardview items : 알람
    private CardView act_detailInput_alarm_cardview;
    private ImageButton act_detailInput_alarm_selfinput_btn;
    private TextView act_detailInput_alarm_self_text;
    private ImageButton act_detailInput_alarm_remove_btn;
    private RelativeRadioGroup act_detailInput_alarm_radiogroup;
//    private RadioButton act_detailInput_alarm_radiobtn_1;
//    private RadioButton act_detailInput_alarm_radiobtn_2;
//    private RadioButton act_detailInput_alarm_radiobtn_3;
//    private RadioButton act_detailInput_alarm_radiobtn_4;
//    private RadioButton act_detailInput_alarm_radiobtn_5;
//    private RadioButton act_detailInput_alarm_radiobtn_6;
    private long mAlarmDate;

    // cardview items : 메모
    private CardView act_detailInput_memo_cardview;
    private EditText act_detailInput_memo_edt;

    //cardview items: more detail buttons
    private ImageButton act_detailInput_more_detail_side_btn_gps;
//    private ImageButton act_detailInput_more_detail_side_btn_share;
    private ImageButton act_detailInput_more_detail_side_btn_alarm;
    private ImageButton act_detailInput_more_detail_side_btn_memo;

    private FontContract mFont;

    private String USER_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.act_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mFont = new FontContract(getApplication().getAssets());
        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 타이틀 아이템
        TextView act_detailInput_toolbar_title = (TextView)findViewById(R.id.act_detailInput_toolbar_title);
        ImageButton act_detailInput_toolbar_save_btn = (ImageButton)findViewById(R.id.act_detailInput_toolbar_save_btn);
        Button act_detailInput_toolbar_back_btn = (Button)findViewById(R.id.act_detailInput_toolbar_back_btn);
        act_detailInput_toolbar_title.setTypeface(mFont.NahumSquareB_Regular());

        // 투두 내용 카드뷰 아이템
        act_detailInput_todo_content_edt = (EditText)findViewById(R.id.act_detailInput_todo_content_edt);
        act_detailInput_todo_content_color_picker = (Button)findViewById(R.id.act_detailInput_todo_content_color_picker);
        mPickedColor = ContextCompat.getColor(getApplicationContext(), R.color.blue);
        act_detailInput_todo_content_edt.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_todo_content_color_picker.setTypeface(mFont.NahumSquareR_Regular());

        // 폴더 카드뷰 아이템
        act_detailInput_folder_spinner = (Spinner)findViewById(R.id.act_detailInput_folder_spinner);

        // 캘린더 카드뷰 아이템
        mStartDate = mEndDate = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
        act_detailInput_calendar_start_title_text = (TextView)findViewById(R.id.act_detailInput_calendar_start_title_text);
        act_detailInput_calendar_end_title_text = (TextView)findViewById(R.id.act_detailInput_calendar_end_title_text);
        act_detailInput_calendar_start_date = (TextView)findViewById(R.id.act_detailInput_calendar_start_date);
        act_detailInput_calendar_end_date = (TextView)findViewById(R.id.act_detailInput_calendar_end_date);
        act_detailInput_calendar_allday_btn = (ToggleButton)findViewById(R.id.act_detailInput_calendar_allday_btn);
        act_detailInput_calendar_start_title_text.setTypeface(mFont.NahumSquareB_Regular());
        act_detailInput_calendar_end_title_text.setTypeface(mFont.NahumSquareB_Regular());
        act_detailInput_calendar_start_date.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_calendar_end_date.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_calendar_start_date.setText(format.format(mStartDate));
        act_detailInput_calendar_end_date.setText(format.format(mEndDate));

        // 구글맵 카드뷰 아이템
        act_detailInput_map_cardview = (CardView)findViewById(R.id.act_detailInput_map_cardview);
        act_detailInput_map_location_guide_text = (TextView)findViewById(R.id.act_detailInput_map_location_guide_text);
        act_detailInput_map_location_text = (TextView)findViewById(R.id.act_detailInput_map_location_text);
        act_detailInput_map_remove_btn = (ImageButton)findViewById(R.id.act_detailInput_map_remove_btn);
        act_detailInput_map_location_guide_text.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_map_location_text.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_googleMap_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.act_detailInput_googleMap_frag);
        act_detailInput_googleMap_frag.getMapAsync(this);

//        //공유 카드뷰 아이템
//        act_detailInput_share_cardview = (CardView)findViewById(R.id.act_detailInput_share_cardview);
//        act_detailInput_share_guide_text = (TextView)findViewById(R.id.act_detailInput_share_guide_text);
//        act_detailInput_share_invitee_recyclerview = (RecyclerView)findViewById(R.id.act_detailInput_share_invitee_recyclerview);
//        mShareInviteeAdapter = new DetailInputInviteeAdapter(act_detailInput_share_invitee_recyclerview);
//        act_detailInput_share_invitee_recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
//        act_detailInput_share_invitee_recyclerview.setAdapter(mShareInviteeAdapter);
//        act_detailInput_share_guide_text.setTypeface(mFont.NahumSquareR_Regular());

        //알람 카드뷰 아이템
        mAlarmDate = 0;
        act_detailInput_alarm_cardview = (CardView)findViewById(R.id.act_detailInput_alarm_cardview);
        act_detailInput_alarm_selfinput_btn = (ImageButton)findViewById(R.id.act_detailInput_alarm_selfinput_btn);
        act_detailInput_alarm_self_text = (TextView)findViewById(R.id.act_detailInput_alarm_self_text);
        act_detailInput_alarm_remove_btn = (ImageButton)findViewById(R.id.act_detailInput_alarm_remove_btn);
        act_detailInput_alarm_radiogroup = (RelativeRadioGroup)findViewById(R.id.act_detailInput_alarm_radiogroup);
//        act_detailInput_alarm_radiobtn_1 = (RadioButton)findViewById(R.id.act_detailInput_alarm_radiobtn_1);
//        act_detailInput_alarm_radiobtn_2 = (RadioButton)findViewById(R.id.act_detailInput_alarm_radiobtn_2);
//        act_detailInput_alarm_radiobtn_3 = (RadioButton)findViewById(R.id.act_detailInput_alarm_radiobtn_3);
//        act_detailInput_alarm_radiobtn_4 = (RadioButton)findViewById(R.id.act_detailInput_alarm_radiobtn_4);
//        act_detailInput_alarm_radiobtn_5 = (RadioButton)findViewById(R.id.act_detailInput_alarm_radiobtn_5);
//        act_detailInput_alarm_radiobtn_6 = (RadioButton)findViewById(R.id.act_detailInput_alarm_radiobtn_6);
        act_detailInput_alarm_self_text.setTypeface(mFont.NahumSquareR_Regular());

        //메모 카드뷰 아이템
        act_detailInput_memo_cardview = (CardView)findViewById(R.id.act_detailInput_memo_cardview);
        act_detailInput_memo_edt = (EditText)findViewById(R.id.act_detailInput_memo_edt);
        act_detailInput_memo_edt.setTypeface(mFont.NahumSquareR_Regular());

        // more detail 버튼 아이템
        act_detailInput_more_detail_side_btn_gps = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_gps);
//        act_detailInput_more_detail_side_btn_share = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_share);
        act_detailInput_more_detail_side_btn_alarm = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_alarm);
        act_detailInput_more_detail_side_btn_memo = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_memo);

        // 폴더 선택 spinner adapter
        List<String> spinnerItem = new ArrayList<>();
        //TODO SQLite 폴더데이터 필요
        ArrayAdapter<String> FolderSpinnerAdapter = new FolderSpinnerAdapter(
                DetailInputActivity.this, R.layout.adpitem_spinner_text, spinnerItem.toArray(new String[spinnerItem.size()]));
        FolderSpinnerAdapter.setDropDownViewResource(R.layout.adpitem_spinner_dropdown);
        act_detailInput_folder_spinner.setAdapter(FolderSpinnerAdapter);

        //타이틀 리스너 등록
        act_detailInput_toolbar_save_btn.setOnClickListener(this);
        act_detailInput_toolbar_back_btn.setOnClickListener(this);
        //투두 카드뷰 리스너 등록
        act_detailInput_todo_content_color_picker.setOnClickListener(this);
        //캘린더 카드뷰 리스너 등록
        act_detailInput_calendar_start_date.setOnClickListener(this);
        act_detailInput_calendar_end_date.setOnClickListener(this);
        act_detailInput_calendar_allday_btn.setOnClickListener(this);
        //장소 카드뷰 리스너 등록
        act_detailInput_map_location_guide_text.setOnClickListener(this);
        act_detailInput_map_remove_btn.setOnClickListener(this);
        //공유 카드뷰 리스너 등록
//        act_detailInput_share_guide_text.setOnClickListener(this);
        //알람 카드뷰 리스너 등록
        act_detailInput_alarm_selfinput_btn.setOnClickListener(this);
        act_detailInput_alarm_self_text.setOnClickListener(this);
        act_detailInput_alarm_remove_btn.setOnClickListener(this);
        //more detail 카드뷰 리스너 등록
        act_detailInput_more_detail_side_btn_gps.setOnClickListener(this);
//        act_detailInput_more_detail_side_btn_share.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_alarm.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_memo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_MAP_REQUEST_CODE) {
            isLocationButtonOneClick = true;
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                findViewById(R.id.act_detailInput_map_null_location_layout).setVisibility(View.GONE);
                findViewById(R.id.act_detailInput_map_used_location_layout).setVisibility(View.VISIBLE);
                act_detailInput_map_location_text.setText(place.getName());
                mLocation = place.getLatLng();
                mLocationName = String.valueOf(place.getName());
                if(mGoogleMap != null){
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    findViewById(R.id.act_detailInput_googleMap_frag).setVisibility(View.VISIBLE);
                }
            } else if(resultCode == RESULT_CANCELED){
                if(mGoogleMap != null && mLocation != null){
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                }
            }
        } else if( (requestCode == DATEPICKER_START_DATE_REQUEST_CODE || requestCode == DATEPICKER_END_DATE_REQUEST_CODE) && resultCode == RESULT_OK){
            long timeInMillis = data.getLongExtra("date", 0);
            SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
            if(act_detailInput_calendar_allday_btn.isChecked()){
                 format = new SimpleDateFormat("M월 d일(E)");
                String date = format.format(timeInMillis);
                act_detailInput_calendar_start_date.setText(date);
                act_detailInput_calendar_end_date.setText(date);
                mStartDate = timeInMillis;
                mEndDate = timeInMillis;
                return;
            }
            String date = format.format(timeInMillis);
            if(requestCode == DATEPICKER_START_DATE_REQUEST_CODE) {
                act_detailInput_calendar_start_date.setText(date);
                mStartDate = timeInMillis;
            } else {
                act_detailInput_calendar_end_date.setText(date);
                mEndDate = timeInMillis;
            }
        } else if( requestCode == DATEPICKER_ALARM_REQUEST_CODE && resultCode == RESULT_OK){
            long timeInMillis = mAlarmDate = data.getLongExtra("date", 0);
            SimpleDateFormat format = new SimpleDateFormat("M월 d일(E) hh:mm a");
            String date = format.format(timeInMillis);
            act_detailInput_alarm_self_text.setText(date);
            findViewById(R.id.act_detailInput_alarm_self_use_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.act_detailInput_alarm_self_null_layout).setVisibility(View.GONE);
        }
    }

    //투두 입력
    private void setTodo(){
        if(TextUtils.isEmpty(act_detailInput_todo_content_edt.getText())){
            act_detailInput_todo_content_edt.setError("내용을 입력해주세요.");
            return;
        }

        Todo todo = new Todo(mPickedColor,
                act_detailInput_folder_spinner.getSelectedItem().toString(), act_detailInput_todo_content_edt.getText().toString(),
                mStartDate, mEndDate, /*alarm*/mAlarmDate, /*lat*/-1, /*lon*/-1, /*location_name*/null, /*memo*/act_detailInput_memo_edt.getText().toString());
        //시작날의 00시 00분
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(mStartDate);
        now.set(Calendar.HOUR, 0); now.set(Calendar.MINUTE, 0); now.set(Calendar.SECOND, 0);
        long nowTimeInMillis = now.getTimeInMillis();
        if(mLocation != null){
            todo.latitude = mLocation.latitude;
            todo.longitude = mLocation.longitude;
            todo.location_name = mLocationName;
        }
        if(act_detailInput_alarm_radiogroup.getCheckedRadioButtonId() != -1){
            switch (act_detailInput_alarm_radiogroup.getCheckedRadioButtonId()){
                case R.id.act_detailInput_alarm_radiobtn_1:
                    mAlarmDate = nowTimeInMillis; break;
                case R.id.act_detailInput_alarm_radiobtn_2:
                    mAlarmDate = nowTimeInMillis - 1000*60*10; break;
                case R.id.act_detailInput_alarm_radiobtn_3:
                    mAlarmDate = nowTimeInMillis - 1000*60*30; break;
                case R.id.act_detailInput_alarm_radiobtn_4:
                    mAlarmDate = nowTimeInMillis - 1000*60*60; break;
                case R.id.act_detailInput_alarm_radiobtn_5:
                    mAlarmDate = nowTimeInMillis - 1000*60*60*2; break;
                case R.id.act_detailInput_alarm_radiobtn_6: //하루전 12시 정각
                    mAlarmDate = nowTimeInMillis - 1000*60*60*12; break;
            }
            todo.alarm_date = mAlarmDate;
        }
    }
    //컬러피커
    private void setColorPicker(){
        final ColorPicker colorPicker = new ColorPicker(DetailInputActivity.this);
        // 다이얼로그 레이아웃 배경색 지정
        colorPicker.getDialogBaseLayout().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        // 팔레트 색상 지정
        colorPicker.setColors(
                ContextCompat.getColor(getApplicationContext(), R.color.red),
                ContextCompat.getColor(getApplicationContext(), R.color.pink),
                ContextCompat.getColor(getApplicationContext(), R.color.purple),
                ContextCompat.getColor(getApplicationContext(), R.color.deep_purple),
                ContextCompat.getColor(getApplicationContext(), R.color.indigo),
                ContextCompat.getColor(getApplicationContext(), R.color.blue),
                ContextCompat.getColor(getApplicationContext(), R.color.light_blue),
                ContextCompat.getColor(getApplicationContext(), R.color.cyan),
                ContextCompat.getColor(getApplicationContext(), R.color.teal),
                ContextCompat.getColor(getApplicationContext(), R.color.green),
                ContextCompat.getColor(getApplicationContext(), R.color.light_green),
                ContextCompat.getColor(getApplicationContext(), R.color.lime),
                ContextCompat.getColor(getApplicationContext(), R.color.yellow),
                ContextCompat.getColor(getApplicationContext(), R.color.amber),
                ContextCompat.getColor(getApplicationContext(), R.color.orange),
                ContextCompat.getColor(getApplicationContext(), R.color.deep_orange),
                ContextCompat.getColor(getApplicationContext(), R.color.brown),
                ContextCompat.getColor(getApplicationContext(), R.color.gray),
                ContextCompat.getColor(getApplicationContext(), R.color.blue_grey));
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                GradientDrawable pickerButtonBgShape = (GradientDrawable)act_detailInput_todo_content_color_picker.getBackground();
                pickerButtonBgShape.setColor(color);
                mPickedColor = color;
            }
            @Override
            public void onCancel() {}
        }).setColumns(5).setDefaultColorButton(ContextCompat.getColor(getApplicationContext(), R.color.blue)).setRoundColorButton(true).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //투두 입력 카드뷰
            case R.id.act_detailInput_todo_content_color_picker:        //컬러피커 선택
                setColorPicker();
                break;

            //캘린더 카드뷰
            case R.id.act_detailInput_calendar_start_date:  //시작날짜
                Intent startDate = new Intent(DetailInputActivity.this, DatePickerDialogActivity.class);
//                startDate.putExtra("type", "start");
                startActivityForResult(startDate, DATEPICKER_START_DATE_REQUEST_CODE);
                break;
            case R.id.act_detailInput_calendar_end_date:    //종료날짜
                Intent endDate = new Intent(DetailInputActivity.this, DatePickerDialogActivity.class);
//                endDate.putExtra("type", "end");
                startActivityForResult(endDate, DATEPICKER_END_DATE_REQUEST_CODE);
                break;
            case R.id.act_detailInput_calendar_allday_btn:  //하루종일 버튼
                if(act_detailInput_calendar_allday_btn.isChecked()){
                    SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)");
//                    act_detailInput_calendar_start_date.setOnClickListener(null);
                    act_detailInput_calendar_end_date.setOnClickListener(null);
                    act_detailInput_calendar_start_date.setText(format.format(mStartDate));
                    act_detailInput_calendar_end_date.setText(format.format(mStartDate));
                } else {
                    SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
//                    act_detailInput_calendar_start_date.setOnClickListener(this);
                    act_detailInput_calendar_end_date.setOnClickListener(this);
                    act_detailInput_calendar_start_date.setText(format.format(mStartDate));
                    act_detailInput_calendar_end_date.setText(format.format(mEndDate));
                }
                break;

            //장소 선택 카드뷰
            case R.id.act_detailInput_map_location_guide_text:
                if(isLocationButtonOneClick){
                    isLocationButtonOneClick = false;
                    if(mMarker != null) mMarker.remove();
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(DetailInputActivity.this), GOOGLE_MAP_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //장소 제거
            case R.id.act_detailInput_map_remove_btn:
                findViewById(R.id.act_detailInput_map_null_location_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.act_detailInput_map_used_location_layout).setVisibility(View.GONE);
                break;
//            //공유 카드뷰
//            case R.id.act_detailInput_share_guide_text:
//                ShareInputDialogFragment.newInstance(new String[]{"test", "test2"}).show(getFragmentManager(), "share_input");
//                break;
            //알람 카드뷰
            case R.id.act_detailInput_alarm_selfinput_btn:
            case R.id.act_detailInput_alarm_self_text:
                act_detailInput_alarm_radiogroup.clearCheck();
                Intent alarmDate = new Intent(DetailInputActivity.this, DatePickerDialogActivity.class);
                startActivityForResult(alarmDate, DATEPICKER_ALARM_REQUEST_CODE);
                break;
            case R.id.act_detailInput_alarm_remove_btn:
                mAlarmDate = 0;
                findViewById(R.id.act_detailInput_alarm_self_use_layout).setVisibility(View.GONE);
                findViewById(R.id.act_detailInput_alarm_self_null_layout).setVisibility(View.VISIBLE);
                break;
            //more detail 카드뷰
            case R.id.act_detailInput_more_detail_side_btn_gps:         //장소 선택 확장
                act_detailInput_map_cardview.setVisibility(View.VISIBLE);
                act_detailInput_more_detail_side_btn_gps.setVisibility(View.GONE);
                break;
//            case R.id.act_detailInput_more_detail_side_btn_share:       //공유 확장
//                act_detailInput_share_cardview.setVisibility(View.VISIBLE);
//                act_detailInput_more_detail_side_btn_share.setVisibility(View.GONE);
//                break;
            case R.id.act_detailInput_more_detail_side_btn_alarm:       //알람 확장
                act_detailInput_alarm_cardview.setVisibility(View.VISIBLE);
                act_detailInput_more_detail_side_btn_alarm.setVisibility(View.GONE);
                break;
            case R.id.act_detailInput_more_detail_side_btn_memo:        //메모 확장
                act_detailInput_memo_cardview.setVisibility(View.VISIBLE);
                act_detailInput_more_detail_side_btn_memo.setVisibility(View.GONE);
                break;
            case R.id.act_detailInput_toolbar_back_btn:
                onBackPressed();
                break;
            case R.id.act_detailInput_toolbar_save_btn:     //세이브버튼
                setTodo();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if(mLocation == null) return;
        mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }

//    @Override
//    public void onInviteeClick(String email) {
//        act_detailInput_share_invitee_recyclerview.setVisibility(View.VISIBLE);
//        if(mShareInviteeAdapter.getList().indexOf(email) != -1) return;
//        mShareInviteeAdapter.addItem(email);
//    }
}

