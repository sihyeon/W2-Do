package com.team.codealmanac.w2do;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
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
import android.widget.Toast;

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
import com.team.codealmanac.w2do.dialog.DatePickerDialogActivity;
import com.team.codealmanac.w2do.dialog.SimpleInputDialog;
import com.team.codealmanac.w2do.models.SimpleTodo;
import com.team.codealmanac.w2do.models.Todo;
import com.team.codealmanac.w2do.models.TodoFolder;

import java.sql.Date;
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
    private long mStartDate;
    private long mEndDate;

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

    //title 부분
    private ImageButton act_detailInput_toolbar_save_btn;
    private Button act_detailInput_toolbar_back_btn;


    private FontContract mFontContract;

    private DatabaseReference mFolderReference;
    private DatabaseReference mTodoReference;
    private DatabaseReference mSimpleTodoReference;
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
        mTodoReference = FirebaseDatabase.getInstance().getReference().child("todo").child(USER_ID);
        mSimpleTodoReference = FirebaseDatabase.getInstance().getReference().child("simple_todo").child(USER_ID);

        // 타이틀 아이템
        act_detailInput_toolbar_save_btn = (ImageButton)findViewById(R.id.act_detailInput_toolbar_save_btn);
        act_detailInput_toolbar_back_btn = (Button)findViewById(R.id.act_detailInput_toolbar_back_btn);

        // 투두 내용 카드뷰 아이템
        act_detailInput_todo_content_edt = (EditText)findViewById(R.id.act_detailInput_todo_content_edt);
        act_detailInput_todo_content_color_picker = (Button)findViewById(R.id.act_detailInput_todo_content_color_picker);
        mPickedColor = ContextCompat.getColor(getApplicationContext(), R.color.blue);
        act_detailInput_todo_content_edt.setTypeface(mFontContract.NahumSquareR_Regular());
        act_detailInput_todo_content_color_picker.setTypeface(mFontContract.NahumSquareR_Regular());

        // 폴더 카드뷰 아이템
        act_detailInput_folder_spinner = (Spinner)findViewById(R.id.act_detailInput_folder_spinner);

        // 캘린더 카드뷰 아이템
        mStartDate = mEndDate = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
        act_detailInput_calendar_start_title_text = (TextView)findViewById(R.id.act_detailInput_calendar_start_title_text);
        act_detailInput_calendar_end_title_text = (TextView)findViewById(R.id.act_detailInput_calendar_end_title_text);
        act_detailInput_calendar_start_date = (TextView)findViewById(R.id.act_detailInput_calendar_start_date);
        act_detailInput_calendar_end_date = (TextView)findViewById(R.id.act_detailInput_calendar_end_date);
        act_detailInput_calendar_allday_btn = (ImageButton)findViewById(R.id.act_detailInput_calendar_allday_btn);
        act_detailInput_calendar_start_title_text.setTypeface(mFontContract.NahumSquareB_Regular());
        act_detailInput_calendar_end_title_text.setTypeface(mFontContract.NahumSquareB_Regular());
        act_detailInput_calendar_start_date.setTypeface(mFontContract.NahumSquareR_Regular());
        act_detailInput_calendar_end_date.setTypeface(mFontContract.NahumSquareR_Regular());
        act_detailInput_calendar_start_date.setText(format.format(mStartDate));
        act_detailInput_calendar_end_date.setText(format.format(mEndDate));
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
        mFolderReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> spinnerItem = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()){
                    spinnerItem.add(item.getValue(TodoFolder.class).name);
                }
                if(!dataSnapshot.exists()){
                    String todoFolderKey = mFolderReference.push().getKey();
                    mFolderReference.child(todoFolderKey).setValue(new TodoFolder(0, "auto-create", 0));
                    mFolderReference.getParent().child("folder_count").setValue(0);
                    spinnerItem.add("auto-create");
                }
                ArrayAdapter<String> Folder_Spinner_Adapter = new FolderSpinnerAdapter(
                        DetailInputActivity.this, R.layout.adpitem_spinner_text, spinnerItem.toArray(new String[spinnerItem.size()]));
                Folder_Spinner_Adapter.setDropDownViewResource(R.layout.adpitem_spinner_dropdown);
                act_detailInput_folder_spinner.setAdapter(Folder_Spinner_Adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
            long timeInMillis = data.getLongExtra("date", 0);
            SimpleDateFormat format = new SimpleDateFormat("M월 d일(E)\nhh:mm a");
            String date = format.format(timeInMillis);

            if(requestCode == DATEPICKER_START_DATE_REQUEST_CODE) {
                act_detailInput_calendar_start_date.setText(date);
                mStartDate = timeInMillis;
            } else {
                act_detailInput_calendar_end_date.setText(date);
                mEndDate = timeInMillis;
            }
        }
    }

    //투두 입력
    private void setTodo(){
        if(TextUtils.isEmpty(act_detailInput_todo_content_edt.getText())){
            act_detailInput_todo_content_edt.setError("내용을 입력해주세요.");
            return;
        }

        mFolderReference.orderByChild("name").equalTo(act_detailInput_folder_spinner.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    TodoFolder todoFolder = data.getValue(TodoFolder.class);
                    data.child("todo_count").getRef().setValue(todoFolder.todo_count+1);
                    Todo todo = new Todo(todoFolder.todo_count, false, mPickedColor,
                            act_detailInput_folder_spinner.getSelectedItem().toString(), act_detailInput_todo_content_edt.getText().toString(),
                            mStartDate, mEndDate, /*alarm*/0, /*alarm-recycle*/false, /*sharing*/null, /*lat*/0, /*lon*/0, /*memo*/null, true);
                    SimpleTodo simpleTodo = new SimpleTodo(mStartDate, act_detailInput_todo_content_edt.getText().toString(),
                            true, false);
                    String todoKey = mTodoReference.push().getKey();
                    String simpleTodoKey = mSimpleTodoReference.push().getKey();
                    mTodoReference.child(todoKey).setValue(todo);
                    mSimpleTodoReference.child(simpleTodoKey).setValue(simpleTodo);
                    Toast.makeText(DetailInputActivity.this, "Todo가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    DetailInputActivity.this.finish();
                    break;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
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
            case R.id.act_detailInput_toolbar_back_btn:
                finish();
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
}

