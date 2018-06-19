package de.tk.annapp.Fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import de.tk.annapp.R;

/**
 * Created by Tobi on 20.09.2017.
 */

public class CalendarFragment extends Fragment {
    View root;
    CalendarView calendarView;

    public static final String TAG = "CalendarFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);

        getActivity().setTitle("Kalender");
        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = root.findViewById(R.id.calendarView);

        return root;
    }


}

