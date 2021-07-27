package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.faint.cucina.R;
import com.faint.cucina.adapters.InnerFragmentPagerAdapter;
import com.faint.cucina.custom.CustomViewPager;
import com.faint.cucina.fragments.reg_fragments.GreetingFragment;
import com.faint.cucina.fragments.reg_fragments.InfoFragment;
import com.faint.cucina.fragments.reg_fragments.RegFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    CustomViewPager viewPager;
    FragmentManager manager;
    InnerFragmentPagerAdapter adapter;

    List<Fragment> fragments;

    FloatingActionButton btn;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btn = findViewById(R.id.btn);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);

        manager = getSupportFragmentManager();

        fragments = new ArrayList<>();
        fragments.add(new GreetingFragment());
        fragments.add(new InfoFragment());
        fragments.add(new RegFragment());

        adapter = new InnerFragmentPagerAdapter(manager, fragments);
        viewPager.setAdapter(adapter);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(position != fragments.size() - 1) {
            position++;
            viewPager.setCurrentItem(position);

            if(position == fragments.size() - 1)
                btn.hide();
        }
    }
}