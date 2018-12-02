package com.pax.tk.annapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    private static final String SUBJECTKEY = "subjectName";
    private static final String EVENTKEY = "eventText";

    private Context context;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }


    @NonNull
    @Override
    public Result doWork() {

        String eventText = getInputData().getString(EVENTKEY);
        String subjectName = getInputData().getString(SUBJECTKEY);
        int ID = eventText.hashCode();

        Util.createPushNotification(getApplicationContext(), ID, eventText, subjectName, getApplicationContext().getResources().getIdentifier("anna_logo",
                "mipmap", getApplicationContext().getPackageName()));

        return Result.SUCCESS;
    }
}
