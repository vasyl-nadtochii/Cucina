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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    View root;
    EditText editTextUsername, editTextPassword, editTextPhone, editTextConfPassword;
    Spinner spinner;
    String city;
    Button regBtn;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_register,
                container, false);

        spinner = root.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(requireContext(), R.array.cities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editTextUsername = root.findViewById(R.id.name_et);
        editTextPassword = root.findViewById(R.id.pass_et);
        editTextConfPassword = root.findViewById(R.id.pass_conf_et);
        editTextPhone = root.findViewById(R.id.phone_et);

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

        /*  CITY CODES:
            Kyiv - 1
            Kharkiv - 2
            Lviv - 3
            Dnipro - 4
            Odesa - 5
            Iv-Fr - 6
            Kherson - 7  */
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Пожалуйста, введите имя!");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Введите пароль!");
            editTextPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            editTextPassword.setError("Введите номер телефона!");
            editTextPassword.requestFocus();
            return;
        }

        if (phone.length() < 12) {
            editTextPassword.setError("Введите корректный номер телефона!");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(editTextConfPassword.getText().toString())) {
            editTextConfPassword.setError("Пароли не совпадают!");
            editTextConfPassword.requestFocus();
            return;
        }

        if (!checkBox.isChecked()) {
            Toast.makeText(requireContext(),
                    "Вы должны принять условия Пользовательского Соглашения для продолжения регистрации!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            //if no error in response
                            String msg;
                            if (!obj.getBoolean("error")) {
                                msg = "Регистрация прошла успешно";
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
                                        msg = "Пользователь с такими данными уже зарегистрирован";
                                        break;
                                    case "010":
                                        msg = "Запрашиваемые параметры недоступны. Повторите позже.";
                                        break;
                                    default:
                                        msg = "Unexpected error.";
                                        break;
                                }
                                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegFragment.this.requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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