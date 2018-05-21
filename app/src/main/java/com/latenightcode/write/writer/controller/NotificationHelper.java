package com.latenightcode.write.writer.controller;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.latenightcode.write.writer.R;

public class NotificationHelper extends ContextWrapper {

    public static final String NOTIFICATION_1_ID = "Every Day Notification";
    public static final String NOTIFICATION_1_NAME = "Every Day Notification";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            createNotificationChannel();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {

        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_1_ID,
                NOTIFICATION_1_NAME,
                NotificationManager.IMPORTANCE_DEFAULT );

        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(R.color.colorPrimary);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(notificationChannel);

    }


    public NotificationManager getManager(){
        if(mManager == null){
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getNotification(){

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        return new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_1_ID)
                .setContentTitle("Hey, You need to write")
                .setContentText("Make this day a history!")
                .setSound(alarmTone)
                .setPriority(1)
                .setSmallIcon(R.drawable.ic_pencil);
    }

}
