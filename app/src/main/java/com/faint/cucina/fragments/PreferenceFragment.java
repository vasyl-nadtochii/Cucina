package com.faint.cucina.fragments;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.faint.cucina.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    ListPreference themePreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_settings);

        // retrieving last theme (pos is retrieved from entry_values)
        final String[] themes = getActivity().getResources().getStringArray(R.array.themes_arr);
        int posTheme = Integer.parseInt(
                PreferenceManager
                        .getDefaultSharedPreferences(requireActivity())
                        .getString("change_theme", "NONE"));

        // init themePref and setting last theme in summary
        themePreference = getPreferenceManager().findPreference("change_theme");
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
    }

    private void notifyAboutChanges() {
        Toast.makeText(requireActivity(), R.string.notify_changes, Toast.LENGTH_SHORT).show();
    }
}
