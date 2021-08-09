package com.faint.cucina.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PreferenceFragment extends PreferenceFragmentCompat {

    ListPreference themePreference;
    EditTextPreference namePref, passwordPref;
    Preference userAgrPref;

    private String userPhone;

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

        userPhone = UserDataSP.getInstance(requireActivity()).getUser().getPhone();

        // change username pref
        namePref = getPreferenceManager().findPreference("change_name");
        namePref.setText(UserDataSP.getInstance(requireActivity()).getUser().getName());
        namePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().trim().length() > 0) {
                    updateNameInDB(newValue.toString());
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        // change the password pref
        passwordPref = getPreferenceManager().findPreference("change_password");

        // this code hides the password, because xml inputType doesn't work
        passwordPref.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editText.selectAll();

                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(99)});
            }
        });

        // and this code changes the password
        passwordPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue.toString().trim().length() > 0
                        && !newValue.toString().contains(" ")) {

                    updatePasswordInDB(newValue.toString());
                    return true;
                }
                else {
                    return false;
                }
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

    private void updateNameInDB(final String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.trim().equals("SUCCESS")) {
                                namePref.setText(newValue);
                                UserDataSP.getInstance(requireContext()).changeName(newValue);

                                Toast.makeText(requireActivity(), "Имя успешно обновлено!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(requireActivity(),
                                        response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireActivity(),
                                "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", userPhone);
                params.put("username", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(request);
    }

    private void updatePasswordInDB(final String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.trim().equals("SUCCESS")) {
                                Toast.makeText(requireActivity(), "Пароль успешно обновлён!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(requireActivity(),
                                        response, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireActivity(),
                                "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", userPhone);
                params.put("password", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(request);
    }

    private void notifyAboutChanges() {
        Toast.makeText(requireActivity(), R.string.notify_changes, Toast.LENGTH_SHORT).show();
    }
}
