package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faint.cucina.R;
import com.faint.cucina.adapters.PhotoVPAdapter;
import com.faint.cucina.classes.Cafe;

import java.util.ArrayList;

public class CafeActivity extends AppCompatActivity {

    TextView address, schedule, state;
    ViewPager viewPager;
    Button orderBtn;

    private Handler handler;

    PhotoVPAdapter photoVPAdapter;

    Cafe cafe;

    private int page = 0;
    private final int delay = 5000;
    boolean touched = false;

    // viewpager auto scroll
    Runnable runnable = new Runnable() {
        public void run() {
            if (photoVPAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);

        address = findViewById(R.id.address);
        schedule = findViewById(R.id.schedule);
        state = findViewById(R.id.state);

        orderBtn = findViewById(R.id.order_btn);

        cafe = getIntent().getParcelableExtra("CAFE");

        assert cafe != null;
        address.setSelected(true);
        address.setText(getResources().getString(R.string.cafe_on) + " " + cafe.getAddress());

        if(cafe.getState() == 1) {
            state.setText(R.string.opened);
            state.setTextColor(getColor(R.color.green));
        }
        else {
            state.setText(R.string.closed);
            // TODO: here should be the reason why it`s not working
            state.setTextColor(getColor(R.color.red));

            orderBtn.setVisibility(View.GONE);
        }

        // just testing
        ArrayList<Drawable> photos = new ArrayList<>();
        photos.add(getDrawable(R.drawable.ph1));
        photos.add(getDrawable(R.drawable.ph2));
        photos.add(getDrawable(R.drawable.ph3));

        photoVPAdapter = new PhotoVPAdapter(photos, CafeActivity.this);

        viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(photoVPAdapter);

        handler = new Handler();

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!touched)
                    touched = true;
                handler.removeCallbacks(runnable);

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!touched)
            handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(handler.hasCallbacks(runnable))
                handler.removeCallbacks(runnable);
        }
    }
}