
package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.HashSet;
import java.util.Set;

import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.SchoolLessonSystem;
import com.pax.tk.annapp.SubjectManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tobi on 20.09.2017.
 */

public class SettingsFragment extends Fragment {
    View root;

    public static final String TAG = "SettingsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);

        getActivity().setTitle(R.string.settings);
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch timetableDividersSwitch = root.findViewById(R.id.dividersSwitch);

        //Setting Switch to actual setting
        Boolean dividers = getActivity().getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.key_timetableGaps), false);
        if (dividers)
            timetableDividersSwitch.setChecked(true);
        else
            timetableDividersSwitch.setChecked(false);


        System.out.println("Show dividers: " + dividers);


        timetableDividersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                /*SharedPreferences myPrefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor myPrefEditor = myPrefs.edit();
                myPrefEditor.putBoolean(getString(R.string.key_timetableGaps), b);
                myPrefEditor.apply();


                SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

                Boolean dividers = sp.getBoolean(getString(R.string.key_timetableGaps), false);*/
                getActivity().getPreferences(MODE_PRIVATE).edit().putBoolean(getString(R.string.key_timetableGaps), b).commit();
            }
        });


        Switch longClickSwitch = root.findViewById(R.id.longClick);

        //Setting Switch to actual setting
//        Boolean longClick = sp.getBoolean(getString(R.string.key_longClick), true);
        Boolean longClick = getActivity().getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.key_longClick), true);
        longClickSwitch.setChecked(longClick);


        longClickSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                /*SharedPreferences myPrefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor myPrefEditor = myPrefs.edit();
                myPrefEditor.putBoolean(getString(R.string.key_longClick), b);
                myPrefEditor.commit();


                SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

                Boolean longClick = sp.getBoolean(getString(R.string.key_longClick), true);*/

                getActivity().getPreferences(MODE_PRIVATE).edit().putBoolean(getString(R.string.key_longClick), b).commit();
            }
        });


        //GENERAL Settings
        Button btnSchoolStart = root.findViewById(R.id.btnSchoolStart);
        try {
            int ss = getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_schoolstart), 480);
            btnSchoolStart.setText(String.valueOf((int) Math.floor(ss / 60)) + ":" + String.format("%02d", ss % 60));
        } catch (Exception e) {
            e.printStackTrace();
        }


        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.key_schoolstart), hourOfDay * 60 + minute).commit();
                //save("schoolStartHour", hourOfDay);
                //save("schoolStartMinute", minute);
                btnSchoolStart.setText(String.valueOf(hourOfDay) + ":" + String.format("%02d", minute));
                setSchoolLessonSystem();
            }
        };

        btnSchoolStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int minute = getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_schoolstart), 480) % 60;

                System.out.println(minute);
                int hourOfDay = (int) Math.floor(getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_schoolstart), 480) / 60);

                TimePickerDialog tpd = new TimePickerDialog(getContext(), R.style.TimePickerTheme, onTimeSetListener, hourOfDay, minute, true); //TODO: fix crah

                tpd.show();
            }
        });

        EditText breakTime = root.findViewById(R.id.breakTime);

        breakTime.setText(String.valueOf(getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_breakTime), 30)));

        breakTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    return;
                if (Integer.valueOf(s.toString()) > 60) {
                    breakTime.setText("60");
                    getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.key_breakTime), 60).commit();
                    setSchoolLessonSystem();
                    return;
                }
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.key_breakTime), Integer.valueOf(s.toString())).commit();
                setSchoolLessonSystem();
            }
        });

        EditText lessonTime = root.findViewById(R.id.lessonTime);
        lessonTime.setText(String.valueOf(getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_lessonTime), 45)));

        lessonTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    return;
                if (Integer.valueOf(s.toString()) > 120) {
                    lessonTime.setText("120");
                    getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.key_lessonTime), 120).commit();
                    return;
                }
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.key_lessonTime), Integer.valueOf(s.toString())).commit();
                setSchoolLessonSystem();
            }
        });


        //Timetable max Lessons stuff
        EditText maxLessons = (EditText) root.findViewById(R.id.maxLesson);

        maxLessons.setText(String.valueOf(getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_maxLesson), 11)));

        maxLessons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty())
                    return;
                if (Integer.valueOf(s.toString()) > 20) {
                    maxLessons.setText("20");
                    getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.key_maxLesson), 20).commit();
                    return;
                }
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.key_maxLesson), Integer.valueOf(s.toString())).commit();
            }
        });


        //Timetable color scheme stuff
        Spinner spinner = (Spinner) root.findViewById(R.id.colorSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.color_schemes_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition), 0) != position) {
                    getActivity().getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.bundleKey_colorThemePosition), position).commit();
                    /*getActivity().finish();
                    final Intent intent = getActivity().getIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);*/
                    getActivity().recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        int colorSchemePosition = (int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition), 0);
        spinner.setSelection(colorSchemePosition);


        Button btn_tutorial = root.findViewById(R.id.btn_tutorial);
        btn_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager fm = getFragmentManager();
                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }*/

                getActivity().getPreferences(Context.MODE_PRIVATE).edit().putBoolean("firstLaunch", true).commit();

                ((MainActivity) getContext()).setFragment(TutorialFragment.TAG);

            }
        });



        return root;
    }


    void setSchoolLessonSystem() {

        Set s = new HashSet<Integer>();
        s.add(1);
        s.add(3);

        int schoolstart = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.key_schoolstart), 480);
        int lessonLength = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.key_lessonTime), 45);
        int breakLength = getActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.key_breakTime), 15);


        SchoolLessonSystem schoolLessonSystem = new SchoolLessonSystem(schoolstart, lessonLength, breakLength, s);
        System.out.println(schoolLessonSystem.getSchoolstart());

        SubjectManager subjectManager = SubjectManager.getInstance();
        subjectManager.setSchoolLessonSystem(schoolLessonSystem);
    }
}

