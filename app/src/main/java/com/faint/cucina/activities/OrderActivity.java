package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.faint.cucina.R;
import com.faint.cucina.adapters.InnerFragmentPagerAdapter;
import com.faint.cucina.classes.Order;
import com.faint.cucina.custom.CustomViewPager;
import com.faint.cucina.fragments.MapFragment;
import com.faint.cucina.fragments.order_conf_fragments.ConfFragment;
import com.faint.cucina.fragments.order_conf_fragments.DescFragment;
import com.faint.cucina.fragments.order_conf_fragments.ResultFragment;
import com.faint.cucina.interfaces.OrderConfInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomViewPager viewPager;
    private FloatingActionButton btn;

    private List<Fragment> fragments;

    public static Order order;

    public static OrderConfInterface orderConfInterface;
    InnerFragmentPagerAdapter adapter;

    int position = 0;
    private boolean showingBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_conf);

        order = getIntent().getParcelableExtra("ORDER");

        btn = findViewById(R.id.btn);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);

        FragmentManager manager = getSupportFragmentManager();

        fragments = new ArrayList<>();
        fragments.add(new MapFragment(true));   // 0
        fragments.add(new DescFragment());  // 1
        fragments.add(new ConfFragment());  // 2
        fragments.add(new ResultFragment());   // 3

        adapter = new InnerFragmentPagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);

        btn.setOnClickListener(this);

        orderConfInterface = new OrderConfInterface() {
            @Override
            public void showBtnNext(boolean show) {
                if(show)
                    btn.show();
                else
                    btn.hide();
            }

            @Override
            public void goToNext() {
                position++;
                viewPager.setCurrentItem(position);
            }
        };
    }

    @Override
    public void onBackPressed() {
        if(position == 0 || position == fragments.size() - 1) {
            super.onBackPressed();
        }
        else {
            position--;
            adapter.notifyDataSetChanged();
            viewPager.setCurrentItem(position);

            if((position != 0 || position != fragments.size() - 1) && !showingBtn) {
                showingBtn = true;
                btn.show();
            }

            if(position == 0 || position == fragments.size() - 1) {
                showingBtn = false;
                btn.hide();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(position != fragments.size() - 1) {
            position++;

            if(position - 1 == 1) {
                order.setClarifications(DescFragment.descText);
            }

            adapter.notifyDataSetChanged();
            viewPager.setCurrentItem(position);

            if(position == 3) {
                // here we should try to send order to db, then show successful or not really successful msg
            }

            if(position == fragments.size() - 1 || position == 0) {
                showingBtn = false;
                btn.hide();
            }
        }
    }
}