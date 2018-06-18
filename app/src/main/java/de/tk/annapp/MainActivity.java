package de.tk.annapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.tk.annapp.Fragments.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SubjectManager subjectManager;

    private String currentFragmentTag= null;
    private Bundle currentFragmentBundle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.AppTheme_green);
        switch (getPreferences(MODE_PRIVATE).getInt(getString(R.string.bundleKey_colorThemePosition),0)){
            case 0:
                //TODO nur zum Anzeigen
                //setTheme(R.style.Tim);
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Creates instance of SubjectManager
        subjectManager = SubjectManager.getInstance();
        subjectManager.setContext(this);

        subjectManager.setSchoolLessonSystem(null);

        subjectManager.load();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /*Default Fragment:*/
        if (savedInstanceState != null)
            setFragment(savedInstanceState.getString(getString(R.string.bundlekey_fragmentTag),HomeFragment.TAG));
        else
            setFragment(HomeFragment.TAG);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment myFragment = getFragmentManager().findFragmentByTag(GradeChildFragment.class.getSimpleName());
        if (myFragment != null && myFragment.isVisible()) {

            Fragment fragment = new GradesFragment();

            Bundle args = new Bundle();
            fragment.setArguments(args);

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            drawer.closeDrawer(GravityCompat.START);
        }
        else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        } else if (id == R.id.nav_privattuition) {
            setFragment(PrivateTuitionFragment.TAG);
        } else if (id == R.id.nav_saleofschoolsupplies) {
            setFragment(SaleOfSchoolSuppliesFragment.TAG);
        } else if (id == R.id.nav_loststuff) {
            setFragment(LostStuffFragment.TAG);
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

        findViewById(R.id.grade).setVisibility(View.GONE);

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
            case PrivateTuitionFragment.TAG:
                fragment = new PrivateTuitionFragment();break;
            case SaleOfSchoolSuppliesFragment.TAG:
                fragment = new SaleOfSchoolSuppliesFragment(); break;
            case LostStuffFragment.TAG:
                fragment = new LostStuffFragment(); break;
            case AnnanewsFragment.TAG:
                fragment = new AnnanewsFragment(); break;
            case SettingsFragment.TAG:
                fragment = new SettingsFragment(); break;
            case FeedbackFragment.TAG:
                fragment = new FeedbackFragment(); break;
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
                //.addToBackStack(null)
                .commit();

        currentFragmentTag = tag;
        return true;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.bundlekey_fragmentTag),currentFragmentTag );
    }
}
