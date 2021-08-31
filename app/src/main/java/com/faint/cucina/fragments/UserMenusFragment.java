package com.faint.cucina.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.faint.cucina.R;
import com.faint.cucina.activities.AddUserDishActivity;
import com.faint.cucina.adapters.UserMenusAdapter;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.classes.UserMenu;
import com.faint.cucina.custom.UserMenusDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserMenusFragment extends Fragment {

    private UserMenusDBHelper myDB;
    private ArrayList<UserMenu> menus;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user_dishes, container, false);

        myDB = new UserMenusDBHelper(requireActivity());

        recyclerView = root.findViewById(R.id.recyclerView);
        FloatingActionButton fabAdd = root.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireActivity(), AddUserDishActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        menus = new ArrayList<>();
        getData();

        recyclerView.setAdapter(new UserMenusAdapter(menus, requireContext()));
    }

    private void getData() {
        Cursor cursor = myDB.readData();

        if(cursor.getCount() == 0) {
            Toast.makeText(requireActivity(), "Похоже, у вас нет польз. меню", Toast.LENGTH_SHORT).show();
        }
        else {
            while(cursor.moveToNext()) {
                Gson gson = new Gson();
                Type listType = new TypeToken< ArrayList<OrderDish> >(){}.getType();

                ArrayList<OrderDish> dishes = gson.fromJson(cursor.getString(1), listType);

                menus.add(new UserMenu(cursor.getString(0), dishes));
            }
        }
    }
}