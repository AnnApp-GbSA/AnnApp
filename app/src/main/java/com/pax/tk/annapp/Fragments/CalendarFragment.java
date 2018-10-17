package com.pax.tk.annapp.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.regex.Pattern;

import com.pax.tk.annapp.CustomEvent;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Util;

/**
 * Created by Tobi on 20.09.2017.
 */

@TargetApi(Build.VERSION_CODES.N)
public class CalendarFragment extends Fragment {
    public static final String TAG = "CalendarFragment";
    private static final String FILE_NAME = "events.ics";
    CompactCalendarView compactCalendarView;
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<Event> ownEvents = new ArrayList<>();
    ArrayList<Event> eventsThisDay = new ArrayList<>();
    long clicked = System.currentTimeMillis();
    String urlLink = "https://calendar.google.com/calendar/ical/o5bthi1gtvamdjhed61rot1e74%40group.calendar.google.com/public/basic.ics";
    String urlLink2 = "https://calendar.google.com/calendar/embed?src=o5bthi1gtvamdjhed61rot1e74@group.calendar.google.com&ctz=Europe/Berlin&pli=1";
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());
    int e = 0;

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.calendar));
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        ArrayList<CustomEvent> eventsPuffer = (ArrayList<CustomEvent>) (new Util()).load(getContext(), "events");
        if (eventsPuffer == null)
            eventsPuffer = new ArrayList<>();
        for (CustomEvent ce :
                eventsPuffer) {
            if (!events.contains(ce)) {
                events.add(new Event(ce.getColor(), ce.getTimeInMillis(), ce.getData()));
            }
        }

        ArrayList<CustomEvent> ownEventsPuffer = (ArrayList<CustomEvent>) (new Util()).load(getContext(), "ownEvents");
        if (ownEventsPuffer == null)
            ownEventsPuffer = new ArrayList<>();
        for (CustomEvent ce :
                ownEventsPuffer) {
            if (!ownEvents.contains(ce)) {
                ownEvents.add(new Event(ce.getColor(), ce.getTimeInMillis(), ce.getData()));
            }
        }

        //TODO reactivate ic sync with calendar is working
        //getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.VISIBLE);

        ((ImageButton) getActivity().findViewById(R.id.syncWithCalendar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSyncWithCalendar();
            }
        });

        TextView monthIndication = root.findViewById(R.id.monthIndication);
        TextView dateInformation = root.findViewById(R.id.dateInformation);
        ImageButton monthBack = root.findViewById(R.id.monthBack);
        ImageButton monthForward = root.findViewById(R.id.monthForward);
        Button ea = root.findViewById(R.id.e);
        ea.setClickable(false);
        FloatingActionButton fabAdd = root.findViewById(R.id.fabAddCalendar);

        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.addEvents(events);
        /*for (Event e :
                events) {
            System.out.println((StringInDate(MillisInDate(e.getTimeInMillis(), false, true, true)))[0] < (StringInDate(MillisInDate(getEndTimeMillis(e), false, true, true)))[0]);
            if(StringInDate(MillisInDate(e.getTimeInMillis(), false, true, true))[0] < StringInDate(MillisInDate(getEndTimeMillis(e), false, true,true))[0] &&
                    StringInDate(MillisInDate(e.getTimeInMillis(), false, true,true))[1] <= StringInDate(MillisInDate(getEndTimeMillis(e), false, true, true))[1] ||
                    StringInDate(MillisInDate(e.getTimeInMillis(), false, true, true))[1] < StringInDate(MillisInDate(getEndTimeMillis(e), false, true, true))[1]){
                System.out.println("hässlichste if der welt erreicht!");
                long oneDay = 86400000L;
                long day = e.getTimeInMillis() - e.getTimeInMillis()%oneDay;
                long endDay = getEndTimeMillis(e) - getEndTimeMillis(e)%oneDay;
                for (int days = 1; day+(days*oneDay)<= endDay; days++){
                    compactCalendarView.addEvent(new Event(e.getColor(),e.getTimeInMillis()+(days*oneDay)));
                }
            }
        }*/

        eventsThisDay.clear();

        new getCalendarData().execute((Void) null);

        monthIndication.setText(getMonth(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + getYear(compactCalendarView.getFirstDayOfCurrentMonth()));
        dateInformation.setText(MillisInDate(System.currentTimeMillis(), false, true, false));
        dateClicked(System.currentTimeMillis());

        monthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollLeft();
                eventsThisDay.clear();
                clicked = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
                dateInformation.setText(MillisInDate(compactCalendarView.getFirstDayOfCurrentMonth().getTime(), false, true, false));
                dateClicked(compactCalendarView.getFirstDayOfCurrentMonth());
            }
        });

        monthForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollRight();
                eventsThisDay.clear();
                clicked = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
                dateInformation.setText(MillisInDate(compactCalendarView.getFirstDayOfCurrentMonth().getTime(), false, true, false));
                dateClicked(compactCalendarView.getFirstDayOfCurrentMonth());
                if (e < 3)
                    e++;
                else e = 0;
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                clicked = dateClicked.getTime();
                eventsThisDay.clear();
                dateInformation.setText(MillisInDate(dateClicked.getTime(), false, true, false));
                dateClicked(dateClicked);
                if (e == 3) {
                    if (clicked == 1536789600000L) {
                        ea.setVisibility(View.VISIBLE);
                        ea.setClickable(true);
                        ea.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("Easteregg gefunden!!");
                                ea.setVisibility(View.GONE);
                                ea.setClickable(false);
                            }
                        });
                    } else
                        e = 0;
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                clicked = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
                eventsThisDay.clear();

                monthIndication.setText(getMonth(compactCalendarView.getFirstDayOfCurrentMonth()) + " - " + getYear(compactCalendarView.getFirstDayOfCurrentMonth()));

                //dateInformation.setText(MillisInDate(compactCalendarView.getFirstDayOfCurrentMonth().getTime(), false, true, false));

                dateInformation.setText(MillisInDate(clicked, false, true, false));


                dateClicked(compactCalendarView.getFirstDayOfCurrentMonth());

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createInputDialog();
            }
        });

        return root;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    onSyncWithCalendar();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    try {
                        finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
        }
    }

    private void saveFile(String filename, String content) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openFile(String directory) {

    }

    private String eventToICS(String uid, String summary, String location, String startDate, String startTime, String endDate, String endTime) {
        String ics;
        if (startTime.equals("1") || endTime.equals("1")) {
            ics = new String("BEGIN:VEVENT\nDTSTART:" + startDate + "T" + startTime + "00Z\nDTEND:" + endDate + "T" + endTime + "00Z\nDTSTAMP:\nUID:" + uid + "\nCREATED:\nDESCRIPTION:\nLAST-MODIFIED:\nLOCATION:" + location + "\nSEQUENCE:0\nSTATUS:CONFIRMED\nSUMMARY:" + summary + "\nTRANSP:OPAQUE\nEND:VEVENT\n");
            return ics;
        }
        ics = new String("BEGIN:VEVENT\nDTSTART:" + startDate + "T" + startTime + "00Z\nDTEND:" + endDate + "T" + endTime + "00Z\nDTSTAMP:\nUID:" + uid + "\nCREATED:\nDESCRIPTION:\nLAST-MODIFIED:\nLOCATION:" + location + "\nSEQUENCE:0\nSTATUS:CONFIRMED\nSUMMARY:" + summary + "\nTRANSP:OPAQUE\nEND:VEVENT\n");
        return ics;
    }

    public void onSyncWithCalendar() {
        checkPermission();


        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 9, 14, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "Jazzercise");
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Berlin");
        //Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        //long eventID = Long.parseLong(uri.getLastPathSegment());
//
// ... do something with event ID
//
//

        /*for (Event e : events) {
            if (e.getTimeInMillis() > System.currentTimeMillis() - 31536000000L){

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(2012, 0, 19, 7, 30);
            Calendar endTime = Calendar.getInstance();
            endTime.set(2012, 0, 19, 8, 30);
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getStartDate(e))
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getEndDate(e))
                    .putExtra(CalendarContract.Events.TITLE, getSummary(e))
                    .putExtra(CalendarContract.Events.DESCRIPTION, getSummary(e))
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, getLocation(e))
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(CalendarContract.Events.EVENT_COLOR, e.getColor());
            startActivity(intent);}
        }*/
/*
        String content = new String("BEGIN:VCALENDAR\nPRODID:AnnApp\nVERSION:2.0\nCALSCALE:GREGORIAN\nMETHOD:PUBLISH\nX-WR-CALNAME:AnnApp\nX-WR-TIMEZONE:Europe/Berlin\nX-WR-CALDESC:\n");
        for (Event ev :
                events) {
            if (ev.getTimeInMillis() > System.currentTimeMillis() - 31536000000L) {
                content = content + eventToICS(getUID(ev), getSummary(ev), getLocation(ev), getStartDate(ev), getStartTime(ev), getEndDate(ev), getEndTime(ev));
            }
        }
        content = content + "END:VCALENDAR";
        saveFile(FILE_NAME, content);
        openFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FILE_NAME);*/
    }

    private String getSummary(Event ev) {
        String[] split = ev.getData().toString().split(Pattern.quote("°°"));
        String summary = split[2];
        return summary;
    }

    private String getLocation(Event ev) {
        String[] split = ev.getData().toString().split(Pattern.quote("°°"));
        String location = split[1];
        return location;
    }

    private String getStartDate(Event ev) {
        String day = new java.text.SimpleDateFormat("dd").format(new java.util.Date(ev.getTimeInMillis()));
        String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date(ev.getTimeInMillis()));
        String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(ev.getTimeInMillis()));
        return year + month + day;
    }

    private String getStartTime(Event ev) {
        String hour = new java.text.SimpleDateFormat("hh").format(new java.util.Date(ev.getTimeInMillis()));
        String minute = new java.text.SimpleDateFormat("mm").format(new java.util.Date(ev.getTimeInMillis()));
        return hour + minute;
    }

    private String getEndDate(Event ev) {
        String[] split = ev.getData().toString().split(Pattern.quote("°°"));
        String endDate = split[0];
        if (!endDate.equals("")) {
            String day = new java.text.SimpleDateFormat("dd").format(new java.util.Date(Long.valueOf(endDate)));
            String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date(Long.valueOf(endDate)));
            String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(Long.valueOf(endDate)));
            return year + month + day;
        }
        return "1";
    }

    private String getEndTime(Event ev) {
        String[] split = ev.getData().toString().split(Pattern.quote("°°"));
        String endTime = split[0];
        if (!endTime.equals("")) {
            String hour = new java.text.SimpleDateFormat("hh").format(new java.util.Date(Long.valueOf(endTime)));
            String minute = new java.text.SimpleDateFormat("mm").format(new java.util.Date(Long.valueOf(endTime)));
            return hour + minute;
        }
        return "1";
    }

    private Long getEndTimeMillis(Event event) {
        String[] split = event.getData().toString().split(Pattern.quote("°°"));
        String endTime = split[0];
        try {
            Long timeMillis = Long.valueOf(endTime);
            return timeMillis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void createInputDialog() {
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.NewDialog);

        View mView = View.inflate(this.getContext(), R.layout.dialog_new_event, null);

        final EditText eventInput = (EditText) mView.findViewById(R.id.eventInput);
        final Button btnStartDateInput = (Button) mView.findViewById(R.id.startDateInput);
        final Button btnEndDateInput = (Button) mView.findViewById(R.id.endDateInput);
        final EditText locationInput = (EditText) mView.findViewById(R.id.locationInput);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);
        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        btnStartDateInput.setText(MillisInDate(clicked, false, true, false));
        Date start = new Date(clicked);
        Date end = new Date(clicked);
        final boolean[] change = {false};

        DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String hour = new java.text.SimpleDateFormat("hh").format(new java.util.Date(clicked));
                String minute = new java.text.SimpleDateFormat("mm").format(new java.util.Date(clicked));
                start.setYear(year - 1900);
                start.setMonth(month);
                start.setDate(dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), R.style.TimePickerTheme, onStartTimeSetListener, Integer.valueOf(hour), Integer.valueOf(minute), true);
                timePickerDialog.setTitle("Uhrzeit auswählen");
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.show();
            }

            TimePickerDialog.OnTimeSetListener onStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    start.setHours(hourOfDay);
                    start.setMinutes(minute);
                    btnStartDateInput.setText(MillisInDate(start.getTime(), true, true, false));
                }
            };
        };

        DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String hour = new java.text.SimpleDateFormat("hh").format(start);
                String minute = new java.text.SimpleDateFormat("mm").format(start);
                end.setYear(year - 1900);
                end.setMonth(month);
                end.setDate(dayOfMonth);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), R.style.TimePickerTheme, onEndTimeSetListener, Integer.valueOf(hour), Integer.valueOf(minute), true);
                timePickerDialog.setTitle("Uhrzeit auswählen");
                timePickerDialog.setCanceledOnTouchOutside(false);
                timePickerDialog.show();
            }

            TimePickerDialog.OnTimeSetListener onEndTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    end.setHours(hourOfDay);
                    end.setMinutes(minute);
                    btnEndDateInput.setText(MillisInDate(end.getTime(), true, true, false));
                    change[0] = true;
                }
            };
        };

        btnStartDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = new java.text.SimpleDateFormat("dd").format(new java.util.Date(clicked));
                String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date(clicked));
                String year = new java.text.SimpleDateFormat("yyyy").format(new java.util.Date(clicked));
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, onStartDateSetListener, Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
                datePickerDialog.setTitle(getString(R.string.chooseDate));
                datePickerDialog.setCanceledOnTouchOutside(false);
                datePickerDialog.show();
            }
        });

        btnEndDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = new java.text.SimpleDateFormat("dd").format(start);
                String month = new java.text.SimpleDateFormat("MM").format(start);
                String year = new java.text.SimpleDateFormat("yyyy").format(start);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), R.style.TimePickerTheme, onEndDateSetListener, Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
                datePickerDialog.setTitle(getString(R.string.chooseDate));
                datePickerDialog.setCanceledOnTouchOutside(false);
                datePickerDialog.show();
            }
        });

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

        bsd.setTitle(R.string.addEvent);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (eventInput.getText().toString().isEmpty()) {
                    createAlertDialog(getString(R.string.warning), getString(R.string.warningMessage), 0);
                    return;
                }

                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                String uid = String.valueOf(eventInput.getText().toString().hashCode());
                Event ev1;
                if (change[0] == false) {
                    ev1 = new Event(color, start.getTime(), "" + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid);
                } else {
                    ev1 = new Event(color, start.getTime(), end.getTime() + "°°" + locationInput.getText().toString() + "°°" + eventInput.getText().toString() + "°°" + uid);
                }
                compactCalendarView.addEvent(ev1);
                events.add(ev1);
                ownEvents.add(ev1);
                save(ownEvents, true);
                save(events, false);
                refresh();
                dateClicked(clicked);

                bsd.cancel();
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

    void createAlertDialog(String title, String text, int ic) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getContext(), android.R.style.Theme_Material_Light_Dialog);
        } else {
            builder = new AlertDialog.Builder(this.getContext());
        }
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(ic)
                .show();
    }

    private int[] StringInDate(String date) {
        String[] split = date.split(". ");
        int[] ints = new int[2];
        ints[0] = Integer.valueOf(split[0]);
        ints[1] = Integer.valueOf(split[1]);
        return ints;
    }

    private String MillisInDate(long time, boolean withTime, boolean withDate, boolean monthAsNumber) {
        String day = new java.text.SimpleDateFormat("dd").format(new java.util.Date(time));
        String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date(time));
        String hour = new java.text.SimpleDateFormat("kk").format(new java.util.Date(time));
        String minute = new java.text.SimpleDateFormat("mm").format(new java.util.Date(time));

        if (monthAsNumber && withDate && !withTime) {
            return day + ". " + month;
        }

        if (month.equals("01")) {
            month = "Januar";
        } else if (month.equals("02")) {
            month = "Februar";
        } else if (month.equals("03")) {
            month = "März";
        } else if (month.equals("04")) {
            month = "April";
        } else if (month.equals("05")) {
            month = "Mai";
        } else if (month.equals("06")) {
            month = "Juni";
        } else if (month.equals("07")) {
            month = "Juli";
        } else if (month.equals("08")) {
            month = "August";
        } else if (month.equals("09")) {
            month = "September";
        } else if (month.equals("10")) {
            month = "Oktober";
        } else if (month.equals("11")) {
            month = "November";
        } else if (month.equals("12")) {
            month = "Dezember";
        } else {
            System.out.println("Fehler bei MillisInDate()");
        }
        if (withTime && withDate) {
            return day + ". " + month + "  " + hour + ":" + minute;
        }
        if (withTime) {
            return hour + ":" + minute;
        }

        return day + ". " + month;
    }

    private String getYear(Date date){
        String year = new java.text.SimpleDateFormat("yyyy").format(date);
        return year;
    }

    private String getMonth(Date date) {
        String month = new java.text.SimpleDateFormat("MM").format(date);

        if (month.equals("01")) {
            month = "Januar";
        } else if (month.equals("02")) {
            month = "Februar";
        } else if (month.equals("03")) {
            month = "März";
        } else if (month.equals("04")) {
            month = "April";
        } else if (month.equals("05")) {
            month = "Mai";
        } else if (month.equals("06")) {
            month = "Juni";
        } else if (month.equals("07")) {
            month = "Juli";
        } else if (month.equals("08")) {
            month = "August";
        } else if (month.equals("09")) {
            month = "September";
        } else if (month.equals("10")) {
            month = "Oktober";
        } else if (month.equals("11")) {
            month = "November";
        } else if (month.equals("12")) {
            month = "Dezember";
        } else {
            month = "error";
        }
        return month;
    }

    private boolean refresh() {
        TextView event = (TextView) root.findViewById(R.id.Event);
        LinearLayout eventList = (LinearLayout) root.findViewById(R.id.eventList);
        eventList.removeAllViews();
        if (eventsThisDay.isEmpty()) {
            event.setVisibility(View.VISIBLE);
            event.setText(R.string.noEventMessage);
            return false;
        }
        event.setVisibility(View.GONE);
        for (int x = 0; x < eventsThisDay.size(); x++) {
            View eventView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.event_layout, null);
            String[] split = eventsThisDay.get(x).getData().toString().split(Pattern.quote("°°"));
            String startDate = MillisInDate(eventsThisDay.get(x).getTimeInMillis(), false, true, false);
            String startTime = MillisInDate(eventsThisDay.get(x).getTimeInMillis(), true, false, false);
            String endDate = "";
            String endTime = "";
            try {
                endDate = MillisInDate(Long.valueOf(split[0]), false, true, false);
                endTime = MillisInDate(Long.valueOf(split[0]), true, false, false);
            } catch (Exception e) {
            }
            String[] splitEnd = endDate.split(Pattern.quote(". "));
            String[] splitStart = startDate.split(Pattern.quote(". "));
            try {
                if (Integer.valueOf(splitEnd[0]) > Integer.valueOf(splitStart[0]) || Integer.valueOf(splitEnd[1]) > Integer.valueOf(splitStart[1])) {
                    endTime = MillisInDate(Long.valueOf(split[0]), true, true, false);
                }
            } catch (Exception e) {
            }
            String location = split[1];
            String summary = split[2];

            TextView startTimeTxt = eventView.findViewById(R.id.startTime);
            startTimeTxt.setText("Beginn: " + startTime);

            TextView endTimeTxt = eventView.findViewById(R.id.endTime);
            endTimeTxt.setText("Ende: " + endTime);

            TextView locationTxt = eventView.findViewById(R.id.location);
            locationTxt.setText("Ort: " + location);

            TextView descriptionTxt = eventView.findViewById(R.id.description);
            descriptionTxt.setText(summary);

            TextView color = eventView.findViewById(R.id.colorMarker);
            color.setBackgroundColor(eventsThisDay.get(x).getColor());

            ImageButton deleteButton = eventView.findViewById(R.id.item_event_button_delete);
            int i;
            int y;
            int z = -1;
            for (i = 0; i < ownEvents.size(); i++) {
                if (eventsThisDay.get(x).equals(ownEvents.get(i))) {
                    deleteButton.setVisibility(View.VISIBLE);
                    for (y = 0; y < events.size(); y++) {
                        if (eventsThisDay.get(x).equals(events.get(y))) {
                            z = y;
                        }
                    }
                }
            }
            int posEv1 = z;
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askDelete(posEv1);
                }
            });
            eventList.addView(eventView);
        }
        return true;
    }

    private void askDelete(int pos1) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getContext(), MainActivity.colorScheme);
        } else {
            builder = new AlertDialog.Builder(this.getContext());
        }
        builder.setTitle(R.string.deleteQuestion)
                .setMessage(this.getContext().getString(R.string.deleteQuestionMessageEvent))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(events.get(pos1));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //I think - do Nothing - but if you want
                    }
                })
                .setIcon(android.R.drawable.ic_delete);

        AlertDialog alertDialog = builder.show();

        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void save(ArrayList<Event> ev, boolean own) {
        ArrayList<CustomEvent> customEvents = new ArrayList<>();
        for (Event event :
                ev) {
            customEvents.add(new CustomEvent(event.getColor(), event.getTimeInMillis(), event.getData()));
        }
        if (own) {
            (new Util()).save(getContext(), customEvents, "ownEvents");
        } else {
            (new Util()).save(getContext(), customEvents, "events");
        }
    }

    private void delete(Event ev1) {
        events.remove(ev1);
        ArrayList<Event> oEvents = new ArrayList<>();
        oEvents.addAll(ownEvents);
        ownEvents.clear();
        for (Event e :
                oEvents) {
            if (events.contains(e)) {
                ownEvents.add(e);
            }
        }
        eventsThisDay.remove(ev1);
        save(events, false);
        save(ownEvents, true);
        compactCalendarView.removeEvent(ev1);
        refresh();
    }

    private void dateClicked(Date date) {
        for (int x = 0; x < events.size(); x++) {
            if (date.getTime() <= events.get(x).getTimeInMillis() && (date.getTime() + 86400000L) > events.get(x).getTimeInMillis()) {
                eventsThisDay.add(events.get(x));
            }
        }
        refresh();
    }

    private void dateClicked(long date) {
        for (int x = 0; x < events.size(); x++) {
            if (date <= events.get(x).getTimeInMillis() && (date + 86400000L) > events.get(x).getTimeInMillis()) {
                boolean already = false;
                for (int y = 0; y < eventsThisDay.size(); y++) {
                    if (eventsThisDay.get(y).equals(events.get(x))) {
                        already = true;
                    }
                }
                if (!already) {
                    eventsThisDay.add(events.get(x));
                }
            }
        }
        refresh();
    }

    private class getCalendarData extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            compactCalendarView.removeAllEvents();
            compactCalendarView.addEvents(events);

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<Event> eventsToAdd = new ArrayList<>();
            try {
                eventsToAdd.addAll(getEvents());
                for (int x = 0; x < eventsToAdd.size(); x++) {
                    if (events.contains(eventsToAdd.get(x))) {
                        eventsToAdd.remove(x);
                    }
                }
                for (Event ev :
                        eventsToAdd) {
                    if (!events.contains(ev)) {
                        events.add(ev);
                    }
                }


                save(events, false);


            } catch (Exception e) {
            }

            return null;
        }

        private long timeInMillis(String date) throws ParseException {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            Date datum = null;
            try {
                datum = (Date) formatter.parse(date);
            } catch (java.text.ParseException e) {
                Log.e(TAG, "Error", e);
                System.out.println("ganz schwerer Fehler");
            }
            long timestamp = datum.getTime();
            return timestamp;
        }

        private ArrayList<Event> getEvents() {
            try {
                URL url = new URL(urlLink);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

                String input;
                ArrayList<String> inputs = new ArrayList<>();
                while ((input = bufferedReader.readLine()) != null) {
                    inputs.add(input);
                }

                bufferedReader.close();

                ArrayList<Event> eventsToGet = new ArrayList<>();

                long startDate = -1;
                long endDate = -1;
                int startMonth = -1;
                int endMonth = -1;
                int startHour = -1;
                int endHour = -1;
                String uid = null;
                String location = null;
                String summary = null;

                for (String s :
                        inputs) {
                    if (s.startsWith("BEGIN:VEVENT")) {
                        startDate = -1;
                        endDate = -1;
                        startMonth = -1;
                        endMonth = -1;
                        startHour = -1;
                        endHour = -1;
                        uid = null;
                        location = null;
                        summary = null;
                    } else if (s.startsWith("DTSTART:")) {
                        String x = s.replace("DTSTART:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            startMonth = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String minutes = x.substring(11, 13);
                            int hours = Integer.valueOf(x.substring(9, 11));
                            if (startMonth > 3 && startMonth < 11) {
                                startHour = (hours + 2);
                            } else if (startMonth > 10 || startMonth < 4) {
                                startHour = (hours + 1);
                            } else {
                                System.out.println("Fehler bei Berechnung der Startzeit");
                            }
                            String date = day + "." + startMonth + "." + year + " " + startHour + ":" + minutes;
                            startDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("DTSTART;VALUE=DATE:")) {
                        String x = s.replace("DTSTART;VALUE=DATE:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year + " " + "00:00";
                            startDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("DTEND:")) {
                        String x = s.replace("DTEND:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            endMonth = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String minutes = x.substring(11, 13);
                            int hours = Integer.valueOf(x.substring(9, 11));
                            if (endMonth > 3 && endMonth < 11) {
                                endHour = (hours + 2);
                            } else if (endMonth > 10 || endMonth < 4) {
                                endHour = (hours + 1);
                            } else {
                                System.out.println("Fehler bei Berechnung der Endzeit");
                            }
                            String date = day + "." + endMonth + "." + year + " " + endHour + ":" + minutes;
                            endDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("DTEND;VALUE=DATE:")) {
                        String x = s.replace("DTEND;VALUE=DATE:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year + " " + "00:00";
                            endDate = timeInMillis(date);
                        } catch (Exception e) {
                        }
                    } else if (s.startsWith("UID:")) {
                        uid = s.replace("UID:", "");
                    } else if (s.startsWith("SUMMARY:")) {
                        summary = s.replace("SUMMARY:", "");
                    } else if (s.startsWith("LOCATION:")) {
                        location = s.replace("LOCATION:", "");
                    } else if (s.startsWith("END:VEVENT")) {
                        eventsToGet.add(new Event(Color.GREEN, startDate, endDate + "°°" + location + "°°" + summary + "°°" + uid));
                    }
                }
                return eventsToGet;

            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            }
            return null;
        }
    }
}



