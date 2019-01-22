package com.pax.tk.annapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pax.tk.annapp.Notification.AlertReceiver;
import com.pax.tk.annapp.Notification.Notification;
import com.pax.tk.annapp.Notification.NotificationStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

//Utility Class
public class Util {

    private static final String SUBJECTKEY = "subjectName";
    private static final String EVENTKEY = "eventText";
    private static final String IDKEY = "ID";
    private static final int STORAGE_PERMISSION_CODE = 2015;

    /**
     * fastest way to round a float to a certain scale
     *
     * @param number float to be round
     * @param scale scale to which the number shall be round
     * @return round number
     */
    public static float round(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return ((float) ((int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp))) / pow;
    }

    /**
     * converts a date into a String with format dd.MM.yyyy
     *
     * @param date date to convert
     * @param context ...
     * @return date as String
     */
    public static String getDateString(Calendar date, Context context) {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.YEAR) != date.get(Calendar.YEAR))
            return new SimpleDateFormat(context.getString(R.string.dateFormat_long)).format(date.getTime());
        else if (now.get(Calendar.DAY_OF_YEAR) + 7 < date.get(Calendar.DAY_OF_YEAR))
            return new SimpleDateFormat(context.getString(R.string.dateFormat_short)).format(date.getTime());
        return getWeekDayShort(date, context);
    }

    /**
     * converts a date into a weekDayShort like "MO" for Monday
     *
     * @param date date to convert
     * @param context ...
     * @return weekDayShort as String
     */
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

    /**
     * converts a day number into a weekday
     *
     * @param day day number to convert
     * @return weekday
     */
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

    /**
     * converts a date into the format dd.MM.yyyy
     *
     * @param calendar date to convert
     * @return date converted
     */
    public static String getFullDate(Calendar calendar) {
        return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
    }

    /**
     * converts a String into a date
     *
     * @param fullCalendar String to convert
     * @return String converted
     */
    public static Calendar getCalendarFromFullString(String fullCalendar) {
        if (fullCalendar.matches("\\d*\\.\\d*\\.\\d*")) {
            String[] datest = fullCalendar.split("\\.");
            Calendar ret = Calendar.getInstance();
            ret.set(Integer.valueOf(datest[2]), Integer.valueOf(datest[1]) - 1, Integer.valueOf(datest[0]));
            return ret;
        }
        return null;
    }

    /**
     * gets the color of a subject
     *
     * @param context ...
     * @param subject subject
     * @return color as int
     */
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

    /**
     * gets the background color
     *
     * @param context ...
     * @return background color as int
     */
    public static  int getBackgroundColor(Context context){
        TypedValue a = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorBackground, a, true);
        return a.data;
    }


    /**
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static Drawable drawableFromUrl(String url) throws Exception {
        Bitmap x;
        HttpURLConnection connection;

        connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    /**
     * loads the saved elements
     *
     * @param context ...
     * @param key key of an element
     * @return saved element
     */
    public Object load(Context context, String key) {
        System.out.println("load---------------- " + key);
        try {
            ObjectInputStream ois = new ObjectInputStream(context.openFileInput(key));
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (Exception e) {
            System.out.println("loading failed ----------------------------------------------------" + " " + key + " " + "-----------------------------------------------------");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * saves elements
     *
     * @param context ...
     * @param object object to save
     * @param key key of the object
     */
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

    /**
     * creates an alert dialog
     *
     * @param title title of the dialog
     * @param text text of the dialog
     * @param ic alert icon
     * @param context ...
     */
    public static void createAlertDialog(String title, String text, int ic, Context context) {
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

    /**
     * closes the keyboard
     *
     * @param layout layout in which the keyboard is
     * @param context ...
     */
    public static void closeKeyboard(LinearLayout layout, Context context){
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
    }

    /**
     * creates a random number between two numbers
     *
     * @param rangeMin smallest possible number
     * @param rangeMax biggest possible number
     * @return random number between rangeMin and rangeMax
     */
    public static int randomNumberGenerator(int rangeMin, int rangeMax) {
        Random r = new Random();
        int createdRanNum = (int) Math.round(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
        return (createdRanNum);
    }

    /**
     * sets an alarm
     *
     * @param context ...
     * @param eventText text of the alarm
     * @param subjectName name of the subject of the alarm
     * @param id id of the alarm
     * @param notidate date at which the notification shall be
     */
    public static void setAlarm(Context context, String eventText, String subjectName, int id, Calendar notidate)
    {
        int notiTime = ((MainActivity)context).getPreferences(MODE_PRIVATE).getInt(context.getString(R.string.key_notificationTime), 480);
        int hourofDay = (int) Math.floor(notiTime / 60);

        notidate.set(Calendar.HOUR_OF_DAY, hourofDay);
        notidate.set(Calendar.MINUTE, notiTime % 60);
        notidate.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent inte = new Intent(context, AlertReceiver.class);
        inte.putExtra(EVENTKEY, eventText)
                .putExtra(SUBJECTKEY, subjectName)
                .putExtra(IDKEY, id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, inte, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notidate.getTimeInMillis(), pendingIntent);
    }

    /**
     * cancels an alarm
     *
     * @param context ...
     * @param eventText text of the alarm
     * @param subjectName name of the subject of the alarm
     * @param id id of the alarm
     */
    public static void cancelAlarm(Context context, String eventText, String subjectName, int id)
    {
        Manager manager = Manager.getInstance();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent inte = new Intent(context, AlertReceiver.class);
        inte.putExtra("eventText", eventText)
                .putExtra("subjectName", subjectName)
                .putExtra("ID", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, inte, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    /**
     * creates a push notification
     *
     * @param context ...
     * @param ID id of the notification
     * @param Description description of the notification
     * @param subject subject in which the notification shall be
     * @param smallIcon icon which will be shown with the notification
     */
    public static void createPushNotification(Context context, int ID, String Description, String subject, int smallIcon/*, Bitmap largeIcon*/) {

        final String CHANNEL_1_ID = "channel_1";

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

            mBuilder = new NotificationCompat.Builder(context, CHANNEL_1_ID);
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
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_1_ID, context.getString(R.string.annapp_notification_channel), importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.YELLOW);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert mNotificationManager != null;
                mBuilder.setChannelId(String.valueOf(CHANNEL_1_ID));
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify(ID /* Request Code */, mBuilder.build());


        } else {
            NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setSmallIcon(smallIcon)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    //.setLargeIcon(largeIcon)
                    .setContentTitle(subject)
                    .setContentText(Description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(ID, notif.build());

        }

        (new NotificationStorage(context)).deleteNotification(ID);
    }

    /**
     * creates an input dialog, sets it up and calls methods
     */
    public void createInputDialog(Context context, Activity activity, RecyclerView recyclerView, Integer position) {

        //AlertDialog.Builder ad = new  AlertDialog.Builder(this.context);
        final BottomSheetDialog bsd = new BottomSheetDialog(context, R.style.NewDialog);

        Manager manager = Manager.getInstance();


        View mView = View.inflate(context, R.layout.dialog_new_grade, null);
        //mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        final EditText gradeInput = (EditText) mView.findViewById(R.id.gradeInput);
        final EditText ratingInput = (EditText) mView.findViewById(R.id.ratingInput);
        final EditText note = (EditText) mView.findViewById(R.id.note);
        final ImageView btnHelp = (ImageView) mView.findViewById(R.id.btnRoomHelp);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);
        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extraLayout.getVisibility() != View.VISIBLE)
                    extraLayout.setVisibility(View.VISIBLE);
                else
                    extraLayout.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });


        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.createAlertDialog(context.getString(R.string.rating), context.getString(R.string.ratingExplanation), 0, context);
            }
        });


        final Spinner subjectSelection = (Spinner) mView.findViewById(R.id.subjectSelection);

        subjectSelection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Util.closeKeyboard(mView.findViewById(R.id.headerLayout), context);
                return false;
            }
        });

        Context wrappedContext = new ContextThemeWrapper(context, R.style.BasicTheme);
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(wrappedContext, R.layout.white_spinner_item, manager.getSubjects());

        adapter.setDropDownViewTheme(context.getTheme());

        subjectSelection.setAdapter(adapter);

        if (position != null)
            subjectSelection.setSelection(position);


        final RadioButton isWritten = mView.findViewById(R.id.isWritten);

        final RadioButton isNotWritten = mView.findViewById(R.id.isNotWritten);


        bsd.setTitle(R.string.addGrade);

        final Boolean[] isWrittenBool = new Boolean[1];

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = 1;

                //testing which button is active for decision whether your Grade is written or whether it's not
                if (isWritten.isChecked())
                    isWrittenBool[0] = true;
                else if (isNotWritten.isChecked())
                    isWrittenBool[0] = false;

                if (gradeInput.getText().toString().isEmpty()) {
                    Util.createAlertDialog(context.getString(R.string.warning), context.getString(R.string.warningMessage), 0, context);
                    return;
                }

                if (!ratingInput.getText().toString().isEmpty())
                    rating = Float.parseFloat(ratingInput.getText().toString());


                Subject subject = (Subject) subjectSelection.getSelectedItem();
                Grade newGrade = new Grade(subject, Integer.valueOf(gradeInput.getText().toString()), isWrittenBool[0], rating, note.getText().toString());
                subject.addGrade(newGrade);
                recyclerView.getAdapter().notifyItemChanged(manager.getSubjects().indexOf(subject));
                if (position == null)
                    ((TextView) activity.findViewById(R.id.grade)).setText(String.valueOf(manager.getWholeGradeAverage()));
                else
                    ((TextView) activity.findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
                //((RVAdapterSubjectList)recyclerView.getAdapter()).addGrade(newGrade);
                bsd.cancel();
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

    /**
     * checks a permission
     *
     * @param context ...
     * @param permission permission to check
     */
    public static void checkPermission(Context context, String permission){
        if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
            askforPermission((Activity) context, permission);
        }
    }

    /**
     * asks for a permission
     *
     * @param activity activity
     * @param permission permission to ask for
     */
    private static void askforPermission(Activity activity, String permission){
        //if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        //optional Dialog to explain why the permission is necessary
        //}
        ActivityCompat.requestPermissions(activity, new String[]{permission}, STORAGE_PERMISSION_CODE);
    }

    /**
     * checks the StoragePermission
     *
     * @param context ...
     */
    public static void checkStoragePermissions(Context context){
        Util.checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        Util.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * refreshs the notification time
     *
     * @param hourofday hour of the day
     * @param minute minute of the hour
     * @param context ...
     */
    public static void refreshNotificationTime(int hourofday, int minute, Context context){

        NotificationStorage notificationStorage = new NotificationStorage(context);

        for (Notification notification : notificationStorage.getNotifications()) {

            Calendar notidate = notification.getDate();
            notidate.set(Calendar.HOUR_OF_DAY, hourofday);
            notidate.set(Calendar.MINUTE, minute);

            cancelAlarm(context, notification.getEventText(), notification.getSubjectName(), notification.getID());
            setAlarm(context, notification.getEventText(), notification.getSubjectName(), notification.getID(), notidate);
        }


    }

    /**
     * cancels all alarms
     *
     * @param context ...
     */
    public static void cancelAllAlarms(Context context){
        NotificationStorage notificationStorage = new NotificationStorage(context);

        for (Notification notification : notificationStorage.getNotifications()) {
            cancelAlarm(context, notification.getEventText(), notification.getSubjectName(), notification.getID());
        }

    }

    public static int occurancesOfCharInString(String input, char regex) {
        char[] chars = input.toCharArray();

        int count = 0;

        for (char character :
                chars) {
            if (character == regex) {
                count++;
            }
        }

        //System.out.println("'" + regex + "' " + count + " times found in: " + input);

        return count;
    }

    public static String cutEdgingSpaces(String string){
        while (string.startsWith(" ")){
            string = string.substring(1);
        }

        while (string.endsWith(" ")){
            string = string.substring(0, string.length()-1);
        }
        return string;
    }

    public static void createSnackbar(Context context, String text, int duration, int textColor){
        Snackbar mySnackbar = Snackbar.make(((Activity) context).getCurrentFocus(), text, duration);
        View snackbarView = mySnackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(textColor);
        mySnackbar.show();
    }
}
