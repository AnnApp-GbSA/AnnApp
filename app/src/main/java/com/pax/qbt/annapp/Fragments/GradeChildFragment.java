package com.pax.qbt.annapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pax.qbt.annapp.R;
import com.pax.qbt.annapp.Adapter.RVAdapterGradeList;
import com.pax.qbt.annapp.Subject;

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
        try{
            gradeMessage.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.grade).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
            System.out.println("displaying grade point average");
        }catch (Exception e){}

        recyclerView = root.findViewById(R.id.recyclerViewGradesId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new RVAdapterGradeList(getActivity(), subject, gradeMessage, getActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition),0)));

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
    }

}

