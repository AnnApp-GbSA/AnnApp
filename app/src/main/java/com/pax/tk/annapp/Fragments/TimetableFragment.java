package com.pax.tk.annapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.Fragment;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.ArrayList;

import com.pax.tk.annapp.Day;
import com.pax.tk.annapp.Lesson;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.Util;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimetableFragment extends Fragment {

    //public static final int COLUMN_SIZE = 5;
    //public static final int ROW_SIZE = 11;

    //default spacing
    int spacing = 4;

    boolean dividers;

    Subject lastSubject;
    Subject uglyAsHellWayToCreateAOtherCoiseOption = new Subject("-", 0, null, null);

    //private AbstractTableAdapter mTableViewAdapter;
    //private TableView mTableView;

    TableLayout tableLayout;

    Manager manager;

    public static final String TAG = "TimetableFragment";

    /**
     * empty constructor is required
     */
    public TimetableFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param savedInstanceState ...
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uglyAsHellWayToCreateAOtherCoiseOption.setName(getString(R.string.newSubject));
        //setRowSize();
        //initData();
    }

    /**
     * initializing variables and calling methods
     *
     * @param inflater           ...
     * @param container          ...
     * @param savedInstanceState ...
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.timetable));
        View root = inflater.inflate(R.layout.fragment_timetable, container, false);
        manager = Manager.getInstance();


        if (!manager.getSubjects().isEmpty())
            lastSubject = manager.getSubjects().get(0);
        else
            lastSubject = uglyAsHellWayToCreateAOtherCoiseOption;
        //RelativeLayout fragment_container = root.findViewById(R.id.fragment_container);

        tableLayout = root.findViewById(R.id.tableLayout);

        /*SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

        dividers = sp.getBoolean(getString(R.string.key_timetableGaps), false);*/

        dividers = getActivity().getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.key_timetableGaps), false);

        HorizontalScrollView sv = root.findViewById(R.id.background);

        /*if (dividers) {
            sv.setBackgroundColor(Color.parseColor("#DADADA"));
        } else {
            sv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }*/

        if (!dividers)
            sv.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        initializeTableView();
        return root;
    }


    /**
     * initializing the tableView
     */
    void initializeTableView() {

        tableLayout.removeAllViews();

        int u;
        System.out.println("Longest day: "+manager.getLongestDaysLessons());
        if ((int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_maxLesson), 12)+1 > manager.getLongestDaysLessons())
            u = (int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_maxLesson), 12)+1;
        else
            u = manager.getLongestDaysLessons();

        for (int i = 0; i < u; i++) {
            TableRow tableRow = new TableRow(this.getContext());
            tableRow.setPadding(0, 0, 0, spacing);

            if(dividers) {
                TypedValue a = new TypedValue();
                getContext().getTheme().resolveAttribute(R.attr.colorAccent, a, true);

                tableRow.setBackgroundColor(a.data);
            }else
                tableRow.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            if (i == 0) {

                Button b = getHeaderButton((int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition), 0));
                tableRow.addView(b);
                tableRow.addView(getSpaceButton());


                int f = 0;
                for (int d = 0; d < 5; d++) {
                    Button btn = getHeaderButton((int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition), 0));

                    switch (f) {
                        case (0):
                            btn.setText(R.string.monday);
                            break;
                        case (1):
                            btn.setText(R.string.tuesday);
                            break;
                        case (2):
                            btn.setText(R.string.wednesday);
                            break;
                        case (3):
                            btn.setText(R.string.thursday);
                            break;
                        case (4):
                            btn.setText(R.string.friday);
                            break;
                        case (5):
                            btn.setText(R.string.saturday);
                            break;
                        case (6):
                            btn.setText(R.string.sunday);
                            break;
                        default:
                            btn.setText("Unnamed Day");
                            break;
                    }
                    f++;


                    tableRow.addView(btn);
                    tableRow.addView(getSpaceButton());

                }
            } else {
                //add row header
                Button btn = getHeaderButton((int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition), 0));
                btn.setText(i + ". " + getString(R.string.lesson));
                tableRow.addView(btn);
                tableRow.addView(getSpaceButton());

                int x = 0;

                for (Day d :
                        manager.getDays()) {

                    x++;
                    String cellName;

                    View cell;


                    try {
                        cellName = d.getLesson(i).getSubject().getName();
                        //add cell
                        cell = getCellButton(d.getLesson(i).getSubject(), String.valueOf(x) + "#" + String.valueOf(i));
                        ((Button) cell).setText(cellName);
                    } catch (Exception e) {
                        cell = getEmptyCellButton(String.valueOf(x) + "#" + String.valueOf(i));
                    }

                    tableRow.addView(cell);
                    tableRow.addView(getSpaceButton());
                }

            }


            tableLayout.addView(tableRow);
        }
        TableRow tr = new TableRow(this.getContext());
        Space bottomSpace = new Space(this.getContext());
        bottomSpace.setMinimumHeight(spacing * 2);
        tr.addView(bottomSpace);
        tableLayout.addView(tr);


    }

    /**
     * creates a spaceButton
     *
     * @return spaceButton as a TextView
     */
    TextView getSpaceButton() {
        TextView btn = new TextView(this.getContext());
        btn.setWidth(spacing);
        btn.setMinWidth(spacing);
        btn.setMaxWidth(spacing);
        return btn;
    }

    /**
     * creates a headerButton
     *
     * @param colorScheme ...
     * @return headerButton
     */
    Button getHeaderButton(int colorScheme) {
        Button btn = new Button(this.getContext());

        btn.setTextColor(getResources().getColor(android.R.color.white));

        TypedValue a = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorTimetableHeader, a, true);

        btn.setBackgroundColor(a.data);

        btn.setTypeface(null, Typeface.BOLD);/*

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Clicked ", Toast.LENGTH_SHORT).show();
            }
        });*/

        return btn;
    }

    /**
     * creates cellButton and makes it clickable to call lessonInfo()
     *
     * @param subject ...
     * @param position ...
     * @return cellButton
     */
    Button getCellButton(Subject subject, String position) {
        Button btn = new Button(this.getContext());

        //general Settings for Cells

        int color = Util.getSubjectColor(this.getContext(), subject);

        btn.setBackgroundColor(color);

        btn.setTag(position);

        btn.setTextColor(getResources().getColor(android.R.color.white));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lessonInfo(view);
            }
        });

        return btn;
    }

    /**
     * creates empty cellButton and makes it clickable to add a lesson
     *
     * @param position ...
     * @return empty cellButton
     */
    Button getEmptyCellButton(String position) {
        Button btn = new Button(this.getContext());
        TypedValue a = new TypedValue();

        //general Settings for empty cells

        if (dividers) {
            getContext().getTheme().resolveAttribute(android.R.attr.colorBackground, a, true);
            btn.setBackgroundColor(a.data);
        } else
           btn.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        btn.setTag(position);


        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Toast.makeText(getContext(), "Empty Cell pressed on Position" + view.getTag(), Toast.LENGTH_SHORT).show();
                /*SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

                Boolean longClick = sp.getBoolean(getString(R.string.key_longClick), true);*/
                Boolean longClick = getActivity().getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.key_longClick), true);

                if (longClick)
                    addLesson(view);
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Empty Cell pressed on Position" + view.getTag(), Toast.LENGTH_SHORT).show();
                /*SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

                Boolean longClick = sp.getBoolean(getString(R.string.key_longClick), true);*/

                Boolean longClick = getActivity().getPreferences(MODE_PRIVATE).getBoolean(getString(R.string.key_longClick), true);

                if (!longClick)
                    addLesson(view);
            }
        });

        return btn;
    }

    /**
     * calls createNewLessonDialog() for the right day and time
     *
     * @param view place in the timetable where a new lesson shall be created
     */
    void addLesson(View view) {
        //get time out of tag
        String[] s = view.getTag().toString().split("#");
        int x = Integer.valueOf(s[0]);
        int y = Integer.valueOf(s[1]);

        //manager.setLesson(manager.getSubjects().get(1), "", y, x-1);

        createNewLessonDialog(x - 1, y, null, false);

    }

    /**
     * calls createLessonInfoDialog() for the right day and time
     *
     * @param view place in the timetable where the info shall be get from
     */
    void lessonInfo(View view) {
        String[] s = view.getTag().toString().split("#");
        int x = Integer.valueOf(s[0]) - 1;
        int y = Integer.valueOf(s[1]);

        createLessonInfoDialog(x, y, manager.getDays()[x].getLesson(y).getSubject());
    }

    /**
     * creates a newLessonDialog, sets it up and calls methods
     *
     * @param day day on which the lesson shall be created
     * @param time time to which the lesson shall be created
     * @param subject subject of the lesson
     * @param subjectEdit true if the subject shall be edited
     */
    public void createNewLessonDialog(final int day, final int time, final Subject subject, final Boolean subjectEdit) {
        //AlertDialog.Builder ad = new  AlertDialog.Builder(this.getContext());
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.NewDialog);


        View mView = View.inflate(this.getContext(), R.layout.dialog_lesson, null);
        //mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);

        final FloatingActionButton btnClear = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        final EditText roomInput = (EditText) mView.findViewById(R.id.roomInput);

        final ImageView btnRoomHelp = (ImageView) mView.findViewById(R.id.btnRoomHelp);

        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);

        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);

        final RadioButton radioBtn1 = mView.findViewById(R.id.rating_1);

        final RadioButton radioBtn2 = mView.findViewById(R.id.rating_2);

        final EditText teacherEdittext = mView.findViewById(R.id.teacherInput);

        final EditText nameEdittext = mView.findViewById(R.id.subjectNameInput);


        btnRoomHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.createAlertDialog(getString(R.string.room), getString(R.string.room_warning), 0, getContext());
            }
        });


        final Spinner subjectSelection = (Spinner) mView.findViewById(R.id.subjectSelection);
        subjectSelection.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Util.closeKeyboard(mView.findViewById(R.id.headerLayout), getContext());
                return false;
            }
        });

        //All the stuff for editing subject
        if (subjectEdit) {
            Space topSpace = mView.findViewById(R.id.topSpace);
            topSpace.setMinimumHeight(20);
            subjectSelection.setVisibility(View.GONE);
            extraLayout.setVisibility(View.VISIBLE);
            nameEdittext.setText(subject.getName());
            if (subject.getRatingSub() == 1)
                radioBtn1.setChecked(true);
            else
                radioBtn2.setChecked(true);
            teacherEdittext.setText(subject.getTeacher());
            roomInput.setText(subject.getRoom());
        }
        ArrayList<Subject> spinnerlist = (ArrayList<Subject>) manager.getSubjects().clone();
        spinnerlist.add(uglyAsHellWayToCreateAOtherCoiseOption);


        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this.getContext(), R.layout.white_spinner_item, spinnerlist);
        subjectSelection.setAdapter(adapter);
        subjectSelection.setSelection(spinnerlist.indexOf(lastSubject));
        subjectSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos == subjectSelection.getAdapter().getCount() - 1 || subjectEdit) {
                    if (extraLayout.getVisibility() == View.GONE) {
                        extraLayout.setVisibility(View.VISIBLE);
                        btnRoomHelp.setVisibility(View.GONE);
                    }
                    if (!subjectEdit) {
                        nameEdittext.setText("");
                        radioBtn1.setChecked(true);
                        teacherEdittext.setText("");
                        roomInput.setText("");
                    }

                    btnExtra.setVisibility(View.GONE);

                } else {
                    extraLayout.setVisibility(View.GONE);
                    Subject selectedSubject = (Subject) subjectSelection.getSelectedItem();
                    nameEdittext.setText(selectedSubject.getName());
                    if (subject != null) {
                        if (subject.getRatingSub() == 1)
                            radioBtn1.setChecked(true);
                        else
                            radioBtn2.setChecked(true);
                    } else {
                        if (selectedSubject.getRatingSub() == 1)
                            radioBtn1.setChecked(true);
                        else
                            radioBtn2.setChecked(true);
                    }
                    teacherEdittext.setText(selectedSubject.getTeacher());
                    roomInput.setText(selectedSubject.getRoom());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (subject != null)
            subjectSelection.setSelection(spinnerlist.indexOf(subject));

        bsd.setTitle("Stunde hinzufügen");
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject oCSubject;
                Subject selectedsubject = (Subject) subjectSelection.getSelectedItem();

                if (subjectSelection.getSelectedItemPosition() == subjectSelection.getAdapter().getCount() - 1) { //Neues Subject
                    if (nameEdittext.getText().toString().equals("")) {
                        Util.createAlertDialog(getString(R.string.name_warning), getString(R.string.name_warning_empty), 0, getContext());
                        return;
                    }
                    oCSubject = new Subject(nameEdittext.getText().toString(),
                            radioBtn1.isChecked() ? 1 : 2,
                            teacherEdittext.getText().toString(),
                            roomInput.getText().toString());
                    if (manager.getSubjects().contains(oCSubject)) {
                        Util.createAlertDialog(getString(R.string.name_warning), getString(R.string.name_warning_dup) + oCSubject.getName() + "\"", 0, getContext());
                        return;
                    }
                    manager.addSubject(oCSubject);
                } else if (nameEdittext.getText().toString().equals(selectedsubject.getName()) &
                        teacherEdittext.getText().toString().equals(selectedsubject.getTeacher()) &
                        (radioBtn1.isChecked() ? 1 : 2) == selectedsubject.getRatingSub()) {
                    oCSubject = selectedsubject;
                } else {
                    if (nameEdittext.getText().toString().equals("")) {
                        Util.createAlertDialog(getString(R.string.name_warning), getString(R.string.name_warning_empty), 0, getContext());
                        return;
                    }
                    selectedsubject.setName(nameEdittext.getText().toString());
                    selectedsubject.setRatingSub(radioBtn1.isChecked() ? 1 : 2);
                    selectedsubject.setTeacher(teacherEdittext.getText().toString());
                    selectedsubject.setRoom(roomInput.getText().toString());
                    oCSubject = selectedsubject;
                }

                System.out.println(oCSubject + "\\" + (roomInput.getText().toString().isEmpty() ? null : roomInput.getText().toString()) + "\\" + day + "\\" + time);
                manager.setLesson(new Lesson(oCSubject, roomInput.getText().toString().isEmpty() ? null : roomInput.getText().toString(), day, time));

                initializeTableView();

                bsd.cancel();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //manager.setLesson(new Lesson(null,null, day, time));
                //initializeTableView();
                bsd.cancel();
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

    /**
     * creates a lessonInfoDialog, sets it up and calls methods
     *
     * @param day day of the lesson
     * @param time time at which the lesson takes place
     * @param subject subject of the lesson
     */
    @SuppressLint("ResourceAsColor")
    public void createLessonInfoDialog(final int day, final int time, final Subject subject) {
        //AlertDialog.Builder ad = new  AlertDialog.Builder(this.getContext());
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.NewDialog);


        View mView = View.inflate(this.getContext(), R.layout.dialog_lesson_info, null);
        //mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        final FloatingActionButton btnClear = (FloatingActionButton) mView.findViewById(R.id.btnCancel);
        final FloatingActionButton btnDelete = (FloatingActionButton) mView.findViewById(R.id.btnDeleteSubject);
        final FloatingActionButton btnEdit = (FloatingActionButton) mView.findViewById(R.id.btnEditSubject);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        TextView roomTxt = (TextView) mView.findViewById(R.id.roomTxt);
        TextView teacherTxt = (TextView) mView.findViewById(R.id.teacherTxt);

        TextView subjectView = (TextView) mView.findViewById(R.id.subjectView);
        TextView teacherView = (TextView) mView.findViewById(R.id.teacherView);
        TextView roomView = (TextView) mView.findViewById(R.id.roomView);

        subjectView.setText(subject.getName());

        subjectView.setMinWidth(900);

        teacherView.setText(subject.getTeacher());

        roomView.setText(manager.getDays()[day].getLesson(time).getRoom());

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsd.cancel();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsd.cancel();
                createDeleteQuestionDialog(manager.getDays()[day].getLesson(time));

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
                createEditQuestionDialog(manager.getDays()[day].getLesson(time));
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

    /**
     * creates a deleteQuestionDialog, sets it up and calls methods
     *
     * @param lesson lesson which shall be deleted
     */
    void createDeleteQuestionDialog(final Lesson lesson) {
        //AlertDialog.Builder ad = new  AlertDialog.Builder(this.getContext());
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.NewDialog);


        View mView = View.inflate(this.getContext(), R.layout.dialog_lesson_delete, null);
        //mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);
        final Button selectionThisLesson = (Button) mView.findViewById(R.id.selectionThisLesson);
        final Button selectionAllLessons = (Button) mView.findViewById(R.id.selectionAllLessons);
        final Button selectionSubject = (Button) mView.findViewById(R.id.selectionSubject);

        selectionAllLessons.setText("Alle " + lesson.getSubject().getName() + "stunden");

        selectionThisLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reallyDeleteQuestionForDAUs(0, lesson, bsd);
            }
        });

        selectionAllLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reallyDeleteQuestionForDAUs(1, lesson, bsd);
            }
        });

        selectionSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reallyDeleteQuestionForDAUs(2, lesson, bsd);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsd.cancel();
            }
        });

        bsd.setContentView(mView);
        bsd.show();
    }

    /**
     * creates a editQuestionDialog, sets it up and calls methods
     *
     * @param lesson lesson which shall be edited
     */
    void createEditQuestionDialog(final Lesson lesson) {
        //AlertDialog.Builder ad = new  AlertDialog.Builder(this.getContext());
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.NewDialog);


        View mView = View.inflate(this.getContext(), R.layout.dialog_lesson_edit, null);
        //mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);
        final Button selectionThisLesson = (Button) mView.findViewById(R.id.selectionThisLesson);
        final Button selectionSubject = (Button) mView.findViewById(R.id.selectionSubject);

        selectionThisLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewLessonDialog(lesson.getDay(), lesson.getTime(), lesson.getSubject(), false);
                bsd.cancel();
            }
        });

        selectionSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewLessonDialog(lesson.getDay(), lesson.getTime(), lesson.getSubject(), true);
                bsd.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsd.cancel();
            }
        });

        bsd.setContentView(mView);
        bsd.show();
    }

    /*void createAlertDialog(String title, String text, int ic) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getContext(), MainActivity.colorScheme);
        } else {
            builder = new AlertDialog.Builder(this.getContext());
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

    }*/

    /**
     * last Question if the lesson, all lessons of the subject or the whole subject shall be deleted
     *
     * @param i only this lesson, all lessons or the whole subject (0, 1, 2)
     * @param lesson lesson which shall be deleted
     * @param bsd ...
     */
    void reallyDeleteQuestionForDAUs(final int i, final Lesson lesson, final BottomSheetDialog bsd) {
        String message = "";
        switch (i) {
            case 0:
                message = getString(R.string.delete_warning_1);
                break;
            case 1:
                message = getString(R.string.delete_warning_2_1) + lesson.getSubject().getName() + getString(R.string.delete_warning_2_2);
                break;
            case 2:
                message = getString(R.string.delete_warning_3);
                break;
        }
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getContext(), MainActivity.colorScheme);
        } else {
            builder = new AlertDialog.Builder(this.getContext());
        }

        builder.setTitle("Wirklich löschen?")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (i) {
                            case 0:
                                manager.deleteLesson(lesson);
                                //manager.save();
                                initializeTableView();
                                break;
                            case 1:
                                manager.deleteAllLessons(lesson);
                                //manager.save();
                                initializeTableView();
                                break;
                            case 2:
                                manager.deleteAllLessons(lesson);
                                manager.deleteSubject(lesson.getSubject());
                                //manager.save();
                                initializeTableView();
                                break;
                        }
                        bsd.cancel();
                    }
                })
                .setIcon(R.drawable.ic_delete);

        AlertDialog alertDialog = builder.show();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}