package com.allever.lose.weight.reciver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.allever.lose.weight.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Mac on 2018/3/7.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final int NOTIFICATIONS_ID = 10000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "time out");
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent actionIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, actionIntent, 0);

        //高版本api
//        Notification notification = new Notification.Builder(context)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle(context.getResources().getString(R.string.app_name))
//                .setContentText(context.getResources().getString(R.string.time_to_action))
//                .setContentIntent(contentIntent)
//                .build();// getNotification()
//
//        notification.defaults |= Notification.DEFAULT_SOUND;
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notification.defaults |= Notification.DEFAULT_LIGHTS;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getResources().getString(R.string.time_to_action))
                .setContentIntent(contentIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        mNotifyMgr.notify(NOTIFICATIONS_ID, builder.build());
    }
}
