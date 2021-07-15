package com.faint.cucina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.faint.cucina.R;
import com.faint.cucina.classes.Dish;

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
        counterView.setText(String.valueOf(counter[position]));

        Button plus, minus;

        plus = root.findViewById(R.id.btn_plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter[position]++;
                counterView.setText(String.valueOf(counter[position]));
            }
        });

        minus = root.findViewById(R.id.btn_minus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter[position] > 0)
                    counter[position]--;
                counterView.setText(String.valueOf(counter[position]));
            }
        });

        name.setText(dishes.get(position).getName());

        Glide.with(context)
                .load(dishes.get(position).getImgUrl())
                .placeholder(R.drawable.load_bg)
                .apply(new RequestOptions().override(500, 400))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

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
