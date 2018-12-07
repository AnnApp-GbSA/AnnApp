package com.pax.tk.annapp.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.os.BuildCompat;
import android.support.v4.os.UserManagerCompat;
import android.util.Log;

import com.pax.tk.annapp.Util;

public class BootCompletedBroadcast extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean bootCompleted;
        String action = intent.getAction();
        Log.i(TAG, "Received action: " + action + ", user unlocked: " + UserManagerCompat
                .isUserUnlocked(context));
        if (BuildCompat.isAtLeastN()) {
            bootCompleted = Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action);
        } else {
            bootCompleted = Intent.ACTION_BOOT_COMPLETED.equals(action);
        }
        if (!bootCompleted) {
            return;
        }

        Util util = new Util();
        NotificationStorage notificationStorage = new NotificationStorage(context);
        for (Notification notification : notificationStorage.getNotifications()) {
            util.setAlarm(context, notification.getEventText(), notification.getSubjectName(), notification.getID(), notification.getDate());
        }
    }
}
