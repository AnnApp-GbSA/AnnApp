package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pax.tk.annapp.Adapter.ELAdapterGradeList;
import com.pax.tk.annapp.Grade;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Adapter.RVAdapterGradeList;
import com.pax.tk.annapp.Subject;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GradeChildFragment extends Fragment {
    View root;

    RecyclerView recyclerView;

    private ExpandableListView listView;
    private ELAdapterGradeList listAdapter;
    private List<Grade> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    private Subject subject;

    public static final String TAG = "LessonGradeOverviewFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        subject = (Subject) args.get(getString(R.string.bundlekey_subject));
        root = inflater.inflate(R.layout.fragment_gradeschild, container, false);
        TextView gradeMessage = (TextView) root.findViewById(R.id.noGrade);
        getActivity().setTitle(subject.getName());
        try{
            gradeMessage.setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.grade).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.grade)).setText(String.valueOf(subject.getGradePointAverage()));
            System.out.println("displaying grade point average");
        }catch (Exception e){}

        listView = root.findViewById(R.id.LVExpand);
        listAdapter = new ELAdapterGradeList(getContext(), subject);

        listView.setAdapter(listAdapter);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setAdapter(new RVAdapterGradeList(getActivity(), subject, gradeMessage, getActivity().getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition),0)));

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
    }

}

