package com.faint.cucina.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.UserDataSP;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {

    private int themeCode;
    private final String url = "http://192.168.1.8/cucina/getCafes.php";
    private String userCity;

    private Intent mainActIntent;

    private ArrayList<Cafe> cafes;
    private AlertDialog.Builder builder;

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

        builder = new AlertDialog.Builder(this);

        mainActIntent = new Intent(getApplicationContext(), MainActivity.class);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initData() {
        cafes = new ArrayList<>();

        boolean accExists = UserDataSP.getInstance(this).isLoggedIn();
        if(accExists) {
            userCity = UserDataSP.getInstance(this).getUser().getCity();

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);

                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    int id = object.getInt("id");
                                    double latitude = object.getDouble("latitude");
                                    double longitude = object.getDouble("longitude");
                                    int state = object.optInt("state");
                                    String address = object.getString("address");

                                    JSONArray jsonArray = new JSONArray(object.getString("img_urls"));
                                    ArrayList<String> urlList = new ArrayList<>();

                                    for(int j = 0; j < jsonArray.length(); j++) {
                                        urlList.add(jsonArray.getString(j));
                                    }

                                    Cafe cafe = new Cafe(latitude, longitude, state, address, id, urlList);
                                    cafes.add(cafe);
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            mainActIntent.putParcelableArrayListExtra("CAFE_LIST", cafes);
                            mainActIntent.putExtra("THEME", themeCode);
                            startActivity(mainActIntent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            builder.setMessage("Не удалось установить интернет-соединение! Проверьте подключение к интернету и повторите попытку")
                                    .setCancelable(false)
                                    .setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            initData();
                                        }
                                    })
                                    .setNegativeButton("Выйти", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finishAndRemoveTask();
                                        }
                                    });

                            final AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_city", userCity);

                    return params;
                }
            };

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
        else {
            Intent loginIntent = new Intent(getApplicationContext(), AuthorizationActivity.class);
            startActivity(loginIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}