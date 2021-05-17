package com.faint.cucina.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faint.cucina.R;
import com.faint.cucina.adapters.OrderLVAdapter;
import com.faint.cucina.classes.DishGroup;

import java.util.ArrayList;

public class OrderPageFragment extends Fragment {

    private final ArrayList<DishGroup> dishList;

    public OrderPageFragment(ArrayList<DishGroup> dishList) {
        this.dishList = dishList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.inner_fragment_order,
                container, false);

        ListView listView = view.findViewById(R.id.listView);

        OrderLVAdapter listAdapter = new OrderLVAdapter(dishList, getActivity());

        listView.setAdapter(listAdapter);

        return view;
    }
}
