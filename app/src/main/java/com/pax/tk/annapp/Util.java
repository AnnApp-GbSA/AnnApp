package com.pax.tk.annapp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

//Utility Class
public class Util {

    //fastest way to round a float to a certain scale
    public static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return ((float) ((int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp))) / pow;
    }

    public static String getDateString(Calendar date, Context context) {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) != date.get(Calendar.YEAR))
            return new SimpleDateFormat(context.getString(R.string.dateFormat_long)).format(date.getTime());
        else if (now.get(Calendar.DAY_OF_YEAR) + 7 < date.get(Calendar.DAY_OF_YEAR))
            return new SimpleDateFormat(context.getString(R.string.dateFormat_short)).format(date.getTime());
        return getWeekDayShort(date, context);
    }

    public static String getWeekDayShort(Calendar date, Context context) {
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return context.getString(R.string.monday_short);
            case Calendar.TUESDAY:
                return context.getString(R.string.tuesday_short);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.wednesday_short);
            case Calendar.THURSDAY:
                return context.getString(R.string.thursday_short);
            case Calendar.FRIDAY:
                return context.getString(R.string.friday_short);
            case Calendar.SATURDAY:
                return context.getString(R.string.saturday_short);
            case Calendar.SUNDAY:
                return context.getString(R.string.sunday_short);
            default:
                return "Error:getWeekDayShort";
        }
    }

    public static int calendarWeekdayByDayIndex(int day) {
        switch (day) {
            case 0:
                return Calendar.MONDAY;
            case 1:
                return Calendar.TUESDAY;
            case 2:
                return Calendar.WEDNESDAY;
            case 3:
                return Calendar.THURSDAY;
            case 4:
                return Calendar.FRIDAY;
            case 5:
                return Calendar.SATURDAY;
            case 6:
                return Calendar.SUNDAY;
            default:
                return -1;
        }
    }

    public static String getFullDate(Calendar calendar) {
        return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
    }

    public static Calendar getCalendarFromFullString(String fullCalendar) {
        if (fullCalendar.matches("\\d*\\.\\d*\\.\\d*")) {
            String[] datest = fullCalendar.split("\\.");
            Calendar ret = Calendar.getInstance();
            ret.set(Integer.valueOf(datest[2]), Integer.valueOf(datest[1]) - 1, Integer.valueOf(datest[0]));
            return ret;
        }
        return null;
    }

    public static void createPushNotificationAtDate(Calendar date, Context context, Notification notification, int notID) {

    }

    public static void createPushNotification(Context context, int ID, String Description, String subject, int smallIcon/*, Bitmap largeIcon*/) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager;
            NotificationCompat.Builder mBuilder;

            /**Creates an explicit intent for an Activity in your app**/
            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                    0 /* Request code */, resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(smallIcon);
            mBuilder.setContentTitle(subject)
                    .setContentText(Description)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Description))
                    .setAutoCancel(false)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setContentIntent(resultPendingIntent);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(String.valueOf(ID), "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert mNotificationManager != null;
                mBuilder.setChannelId(String.valueOf(ID));
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(0 /* Request Code */, mBuilder.build());



        } else {
            NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setSmallIcon(smallIcon)
                    //.setLargeIcon(largeIcon)
                    .setContentTitle(subject)
                    .setContentText(Description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID, notif.build());

        }

    }

    public static int getSubjectColor(Context context, Subject subject) {
        Manager manager = Manager.getInstance();
        int index = manager.getSubjects().indexOf(subject);

        TypedValue a = new TypedValue();

        switch (index) {
            case 0:
                context.getTheme().resolveAttribute(R.attr.subject0, a, true);
                break;
            case 1:
                context.getTheme().resolveAttribute(R.attr.subject1, a, true);
                break;
            case 2:
                context.getTheme().resolveAttribute(R.attr.subject2, a, true);
                break;
            case 3:
                context.getTheme().resolveAttribute(R.attr.subject3, a, true);
                break;
            case 4:
                context.getTheme().resolveAttribute(R.attr.subject4, a, true);
                break;
            case 5:
                context.getTheme().resolveAttribute(R.attr.subject5, a, true);
                break;
            case 6:
                context.getTheme().resolveAttribute(R.attr.subject6, a, true);
                break;
            case 7:
                context.getTheme().resolveAttribute(R.attr.subject7, a, true);
                break;
            case 8:
                context.getTheme().resolveAttribute(R.attr.subject8, a, true);
                break;
            case 9:
                context.getTheme().resolveAttribute(R.attr.subject9, a, true);
                break;
            case 10:
                context.getTheme().resolveAttribute(R.attr.subject10, a, true);
                break;
            case 11:
                context.getTheme().resolveAttribute(R.attr.subject11, a, true);
                break;
            case 12:
                context.getTheme().resolveAttribute(R.attr.subject12, a, true);
                break;
            case 13:
                context.getTheme().resolveAttribute(R.attr.subject13, a, true);
                break;
            case 14:
                context.getTheme().resolveAttribute(R.attr.subject14, a, true);
                break;
        }
        return a.data;
    }

    public static Drawable drawableFromUrl(String url) throws Exception {
        Bitmap x;
        HttpURLConnection connection;

        connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    public Object load(Context context, String key) {
        System.out.println("load---------------- " + key);
        try {
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput(key));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            System.out.println("loading failed ----------------------------------------------------"+" "+ key +" "+"-----------------------------------------------------");
            e.printStackTrace();
            return null;
        }
    }

    public void save(Context context, Object object, String key){
        System.out.println("save---------------- " + key);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(key, MODE_PRIVATE));
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  void createAlertDialog(String title, String text, int ic, Context context) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, MainActivity.colorScheme);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(ic);

        AlertDialog alertDialog = builder.show();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public static void closeKeyboard(LinearLayout layout, Context context){
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
    }
}
