
package de.tk.annapp.Fragments;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.tk.annapp.Day;
import de.tk.annapp.R;
import de.tk.annapp.SchoolLessonSystem;
import de.tk.annapp.SubjectManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Tobi on 20.09.2017.
 */

public class SettingsFragment extends Fragment {
    View root;

    public static final String TAG = "SettingsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle(R.string.settings);
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch timetableDividersSwitch = root.findViewById(R.id.dividersSwitch);

        //Setting Switch to actual setting
        SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
        Boolean dividers = sp.getBoolean("timetableDividers", false);
        timetableDividersSwitch.setChecked(dividers);


        timetableDividersSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                SharedPreferences myPrefs = getContext().getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor myPrefEditor = myPrefs.edit();
                myPrefEditor.putBoolean("timetableDividers", b);
                myPrefEditor.commit();


                SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

                Boolean dividers = sp.getBoolean("timetableDividers", false);
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

                TimePickerDialog tpd = new TimePickerDialog(getContext(), onTimeSetListener, hourOfDay, minute, true);
                tpd.show();
            }
        });

        EditText breakTime = root.findViewById(R.id.breakTime);

        breakTime.setText(String.valueOf(getActivity().getPreferences(MODE_PRIVATE).getInt("breakTime", 30)));

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
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("breakTime", Integer.valueOf(s.toString())).commit();
                setSchoolLessonSystem();
            }
        });

        EditText lessonTime = root.findViewById(R.id.lessonTime);
        lessonTime.setText(String.valueOf(getActivity().getPreferences(MODE_PRIVATE).getInt("lessonTime", 45)));

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
                getActivity().getPreferences(MODE_PRIVATE).edit().putInt("lessonTime", Integer.valueOf(s.toString())).commit();
                setSchoolLessonSystem();
            }
        });


        //Timetable max Lessons stuff
        EditText maxLessons = (EditText) root.findViewById(R.id.maxLesson);

        maxLessons.setText(String.valueOf(getActivity().getPreferences(MODE_PRIVATE).getInt("maxlessons", 11)));

        maxLessons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                    getActivity().getPreferences(MODE_PRIVATE).edit().putInt("maxlessons", Integer.valueOf(s.toString())).commit();
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
                if (getActivity().getPreferences(MODE_PRIVATE).getInt("colorSchemePosition", 0) != position) {
                    getActivity().getPreferences(MODE_PRIVATE).edit().putInt("colorSchemePosition", position).commit();
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

        int colorSchemePosition = (int) getActivity().getPreferences(MODE_PRIVATE).getInt("colorSchemePosition", 0);
        spinner.setSelection(colorSchemePosition);


        return root;
    }


    void setSchoolLessonSystem() {

        Set s = new HashSet<Integer>();
        s.add(1);
        s.add(3);

        int schoolstart = getActivity().getPreferences(Context.MODE_PRIVATE).getInt("schoolstart", 480);
        int lessonLength = getActivity().getPreferences(Context.MODE_PRIVATE).getInt("lessonTime", 45);
        int breakLength = getActivity().getPreferences(Context.MODE_PRIVATE).getInt("breakTime", 15);


        SchoolLessonSystem schoolLessonSystem = new SchoolLessonSystem(schoolstart, lessonLength, breakLength, s);
        System.out.println(schoolLessonSystem.getSchoolstart());

        SubjectManager subjectManager = SubjectManager.getInstance();
        subjectManager.setSchoolLessonSystem(schoolLessonSystem);
    }
}

