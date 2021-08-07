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

    public static ArrayList<Cafe> cafes;
    public static User user;

    DrawerLayout drawer;
    ProgressBar progressBar;
    SharedPreferences prefs;

    public static int themeCode;
    private boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        themeCode = getIntent().getIntExtra("THEME", 0);
        cafes = getIntent().getParcelableArrayListExtra("CAFE_LIST");

        progressBar = findViewById(R.id.progressBar);

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

        TextView nameTxt = navHeader.findViewById(R.id.name_txt);
        TextView phoneTxt = navHeader.findViewById(R.id.phone_txt);

        nameTxt.setText(user.getName());
        String phoneForm = "+" + user.getPhone();
        phoneTxt.setText(phoneForm);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace( R.id.fragment_container,
                    new NewsFragment() ).commit();
            navigationView.setCheckedItem(R.id.news);
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
                        .replace( R.id.fragment_container, new MapFragment(false) )
                        .commit();
                break;
            case R.id.news:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace( R.id.fragment_container, new NewsFragment() )
                        .commit();
                break;
            case R.id.user_orders:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace( R.id.fragment_container, new UserOrdersFragment() )
                        .commit();
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