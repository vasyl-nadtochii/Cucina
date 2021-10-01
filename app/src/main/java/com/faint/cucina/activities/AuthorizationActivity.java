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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.classes.User;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.faint.cucina.custom.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationActivity extends AppCompatActivity
            implements View.OnKeyListener, View.OnClickListener {

    private EditText phoneEt, passEt;
    private ProgressBar progressBar;
    private Button loginBtn;
    private TextView regLink;

    private boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        regLink = findViewById(R.id.reg);
        phoneEt = findViewById(R.id.mail_et);
        passEt = findViewById(R.id.pass_et);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.loadingPB);

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
            phoneEt.setError(getString(R.string.empty_phone));
            phoneEt.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passEt.setError(getString(R.string.empty_password));
            passEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setClickable(false);
        regLink.setClickable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                response -> {
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(response);

                        String msg;
                        //if no error in response
                        if (!obj.getBoolean("error")) {

                            msg = getString(R.string.success_auth);
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
                                msg = getString(R.string.incorrect_auth);
                            }
                            else {
                                msg = "Unexpected error.";
                            }
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                            loginBtn.setClickable(true);
                            regLink.setClickable(true);
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.network_err), Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.INVISIBLE);
                    loginBtn.setClickable(true);
                })
        {
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

        new Handler().postDelayed(() -> backPressedOnce = false, 2000);
    }
}