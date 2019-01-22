package com.pax.tk.annapp.Fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import com.pax.tk.annapp.Manager;
import com.pax.tk.annapp.News;
import com.pax.tk.annapp.R;
import com.pax.tk.annapp.Adapter.RVAdapterNews;
import com.pax.tk.annapp.Util;

/**
 * Created by Tobi on 20.09.2017.
 */

public class AnnanewsFragment extends Fragment {
    private View root;
    private SwipeRefreshLayout mSwipeLayout;
    private ArrayList<News> mFeedModelList;
    private Manager manager;
    private RVAdapterNews rvAdapterNews;
    Boolean cancelled;

    public static final String TAG = "AnnanewsFragment";

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

        getActivity().setTitle(getString(R.string.AnnaNews));
        root = inflater.inflate(R.layout.fragment_annanews, container, false);
        RecyclerView rv = root.findViewById(R.id.rv_news);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAdapterNews = new RVAdapterNews(getContext());
        rv.setAdapter(rvAdapterNews);

        mSwipeLayout = root.findViewById(R.id.swipeRefreshLayout);
        manager = Manager.getInstance();

        new FetchFeedTask().execute((Void) null);

        rvAdapterNews.update();

        TypedValue a = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorBackground, a, true);
        mSwipeLayout.setProgressBackgroundColorSchemeColor(a.data);

        mSwipeLayout.setColorSchemeColors(Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000), Util.randomNumberGenerator(0, 1000000000));

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchFeedTask().execute((Void) null);

                rvAdapterNews.update();
                System.out.println("onRefresh");
            }
        });


        return root;
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String[] urlLinks = new String[1];

        /**
         * method is called before loading the AnnaNews
         * sets the link where the news are from and sets the layout refreshable
         */
        @Override
        protected void onPreExecute() {
            urlLinks[0] = "http://gym-anna.de/wordpress/?feed=rss2";
            //urlLinks[1] = "url";
            mSwipeLayout.setRefreshing(true);
            //urlLink = "http://gym-anna.de/wordpress/?feed=rss2";
        }

        /**
         * tests if the link is usable and makes it usable if not
         * loads the information from the link into an ArrayList of news with calling the parseFeed()
         * calls the mergeNews() in the manager
         *
         * @param voids ...
         * @return true if the method is successful and false if an IOException is caught
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            for (String urlLink :
                    urlLinks) {
                try {
                    if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                        urlLink = "http://" + urlLink;

                    URL url = new URL(urlLink);
                    InputStream inputStream = url.openConnection().getInputStream();
                    ArrayList<News> news = parseFeed(inputStream);
                    //manager.mergeNews(news);
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, "Error", e);
                    Looper.prepare();
                    Toast.makeText(getContext(), R.string.noConnection, Toast.LENGTH_SHORT).show();
                }
                return false;

            }
            return false;
        }

        /**
         * method is called after loading the AnnaNews
         * disable the refreshable layout
         *
         * @param success ...
         */
        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);
            cancelled = true;
        }
    }

    /**
     * finds the text between two xmlTags
     *
     * @param content  xmlText which in the text should be found
     * @param startTag ...
     * @param endTag   ...
     * @return text between the two xmlTags
     */
    private String xmlget(String content, String startTag, String endTag) {
        String ret = content.substring(content.indexOf(startTag) + startTag.length());
        return ret.substring(0, ret.indexOf(endTag));
    }

    /**
     * gets the news in a inputStream and returns them in an ArrayList
     *
     * @param inputStream ...
     * @return ArrayList of News with all news from the inputStream in it
     * @throws IOException ...
     */
    public ArrayList<News> parseFeed(InputStream inputStream) throws IOException {
        ArrayList<News> items = new ArrayList<>();
        java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
        String content = s.hasNext() ? s.next() : "";
        //content = content.replaceAll("\n","");
        content = htmlToString(content);

        inputStream.close();
        while (content.contains("<item>")) {
            String item = xmlget(content, "<item>", "</item>");
            content = content.substring(content.indexOf("</item>") + "</item>".length());
            String title = xmlget(item, "<title>", "</title>");
            String link = xmlget(item, "<link>", "</link>");
            String description = xmlget(item, "<description><![CDATA[", "]]></description>");
            String imageurl = item.contains("<content:encoded><![CDATA[<p><a href=\"") ? xmlget(item, "<content:encoded><![CDATA[<p><a href=\"", "\">") : null;
            String article = xmlget(item, "<content:encoded><![CDATA[", "]]></content:encoded>");
            article = article.replaceAll("</p><p>", "\n\n");
            String rawArticle = article;
            rawArticle = rawArticle.replace("<span style=\"background-color: #99cc00;\">", "<span style=\"background-color: #555555;\">");

            /*while (article.contains("<a"))
                article = xmlcut(article, "<a","</a>");
            while (article.contains("<span"))
                article = xmlcut(article, "<span","</span>");
            article = article.replaceAll("<p>","");
            article = article.replaceAll("</p>","");
            while (article.contains("<p "))
                article = xmlcut(article,"<p ",">");*/

            Drawable image = Manager.getInstance().getFromURl(imageurl);
            items.add(new News(title, link, description, article, rawArticle, imageurl));
            manager.addNews(new News(title, link, description, article, rawArticle, imageurl));
            synchronized(rvAdapterNews){
                rvAdapterNews.notify();
            }
        }
        //return items;
        return null;
    }

    /**
     * replaces html shortcuts with Strings
     *
     * @param string input String with html shortcuts in it
     * @return edited String with hopefully no html shortcuts in it
     */
    public String htmlToString(String string) {
        String edit;
        edit = string.replaceAll("&#8220;", "“");
        edit = edit.replaceAll("&#8221;", "”");
        edit = edit.replaceAll("&#8216;", "‘");
        edit = edit.replaceAll("&#8217;", "’");
        edit = edit.replaceAll("&#8230;", "...");
        edit = edit.replaceAll("&nbsp;", "");
        edit = edit.replaceAll("&#8211;", "–");
        edit = edit.replaceAll("<br />", "\n");

        return edit;
    }
}

