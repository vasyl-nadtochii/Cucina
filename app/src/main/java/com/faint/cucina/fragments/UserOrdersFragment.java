package com.faint.cucina.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.adapters.NewsLVAdapter;
import com.faint.cucina.adapters.NewsVPAdapter;
import com.faint.cucina.adapters.UserOrdersLVAdapter;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.classes.Order;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.UserDataSP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserOrdersFragment extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private ViewGroup msg_layout, err_layout;

    private ArrayList<Order> orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_orders, container, false);

        listView = root.findViewById(R.id.listView);
        msg_layout = root.findViewById(R.id.message_layout);
        err_layout = root.findViewById(R.id.error_layout);
        progressBar = root.findViewById(R.id.progressBar);

        orderList = new ArrayList<>();
        getOrders();

        refreshLayout = root.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrders();
                refreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void getOrders() {
        WifiManager wifi = (WifiManager)
                requireActivity().getSystemService(Context.WIFI_SERVICE);

        if (wifi.isWifiEnabled()) {
            if(!orderList.isEmpty()) {
                orderList.clear();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        String url = "http://192.168.1.8/cucina/getUserOrders.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                String jsonDishes = object.getString("order_list");
                                Gson gson = new Gson();
                                Type listType = new TypeToken< ArrayList<OrderDish> >(){}.getType();

                                ArrayList<OrderDish> dishes = gson.fromJson(jsonDishes, listType);
                                String orderClar = object.getString("order_clar");
                                int orderCafeID = object.getInt("order_cafe_id");
                                int state = object.getInt("state");
                                int id = object.getInt("id");

                                Order order = new Order(MainActivity.user.getName(), MainActivity.user.getPhone(),
                                        dishes, orderClar, orderCafeID, state, id);

                                orderList.add(order);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(orderList.size() == 0) {
                            msg_layout.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                        else {
                            UserOrdersLVAdapter adapter = new UserOrdersLVAdapter(requireContext(), orderList);
                            listView.setAdapter(adapter);

                            listView.setVisibility(View.VISIBLE);
                            msg_layout.setVisibility(View.GONE);
                        }

                        err_layout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireActivity(),
                                "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show();

                        if(orderList.isEmpty()) {
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);
                            err_layout.setVisibility(View.VISIBLE);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_phone", MainActivity.user.getPhone());

                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }
}