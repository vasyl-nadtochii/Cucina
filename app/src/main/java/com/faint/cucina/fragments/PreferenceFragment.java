package com.faint.cucina.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.custom.UserMenusDBHelper;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private ListPreference themePref, citiesPref;
    private EditTextPreference namePref;
    private EditTextPreference phonePref;

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
        themePref = getPreferenceManager().findPreference("change_theme");
        assert themePref != null;
        themePref.setSummary(themes[posTheme]);

        // changing summary by interacting with preference
        themePref.setOnPreferenceChangeListener((preference, newValue) -> {
            themePref.setSummary(themes[Integer.parseInt(newValue.toString())]);
            notifyAboutChanges();

            return true;
        });

        // change username pref
        namePref = getPreferenceManager().findPreference("change_name");
        assert namePref != null;
        namePref.setText(UserDataSP.getInstance(requireActivity()).getUser().getName());
        namePref.setOnPreferenceChangeListener((preference, newValue) -> {

            if(newValue.toString().trim().length() > 0) {
                updateNameInDB(newValue.toString());
                return true;
            }
            else {
                return false;
            }
        });

        // change the password pref
        EditTextPreference passwordPref = getPreferenceManager().findPreference("change_password");

        // this code hides the password, because xml inputType doesn't work
        assert passwordPref != null;
        passwordPref.setText("");
        passwordPref.setOnBindEditTextListener(editText -> {

            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.selectAll();

            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(99)});
        });

        // and this code changes the password
        passwordPref.setOnPreferenceChangeListener((preference, newValue) -> {

            if(newValue.toString().trim().length() > 0 &&
                    newValue.toString().trim().matches("^[a-zA-Z0-9_]+$")) {
                updatePasswordInDB(newValue.toString());
            }
            else {
                Toast.makeText(requireActivity(),
                        requireActivity().getString(R.string.password_alert),
                        Toast.LENGTH_SHORT).show();
            }

            return false;
        });

        phonePref = getPreferenceManager().findPreference("change_phone");
        assert phonePref != null;
        phonePref.setOnBindEditTextListener(editText -> {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_PHONE);

            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        });

        phonePref.setText(UserDataSP.getInstance(requireActivity()).getUser().getPhone());
        phonePref.setOnPreferenceChangeListener((preference, newValue) -> {
            if(newValue.toString().trim().length() == 12 &&
                    !newValue.equals(UserDataSP.getInstance(requireActivity()).getUser().getPhone())) {

                updatePhoneInDB(newValue.toString());
                return true;
            }
            else if(newValue.equals(UserDataSP.getInstance(requireActivity()).getUser().getPhone())) {
                return false;
            }
            else if(newValue.toString().trim().length() < 12) {
                Toast.makeText(requireActivity(),
                        requireActivity().getString(R.string.phone_alert1),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                Toast.makeText(requireActivity(),
                        requireActivity().getString(R.string.phone_alert2),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // change city
        citiesPref = getPreferenceManager().findPreference("change_city");
        assert citiesPref != null;
        citiesPref.setValueIndex(Integer.parseInt(UserDataSP.getInstance(requireActivity()).getUser().getCity()) - 1);
        citiesPref.setOnPreferenceChangeListener((preference, newValue) -> {

            if(!newValue.toString().equals(UserDataSP.getInstance(requireContext()).getUser().getCity())) {
                updateCityInDB(newValue.toString());
                return true;
            }
            else
                return false;
        });

        // user agreement
        Preference userAgrPref = getPreferenceManager().findPreference("user_agreement");
        assert userAgrPref != null;
        userAgrPref.setOnPreferenceClickListener(preference -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://neurosite.tilda.ws/template"));
            startActivity(intent);

            return true;
        });

        Preference bugReportPref = getPreferenceManager().findPreference("bug_report");
        assert bugReportPref != null;
        bugReportPref.setOnPreferenceClickListener(preference -> {

            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm", Locale.getDefault());

            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"faint.incorp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,
                    "BUG REPORT - " + sdf.format(new Date()) + ". SDK ver. " + Build.VERSION.SDK_INT);
            intent.putExtra(Intent.EXTRA_TEXT, requireActivity().getString(R.string.describe_problem));
            intent.setPackage("com.google.android.gm");

            if (intent.resolveActivity(requireActivity().getPackageManager()) != null)
                startActivity(intent);
            else
                Toast.makeText(requireActivity(), "GMail App is not installed", Toast.LENGTH_SHORT).show();

            return true;
        });
    }

    private void updateNameInDB(final String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_NAME,
                response -> {
                    try {
                        if(response.trim().equals("SUCCESS")) {
                            namePref.setText(newValue);
                            UserDataSP.getInstance(requireContext()).changeData(UserDataSP.KEY_USERNAME, newValue);

                            MainActivity.user.setName(newValue);
                            MainActivity.dataChanged = true;

                            Toast.makeText(requireActivity(), requireActivity().getString(R.string.name_updated), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(requireActivity(),
                                    response, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(requireActivity(),
                        requireActivity().getString(R.string.network_err), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", UserDataSP.getInstance(requireActivity()).getUser().getPhone());
                params.put("username", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(request);
    }

    private void updatePasswordInDB(final String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_PASSWORD,
                response -> {
                    try {
                        if(response.trim().equals("SUCCESS")) {
                            Toast.makeText(requireActivity(), requireActivity().getString(R.string.password_updated), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(requireActivity(),
                                    response, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(requireActivity(),
                        requireActivity().getString(R.string.network_err), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", UserDataSP.getInstance(requireActivity()).getUser().getPhone());
                params.put("password", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(request);
    }

    public void updateCityInDB(final String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_CITY,
                response -> {
                    try {
                        if(response.trim().equals("SUCCESS")) {
                            UserDataSP.getInstance(requireContext()).changeData(UserDataSP.KEY_CITY, newValue);

                            MainActivity.user.setCity(newValue);
                            MainActivity.dataChanged = true;

                            Toast.makeText(requireActivity(),
                                    requireActivity().getString(R.string.city_updated), Toast.LENGTH_SHORT).show();
                        }
                        else if(response.trim().equals("ACTIVE_ORDERS")) {
                            Toast.makeText(requireActivity(),
                                    requireActivity().getString(R.string.cant_change_city),
                                    Toast.LENGTH_LONG).show();

                            citiesPref.setValueIndex(Integer.parseInt(UserDataSP.getInstance(requireActivity()).getUser().getCity()) - 1);
                        }
                        else {
                            Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(requireActivity(),
                        requireActivity().getString(R.string.network_err), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", UserDataSP.getInstance(requireActivity()).getUser().getPhone());
                params.put("city", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(request);
    }

    private void updatePhoneInDB(final String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_PHONE,
                response -> {
                    try {
                        switch (response.trim()) {
                            case "SUCCESS":
                                phonePref.setText(newValue);

                                UserMenusDBHelper myDB = new UserMenusDBHelper(requireActivity());
                                myDB.updatePhone(UserDataSP.getInstance(requireActivity()).getUser().getPhone(), newValue);

                                UserDataSP.getInstance(requireContext()).changeData(UserDataSP.KEY_PHONE, newValue);

                                MainActivity.user.setPhone(newValue);
                                MainActivity.dataChanged = true;

                                Toast.makeText(requireActivity(), requireActivity().getString(R.string.phone_changed), Toast.LENGTH_SHORT).show();
                                break;
                            case "ACTIVE_ORDERS":
                                Toast.makeText(requireActivity(),
                                        requireActivity().getString(R.string.has_active_orders),
                                        Toast.LENGTH_LONG).show();

                                phonePref.setText(UserDataSP.getInstance(requireActivity()).getUser().getPhone());
                                break;
                            case "PHONE_TAKEN":
                                Toast.makeText(requireActivity(),
                                        requireActivity().getString(R.string.phone_exists),
                                        Toast.LENGTH_LONG).show();

                                phonePref.setText(UserDataSP.getInstance(requireActivity()).getUser().getPhone());
                                break;
                            default:
                                Toast.makeText(requireActivity(),
                                        response, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(requireActivity(),
                        requireActivity().getString(R.string.network_err), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", UserDataSP.getInstance(requireActivity()).getUser().getPhone());
                params.put("new_phone", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(request);
    }

    private void notifyAboutChanges() {
        Toast.makeText(requireActivity(), R.string.notify_changes, Toast.LENGTH_SHORT).show();
    }
}
