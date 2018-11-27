package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pax.tk.annapp.R;

/**
 * Created by Tobi on 20.09.2017.
 */

public class RepresentationPlan extends Fragment {
    View root;

    public static final String TAG = "RepresentationPlanFragment";

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

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.representationPlan));
        root = inflater.inflate(R.layout.fragment_representationplan, container, false);
        return root;
    }
}

