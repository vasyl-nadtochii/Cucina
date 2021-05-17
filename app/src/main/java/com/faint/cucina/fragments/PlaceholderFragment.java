package com.faint.cucina.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.adapters.OrderLVAdapter;
import com.faint.cucina.classes.DishGroup;
import com.faint.cucina.interfaces.OrderInterface;
import com.faint.cucina.models.PageViewModel;

import java.util.ArrayList;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public View view;
    public ListView list_menus, list_own;

    ArrayList<DishGroup> scList, rmList;

    OrderLVAdapter scAdapter, rmAdapter;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);

        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inner_fragment_order, container, false);

        list_menus = view.findViewById(R.id.list1);
        list_own = view.findViewById(R.id.list2);

        // sc -- single choice; rm -- ready-made
        scList = new ArrayList<>(MainActivity.scGroups);
        scAdapter = new OrderLVAdapter(scList, getActivity());

        rmList = new ArrayList<>(MainActivity.rmGroups);
        rmAdapter = new OrderLVAdapter(rmList, getActivity());

        list_own.setAdapter(scAdapter);
        list_menus.setAdapter(rmAdapter);

        if (pageViewModel.getIndex() == 1) {
            list_menus.setVisibility(View.VISIBLE);
            list_own.setVisibility(View.GONE);
        } else {
            list_own.setVisibility(View.VISIBLE);
            list_menus.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}