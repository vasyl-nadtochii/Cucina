package com.faint.cucina.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faint.cucina.R;
import com.faint.cucina.classes.OrderDish;

import java.util.ArrayList;

public class OrderConfLVAdapter extends ArrayAdapter<OrderDish> {

    private final Context context;
    ArrayList<OrderDish> list;

    public OrderConfLVAdapter(@NonNull Context context, ArrayList<OrderDish> list) {
        super(context, R.layout.order_conf_item, list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(R.layout.order_conf_item, parent, false);

        TextView name = view.findViewById(R.id.name);
        TextView amount = view.findViewById(R.id.amount);

        name.setText(list.get(position).getName());

        String amountStr = "Количество: " + list.get(position).getAmount();
        amount.setText(amountStr);

        return view;
    }
}
