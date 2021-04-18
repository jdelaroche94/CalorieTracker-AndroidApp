package com.example.calorietrackerv1;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import java.util.Calendar;

/**
 * This class is responsible to create a Navigation Drawer with and manage the different
 * fragment screens available to the user.
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlarmManager alarmMgr;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;

    /**
     * This method is responsible to create the navigation drawer and activate the
     * alarm which creates the consumption of the day.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView)
                findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("Calorie Tracker");


        Bundle parameters = new Bundle();
        parameters.putString("userId", getIntent().getStringExtra("userId"));
        parameters.putString("firstName", Input.getFirstName(getIntent().getStringExtra("givenName")));

        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(this, ScheduledIntentService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        //alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Set the alarm to start at 11:59 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 24 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60 *24, pendingIntent);


        Fragment homeFragment = new HomeFragment();
        homeFragment.setArguments(parameters);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
    }

    /**
     * This method is responsible to receive an item selected by the user and launch the
     * fragment which corresponds with the item selected.
     * @param item A Menu Item with the user choice.
     * @return A boolean value, true if the user select any option different than log Out
     * or false if they does not.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final String userId = getIntent().getStringExtra("userId");
        final String firstName = Input.getFirstName(getIntent().getStringExtra("givenName"));
        Bundle parameters = new Bundle();
        parameters.putString("userId", userId);
        parameters.putString("firstName", firstName);
        boolean logOut = false;
        int id = item.getItemId();
        Fragment nextFragment = null;
        switch (id) {
            case R.id.nav_home:
                nextFragment = new HomeFragment();
                nextFragment.setArguments(parameters);
                break;
            case R.id.nav_daily_diet_screen:
                nextFragment = new DailyDietFragment();
                nextFragment.setArguments(parameters);
                break;
            case R.id.nav_add_new_food_screen:
                nextFragment = new NewFoodFragment();
                nextFragment.setArguments(parameters);
                break;
            case R.id.nav_steps:
                nextFragment = new StepsFragment();
                nextFragment.setArguments(parameters);
                break;
            case R.id.nav_map:
                nextFragment = new MapFragment();
                nextFragment.setArguments(parameters);
                break;
            case R.id.nav_report_piechart:
                nextFragment = new PieChartReportFragment();
                nextFragment.setArguments(parameters);
                break;
            case R.id.nav_report_bargraph:
                nextFragment = new BarGraphReportFragment();
                nextFragment.setArguments(parameters);
                break;
            case R.id.nav_log_out:{
                logOut = true;
                break;
            }
        }
        if (logOut==false){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        else{
            Intent returnIntent = getIntent();
            String message = "Log Out";
            returnIntent.putExtra("message", message);
            setResult(RESULT_CANCELED, returnIntent);
            finish();
            return false;
        }
    }
}
