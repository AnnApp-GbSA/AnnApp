package com.pax.tk.annapp.Adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.pax.tk.annapp.Grade;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Util;

import java.util.ArrayList;


public class RVAdapterGradeList extends RecyclerView.Adapter<RVAdapterGradeList.RecyclerVH> {

    Context context;
    private ArrayList<Grade> grades;
    private Manager manager;
    private Subject subject;
    private TextView gradeMessage;

    boolean isWrittenBool;

    private SparseBooleanArray expandedState = new SparseBooleanArray();

    /**
     * creates a adapter grade list
     *
     * @param context ...
     * @param subject subject of the grades
     * @param gradeMessage TextView of the grades
     */
    public RVAdapterGradeList(Context context, Subject subject, TextView gradeMessage) {
        this.context = context;
        manager = Manager.getInstance();
        this.subject = subject;
        grades = subject.getAllGrades();
        this.gradeMessage = gradeMessage;

        for (int i = 0; i < grades.size(); i++){
            expandedState.append(i, false);
        }
    }

    /**
     * creates the ViewHolder
     *
     * @param parent parent ViewGroup
     * @param viewType type of the view
     * @return created ViewHolder
     */
    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_grade_list, parent, false);
        return new RecyclerVH(view);
    }

    /**
     * binds the ViewHolder
     *
     * @param holder ViewHolder
     * @param position position as int
     */
    @Override
    public void onBindViewHolder(RecyclerVH holder, int position) {
        Grade grade = grades.get(position);
        holder.cardView.setCardBackgroundColor(Util.getSubjectColor(context, subject));
        holder.gradeTxt.setTypeface(Typeface.DEFAULT_BOLD);
        holder.gradeTxt.setText(Integer.toString(grade.getGrade()));

        String written;
        if (grade.iswritten()) {
            written = context.getResources().getString(R.string.written);
        } else {
            written = context.getResources().getString(R.string.oral);
        }

        String noteTxt;
        String shortText;

        if (!grade.getNote().isEmpty()) {
            noteTxt = grade.getNote() + "\n" + context.getResources().getString(R.string.rating) + ": " + grade.getRating() + "\n" + written;
            holder.noteTxt.setTag(noteTxt);

            if (grade.getNote().length() >= 20) {
                shortText = grade.getNote().substring(0, 20) + "...";
            } else {
                shortText = grade.getNote();
            }

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.noteTxt.getText().equals(shortText) && noteTxt != null){
                        holder.noteTxt.setText(noteTxt);
                        changeRotate(holder.arrowButton, 0f, 180f).start();
                    }else if(holder.noteTxt.getText().equals(noteTxt) && noteTxt != null){
                        holder.noteTxt.setText(shortText);
                        changeRotate(holder.arrowButton, 180f, 0f).start();
                    }
                }
            });

            holder.arrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.noteTxt.getText().equals(shortText) && noteTxt != null){
                        holder.noteTxt.setText(noteTxt);
                        changeRotate(holder.arrowButton, 0f, 180f).start();
                    }else if(holder.noteTxt.getText().equals(noteTxt) && noteTxt != null){
                        holder.noteTxt.setText(shortText);
                        changeRotate(holder.arrowButton, 180f, 0f).start();
                    }
                }
            });

        } else {
            holder.arrowButton.setVisibility(View.GONE);
            shortText = context.getResources().getString(R.string.rating) + ": " + grade.getRating() + ", " + written;
        }

        holder.noteTxt.setText(shortText);



        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askDelete(grades.get(holder.getAdapterPosition()));
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Create InputDialog...");
                createEditDialog(grades.get(holder.getAdapterPosition()));
            }
        });
    }

    /**
     * gets the size of the grades
     *
     * @return size
     */
    @Override
    public int getItemCount() {
        return grades.size();
    }

    public class RecyclerVH extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView noteTxt;
        TextView gradeTxt;
        ImageButton editButton;
        ImageButton deleteButton;
        LinearLayout itemLayout;
        ImageButton arrowButton;

        public RecyclerVH(View itemView){
            super(itemView);

            arrowButton = itemView.findViewById(R.id.item_grade_arrow);
            itemLayout = itemView.findViewById(R.id.layout_grade);
            cardView = itemView.findViewById(R.id.cardView_grades);
            editButton = itemView.findViewById(R.id.item_grade_button_edit);
            deleteButton = itemView.findViewById(R.id.item_grade_button_delete);
            noteTxt = itemView.findViewById(R.id.item_grade_note);
            gradeTxt = itemView.findViewById(R.id.item_grade_grade);
        }
    }

    /**
     * creates an edit dialog for a grade
     *
     * @param grade grade to be edited
     */
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
                Util.createAlertDialog(context.getString(R.string.rating), context.getString(R.string.ratingExplanation), 0, context);
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
                    Util.createAlertDialog(context.getString(R.string.warning), context.getString(R.string.warningMessage), android.R.drawable.ic_dialog_alert, context);
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

    /*void createAlertDialog(String title, String text, int ic) {
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
    }*/

    /**
     * ask to delete a grade
     *
     * @param grade grade which shall be deleted
     */
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

    /**
     * deletes a grade
     *
     * @param grade grade to delete
     */
    public void delete(Grade grade) {
        int formerIndex = grades.indexOf(grade);
        grades.remove(formerIndex);
        grade.getSubject().removeGrade(grade);
        notifyItemRemoved(formerIndex);
        ((TextView) ((Activity) context).findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
        if (grades.isEmpty()) {
            gradeMessage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * adds a grade to the list
     *
     * @param grade grade to add
     */
    public void addGrade(Grade grade) {
        if (grades.contains(grade))
            notifyItemInserted(grades.indexOf(grade));
    }

    /**
     * changes the Rotation
     *
     * @param button button
     * @param to to what shall be rotated
     * @param from from where shall be rotated
     * @return object animator
     */
    private ObjectAnimator changeRotate(ImageButton button, float to, float from){
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", from, to);
        animator.setDuration(200);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

}



