package com.team.codealmanac.w2do.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.health.SystemHealthManager;
import android.util.Log;

import com.team.codealmanac.w2do.LockScreenActivity;
import com.team.codealmanac.w2do.MainActivity;
import com.team.codealmanac.w2do.database.PreferencesManager;

public class W2DoBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        switch (intent.getAction()){
            case Intent.ACTION_SCREEN_OFF:
                Intent i = new Intent(context, LockScreenActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
//            case Intent.ACTION_SCREEN_ON:
//                break;
            case Intent.ACTION_TIME_TICK:
                break;

            // TODO: 2017-07-18 사진 수 만큼 조건 바꿔줘야함 또는 따로 변수 빼던지 해야함.
            case Intent.ACTION_DATE_CHANGED:
                PreferencesManager preferencesManager = new PreferencesManager(context);
                int sequence = preferencesManager.getBackGroundSquence();
                sequence = (sequence == 2 ? 1 : sequence + 1);
                preferencesManager.setBackGroundSequence(sequence);
                if(MainActivity.mContext != null) ((MainActivity)MainActivity.mContext).setBackGround();
                break;
            default:
        }
    }
}