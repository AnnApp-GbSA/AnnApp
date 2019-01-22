package com.pax.tk.annapp.Fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Util;

/**
 * Created by Tobi on 20.09.2017.
 */

public class RepresentationPlanFragment extends Fragment {
    View root;
    WebView webView;
    WebView webView2;
    int color;
    private SwipeRefreshLayout mSwipeLayout;


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
        getActivity().findViewById(R.id.appInformationBtn).setVisibility(View.GONE);

        getActivity().setTitle(getString(R.string.representationPlan));
        root = inflater.inflate(R.layout.fragment_representationplan, container, false);

        TypedValue a = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.textColorPrimary, a, true);
        color = a.data;

        webView = root.findViewById(R.id.webView);
        webView2 = root.findViewById(R.id.webView2);

        mSwipeLayout = root.findViewById(R.id.swipeRefreshLayout);
        TypedValue b = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBackground, b, true);
        mSwipeLayout.setProgressBackgroundColorSchemeColor(b.data);

        mSwipeLayout.setColorSchemeColors(Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000));

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setWebView(webView, "http://gym-anna.de/vplan_annapp/subst_001.htm");
                setWebView(webView2, "http://gym-anna.de/vplan_annapp/subst_002.htm");
            }
        });

        setWebView(webView, "http://gym-anna.de/vplan_annapp/subst_001.htm");
        setWebView(webView2, "http://gym-anna.de/vplan_annapp/subst_002.htm");




        return root;
    }

    public boolean checkInternetConnection() {
        Context context = getContext();
        ConnectivityManager con_manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }


    void setWebView(WebView webView, String url){

        if (!checkInternetConnection()){
            webView.setVisibility(View.GONE);
            root.findViewById(R.id.noInternetConnection).setVisibility(View.VISIBLE);
        }

        webView.getSettings().setJavaScriptEnabled(false);
        mSwipeLayout.setRefreshing(true);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mSwipeLayout.setRefreshing(false);
            }
        });


        Ion.with(getContext()).load(url).asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (result == null){
                    webView.setVisibility(View.GONE);
                    root.findViewById(R.id.noInternetConnection).setVisibility(View.VISIBLE);
                    return;
                }



                String finalView = result;
                finalView = finalView.replace("���", "→");
                finalView = finalView.replace("<a href=\"http://www.untis.at\" target=\"_blank\" >Untis Stundenplan Software</a>", "");

                if (color == -1) {
                    finalView = finalView.replace("background: #fff; color: #272727;", "background: #212121; color: #ffffff;");
                    finalView = finalView.replace("<p>", "<p style=\"color:white;\">");
                    finalView = finalView.replace("<tr class='info'>", "<tr class='info' style=\"color:white;\">");
                }

                webView.loadData(finalView, "text/html", "UTF-32");
                System.out.println(finalView);
            }
        });


        //webView.set
        //webView.loadUrl("http://www.gym-anna.de/vplan_annapp/subst_001.htm");

        webView.getSettings();
        webView.setBackgroundColor(Color.TRANSPARENT);

    }
}

