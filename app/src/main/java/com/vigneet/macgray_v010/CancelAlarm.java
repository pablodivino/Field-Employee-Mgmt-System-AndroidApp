package com.vigneet.macgray_v010;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Vigneet on 16-05-2016.
 */
public class CancelAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent locationIntent = new Intent(context,UpdateLocation.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 4444, locationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        if(alarmManager!= null){
            alarmManager.cancel(pendingIntent);
        }

    }
}
