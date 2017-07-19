package com.team.codealmanac.w2do.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.team.codealmanac.w2do.R;
import com.team.codealmanac.w2do.receiver.W2DoBroadCastReceiver;

/**
 * 이 서비스의 기능
 *  - LockScreenActivity 띄움
 *
 */
public class W2DoForeGroundService extends Service{
    private final String TAG = W2DoForeGroundService.class.getSimpleName();
    private final int FOREGROUNDID = 1000;
    private W2DoBroadCastReceiver mReceiver = null;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new W2DoBroadCastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_TIME_TICK);      //매분마다 발생 이벤트
        filter.addAction(Intent.ACTION_DATE_CHANGED);   //날짜 변경마다 발생 이벤트
        registerReceiver(mReceiver, filter);
        startForeGroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if(intent != null && intent.getAction() == null && mReceiver == null){
//            mReceiver = new W2DoBroadCastReceiver();
//            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//            filter.addAction(Intent.ACTION_SCREEN_ON);      //화면 켜질때 이벤트
////            filter.addAction(Intent.ACTION_TIME_TICK);      //매분마다 발생 이벤트
//            filter.addAction(Intent.ACTION_DATE_CHANGED);   //날짜 변경마다 발생 이벤트
//            registerReceiver(mReceiver, filter);
//        }
        return START_REDELIVER_INTENT;
    }

    public void startForeGroundService(){
        try{
            startForeground(FOREGROUNDID, getNotification());

        } catch (Exception e){
            Log.e(TAG, "startForeGroundService error: " + e);
        }
    }

    public Notification getNotification() throws Exception{
        int smallIcon = R.mipmap.launcher;
        Notification notification =  new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(smallIcon)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setAutoCancel(true)
                .setWhen(0)
                .setTicker("").build();
        notification.flags = 16;
        return  notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
        stopForeground(true);
    }
}