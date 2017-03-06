package com.team.codealmanac.w2do;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.content.ContextCompat;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.codealmanac.w2do.adapter.FolderSpinnerAdapter;
import com.team.codealmanac.w2do.dialog.DatePickerDialogActivity;

import petrov.kristiyan.colorpicker.ColorPicker;

public class DetailInputActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback{
    private final String TAG = "DetailInputActivity";
    private final int GOOGLE_MAP_REQUEST_CODE = 1;
    private final int DATEPICKER_START_DATE_REQUEST_CODE = 2;
    private final int DATEPICKER_END_DATE_REQUEST_CODE = 3;
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
    private ImageButton act_detailInput_calendar_allday_btn;

    // cardview items : 지도
    private boolean isLocationButtonOneClick = true;
    private CardView act_detailInput_map_cardview;
    private EditText act_detailInput_map_location_edt;
    private MapFragment act_detailInput_googleMap_frag;
    private GoogleMap mGoogleMap;
    private LatLng mLocation;
    private Marker mMarker;

    // cardview items : 공유
    private CardView act_detailInput_share_cardview;

    // cardview items : 알람
    private CardView act_detailInput_alarm_cardview;

    // cardview items : 메모
    private CardView act_detailInput_memo_cardview;

    //cardview items: more detail buttons
    private ImageButton act_detailInput_more_detail_side_btn_gps;
    private ImageButton act_detailInput_more_detail_side_btn_share;
    private ImageButton act_detailInput_more_detail_side_btn_alarm;
    private ImageButton act_detailInput_more_detail_side_btn_memo;

    private FontContract mFontContract;

    private DatabaseReference mFolderReference;
    private String USER_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.act_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mFontContract = new FontContract(getApplication().getAssets());
        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFolderReference = FirebaseDatabase.getInstance().getReference().child("todo_folder").child(USER_ID).child("folder");

        // 투두 내용 카드뷰 아이템
        act_detailInput_todo_content_edt = (EditText)findViewById(R.id.act_detailInput_todo_content_edt);
        act_detailInput_todo_content_color_picker = (Button)findViewById(R.id.act_detailInput_todo_content_color_picker);
        mPickedColor = ContextCompat.getColor(getApplicationContext(), R.color.blue);
        act_detailInput_todo_content_edt.setTypeface(mFontContract.NahumSquareR_Regular());
        act_detailInput_todo_content_color_picker.setTypeface(mFontContract.NahumSquareR_Regular());

        // 폴더 카드뷰 아이템
        act_detailInput_folder_spinner = (Spinner)findViewById(R.id.act_detailInput_folder_spinner);

        // 캘린더 카드뷰 아이템
        act_detailInput_calendar_start_title_text = (TextView)findViewById(R.id.act_detailInput_calendar_start_title_text);
        act_detailInput_calendar_end_title_text = (TextView)findViewById(R.id.act_detailInput_calendar_end_title_text);
        act_detailInput_calendar_start_date = (TextView)findViewById(R.id.act_detailInput_calendar_start_date);
        act_detailInput_calendar_end_date = (TextView)findViewById(R.id.act_detailInput_calendar_end_date);
        act_detailInput_calendar_allday_btn = (ImageButton)findViewById(R.id.act_detailInput_calendar_allday_btn);
        act_detailInput_calendar_start_title_text.setTypeface(mFontContract.NahumSquareB_Regular());
        act_detailInput_calendar_end_title_text.setTypeface(mFontContract.NahumSquareB_Regular());
        act_detailInput_calendar_start_date.setTypeface(mFontContract.NahumSquareR_Regular());
        act_detailInput_calendar_end_date.setTypeface(mFontContract.NahumSquareR_Regular());

        // 구글맵 카드뷰 아이템
        act_detailInput_map_cardview = (CardView)findViewById(R.id.act_detailInput_map_cardview);
        act_detailInput_map_location_edt = (EditText)findViewById(R.id.act_detailInput_map_location_edt);
        act_detailInput_googleMap_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.act_detailInput_googleMap_frag);
        act_detailInput_googleMap_frag.getMapAsync(this);

        findViewById(R.id.act_detailInput_googleMap_frag).setVisibility(View.GONE); //프래그먼트 처음에 안보이게.

        //공유 카드뷰 아이템
        act_detailInput_share_cardview = (CardView)findViewById(R.id.act_detailInput_share_cardview);
        //알람 카드뷰 아이템
        act_detailInput_alarm_cardview = (CardView)findViewById(R.id.act_detailInput_alarm_cardview);
        //메모 카드뷰 아이템
        act_detailInput_memo_cardview = (CardView)findViewById(R.id.act_detailInput_memo_cardview);

        // more detail 버튼 아이템
        act_detailInput_more_detail_side_btn_gps = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_gps);
        act_detailInput_more_detail_side_btn_share = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_share);
        act_detailInput_more_detail_side_btn_alarm = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_alarm);
        act_detailInput_more_detail_side_btn_memo = (ImageButton)findViewById(R.id.act_detailInput_more_detail_side_btn_memo);

        // 폴더 선택 spinner adapter

        String[] Folder_Spinner_item = getResources().getStringArray(R.array.folder);
        ArrayAdapter<String> Folder_Spinner_Adapter = new FolderSpinnerAdapter(
                this, R.layout.adpitem_spinner_text, Folder_Spinner_item);
        Folder_Spinner_Adapter.setDropDownViewResource(R.layout.adpitem_spinner_dropdown);
        act_detailInput_folder_spinner.setAdapter(Folder_Spinner_Adapter);

        //투두 카드뷰 리스너 등록
        act_detailInput_todo_content_color_picker.setOnClickListener(this);
        //캘린더 카드뷰 리스너 등록
        act_detailInput_calendar_start_date.setOnClickListener(this);
        act_detailInput_calendar_end_date.setOnClickListener(this);
        act_detailInput_calendar_allday_btn.setOnClickListener(this);
        //장소 카드뷰 리스너 등록
        act_detailInput_map_location_edt.setOnClickListener(this);
        //more detail 카드뷰 리스너 등록
        act_detailInput_more_detail_side_btn_gps.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_share.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_alarm.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_memo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_MAP_REQUEST_CODE) {
            isLocationButtonOneClick = true;
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                act_detailInput_map_location_edt.setText(place.getName());
                mLocation = place.getLatLng();
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
            String date = "";
            int month, day, dayofweek, hour, minute;
            month = data.getIntExtra("Month", 0);
            day = data.getIntExtra("Day", 0);
//            dayofweek = data.getIntExtra("요일", 0);
            hour = data.getIntExtra("Hour", 0);
            minute = data.getIntExtra("Minute", 0);
            date += month + "월 "
                    + day + "일";
//                    + "(" + dayofweek + ")";
            date += "\n" + hour + ":"
                    + minute;
            if(requestCode == DATEPICKER_START_DATE_REQUEST_CODE) act_detailInput_calendar_start_date.setText(date);
            else act_detailInput_calendar_end_date.setText(date);
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
                Log.d(TAG, "colorPicker position" + position + ", color" + color + ", red:" + R.color.red);
                GradientDrawable pickerButtonBgShape = (GradientDrawable)act_detailInput_todo_content_color_picker.getBackground();
                pickerButtonBgShape.setColor(color);
            }
            @Override
            public void onCancel() {
                Log.d(TAG, "colorPicker cancel");
            }
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
                startDate.putExtra("type", "start");
                startActivityForResult(startDate, DATEPICKER_START_DATE_REQUEST_CODE);
                break;
            case R.id.act_detailInput_calendar_end_date:    //종료날짜
                Intent endDate = new Intent(DetailInputActivity.this, DatePickerDialogActivity.class);
                endDate.putExtra("type", "end");
                startActivityForResult(endDate, DATEPICKER_END_DATE_REQUEST_CODE);
                break;
            case R.id.act_detailInput_calendar_allday_btn:  //하루종일 버튼
                break;
            //장소 선택 카드뷰
            case R.id.act_detailInput_map_location_edt:
                if(isLocationButtonOneClick){
                    isLocationButtonOneClick = false;
                    if(mMarker != null) mMarker.remove();
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(DetailInputActivity.this), GOOGLE_MAP_REQUEST_CODE);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //more detail 카드뷰
            case R.id.act_detailInput_more_detail_side_btn_gps:         //장소 선택 확장
                act_detailInput_map_cardview.setVisibility(View.VISIBLE);
                act_detailInput_more_detail_side_btn_gps.setVisibility(View.GONE);
                break;
            case R.id.act_detailInput_more_detail_side_btn_share:       //공유 확장
                act_detailInput_share_cardview.setVisibility(View.VISIBLE);
                act_detailInput_more_detail_side_btn_share.setVisibility(View.GONE);
                break;
            case R.id.act_detailInput_more_detail_side_btn_alarm:       //알람 확장
                act_detailInput_alarm_cardview.setVisibility(View.VISIBLE);
                act_detailInput_more_detail_side_btn_alarm.setVisibility(View.GONE);
                break;
            case R.id.act_detailInput_more_detail_side_btn_memo:        //메모 확장
                act_detailInput_memo_cardview.setVisibility(View.VISIBLE);
                act_detailInput_more_detail_side_btn_memo.setVisibility(View.GONE);
                break;
        }
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if(mLocation == null) return;
        mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}

