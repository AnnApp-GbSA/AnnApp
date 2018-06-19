package de.tk.annapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tk.annapp.R;

/**
 * Created by Tobi on 20.09.2017.
 */

public class PrivateTuitionFragment extends Fragment  {
    View root;

    public static final String TAG = "TuitionFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.privateTuition));
        root = inflater.inflate(R.layout.fragment_privatetuition, container, false);
        return root;
    }
}
