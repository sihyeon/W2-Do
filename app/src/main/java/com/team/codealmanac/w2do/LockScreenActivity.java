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

import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.team.codealmanac.w2do.adapter.TodoLockScreenAdapter;
import com.team.codealmanac.w2do.assistant.LocationInfoAssistant;
import com.team.codealmanac.w2do.contract.FontContract;
import com.team.codealmanac.w2do.database.PreferencesManager;
import com.team.codealmanac.w2do.database.SQLiteManager;
import com.team.codealmanac.w2do.listeners.OnSwipeTouchListener;
import com.team.codealmanac.w2do.models.MainSchedule;
import com.team.codealmanac.w2do.service.W2DoForeGroundService;

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

    public static final String TYPE_TODO = "todo";
    public static final String TYPE_MAINSCHEDULE = "main_schedule";

    private LocationInfoAssistant mLocationInfoManager;

    private FontContract mFont;
    private RelativeLayout layout_lock_screen_main;
    private ImageView act_lockscreen_left_guide_icon;
    private ImageView act_lockscreen_right_guide_icon;
    private ViewGroup.LayoutParams mLeft_Icon_Original_Params;
    private ViewGroup.LayoutParams mRight_Icon_Original_Params;

    private FrameLayout act_lockscreen_mask_mainschedule;
    private FrameLayout act_lockscreen_mask_todo;

    private LinearLayout act_lockscreen_layout_todo_layout;
    private RecyclerView act_lockscreen_todo_listview;

    private LinearLayout act_lockscreen_layout_exist_mainschedule;
    private LinearLayout act_lockscreen_layout_ignore_mainschedule;

    SQLiteManager sqliteManager;
    private PreferencesManager mPreferencesManager;

    //인터페이스
    @Override
    public void setLocation(Location location) {
        if (location != null && getNetworkState()) {
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
        sqliteManager = new SQLiteManager(getApplicationContext());
        mPreferencesManager = new PreferencesManager(getApplicationContext());
        //상태바 없앰

        /*  FLAG_SHOW_WHEN_LOCKED = 잠금화면 위로 액티비티 실행
            FLAG_DISMISS_KEYGUARD = 키 가드 해제 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationInfoManager = LocationInfoAssistant.getInstance();
            mLocationInfoManager.onStartLocation(getApplicationContext(), this);
        }
        //메인스케줄 레이아웃
        act_lockscreen_layout_exist_mainschedule = (LinearLayout) findViewById(R.id.act_lockscreen_layout_exist_mainschedule);
        act_lockscreen_layout_ignore_mainschedule = (LinearLayout) findViewById(R.id.act_lockscreen_layout_ignore_mainschedule);

        //투두
        act_lockscreen_layout_todo_layout = (LinearLayout) findViewById(R.id.act_lockscreen_layout_todo_layout);
        act_lockscreen_todo_listview = (RecyclerView) findViewById(R.id.act_lockscreen_todo_listview);

        // 그림자 레이어 선언
        act_lockscreen_mask_mainschedule = (FrameLayout) findViewById(R.id.act_lockscreen_mask_mainschedule);
        act_lockscreen_mask_todo = (FrameLayout) findViewById(R.id.act_lockscreen_mask_todo);

        // 스와이프 애니메이션 실행 준비
        layout_lock_screen_main = (RelativeLayout) findViewById(R.id.layout_lock_screen_main);
        act_lockscreen_right_guide_icon = (ImageView) findViewById(R.id.act_lockscreen_right_guide_icon);
        act_lockscreen_left_guide_icon = (ImageView) findViewById(R.id.act_lockscreen_left_guide_icon);
        final int icon_original_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 57, getResources().getDisplayMetrics());

        //스와이프 동작.
        layout_lock_screen_main.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeLeft() {
                // 왓투두 앱 실행하게 된다.
                Intent mainAppIntent = new Intent(LockScreenActivity.this, LoginActivity.class);
                mainAppIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainAppIntent);
                finish();
            }

            public void onSwipeRight() {
                // 본인 잠금 화면, 홈화면으로 진입하게 된다.
                finish();
            }

            public void onSwipingEvent(float diffX){
                if (diffX > 0) {
                    ViewGroup.LayoutParams Right_Icon_Params = act_lockscreen_right_guide_icon.getLayoutParams();
                    Right_Icon_Params.width = icon_original_size + (int)Math.abs(diffX);
                    Right_Icon_Params.height = icon_original_size + (int)Math.abs(diffX);
                    act_lockscreen_right_guide_icon.setLayoutParams(Right_Icon_Params);
                } else {
                    ViewGroup.LayoutParams Left_Icon_Params = act_lockscreen_left_guide_icon.getLayoutParams();
                    Left_Icon_Params.width = icon_original_size + (int)Math.abs(diffX);
                    Left_Icon_Params.height = icon_original_size + (int)Math.abs(diffX);
                    act_lockscreen_left_guide_icon.setLayoutParams(Left_Icon_Params);
                }
            }
            public void onSingleTapConfirmed(){
                ViewGroup.LayoutParams Left_Icon_Params = act_lockscreen_left_guide_icon.getLayoutParams();
                Left_Icon_Params.width = icon_original_size;
                Left_Icon_Params.height = icon_original_size;
                act_lockscreen_left_guide_icon.setLayoutParams(Left_Icon_Params);

                ViewGroup.LayoutParams Right_Icon_Params = act_lockscreen_right_guide_icon.getLayoutParams();
                Right_Icon_Params.width = icon_original_size;
                Right_Icon_Params.height = icon_original_size;
                act_lockscreen_right_guide_icon.setLayoutParams(Right_Icon_Params);
            }

        });

        //폰트지정
        ((TextView) findViewById(R.id.act_lockscreen_greeting)).setTypeface(mFont.NahumSquareR_Regular());
        ((TextView) findViewById(R.id.act_lockscreen_nickname)).setTypeface(mFont.NahumSquareR_Regular());
        ((TextView) findViewById(R.id.act_lockscreen_mainschedule_header)).setTypeface(mFont.RobotoMedium());
        ((TextView) findViewById(R.id.act_lockscreen_mainschedule)).setTypeface(mFont.NahumSquareR_Regular());
        ((TextView) findViewById(R.id.act_lockscreen_what_mainschedule)).setTypeface(mFont.NahumSquareB_Regular());


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
        String format = "MM .dd  EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        date.setText(dateFormat.format(new Date()));

        setGreetingText(mPreferencesManager.getNickname());

        // TODO: 2017-06-12 메인스케줄 / 투두 보이기 구현
        String lockScreenType = mPreferencesManager.getLockScreenType();
        //메인스케줄
        if (lockScreenType.equals(TYPE_MAINSCHEDULE)) {
            MainSchedule mainSchedule = sqliteManager.getMainSchedule();
            if (mainSchedule != null) {
                existMainSchedule(mainSchedule.content);
            } else {
                nonexistMainSchedule();
            }
        } else if (lockScreenType.equals(TYPE_TODO)) {
            callTodoShow();
        }
        stopService(new Intent(getApplicationContext(), W2DoForeGroundService.class));
    }

    @Override
    protected void onStop() {
        startService(new Intent(getApplicationContext(), W2DoForeGroundService.class));
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mLocationInfoManager != null) mLocationInfoManager.onStopLocation();
        super.onDestroy();
    }

    //지역정보를 불러오는데 하나라도 실패하면 전부 안보이게함.
    private void setLocationVisibility(boolean visibility) {
        if (visibility)
            findViewById(R.id.act_lockscreen_location_layout).setVisibility(View.VISIBLE);
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
        temperatureText.setText(String.format(Locale.KOREA, "%dº", (int) temperature));
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
            greetingMessage = morning[(int) (Math.random() * morning.length)];
        } else if (12 <= presentHour && presentHour <= 18) {     //오후(점심)
            String[] afternoon = getResources().getStringArray(R.array.greetings_afternoon);
            greetingMessage = afternoon[(int) (Math.random() * afternoon.length)];
        } else {                                                 //저녁
            String[] evening = getResources().getStringArray(R.array.greetings_evening);
            greetingMessage = evening[(int) (Math.random() * evening.length)];
        }

        greetingText.setText(greetingMessage);
        if (nickname != null) userNameText.setText(nickname);
    }

    public void existMainSchedule(String mainSchedule) {
        act_lockscreen_layout_exist_mainschedule.setVisibility(View.VISIBLE);
        act_lockscreen_layout_ignore_mainschedule.setVisibility(View.GONE);
        act_lockscreen_mask_mainschedule.setVisibility(View.VISIBLE);
        act_lockscreen_mask_todo.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.act_lockscreen_mainschedule)).setText(mainSchedule);
        Log.d(TAG, "existMainSchedule 불림");
    }

    public void nonexistMainSchedule() {
        act_lockscreen_layout_exist_mainschedule.setVisibility(View.GONE);
        act_lockscreen_layout_ignore_mainschedule.setVisibility(View.VISIBLE);
        act_lockscreen_mask_mainschedule.setVisibility(View.VISIBLE);
        act_lockscreen_mask_todo.setVisibility(View.GONE);
        Log.d(TAG, "nonexistMainSchedule 불림");
    }

    public void callTodoShow() {
        act_lockscreen_layout_exist_mainschedule.setVisibility(View.GONE);
        act_lockscreen_layout_ignore_mainschedule.setVisibility(View.GONE);
        act_lockscreen_mask_todo.setVisibility(View.VISIBLE);
        act_lockscreen_mask_mainschedule.setVisibility(View.GONE);

        act_lockscreen_layout_todo_layout.setVisibility(View.VISIBLE);
        act_lockscreen_todo_listview.setHasFixedSize(true);
        act_lockscreen_todo_listview.setLayoutManager(new GridLayoutManager(this, 1));
        TodoLockScreenAdapter todoAdapter = new TodoLockScreenAdapter(getApplicationContext());
        act_lockscreen_todo_listview.setAdapter(todoAdapter);
        Log.d(TAG, "Calltodoshow 불림");
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
