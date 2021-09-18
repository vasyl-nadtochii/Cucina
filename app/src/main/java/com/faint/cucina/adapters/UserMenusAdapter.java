package com.faint.cucina.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.activities.UserMenuActivity;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.classes.UserMenu;
import com.faint.cucina.custom.UserMenusDBHelper;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.fragments.OrderFragment;
import com.faint.cucina.login_register.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
        UserMenusDBHelper myDB = new UserMenusDBHelper(context);

        holder.warnImg.setVisibility(View.GONE);
        holder.name.setText(menus.get(position).getName());

        final StringBuilder[] dishesStr = {new StringBuilder()};

        for(int i = 0; i < menus.get(position).getDishes().size(); i++) {
            dishesStr[0].append(menus.get(position).getDishes().get(i).getName())
                    .append(" - ")
                    .append(menus.get(position).getDishes().get(i).getAmount());

            if(i != menus.get(position).getDishes().size() - 1) {
                dishesStr[0].append("\n");
            }
        }

        holder.dishes.setText(dishesStr[0].toString());

        ArrayList<String> dishNames = new ArrayList<>();
        for(OrderDish dish : menus.get(position).getDishes()) {
            dishNames.add(dish.getName());
        }

        holder.deleteImg.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Удалить польз. меню")
                    .setMessage("Вы уверены, что хотите удалить польз. меню \"" + menus.get(position).getName() + "\"?")
                    .setCancelable(true)
                    .setPositiveButton("Да", (dialogInterface, i) -> {
                        myDB.deleteOneRow(menus.get(position).getID());
                        menus.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Нет", null);

            final AlertDialog alert = builder.create();
            alert.show();
        });

        holder.editImg.setOnClickListener(view -> {
            Intent intent = new Intent(context, UserMenuActivity.class);

            intent.putExtra("EDITING", true);
            intent.putExtra("ID", menus.get(position).getID());
            intent.putExtra("NAME", menus.get(position).getName());
            intent.putExtra("DISH_LIST", menus.get(position).getDishes());

            context.startActivity(intent);
        });

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHECK_REMOVED_DISHES,
                response -> {
                    if(!response.trim().equals("OK")) {
                        Type listType = new TypeToken< ArrayList<String> >(){}.getType();
                        ArrayList<String> deletedNames = new Gson().fromJson(response, listType);

                        holder.warnImg.setVisibility(View.VISIBLE);
                        holder.warnImg.setOnClickListener(view -> {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            builder.setTitle("Обнаружены удалённые блюда")
                                    .setMessage("Похоже, в вашем пользовательском меню имеются удалённые блюда." +
                                            " Нажмите кнопку УДАЛИТЬ, чтобы удалить несуществующие блюда")
                                    .setCancelable(true)
                                    .setPositiveButton("УДАЛИТЬ",
                                        (dialogInterface, p) -> {
                                            if(menus.get(position).getDishes().size() == deletedNames.size()) {
                                                myDB.deleteOneRow(menus.get(position).getID());
                                                menus.remove(position);
                                            }
                                            else {
                                                menus.get(position).setDishes(removeDeleted(menus.get(position).getID(), deletedNames));
                                            }
                                            notifyDataSetChanged();
                                        });

                            final AlertDialog alert = builder.create();
                            alert.show();
                        });

                        holder.add.setClickable(false);
                        holder.remove.setClickable(false);
                    }
                    else {
                        holder.warnImg.setVisibility(View.GONE);
                        holder.add.setClickable(true);
                        holder.remove.setClickable(true);

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

    private ArrayList<OrderDish> removeDeleted(String id, ArrayList<String> deletedNames) {
        UserMenusDBHelper myDB = new UserMenusDBHelper(context);

        Cursor cursor = myDB.getDishes(id);
        ArrayList<OrderDish> dishes = new ArrayList<>();

        if(cursor != null) {
            cursor.moveToFirst();

            Gson gson = new Gson();
            Type listType = new TypeToken< ArrayList<OrderDish> >(){}.getType();

            dishes = gson.fromJson(cursor.getString(0), listType);

            for(String deletedName : deletedNames) {
                for(int i = 0; i < dishes.size(); i++) {
                    OrderDish dish = dishes.get(i);

                    if(dish.getName().equals(deletedName)) {
                        dishes.remove(i);
                        i--;
                    }
                }
            }

            myDB.setDishes(id, dishes);
        }
        else {
            Log.d("BUG", "Cursor is null");
        }

        return dishes;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView name, dishes, counterView;
        Button add, remove;
        ImageView warnImg, editImg, deleteImg;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            dishes = itemView.findViewById(R.id.dishes);
            counterView = itemView.findViewById(R.id.counter);

            add = itemView.findViewById(R.id.btn_add);
            remove = itemView.findViewById(R.id.btn_remove);

            warnImg = itemView.findViewById(R.id.warning_img);
            editImg = itemView.findViewById(R.id.editImg);
            deleteImg = itemView.findViewById(R.id.delImg);
        }
    }
}
