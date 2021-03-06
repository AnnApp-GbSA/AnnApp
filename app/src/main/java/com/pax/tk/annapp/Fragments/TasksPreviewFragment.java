package com.pax.tk.annapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Adapter.RVAdapterTaskList;
import com.pax.tk.annapp.SchoolLessonSystem;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Task;
import com.pax.tk.annapp.Util;

import static android.R.layout.simple_spinner_dropdown_item;

//TODO Is this really necessary or can be channged to TaskFragment?
public class TasksPreviewFragment extends Fragment {
    View root;
    private Manager manager;
    RecyclerView recyclerView;

    public static final String TAG = "HomeTaskFragment";

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

        root = inflater.inflate(R.layout.fragment_tasks, container, false);

        manager = Manager.getInstance();

        FloatingActionButton fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAddTask);
        fabAdd.setVisibility(View.GONE);

        TextView taskMessage = (TextView) root.findViewById(R.id.noTask);

        for (Subject s :
                manager.getSubjects()) {
            if(!s.getAllTasks().isEmpty()){
                taskMessage.setVisibility(View.INVISIBLE);
            }
        }

        recyclerView = root.findViewById(R.id.recyclerViewTasksId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new RVAdapterTaskList(getActivity(), taskMessage, true));

        return root;
    }

    /**
     * recreates the activity
     *
     * @param a activity to recreate
     */
    @SuppressLint("NewApi")
    public static final void recreateActivityCompat(final Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
        } else {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
    }

    /**
     * creates an input dialog, sets it up and calls methods
     */
    public void createInputDialog() {


        final BottomSheetDialog bsd = new BottomSheetDialog(this.getContext(),R.style.NewDialog);

        View mView = View.inflate(this.getContext(), R.layout.dialog_new_task, null); //TODO EInes der Layouts elemenieren

        String[] duedates = new String[]{getString(R.string.nextLesson), getString(R.string.next2Lesson), getString(R.string.tomorrow), getString(R.string.nextWeek), getString(R.string.chooseDate)};

        String[] kinds = new String[]{getString(R.string.homework), getString(R.string.exam), getString(R.string.note)};

        final EditText task = (EditText) mView.findViewById(R.id.spinner_task_input_task);
        final ArrayList<Subject> subjects = manager.getSubjects();

        if (subjects.isEmpty()) {
            Util.createAlertDialog(getString(R.string.warning), getString(R.string.addSubjectMessage), android.R.drawable.ic_dialog_alert, getContext());
            return;
        }

        final Spinner subjectSelection = (Spinner) mView.findViewById(R.id.spinner_task_input_subject);
        ArrayAdapter<Subject> adapterSubject = new ArrayAdapter<>(getContext(), simple_spinner_dropdown_item, subjects);
        subjectSelection.setAdapter(adapterSubject);

        final Spinner timeSelection = (Spinner) mView.findViewById(R.id.spinner_task_input_time);
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(this.getContext(), simple_spinner_dropdown_item, duedates);
        timeSelection.setAdapter(adapterTime);
        timeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (((String) timeSelection.getItemAtPosition(i)).equals(getString(R.string.chooseDate))) {
                    timeSelection.setSelection(0);
                    Calendar date = Calendar.getInstance();
                    //TODO Has to be addapted to other notations
                    if (((String) timeSelection.getItemAtPosition(i - 1)).matches(getString(R.string.dateFormat_pattern))) {
                        date = Util.getCalendarFromFullString(((String) timeSelection.getItemAtPosition(i - 1)));
                    }
                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            String[] pos = new String[]{getString(R.string.nextLesson), getString(R.string.next2Lesson), getString(R.string.tomorrow), getString(R.string.nextWeek), dayOfMonth + "." + (monthOfYear+1) + "." + year, getString(R.string.chooseDate)};
                            ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(getContext(), simple_spinner_dropdown_item, pos);
                            timeSelection.setAdapter(adapterTime);
                            timeSelection.setSelection(4);
                        }
                    };
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            getContext(), onDateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setTitle(getString(R.string.chooseDate));
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final Spinner kindSelection = (Spinner) mView.findViewById(R.id.spinner_task_input_kind);
        ArrayAdapter<String> adapterKind = new ArrayAdapter<String>(this.getContext(), simple_spinner_dropdown_item, kinds);
        kindSelection.setAdapter(adapterKind);


        FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });


        FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (task.getText().toString().isEmpty()) {
                    Util.createAlertDialog(getString(R.string.warning), getString(R.string.warningMessage), android.R.drawable.ic_dialog_alert, getContext());
                    return;
                }

                Subject subject = (Subject) subjectSelection.getSelectedItem();

                Calendar due = Calendar.getInstance();//"Nächste Stunde","Übernächste Stunde","Morgen","Nächste Woche",
                Calendar now = Calendar.getInstance();

                SchoolLessonSystem sls = manager.getSchoolLessonSystem();
                if (timeSelection.getSelectedItem().toString().equals(getString(R.string.nextLesson))) {
                    due = subject.getNextLessonAfter(due,sls);
                } else if (timeSelection.getSelectedItem().toString().equals(getString(R.string.next2Lesson))) {
                    due = subject.getNextLessonAfter(subject.getNextLessonAfter(due,sls),sls);
                } else if (timeSelection.getSelectedItem().toString().equals(getString(R.string.tomorrow))) {
                    due.add(Calendar.DAY_OF_YEAR, 1);
                } else if (timeSelection.getSelectedItem().toString().equals(getString(R.string.nextWeek))) {
                    due.add(Calendar.WEEK_OF_YEAR, 1);
                } else if (timeSelection.getSelectedItem().toString().matches(getString(R.string.dateFormat_pattern))) {
                    due = Util.getCalendarFromFullString(timeSelection.getSelectedItem().toString());
                } else {
                    Util.createAlertDialog(/*getString(R.string.warning)*/getString(R.string.warning), getString(R.string.appRestartMessage), android.R.drawable.ic_dialog_alert, getContext());
                    return;
                }

                String shortKind;
                if (kindSelection.getSelectedItem().toString().equals(getString(R.string.homework))) {
                    shortKind = getString(R.string.homework_short);
                } else if (kindSelection.getSelectedItem().toString().equals(getString(R.string.exam))) {
                    shortKind = getString(R.string.exam_short);
                } else if (kindSelection.getSelectedItem().toString().equals(getString(R.string.note))) {
                    shortKind = getString(R.string.note_short);
                } else {
                    shortKind = "";
                    Util.createAlertDialog(getString(R.string.warning), getString(R.string.appRestartMessage), android.R.drawable.ic_dialog_alert, getContext());
                }
                Task newTask = new Task(task.getText().toString(), Calendar.getInstance(), shortKind, subject, due);
                subject.addTask(newTask);
                ((RVAdapterTaskList) recyclerView.getAdapter()).addTask(newTask);
                root.findViewById(R.id.noTask).setVisibility(View.GONE);
                //manager.save();
                bsd.cancel();
            }
        });


        bsd.setTitle(R.string.addTask);
                /*.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (task.getText().toString().isEmpty()) {
                            createAlertDialog(getString(R.string.warning), getString(R.string.warningMessage), android.R.drawable.ic_dialog_alert);
                            return;
                        }

                        Subject subject = (Subject) subjectSelection.getSelectedItem();
                        Calendar due = Calendar.getInstance();//"Nächste Stunde","Übernächste Stunde","Morgen","Nächste Woche",
                        Calendar now = Calendar.getInstance();

                        SchoolLessonSystem sls = manager.getSchoolLessonSystem();
                        if (timeSelection.getSelectedItem().toString().equals("Nächste Stunde")) {
                            due = subject.getNextLessonAfter(due,sls);
                        } else if (timeSelection.getSelectedItem().toString().equals("Übernächste Stunde")) {
                            due = subject.getNextLessonAfter(subject.getNextLessonAfter(due,sls),sls);
                        } else if (timeSelection.getSelectedItem().toString().equals("Morgen")) {
                            due.add(Calendar.DAY_OF_YEAR, 1);
                        } else if (timeSelection.getSelectedItem().toString().equals("Nächste Woche")) {
                            due.add(Calendar.WEEK_OF_YEAR, 1);
                        } else if (timeSelection.getSelectedItem().toString().matches("\\d*\\.\\d*\\.\\d*")) {
                            due = Util.getCalendarFromFullString(timeSelection.getSelectedItem().toString());
                        } else {
                            createAlertDialog(/*getString(R.string.warning)*//*"Achtung", "Bitte starten sie die App neu. Ein Fehler ist aufgetreten.", android.R.drawable.ic_dialog_alert);
                            return;
                        }

                        String shortKind;
                        if (kindSelection.getSelectedItem().toString().equals("Hausaufgabe")) {
                            shortKind = "HA";
                        } else if (kindSelection.getSelectedItem().toString().equals("Schulaufgabe")) {
                            shortKind = "SA";
                        } else if (kindSelection.getSelectedItem().toString().equals("Notiz")) {
                            shortKind = "No";
                        } else {
                            shortKind = "";
                            createAlertDialog(getString(R.string.warning), "Bitte starten sie die App neu. Ein Fehler ist aufgetreten.", android.R.drawable.ic_dialog_alert);
                        }
                        Task newTask = new Task(task.getText().toString(), Calendar.getInstance(), shortKind, subject, due);
                        subject.addTask(newTask);
                        ((RVAdapterTaskList) recyclerView.getAdapter()).addTask(newTask);
                        root.findViewById(R.id.noTask).setVisibility(View.GONE);
                        manager.save();
                    }
                })*/
                bsd.setContentView(mView);
                bsd.show();
    }
}
