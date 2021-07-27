package com.faint.cucina.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.faint.cucina.R;
import com.faint.cucina.activities.DishDescActivity;
import com.faint.cucina.classes.Dish;
import com.faint.cucina.classes.OrderDish;
import com.faint.cucina.fragments.OrderFragment;
import com.faint.cucina.fragments.OrderPageFragment;

import java.util.ArrayList;
import java.util.Locale;

public class OrderVPAdapter extends PagerAdapter {

    private final ArrayList<Dish> dishes;
    private final Context context;
    View root;

    int[] counter;

    public OrderVPAdapter(ArrayList<Dish> dishes, Context context) {
        this.dishes = dishes;
        this.context = context;
        counter = new int[dishes.size()];
    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        root = layoutInflater.inflate(R.layout.order_vp_item, container, false);

        ImageView imageView = root.findViewById(R.id.img);

        TextView name = root.findViewById(R.id.name);
        final TextView counterView = root.findViewById(R.id.counter);
        TextView price = root.findViewById(R.id.price_field);

        counterView.setText(String.valueOf(counter[position]));

        CardView cardView = root.findViewById(R.id.card);
        cardView.setBackgroundResource(R.drawable.ripple);

        Button plus, minus;

        plus = root.findViewById(R.id.btn_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter[position]++;
                counterView.setText(String.valueOf(counter[position]));

                OrderFragment.orderInterface.addDishToOrder(dishes.get(position));

                if(OrderFragment.order.getOrderList().size() == 1) {
                    OrderFragment.orderInterface.showHideFAB(true);
                }
            }
        });

        minus = root.findViewById(R.id.btn_minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter[position] > 0)
                    counter[position]--;
                counterView.setText(String.valueOf(counter[position]));

                OrderFragment.orderInterface.removeDishFromOrder(dishes.get(position));

                if(OrderFragment.order.getOrderList().size() == 0) {
                    OrderFragment.orderInterface.showHideFAB(false);
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

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DishDescActivity.class);
                intent.putExtra("name", dishes.get(position).getName())
                        .putExtra("desc", dishes.get(position).getDesc())
                        .putExtra("imgUrl", dishes.get(position).getImgUrl())
                        .putExtra("price", dishes.get(position).getPrice());

                context.startActivity(intent);
            }
        });

        container.addView(root, 0);

        return root;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
