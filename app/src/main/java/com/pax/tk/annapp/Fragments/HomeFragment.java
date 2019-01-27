package com.pax.tk.annapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.io.File;
import java.sql.SQLOutput;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.pax.tk.annapp.Day;
import com.pax.tk.annapp.FileChooser;
import com.pax.tk.annapp.Lesson;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Util;

/**
 * Created by Tobi on 20.09.2017.
 */

public class HomeFragment extends Fragment {

    View root;
    LinearLayout timeTable;
    Manager manager;
    View divider;
    View fragment;
    Button newTimeTable;
    Button importTimeTable;

    public static final String TAG = "HomeFragment";

    /**
     * initializing variables and calling methods
     *
     * @param inflater           ...
     * @param container          ...
     * @param savedInstanceState ...
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appInformationBtn).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.Home));
        //root = inflater.inflate(R.layout.fragment_home, container, false);


        /*Intent intent = new Intent(getContext(), com.pax.tk.annapp.FlappyBird.GameActivity.class);
        getContext().startActivity(intent);*/


        if (root != null) {
            ViewGroup parent = (ViewGroup) root.getParent();
            if (parent != null)
                parent.removeView(root);
        }
        try {
            root = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        manager = Manager.getInstance();

        timeTable = root.findViewById(R.id.timeTable);
        divider = root.findViewById(R.id.divider);
        fragment = root.findViewById(R.id.fragment);
        newTimeTable = root.findViewById(R.id.newTimeTable);
        importTimeTable = root.findViewById(R.id.importTimeTable);
        newTimeTable.setText(R.string.newTimeTable);
        importTimeTable.setText(R.string.importTimeTable);

        int i = 0;
        for (Day d:
             manager.getDays()) {
            boolean subject = false;
            for (Lesson l:
                 d.getLessons()) {
                try {
                    if (l.getSubject() != null) {
                        subject = true;
                    }
                }catch (Exception e){e.printStackTrace();}
            }
            if(d.getLessons().isEmpty() || !subject){
                i++;
            }
        }
        if(i >= manager.getDays().length){
            setTimeTableEmpty();
        }else{
            setTimeTable();
        }

        System.out.println("HomeCreated");
        //Util.createPushNotification(this.getContext(), 0, "AnnApp", "Du hast die AnnApp\ngestartet!", R.drawable.ic_add/*, BitmapFactory.decodeResource(getResources(), R.drawable.ic_add)*/);


        return root;


    }

    /**
     * initializing buttons for an empty timetable
     */
    private void setTimeTableEmpty(){
        timeTable.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
        fragment.setVisibility(View.GONE);
        newTimeTable.setVisibility(View.VISIBLE);
        importTimeTable.setVisibility(View.VISIBLE);

        newTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new TimetableFragment();
                Bundle bundle = new Bundle();
                newFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, newFragment)
                        .addToBackStack(TimetableFragment.TAG)
                        .commit();
            }
        });

        importTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FileChooser(getActivity()).setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        Util.parseFileToTimetable(file, getContext(), manager);
                    }
                }).showDialog();
            }
        });
    }

    /**
     * initializing the timetable
     */
    void setTimeTable() {
        timeTable.setVisibility(View.VISIBLE);
        divider.setVisibility(View.VISIBLE);
        fragment.setVisibility(View.VISIBLE);
        newTimeTable.setVisibility(View.GONE);
        importTimeTable.setVisibility(View.GONE);
        GregorianCalendar gc = new GregorianCalendar();
        int i = gc.get(Calendar.DAY_OF_WEEK);
        int dayOfWeek = 0;
        switch (i) {
            case 2:
                dayOfWeek = 0;
                break;
            case 3:
                dayOfWeek = 1;
                break;
            case 4:
                dayOfWeek = 2;
                break;
            case 5:
                dayOfWeek = 3;
                break;
            case 6:
                dayOfWeek = 4;
                break;
            case 7:
                dayOfWeek = 0;
                break;
            case 8:
                dayOfWeek = 0;
                break;
        }

        while(manager.getDays()[dayOfWeek].getLessons().isEmpty() || checkDayEmpty(dayOfWeek)){
            dayOfWeek++;
            if(dayOfWeek > 4){
                dayOfWeek = 0;
            }
        }
        for (Lesson l :
                manager.getDays()[dayOfWeek].getLessons()) {

            View btn;

            System.out.println(manager.getDays()[dayOfWeek].getLessons());

            timeTable.setShowDividers(View.VISIBLE);

            if (l == null || l.getSubject() == null)
                btn = getEmptyCellButton("");
            else {
                btn = getCellCardView(l.getSubject(), "");
            }

            timeTable.addView(btn);
            Space space = new Space(getContext());
            space.setMinimumHeight(8);
            timeTable.addView(space);
        }

        try {
            timeTable.removeViewAt(0);
        } catch (NullPointerException npe) {
            divider.setVisibility(View.GONE);
        }
    }

    private boolean checkDayEmpty(int dayOfWeek){
        boolean empty = true;
        for (Lesson l:
                manager.getDays()[dayOfWeek].getLessons()) {
            try {
                if (l.getSubject() != null) {
                    empty = false;
                }
            }catch (Exception e){e.printStackTrace();}
        }
        return empty;
    }

    /**
     * creates a cardView for a subject and makes it clickable to get into the timetableFragment
     *
     * @param subject ...
     * @param position ...
     * @return cardView with the subject in it
     */
    CardView getCellCardView(Subject subject, String position) {
        CardView cardView = new CardView(this.getContext());
        cardView.setRadius(20);

        //general Settings for Cells
        //cardView.setTextColor(getResources().getColor(R.color.default_background_color));

        int color = 0;

        int index = manager.getSubjects().indexOf(subject);
        for (int i; index > 14; index = index - 14) {
        }

        color = new Util().getSubjectColor(getContext(), subject);

        /*GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 24 );
        shape.setColor(color);

        cardView.setBackground(shape);*/

        cardView.setCardBackgroundColor(color);
        cardView.setCardElevation(3f);

        cardView.setTag(position);

        TextView txt = new TextView(getContext());
        txt.setText(subject.getName());
        txt.setTextColor(getContext().getColor(android.R.color.white));
        txt.setTextSize(20);
        txt.setPadding(8, 8, 16, 8);


        cardView.addView(txt);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).setFragment(TimetableFragment.TAG);
            }
        });

        return cardView;
    }

    /**
     * creates empty cardView if lesson is empty and makes it clickable to get into the timetableFragment
     *
     * @param position ...
     * @return empty cardView
     */
    CardView getEmptyCellButton(String position) {
        CardView btn = new CardView(this.getContext());


        //general Settings for empty cells

        btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btn.addView(new TextView(this.getContext()));

        btn.setTag(position);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getContext()).setFragment(TimetableFragment.TAG);
            }
        });
        return btn;
    }

}
