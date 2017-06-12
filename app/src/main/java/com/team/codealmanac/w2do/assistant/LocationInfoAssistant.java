package com.team.codealmanac.w2do.assistant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Choi Jaeung on 2016-12-05.
 */
public class LocationInfoAssistant implements LocationListener {
    private static final int MIN_DISTANCE_CHANGE_FOR_UPDATE = 100;   //최소 10미터마다 업데이트
    private static final int MIN_TIME_FOR_UPDATE = 3000 /** 60*/;       //최소 3초마다 업데이트

    private LocationManager mLocationManager;   //프로바이더 리스너 등등 등록
    private InterfaceLocationInfoManager mInterface;
    private Context mContext;

    public interface InterfaceLocationInfoManager {
        void setLocation(Location location);
    }

    //싱글톤
    private static LocationInfoAssistant ourInstance;
    public static LocationInfoAssistant getInstance() {
        if (ourInstance == null) {
            ourInstance = new LocationInfoAssistant();
        }
        return ourInstance;
    }
    private LocationInfoAssistant() {}

    public void onStartLocation(Context context, AppCompatActivity activity) {
        mInterface = (InterfaceLocationInfoManager) activity;
        mContext = context.getApplicationContext();
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);   //GPS켜져있는 여부 상관없이 센서를 확인하도록 등록
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
        Log.d("location", "onStartLocation 호출");
    }

    public void onStopLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mLocationManager.removeUpdates(this);
        Log.d("location", "onStopLocation 호출");
    }

    @Override
    protected void finalize() throws Throwable {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mLocationManager.removeUpdates(this);
        super.finalize();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("location", "locationChange lat: " + location.getLatitude() + ", lon: " + location.getLongitude());
        mInterface.setLocation(location);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        mLocationManager.removeUpdates(this);
    }
    //지역을 불러올 수 없을때나. 또는 불가능했던 지역이 불러올 수 있게 되었을 때 불리는 듯 함
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    //GPS 센서를 사용이 가능할 때 불림
    @Override
    public void onProviderEnabled(String provider) {}
    //GPS 센서를 사용이 불가능해질때 불림.
    @Override
    public void onProviderDisabled(String provider) {}
}
