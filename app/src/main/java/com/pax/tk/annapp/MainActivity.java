package com.pax.tk.annapp;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pax.tk.annapp.Fragments.AnnanewsFragment;
import com.pax.tk.annapp.Fragments.CalendarFragment;
import com.pax.tk.annapp.Fragments.FeedbackFragment;
import com.pax.tk.annapp.Fragments.GradeChildFragment;
import com.pax.tk.annapp.Fragments.GradesFragment;
import com.pax.tk.annapp.Fragments.HomeFragment;
import com.pax.tk.annapp.Fragments.RepresentationPlan;
import com.pax.tk.annapp.Fragments.SecuronFragment;
import com.pax.tk.annapp.Fragments.SettingsFragment;
import com.pax.tk.annapp.Fragments.TasksFragment;
import com.pax.tk.annapp.Fragments.TimetableFragment;
import com.pax.tk.annapp.Fragments.TutorialFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SubjectManager subjectManager;

    private String currentFragmentTag = null;
    private String prevFragmentTag = null;
    private Bundle currentFragmentBundle = null;

    private NavigationView navigationView = null;
    public static int colorScheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setTheme(R.style.AppTheme_green);
        switch (getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition), 0)) {
            case 0:
                colorScheme = R.style.AppThemeDefault;
                //TODO nur zum Anzeigen
                //setTheme(R.style.Tim);
                //setTheme(R.style.AppThemeColorful);
                break;
            case 1:
                colorScheme = R.style.AppThemeOrange;
                setTheme(R.style.AppThemeOrange);
                break;
            case 2:
                colorScheme = R.style.AppThemeBlue;
                setTheme(R.style.AppThemeBlue);
                break;
            case 3:
                colorScheme = R.style.AppThemeColorful;
                setTheme(R.style.AppThemeColorful);
        }

        Date d = new Date();
        d.setMinutes(d.getMinutes() + 1);

        System.out.println(d);

        new Util().createPushNotification(d, getApplicationContext(), 0, "test", "test", R.drawable.ic_add);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creates instance of SubjectManager
        subjectManager = SubjectManager.getInstance();
        subjectManager.setContext(this);

        subjectManager.setSchoolLessonSystem(null);

        subjectManager.load();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getFragmentManager();

                if (fm.getBackStackEntryCount() > 0) {
                    currentFragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
                    selectNavigationDrawerItem(currentFragmentTag);
                }
            }
        });


        findViewById(R.id.include).findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                closeKeyboard();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (getPreferences(MODE_PRIVATE).getBoolean("firstLaunch", true)) {
            setFragment(TutorialFragment.TAG);
        } else {

            /*Default Fragment:*/
            if (savedInstanceState != null)
                setFragment(savedInstanceState.getString("fragmentTag", HomeFragment.TAG));
            else
                setFragment(HomeFragment.TAG);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {

        //Disable Back Button while in Tutorial
        if (getPreferences(MODE_PRIVATE).getBoolean("firstLaunch", true))
            return;


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        FragmentManager fm = getFragmentManager();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 1) {

            fm.popBackStack();

            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_myday) {
            setFragment(HomeFragment.TAG);
        } else if (id == R.id.nav_timetable) {
            setFragment(TimetableFragment.TAG);
        } else if (id == R.id.nav_grades) {
            setFragment(GradesFragment.TAG);
        } else if (id == R.id.nav_tasks) {
            setFragment(TasksFragment.TAG);
        } else if (id == R.id.nav_calendar) {
            setFragment(CalendarFragment.TAG);
        }  else if (id == R.id.nav_securon) {
            setFragment(SecuronFragment.TAG);
        } else if (id == R.id.nav_representationplan) {
            setFragment(RepresentationPlan.TAG);
        }else if (id == R.id.nav_annanews) {
            setFragment(AnnanewsFragment.TAG);
        } else if (id == R.id.nav_settings) {
            setFragment(SettingsFragment.TAG);
        } else if (id == R.id.nav_feedback) {
            setFragment(FeedbackFragment.TAG);
        } else if (id == R.id.nav_share) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.pax.tk.annapp");
            startActivity(Intent.createChooser(i, getString(R.string.shareAnnApp)));
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean setFragment(String tag) {
        return setFragment(tag, new Bundle());
    }

    public boolean setFragment(String tag, Bundle bundle) {
        if (tag == null) return false;
        if (tag.equals(currentFragmentTag)) return true;

        Fragment fragment = null;
        switch (tag) {
            case HomeFragment.TAG:
                fragment = new HomeFragment();
                break;
            case TimetableFragment.TAG:
                fragment = new TimetableFragment();
                break;
            case GradesFragment.TAG:
                fragment = new GradesFragment();
                break;
            case GradeChildFragment.TAG:
                fragment = new GradeChildFragment();
                break;
            case TasksFragment.TAG:
                fragment = new TasksFragment();
                break;
            case CalendarFragment.TAG:
                fragment = new CalendarFragment();
                break;
            case SecuronFragment.TAG:
                fragment = new SecuronFragment();
                break;
            case RepresentationPlan.TAG:
                fragment = new RepresentationPlan();
                break;
            case AnnanewsFragment.TAG:
                fragment = new AnnanewsFragment();
                break;
            case SettingsFragment.TAG:
                fragment = new SettingsFragment();
                break;
            case FeedbackFragment.TAG:
                fragment = new FeedbackFragment();
                break;
            case TutorialFragment.TAG:
                fragment = new TutorialFragment();
                break;
        }

        if (fragment == null) {
            System.out.println("Main Activity: Your button for the fragment has no fragment defined to put into the Layout");
            return false;
        }

        fragment.setArguments(bundle);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(tag)
                .commit();

        currentFragmentTag = tag;
        return true;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.bundlekey_fragmentTag), currentFragmentTag);
    }

    public void selectNavigationDrawerItem(String tag) {

        switch (tag) {
            case HomeFragment.TAG:
                navigationView.getMenu().getItem(0).setChecked(true);
                break;
            case TimetableFragment.TAG:
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
            case GradesFragment.TAG:
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
            case TasksFragment.TAG:
                navigationView.getMenu().getItem(3).setChecked(true);
                break;
            case CalendarFragment.TAG:
                navigationView.getMenu().getItem(4).setChecked(true);
                break;
            case SecuronFragment.TAG:
                navigationView.getMenu().getItem(5).setChecked(true);
                break;
            case AnnanewsFragment.TAG:
                navigationView.getMenu().getItem(7).setChecked(true);
                break;
            case FeedbackFragment.TAG:
                navigationView.getMenu().getItem(9).setChecked(true);
                break;
            case SettingsFragment.TAG:
                navigationView.getMenu().getItem(10).setChecked(true);
                break;
            case GradeChildFragment.TAG:
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
            case RepresentationPlan.TAG:
                navigationView.getMenu().getItem(6).setChecked(true);
                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        subjectManager.save();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subjectManager.save();
    }

    @Override
    protected void onPause() {
        super.onPause();
        subjectManager.save();
    }

    private void closeKeyboard(){InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.drawer_layout).getWindowToken(), 0);}
}
