package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;

public class AnnouncementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        // getting Announcement we passed via intent in NewsFragment
        Announcement event = getIntent().getParcelableExtra("ANNOUNCEMENT");

        ImageView img = findViewById(R.id.img);

        TextView title = findViewById(R.id.title);
        TextView desc = findViewById(R.id.txt);

        title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        desc.setMovementMethod(new ScrollingMovementMethod());

        // filling views with content we get from passed Announcement event (if it`s not null)
        if(event != null) {
            img.setImageResource(event.getImage());

            title.setText(event.getTitle());
            desc.setText(event.getDesc());
        }

    }
}