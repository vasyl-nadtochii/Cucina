package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.faint.cucina.R;

public class AuthorizationActivity extends AppCompatActivity
            implements View.OnKeyListener, View.OnClickListener {

    TextView regLink;
    EditText mailEt, passEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        regLink = findViewById(R.id.reg);

        mailEt = findViewById(R.id.mail_et);
        passEt = findViewById(R.id.pass_et);

        mailEt.setOnKeyListener(this);
        passEt.setOnKeyListener(this);

        regLink.setOnClickListener(this);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                (i == KeyEvent.KEYCODE_ENTER)) {

            if (getCurrentFocus() != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
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
    }
}