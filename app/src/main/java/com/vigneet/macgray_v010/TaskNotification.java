package com.vigneet.macgray_v010;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Vigneet on 16-05-2016.
 */
public class TaskNotification extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.ic_warning);
        mBuilder.setContentTitle("MacGray Solutions Pvt. Ltd.");
        mBuilder.setContentText("Please complete the task as soon as possible!");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1139, mBuilder.build());
    }
}
