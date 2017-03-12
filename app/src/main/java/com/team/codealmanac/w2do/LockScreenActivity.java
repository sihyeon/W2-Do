package com.team.codealmanac.w2do;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team.codealmanac.w2do.assistant.LocationInfoAssistant;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.listeners.OnSwipeTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LockScreenActivity extends BaseActivity implements LocationInfoAssistant.InterfaceLocationInfoManager {
    private final int GEO_PERMISSIONS_REQUEST = 1;
    private LocationInfoAssistant mLocationInfoManager;

    private boolean isPermission;

    private FontContract mFont;

    private DatabaseReference mMainScheduleReference;
    private DatabaseReference mUserReference;
    private ValueEventListener mMainScheduleListener;

    //인터페이스
    @Override
    public void setLocation(Location location) {
        if (location != null && isPermission && getNetworkState()) {
            setAddress(location);
            getWeatherData(location);
            setLocationVisibility(true);
        } else {
            setLocationVisibility(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        mFont = new FontContract(getAssets());
        //상태바 없앰
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*  FLAG_SHOW_WHEN_LOCKED = 잠금화면 위로 액티비티 실행
            FLAG_DISMISS_KEYGUARD = 키 가드 해제 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        permissionChecking();
        if (isPermission) {
            mLocationInfoManager = LocationInfoAssistant.getInstance();
            mLocationInfoManager.onStartLocation(getApplicationContext(), this);
        }



        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // [START initialize_database_ref]
        mMainScheduleReference = FirebaseDatabase.getInstance().getReference().child("main_schedule").child(userId).child("visible");
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        // [END initialize_database_ref]

        //스와이프 동작.
        findViewById(R.id.layout_lock_screen_main).setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            public void onSwipeLeft(){
                Intent mainappintent = new Intent(LockScreenActivity.this, MainActivity.class);
                startActivity(mainappintent);
                finish();
            }
            public void onSwipeRight(){
                Toast.makeText(LockScreenActivity.this, "Right", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //폰트지정
        ((TextView)findViewById(R.id.act_lockscreen_greeting)).setTypeface(mFont.NahumSquareB_Regular());
        ((TextView)findViewById(R.id.act_lockscreen_nickname)).setTypeface(mFont.NahumSquareB_Regular());
        ((TextView)findViewById(R.id.act_lockscreen_mainschedule_header)).setTypeface(mFont.FranklinGothic_Demi());
        ((TextView)findViewById(R.id.act_lockscreen_mainschedule)).setTypeface(mFont.NahumSquareB_Regular());
        ((TextView)findViewById(R.id.act_lockscreen_what_mainschedule)).setTypeface(mFont.NahumSquareB_Regular());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) findViewById(R.id.act_lockscreen_digital_clock);
        digitalClock.setTypeface(mFont.YiSunShinDotumM_Regular());

        //datetime
        TextView date = (TextView) findViewById(R.id.act_lockscreen_date);
        date.setTypeface(mFont.YiSunShinDotumM_Regular());
        String format = new String("MM .dd  EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        date.setText(sdf.format(new Date()));



        //파베 실시간디비 리스너 등록
        mUserReference.child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    User_NickName item = dataSnapshot.getValue(User_NickName.class);
                    String item = dataSnapshot.getValue().toString();
                    Log.d("파베테스트", dataSnapshot.toString());
                    setGreetingText(item);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        ValueEventListener mainScheduleListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    existMainSchedule(dataSnapshot.getValue().toString());
                } else {
                    nonexistMainSchedule();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        mMainScheduleReference.addValueEventListener(mainScheduleListener);
        mMainScheduleListener = mainScheduleListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mMainScheduleListener != null){
            mMainScheduleReference.removeEventListener(mMainScheduleListener);
        }
    }

    @Override
    protected void onDestroy() {
        if (isPermission) mLocationInfoManager.onStopLocation();
        super.onDestroy();
    }

    private void permissionChecking() {
        String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE};
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(getApplicationContext(), permission) != PermissionChecker.PERMISSION_GRANTED) {
                //권한이 없을 경우 권한을 요청
                ActivityCompat.requestPermissions(this, permissions, GEO_PERMISSIONS_REQUEST);
                isPermission = false;
                return;
            }
        }
        isPermission = true;
    }

    // 권한 요청의 결과를 받아옴.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("LcokScreenFragment", "리퀘스트퍼미션리절트 진입");
        if (requestCode == GEO_PERMISSIONS_REQUEST) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {  //권한 받음
                    Log.d("LcokScreenFragment", "권한 받음");
                } else { //권한 거부함
                    Log.d("LcokScreenFragment", "권한 거부함");
                    isPermission = false;
                    return;
                }
            }
            isPermission = true;
            mLocationInfoManager = LocationInfoAssistant.getInstance();
            mLocationInfoManager.onStartLocation(getApplicationContext(), this);
        }
    }

    //지역정보를 불러오는데 하나라도 실패하면 전부 안보이게함.
    private void setLocationVisibility(boolean visibility) {
        if (visibility) {
            findViewById(R.id.act_lockscreen_location).setVisibility(View.VISIBLE);
            findViewById(R.id.act_lockscreen_weathericon).setVisibility(View.VISIBLE);
            findViewById(R.id.act_lockscreen_temperature).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.act_lockscreen_location).setVisibility(View.INVISIBLE);
            findViewById(R.id.act_lockscreen_weathericon).setVisibility(View.INVISIBLE);
            findViewById(R.id.act_lockscreen_temperature).setVisibility(View.INVISIBLE);
        }
    }

    //네트워크 상태 체크
    private boolean getNetworkState() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) return true;
        else return false;
    }

    //OpenWeatherMap에서 날씨정보를 받아옴.
    private void getWeatherData(Location location) {
        if (!getNetworkState()) return;
        String key = "e883ecf0bc01088daed574539d20aa43";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://api.openweathermap.org/data/2.5/weather?" +
                "APPID=" + key +
                "&lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() +
                "&mode=json&units=metric";
        Log.d("jsonTest", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //날씨 텍스트를 받아와서 아이콘을 지정함, 그리고 온도를 지정
                            setWeatherText(response.getJSONArray("weather").getJSONObject(0).getInt("id")
                                    , response.getJSONObject("menu_main_toolbar").getDouble("temp"));
                            setLocationVisibility(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setLocationVisibility(false);
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    //날씨 아이콘 선택
    private void setWeatherText(int weather, double temperature) {
        int presentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String weatherIconStringId = "wi_";
        if (weather == 800) { //clear sky
            if (5 <= presentHour && presentHour <= 17) {
                //아침 & 낮
                weatherIconStringId += "day_sunny";
            } else {
                //저녁
                weatherIconStringId += "night_clear";
            }
        } else if (801 <= weather && weather <= 804) { //구름
            switch (weather) {
                case 801:
                    if (5 <= presentHour && presentHour <= 17) {
                        //아침 & 낮
                        weatherIconStringId += "day_cloudy";
                    } else {
                        //저녁
                        weatherIconStringId += "night_alt_cloudy";
                    }
                    break;
                case 802:
                    weatherIconStringId += "cloud";
                    break;
                default:
                    weatherIconStringId += "cloudy";
            }
        } else if ((500 <= weather && weather <= 531) || (300 <= weather && weather <= 321)) { //비
            if (weather == 521) {
                weatherIconStringId += "showers";
            } else {
                weatherIconStringId += "rain";
            }
        } else if (200 <= weather && weather <= 232) {  //폭풍
            weatherIconStringId += "thunderstorm";
        } else if (600 <= weather && weather <= 622) {  //눈
            weatherIconStringId += "snow";
        } else if (701 <= weather && weather <= 781) {  //안개
            weatherIconStringId += "fog";
        } else {
            weatherIconStringId += "na";    //모름
        }
        //날씨 아이콘 지정
        int resId = getResources().getIdentifier(weatherIconStringId, "string", getPackageName());
        TextView weatherIconText = (TextView) findViewById(R.id.act_lockscreen_weathericon);
        weatherIconText.setTypeface(Typeface.createFromAsset(getAssets(), "weathericons-regular-webfont.ttf"));
        weatherIconText.setText(getString(resId));

        //온도 지정
        TextView temperatureText = (TextView) findViewById(R.id.act_lockscreen_temperature);
        Typeface tempType = Typeface.createFromAsset(getAssets(), "FranklinGothicMediumCond.TTF");
        temperatureText.setTypeface(tempType);
        temperatureText.setText((int) temperature + "º");
    }

    //지역정보를 설정함
    private void setAddress(Location location) {
        if (!getNetworkState()) return;
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREAN);
            List<Address> addrData = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
            if (addrData != null) {
                if (addrData.get(0).getLocality() != null || addrData.get(0).getSubLocality() != null) {
                    String address = (addrData.get(0).getLocality() == null ? "" : addrData.get(0).getLocality())
                            + " " +
                            (addrData.get(0).getSubLocality() == null ? "" : addrData.get(0).getSubLocality());
                    TextView addrText = (TextView) findViewById(R.id.act_lockscreen_location);
                    Typeface addrType = Typeface.createFromAsset(getAssets(), "NanumSquareR.ttf");
                    addrText.setTypeface(addrType);
                    addrText.setText(address);
                    setLocationVisibility(true);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLocationVisibility(false);
    }

    private void setGreetingText(String nickname) {
        String greetingMessage = "";
        int presentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        TextView greetingText = (TextView) findViewById(R.id.act_lockscreen_greeting);
        TextView userNameText = (TextView) findViewById(R.id.act_lockscreen_nickname);

        //아침
        if (4 <= presentHour && presentHour <= 11)
            greetingMessage += getString(R.string.greetings_morning_1);
            //오후(점심)
        else if (12 <= presentHour && presentHour <= 18)
            greetingMessage += getString(R.string.greetings_afternoon_1);
            //저녁
        else greetingMessage += getString(R.string.greetings_evening_1);

        greetingText.setText(greetingMessage);
        userNameText.setText(nickname);
    }

    private void existMainSchedule(String mainSchedule){
        findViewById(R.id.act_lockscreen_layout_exist_mainschedule).setVisibility(View.VISIBLE);
        findViewById(R.id.act_lockscreen_layout_ignore_mainschedule).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.act_lockscreen_mainschedule)).setText(mainSchedule);
    }

    private void nonexistMainSchedule(){
        findViewById(R.id.act_lockscreen_layout_exist_mainschedule).setVisibility(View.GONE);
        findViewById(R.id.act_lockscreen_layout_ignore_mainschedule).setVisibility(View.VISIBLE);
    }

    //메뉴키, 백키 잠금
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_HOME:
//            case KeyEvent.KEYCODE_ESCAPE:
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
