package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesActivity;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.pax.tk.annapp.BuildConfig;
import com.pax.tk.annapp.R;

public class AppInformationFragment extends Fragment {

    public static final String TAG = "AppInformationFragment";

    private ImageView appLogo;
    private float cornerRadius = 1;
    private TextView versionText;
    private TextView infoText;
    private LinearLayout playStoreLayout;
    private LinearLayout websiteLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().findViewById(R.id.grade).setVisibility(View.GONE);
        getActivity().findViewById(R.id.appInformationBtn).setVisibility(View.GONE);
        getActivity().findViewById(R.id.syncWithCalendar).setVisibility(View.GONE);

        getActivity().setTitle("Information");


        View root = inflater.inflate(R.layout.fragment_appinformation, container, false);

        Button btn_libraries = root.findViewById(R.id.btn_libraries);

        appLogo = root.findViewById(R.id.information_AppLogo);

        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, src);
        dr.setCornerRadius(cornerRadius);

        versionText = root.findViewById(R.id.information_Version);

        versionText.setText(BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        Context context = getContext();

        infoText = root.findViewById(R.id.app_information);
        infoText.setText(context.getString(R.string.app_information_general) + "\n" + context.getString(R.string.app_information_teacher) + "\n" + context.getString(R.string.app_information_developer) + "\n" + context.getString(R.string.app_information_projectleader) + "\n" + context.getString(R.string.app_information_pupils));

        playStoreLayout = root.findViewById(R.id.information_layout_playstore);

        playStoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        websiteLayout = root.findViewById(R.id.information_layout_website);

        websiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://gym-anna.de/wordpress/");
                Intent websiteInt = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(websiteInt);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Unable to connect to Website", Toast.LENGTH_LONG).show();
                }
            }
        });


        btn_libraries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OssLicensesMenuActivity.setActivityTitle("Open-Source Bibliotheken");
                Intent intent = new Intent(getContext(), OssLicensesMenuActivity.class);
                //intent.putExtra("title", "Open-Source Bibliotheken");

                startActivity(intent);
            }
        });


        return root;
    }
}