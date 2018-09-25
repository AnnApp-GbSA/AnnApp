package com.pax.tk.annapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.pax.tk.annapp.Grade;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.SubjectManager;

import java.util.ArrayList;


public class RVAdapterGradeList extends RecyclerView.Adapter<RVAdapterGradeList.RecyclerVH> {

    Context context;
    private ArrayList<Grade> grades;
    private SubjectManager subjectManager;
    private Subject subject;
    private TextView gradeMessage;

    boolean isWrittenBool;

    private SparseBooleanArray mTogglePositions;

    public RVAdapterGradeList(Context context, Subject subject, TextView gradeMessage, int colorSchemePosition) {
        this.context = context;
        subjectManager = SubjectManager.getInstance();
        subjectManager.load();
        this.subject = subject;
        grades = subject.getAllGrades();
        this.gradeMessage = gradeMessage;
        mTogglePositions = new SparseBooleanArray();

    }

    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_grade_list, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerVH holder, int position) {

        holder.gradeTxt.setText(String.valueOf(grades.get(position).getGrade()));

        if (!grades.get(position).getNote().isEmpty())
            holder.expandableTextView.setText(grades.get(position).getNote() + "\n" + context.getString(R.string.ratingList) + grades.get(position).getRating(), mTogglePositions, position);
        else
            holder.expandableTextView.setText(context.getString(R.string.ratingList) + grades.get(position).getRating(), mTogglePositions, position);


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askDelete(grades.get(position));
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Create InputDialog...");
                createEditDialog(grades.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    //Viewholder Class
    public class RecyclerVH extends RecyclerView.ViewHolder {
        TextView gradeTxt;
        ExpandableTextView expandableTextView;
        ImageButton editButton;
        ImageButton deleteButton;
        RelativeLayout relativeLayout;

        public RecyclerVH(View itemView) {
            super(itemView);
            editButton = itemView.findViewById(R.id.item_grade_button_edit);
            deleteButton = itemView.findViewById(R.id.item_grade_button_delete);
            expandableTextView = itemView.findViewById(R.id.expandable_text_view);
            gradeTxt = itemView.findViewById(R.id.item_grade_grade);
        }
    }


    public void createEditDialog(final Grade grade) {
        BottomSheetDialog bsd = new BottomSheetDialog(context, R.style.NewDialog);


        //View mView = getLayoutInflater().inflate(R.layout.fragment_grade_input, null);
        View mView = View.inflate(context, R.layout.dialog_edit_grade, null);

        final EditText gradeInput = (EditText) mView.findViewById(R.id.edit_gradeInput);
        gradeInput.setText(String.valueOf(grade.getGrade()));

        final EditText ratingInput = (EditText) mView.findViewById(R.id.edit_ratingInput);
        ratingInput.setText(String.valueOf(grade.getRating()));

        final EditText note = (EditText) mView.findViewById(R.id.edit_noteInput);
        note.setText(grade.getNote());

        final ImageView btnHelp = (ImageView) mView.findViewById(R.id.edit_btnRoomHelp);
        final Button btnExtra = (Button) mView.findViewById(R.id.edit_btnExtra);
        final FloatingActionButton btnCancel = mView.findViewById(R.id.edit_grade_btnCancel);
        final FloatingActionButton btnOk = mView.findViewById(R.id.edit_grade_btnOK);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.edit_extraLayout);

        final TextView subjectText = mView.findViewById(R.id.edit_grade_subjectText);
        subjectText.setText(subject.getName());

        final RadioButton isWritten = mView.findViewById(R.id.edit_isWritten);

        final RadioButton isNotWritten = mView.findViewById(R.id.edit_isNotWritten);

        if (grade.iswritten())
            isWritten.setChecked(true);
        else
            isNotWritten.setChecked(true);

        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extraLayout.getVisibility() != View.VISIBLE)
                    extraLayout.setVisibility(View.VISIBLE);
                else
                    extraLayout.setVisibility(View.GONE);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog(context.getString(R.string.rating), context.getString(R.string.ratingExplanation), 0);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });

        bsd.setTitle(context.getString(R.string.editGrade));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = 1;

                //testing which button is active for decision whether your Grade is written or whether it's not
                if (isWritten.isChecked())
                    isWrittenBool = true;
                else if (isNotWritten.isChecked())
                    isWrittenBool = false;

                if (gradeInput.getText().toString().isEmpty()) {
                    createAlertDialog(context.getString(R.string.warning), context.getString(R.string.warningMessage), android.R.drawable.ic_dialog_alert);
                    return;
                }

                if (!ratingInput.getText().toString().isEmpty())
                    rating = Float.parseFloat(ratingInput.getText().toString());

                grade.setGrade(Integer.valueOf(gradeInput.getText().toString()));
                grade.setIswritten(isWrittenBool);
                grade.setRating(rating);
                grade.setNote(note.getText().toString());
                notifyItemChanged(grades.indexOf(grade));

                ((TextView) ((Activity) context).findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));

                bsd.cancel();
            }
        });
                /*.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        float rating = 1;
                        //testing which button is active for decision whether your Grade is written or whether it's not
                        if(isWritten.isChecked())
                            isWrittenBool = true;
                        else if(isNotWritten.isChecked())
                            isWrittenBool = false;
                        if(gradeInput.getText().toString().isEmpty()){
                            createAlertDialog(context.getString(R.string.warning), context.getString(R.string.warningMessage), android.R.drawable.ic_dialog_alert);
                            return;
                        }
                        if(!ratingInput.getText().toString().isEmpty())
                            rating = Float.parseFloat(ratingInput.getText().toString());
                        grade.setGrade(Integer.valueOf(gradeInput.getText().toString()));
                        grade.setIswritten(isWrittenBool);
                        grade.setRating(rating);
                        grade.setNote(note.getText().toString());
                        notifyItemChanged(grades.indexOf(grade));
                        ((TextView)((Activity)context).findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing
                    }
                });
*/
                /*adTrueDialog = ad.show();
                adTrueDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                WindowManager.LayoutParams lp = adTrueDialog.getWindow().getAttributes();
                lp.dimAmount = 0.7f;
                adTrueDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/

        bsd.setContentView(mView);
        bsd.show();
    }

    void createAlertDialog(String title, String text, int ic) {
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, MainActivity.colorScheme);
        } else {
            builder = new AlertDialog.Builder(context);
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

    public void askDelete(final Grade grade) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, MainActivity.colorScheme);

        builder.setTitle(R.string.deleteQuestion)
                .setMessage(context.getString(R.string.deleteQuestionMessage))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(grade);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //I think - do Nothing - but if you want
                    }
                })
                .setIcon(android.R.drawable.ic_delete);

        AlertDialog askDialog = builder.show();
        askDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = askDialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        askDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    public void delete(Grade grade) {
        int formerIndex = grades.indexOf(grade);
        grade.getSubject().removeGrade(grade);
        try {
            subject.removeGrade(grade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            grades.remove(grade);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyItemRemoved(formerIndex);
        ((TextView) ((Activity) context).findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
        if (grades.isEmpty()) {
            gradeMessage.setVisibility(View.VISIBLE);
        }

        subjectManager.save();
    }

    public void addGrade(Grade grade) {
        if (grades.contains(grade))
            notifyItemInserted(grades.indexOf(grade));
    }
}