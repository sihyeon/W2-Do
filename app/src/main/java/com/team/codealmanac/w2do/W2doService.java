package com.team.codealmanac.w2do;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class W2DoService extends Service{
    private W2DoBroadcastReceiver mReceiver = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new W2DoBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() == null && mReceiver == null){
            mReceiver = new W2DoBroadcastReceiver();
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);      //화면 켜질때 이벤트
            filter.addAction(Intent.ACTION_TIME_TICK);      //매분마다 발생 이벤트
            filter.addAction(Intent.ACTION_DATE_CHANGED);   //날짜 변경마다 발생 이벤트
//            filter.addAction(Intent.ACTION_BOOT_COMPLETED); //아침 알림 서비스 실행 이벤트
            registerReceiver(mReceiver, filter);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }
}
