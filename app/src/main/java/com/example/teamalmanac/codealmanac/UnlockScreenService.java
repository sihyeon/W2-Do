package com.example.teamalmanac.codealmanac;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class UnlockScreenService extends Service {

    private UnlockScreenReceiver mReceiver = null;

    public UnlockScreenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new UnlockScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver, filter);

//        KeyguardManager.KeyguardLock key;
//        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
//        key = km.newKeyguardLock("IN");
//        key.disableKeyguard();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() == null && mReceiver == null){
            mReceiver = new UnlockScreenReceiver();
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
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
