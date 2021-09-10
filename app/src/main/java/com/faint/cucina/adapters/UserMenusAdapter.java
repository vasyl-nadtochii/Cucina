package com.faint.cucina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faint.cucina.R;
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.classes.UserMenu;
import com.faint.cucina.fragments.OrderFragment;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserMenusAdapter extends RecyclerView.Adapter<UserMenusAdapter.CustomViewHolder> {

    private final Context context;
    private final ArrayList<UserMenu> menus;
    private final int[] counter;
    private final boolean usesActivityList;

    public UserMenusAdapter(ArrayList<UserMenu> menus, Context context, boolean usesActivityList) {
        this.menus = menus;
        this.context = context;
        this.usesActivityList = usesActivityList;
        counter = new int[menus.size()];
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_menu_item, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.name.setText(menus.get(position).getName());

        StringBuilder dishesStr = new StringBuilder();
        ArrayList<OrderDish> dishes = menus.get(position).getDishes();

        for(int i = 0; i < dishes.size(); i++) {
            dishesStr.append(dishes.get(i).getName());

            if(i != dishes.size() - 1) {
                dishesStr.append("\n");
            }
        }

        holder.dishes.setText(dishesStr.toString());

        holder.add.setOnClickListener(view -> {
            counter[position]++;
            holder.counterView.setText(String.valueOf(counter[position]));

            OrderFragment.menusInterface.addUserDishToOrder(menus.get(position));

            if(!usesActivityList) {
                if(OrderFragment.orderList.size() == 1) {
                    OrderFragment.orderInterface.showHideFABNext(true);
                }
            }
            else {
                if(OrderActivity.order.getOrderList().size() == 1) {
                    OrderFragment.orderInterface.showHideFABNext(true);
                }
            }
        });

        holder.remove.setOnClickListener(view -> {
            if (counter[position] > 0) {
                counter[position]--;

                holder.counterView.setText(String.valueOf(counter[position]));

                OrderFragment.menusInterface.removeUserDishFromOrder(menus.get(position));

                if(!usesActivityList) {
                    if(OrderFragment.orderList.size() == 0) {
                        OrderFragment.orderInterface.showHideFABNext(false);
                    }
                }
                else {
                    if(OrderActivity.order.getOrderList().size() == 0) {
                        OrderFragment.orderInterface.showHideFABNext(false);
                    }
                }
            }
        });

        ArrayList<String> dishNames = new ArrayList<>();
        for(OrderDish dish : dishes) {
            dishNames.add(dish.getName());
        }

        String jsonDishes = new Gson().toJson(dishNames);

        if(checkForDeletedDishes(jsonDishes)) {
            holder.warnImg.setVisibility(View.VISIBLE);
            holder.add.setClickable(false);
            holder.remove.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView name, dishes, counterView;
        Button add, remove;
        ImageView warnImg;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            dishes = (TextView) itemView.findViewById(R.id.dishes);
            counterView = (TextView) itemView.findViewById(R.id.counter);

            add = (Button) itemView.findViewById(R.id.btn_add);
            remove = (Button) itemView.findViewById(R.id.btn_remove);

            warnImg = (ImageView) itemView.findViewById(R.id.warning_img);
        }
    }

    private boolean checkForDeletedDishes(String jsonArray) {

        return false;
    }
}
