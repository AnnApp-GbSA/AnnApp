package de.tk.annapp.Fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.service.autofill.FillEventHistory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import de.tk.annapp.News;
import de.tk.annapp.R;
import de.tk.annapp.SchoolEvent;
import de.tk.annapp.SubjectManager;

/**
 * Created by Tobi on 20.09.2017.
 */

public class CalendarFragment extends Fragment{
    public static final String TAG = "CalendarFragment";
    CompactCalendarView compactCalendarView;
    ArrayList<Event> events = new ArrayList<>();
    String urlLink = "https://calendar.google.com/calendar/ical/o5bthi1gtvamdjhed61rot1e74%40group.calendar.google.com/public/basic.ics";
    String urlLink2 = "https://calendar.google.com/calendar/embed?src=o5bthi1gtvamdjhed61rot1e74@group.calendar.google.com&ctz=Europe/Berlin&pli=1";
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.getDefault());

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);

        getActivity().setTitle("Kalender");
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        doInBackground();

        TextView monthIndication = root.findViewById(R.id.monthIndication);
        TextView dateInformation = root.findViewById(R.id.dateInformation);

        compactCalendarView = root.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        Event ev1 = new Event(Color.GREEN, 1530489600000L, "blabla");
        events.add(ev1);

        Event ev2 = new Event(Color.GREEN, 1530835200000L, "blsdfnkhds");
        events.add(ev2);

        compactCalendarView.addEvents(events);

        compactCalendarView.setCalendarBackgroundColor(Color.BLACK);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                for(int x = 0; x < events.size(); x++) {
                    if (dateClicked.getTime() == events.get(x).getTimeInMillis()) {
                        dateInformation.setText(events.get(x).getData().toString());
                    }
                    else{
                        dateInformation.setText("");
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthIndication.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        return root;
    }

    protected void doInBackground() {
        try {
            /*if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                urlLink = "http://" + urlLink;*/

            URL url = new URL(urlLink);

            URLConnection con = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            /*String strLine = "";
            while((strLine = bufferedReader.readLine()) != null)
                System.out.println(strLine);
            bufferedReader.close();*/

            String input;
            while((input = bufferedReader.readLine()) != null){
                System.out.println(input);
            }
            bufferedReader.close();

            //InputStream inputStream = url.openConnection().getInputStream();
            //subjectManager.mergeNews(parseFeed(inputStream));
        } catch (IOException e) {
            Log.e(TAG, "Error", e);
        }
    }
}



