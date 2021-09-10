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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.faint.cucina.R;
import com.faint.cucina.activities.AddUserMenuActivity;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.fragments.OrderFragment;

import java.util.ArrayList;

public class DishForMenuLVAdapter extends ArrayAdapter<Dish> {

    private final ArrayList<Dish> dishes;
    private final Context context;

    private final int[] counter;

    public DishForMenuLVAdapter(@NonNull Context context, ArrayList<Dish> dishes) {
        super(context, R.layout.order_vp_item, dishes);
        this.context = context;
        this.dishes = dishes;
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
        TextView price = root.findViewById(R.id.price_field);

        counterView.setText(String.valueOf(counter[position]));

        CardView cardView = root.findViewById(R.id.card);
        cardView.setCardBackgroundColor(context.getColor(R.color.card_bg));

        Button plus, minus;

        plus = root.findViewById(R.id.btn_plus);
        plus.setOnClickListener(view -> {
            counter[position]++;
            counterView.setText(String.valueOf(counter[position]));

            AddUserMenuActivity.menu.addDish(dishes.get(position));

            if(AddUserMenuActivity.menu.getDishes().size() > 0) {
                AddUserMenuActivity.fab.show();
            }
        });

        minus = root.findViewById(R.id.btn_minus);
        minus.setOnClickListener(v -> {
            if (counter[position] > 0) {
                counter[position]--;

                counterView.setText(String.valueOf(counter[position]));

                AddUserMenuActivity.menu.removeDish(dishes.get(position));

                if(AddUserMenuActivity.menu.getDishes().size() == 0) {
                    AddUserMenuActivity.fab.hide();
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
