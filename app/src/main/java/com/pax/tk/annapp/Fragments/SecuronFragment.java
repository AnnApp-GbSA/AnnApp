package com.pax.tk.annapp.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;*/
import com.koushikdutta.ion.builder.Builders;
import com.pax.tk.annapp.BuildConfig;
import com.pax.tk.annapp.MainActivity;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Util;
import com.pax.tk.annapp.onBackPressed;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class SecuronFragment extends Fragment implements onBackPressed {

    View root;
    public ArrayList<View> views;
    List<DavResource> resources;
    LinearLayout linearLayout;
    String basePath = "https://deby0002.securon.eu/dav/";
    String parentPath = "";
    String path = basePath;
    File file;
    String inputStream;
    Sardine sardine;
    SwipeRefreshLayout swipeRefreshLayout;
    AsyncTask<Void, Void, Void> startFTPConnection = null;
    public Boolean isCancelled = false;

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
        linearLayout = root.findViewById(R.id.linearLayout);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);

        init();

        return root;
    }

    private void init() {

        TypedValue a = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.backgroundTint, a, true);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(a.data);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startFTPConnection = new startFTPConnection().execute((Void) null);
            }
        });

        startFTPConnection = new startFTPConnection().execute((Void) null);
    }

    @Override
    public void onBackPressed() {
        /*String[] paths = path.split("/");
        path = "";
        for (String s :
                paths) {
            path += "/";
            path += s;
        }
        path += "/";
        linearLayout.removeAllViews();*/

        if (parentPath.equals("/")) {
            if (startFTPConnection != null){
                isCancelled = true;
                System.out.println("FTPConnection gecancelled");
            }
            getFragmentManager().popBackStack();
        }

        path = basePath.replace("/dav", "") + parentPath;
        System.out.println("path: " + path);
        Toast.makeText(getContext(), parentPath, Toast.LENGTH_SHORT).show();
        init();
    }


    private class startFTPConnection extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setColorSchemeColors(Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000));
            swipeRefreshLayout.setRefreshing(true);
            linearLayout.removeAllViews();
        }

        /**
         * does nothing
         *
         * @param voids ...
         * @return null
         */
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sardine = new OkHttpSardine();
                final String username = getActivity().getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.key_username), "°°°");
                final String password = getActivity().getPreferences(Context.MODE_PRIVATE).getString(getString(R.string.key_password), "°°°");

                if (username.equals("°°°") || password.equals("°°°")) {
                    Toast.makeText(getContext(), R.string.set_username_and_password, Toast.LENGTH_SHORT).show();
                    return null;
                }


                sardine.setCredentials(username, password, true);
                System.out.println(username);


                resources = sardine.list(path);

                System.out.println("resources: " + resources);

                InputStream inputStream = sardine.get("https://deby0002.securon.eu/dav/Austauschlaufwerk/12_d3/Peter Stamm_Agnes_S-Version.pptx");

                //views = new ArrayList<>();

                /*for (int i = 1; i < resources.size(); i++) {

                    DavResource resource = resources.get(i);

                    View itemView = LayoutInflater.from(getContext()).inflate(
                            R.layout.item_securon, null);
                    if (!resource.isDirectory()){
                        itemView.findViewById(R.id.folderImageView).setVisibility(View.GONE);
                    }

                    ((TextView) itemView.findViewById(R.id.nameTextView)).setText(resource.getName());

                    views.add(itemView);

                }*/

                return null;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (resources != null) {
                synchronized (resources) {

                    if (resources != null) {
                        ((TextView) root.findViewById(R.id.pathTextView)).setText(resources.get(0).toString());

                        File f2 = new File(resources.get(0).getPath());
                        f2 = f2.getParentFile();
                        System.out.println("parent: " + f2.getPath());
                        parentPath = f2.getPath();

                        for (int i = 1; i < resources.size(); i++) {

                            if (isCancelled)
                                return;

                            DavResource davresource = resources.get(i);
                            View itemView = LayoutInflater.from(getContext()).inflate(
                                    R.layout.item_securon, null);
                            if (!davresource.isDirectory()) {
                                itemView.findViewById(R.id.folderImageView).setVisibility(View.GONE);
                            }

                            ((TextView) itemView.findViewById(R.id.nameTextView)).setText(davresource.getName());

                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (davresource.isDirectory()) {
                                        path = basePath.replace("/dav", "");
                                        path += davresource.getPath();
                                        linearLayout.removeAllViews();
                                        init();
                                    } else {
                                        System.out.println("no Folder");
                                        file = new File("/storage/emulated/0/AnnApp" + davresource.getPath());
                                        inputStream = path + davresource.getName();

                                        new saveAndOpenFile().execute((Void) null);
                                    }
                                }
                            });

                            linearLayout.addView(itemView);
                            System.out.println("Resourcen: " + davresource.getName());
                        }
                    }
                }
            }

            swipeRefreshLayout.setRefreshing(false);
        }
    }


    private class saveAndOpenFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (inputStream != null && file != null && !inputStream.isEmpty()) {
                InputStream newInputStream = null;
                try {
                    newInputStream = sardine.get(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = null;

                saveFile(file, newInputStream);
                /*try {
                    openFile(getContext(), newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            swipeRefreshLayout.setRefreshing(false);
            openFile(file);
            file = null;
        }
    }

    private void saveFile(File file, InputStream input) {
        Util.checkStoragePermissions(getContext());
        System.out.println("saving at " + file.getAbsolutePath());

        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = input.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } finally {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openFile(File file) {

        //Util.checkStoragePermissions(getContext());
       /* System.out.println("File: "+file);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            System.out.println("Intent: " + intent);
            System.out.println("IntentData: " + FileProvider.getUriForFile(getContext(), "authority", file));
            //intent.setDataAndType((new FileProvider()).getUriForFile(getContext(), "authority", file), MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath()));
            intent.setData((new FileProvider()).getUriForFile(getContext(), "authority", file));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            */// no Activity to handle this kind of files
        Uri fileURI = FileProvider.getUriForFile(getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                file);

        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(fileURI,getMimeType(fileURI.getPath()));
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, getString(R.string.open_file));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a file reader here, or something
        }


    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
