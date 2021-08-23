package com.faint.cucina.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.faint.cucina.R;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.classes.User;
import com.faint.cucina.fragments.MapFragment;
import com.faint.cucina.fragments.NewsFragment;
import com.faint.cucina.fragments.OrderFragment;
import com.faint.cucina.fragments.UserOrdersFragment;
import com.faint.cucina.login_register.UserDataSP;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static User user;

    private DrawerLayout drawer;
    private SharedPreferences prefs;

    private TextView nameTxt, cityTxt, phoneTxt;

    public static int themeCode;
    private boolean backPressedOnce = false;
    public static boolean dataChanged;
    private String[] themes;

    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataChanged = false;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        themeCode = getIntent().getIntExtra("THEME", 0);
        user = UserDataSP.getInstance(this).getUser();

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

        View navHeader = navigationView.getHeaderView(0);

        nameTxt = navHeader.findViewById(R.id.name_txt);
        nameTxt.setText(user.getName());

        themes = getResources().getStringArray(R.array.cities);

        cityTxt = navHeader.findViewById(R.id.city_txt);
        cityTxt.setText(themes[Integer.parseInt(user.getCity()) - 1]);

        phoneTxt = navHeader.findViewById(R.id.phone_txt);
        String phoneForm = "+" + user.getPhone();
        phoneTxt.setText(phoneForm);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,
                    new NewsFragment() ).commit();

            navigationView.setCheckedItem(R.id.news);
            currentPage = R.id.news;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int newTheme = Integer.parseInt(prefs.getString("change_theme", "0"));

        if(newTheme != themeCode) {
            Intent restartIntent = new Intent(this, StartActivity.class);
            startActivity(restartIntent);
        }
        else if(dataChanged) {
            nameTxt.setText(user.getName());
            phoneTxt.setText(user.getPhone());
            cityTxt.setText(themes[Integer.parseInt(user.getCity()) - 1]);

            dataChanged = false;
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
                if(currentPage != R.id.order) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace( R.id.fragment_container, new OrderFragment() )
                            .commit();

                    currentPage = R.id.order;
                }
                break;
            case R.id.map:
                if(currentPage != R.id.map) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new MapFragment(false))
                            .commit();

                    currentPage = R.id.map;
                }
                break;
            case R.id.news:
                if(currentPage != R.id.news) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new NewsFragment())
                            .commit();
                    currentPage = R.id.news;
                }
                break;
            case R.id.user_orders:
                if(currentPage != R.id.user_orders) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new UserOrdersFragment())
                            .commit();
                    currentPage = R.id.user_orders;
                }
                break;
            case R.id.setts:
                startActivity( new Intent(this, SettingsActivity.class) );
                break;
            case R.id.logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage(getString(R.string.logout_conf))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                UserDataSP.getInstance(getApplicationContext()).logout();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null);

                final AlertDialog alert = builder.create();
                alert.show();

                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}