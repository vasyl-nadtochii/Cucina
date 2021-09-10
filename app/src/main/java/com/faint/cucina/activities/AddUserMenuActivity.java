package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.adapters.DishForMenuLVAdapter;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.UserMenu;
import com.faint.cucina.custom.UserMenusDBHelper;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.UserDataSP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddUserMenuActivity extends AppCompatActivity {

    private ListView listView;
    private ViewGroup errorLayout;
    private ProgressBar progressBar;
    private EditText nameEditText;
    public static FloatingActionButton fab;

    private ArrayList<Dish> dishList;

    private DishForMenuLVAdapter listAdapter;

    public static UserMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_dish);

        listView = findViewById(R.id.listView);
        errorLayout = findViewById(R.id.error_layout);
        progressBar = findViewById(R.id.progressBar);
        nameEditText = findViewById(R.id.et_name);
        fab = findViewById(R.id.fab);

        menu = new UserMenu(null, new ArrayList<>());
        dishList = new ArrayList<>();

        getDishes();

        fab.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            if(name.length() > 0) {
                menu.setName(name);

                UserMenusDBHelper myDB = new UserMenusDBHelper(getApplicationContext());

                myDB.addMenu(UserDataSP.getInstance(getApplicationContext()).getUser().getPhone(),
                        new Gson().toJson(menu.getDishes()), menu.getName());

                finish();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Введите название вашего меню!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDishes() {
        if (isNetworkAvailable()) {
            if(!dishList.isEmpty()) {
                dishList.clear();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        String url = "https://cucinacafeapp.000webhostapp.com/getDishes.php";
        StringRequest request =
                new StringRequest(Request.Method.GET, url, response -> {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            String name = object.getString("name");
                            String desc = object.getString("description");
                            String imgUrl = object.getString("img_url");
                            int price = object.getInt("price");

                            Dish dish = new Dish(name, desc, imgUrl, price);

                            dishList.add(dish);
                        }

                        listAdapter = new DishForMenuLVAdapter(AddUserMenuActivity.this, dishList);

                        progressBar.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);

                        listView.setAdapter(listAdapter);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show();

                    if(dishList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }

                    MainActivity.requestFinished = true;
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}