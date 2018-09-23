package de.tk.annapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.tk.annapp.Adapter.SliderAdapter;


import de.tk.annapp.R;
import de.tk.annapp.SubjectManager;

public class TutorialFragment extends Fragment{
    View root;

    private ViewPager slideViewPager;
    private LinearLayout dotLayout;

    private TextView[] dots;
    private Button nextButton;
    private Button backButton;

    private int tutorialLength;

    private int currentPageCount;

    private SliderAdapter sliderAdapter;

    private SubjectManager subjectManager;

    public static final String TAG = "TutorialFragment";

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tutorial, container, false);

        System.out.println("Creating Tutorial-----------------------------------------------");

        subjectManager = SubjectManager.getInstance();

        slideViewPager = (ViewPager) root.findViewById(R.id.tutorial_viewPager);
        dotLayout = root.findViewById(R.id.tutorial_dotLayout);
        nextButton = root.findViewById(R.id.tutorial_nextButton);
        backButton = root.findViewById(R.id.tutorial_backButton);

        tutorialLength = getResources().getStringArray(R.array.slide_description).length;

        getActivity().findViewById(R.id.include).findViewById(R.id.toolbar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.nav_view).setVisibility(View.INVISIBLE);
        ((DrawerLayout)getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



//        SharedPreferences myPrefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor myPrefEditor = myPrefs.edit();
//        myPrefEditor.putBoolean("firstLaunch", false);
//        myPrefEditor.apply();
        startTutorial();

        return root;
    }

    @SuppressLint("NewApi")
    public static final void recreateActivityCompat(final Activity a) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            a.recreate();
        } else {
            final Intent intent = a.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            a.finish();
            a.overridePendingTransition(0, 0);
            a.startActivity(intent);
            a.overridePendingTransition(0, 0);
        }
    }

    private void startTutorial(){

        sliderAdapter = new SliderAdapter(this.getContext());
        slideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        slideViewPager.addOnPageChangeListener(viewListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPageCount+1 < dots.length) {
                    System.out.println(currentPageCount);
                    System.out.println(dots.length);
                    slideViewPager.setCurrentItem(currentPageCount + 1);
                }
                else if(currentPageCount+1 == dots.length){
                    // Insert the fragment by replacing any existing fragment
                    getActivity().getPreferences(Context.MODE_PRIVATE).edit().putBoolean("firstLaunch", false).commit();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, new HomeFragment())
                            .commit();


                    getActivity().findViewById(R.id.include).findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

                    getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

                    ((DrawerLayout)getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideViewPager.setCurrentItem(currentPageCount -1);
            }
        });

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPageCount = position;
            addDotsIndicator(position);

            if(position == 0){
                nextButton.setEnabled(true);
                backButton.setEnabled(false);
                backButton.setVisibility(View.INVISIBLE);

                backButton.setText("");
                nextButton.setText("Next");
            } else if(position == dots.length -1){
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                backButton.setText("Back");
                nextButton.setText("Finish");
                System.out.println(currentPageCount);
                System.out.println(dots.length);
            } else {
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                backButton.setText("Back");
                nextButton.setText("Next");


            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void addDotsIndicator(int position){
        dots = new TextView[tutorialLength];
        dotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this.getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(32);
            dots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            dotLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

}
