package com.faint.cucina.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.adapters.SectionsPagerAdapter;
import com.faint.cucina.classes.DishGroup;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderFragment extends Fragment {

    View root;
    TabLayout tabs;
    ViewPager sectPager;

    SectionsPagerAdapter sectionsPagerAdapter;

    ArrayList<DishGroup> rmGroups, scGroups; // rm - ready-made, sc - self choice

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order, container, false);

        sectPager = root.findViewById(R.id.view_pager);
        tabs = root.findViewById(R.id.tabs);

        List<Fragment> pages = new ArrayList<>();
        pages.add(new OrderPageFragment(1));
        pages.add(new OrderPageFragment(2));

        sectionsPagerAdapter = new SectionsPagerAdapter(requireActivity(),
                requireActivity().getSupportFragmentManager(), pages);
        sectPager.setAdapter(sectionsPagerAdapter);

        tabs.setupWithViewPager(sectPager);

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
