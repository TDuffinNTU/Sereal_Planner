package com.example.sereal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;


// BASED ON CODE FROM https://codinginflow.com/tutorials/android/alarmmanager
// Shows user a notification
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle extras = intent.getExtras();
        String title = extras.getString("title");

        NotificationHelper helper = new NotificationHelper(context);
        NotificationCompat.Builder builder = helper.getChannelNotification();
        builder.setContentTitle(title);
        builder.setContentText("Sereal Routine Reminder");
        helper.getManager().notify(1, builder.build());
    }
}
