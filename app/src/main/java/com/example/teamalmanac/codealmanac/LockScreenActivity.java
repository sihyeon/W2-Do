package com.example.teamalmanac.codealmanac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.example.teamalmanac.codealmanac.database.DataManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LockScreenActivity extends AppCompatActivity implements LocationInfoManager.InterfaceLocationInfoManager{
    private final int GEO_PERMISSIONS_REQUEST = 1;

    private LocationInfoManager mLocationInfoManager;
    private DataManager mDB = null;

    private boolean isPermission;

    @Override
    public void setLocation(Location location) {
        if (location != null && isPermission && getNetworkState()) {
            setAddress(location);
            setWeather(location);
            setLocationVisibility(true);
        } else {
            setLocationVisibility(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        //스와이프 동작.
        findViewById(R.id.layout_lock_screen_main).setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            public void onSwipeLeft(){
                Intent mainappintent = new Intent(LockScreenActivity.this, APP2MAIN.class);
                startActivity(mainappintent);
                finish();
            }
            public void onSwipeRight(){
                Toast.makeText(LockScreenActivity.this, "Right", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mDB = new DataManager(getApplicationContext());

        //상태바 없앰
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*  FLAG_SHOW_WHEN_LOCKED = 잠금화면 위로 액티비티 실행
            FLAG_DISMISS_KEYGUARD = 키 가드 해제 */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        permissionChecking();
        if(isPermission) {
            mLocationInfoManager = LocationInfoManager.getInstance();
            mLocationInfoManager.onStartLocation(getApplicationContext(), this);
        }

        setMainText();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Digital Clock FONT asset
        TextClock digitalClock = (TextClock) findViewById(R.id.digital_clock);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "YiSunShinDotumM-Regular.ttf");
        digitalClock.setTypeface(typeface);

        //datetime
        TextView dt = (TextView) findViewById(R.id.text_date);
        String format = new String("MM .dd  EEEE");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        dt.setText(sdf.format(new Date()));
        Typeface type = Typeface.createFromAsset(getAssets(), "YiSunShinDotumM-Regular.ttf");
        dt.setTypeface(type);

        //화면 이동 메세지 폰트
        TextView mv = (TextView) findViewById(R.id.text_screen_pull);
        Typeface mvtype = Typeface.createFromAsset(getAssets(), "NanumSquareR.ttf");
        mv.setTypeface(mvtype);
    }

    @Override
    protected void onDestroy() {
        if(isPermission) mLocationInfoManager.onStopLocation();
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
            mLocationInfoManager = LocationInfoManager.getInstance();
            mLocationInfoManager.onStartLocation(getApplicationContext(), this);
        }
    }

    //지역정보를 불러오는데 하나라도 실패하면 전부 안보이게함.
    private void setLocationVisibility(boolean visibility) {
        if (visibility) {
            findViewById(R.id.text_location).setVisibility(View.VISIBLE);
            findViewById(R.id.text_weather_icon).setVisibility(View.VISIBLE);
            findViewById(R.id.text_temp).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.text_location).setVisibility(View.INVISIBLE);
            findViewById(R.id.text_weather_icon).setVisibility(View.INVISIBLE);
            findViewById(R.id.text_temp).setVisibility(View.INVISIBLE);
        }
    }

    //네트워크 상태 체크
    private boolean getNetworkState(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null) return true;
        else return false;
    }

    //지역정보를 설정함
    private void setAddress(Location location) {
        if(!getNetworkState()) return;
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.KOREAN);
            List<Address> addrData = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
            if (addrData != null) {
                if (addrData.get(0).getLocality() != null || addrData.get(0).getSubLocality() != null) {
                    String address = (addrData.get(0).getLocality() == null ? "" : addrData.get(0).getLocality())
                            + " " +
                            (addrData.get(0).getSubLocality() == null ? "" : addrData.get(0).getSubLocality());
                    TextView addrText = (TextView) findViewById(R.id.text_location);
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

    //OpenWeatherMap에서 날씨정보를 받아옴.
    private void setWeather(Location location) {
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
                            //날씨 텍스트를 받아와서 아이콘을 지정함
//                            Log.d("jsonTest", response.getJSONArray("weather").getJSONObject(0).getString("description"));
                            setWeathersIcon(response.getJSONArray("weather").getJSONObject(0).getInt("id"));
                            //온도 받아와서 온도지정
//                            Log.d("jsonTest", response.getJSONObject("main").getString("temp") + "");
                            setTemperature(response.getJSONObject("main").getDouble("temp"));
                            setLocationVisibility(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("error", "not data");   //json으로 데이터를 받는데 실패하면 날씨가 안보이게함.
                setLocationVisibility(false);
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setTemperature(double temperature) {
        TextView temperatureText = (TextView) findViewById(R.id.text_temp);
        Typeface tempType = Typeface.createFromAsset(getAssets(), "FranklinGothic-MediumCond.TTF");
        temperatureText.setTypeface(tempType);
        temperatureText.setText((int) temperature + "º");
//        temperatureText.setText(String.format("%.0f", temperature) + "º");
    }

    //날씨 아이콘 선택
    private void setWeathersIcon(int weather) {
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
        //날씨 아이콘 선택
        int resId = getResources().getIdentifier(weatherIconStringId, "string", getPackageName());
//        Log.d("weather", weatherIconStringId);
        TextView weatherIconText = (TextView) findViewById(R.id.text_weather_icon);
        weatherIconText.setTypeface(Typeface.createFromAsset(getAssets(), "weathericons-regular-webfont.ttf"));
        weatherIconText.setText(getString(resId));
    }

    //메인 포커스 세팅
    public void setMainText() {
        String userName = mDB.getUserName() + "님";
        String mainFocus = "테스트용 메인포커스";
        String greetingMessage = "";
        String mainfocusMessage = "";

        TextView greetingText = (TextView) findViewById(R.id.text_greeting);
        TextView userNameText = (TextView) findViewById(R.id.text_user_name);
        TextView todayText = (TextView) findViewById(R.id.text_today);
        TextView mainfocusText = (TextView) findViewById(R.id.text_mainfocus);


        if (userName != null) {
            //인사말 설정.
            int presentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            //아침
            if (4 <= presentHour && presentHour <= 11) greetingMessage += getString(R.string.greetings_morning_1);
                //오후(점심)
            else if (12 <= presentHour && presentHour <= 18) greetingMessage += getString(R.string.greetings_afternoon_1);
                //저녁
            else greetingMessage += getString(R.string.greetings_evening_1);

            //이름이 있을때만 메인포커스 보이기
            if (mainFocus != null) {    //메인포커스가 있을때 -> today 보임
                todayText.setVisibility(View.VISIBLE);
                mainfocusText.setVisibility(View.VISIBLE);
                // 메인포커스 below text_today, marginTop 15dp TextSize
                RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) mainfocusText.getLayoutParams();
                relativeParams.addRule(RelativeLayout.BELOW, R.id.text_today);
                relativeParams.topMargin = Math.round(0f * getResources().getDisplayMetrics().density); //today와의 topMargin 설정
                mainfocusText.setLayoutParams(relativeParams);
                mainfocusText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                mainfocusMessage += mainFocus;
                //취소선 확인
                mainfocusText.setPaintFlags(mainfocusText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                if(mainFocusBeen.getButton_visibility().equals(String.valueOf(View.VISIBLE))) mainfocusText.setPaintFlags(mainfocusText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                else mainfocusText.setPaintFlags(mainfocusText.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            } else {        //메인포커스가 없을때 -> today 안보임
                todayText.setVisibility(View.INVISIBLE);
                mainfocusText.setVisibility(View.VISIBLE);
                //메인포커스 디자인 수정
                RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) mainfocusText.getLayoutParams();
                relativeParams.addRule(RelativeLayout.BELOW, R.id.text_user_name);
//            relativeParams.topMargin = Math.round(64f * getContext().getResources().getDisplayMetrics().density); //dp설정
                relativeParams.topMargin = translatePxToDp(64f); //dp설정
                mainfocusText.setLayoutParams(relativeParams);
                mainfocusText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                mainfocusMessage += getString(R.string.what_main_schedule);
                mainfocusText.setPaintFlags(mainfocusText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                mainfocusText.setPaintFlags(mainfocusText.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
            }
        } else {
            greetingMessage += "";
            todayText.setVisibility(View.INVISIBLE);
            mainfocusText.setVisibility(View.INVISIBLE);
        }

        //today 부분의 글꼴
        Typeface todayType = Typeface.createFromAsset(getAssets(), "FranklinGothic-Demi.TTF");
        todayText.setTypeface(todayType);

        //Good morning,~ 부분의 글꼴
        Typeface fontType = Typeface.createFromAsset(getAssets(), "FranklinGothic-MediumCond.TTF");
        greetingText.setTypeface(fontType);     //인사말
        userNameText.setTypeface(fontType);     //사용자 이름
        mainfocusText.setTypeface(fontType);    //mainfocus

        greetingText.setText(greetingMessage);
        userNameText.setText(userName);
        mainfocusText.setText(mainfocusMessage);

    }
    //dp로 변환
    private int translatePxToDp(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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
