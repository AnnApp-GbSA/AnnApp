package de.tk.annapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import de.tk.annapp.Fragments.GradeChildFragment;
import de.tk.annapp.MainActivity;
import de.tk.annapp.NewsDetailActivity;
import de.tk.annapp.R;
import de.tk.annapp.Subject;
import de.tk.annapp.SubjectManager;
import de.tk.annapp.Grade;

public class RVAdapterGradeList extends RecyclerView.Adapter<RVAdapterGradeList.RecyclerVH> {

    Context context;
    private ArrayList<Grade> grades;
    private SubjectManager subjectManager;
    private Subject subject;
    private TextView gradeMessage;

    boolean isWrittenBool;
    AlertDialog adTrueDialog;

    public RVAdapterGradeList(Context context, Subject subject, TextView gradeMessage){
        this.context = context;
        subjectManager = SubjectManager.getInstance();
        this.subject = subject;
        grades = subject.getAllGrades();
        this.gradeMessage = gradeMessage;
    }

    @Override
    public RecyclerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_grade_list, parent, false);
        return new RecyclerVH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerVH holder, final int position) {
        holder.gradeTxt.setText(String.valueOf(grades.get(position).getGrade()));

        if(!grades.get(position).getNote().isEmpty())
            holder.expandableTextView.setText(grades.get(position).getNote() + "\n" +  context.getString(R.string.ratingList) + grades.get(position).getRating());
        else
            holder.expandableTextView.setText(context.getString(R.string.ratingList) + grades.get(position).getRating());

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
    public class RecyclerVH extends RecyclerView.ViewHolder{
        TextView gradeTxt;
        ExpandableTextView expandableTextView;
        ImageButton editButton;
        ImageButton deleteButton;

        public RecyclerVH(View itemView){
            super(itemView);
            editButton = itemView.findViewById(R.id.item_grade_button_edit);
            deleteButton = itemView.findViewById(R.id.item_grade_button_delete);
            expandableTextView = itemView.findViewById(R.id.expandable_text_view);
            gradeTxt = itemView.findViewById(R.id.item_grade_grade);
        }
    }




    public void createEditDialog(final Grade grade){

        AlertDialog.Builder ad = new  AlertDialog.Builder(context);



        //View mView = getLayoutInflater().inflate(R.layout.fragment_grade_input, null);
        View mView = View.inflate(context, R.layout.dialog_edit_grade, null);

        final TextView gradeEditTitle = mView.findViewById(R.id.grade_edit_title);
        gradeEditTitle.setText(context.getString(R.string.editGrade));

        final EditText gradeInput = (EditText) mView.findViewById(R.id.gradeInput);
        gradeInput.setText(String.valueOf(grade.getGrade()));

        final  EditText ratingInput =(EditText) mView.findViewById(R.id.ratingInput);
        ratingInput.setText(String.valueOf(grade.getRating()));

        final EditText note = (EditText) mView.findViewById(R.id.note);
        note.setText(grade.getNote());

        final Button btnCancel = mView.findViewById(R.id.grade_edit_cancel);
        final Button btnOk = mView.findViewById(R.id.grade_edit_ok);

        final ImageView btnHelp = (ImageView) mView.findViewById(R.id.btnRoomHelp);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);

        final RadioButton isWritten = mView.findViewById(R.id.isWritten);

        final RadioButton isNotWritten = mView.findViewById(R.id.isNotWritten);

        if(grade.iswritten())
            isWritten.setChecked(true);
        else
            isNotWritten.setChecked(true);

        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(extraLayout.getVisibility() != View.VISIBLE)
                    extraLayout.setVisibility(View.VISIBLE);
                else
                    extraLayout.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adTrueDialog.cancel();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                adTrueDialog.cancel();
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog(context.getString(R.string.rating), context.getString(R.string.ratingExplanation), 0);
            }
        });






        ad
                //.setTitle(context.getString(R.string.editGrade) + subject.getName())
                .setView(mView);
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


                        Subject subject = subjectManager.getSubjectByName(subjectSelection.getSelectedItem().toString());
                        subject.addGrade(Integer.valueOf(gradeInput.getText().toString()), isWrittenBool, rating, note.getText().toString());

                        grade.setGrade(Integer.valueOf(gradeInput.getText().toString()));
                        grade.setIswritten(isWrittenBool);
                        grade.setRating(rating);
                        grade.setNote(note.getText().toString());
                        notifyItemChanged(grades.indexOf(grade));

                        subjectManager.save();
                        ((TextView)((Activity)context).findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Do nothing

                    }
                })*/;

                adTrueDialog = ad.show();
    }

    void createAlertDialog(String title, String text, int ic){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(ic)
                .show();
    }

    public void askDelete(final Grade grade){

        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
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
                .setIcon(android.R.drawable.ic_delete)
                .show();

    }
    public void delete(Grade grade){
        int formerIndex = grades.indexOf(grade);
        grade.getSubject().removeGrade(grade);
        if(grades.contains(grade))
            grades.remove(grade);
        notifyItemRemoved(formerIndex);
        ((TextView)((Activity)context).findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
        if(grades.isEmpty()){
            gradeMessage.setVisibility(View.VISIBLE);
        }
    }
    public void addGrade(Grade grade){
        if(grades.contains(grade))
            notifyItemInserted(grades.indexOf(grade));
    }
}
