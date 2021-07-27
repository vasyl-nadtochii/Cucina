package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.faint.cucina.R;

public class DishDescActivity extends AppCompatActivity {

    ImageView img;
    TextView nameTV, descTV, priceTV;

    String name, desc, imgUrl, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_desc);

        img = findViewById(R.id.img);
        nameTV = findViewById(R.id.name);
        descTV = findViewById(R.id.desc);
        priceTV = findViewById(R.id.price);

        nameTV.setPaintFlags(nameTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        descTV.setMovementMethod(new ScrollingMovementMethod());

        name = getIntent().getStringExtra("name");
        desc = getIntent().getStringExtra("desc");
        imgUrl = getIntent().getStringExtra("imgUrl");
        price = "Цена: " + getIntent().getIntExtra("price", -1) + " UAH";

        Glide.with(this)
                .load(imgUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .fitCenter()
                .into(img);

        nameTV.setText(name);
        descTV.setText(desc);
        priceTV.setText(price);
    }
}