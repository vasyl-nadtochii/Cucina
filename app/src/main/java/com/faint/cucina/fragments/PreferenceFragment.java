package com.faint.cucina.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.faint.cucina.R;

import java.util.Objects;

public class PreferenceFragment extends PreferenceFragmentCompat {

    ListPreference themePreference;
    Preference userAgrPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_settings);

        // retrieving last theme (pos is retrieved from entry_values)
        final String[] themes =
                requireActivity().getResources().getStringArray(R.array.themes_arr);

        int posTheme = Integer.parseInt(
                PreferenceManager
                        .getDefaultSharedPreferences(requireActivity())
                        .getString("change_theme", "NONE"));

        // init themePref and setting last theme in summary
        themePreference = getPreferenceManager().findPreference("change_theme");
        assert themePreference != null;
        themePreference.setSummary(themes[posTheme]);

        // changing summary by interacting with preference
        themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                themePreference.setSummary(themes[Integer.parseInt(newValue.toString())]);
                notifyAboutChanges();

                return true;
            }
        });
        
        userAgrPref = getPreferenceManager().findPreference("user_agreement");
        assert userAgrPref != null;
        userAgrPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://neurosite.tilda.ws/template"));
                startActivity(intent);

                return true;
            }
        });
    }

    private void notifyAboutChanges() {
        Toast.makeText(requireActivity(), R.string.notify_changes, Toast.LENGTH_SHORT).show();
    }
}
