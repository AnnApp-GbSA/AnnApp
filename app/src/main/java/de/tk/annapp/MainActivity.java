package de.tk.annapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;

import de.tk.annapp.Fragments.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SubjectManager subjectManager;

    private String currentFragmentTag= null;
    private String prevFragmentTag = null;
    private Bundle currentFragmentBundle = null;

    private NavigationView navigationView = null;
    public static int colorScheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppTheme_green);
        switch (getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition),0)){
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
        d.setMinutes(d.getMinutes()+1);

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

                if(fm.getBackStackEntryCount() > 0) {
                    currentFragmentTag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
                    selectNavigationDrawerItem(currentFragmentTag);
                }
            }
        });


        findViewById(R.id.include).findViewById(R.id.toolbar).setVisibility(View.VISIBLE);

        findViewById(R.id.nav_view).setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(getPreferences(MODE_PRIVATE).getBoolean("firstLaunch", true)){
            setFragment(TutorialFragment.TAG);
        }else {

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*Fragment myFragment = getFragmentManager().findFragmentByTag(GradeChildFragment.class.getSimpleName());
        if (myFragment != null && myFragment.isVisible()) {
            setFragment(GradesFragment.TAG);
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
             super.onBackPressed();
        }*/

        FragmentManager fm = getFragmentManager();
        //Fragment myFragment = getFragmentManager().findFragmentByTag(prevFragmentTag);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if(fm.getBackStackEntryCount() > 1) {

            fm.popBackStack();

            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
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
        } else if (id == R.id.nav_representationplan) {
            setFragment(RepresentationPlan.TAG);
        } else if (id == R.id.nav_annanews) {
            setFragment(AnnanewsFragment.TAG);
        } else if (id == R.id.nav_settings) {
            setFragment(SettingsFragment.TAG);
        } else if (id == R.id.nav_feedback) {
            setFragment(FeedbackFragment.TAG);
        } else if (id == R.id.nav_share){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.pax.qbt.annapp");
            startActivity(Intent.createChooser(i, getString(R.string.shareAnnApp)));
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean setFragment(String tag){
        return setFragment(tag,new Bundle());
    }

    public boolean setFragment(String tag, Bundle bundle){
        if (tag==null) return false;
        if(tag.equals(currentFragmentTag)) return true;

        Fragment fragment = null;
        switch (tag){
            case HomeFragment.TAG:
                fragment = new HomeFragment(); break;
            case TimetableFragment.TAG:
                fragment = new TimetableFragment(); break;
            case GradesFragment.TAG:
                fragment = new GradesFragment(); break;
            case GradeChildFragment.TAG:
                fragment = new GradeChildFragment(); break;
            case TasksFragment.TAG:
                fragment = new TasksFragment(); break;
            case CalendarFragment.TAG:
                fragment = new CalendarFragment(); break;
            case RepresentationPlan.TAG:
                fragment = new RepresentationPlan(); break;
            case AnnanewsFragment.TAG:
                fragment = new AnnanewsFragment(); break;
            case SettingsFragment.TAG:
                fragment = new SettingsFragment(); break;
            case FeedbackFragment.TAG:
                fragment = new FeedbackFragment(); break;
            case TutorialFragment.TAG:
                fragment = new TutorialFragment(); break;
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
        outState.putSerializable(getString(R.string.bundlekey_fragmentTag),currentFragmentTag );
    }

    public void selectNavigationDrawerItem(String tag){

        switch (tag){
            case HomeFragment.TAG:
                navigationView.getMenu().getItem(0).setChecked(true); break;
            case TimetableFragment.TAG:
                navigationView.getMenu().getItem(1).setChecked(true); break;
            case GradesFragment.TAG:
                navigationView.getMenu().getItem(2).setChecked(true); break;
            case CalendarFragment.TAG:
                navigationView.getMenu().getItem(4).setChecked(true); break;
            case AnnanewsFragment.TAG:
                navigationView.getMenu().getItem(6).setChecked(true); break;
            case FeedbackFragment.TAG:
                navigationView.getMenu().getItem(8).setChecked(true); break;
            case SettingsFragment.TAG:
                navigationView.getMenu().getItem(9).setChecked(true); break;
            case TasksFragment.TAG:
                navigationView.getMenu().getItem(3).setChecked(true); break;
            case GradeChildFragment.TAG:
                navigationView.getMenu().getItem(2).setChecked(true); break;
        }

    }

    @Override
    protected void onStop() {
        subjectManager.save();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        subjectManager.save();
        super.onDestroy();
    }
}
