package com.faint.cucina.fragments.reg_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.StartActivity;
import com.faint.cucina.classes.User;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.faint.cucina.custom.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private EditText editTextUsername, editTextPassword, editTextPhone, editTextConfPassword;
    private String city;
    private CheckBox checkBox;
    private ProgressBar progressBar;
    private Button regBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register,
                container, false);

        Spinner spinner = root.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(requireContext(), R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editTextUsername = root.findViewById(R.id.name_et);
        editTextPassword = root.findViewById(R.id.pass_et);
        editTextConfPassword = root.findViewById(R.id.pass_conf_et);
        editTextPhone = root.findViewById(R.id.phone_et);
        progressBar = root.findViewById(R.id.loadingPB);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        regBtn = root.findViewById(R.id.regBtn);
        regBtn.setOnClickListener(this);

        checkBox = root.findViewById(R.id.checkbox);

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        city = String.valueOf(position + 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(requireActivity().getString(R.string.empty_name));
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(requireActivity().getString(R.string.empty_password));
            editTextPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError(requireActivity().getString(R.string.empty_phone));
            editTextPassword.requestFocus();
            return;
        }

        if (phone.length() < 12) {
            editTextPassword.setError(requireActivity().getString(R.string.incorrect_phone));
            editTextPassword.requestFocus();
            return;
        }

        if(!password.matches("^[a-zA-Z0-9_]+$")) {
            editTextPassword
                    .setError(requireActivity().getString(R.string.incorrect_password));
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(editTextConfPassword.getText().toString())) {
            editTextConfPassword.setError(requireActivity().getString(R.string.mismatch_passwords));
            editTextConfPassword.requestFocus();
            return;
        }

        if (!checkBox.isChecked()) {
            Toast.makeText(requireContext(),
                    requireActivity().getString(R.string.user_agreement_not_accepted),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        regBtn.setClickable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                response -> {
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(response);
                        //if no error in response
                        String msg;
                        if (!obj.getBoolean("error")) {
                            msg = requireActivity().getString(R.string.successful_registration);
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();

                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("user");

                            //creating a new user object
                            User user = new User(
                                    userJson.getInt("id"),
                                    userJson.getString("username"),
                                    userJson.getString("phone"),
                                    userJson.getString("city")
                            );

                            //storing the user in shared preferences
                            UserDataSP.getInstance(requireContext()).userLogin(user);

                            //starting the profile activity
                            requireActivity().finish();
                            startActivity(new Intent(requireContext(), StartActivity.class));
                        }
                        else {    // if err occurred
                            switch (obj.getString("message")) {
                                case "000":
                                    msg = requireActivity().getString(R.string.phone_exists);
                                    break;
                                case "010":
                                    msg = requireActivity().getString(R.string.network_err);
                                    break;
                                default:
                                    msg = "Unexpected error.";
                                    break;
                            }
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();

                            regBtn.setClickable(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                },
                error -> {
                    Toast.makeText(RegFragment.this.requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.INVISIBLE);
                    regBtn.setClickable(true);
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("phone", phone);
                params.put("city", city);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.regBtn) {
            registerUser();
        }
    }
}