package com.faint.cucina.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.adapters.UserOrdersLVAdapter;
import com.faint.cucina.classes.Order;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserOrdersFragment extends Fragment {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private ViewGroup msg_layout, err_layout;
    private FloatingActionButton fab;

    private UserOrdersLVAdapter adapter;

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
        getOrders(false);

        refreshLayout = root.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> getOrders(true));

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder( requireActivity() );

            builder.setTitle(requireActivity().getString(R.string.delete_declined))
                    .setMessage(requireActivity().getString(R.string.delete_declined_body))
                    .setCancelable(true)
                    .setPositiveButton(requireActivity().getString(R.string.yes), (dialogInterface, i) -> removeOrders(-1, -1))
                    .setNegativeButton(requireActivity().getString(R.string.no), null);

            final AlertDialog alert = builder.create();
            alert.show();
        });

        return root;
    }

    private void getOrders(boolean refreshing) {
        if (isNetworkAvailable()) {
            if(!orderList.isEmpty()) {
                orderList.clear();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        MainActivity.requestFinished = false;

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_GET_ORDERS,
                response -> {
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

                        fab.show();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(orderList.size() == 0) {
                        msg_layout.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                    else {
                        adapter = new UserOrdersLVAdapter(requireContext(), orderList);
                        listView.setAdapter(adapter);

                        listView.setVisibility(View.VISIBLE);
                        msg_layout.setVisibility(View.GONE);
                    }

                    listView.setOnItemClickListener((adapterView, view, pos, l) -> {
                        final AlertDialog.Builder builder = new AlertDialog.Builder( requireActivity() );

                        StringBuilder dishesStr = new StringBuilder();
                        for(int i = 0; i < orderList.get(pos).getOrderList().size(); i++) {
                            OrderDish dish = orderList.get(pos).getOrderList().get(i);

                            dishesStr.append( dish.getName() ).append(" - ").append( dish.getAmount() );

                            if(i != orderList.get(pos).getOrderList().size() - 1)
                                dishesStr.append("\n");
                        }

                        builder.setTitle(requireContext().getString(R.string.your_order))
                                .setMessage(dishesStr)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null);

                        if(orderList.get(pos).getState() > 2) {
                            builder.setNegativeButton(requireContext().getString(R.string.delete), (dialogInterface, i) ->
                                    removeOrders(orderList.get(pos).getId(), pos));
                        }

                        final AlertDialog alert = builder.create();
                        alert.show();
                    });

                    err_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    if(refreshing) {
                        refreshLayout.setRefreshing(false);
                    }

                    MainActivity.requestFinished = true;
                },
                error -> {
                    Toast.makeText(requireActivity(),
                            requireContext().getString(R.string.network_err), Toast.LENGTH_SHORT).show();

                    if(orderList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        msg_layout.setVisibility(View.GONE);
                        err_layout.setVisibility(View.VISIBLE);
                    }

                    if(refreshing) {
                        refreshLayout.setRefreshing(false);
                    }

                    MainActivity.requestFinished = true;
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

    private void removeOrders(final int id, final int pos) {
        MainActivity.requestFinished = false;

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_REMOVE_ORDER,
                response -> {
                    if(response.trim().equals("1")) {
                        Toast.makeText(requireActivity(), requireContext().getString(R.string.successfully_deleted), Toast.LENGTH_SHORT).show();

                        if(pos != -1) {
                            orderList.remove(pos);
                        }
                        else {
                            for(int i = 0; i < orderList.size(); i++) {
                                Order order = orderList.get(i);

                                if(order.getState() > 2) {
                                    orderList.remove(i);
                                    i--;
                                }
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(requireActivity(), requireContext().getString(R.string.network_err), Toast.LENGTH_SHORT).show();
                    }

                    MainActivity.requestFinished = true;
                },
                error -> {
                    Toast.makeText(requireActivity(),
                            requireContext().getString(R.string.network_err),
                            Toast.LENGTH_SHORT).show();

                    MainActivity.requestFinished = true;
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("phone", UserDataSP.getInstance(requireContext()).getUser().getPhone());

                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}