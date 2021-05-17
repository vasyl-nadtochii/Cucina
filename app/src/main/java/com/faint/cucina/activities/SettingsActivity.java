package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.faint.cucina.R;
import com.faint.cucina.fragments.PreferenceFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_preferences, new PreferenceFragment())
                .commit();
    }
}