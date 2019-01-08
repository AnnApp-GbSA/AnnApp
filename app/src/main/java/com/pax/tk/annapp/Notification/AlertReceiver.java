package com.pax.tk.annapp.Notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.pax.tk.annapp.Util;

public class AlertReceiver extends BroadcastReceiver {

    private static final String SUBJECTKEY = "subjectName";
    private static final String EVENTKEY = "eventText";
    private static final String IDKEY = "ID";

    /**
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String eventText = intent.getStringExtra(EVENTKEY);
        String subjectName = intent.getStringExtra(SUBJECTKEY);
        int ID = intent.getIntExtra(IDKEY, 0);

        Util.createPushNotification(context, ID, eventText, subjectName, context.getResources().getIdentifier("anna_logo",
                "mipmap", context.getPackageName()));
    }



}
