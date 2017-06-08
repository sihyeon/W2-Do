package com.team.codealmanac.w2do;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.transition.ChangeBounds;
import android.support.transition.Fade;

import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.os.Bundle;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.team.codealmanac.w2do.assistant.LocationInfoAssistant;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.PreferencesManager;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.listeners.OnSwipeTouchListener;
import com.team.codealmanac.w2do.models.MainSchedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LockScreenActivity extends BaseActivity implements LocationInfoAssistant.InterfaceLocationInfoManager {
    private final String TAG = "LockScreenActivity";
    private final int GEO_PERMISSIONS_REQUEST = 1;
    private LocationInfoAssistant mLocationInfoManager;

    private boolean isPermission;
    private FontContract mFont;
    private RelativeLayout layout_lock_screen_main;
    private ImageView lefticon;
    private ImageView righticon;
    private boolean sizeChanged = false;
    private FrameLayout bg_ms_layer;
    private FrameLayout bg_todo_layer;

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

        // 그림자 레이어 선언
        bg_ms_layer = (FrameLayout)findViewById(R.id.bg_ms_layer);
        bg_todo_layer = (FrameLayout)findViewById(R.id.bg_todo_layer);

        // 스와이프 애니메이션 실행 준비
        layout_lock_screen_main = (RelativeLayout)findViewById(R.id.layout_lock_screen_main);
        lefticon = (ImageView)findViewById(R.id.left_icon);
        righticon = (ImageView)findViewById(R.id.right_icon);

        //스와이프 동작.
        layout_lock_screen_main.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            public void onSwipeLeft(){
                // 왓투두 앱 실행하게 된다.
                Intent mainappintent = new Intent(LockScreenActivity.this, LoginActivity.class);
                mainappintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainappintent);
                finish();
            }
            public void onSwipeRight(){
                // 본인 잠금 화면, 홈화면으로 진입하게 된다.
                finish();
            }
        });

        setGreetingText(PreferencesManager.getNickname(getApplicationContext()));

        //폰트지정
        ((TextView)findViewById(R.id.act_lockscreen_greeting)).setTypeface(mFont.NahumSquareR_Regular());
        ((TextView)findViewById(R.id.act_lockscreen_nickname)).setTypeface(mFont.NahumSquareR_Regular());
        ((TextView)findViewById(R.id.act_lockscreen_mainschedule_header)).setTypeface(mFont.RobotoMedium());
        ((TextView)findViewById(R.id.act_lockscreen_mainschedule)).setTypeface(mFont.NahumSquareR_Regular());
        ((TextView)findViewById(R.id.act_lockscreen_what_mainschedule)).setTypeface(mFont.NahumSquareB_Regular());

        String nickname = PreferencesManager.getNickname(getApplicationContext());
        //파베 실시간디비 리스너 등록
        setGreetingText(nickname);

        SQLiteManager sqliteManager = new SQLiteManager(getApplicationContext());
        MainSchedule mainSchedule = sqliteManager.getMainSchedule();
        if(mainSchedule != null){
            existMainSchedule(mainSchedule.content);
        } else {
            nonexistMainSchedule();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) findViewById(R.id.act_lockscreen_digital_clock);
        digitalClock.setTypeface(mFont.RobotoThin());

        //datetime
        TextView date = (TextView) findViewById(R.id.act_lockscreen_date);
        date.setTypeface(mFont.RobotoLight());
        String format = new String("MM .dd  EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        date.setText(sdf.format(new Date()));

        setGreetingText(null);
    }

    @Override
    protected void onStop() {
        super.onStop();

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
                    LockScreenActivity.this.finish();
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
        if (visibility) findViewById(R.id.act_lockscreen_location_layout).setVisibility(View.VISIBLE);
        else findViewById(R.id.act_lockscreen_location_layout).setVisibility(View.INVISIBLE);
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
                                    , response.getJSONObject("main").getDouble("temp"));
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
        temperatureText.setTypeface(mFont.RobotoLight());
        temperatureText.setText(String.format(Locale.KOREA, "%dº", (int)temperature));
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
                    //장소 텍스트 뷰 글꼴 선언
                    TextView addrText = (TextView) findViewById(R.id.act_lockscreen_location);
                    addrText.setTypeface(mFont.NahumSquareR_Regular());
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
        String greetingMessage;
        int presentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        TextView greetingText = (TextView) findViewById(R.id.act_lockscreen_greeting);
        TextView userNameText = (TextView) findViewById(R.id.act_lockscreen_nickname);

        if (4 <= presentHour && presentHour <= 11) {            //아침
            String[] morning = getResources().getStringArray(R.array.greetings_morning);
            greetingMessage = morning[(int)(Math.random() * morning.length)];
        }else if (12 <= presentHour && presentHour <= 18) {     //오후(점심)
            String[] afternoon = getResources().getStringArray(R.array.greetings_afternoon);
            greetingMessage = afternoon[(int)(Math.random() * afternoon.length)];
        }else {                                                 //저녁
            String[] evening = getResources().getStringArray(R.array.greetings_evening);
            greetingMessage = evening[(int)(Math.random()*evening.length)];
        }

        greetingText.setText(greetingMessage);
        if(nickname != null) userNameText.setText(nickname);
    }

    private void existMainSchedule(String mainSchedule){
        findViewById(R.id.act_lockscreen_layout_exist_mainschedule).setVisibility(View.VISIBLE);
        findViewById(R.id.act_lockscreen_layout_ignore_mainschedule).setVisibility(View.GONE);
        bg_ms_layer.setVisibility(View.VISIBLE);
        bg_todo_layer.setVisibility(View.GONE);
        ((TextView)findViewById(R.id.act_lockscreen_mainschedule)).setText(mainSchedule);
        Log.d(TAG, "existMainSchedule 불림");
    }

    private void nonexistMainSchedule(){
        findViewById(R.id.act_lockscreen_layout_exist_mainschedule).setVisibility(View.GONE);
        findViewById(R.id.act_lockscreen_layout_ignore_mainschedule).setVisibility(View.VISIBLE);
        bg_ms_layer.setVisibility(View.VISIBLE);
        bg_todo_layer.setVisibility(View.GONE);
        Log.d(TAG, "nonexistMainSchedule 불림");
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
