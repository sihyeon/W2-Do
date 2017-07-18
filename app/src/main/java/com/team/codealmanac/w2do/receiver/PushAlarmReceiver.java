package com.team.codealmanac.w2do.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.team.codealmanac.w2do.LoginActivity;
import com.team.codealmanac.w2do.R;

import java.util.Calendar;

import static android.content.Context.POWER_SERVICE;

/**
 * Created by sihyeon on 2017-05-27.
 */

public class PushAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentActivity = new Intent(context, LoginActivity.class);
        intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentActivity,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.drawable.icn_logo)
                .setContentTitle("좋은 아침입니다~!")
                .setContentText("오늘의 일정은 무엇인가요?")
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);
        notificationManager.notify( 2, builder.build());
    }
}
