package com.vigneet.macgray_v010;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by Vigneet on 16-05-2016.
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            Intent setAlarmIntent = new Intent(context,SetAlarm.class);
            PendingIntent pendingSetAlarmIntent = PendingIntent.getBroadcast(
                    context, 4441, setAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY ,pendingSetAlarmIntent);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(System.currentTimeMillis());
            calendar2.set(Calendar.HOUR_OF_DAY, 18);
            Intent cancelAlarmIntent = new Intent(context,CancelAlarm.class);
            PendingIntent pendingCancelAlarmIntent = PendingIntent.getBroadcast(
                    context, 4442, cancelAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),AlarmManager.INTERVAL_DAY ,pendingCancelAlarmIntent);


        }
    }
}
