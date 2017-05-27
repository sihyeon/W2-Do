package com.team.codealmanac.w2do;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.health.SystemHealthManager;
import android.util.Log;

public class W2DoBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        switch (intent.getAction()){
            case Intent.ACTION_SCREEN_OFF:
                Intent i = new Intent(context, LockScreenActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case Intent.ACTION_SCREEN_ON:
                break;
            case Intent.ACTION_TIME_TICK:
                break;
            case Intent.ACTION_DATE_CHANGED:
                break;
            default:
        }
    }
}
