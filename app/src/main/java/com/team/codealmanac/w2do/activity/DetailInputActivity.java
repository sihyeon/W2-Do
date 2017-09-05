package com.team.codealmanac.w2do.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;

import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.adapter.FolderSpinnerAdapter;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.customView.RelativeRadioGroup;
import com.team.codealmanac.w2do.dialog.DatePickerDialogActivity;
import com.team.codealmanac.w2do.realmObjects.TodoFolderObject;
import com.team.codealmanac.w2do.realmObjects.TodoObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import io.realm.Realm;
import io.realm.RealmResults;
import petrov.kristiyan.colorpicker.ColorPicker;

/**
 * 투두를 상세입력하는 액티비티
 * 투두 상세 수정과 공유함. 구분은 intent를 통해 들어온 데이터로.
 */

public class DetailInputActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {
    private final String TAG = "DetailInputActivity";
    public static final String INPUT_TYPE = "input_type";
    public static final String TYPE_ADD = "add";
    public static final String TYPE_UPDATE = "update";
    public static final String UPDATE_TODO_ID = "todo_id";

    private final int GOOGLE_MAP_REQUEST_CODE = 1;
    private final int DATEPICKER_START_DATE_REQUEST_CODE = 2;
    private final int DATEPICKER_END_DATE_REQUEST_CODE = 3;
    private final int DATEPICKER_ALARM_REQUEST_CODE = 4;

    // 첫번째 cardview items : 할일 입력, 컬러 설정
    private EditText act_detailInput_todo_content_edt;
    private Button act_detailInput_todo_content_color_picker;
    private int pickedColor;

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

    // cardview items : 알람
    private CardView act_detailInput_alarm_cardview;
    private ImageButton act_detailInput_alarm_selfinput_btn;
    private TextView act_detailInput_alarm_self_text;
    private ImageButton act_detailInput_alarm_remove_btn;
    private RelativeRadioGroup act_detailInput_alarm_radiogroup;
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

    private int updateTodoId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.frag_detailtodo_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mFont = new FontContract(getApplication().getAssets());
        // 타이틀 아이템
        TextView act_detailInput_toolbar_title = (TextView) findViewById(R.id.frag_detailinput_toolbar_title);
        ImageButton act_detailInput_toolbar_save_btn = (ImageButton) findViewById(R.id.frag_detailinput_toolbar_save_btn);
        Button act_detailInput_toolbar_back_btn = (Button) findViewById(R.id.frag_detailinput_toolbar_back_btn);
        act_detailInput_toolbar_title.setTypeface(mFont.NahumSquareB_Regular());

        // 투두 내용 카드뷰 아이템
        act_detailInput_todo_content_edt = (EditText) findViewById(R.id.act_detailInput_todo_content_edt);
        act_detailInput_todo_content_color_picker = (Button) findViewById(R.id.act_detailInput_todo_content_color_picker);
        pickedColor = ContextCompat.getColor(getApplicationContext(), R.color.blue);
        act_detailInput_todo_content_edt.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_todo_content_color_picker.setTypeface(mFont.NahumSquareR_Regular());

        // 폴더 카드뷰 아이템
        act_detailInput_folder_spinner = (Spinner) findViewById(R.id.act_detailInput_folder_spinner);

        // 캘린더 카드뷰 아이템
        mStartDate = mEndDate = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
        act_detailInput_calendar_start_title_text = (TextView) findViewById(R.id.act_detailInput_calendar_start_title_text);
        act_detailInput_calendar_end_title_text = (TextView) findViewById(R.id.act_detailInput_calendar_end_title_text);
        act_detailInput_calendar_start_date = (TextView) findViewById(R.id.act_detailInput_calendar_start_date);
        act_detailInput_calendar_end_date = (TextView) findViewById(R.id.act_detailInput_calendar_end_date);
        act_detailInput_calendar_allday_btn = (ToggleButton) findViewById(R.id.act_detailInput_calendar_allday_btn);
        act_detailInput_calendar_start_title_text.setTypeface(mFont.NahumSquareB_Regular());
        act_detailInput_calendar_end_title_text.setTypeface(mFont.NahumSquareB_Regular());
        act_detailInput_calendar_start_date.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_calendar_end_date.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_calendar_start_date.setText(format.format(mStartDate));
        act_detailInput_calendar_end_date.setText(format.format(mEndDate));

        // 구글맵 카드뷰 아이템
        act_detailInput_map_cardview = (CardView) findViewById(R.id.act_detailInput_map_cardview);
        act_detailInput_map_location_guide_text = (TextView) findViewById(R.id.act_detailInput_map_location_guide_text);
        act_detailInput_map_location_text = (TextView) findViewById(R.id.act_detailInput_map_location_text);
        act_detailInput_map_remove_btn = (ImageButton) findViewById(R.id.act_detailInput_map_remove_btn);
        act_detailInput_map_location_guide_text.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_map_location_text.setTypeface(mFont.NahumSquareR_Regular());
        act_detailInput_googleMap_frag = (MapFragment) getFragmentManager().findFragmentById(R.id.act_detailInput_googleMap_frag);
        act_detailInput_googleMap_frag.getMapAsync(this);

        //알람 카드뷰 아이템
        mAlarmDate = 0;
        act_detailInput_alarm_cardview = (CardView) findViewById(R.id.act_detailInput_alarm_cardview);
        act_detailInput_alarm_selfinput_btn = (ImageButton) findViewById(R.id.act_detailInput_alarm_selfinput_btn);
        act_detailInput_alarm_self_text = (TextView) findViewById(R.id.act_detailInput_alarm_self_text);
        act_detailInput_alarm_remove_btn = (ImageButton) findViewById(R.id.act_detailInput_alarm_remove_btn);
        act_detailInput_alarm_radiogroup = (RelativeRadioGroup) findViewById(R.id.act_detailInput_alarm_radiogroup);
        act_detailInput_alarm_self_text.setTypeface(mFont.NahumSquareR_Regular());

        //메모 카드뷰 아이템
        act_detailInput_memo_cardview = (CardView) findViewById(R.id.act_detailInput_memo_cardview);
        act_detailInput_memo_edt = (EditText) findViewById(R.id.act_detailInput_memo_edt);
        act_detailInput_memo_edt.setTypeface(mFont.NahumSquareR_Regular());

        // more detail 버튼 아이템
        act_detailInput_more_detail_side_btn_gps = (ImageButton) findViewById(R.id.act_detailInput_more_detail_side_btn_gps);
        act_detailInput_more_detail_side_btn_alarm = (ImageButton) findViewById(R.id.act_detailInput_more_detail_side_btn_alarm);
        act_detailInput_more_detail_side_btn_memo = (ImageButton) findViewById(R.id.act_detailInput_more_detail_side_btn_memo);

        // 폴더 선택 spinner adapter
        ArrayList<String> spinnerItem = getFolderList();
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
        //알람 카드뷰 리스너 등록
        act_detailInput_alarm_selfinput_btn.setOnClickListener(this);
        act_detailInput_alarm_self_text.setOnClickListener(this);
        act_detailInput_alarm_remove_btn.setOnClickListener(this);
        //more detail 카드뷰 리스너 등록
        act_detailInput_more_detail_side_btn_gps.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_alarm.setOnClickListener(this);
        act_detailInput_more_detail_side_btn_memo.setOnClickListener(this);


        if (isUpdate()) setUpdateTodo();
    }

    private void setUpdateTodo(){
        ImageButton toolbarDeleteBtn = (ImageButton) findViewById(R.id.frag_detailinput_toolbar_delete_btn);
        toolbarDeleteBtn.setVisibility(View.VISIBLE);
        toolbarDeleteBtn.setOnClickListener(this);

        updateTodoId = getIntent().getIntExtra(UPDATE_TODO_ID, 0);
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoObject todoObject = getRealm().where(TodoObject.class).equalTo("ID", updateTodoId).findFirst();
                TodoFolderObject folderObject = getRealm().where(TodoFolderObject.class).equalTo("todoList.ID", updateTodoId).findFirst();

                //내용
                act_detailInput_todo_content_edt.setText(todoObject.getContent());
                //색
                pickedColor = todoObject.getColor();
                GradientDrawable pickerButtonBgShape = (GradientDrawable) act_detailInput_todo_content_color_picker.getBackground();
                pickerButtonBgShape.setColor(pickedColor);
                //폴더
                act_detailInput_folder_spinner.setSelection(((FolderSpinnerAdapter)act_detailInput_folder_spinner.getAdapter())
                        .getPosition(folderObject.getName()));
                //날짜
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(todoObject.getStartDate());
                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                long dateTimeInMillis = calendar.getTimeInMillis();
                SimpleDateFormat format = (dateTimeInMillis == todoObject.getStartDate() && dateTimeInMillis == todoObject.getEndDate())?
                        new SimpleDateFormat("M월 d일(E)") : new SimpleDateFormat("M월 d일(E)\nhh:mm a");
                act_detailInput_calendar_start_date.setText(format.format(todoObject.getStartDate()));
                act_detailInput_calendar_end_date.setText(format.format(todoObject.getEndDate()));
                //장소
                if (todoObject.getLatitude() != 500) {
                    isSeeMapCard(true);
                    isValidMap(true);
                    mLocationName = todoObject.getLocationName();
                    act_detailInput_map_location_text.setText(mLocationName);
                    mLocation = new LatLng(todoObject.getLatitude(), todoObject.getLongitude());
                    if (mGoogleMap != null) {
                        mMarker = mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        findViewById(R.id.act_detailInput_googleMap_frag).setVisibility(View.VISIBLE);
                    }
                }
                //알람
                if (todoObject.getAlarmDate() != 0) {
                    isSeeAlarmCard(true);
                    if (todoObject.getAlarmDate() == dateTimeInMillis) {
                        act_detailInput_alarm_radiogroup.check(R.id.act_detailInput_alarm_radiobtn_1);
                    } else if (todoObject.getAlarmDate() == dateTimeInMillis - 1000 * 60 * 10) {
                        act_detailInput_alarm_radiogroup.check(R.id.act_detailInput_alarm_radiobtn_2);
                    } else if (todoObject.getAlarmDate() == dateTimeInMillis - 1000 * 60 * 30) {
                        act_detailInput_alarm_radiogroup.check(R.id.act_detailInput_alarm_radiobtn_3);
                    } else if (todoObject.getAlarmDate() == dateTimeInMillis - 1000 * 60 * 60) {
                        act_detailInput_alarm_radiogroup.check(R.id.act_detailInput_alarm_radiobtn_4);
                    } else if (todoObject.getAlarmDate() == dateTimeInMillis - 1000 * 60 * 60 * 2) {
                        act_detailInput_alarm_radiogroup.check(R.id.act_detailInput_alarm_radiobtn_5);
                    } else if (todoObject.getAlarmDate() == dateTimeInMillis - 1000 * 60 * 60 * 12) {
                        act_detailInput_alarm_radiogroup.check(R.id.act_detailInput_alarm_radiobtn_6);
                    } else {
                        act_detailInput_alarm_self_text.setText(format.format(todoObject.getAlarmDate()));
                        isValidCustomAlarm(true);
                    }
                    mAlarmDate = todoObject.getAlarmDate();
                }

                if (todoObject.getMemo() != null) {
                    act_detailInput_memo_edt.setText(todoObject.getMemo());
                    isSeeMemoCard(true);
                }
            }
        });
    }

    private boolean isUpdate(){
        return getIntent().getStringExtra(INPUT_TYPE).equals(TYPE_UPDATE);
    }

    private ArrayList<String> getFolderList() {
        getRealm().beginTransaction();
        RealmResults<TodoFolderObject> results = getRealm().where(TodoFolderObject.class).findAll();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            list.add(results.get(i).getName());
        }
        getRealm().commitTransaction();
        return list;
    }

    private long getAlarmDate() {
        if (act_detailInput_alarm_radiogroup.getCheckedRadioButtonId() != -1) return 0;

        //시작날의 00시 00분
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(mStartDate);
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayTimeInMillis = today.getTimeInMillis();

        switch (act_detailInput_alarm_radiogroup.getCheckedRadioButtonId()) {
            case R.id.act_detailInput_alarm_radiobtn_1:
                mAlarmDate = todayTimeInMillis;
                break;
            case R.id.act_detailInput_alarm_radiobtn_2:
                mAlarmDate = todayTimeInMillis - 1000 * 60 * 10;
                break;
            case R.id.act_detailInput_alarm_radiobtn_3:
                mAlarmDate = todayTimeInMillis - 1000 * 60 * 30;
                break;
            case R.id.act_detailInput_alarm_radiobtn_4:
                mAlarmDate = todayTimeInMillis - 1000 * 60 * 60;
                break;
            case R.id.act_detailInput_alarm_radiobtn_5:
                mAlarmDate = todayTimeInMillis - 1000 * 60 * 60 * 2;
                break;
            case R.id.act_detailInput_alarm_radiobtn_6: //하루전 12시 정각
                mAlarmDate = todayTimeInMillis - 1000 * 60 * 60 * 12;
                break;
        }
        return mAlarmDate;
    }

    private TodoObject createTodoObj() {
        TodoObject todo = new TodoObject(pickedColor,
                act_detailInput_todo_content_edt.getText().toString(),
                mStartDate, mEndDate, /*memo*/act_detailInput_memo_edt.getText().toString());

        if (mLocation != null) {
            todo.setLatitude(mLocation.latitude);
            todo.setLongitude(mLocation.longitude);
            todo.setLocationName(mLocationName);
        }
        todo.setAlarmDate(getAlarmDate());

        return todo;
    }

    private void submitTodo() {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoObject todoObject = realm.copyToRealm(createTodoObj());

                TodoFolderObject todoFolderObject = realm.where(TodoFolderObject.class).equalTo("name", act_detailInput_folder_spinner.getSelectedItem().toString()).findFirst();
                todoFolderObject.getTodoList().add(todoObject);

                finish();
            }
        });
    }

    private void isSeeMapCard(boolean b){
        findViewById(R.id.act_detailInput_map_cardview).setVisibility(b? View.VISIBLE : View.GONE);
        act_detailInput_more_detail_side_btn_gps.setVisibility(b? View.GONE : View.VISIBLE);
    }

    private void isValidMap(boolean b){
        findViewById(R.id.act_detailInput_map_null_location_layout).setVisibility(b? View.GONE : View.VISIBLE);
        findViewById(R.id.act_detailInput_map_used_location_layout).setVisibility(b? View.VISIBLE : View.GONE);
    }

    private void setLocation(LatLng location, String locationName){
        mLocation = location;
        mLocationName = locationName;
        act_detailInput_map_location_text.setText(locationName);
        if (mGoogleMap != null) {
            mMarker = mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            findViewById(R.id.act_detailInput_googleMap_frag).setVisibility(View.VISIBLE);
        }
    }

    private void isSeeAlarmCard(boolean b){
        act_detailInput_alarm_cardview.setVisibility(b? View.VISIBLE : View.GONE);
        act_detailInput_more_detail_side_btn_alarm.setVisibility(b? View.GONE : View.VISIBLE);
    }

    private void isValidCustomAlarm(boolean b){
        findViewById(R.id.act_detailInput_alarm_self_use_layout).setVisibility(b? View.VISIBLE : View.GONE);
        findViewById(R.id.act_detailInput_alarm_self_null_layout).setVisibility(b? View.GONE : View.VISIBLE);
    }

    private void isSeeMemoCard(boolean b){
        act_detailInput_memo_cardview.setVisibility(b? View.VISIBLE : View.GONE);
        act_detailInput_more_detail_side_btn_memo.setVisibility(b? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GOOGLE_MAP_REQUEST_CODE) {
            isLocationButtonOneClick = true;
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                isValidMap(true);
                setLocation(place.getLatLng(), place.getName().toString());
            }
        } else if ((requestCode == DATEPICKER_START_DATE_REQUEST_CODE || requestCode == DATEPICKER_END_DATE_REQUEST_CODE) && resultCode == RESULT_OK) {
            long timeInMillis = data.getLongExtra("date", 0);
            SimpleDateFormat format;
            if (act_detailInput_calendar_allday_btn.isChecked()) {
                format = new SimpleDateFormat("M월 d일(E)");
                String date = format.format(timeInMillis);
                act_detailInput_calendar_start_date.setText(date);
                act_detailInput_calendar_end_date.setText(date);
                mStartDate = timeInMillis;
                mEndDate = timeInMillis;
                return;
            }
            format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
            String date = format.format(timeInMillis);
            if (requestCode == DATEPICKER_START_DATE_REQUEST_CODE) {
                if (mEndDate < timeInMillis) {
                    Toast.makeText(getApplicationContext(), "종료날짜보다 작아야합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                act_detailInput_calendar_start_date.setText(date);
                mStartDate = timeInMillis;
            } else {
                if (mStartDate > timeInMillis) {
                    Toast.makeText(getApplicationContext(), "시작날짜보다 커야합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                act_detailInput_calendar_end_date.setText(date);
                mEndDate = timeInMillis;
            }
        } else if (requestCode == DATEPICKER_ALARM_REQUEST_CODE && resultCode == RESULT_OK) {
            long timeInMillis = mAlarmDate = data.getLongExtra("date", 0);
            SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
            String date = format.format(timeInMillis);
            act_detailInput_alarm_self_text.setText(date);
            isValidCustomAlarm(true);
        }
    }

    //컬러피커
    private void setColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(DetailInputActivity.this);
        // 다이얼로그 레이아웃 배경색 지정 left top bottom right 순 패딩임
        colorPicker.setTitle("").getDialogBaseLayout().setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        colorPicker.setColorButtonMargin(10, 10, 10, 10).setTitlePadding(0, -15, 0, 0);
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
                ContextCompat.getColor(getApplicationContext(), R.color.blue_grey),
                ContextCompat.getColor(getApplicationContext(), R.color.color1),
                ContextCompat.getColor(getApplicationContext(), R.color.color2),
                ContextCompat.getColor(getApplicationContext(), R.color.color3),
                ContextCompat.getColor(getApplicationContext(), R.color.color4),
                ContextCompat.getColor(getApplicationContext(), R.color.color5),
                ContextCompat.getColor(getApplicationContext(), R.color.color6));
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                GradientDrawable pickerButtonBgShape = (GradientDrawable) act_detailInput_todo_content_color_picker.getBackground();
                pickerButtonBgShape.setColor(color);
                pickedColor = color;
            }

            @Override
            public void onCancel() {
            }
        }).setColumns(5).setDefaultColorButton(pickedColor).setRoundColorButton(true).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                SimpleDateFormat format;
                if (act_detailInput_calendar_allday_btn.isChecked()) {
                    format = new SimpleDateFormat("M월 d일(E)");
                    act_detailInput_calendar_end_date.setOnClickListener(null);
                    act_detailInput_calendar_start_date.setText(format.format(mStartDate));
                    act_detailInput_calendar_end_date.setText(format.format(mStartDate));
                } else {
                    format= new SimpleDateFormat("M월 d일(E)\nhh:mm a");
                    act_detailInput_calendar_end_date.setOnClickListener(this);
                    act_detailInput_calendar_start_date.setText(format.format(mStartDate));
                    act_detailInput_calendar_end_date.setText(format.format(mEndDate));
                }
                break;

            //장소 선택 카드뷰
            case R.id.act_detailInput_map_location_guide_text:
                if (isLocationButtonOneClick) {
                    isLocationButtonOneClick = false;
                    if (mMarker != null) mMarker.remove();
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
                mLocation = null;
                mLocationName = null;
                isValidMap(false);
                break;
            //알람 카드뷰
            case R.id.act_detailInput_alarm_selfinput_btn:
            case R.id.act_detailInput_alarm_self_text:
                act_detailInput_alarm_radiogroup.clearCheck();
                Intent alarmDate = new Intent(DetailInputActivity.this, DatePickerDialogActivity.class);
                startActivityForResult(alarmDate, DATEPICKER_ALARM_REQUEST_CODE);
                break;
            case R.id.act_detailInput_alarm_remove_btn:
                mAlarmDate = 0;
                isValidCustomAlarm(false);
                break;
            //more detail 카드뷰
            case R.id.act_detailInput_more_detail_side_btn_gps:         //장소 선택 확장
                isSeeMapCard(true);
                break;
            case R.id.act_detailInput_more_detail_side_btn_alarm:       //알람 확장
                isSeeAlarmCard(true);
                break;
            case R.id.act_detailInput_more_detail_side_btn_memo:        //메모 확장
                isSeeMemoCard(true);
                break;
            case R.id.frag_detailinput_toolbar_back_btn:
                onBackPressed();
                break;
            case R.id.frag_detailinput_toolbar_save_btn:     //세이브버튼
                if (TextUtils.isEmpty(act_detailInput_todo_content_edt.getText())) {
                    act_detailInput_todo_content_edt.setError("내용을 입력해주세요.");
                    return;
                }
                submitTodo();
                break;
            case R.id.frag_detailinput_toolbar_delete_btn:
                deleteTodo();
                DetailInputActivity.this.finish();
                break;
        }
    }

    private void deleteTodo(){
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(TodoObject.class).equalTo("ID", updateTodoId).findFirst().deleteFromRealm();
//                TodoFolderObject folderObject = realm.where(TodoFolderObject.class).equalTo("todoList.ID", todoId).findFirst();
//                folderObject.getTodoList().where().equalTo("ID", todoId).findFirst().deleteFromRealm();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mLocation == null) return;
        mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}

