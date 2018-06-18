package de.tk.annapp.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
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


import de.tk.annapp.Grade;
import de.tk.annapp.R;
import de.tk.annapp.Adapter.RVAdapterSubjectList;
import de.tk.annapp.Subject;
import de.tk.annapp.SubjectManager;

import static android.R.layout.simple_spinner_dropdown_item;

public class GradesFragment extends Fragment {
    View root;

    boolean isWrittenBool;

    private SubjectManager subjectManager;

    RecyclerView recyclerView;

    public static final String TAG = "GradeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(getString(R.string.grades));

        root = inflater.inflate(R.layout.fragment_grades, container, false);

        //Get Singelton subjectManager
        subjectManager = SubjectManager.getInstance();

        String avg = String.valueOf(subjectManager.getWholeGradeAverage());
        if(!avg.equals("NaN")){
            getActivity().findViewById(R.id.grade).setVisibility(View.VISIBLE);
            ((TextView)getActivity().findViewById(R.id.grade)).setText(String.valueOf(subjectManager.getWholeGradeAverage()));
        }

        FloatingActionButton fabAdd = (FloatingActionButton) root.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (subjectManager.getSubjects().isEmpty()) {
                    createAlertDialog(getContext().getString(R.string.warning), "Bitte fügen Sie ein Fach hinzu!", android.R.drawable.ic_dialog_alert);
                } else
                    createInputDialog();
            }
        });
        TextView missingSubjectsWarning = (TextView) root.findViewById(R.id.missingSubjectsWarning);
        if (subjectManager.getSubjects().isEmpty())
            missingSubjectsWarning.setVisibility(View.VISIBLE);

        recyclerView = root.findViewById(R.id.recyclerViewSubjectsId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new RVAdapterSubjectList(getActivity(), subjectManager.getSubjects()));

        //addTestGrades();

        return root;
    }

/*
    private void addTestGrades(){
        subjectManager.addSubject("Mathe", 2);
        subjectManager.addSubject("Deutsch", 2);

        subjectManager.getSubjectByName("Deutsch").addGrade(3, true, 1, "grrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");

        Subject mathe = subjectManager.getSubjectByName("Mathe");
        System.out.println(mathe.name);

        mathe.addGrade(4, true, 2, "KLAUSUR");
        mathe.addGrade(3, true, 1, "weffffffffffffffffffffffffffffffffffffffffff");

        System.out.println(mathe.getGradePointAverage());

        mathe.addGrade(1, false , 3, "wfeqqqqqtttttttttttttttttttttttt");
        mathe.addGrade(2, false, 1, "gggggggggggggggggggggggggggggggggggggggggggggggggggggggg");

        System.out.println(mathe.getGradePointAverage());

        System.out.println("" + subjectManager.getWholeGradeAverage());
    }*/

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
                createAlertDialog(getString(R.string.rating), getString(R.string.ratingExplanation), 0);
            }
        });


        final Spinner subjectSelection = (Spinner) mView.findViewById(R.id.subjectSelection);

        //TODO SpinnerColor
        Context wrappedContext = new ContextThemeWrapper(getContext(), R.style.BasicTheme);
        ArrayAdapter<Subject> adapter = new ArrayAdapter<>(wrappedContext, R.layout.spinner_item, subjectManager.getSubjects());

        adapter.setDropDownViewTheme(getActivity().getTheme());

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
                    createAlertDialog("Error!", "Please fill in all needed information", 0);
                    return;
                }

                if(!ratingInput.getText().toString().isEmpty())
                    rating = Float.parseFloat(ratingInput.getText().toString());


                Subject subject = (Subject) subjectSelection.getSelectedItem();
                Grade newGrade =new Grade(subject, Integer.valueOf(gradeInput.getText().toString()), isWrittenBool, rating, note.getText().toString());
                subject.addGrade(newGrade);
                recyclerView.getAdapter().notifyItemChanged(subjectManager.getSubjects().indexOf(subject));
                ((TextView)getActivity().findViewById(R.id.grade)).setText(String.valueOf(subjectManager.getWholeGradeAverage()));
                subjectManager.save();
                //((RVAdapterSubjectList)recyclerView.getAdapter()).addGrade(newGrade);
                bsd.cancel();
            }
        });
        bsd.setContentView(mView);
        bsd.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
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


                        Subject subject = subjectManager.getSubjectByName(subjectSelection.getSelectedItem().toString());
                        subject.addGrade(Integer.valueOf(gradeInput.getText().toString()), isWrittenBool, rating, note.getText().toString());
                        subjectManager.save(getContext(), "subjects");
                        recyclerView.setAdapter(new RVAdapterSubjectList(getActivity(), subjectManager.getSubjects()));                    }
                })*/

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
                .setIcon(ic)
                .show();
    }
}
