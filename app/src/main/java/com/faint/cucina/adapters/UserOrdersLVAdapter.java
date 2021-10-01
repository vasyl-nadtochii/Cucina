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
import androidx.cardview.widget.CardView;

import com.faint.cucina.R;
import com.faint.cucina.classes.Order;

import java.util.ArrayList;

public class UserOrdersLVAdapter extends ArrayAdapter<Order> {

    private final Context context;
    private final ArrayList<Order> orders;

    public UserOrdersLVAdapter(@NonNull Context context, ArrayList<Order> orders) {
        super(context, R.layout.user_orders_lv_item, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View item = layoutInflater.inflate(R.layout.user_orders_lv_item, parent, false);

        CardView cardView = item.findViewById(R.id.card);
        TextView header = item.findViewById(R.id.header);
        TextView desc = item.findViewById(R.id.desc);

        cardView.setBackgroundResource(R.drawable.ripple);

        String stateStr = "";
        switch (orders.get(position).getState()) {
            case 0:
                stateStr = context.getString(R.string.waiting_state);
                break;
            case 1:
                header.setTextSize(17);
                stateStr = context.getString(R.string.preparing_state);
                break;
            case 2:
                header.setTextColor(context.getResources().getColor(R.color.green, context.getTheme()));
                stateStr = context.getString(R.string.ready_state);
                break;
            case 3:
                header.setTextColor(context.getResources().getColor(R.color.red, context.getTheme()));
                stateStr = context.getString(R.string.declined_state);
                break;
        }

        String headStr = "ID: " + orders.get(position).getId() + ". "
                + context.getString(R.string.state) + " " + stateStr;
        header.setText(headStr);

        String descStr = context.getString(R.string.clarification) + " " + orders.get(position).getClarifications();
        desc.setText(descStr);

        return item;
    }
}
