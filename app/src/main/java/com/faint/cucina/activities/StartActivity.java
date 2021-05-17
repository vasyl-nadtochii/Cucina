package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.DishGroup;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int themeCode = Integer.parseInt(prefs.getString("change_theme", "0"));

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

        ArrayList<Announcement> eventList = new ArrayList<>();
        ArrayList<Cafe> cafes = new ArrayList<>();

        ArrayList<DishGroup> scGroups = new ArrayList<>();
        ArrayList<DishGroup> rmGroups = new ArrayList<>();

        // TODO: replace manual insert with the automatic from DBs

        eventList.add( new Announcement(R.drawable.pizza, Announcement.TYPE_GOOD_NEWS, "Спец-предложение! Только три дня!",
                "Не пропустите бесплатную пиццу \"Маргарита\" при заказе от 200 грн. Предложение действует до 07.02.2021!") );

        eventList.add( new Announcement(R.drawable.kherson, Announcement.TYPE_NEW_LOCATION, "Мы открылись в г. Херсон!",
                "Присоединяйтесь к празднику по случаю открытия со скидками и весёлой атмосферой!") );

        eventList.add( new Announcement(R.drawable.covid, Announcement.TYPE_WARNING,"Меры противодействия COVID-19",
                "В связи с ослаблением карантинных ограничений, завдения вновь могут работать в обычном режиме, " +
                        "однако настоятельно просим соблюдать базовые меры предосторожности!однако настоятельно просим соблюдать " +
                        "базовые меры предосторожности!однако настоятельно просим соблюдать базовые меры предосторожности!однако " +
                        "настоятельно просим соблюдать базовые меры предосторожности!однако настоятельно просим соблюдать базовые меры " +
                        "предосторожности!однако настоятельно просим соблюдать базовые меры предосторожности!однако настоятельно просим соблюдать " +
                        "базовые меры предосторожности!однако настоятельно просим соблюдать базовые меры предосторожности!однако настоятельно просим " +
                        "соблюдать базовые меры предосторожности!однако настоятельно просим соблюдать базовые меры предосторожности!однако настоятельно" +
                        " просим соблюдать базовые меры предосторожности!однако настоятельно просим соблюдать базовые меры предосторожности!однако настоятельно" +
                        " просим соблюдать базовые меры предосторожности!однако настоятельно просим соблюдать базовые меры предосторожности!однако настоятельно " +
                        "просим соблюдать базовые меры предосторожности!") ); // just for testing scrollview

        eventList.add( new Announcement(R.drawable.lviv, Announcement.TYPE_BAD_NEWS, "Ремонтные работы в г. Львов",
                "В период с 03.02.2021 по 07.02.2021 в заведении на ул. Степана Бандеры 7 будут проводиться технические работы. " +
                        "Просим прощения за неудобства") );

        cafes.add( new Cafe(49.8247093178, 24.079084508121014, true, "ул. Садивныча 27"));
        cafes.add( new Cafe(49.828469, 24.07097015, true, "ул. Суворова 1"));
        cafes.add( new Cafe(49.8202077703, 24.07606299, true, "ул. Евгена Коновальца 1"));
        cafes.add( new Cafe(49.8252491899, 24.0738223493, true, "пл. Митна"));
        cafes.add( new Cafe(49.8, 24, false, "ул. Степана Бандеры 7"));

        scGroups.add( new DishGroup("Главное блюдо", new ArrayList<Dish>()) ); // main, salad, drinks, desserts
        scGroups.add( new DishGroup("Салаты", new ArrayList<Dish>()) );
        scGroups.add( new DishGroup("Десерты", new ArrayList<Dish>()) );
        scGroups.add( new DishGroup("Напитки", new ArrayList<Dish>()) );

        for(DishGroup group : scGroups) {
            // TODO: in future fill subgroups from db
            ArrayList<Dish> dishes = new ArrayList<>();

            dishes.add(new Dish("Капричоза", R.drawable.pizza));
            dishes.add(new Dish("Салат цезарь", R.drawable.bigtasty));
            dishes.add(new Dish("Сoca-Cola", R.drawable.cola));

            group.setDishes(dishes);
        }

        rmGroups.add( new DishGroup("Главное блюдо", new ArrayList<Dish>()) );
        rmGroups.add( new DishGroup("Салаты", new ArrayList<Dish>()) );
        rmGroups.add( new DishGroup("Десерты", new ArrayList<Dish>()) );
        rmGroups.add( new DishGroup("Напитки", new ArrayList<Dish>()) );

        for(DishGroup group : rmGroups) {
            // TODO: in future fill subgroups from db
            ArrayList<Dish> dishes = new ArrayList<>();

            dishes.add(new Dish("Капричоза", R.drawable.pizza));
            dishes.add(new Dish("Салат цезарь", R.drawable.bigtasty));
            dishes.add(new Dish("Сoca-Cola", R.drawable.cola));

            group.setDishes(dishes);
        }

        // here we should try to retrieve data from server (if User is authorized)

        final Intent trIntent = new Intent(this, MainActivity.class);  // init MainActivity
        trIntent.putParcelableArrayListExtra("EVENT_LIST", eventList)
                .putParcelableArrayListExtra("CAFE_LIST", cafes) // passing list via intent to MainActivity
                .putParcelableArrayListExtra("ORDER_SC_LIST", scGroups) // change bitmap to int (id at first time)
                .putParcelableArrayListExtra("ORDER_RM_LIST", rmGroups)
                .putExtra("THEME", themeCode);

        // TODO: check if user is authorized or not

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(trIntent);
            }
        }, 500); // Handler will be recursive (3-4? times), then app will notify user sth is wrong
    }
}