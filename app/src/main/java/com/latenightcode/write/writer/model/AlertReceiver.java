package com.latenightcode.write.writer.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.latenightcode.write.writer.TextEditorActivity;
import com.latenightcode.write.writer.controller.NotificationHelper;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        intent = new Intent();
        intent.setClass(context, TextEditorActivity.class); //Test is a dummy class name where to redirect
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notificationBuilder = notificationHelper.getNotification();
        notificationHelper.getManager().notify(1, notificationBuilder.build());

    }
}
