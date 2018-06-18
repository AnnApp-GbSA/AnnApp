package de.tk.annapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tk.annapp.R;
import de.tk.annapp.Adapter.RVAdapterGradeList;
import de.tk.annapp.Subject;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class GradeChildFragment extends Fragment {
    View root;

    RecyclerView recyclerView;

    public static final String TAG = "LessonGradeOverviewFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        Subject subject = (Subject) args.get(getString(R.string.bundlekey_subject));
        root = inflater.inflate(R.layout.fragment_gradeschild, container, false);
        TextView gradeMessage = (TextView) root.findViewById(R.id.noGrade);
        getActivity().setTitle(subject.getName());
        if(!subject.getAllGrades().isEmpty()) {
            gradeMessage.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.grade).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
        }

        recyclerView = root.findViewById(R.id.recyclerViewGradesId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new RVAdapterGradeList(getActivity(), subject, gradeMessage));

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
    }
}

