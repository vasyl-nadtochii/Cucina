package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

        boolean accExists = UserDataSP.getInstance(this).isLoggedIn();
        if(accExists) {
            final Intent mainActIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainActIntent.putExtra("THEME", themeCode);

            FirebaseMessaging
                    .getInstance()
                    .getToken()
                    .addOnCompleteListener(task -> {

                        if(!task.isSuccessful()) {
                            return;
                        }

                        String token = task.getResult();

                        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHECK_TOKEN,
                                response -> {
                                    if(!response.trim().equals("SUCCESS")) {
                                        Log.d("BUG:", response);
                                    }
                                },
                                error -> {
                                    Log.d("ERR:", Objects.requireNonNull(error.getMessage()));
                                })
                        {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("phone", UserDataSP.getInstance(getApplicationContext()).getUser().getPhone());
                                params.put("token", token);
                                return params;
                            }
                        };

                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                    });

            Handler handler = new Handler();
            handler.postDelayed(() -> startActivity(mainActIntent), 100);
        }
        else {
            Intent loginIntent = new Intent(getApplicationContext(), AuthorizationActivity.class);
            startActivity(loginIntent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}