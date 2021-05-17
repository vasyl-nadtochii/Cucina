package com.faint.cucina.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;

import java.util.ArrayList;

public class NewsLVAdapter extends ArrayAdapter<Announcement> {

    private ArrayList<Announcement> events;
    private Context context;

    public NewsLVAdapter(ArrayList<Announcement> events, Context context) {
        super(context, R.layout.lv_item, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder")
        View item = layoutInflater.inflate(R.layout.lv_item, parent, false);

        CardView card = item.findViewById(R.id.card);
        TextView title = item.findViewById(R.id.tView);
        ImageView icon = item.findViewById(R.id.icon);

        card.setBackgroundResource(R.drawable.ripple);

        title.setText(events.get(position).getTitle());

        // icon should change depending on the news` type
        switch ( events.get(position).getType() ) {
            case Announcement.TYPE_NEW_LOCATION:
                icon.setImageResource(R.drawable.ic_new_location);
                break;
            case Announcement.TYPE_GOOD_NEWS:
                icon.setImageResource(R.drawable.ic_good_news);
                break;
            case Announcement.TYPE_WARNING:
                icon.setImageResource(R.drawable.ic_warning);
                break;
            case Announcement.TYPE_BAD_NEWS:
                icon.setImageResource(R.drawable.ic_bad_news);
                break;
        }

        return item;
    }
}
