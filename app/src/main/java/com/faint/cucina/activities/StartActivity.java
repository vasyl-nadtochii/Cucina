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
import com.faint.cucina.login_register.UserDataSP;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    int themeCode;
    private boolean accExists;

    Intent mainActIntent;
    Intent loginIntent;

    ArrayList<Cafe> cafes;
    ArrayList<DishGroup> scGroups;
    ArrayList<DishGroup> rmGroups;

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
                                .putParcelableArrayListExtra("ORDER_SC_LIST", scGroups)
                                .putParcelableArrayListExtra("ORDER_RM_LIST", rmGroups)
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

        scGroups = new ArrayList<>();
        rmGroups = new ArrayList<>();

        accExists = UserDataSP.getInstance(this).isLoggedIn();
        if(accExists) {
            cafes.add( new Cafe(49.8247093178, 24.079084508121014, true, "ул. Садивныча 27"));
            cafes.add( new Cafe(49.828469, 24.07097015, true, "ул. Суворова 1"));
            cafes.add( new Cafe(49.8202077703, 24.07606299, true, "ул. Евгена Коновальца 1"));
            cafes.add( new Cafe(49.8252491899, 24.0738223493, true, "пл. Митна"));
            cafes.add( new Cafe(49.8, 24, false, "ул. Степана Бандеры 7"));

            scGroups.add( new DishGroup("Главное блюдо", new ArrayList<Dish>()) ); // main, salad, drinks, desserts
            scGroups.add( new DishGroup("Салаты", new ArrayList<Dish>()) );
            scGroups.add( new DishGroup("Десерты", new ArrayList<Dish>()) );
            scGroups.add( new DishGroup("Напитки", new ArrayList<Dish>()) );

            for (DishGroup group : scGroups) {
                // TODO: fill subgroups from db in future
                ArrayList<Dish> dishes = new ArrayList<>();

                dishes.add(new Dish("Капричоза", R.drawable.pizza));
                dishes.add(new Dish("Салат цезарь", R.drawable.bigtasty));
                dishes.add(new Dish("Сoca-Cola",  R.drawable.cola));

                group.setDishes(dishes);
            }

            rmGroups.add( new DishGroup("Главное блюдо", new ArrayList<Dish>()) );
            rmGroups.add( new DishGroup("Салаты", new ArrayList<Dish>()) );
            rmGroups.add( new DishGroup("Десерты", new ArrayList<Dish>()) );
            rmGroups.add( new DishGroup("Напитки", new ArrayList<Dish>()) );

            for (DishGroup group : rmGroups) {
                // TODO: in future fill subgroups from db
                ArrayList<Dish> dishes = new ArrayList<>();

                dishes.add(new Dish("Капричоза", R.drawable.pizza));
                dishes.add(new Dish("Салат цезарь", R.drawable.bigtasty));
                dishes.add(new Dish("Сoca-Cola", R.drawable.cola));

                group.setDishes(dishes);
            }
        }
    }
}