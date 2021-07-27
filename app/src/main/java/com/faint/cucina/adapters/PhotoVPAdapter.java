package com.faint.cucina.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.faint.cucina.R;

import java.util.List;

public class PhotoVPAdapter extends PagerAdapter {

    private final List<Drawable> photos;
    private final Context context;

    public PhotoVPAdapter(List<Drawable> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.photo_vp_item, container, false);

        ImageView imageView;
        imageView = view.findViewById(R.id.img);
        imageView.setImageDrawable( photos.get(position) );

        container.addView(view, 0);

        return view;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }
}
