package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.classes.User;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.faint.cucina.custom.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationActivity extends AppCompatActivity
            implements View.OnKeyListener, View.OnClickListener {

    TextView regLink;
    EditText phoneEt, passEt;
    Button loginBtn;

    private boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        regLink = findViewById(R.id.reg);

        phoneEt = findViewById(R.id.mail_et);
        passEt = findViewById(R.id.pass_et);
        loginBtn = findViewById(R.id.loginBtn);

        phoneEt.setOnKeyListener(this);
        passEt.setOnKeyListener(this);

        regLink.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {

            if (getCurrentFocus() != null) {
                InputMethodManager imm =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            view.clearFocus();

            return true;
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.reg) {
            startActivity(new Intent(this, RegistrationActivity.class));
        }
        else {
            userLogin();
        }
    }

    private void userLogin() {
        final String phone = phoneEt.getText().toString();
        final String password = passEt.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            phoneEt.setError("Пожалуйста, введите номер телефона");
            phoneEt.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passEt.setError("Пожалуйста, введите пароль");
            passEt.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            String msg;
                            //if no error in response
                            if (!obj.getBoolean("error")) {

                                msg = "Авторизация прошла успешно";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

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
                                UserDataSP.getInstance(getApplicationContext()).userLogin(user);
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                            }
                            else {
                                if ("11".equals(obj.getString("message"))) {
                                    msg = "Неверно введён номер телефона или пароль";
                                }
                                else {
                                    msg = "Unexpected error.";
                                }
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException | JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if(backPressedOnce) {
            super.onBackPressed();
            finishAffinity();   // and then it closes all activities
        }

        this.backPressedOnce = true;    // double click check
        Toast.makeText(this, R.string.press_again, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);
    }
}