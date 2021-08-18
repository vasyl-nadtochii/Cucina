package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.fragments.order_conf_fragments.ResultFragment;
import com.faint.cucina.login_register.URLs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ComplaintActivity extends AppCompatActivity {

    private TextView counterView;

    private String complaintText;

    private int cafeID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        cafeID = getIntent().getIntExtra("CAFE_ID", -1);
        userName = getIntent().getStringExtra("USER_NAME");

        EditText editText = findViewById(R.id.edit_text);
        counterView = findViewById(R.id.counter);

        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                complaintText = s.toString();

                String length = s.length() + "/600";
                counterView.setText(length);
            }

            public void afterTextChanged(Editable s) { }
        };

        editText.addTextChangedListener(mTextEditorWatcher);

        FloatingActionButton fab = findViewById(R.id.fab_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComplaint(cafeID, userName, complaintText);
            }
        });
    }

    private void postComplaint(final int cafeID, final String username, final String complaint) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_POST_COMPLAINT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("1")) {
                            Toast.makeText(getApplicationContext(),
                                    "Ваша жалоба успешно опубликована!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    response,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                "Не удалось подключиться к серверу, проверьте интернет-соединение",
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(cafeID));
                params.put("name", username);
                params.put("complaint", complaint);

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}