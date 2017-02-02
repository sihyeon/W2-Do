package com.team.codealmanac.w2do;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import petrov.kristiyan.colorpicker.ColorPicker;

public class DetailInputActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback{
    // cardview items : 폴더 선택,내용입력,색상 설정
    private CardView act_detailInput_title_cardview;
    private Spinner act_detailInput_folder_spinner;
    private TextView act_detailInput_title_text;
    private Button act_detailInput_color_picker;
    private EditText act_detailInput_content_edittext;
    private TextView act_detailInput_folder_text;

    // cardview items : 캘린더,시간설정
    private CardView act_detailInput_calendar_cardview;
    private TextView act_detailInput_calendar_start_text;
    private TextView act_detailInput_calendar_end_text;
    private TextView act_detailInput_start_date_display;
    private TextView act_detailInput_end_date_display;
    private Button act_detailInput_calendar_allday_btn;
    private TextView act_detailInput_start_time_display;
    private TextView act_detailInput_end_time_display;

    // cardview items : 지도
    private boolean isLocationButtonOneClick = true;
    private CardView act_detailInput_map_cardview;
    private EditText act_detailInput_map_location;
    private MapFragment act_detailInput_googleMap;
    private GoogleMap mGoogleMap;
    private LatLng mLocation;
    private Marker mMarker;

    //cardview items: more detail buttons
    private CardView act_detailInput_more_detail_btn_cardview;
    private TextView act_detailInput_more_detail_title_text;
    private Button act_detailInput_more_detail_side_btn_gps;
    private Button act_detailInput_more_detail_side_btn_alarm;
    private Button act_detailInput_more_detail_side_btn_memo;
    private Button act_detailInput_more_detail_side_btn_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_input);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        // title cardview items
        act_detailInput_title_cardview = (CardView)findViewById(R.id.act_detailInput_title_cardview);
        act_detailInput_title_text = (TextView)findViewById(R.id.act_detailInput_title_text);
        act_detailInput_color_picker = (Button)findViewById(R.id.act_detailInput_color_picker);
        act_detailInput_content_edittext = (EditText)findViewById(R.id.act_detailInput_content_edittext);
        act_detailInput_folder_text = (TextView)findViewById(R.id.act_detailInput_folder_text);
        act_detailInput_folder_spinner = (Spinner)findViewById(R.id.act_detailInput_folder_spinner);

        // calendar cardview items
        act_detailInput_calendar_cardview = (CardView)findViewById(R.id.act_detailInput_calendar_cardview);
        act_detailInput_calendar_start_text = (TextView)findViewById(R.id.act_detailInput_calendar_start_text);
        act_detailInput_calendar_end_text = (TextView)findViewById(R.id.act_detailInput_calendar_end_text);
        act_detailInput_start_date_display = (TextView)findViewById(R.id.act_detailInput_start_date_display);
        act_detailInput_end_date_display = (TextView)findViewById(R.id.act_detailInput_end_date_display);
        act_detailInput_start_time_display = (TextView)findViewById(R.id.act_detailInput_start_time_display);
        act_detailInput_end_time_display = (TextView)findViewById(R.id.act_detailInput_end_time_display);
        act_detailInput_calendar_allday_btn = (Button)findViewById(R.id.act_detailInput_calendar_allday_btn);

        //google map cardview items
        act_detailInput_map_cardview = (CardView)findViewById(R.id.act_detailInput_map_cardview);
        act_detailInput_map_location = (EditText)findViewById(R.id.act_detailInput_map_location);
        act_detailInput_googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.act_detailInput_googleMap);
        act_detailInput_googleMap.getMapAsync(this);

        //more_detail cardview items
        act_detailInput_more_detail_btn_cardview = (CardView)findViewById(R.id.act_detailInput_more_detail_btn_cardview);
        act_detailInput_more_detail_title_text = (TextView)findViewById(R.id.act_detailInput_more_detail_title_text);
        act_detailInput_more_detail_side_btn_gps = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_gps);
        act_detailInput_more_detail_side_btn_alarm = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_alarm);
        act_detailInput_more_detail_side_btn_memo = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_memo);
        act_detailInput_more_detail_side_btn_share = (Button)findViewById(R.id.act_detailInput_more_detail_side_btn_share);

        //OnClickListener 연결
        act_detailInput_color_picker.setOnClickListener(this);
        act_detailInput_start_date_display.setOnClickListener(this);
        act_detailInput_end_date_display.setOnClickListener(this);
        act_detailInput_start_time_display.setOnClickListener(this);
        act_detailInput_end_time_display.setOnClickListener(this);


        // 폴더 선택 spinner adapter
        String[] Folder_Spinner_item = getResources().getStringArray(R.array.folder);
        ArrayAdapter<String> Folder_Spinner_Adapter = new ArrayAdapter<String>(
                this, R.layout.activity_detailinput_folder_spinner_textview, Folder_Spinner_item);
        Folder_Spinner_Adapter.setDropDownViewResource(R.layout.activity_detailinput_folder_spinner_textview);
        act_detailInput_folder_spinner.setAdapter(Folder_Spinner_Adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.act_detailInput_googleMap).setVisibility(View.GONE);

        act_detailInput_map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLocationButtonOneClick){
                    isLocationButtonOneClick = false;
                    if(mMarker != null) mMarker.remove();
                    int PLACE_PICKER_REQUEST = 1;
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(DetailInputActivity.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        View.OnClickListener MoreDetailButtonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.act_detailInput_more_detail_side_btn_gps:
                        act_detailInput_map_cardview.setVisibility(View.VISIBLE);
                        act_detailInput_more_detail_side_btn_gps.setVisibility(View.GONE);
                        break;
                }
            }
        };

        act_detailInput_googleMap.setMenuVisibility(false);

        act_detailInput_more_detail_side_btn_gps.setOnClickListener(MoreDetailButtonsListener);
        act_detailInput_more_detail_side_btn_alarm.setOnClickListener(MoreDetailButtonsListener);
        act_detailInput_more_detail_side_btn_memo.setOnClickListener(MoreDetailButtonsListener);
        act_detailInput_more_detail_side_btn_share.setOnClickListener(MoreDetailButtonsListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            isLocationButtonOneClick = true;
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                act_detailInput_map_location.setText(place.getName());
                mLocation = place.getLatLng();
                if(mGoogleMap != null){
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                    findViewById(R.id.act_detailInput_googleMap).setVisibility(View.VISIBLE);
                }
            } else if(resultCode == RESULT_CANCELED){
                if(mGoogleMap != null && mLocation != null){
                    mMarker = mGoogleMap.addMarker(new MarkerOptions().position(mLocation));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                }
            }
        }
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
                Intent start_date_time_picker = new Intent(DetailInputActivity.this,DateTimeMainTabActivity.class);
                startActivity(start_date_time_picker);
                break;
            case R.id.act_detailInput_end_date_display:
                Intent end_date_time_picker = new Intent(DetailInputActivity.this,DateTimeMainTabActivity.class);
                startActivity(end_date_time_picker);
                break;
            case R.id.act_detailInput_start_time_display:
                Intent start_time_picker = new Intent(DetailInputActivity.this,DateTimeMainTabActivity.class);
                startActivity(start_time_picker);
                break;
            case R.id.act_detailInput_end_time_display:
                Intent end_time_picker = new Intent(DetailInputActivity.this,DateTimeMainTabActivity.class);
                startActivity(end_time_picker);
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

