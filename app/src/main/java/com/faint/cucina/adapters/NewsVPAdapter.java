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

import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.fragments.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsVPAdapter extends PagerAdapter {

    private ArrayList<Announcement> events; // !!!
    private Context context;

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

        // filling UI elements with data
        imageView.setImageResource( events.get(position).getImage() );
        title.setText( events.get(position).getTitle() );
        desc.setText( events.get(position).getDesc() );

        // Item onClickListener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // also transition with selected announcement
                context.startActivity( NewsFragment.getNewsIntent(context, position) );
            }
        });

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }
}
