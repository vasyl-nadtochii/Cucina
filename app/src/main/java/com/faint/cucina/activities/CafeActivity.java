package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faint.cucina.R;
import com.faint.cucina.adapters.PhotoVPAdapter;
import com.faint.cucina.classes.Cafe;
import com.faint.cucina.login_register.UserDataSP;

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
        address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView state = findViewById(R.id.state);

        Button orderBtn = findViewById(R.id.order_btn);
        Button complaintBtn = findViewById(R.id.complaint_btn);
        Button trackBtn = findViewById(R.id.track_btn);

        complaintBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);

            assert cafe != null;
            intent.putExtra("CAFE_ID", cafe.getCafeID());
            intent.putExtra("USER_NAME", UserDataSP.getInstance(getApplicationContext()).getUser().getName());

            startActivity(intent);
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
                state.setText("◉ " + getString(R.string.takeaway));
                state.setTextColor(getColor(R.color.yellow));
                break;
            case 3:
                state.setText("◉ " + getString(R.string.closing));
                state.setTextColor(getColor(R.color.orange));
                orderBtn.setVisibility(View.GONE);
                break;
        }

        orderBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CafeActivity.this, OrderActivity.class);
            intent.putExtra("HAS_DISHES", false);
            intent.putExtra("CAFE_ID", cafe.getCafeID());
            intent.putExtra("CAFE_ADDRESS", cafe.getAddress());

            startActivity(intent);
        });

        trackBtn.setOnClickListener(view -> {
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            // we gave location permission before, so there is no need to request it again
            @SuppressLint("MissingPermission")
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            assert location != null;
            double userLatitude = location.getLatitude();
            double userLongitude = location.getLongitude();

            try {
                Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + userLatitude
                    + "," + userLongitude + "/" + cafe.getLatitude() + "," + cafe.getLongitude());

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

        photoVPAdapter = new PhotoVPAdapter(cafe.getImgUrls(), CafeActivity.this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(photoVPAdapter);

        handler = new Handler();
        viewPager.setOnTouchListener((view, motionEvent) -> {
            if(!touched)
                touched = true;
            handler.removeCallbacks(runnable);

            return false;
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