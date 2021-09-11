package com.faint.cucina.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.classes.UserMenu;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.fragments.OrderFragment;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

        ArrayList<String> dishNames = new ArrayList<>();
        for(OrderDish dish : dishes) {
            dishNames.add(dish.getName());
        }

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHECK_REMOVED_DISHES,
                response -> {
                    if(response.trim().equals("HAS_DELETED")) {
                        holder.warnImg.setVisibility(View.VISIBLE);
                        holder.warnImg.setOnClickListener(view -> {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            builder.setTitle("Обнаружены удалённые блюда")
                                    .setMessage("Похоже, в вашем пользовательском меню имеются удалённые блюда." +
                                            " Проверьте состав меню и удалите более несуществующие блюда!")
                                    .setCancelable(true)
                                    .setPositiveButton("Ок", null);

                            final AlertDialog alert = builder.create();
                            alert.show();
                        });

                        holder.add.setClickable(false);
                        holder.remove.setClickable(false);
                    }
                    else {
                        holder.add.setOnClickListener(view -> {
                            counter[position]++;
                            holder.counterView.setText(String.valueOf(counter[position]));

                            OrderFragment.menusInterface.addUserDishToOrder(menus.get(position));

                            if(!usesActivityList) {
                                if(OrderFragment.orderList.size() > 0) {
                                    OrderFragment.orderInterface.showHideFABNext(true);
                                }
                            }
                            else {
                                if(OrderActivity.order.getOrderList().size() > 0) {
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
                    }
                },
                error ->
                    Log.d("ERR:", Objects.requireNonNull(error.getMessage())))
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("dishes_json", new Gson().toJson(dishNames));
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
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

            name = itemView.findViewById(R.id.name);
            dishes = itemView.findViewById(R.id.dishes);
            counterView = itemView.findViewById(R.id.counter);

            add = itemView.findViewById(R.id.btn_add);
            remove = itemView.findViewById(R.id.btn_remove);

            warnImg = itemView.findViewById(R.id.warning_img);
        }
    }
}
