package com.pax.tk.annapp.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.SchoolLessonSystem;
import com.pax.tk.annapp.Task;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Util;

import static android.R.layout.simple_spinner_dropdown_item;

public class RVAdapterTaskList extends RecyclerView.Adapter<RVAdapterTaskList.RecyclerVHTask> {
    private Context context;
    private ArrayList<Task> tasks = new ArrayList<>();
    private Manager manager;
    private TextView taskMessage;
    private int pos;
    private boolean isPreview;
    public Set<Event> privateEvents = new HashSet<>();

    public RVAdapterTaskList(Context context, TextView taskMessage, boolean isPreview) {

        this.context = context;
        this.taskMessage = taskMessage;
        manager = Manager.getInstance();
        this.isPreview = isPreview;

        privateEvents = manager.getPrivateEvents();
        constructor();

    }

    void constructor() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);


        pos = -1;

        tasks.clear();
        ArrayList<Subject> subjects = manager.getSubjects();


        for (Subject s : subjects) {
            if (s.getAllTasks().isEmpty())
                continue;
            tasks.add(null);
            pos++;
            s.setPosition(pos);
            for (Task t : s.getAllTasksSorted()) {

                tasks.add(t); /*if (t.getDue().before(yesterday)) { The deletingstuff has to happen somewhere else
                    askDelete(t);
                    Toast.makeText(context,"Old Tasks were deleted",Toast.LENGTH_SHORT).show();
                    continue;
                }*/
                pos++;
            }
        }

        /*if(!tasks.isEmpty()){
            for (Task t : tasks){
                if (t.getDate().getTimeInMillis() < System.currentTimeMillis() + 518400000) {
                    Task tas = new Task(t.getTask(), t.getDate(), t.getKind(), t.getSubject(), t.getDue());
                    tasks.remove(t);
                    tasks.add(tas); //TODO Test
                }
            }
        }*/

        if (tasks.isEmpty()) {// Default if nothing is added
            //tasks.add(new Task(null, null, null, null, context.getString(R.string.insertTask)));
        }
    }

    @Override
    public RecyclerVHTask onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (!isPreview)
            v = LayoutInflater.from(context).inflate(R.layout.item_task_list, parent, false);
        else
            v = LayoutInflater.from(context).inflate(R.layout.item_task_preview_list, parent, false);
        return new RecyclerVHTask(v);
    }

    @Override
    public void onBindViewHolder(RecyclerVHTask holder, int position) {
        if (tasks.get(position) == null) {
            holder.content.setVisibility(View.GONE);
            holder.subjectTxt.setVisibility(View.VISIBLE);
            holder.subjectTxt.setText(tasks.get(position + 1).getSubject().getName());
            holder.space.setVisibility(View.GONE);
            holder.cardViewTask.setCardBackgroundColor(new Util().getSubjectColor(context, tasks.get(position + 1).getSubject()));
            holder.subjectTxt.setTextColor(context.getColor(android.R.color.white));
            return;
        } else {

            holder.content.setVisibility(View.VISIBLE);
            holder.subjectTxt.setVisibility(View.GONE);
            holder.space.setVisibility(View.VISIBLE);
        }
        holder.dateTxt.setText(Util.getDateString(tasks.get(position).getDue(), context)); //When the Task is due
        holder.taskTxt.setText(tasks.get(position).getTask());
        holder.kindTxt.setText(tasks.get(position).getKind());

        if (!isPreview) {
            holder.editButton.setOnClickListener(new OnEditListener(tasks.get(position)));
            holder.deleteButton.setOnClickListener(new OnDeleteListener(tasks.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private class OnEditListener implements View.OnClickListener {

        private Task t;

        public OnEditListener(Task t) {
            this.t = t;
        }

        @Override
        public void onClick(View v) {
            createEditDialog(t);
        }
    }

    private class OnDeleteListener implements View.OnClickListener {

        private Task t;

        public OnDeleteListener(Task t) {
            this.t = t;
        }

        @Override
        public void onClick(View v) {
            askDelete(t);
        }
    }

    //Viewholder Class
    public class RecyclerVHTask extends RecyclerView.ViewHolder {
        TextView taskTxt;
        TextView dateTxt;
        TextView kindTxt;
        TextView subjectTxt;
        ImageButton editButton;
        ImageButton deleteButton;
        View content;
        Button space;
        CardView cardViewTask;

        public RecyclerVHTask(View itemView) {
            super(itemView);
            dateTxt = itemView.findViewById(R.id.item_task_date);
            taskTxt = itemView.findViewById(R.id.item_task_task);
            kindTxt = itemView.findViewById(R.id.item_task_kind);
            subjectTxt = itemView.findViewById(R.id.item_task_subject);
            editButton = itemView.findViewById(R.id.button_item_task_edit);
            deleteButton = itemView.findViewById(R.id.button_item_task_delete);
            content = itemView.findViewById(R.id.relativeLayout_item_task_content);
            space = itemView.findViewById(R.id.space);
            cardViewTask = itemView.findViewById(R.id.cardViewTask);
        }
    }

    public void createEditDialog(final Task task) {
        BottomSheetDialog bsd = new BottomSheetDialog(context, R.style.NewDialog);

        //Find a method to get the current Theme Resource ID

        View mView = View.inflate(context, R.layout.dialog_edit_task, null);

        String eventText = task.getKind() + ": " + task.getTask();
        String uid = String.valueOf(eventText.hashCode());

        removeTaskEvent(uid);

        Calendar now = Calendar.getInstance();
        String[] duedates;
        if (task.getDue().get(Calendar.YEAR) == now.get(Calendar.YEAR) & task.getDue().get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR))
            duedates = new String[]{"Nächste Stunde","Übernächste Stunde","Morgen","Nächste Woche", "Datum auswählen"};
        else
            duedates = new String[]{"Nächste Stunde","Übernächste Stunde","Morgen","Nächste Woche", Util.getFullDate(task.getDue()), "Datum auswählen"};

        duedates = new String[]{context.getString(R.string.nextLesson), context.getString(R.string.next2Lesson), context.getString(R.string.tomorrow), context.getString(R.string.nextWeek), Util.getFullDate(task.getDue()), context.getString(R.string.chooseDate)};

        String[] kinds = new String[]{context.getString(R.string.homework), context.getString(R.string.exam), context.getString(R.string.note)};

        final EditText taskInput = (EditText) mView.findViewById(R.id.edit_taskInput);
        taskInput.setText(String.valueOf(task.getTask()));

        final TextView subjectName = mView.findViewById(R.id.edit_grade_subjectText);
        subjectName.setText(task.getSubject().getName());

        final FloatingActionButton cancelBtn = mView.findViewById(R.id.edit_task_btnCancel);
        final FloatingActionButton okBtn = mView.findViewById(R.id.edit_task_btnOK);

        final Spinner kindSelection = (Spinner) mView.findViewById(R.id.edit_spinner_task_input_kind);
        ArrayAdapter<String> adapterKind = new ArrayAdapter<String>(context, simple_spinner_dropdown_item, kinds);
        kindSelection.setAdapter(adapterKind);
        if (task.getKind().equals(context.getString(R.string.homework_short))) {
            kindSelection.setSelection(0);

        } else if (task.getKind().equals(context.getString(R.string.exam_short))) {
            kindSelection.setSelection(1);

        } else if (task.getKind().equals(context.getString(R.string.note_short))) {
            kindSelection.setSelection(2);

        }

        final Spinner timeSelection = (Spinner) mView.findViewById(R.id.edit_spinner_task_input_time);
        ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(context, simple_spinner_dropdown_item, duedates);
        timeSelection.setAdapter(adapterTime);
        timeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (((String) timeSelection.getItemAtPosition(pos)).equals("Datum auswählen")) {
                    timeSelection.setSelection(0);
                    Calendar date = Calendar.getInstance();
                    if (((String) timeSelection.getItemAtPosition(pos - 1)).matches("\\d*\\.\\d*\\.\\d*")) {
                        date = Util.getCalendarFromFullString(((String) timeSelection.getItemAtPosition(pos - 1)));
                    }
                    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            String[] pos = new String[]{"Nächste Stunde", "Übernächste Stunde", "Morgen", "Nächste Woche", dayOfMonth + "." + (monthOfYear + 1) + "." + year, "Datum auswählen"};
                            ArrayAdapter<String> adapterTime = new ArrayAdapter<String>(context, simple_spinner_dropdown_item, pos);
                            timeSelection.setAdapter(adapterTime);
                            timeSelection.setSelection(4);
                        }
                    };
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            context, onDateSetListener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.setTitle("Datum auswählen");
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        timeSelection.setSelection(4);

        bsd.setTitle(context.getString(R.string.editTask));


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskInput.getText().toString().isEmpty()) {
                    Util.createAlertDialog(context.getString(R.string.warning), context.getString(R.string.warningMessage), android.R.drawable.ic_dialog_alert, context);
                    return;
                }

                String shortKind;//TODO change this... somehow
                String s = (String) kindSelection.getSelectedItem();
                if (s.equals(context.getString(R.string.homework))) {
                    shortKind = context.getString(R.string.homework_short);
                } else if (s.equals(context.getString(R.string.exam))) {
                    shortKind = context.getString(R.string.exam_short);
                } else if (s.equals(context.getString(R.string.note))) {
                    shortKind = context.getString(R.string.note_short);
                } else {
                    shortKind = context.getString(R.string.error);
                }

                Calendar due = Calendar.getInstance();
                SchoolLessonSystem sls = manager.getSchoolLessonSystem();
                if (timeSelection.getSelectedItem().toString().equals("Nächste Stunde")) {
                    due = task.getSubject().getNextLessonAfter(due, sls);
                } else if (timeSelection.getSelectedItem().toString().equals("Übernächste Stunde")) {
                    System.out.println(due);
                    due = task.getSubject().getNextLessonAfter(task.getSubject().getNextLessonAfter(due, sls), sls);
                } else if (timeSelection.getSelectedItem().toString().equals("Morgen")) {
                    due.add(Calendar.DAY_OF_YEAR, 1);
                } else if (timeSelection.getSelectedItem().toString().equals("Nächste Woche")) {
                    due.add(Calendar.WEEK_OF_YEAR, 1);
                } else if (timeSelection.getSelectedItem().toString().matches("\\d*\\.\\d*\\.\\d*")) {
                    due = Util.getCalendarFromFullString(timeSelection.getSelectedItem().toString());
                } else {
                    Util.createAlertDialog(context.getString(R.string.warning) + "Achtung", "Bitte starten sie die App neu. Ein Fehler ist aufgetreten.", android.R.drawable.ic_dialog_alert, context);
                    return;
                }

                due.set(Calendar.DAY_OF_MONTH, due.getTime().getDate() - 1);
                due.set(Calendar.HOUR_OF_DAY, 24);
                due.set(Calendar.MINUTE, 0);


                String eventText = shortKind + ": " + taskInput.getText().toString();
                String uid = String.valueOf(eventText.hashCode());
                Event event = new Event(Color.CYAN, due.getTimeInMillis(),  due.getTimeInMillis() + "°°" + "°°" + eventText + "°°" + uid);
                manager.addPrivateEvent(event);


                task.setDue(due);
                task.setKind(shortKind);
                task.setTask(taskInput.getText().toString());
                notifyItemChanged(tasks.indexOf(task));
                int altindex = tasks.indexOf(task);
                constructor();
                int newindex = tasks.indexOf(task);
                notifyItemMoved(altindex, newindex);

                bsd.cancel();
            }
        });

        bsd.setContentView(mView);
        bsd.show();
    }


    void createAlertDialog(String title, String text, int ic) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, MainActivity.colorScheme);

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

    public void askDelete(final Task task) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, MainActivity.colorScheme);

        builder.setTitle(R.string.deleteQuestion)
                .setMessage(context.getString(R.string.deleteQuestionMessageTask))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(task);
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

    private void delete(Task task) {
        int index = tasks.indexOf(task);
        task.getSubject().removeTask(task);
        if (task.getSubject().getAllTasks().isEmpty()) {
            tasks.remove(index - 1);
            notifyItemRemoved(index - 1);
            index--;
        }

        tasks.remove(task);
        notifyItemRemoved(index);

        String eventText = task.getKind() + ": " + task.getTask();
        String uid = String.valueOf(eventText.hashCode());

        removeTaskEvent(uid);

        if (tasks.isEmpty()) {
            taskMessage.setVisibility(View.VISIBLE);
        }
    }

    public void addTask(Task task) {
        constructor();
        if (tasks.contains(task)) {
            int index = tasks.indexOf(task);
            if (task.getSubject().getAllTasks().size() == 1) {
                notifyItemInserted(index - 1);
                notifyItemInserted(index);
            } else {
                notifyItemInserted(index);
            }


        }
    }

    private void removeTaskEvent(String uid){
        for(Event e : privateEvents){
            String[] split = e.getData().toString().split(Pattern.quote("°°"));
            String id = split[3];
            if(uid.equals(id)){
                manager.removePrivateEvent(e);
                break;
            }
        }
    }
}
