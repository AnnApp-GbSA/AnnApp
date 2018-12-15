package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pax.tk.annapp.Grade;
import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Adapter.RVAdapterGradeList;
import com.pax.tk.annapp.Subject;
import com.pax.tk.annapp.Util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class GradeChildFragment extends Fragment {
    View root;

    RecyclerView recyclerView;
    private Subject subject;
    FloatingActionButton fabAdd;

    public static final String TAG = "LessonGradeOverviewFragment";

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
        Bundle args = getArguments();
        subject = (Subject) args.get(getString(R.string.bundlekey_subject));
        root = inflater.inflate(R.layout.fragment_gradeschild, container, false);
        fabAdd = root.findViewById(R.id.fabAdd);
        TextView gradeMessage = (TextView) root.findViewById(R.id.noGrade);
        recyclerView = root.findViewById(R.id.recyclerViewGradesId);
        Manager manager = Manager.getInstance();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = manager.getSubjects().indexOf(subject);
                (new Util()).createInputDialog(getContext(), getActivity(), recyclerView, position);
            }
        });

        getActivity().setTitle(subject.getName());
        try{
            gradeMessage.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.grade).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
            System.out.println("displaying grade point average");
        }catch (Exception e){}

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RVAdapterGradeList(getActivity(), subject, gradeMessage));

        return root;
    }

    /**
     * destroys the view
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
    }

}

