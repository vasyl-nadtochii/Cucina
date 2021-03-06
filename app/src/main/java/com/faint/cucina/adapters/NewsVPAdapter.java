package com.faint.cucina.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.fragments.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsVPAdapter extends PagerAdapter {

    private final ArrayList<Announcement> events;
    private final Context context;

    public NewsVPAdapter(ArrayList<Announcement> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        // init root view (our 'card')
        View view = layoutInflater.inflate(R.layout.vp_item, container, false);

        ImageView imageView;
        TextView title, desc;
        CardView card;

        // init views
        card = view.findViewById(R.id.card);
        card.setBackgroundResource(R.drawable.ripple);

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        title.setText( events.get(position).getTitle() );
        desc.setText( events.get(position).getDesc() );

        // filling UI elements with data
        Glide.with(context)
                .load(events.get(position).getImageUrl())
                .placeholder(R.drawable.load_bg)
                .apply(new RequestOptions().override(800, 600))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        // Item onClickListener
        view.setOnClickListener(view1 -> {
            // also transition with selected announcement
            context.startActivity( NewsFragment.getNewsIntent(context, position, events) );
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }
}
