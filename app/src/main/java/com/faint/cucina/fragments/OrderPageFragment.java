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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.adapters.OrderLVAdapter;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.DishGroup;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.login_register.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderPageFragment extends Fragment {

    private ArrayList<DishGroup> dishList;

    private ViewGroup errorLayout;
    private ProgressBar progressBar;
    private ListView listView;

    private OrderLVAdapter listAdapter;

    private final int categoryCode;   // 1 - rm, 2 - sc (!)

    public OrderPageFragment(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.inner_fragment_order,
                container, false);

        listView = view.findViewById(R.id.listView);
        errorLayout = view.findViewById(R.id.error_layout);
        progressBar = view.findViewById(R.id.progressBar);

        dishList = new ArrayList<>();
        getDishes();

        return view;
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

        MainActivity.requestFinished = false;

        StringRequest request =
            new StringRequest(Request.Method.GET, URLs.URL_GET_DISHES, response -> {
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        int category = object.getInt("category");

                        if(category == categoryCode) {
                            String name = object.getString("name");
                            String group = object.getString("dish_group");
                            String desc = object.getString("description");
                            String imgUrl = object.getString("img_url");
                            int price = object.getInt("price");

                            Dish dish = new Dish(name, desc, imgUrl, price);

                            boolean foundGroup = false;
                            if(dishList.size() != 0) {
                                for(DishGroup dishGroup : dishList) {
                                    if(dishGroup.getName().equals(group)) {
                                        foundGroup = true;
                                        dishGroup.addDish(dish);
                                    }
                                }
                            }

                            if(!foundGroup) {
                                ArrayList<Dish> dishes = new ArrayList<>();
                                dishes.add(dish);

                                DishGroup newDishGroup = new DishGroup(group, dishes);
                                dishList.add(newDishGroup);
                            }
                        }
                    }

                    listAdapter = new OrderLVAdapter(dishList, getActivity(), OrderFragment.forOrder);

                    listView.setAdapter(listAdapter);

                    progressBar.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                    MainActivity.requestFinished = true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
                    Toast.makeText(requireActivity(),
                            requireActivity().getString(R.string.network_err), Toast.LENGTH_SHORT).show();

                    if(dishList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        errorLayout.setVisibility(View.VISIBLE);
                    }

                    MainActivity.requestFinished = true;
                });

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(listAdapter != null)
            listAdapter.notifyDataSetChanged();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
