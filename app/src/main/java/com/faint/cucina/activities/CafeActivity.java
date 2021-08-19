package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.faint.cucina.login_register.UserDataSP;

import java.util.ArrayList;

public class CafeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PhotoVPAdapter photoVPAdapter;

    private Handler handler;

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

        final Cafe cafe = getIntent().getParcelableExtra("CAFE");

        TextView address = findViewById(R.id.address);
        TextView state = findViewById(R.id.state);

        Button orderBtn = findViewById(R.id.order_btn);
        Button complaintBtn = findViewById(R.id.complaint_btn);

        complaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);

                assert cafe != null;
                intent.putExtra("CAFE_ID", cafe.getCafeID());
                intent.putExtra("USER_NAME", UserDataSP.getInstance(getApplicationContext()).getUser().getName());

                startActivity(intent);
            }
        });

        assert cafe != null;
        address.setSelected(true);
        address.setText(getResources().getString(R.string.cafe_on) + " " + cafe.getAddress());

        switch (cafe.getState()) {
            case 1:
                state.setText("◉ " + getString(R.string.opened));
                state.setTextColor(getColor(R.color.green));
                break;
            case 2:
                state.setText("◉ Работаем на вынос");
                state.setTextColor(getColor(R.color.yellow));
                break;
            case 3:
                state.setText("◉ Скоро закрываемся");
                state.setTextColor(getColor(R.color.orange));
                orderBtn.setVisibility(View.GONE);
                break;
        }

        photoVPAdapter = new PhotoVPAdapter(cafe.getImgUrls(), CafeActivity.this);
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