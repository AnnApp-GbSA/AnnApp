package com.pax.tk.annapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.pax.tk.annapp.Grade;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ELAdapterGradeList extends BaseExpandableListAdapter {

    private Context context;
    private Subject subject;
    private ArrayList<Grade> grades;

    private boolean isWrittenBool;

    public ELAdapterGradeList(Context context, Subject subject){
        this.context = context;
        this.subject = subject;
        grades = subject.getAllGrades();
    }

    @Override
    public int getGroupCount() {
        return grades.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Grade getGroup(int groupPosition) {
        return grades.get(groupPosition);
    }

    @Override
    public Grade getChild(int groupPosition, int childPosition) {
        return grades.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = Integer.toString(getGroup(groupPosition).getGrade());
        ImageButton editButton;

        System.out.println("isExpanded:" + isExpanded);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.group_grade_list, null);
        }

        editButton = convertView.findViewById(R.id.item_grade_button_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Create InputDialog...");
                createEditDialog(grades.get(groupPosition));
            }
        });

        TextView gradetext = convertView.findViewById(R.id.item_grade_grade);
        gradetext.setTypeface(null, Typeface.BOLD);
        gradetext.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Grade grade = getChild(groupPosition, childPosition);
        final String childText = grade.getNote() + "/n Wertung:" + grade.getRating();
        System.out.println(childText);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_grade_list, parent, false);
        }

        TextView txtListChild = convertView.findViewById(R.id.igListItem);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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
                //notifyItemChanged(grades.indexOf(grade));

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
}
