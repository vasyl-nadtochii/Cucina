package com.faint.cucina.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.classes.DishGroup;
import com.faint.cucina.fragments.MapFragment;
import com.faint.cucina.fragments.NewsFragment;
import com.faint.cucina.fragments.OrderFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static android.content.res.Configuration.UI_MODE_NIGHT_YES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<Announcement> eventList;
    public static ArrayList<Cafe> cafes;
    public static ArrayList<DishGroup> scGroups, rmGroups;

    DrawerLayout drawer;

    SharedPreferences prefs;

    public static int themeCode;

    private boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        themeCode = getIntent().getIntExtra("THEME", 0);

        // getting ArrayList we passed via intent -- it`s available to Fragments !
        eventList = getIntent().getParcelableArrayListExtra("EVENT_LIST");
        cafes = getIntent().getParcelableArrayListExtra("CAFE_LIST");

        scGroups = getIntent().getParcelableArrayListExtra("ORDER_SC_LIST");
        rmGroups = getIntent().getParcelableArrayListExtra("ORDER_RM_LIST");

        drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // setting controls for Action(Tool)Bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,
                    new NewsFragment() ).commit();
            navigationView.setCheckedItem(R.id.news);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int newTheme = Integer.parseInt(prefs.getString("change_theme", "NONE"));

        if(newTheme != themeCode) {
            Intent restartIntent = new Intent(this, StartActivity.class);
            startActivity(restartIntent);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START); // if menu is opened, it closes menu
        }
        else {
            if(backPressedOnce) {
                super.onBackPressed();
                finishAffinity();   // and then it closes all activities
            }

            this.backPressedOnce = true;    // double click check
            Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedOnce = false;
                }
            }, 2000);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.order:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace( R.id.fragment_container, new OrderFragment() )
                        .commit();
                break;
            case R.id.map:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace( R.id.fragment_container, new MapFragment() )
                        .commit();
                break;
            case R.id.news:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace( R.id.fragment_container, new NewsFragment() )
                        .commit();
                break;
            case R.id.setts:
                startActivity( new Intent(this, SettingsActivity.class) );
                break;
            case R.id.login:
                startActivity( new Intent(this, AuthorizationActivity.class) ); // debug only
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}