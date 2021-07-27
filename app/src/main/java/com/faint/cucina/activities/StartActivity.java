package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.DishGroup;
import com.faint.cucina.fragments.NewsFragment;
import com.faint.cucina.login_register.UserDataSP;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    int themeCode;
    private boolean accExists;

    Intent mainActIntent;
    Intent loginIntent;

    ArrayList<Cafe> cafes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        themeCode = Integer.parseInt(prefs.getString("change_theme", "0"));

        switch(themeCode) {
            case 0:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case 2:
                if(Build.VERSION.SDK_INT >= 29) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;
        }

        Thread initThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    initData();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(accExists) {
                        mainActIntent = new Intent(getApplicationContext(), MainActivity.class);

                        // TODO: check if user is authorized or not
                        mainActIntent
                                .putParcelableArrayListExtra("CAFE_LIST", cafes) // passing list via intent to MainActivity
                                .putExtra("THEME", themeCode);

                        startActivity(mainActIntent);
                    }
                    else {
                        loginIntent = new Intent(getApplicationContext(), AuthorizationActivity.class);
                        startActivity(loginIntent);
                    }
                    finish();
                }
            }
        };
        initThread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initData() {
        cafes = new ArrayList<>();

        accExists = UserDataSP.getInstance(this).isLoggedIn();
        if(accExists) {
            cafes.add( new Cafe(49.8247093178, 24.079084508121014, true, "ул. Садивныча 27", 1));
            cafes.add( new Cafe(46.6387464, 32.5679945, true, "ул. Суворова 1", 2));
            cafes.add( new Cafe(49.8202077703, 24.07606299, true, "ул. Евгена Коновальца 1", 3));
            cafes.add( new Cafe(49.8252491899, 24.0738223493, true, "пл. Митна", 4));
            cafes.add( new Cafe(49.8, 24, false, "ул. Степана Бандеры 7", 5));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}