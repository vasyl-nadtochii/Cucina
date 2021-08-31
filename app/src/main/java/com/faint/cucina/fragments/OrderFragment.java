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
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.adapters.OrderPagerAdapter;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.interfaces.OrderInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private FloatingActionButton fabNext;

    public static ArrayList<OrderDish> orderList;

    public static OrderInterface orderInterface;

    public static boolean forOrder;

    public OrderFragment(boolean forOrder) {
        OrderFragment.forOrder = forOrder;
    }

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
        pages.add(new UserMenusFragment());

        OrderPagerAdapter orderPagerAdapter = new OrderPagerAdapter(requireActivity(),
                getChildFragmentManager(), pages);
        sectPager.setAdapter(orderPagerAdapter);
        sectPager.setOffscreenPageLimit(3);

        tabs.setupWithViewPager(sectPager);

        if(forOrder) {
            tabs.setBackgroundColor(requireActivity().getResources().getColor(R.color.start_bg, requireActivity().getTheme()));
        }

        orderList = new ArrayList<>();

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!forOrder) {
                    Intent intent = new Intent(getContext(), OrderActivity.class);

                    intent.putExtra("HAS_DISHES", true);
                    intent.putExtra("ORDER_LIST", orderList);

                    startActivity(intent);
                }
                else {
                    OrderActivity.orderConfInterface.goToNext();
                    OrderActivity.orderConfInterface.showBtnNext(true);
                }
            }
        });

        orderInterface = new OrderInterface() {
            @Override
            public void addDishToOrder(Dish dish) {
                if(!forOrder) {
                    addDish(dish);
                }
                else {
                    OrderActivity.order.addDishToOrder(dish);
                }
            }

            @Override
            public void removeDishFromOrder(Dish dish) {
                if(!forOrder) {
                    removeDish(dish);
                }
                else {
                    OrderActivity.order.removeDish(dish);
                }
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

    public void addDish(Dish dishToAdd) {
        boolean found = false;

        for(OrderDish dish : orderList) {
            if(dish.getName().equals(dishToAdd.getName())) {
                found = true;
                dish.setAmount(dish.getAmount() + 1);

                break;
            }
        }

        if(!found) {
            orderList.add(new OrderDish(1, dishToAdd.getName()));
        }
    }

    public void removeDish(Dish dishToRemove) {
        for(OrderDish dish : orderList) {
            if(dish.getName().equals(dishToRemove.getName())) {
                dish.setAmount(dish.getAmount() - 1);

                if(dish.getAmount() == 0) {
                    orderList.remove(dish);
                }

                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
