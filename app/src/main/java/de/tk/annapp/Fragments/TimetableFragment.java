package de.tk.annapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

import de.tk.annapp.Day;
import de.tk.annapp.Lesson;
import de.tk.annapp.MainActivity;
import de.tk.annapp.R;
import de.tk.annapp.Subject;
import de.tk.annapp.SubjectManager;
import de.tk.annapp.Task;
import de.tk.annapp.Util;

import static android.R.layout.simple_spinner_dropdown_item;
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

    SubjectManager subjectManager;

    public static final String TAG = "TimetableFragment";

    public TimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uglyAsHellWayToCreateAOtherCoiseOption.setName(getString(R.string.newSubject));
        //setRowSize();
        //initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.timetable));
        View root = inflater.inflate(R.layout.fragment_timetable, container, false);
        subjectManager = SubjectManager.getInstance();

        if (!subjectManager.getSubjects().isEmpty())
            lastSubject = subjectManager.getSubjects().get(0);
        else
            lastSubject = uglyAsHellWayToCreateAOtherCoiseOption;
        //RelativeLayout fragment_container = root.findViewById(R.id.fragment_container);

        tableLayout = root.findViewById(R.id.tableLayout);

        SharedPreferences sp = getContext().getSharedPreferences("prefs", MODE_PRIVATE);

        dividers = sp.getBoolean(getString(R.string.key_timetableGaps), false);

        HorizontalScrollView sv = root.findViewById(R.id.background);

        /*if (dividers) {
            sv.setBackgroundColor(Color.parseColor("#DADADA"));
        } else {
            sv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }*/

        if(!dividers)
            sv.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        initializeTableView();
        return root;
    }


    void initializeTableView() {

        tableLayout.removeAllViews();

        int u;
        System.out.println(subjectManager.getLongestDaysLessons());
        if ((int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_maxLesson), 11) > subjectManager.getLongestDaysLessons())
            u = (int) getActivity().getPreferences(MODE_PRIVATE).getInt(getString(R.string.key_maxLesson), 11);
        else
            u = subjectManager.getLongestDaysLessons();

        for (int i = 0; i < u; i++) {
            TableRow tableRow = new TableRow(this.getContext());
            tableRow.setPadding(0, 0, 0, spacing);


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
                        subjectManager.getDays()) {

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

    TextView getSpaceButton() {
        TextView btn = new TextView(this.getContext());
        btn.setWidth(spacing);
        btn.setMinWidth(spacing);
        btn.setMaxWidth(spacing);
        return btn;
    }

    Button getHeaderButton(int colorScheme) {
        Button btn = new Button(this.getContext());

        btn.setTextColor(getResources().getColor(android.R.color.white));

        TypedValue a = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorTimetableHeader, a, true);

        btn.setBackgroundColor(a.data);

        btn.setTypeface(null, Typeface.BOLD);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Clicked ", Toast.LENGTH_SHORT).show();
            }
        });

        return btn;
    }

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
                addLesson(view);
                return false;
            }
        });
        return btn;
    }

    void addLesson(View view) {
        //get time out of tag
        String[] s = view.getTag().toString().split("#");
        int x = Integer.valueOf(s[0]);
        int y = Integer.valueOf(s[1]);

        //subjectManager.setLesson(subjectManager.getSubjects().get(1), "", y, x-1);

        createNewLessonDialog(x - 1, y, null, false);

    }

    void lessonInfo(View view) {
        String[] s = view.getTag().toString().split("#");
        int x = Integer.valueOf(s[0]) - 1;
        int y = Integer.valueOf(s[1]);

        createLessonInfoDialog(x, y, subjectManager.getDays()[x].getLesson(y).getSubject());
    }

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
                createAlertDialog(getString(R.string.room), getString(R.string.room_warning), 0);
            }
        });


        final Spinner subjectSelection = (Spinner) mView.findViewById(R.id.subjectSelection);

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
        ArrayList<Subject> spinnerlist = (ArrayList<Subject>) subjectManager.getSubjects().clone();
        spinnerlist.add(uglyAsHellWayToCreateAOtherCoiseOption);


        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(this.getContext(), R.layout.spinner_item, spinnerlist);
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
                        createAlertDialog(getString(R.string.name_warning), getString(R.string.name_warning_empty), 0);
                        return;
                    }
                    oCSubject = new Subject(nameEdittext.getText().toString(),
                            radioBtn1.isChecked() ? 1 : 2,
                            teacherEdittext.getText().toString(),
                            roomInput.getText().toString());
                    if (subjectManager.getSubjects().contains(oCSubject)) {
                        createAlertDialog(getString(R.string.name_warning), getString(R.string.name_warning_dup) + oCSubject.getName() + "\"", 0);
                        return;
                    }
                    subjectManager.addSubject(oCSubject);
                } else if (nameEdittext.getText().toString().equals(selectedsubject.getName()) &
                        teacherEdittext.getText().toString().equals(selectedsubject.getTeacher()) &
                        (radioBtn1.isChecked() ? 1 : 2) == selectedsubject.getRatingSub()) {
                    oCSubject = selectedsubject;
                } else {
                    if (nameEdittext.getText().toString().equals("")) {
                        createAlertDialog(getString(R.string.name_warning), getString(R.string.name_warning_empty), 0);
                        return;
                    }
                    selectedsubject.setName(nameEdittext.getText().toString());
                    selectedsubject.setRatingSub(radioBtn1.isChecked() ? 1 : 2);
                    selectedsubject.setTeacher(teacherEdittext.getText().toString());
                    selectedsubject.setRoom(roomInput.getText().toString());
                    oCSubject = selectedsubject;
                }

                System.out.println(oCSubject + "\\" + (roomInput.getText().toString().isEmpty() ? null : roomInput.getText().toString()) + "\\" + day + "\\" + time);
                subjectManager.setLesson(new Lesson(oCSubject, roomInput.getText().toString().isEmpty() ? null : roomInput.getText().toString(), day, time));

                initializeTableView();

                bsd.cancel();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //subjectManager.setLesson(new Lesson(null,null, day, time));
                //initializeTableView();
                bsd.cancel();
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }


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

        roomView.setText(subjectManager.getDays()[day].getLesson(time).getRoom());

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
                createDeleteQuestionDialog(subjectManager.getDays()[day].getLesson(time));

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
                createEditQuestionDialog(subjectManager.getDays()[day].getLesson(time));
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

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
                .setIcon(ic);

        AlertDialog alertDialog = builder.show();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

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
                                subjectManager.deleteLesson(lesson);
                                subjectManager.save();
                                initializeTableView();
                                break;
                            case 1:
                                subjectManager.deleteAllLessons(lesson);
                                subjectManager.save();
                                initializeTableView();
                                break;
                            case 2:
                                subjectManager.deleteAllLessons(lesson);
                                subjectManager.deleteSubject(lesson.getSubject());
                                subjectManager.save();
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

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
            System.out.println("-----------------------------------------set Margins");
        }
    }

}

