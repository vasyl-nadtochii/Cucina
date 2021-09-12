package com.faint.cucina.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.faint.cucina.R;
import com.faint.cucina.activities.UserMenuActivity;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.OrderDish;

import java.util.ArrayList;

public class DishForMenuLVAdapter extends ArrayAdapter<Dish> {

    private final ArrayList<Dish> dishes;
    private ArrayList<OrderDish> dishesToUpdate;

    private final Context context;

    private final int[] counter;

    public DishForMenuLVAdapter(@NonNull Context context, ArrayList<Dish> dishes) {
        super(context, R.layout.order_vp_item, dishes);

        this.context = context;
        this.dishes = dishes;

        counter = new int[dishes.size()];
    }

    // use this in case we wanna edit user menu
    public DishForMenuLVAdapter(@NonNull Context context, ArrayList<Dish> dishes, ArrayList<OrderDish> dishesToUpdate) {
        super(context, R.layout.order_vp_item, dishes);

        this.context = context;
        this.dishes = dishes;
        this.dishesToUpdate = dishesToUpdate;

        counter = new int[dishes.size()];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View root = layoutInflater.inflate(R.layout.order_vp_item, parent, false);

        ImageView imageView = root.findViewById(R.id.img);

        TextView name = root.findViewById(R.id.name);
        final TextView counterView = root.findViewById(R.id.counter);

        if(dishesToUpdate != null) {
            UserMenuActivity.fab.show();

            for(int i = 0; i < dishesToUpdate.size(); i++) {
                if(dishesToUpdate.get(i).getName().equals(dishes.get(position).getName())) {
                    counter[position] += dishesToUpdate.get(i).getAmount();
                    break;
                }
            }
        }

        counterView.setText(String.valueOf(counter[position]));

        TextView price = root.findViewById(R.id.price_field);

        CardView cardView = root.findViewById(R.id.card);
        cardView.setCardBackgroundColor(context.getColor(R.color.card_bg));

        Button plus, minus;

        root.findViewById(R.id.infoBtn).setVisibility(View.GONE);

        plus = root.findViewById(R.id.btn_plus);
        plus.setOnClickListener(view -> {
            counter[position]++;
            counterView.setText(String.valueOf(counter[position]));

            UserMenuActivity.menu.addDish(dishes.get(position));

            if(UserMenuActivity.menu.getDishes().size() > 0) {
                UserMenuActivity.fab.setClickable(true);
                UserMenuActivity.fab.show();
            }
        });

        minus = root.findViewById(R.id.btn_minus);
        minus.setOnClickListener(v -> {
            if (counter[position] > 0) {
                counter[position]--;

                counterView.setText(String.valueOf(counter[position]));

                UserMenuActivity.menu.removeDish(dishes.get(position));

                if(UserMenuActivity.menu.getDishes().size() == 0) {
                    UserMenuActivity.fab.setClickable(false);
                    UserMenuActivity.fab.hide();
                }
                else if(dishesToUpdate != null && UserMenuActivity.menu.getDishes().size() > 0) {
                    UserMenuActivity.fab.setClickable(true);
                    UserMenuActivity.fab.show();
                }
            }
        });

        name.setText(dishes.get(position).getName());
        String priceStr = dishes.get(position).getPrice() + " UAH";
        price.setText(priceStr);

        Glide.with(context)
                .load(dishes.get(position).getImgUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        return root;
    }
}
