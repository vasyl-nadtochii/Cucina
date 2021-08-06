package com.faint.cucina.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.faint.cucina.R;

import java.util.List;

public class PhotoVPAdapter extends PagerAdapter {

    private final List<String> photos_urls;
    private final Context context;

    public PhotoVPAdapter(List<String> photos_urls, Context context) {
        this.photos_urls = photos_urls;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.photo_vp_item, container, false);

        ImageView imageView = view.findViewById(R.id.img);
        //imageView.setImageDrawable( photos.get(position) );
        Glide.with(context)
                .load(photos_urls.get(position))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);

        container.addView(view, 0);

        return view;
    }

    @Override
    public int getCount() {
        return photos_urls.size();
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
