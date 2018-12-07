package com.pax.tk.annapp.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.os.BuildCompat;
import android.util.Log;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NotificationStorage {

    private static final String TAG = NotificationStorage.class.getSimpleName();
    private static final String ALARM_PREFERENCES_NAME = "alarm_preferences";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private SharedPreferences mSharedPreferences;

    public NotificationStorage(Context context) {
        Context storageContext;
        if (BuildCompat.isAtLeastN()) {
            // All N devices have split storage areas, but we may need to
            // move the existing preferences to the new device protected
            // storage area, which is where the data lives from now on.
            final Context deviceContext = context.createDeviceProtectedStorageContext();
            if (!deviceContext.moveSharedPreferencesFrom(context,
                    ALARM_PREFERENCES_NAME)) {
                Log.w(TAG, "Failed to migrate shared preferences.");
            }
            storageContext = deviceContext;
        } else {
            storageContext = context;
        }
        mSharedPreferences = storageContext
                .getSharedPreferences(ALARM_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public Notification saveNotification(Notification notification) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(String.valueOf(notification.getID()), notification.toJson());
        editor.apply();
        return notification;
    }

    /**
     * Retrieves the alarms stored in the SharedPreferences.
     * This method takes linear time as the alarms count.
     *
     * @return a {@link Set} of alarms.
     */
    public Set<Notification> getNotifications() {
        Set<Notification> alarms = new HashSet<>();
        for (Map.Entry<String, ?> entry : mSharedPreferences.getAll().entrySet()) {
            alarms.add(Notification.fromJson(entry.getValue().toString()));
        }
        return alarms;
    }

    /**
     * Delete the alarm instance passed as an argument from the SharedPreferences.
     * This method iterates through the alarms stored in the SharedPreferences, takes linear time
     * as the alarms count.
     *
     * @param id the notification id to be deleted
     */
    public void deleteNotification(int id) {
        for (Map.Entry<String, ?> entry : mSharedPreferences.getAll().entrySet()) {
            Notification notification = Notification.fromJson(entry.getValue().toString());
            if (notification.getID() == id) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.remove(String.valueOf(notification.getID()));
                editor.apply();
                return;
            }
        }
    }
}