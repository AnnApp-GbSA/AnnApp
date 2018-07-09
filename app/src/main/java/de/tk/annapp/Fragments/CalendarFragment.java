package de.tk.annapp.Fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import de.tk.annapp.Grade;
import de.tk.annapp.R;
import de.tk.annapp.Subject;
import de.tk.annapp.SubjectManager;

/**
 * Created by Tobi on 20.09.2017.
 */

@TargetApi(Build.VERSION_CODES.N)
public class CalendarFragment extends Fragment {
    public static final String TAG = "CalendarFragment";
    CompactCalendarView compactCalendarView;
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<Event> eventsThisDay = new ArrayList<>();
    long clicked = System.currentTimeMillis();
    String urlLink = "https://calendar.google.com/calendar/ical/o5bthi1gtvamdjhed61rot1e74%40group.calendar.google.com/public/basic.ics";
    String urlLink2 = "https://calendar.google.com/calendar/embed?src=o5bthi1gtvamdjhed61rot1e74@group.calendar.google.com&ctz=Europe/Berlin&pli=1";
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.calendar));
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.VISIBLE);
        ((Button)getActivity().findViewById(R.id.syncWithCalendar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSyncWithCalendar();
            }
        });

        TextView monthIndication = root.findViewById(R.id.monthIndication);
        TextView dateInformation = root.findViewById(R.id.dateInformation);
        Button monthBack = root.findViewById(R.id.monthBack);
        Button monthForward = root.findViewById(R.id.monthForward);
        FloatingActionButton fabAdd = root.findViewById(R.id.fabAddCalendar);

        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        new getCalendarData().execute((Void) null);

        eventsThisDay.clear();
        monthIndication.setText(dateFormatMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        dateInformation.setText(MillisInDate(System.currentTimeMillis()));
        dateClicked(System.currentTimeMillis());

        monthBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollLeft();
                eventsThisDay.clear();
                clicked = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
                dateInformation.setText(MillisInDate(compactCalendarView.getFirstDayOfCurrentMonth().getTime()));
                dateClicked(compactCalendarView.getFirstDayOfCurrentMonth());
            }
        });

        monthForward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollRight();
                eventsThisDay.clear();
                clicked = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
                dateInformation.setText(MillisInDate(compactCalendarView.getFirstDayOfCurrentMonth().getTime()));
                dateClicked(compactCalendarView.getFirstDayOfCurrentMonth());
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                clicked = dateClicked.getTime();
                eventsThisDay.clear();
                dateInformation.setText(MillisInDate(dateClicked.getTime()));
                dateClicked(dateClicked);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    monthIndication.setText(dateFormatMonth.format(firstDayOfNewMonth));
                    clicked = compactCalendarView.getFirstDayOfCurrentMonth().getTime();
                    eventsThisDay.clear();
                    dateInformation.setText(MillisInDate(compactCalendarView.getFirstDayOfCurrentMonth().getTime()));
                    dateClicked(compactCalendarView.getFirstDayOfCurrentMonth());
                } else
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
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

    private void  onSyncWithCalendar(){

    }

    public void createInputDialog(){
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(),R.style.NewDialog);

        View mView = View.inflate(this.getContext(), R.layout.dialog_new_event, null);

        final EditText eventInput = (EditText) mView.findViewById(R.id.eventInput);
        final EditText startDateInput = (EditText) mView.findViewById(R.id.startDateInput);
        final EditText endDateInput = (EditText) mView.findViewById(R.id.endDateInput);
        final EditText endTimeInput = (EditText) mView.findViewById(R.id.endTimeInput);
        final EditText startTimeInput = (EditText) mView.findViewById(R.id.startTimeInput);
        final EditText locationInput = (EditText) mView.findViewById(R.id.locationInput);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);
        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        startDateInput.setText(MillisInDate(clicked));

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
            public void onClick(View view){

                if(eventInput.getText().toString().isEmpty() || startDateInput.getText().toString().isEmpty()){
                    createAlertDialog(getString(R.string.warning), getString(R.string.warningMessage), 0);
                    return;
                }


                Event ev1 = new Event(Color.YELLOW, clicked, startDateInput.getText().toString() + "°" + endDateInput.getText().toString() + "°" + startTimeInput.getText().toString() + "°" + endTimeInput.getText().toString() + "°" + locationInput.getText().toString() + "°" + eventInput.getText().toString());
                compactCalendarView.addEvent(ev1);
                events.add(ev1);

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

    private String MillisInDate(long time){
        String day = new java.text.SimpleDateFormat("dd").format(new java.util.Date(time));
        String month = new java.text.SimpleDateFormat("MM").format(new java.util.Date(time));
        if(month.equals("01")){
            month = "Januar";
        }
        else if(month.equals("02")){
            month = "Februar";
        }
        else if(month.equals("03")){
            month = "März";
        }
        else if(month.equals("04")){
            month = "April";
        }
        else if(month.equals("05")){
            month = "Mai";
        }
        else if(month.equals("06")){
            month = "Juni";
        }
        else if(month.equals("07")){
            month = "Juli";
        }
        else if(month.equals("08")){
            month = "August";
        }
        else if(month.equals("09")){
            month = "September";
        }
        else if(month.equals("10")){
            month = "Oktober";
        }
        else if(month.equals("11")){
            month = "November";
        }
        else if(month.equals("12")){
            month = "Dezember";
        }
        else{
            System.out.println("Fehler bei MillisInDate()");
        }
        return day + ". " + month;
    }

    private boolean eventInformation(){
        TextView event = (TextView) root.findViewById(R.id.Event);
        LinearLayout eventList = (LinearLayout) root.findViewById(R.id.eventList);
        String text = "";
        if(eventsThisDay.isEmpty()){
            event.setVisibility(View.VISIBLE);
            event.setText(R.string.noEventMessage);
            return false;
        }
        event.setVisibility(View.GONE);
        for(int x = 0; x < eventsThisDay.size(); x++){
            View eventView = LayoutInflater.from(this.getContext()).inflate(
                    R.layout.event_layout, null);
            String[] split = eventsThisDay.get(x).getData().toString().split(Pattern.quote("°"));
            String startDate = split[0];
            String endDate = split[1];
            String startTime = split[2];
            String endTime = split[3];
            String location = split[4];
            String summary = split[5];
            /*if(summary != null && summary != "null"){
                text = text + "Beschreibung: " + summary + "\n";
            }else{
                text = text + "Beschreibung:\n";
            }
            if(startTime != null && startTime != "null"){
                text = text + "Anfang: " + startTime + "\n";
            }else{
                text = text + "Anfang:\n";
            }
            if(endTime != null && endTime != "null"){
                text = text + "Ende: " + endTime + "\n";
            }else{
                text = text + "Ende:\n";
            }
            if(location != null && location != "null"){
                text = text + "Ort: " + location + "\n\n";
            }else{
                text = text + "Ort:\n\n";
            }*/

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


            eventList.addView(eventView);
        }
        //event.setText(text);
        return true;
    }

    private void dateClicked(Date date){
        for(int x = 0; x < events.size(); x++){
            if(date.getTime() <= events.get(x).getTimeInMillis() && (date.getTime() + 86400000L) > events.get(x).getTimeInMillis()) {
                eventsThisDay.add(events.get(x));
            }
        }
        eventInformation();
    }

    private void dateClicked(long date){
        for(int x = 0; x < events.size(); x++){
            if(date <= events.get(x).getTimeInMillis() && (date + 86400000L) > events.get(x).getTimeInMillis()) {
                eventsThisDay.add(events.get(x));
            }
        }
        eventInformation();
    }

    private class getCalendarData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<Event> eventsToAdd = new ArrayList<>();
            try {
                eventsToAdd.addAll(getEvents());
                compactCalendarView.addEvents(eventsToAdd);
                for(Event ev :
                        eventsToAdd){
                    events.add(ev);
                }
            } catch (Exception e){}

            return null;
        }

        private long timeInMillis(String date) throws ParseException {
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date datum = null;
            try {
                datum = (Date) formatter.parse(date);
            }catch (java.text.ParseException e){
                Log.e(TAG, "Error", e);
            }
            long timestamp = datum.getTime();
            return timestamp;
        }

        private ArrayList<Event> getEvents() {
            try{
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
                String startTime = null;
                String endTime = null;
                String location = null;
                String summary = null;

                for (String s :
                        inputs) {
                    if (s.startsWith("BEGIN:VEVENT")) {
                        startDate = -1;
                        endDate = -1;
                        startTime = null;
                        endTime = null;
                        location = null;
                        summary = null;
                    } else if (s.startsWith("DTSTART:")) {
                        String x = s.replace("DTSTART:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year;
                            startDate = timeInMillis(date);
                        } catch (Exception e) {}
                        try {
                            String minutes = x.substring(11, 13);
                            String hours = x.substring(9, 11);
                            startTime = hours + ":" + minutes + " Uhr";
                        } catch (Exception e) {}
                    } else if (s.startsWith("DTSTART;VALUE=DATE:")) {
                        String x = s.replace("DTSTART;VALUE=DATE:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year;
                            startDate = timeInMillis(date);
                        } catch (Exception e) {}
                    } else if (s.startsWith("DTEND:")) {
                        String x = s.replace("DTEND:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year;
                            endDate = timeInMillis(date);
                        } catch (Exception e) {}
                        try {
                            String minutes = x.substring(11, 13);
                            String hours = x.substring(9, 11);
                            endTime = hours + ":" + minutes + ":" + " Uhr";
                        } catch (Exception e) {}
                    } else if (s.startsWith("DTEND;VALUE=DATE:")) {
                        String x = s.replace("DTEND;VALUE=DATE:", "");
                        try {
                            int year = Integer.valueOf(x.substring(0, 4));
                            int month = Integer.valueOf(x.substring(4, 6));
                            int day = Integer.valueOf(x.substring(6, 8));
                            String date = day + "." + month + "." + year;
                            endDate = timeInMillis(date);
                        } catch (Exception e) {}
                    } else if (s.startsWith("SUMMARY:")) {
                        summary = s.replace("SUMMARY:", "");
                    } else if (s.startsWith("LOCATION:")) {
                        location = s.replace("LOCATION:", "");
                    } else if (s.startsWith("END:VEVENT")) {
                        eventsToGet.add(new Event(Color.GREEN, startDate, startDate + "°" + endDate + "°" + startTime + "°" + endTime + "°" + location + "°" + summary));
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



