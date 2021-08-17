package com.faint.cucina.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.adapters.SectionsPagerAdapter;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.Order;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.classes.User;
import com.faint.cucina.interfaces.OrderInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private FloatingActionButton fabNext;

    public static Order order;
    public static OrderInterface orderInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        ViewPager sectPager = root.findViewById(R.id.view_pager);
        TabLayout tabs = root.findViewById(R.id.tabs);
        fabNext = root.findViewById(R.id.fabNext);

        List<Fragment> pages = new ArrayList<>();
        pages.add(new OrderPageFragment(1)); // 1 - ready-made
        pages.add(new OrderPageFragment(2)); // 2 - self choice

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(requireActivity(),
                requireActivity().getSupportFragmentManager(), pages);
        sectPager.setAdapter(sectionsPagerAdapter);

        tabs.setupWithViewPager(sectPager);

        User user = MainActivity.user;
        order = new Order(user.getName(), user.getPhone(), new ArrayList<OrderDish>(), "", -1, 0, -1);

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                intent.putExtra("ORDER", order);
                startActivity(intent);
            }
        });

        orderInterface = new OrderInterface() {
            @Override
            public void addDishToOrder(Dish dish) {
                order.addDishToOrder(dish);
            }

            @Override
            public void removeDishFromOrder(Dish dish) {
                order.removeDish(dish);
            }

            @Override
            public void showHideFAB(boolean show) {
                if(show)
                    fabNext.show();
                else
                    fabNext.hide();
            }
        };

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
