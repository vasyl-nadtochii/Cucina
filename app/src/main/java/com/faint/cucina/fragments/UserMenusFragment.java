package com.faint.cucina.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faint.cucina.R;
import com.faint.cucina.adapters.UserMenusAdapter;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.classes.UserMenu;
import com.faint.cucina.custom.UserMenusDBHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class UserMenusFragment extends Fragment {

    private UserMenusDBHelper myDB;
    private ArrayList<UserMenu> menus;
    private RecyclerView recyclerView;
    private ViewGroup layoutMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_dishes, container, false);

        myDB = new UserMenusDBHelper(requireActivity());

        layoutMsg = root.findViewById(R.id.message_layout);
        recyclerView = root.findViewById(R.id.recyclerView);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        menus = new ArrayList<>();

        getData();
    }

    private void getData() {
        Cursor cursor = myDB.readData();

        if(cursor.getCount() == 0) {
            layoutMsg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            while(cursor.moveToNext()) {
                Gson gson = new Gson();
                Type listType = new TypeToken< ArrayList<OrderDish> >(){}.getType();

                ArrayList<OrderDish> dishes = gson.fromJson(cursor.getString(2), listType);

                menus.add(new UserMenu(cursor.getString(0), cursor.getString(1), dishes));
            }

            recyclerView.setAdapter(new UserMenusAdapter(menus, requireContext(), OrderFragment.forOrder));
            recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

            layoutMsg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}