package com.faint.cucina.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.faint.cucina.R;
import com.faint.cucina.classes.DishGroup;
import com.faint.cucina.interfaces.OrderInterface;

import java.util.ArrayList;
import java.util.Locale;

public class OrderLVAdapter extends ArrayAdapter<DishGroup> {

    private final Context context;
    private final ArrayList<DishGroup> categories;
    private final boolean usesActivityList;

    public OrderLVAdapter(ArrayList<DishGroup> categories, Context context, boolean usesActivityList) {
        super(context, R.layout.order_lv_item, categories);
        this.usesActivityList = usesActivityList;
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.order_lv_item, parent, false);

        TextView name = view.findViewById(R.id.category_name);

        ViewPager viewPager = view.findViewById(R.id.view_pager);

        OrderVPAdapter vpAdapter = new OrderVPAdapter(categories.get(position).getDishes(), context, usesActivityList);

        viewPager.setAdapter(vpAdapter);

        name.setText( categories.get(position).getName() );

        return view;
    }
}
