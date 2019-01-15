package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.pax.tk.annapp.Grade;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Adapter.RVAdapterSubjectList;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Util;

public class GradesFragment extends Fragment {
    View root;

    boolean isWrittenBool;

    private Manager manager;

    RecyclerView recyclerView;

    public static final String TAG = "GradeFragment";

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
        getActivity().setTitle(getString(R.string.grades));

        root = inflater.inflate(R.layout.fragment_grades, container, false);

        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appInformationBtn).setVisibility(View.GONE);

        //Get Singelton manager
        manager = Manager.getInstance();

        String avg = String.valueOf(manager.getWholeGradeAverage());
        if(!avg.equals("NaN")){
            getActivity().findViewById(R.id.grade).setVisibility(View.VISIBLE);
            ((TextView)getActivity().findViewById(R.id.grade)).setText(String.valueOf(manager.getWholeGradeAverage()));
        }

        FloatingActionButton fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (manager.getSubjects().isEmpty()) {
                    Util.createAlertDialog(getContext().getString(R.string.warning), getString(R.string.addSubjectMessage), android.R.drawable.ic_dialog_alert, getContext());
                } else{
                    //createInputDialog();
                    (new Util()).createInputDialog(getContext(), getActivity(), recyclerView, null);
                    }
            }
        });
        TextView missingSubjectsWarning = (TextView) root.findViewById(R.id.missingSubjectsWarning);
        if (manager.getSubjects().isEmpty())
            missingSubjectsWarning.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.recyclerViewSubjectsId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new RVAdapterSubjectList(getActivity(), manager.getSubjects()));

        //addTestGrades();

        return root;
    }

    /**
     * creates an input dialog, sets it up and calls methods
     */
    public void createInputDialog() {

        //AlertDialog.Builder ad = new  AlertDialog.Builder(this.getContext());
        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(),R.style.NewDialog);




        View mView = View.inflate(this.getContext(), R.layout.dialog_new_grade, null);
        //mView.setBackgroundColor(getResources().getColor(android.R.color.transparent));


        final EditText gradeInput = (EditText) mView.findViewById(R.id.gradeInput);
        final EditText ratingInput = (EditText) mView.findViewById(R.id.ratingInput);
        final EditText note = (EditText) mView.findViewById(R.id.note);
        final ImageView btnHelp = (ImageView) mView.findViewById(R.id.btnRoomHelp);
        final Button btnExtra = (Button) mView.findViewById(R.id.btnExtra);
        final LinearLayout extraLayout = (LinearLayout) mView.findViewById(R.id.extraLayout);
        final FloatingActionButton btnOK = (FloatingActionButton) mView.findViewById(R.id.btnOK);
        final FloatingActionButton btnCancel = (FloatingActionButton) mView.findViewById(R.id.btnCancel);

        btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extraLayout.getVisibility() != View.VISIBLE)
                    extraLayout.setVisibility(View.VISIBLE);
                else
                    extraLayout.setVisibility(View.GONE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsd.cancel();
            }
        });


        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.createAlertDialog(getString(R.string.rating), getString(R.string.ratingExplanation), 0, getContext());
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

        Context wrappedContext = new ContextThemeWrapper(getContext(), R.style.BasicTheme);
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(wrappedContext, R.layout.white_spinner_item, manager.getSubjects());

        adapter.setDropDownViewTheme(getActivity().getTheme());

        subjectSelection.setAdapter(adapter);
        subjectSelection.setAdapter(adapter);


        final RadioButton isWritten = mView.findViewById(R.id.isWritten);

        final RadioButton isNotWritten = mView.findViewById(R.id.isNotWritten);


        bsd.setTitle(R.string.addGrade);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = 1;

                //testing which button is active for decision whether your Grade is written or whether it's not
                if(isWritten.isChecked())
                    isWrittenBool = true;
                else if(isNotWritten.isChecked())
                    isWrittenBool = false;

                if(gradeInput.getText().toString().isEmpty()){
                    Util.createAlertDialog(getString(R.string.warning), getString(R.string.warningMessage), 0, getContext());
                    return;
                }

                if(!ratingInput.getText().toString().isEmpty())
                    rating = Float.parseFloat(ratingInput.getText().toString());


                Subject subject = (Subject) subjectSelection.getSelectedItem();
                Grade newGrade =new Grade(subject, Integer.valueOf(gradeInput.getText().toString()), isWrittenBool, rating, note.getText().toString());
                subject.addGrade(newGrade);
                recyclerView.getAdapter().notifyItemChanged(manager.getSubjects().indexOf(subject));
                ((TextView)getActivity().findViewById(R.id.grade)).setText(String.valueOf(manager.getWholeGradeAverage()));
                //((RVAdapterSubjectList)recyclerView.getAdapter()).addGrade(newGrade);
                bsd.cancel();
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

    /**
     * destroys the view
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
    }

    /*float rating = 1;

                        //testing which button is active for decision whether your Grade is written or whether it's not
                        if(isWritten.isChecked())
                            isWrittenBool = true;
                        else if(isNotWritten.isChecked())
                            isWrittenBool = false;

                        if(gradeInput.getText().toString().isEmpty()){
                            createAlertDialog("Error!", "Please fill in all needed information", 0);
                            return;
                        }

                        if(!ratingInput.getText().toString().isEmpty())
                            rating = Float.parseFloat(ratingInput.getText().toString());


                        Subject subject = manager.getSubjectByName(subjectSelection.getSelectedItem().toString());
                        subject.addGrade(Integer.valueOf(gradeInput.getText().toString()), isWrittenBool, rating, note.getText().toString());
                        manager.save(getContext(), "subjects");
                        recyclerView.setAdapter(new RVAdapterSubjectList(getActivity(), manager.getSubjects()));                    }
                })*/


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

}
