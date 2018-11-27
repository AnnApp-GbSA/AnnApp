package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/*
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;*/
import com.pax.tk.annapp.R;

import java.io.IOException;
import java.util.List;


public class SecuronFragment extends Fragment {

    View root;
    String name = "tkiehnlein";
    String password = "123456789";

    public static final String TAG = "SecuronFragment";

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

        getActivity().setTitle("Securon");
        root = inflater.inflate(R.layout.fragment_securon, container, false);

        //startFTP(name, password);
        new startFTPConnection().execute((Void) null);

        return root;
    }

    /**
     *
     * @param name ...
     * @param password ...
     */
    private static void startFTP (String name, String password) {


    }

    private class startFTPConnection extends AsyncTask<Void, Void, Boolean> {

        /**
         * does nothing
         * @param voids ...
         * @return null
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
//                FTPClient ftpClient = new FTPClient();
//                ftpClient.connect("deby0002.securon.eu/dav");
//                ftpClient.login(name, password);
////            ftpClient.changeWorkingDirectory(serverRoad);
//                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//
//
//
//                ftpClient.disconnect();


               /* Sardine sardine = SardineFactory.begin(name, password);
                List<DavResource> resources = null;
                try {
                    resources = sardine.list("https://deby0002.securon.eu/dav/");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (DavResource res : resources) {
                    System.out.println(res); // calls the .toString() method.
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
