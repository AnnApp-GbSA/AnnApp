package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/*
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;*/
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.pax.tk.annapp.R;

import java.io.IOException;
import java.util.List;


public class SecuronFragment extends Fragment {

    View root;
    String name = "tkiehnlein";
    String password = "";

    public static final String TAG = "SecuronFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);

        getActivity().setTitle("Securon");
        root = inflater.inflate(R.layout.fragment_securon, container, false);

        //startSardine(name, password);

        return root;
    }

    /*private static void startSardine(String name, String password) {
        Sardine sardine = SardineFactory.begin(name, password);
        List<DavResource> resources = null;
        try {
            resources = sardine.list("https://deby0002.securon.eu/dav/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (DavResource res : resources) {
            System.out.println(res); // calls the .toString() method.
        }
    }*/

}
