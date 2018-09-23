package com.pax.qbt.annapp;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsDetailActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageViewToolbar;
    AppBarLayout appBarLayout;
    com.pax.qbt.annapp.News news;
    int colorPrimary;
    int colorAccent;
    int colorPrimaryDark;
    int defaultTextColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.setTheme(R.style.AppThemeColorful);
        switch (bundle.getInt(getString(R.string.bundleKey_colorThemePosition))){
            case 0:
                //TODO nur zum Anzeigen
                //setTheme(R.style.Tim);
                setTheme(R.style.AppThemeDefault);
                break;
            case 1:
                setTheme(R.style.AppThemeOrange);
                break;
            case 2:
                setTheme(R.style.AppThemeBlue);
                break;
            case 3:
                setTheme(R.style.AppThemeColorful);
        }
        setContentView(R.layout.activity_news_detail);

        textView = this.findViewById(R.id.textViewText);
        imageViewToolbar = this.findViewById(R.id.imageViewToolbar);
        appBarLayout = this.findViewById(R.id.appbar);

        System.out.println("onCreateView");

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        colorPrimary=typedValue.data;
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        colorPrimaryDark=typedValue.data;
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        colorAccent=typedValue.data;




        news = (com.pax.qbt.annapp.News) bundle.getSerializable(getString(R.string.bundlekey_news));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getColor(android.R.color.white));
        setSupportActionBar(toolbar);


        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getColor(android.R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getColor(android.R.color.white));

        /*collapsingToolbarLayout.setContentScrimColor(colorPrimary);
        appBarLayout.setBackgroundColor(colorPrimary);*/

        //Change the color on top of the toolbar
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = NewsDetailActivity.this.getWindow();
            window.setStatusBarColor(colorPrimaryDark);
        }*/

        this.setTitle(news.getTitle());

        imageViewToolbar.setImageDrawable(com.pax.qbt.annapp.SubjectManager.getInstance().getFromURl(news.getImageurl()));

        textView.setText(news.getArticle());
        System.out.println(news.getArticle());


    }


}
