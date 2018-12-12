package com.pax.tk.annapp;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsDetailActivity extends AppCompatActivity {

    TextView tvTitle;
    ImageView imageViewToolbar;
    AppBarLayout appBarLayout;
    com.pax.tk.annapp.News news;
    int colorPrimary;
    int colorAccent;
    int colorPrimaryDark;
    WebView webView;
    boolean whiteText = false;

    /**
     * creates the NewsDetailActivity, sets it up and calls methods
     *
     * @param savedInstanceState ...
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.setTheme(R.style.AppThemeColorful);
        switch (bundle.getInt(getString(R.string.bundleKey_colorThemePosition))) {
            case 0:
                //TODO nur zum Anzeigen
                //setTheme(R.style.Tim);
                setTheme(R.style.AppThemeDefault);
                break;
            case 1:
                setTheme(R.style.AppThemeOrange);
                whiteText = true;
                break;
            case 2:
                setTheme(R.style.AppThemeBlue);
                break;
            case 3:
                setTheme(R.style.AppThemeColorful);
                whiteText = true;
                break;
        }
        setContentView(R.layout.activity_news_detail);

        tvTitle = this.findViewById(R.id.tvTitle);
        imageViewToolbar = this.findViewById(R.id.imageViewToolbar);
        appBarLayout = this.findViewById(R.id.appbar);
        webView = this.findViewById(R.id.webView);

        System.out.println("onCreateView");

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        colorPrimary = typedValue.data;
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        colorPrimaryDark = typedValue.data;
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        colorAccent = typedValue.data;


        news = (com.pax.tk.annapp.News) bundle.getSerializable(getString(R.string.bundlekey_news));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        tvTitle.setText(news.getTitle());

        webView.getSettings().setJavaScriptEnabled(true);

        if (whiteText)
            webView.loadDataWithBaseURL("", "<font color='white'>" + news.getRawArticle(), "text/html", "UTF-8", "");
        else
            webView.loadDataWithBaseURL("", news.getRawArticle(), "text/html", "UTF-8", "");
        webView.getSettings();
        webView.setBackgroundColor(Color.TRANSPARENT);

        //disabling scrolling in webView
        webView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(android.R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getColor(android.R.color.transparent));

        /*collapsingToolbarLayout.setContentScrimColor(colorPrimary);
        appBarLayout.setBackgroundColor(colorPrimary);*/

        //Change the color on top of the toolbar
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = NewsDetailActivity.this.getWindow();
            window.setStatusBarColor(colorPrimaryDark);
        }*/

        this.setTitle(news.getTitle());

        imageViewToolbar.setImageDrawable(Manager.getInstance().getFromURl(news.getImageurl()));

        System.out.println(news.getArticle());


    }


}
