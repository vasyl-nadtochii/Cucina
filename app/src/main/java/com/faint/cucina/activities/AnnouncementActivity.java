package com.faint.cucina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.faint.cucina.R;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.classes.async.Translator;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AnnouncementActivity extends AppCompatActivity {

    FloatingActionButton fabTranslate;
    FABProgressCircle progressCircle;

    boolean translated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        // getting Announcement we passed via intent in NewsFragment
        final Announcement event = getIntent().getParcelableExtra("ANNOUNCEMENT");

        ImageView img = findViewById(R.id.img);

        final TextView title = findViewById(R.id.title);
        final TextView desc = findViewById(R.id.txt);

        title.setPaintFlags(title.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        desc.setMovementMethod(new ScrollingMovementMethod());

        // filling views with content we get from passed Announcement event (if it`s not null)
        if(event != null) {
            Glide.with(this)
                    .load(event.getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fitCenter()
                    .into(img);

            title.setText(event.getTitle());
            desc.setText(event.getDesc());
        }

        fabTranslate = findViewById(R.id.fabTranslate);
        progressCircle = findViewById(R.id.fabProgressCircle);

        if(Locale.getDefault().getLanguage().equals("ru")) {
            fabTranslate.setVisibility(View.GONE);
        }

        fabTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert event != null;
                final String[] translatedTitle = { event.getTitle() };
                final String[] translatedDesc = { event.getDesc() };

                Thread trThread = new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        String currLang = Locale.getDefault().getLanguage();

                        try {
                            if(!translated) {
                                progressCircle.show();

                                translatedTitle[0] = new Translator().execute("ru", currLang, translatedTitle[0]).get();
                                translatedDesc[0] = new Translator().execute("ru", currLang, translatedDesc[0]).get();

                                if (translatedTitle[0].contains("&quot;")) {
                                    translatedTitle[0] = translatedTitle[0].replaceAll("&quot;", "\"");
                                }

                                if (translatedDesc[0].contains("&quot;")) {
                                    translatedDesc[0] = translatedDesc[0].replaceAll("&quot;", "\"");
                                }

                                translated = true;
                            }
                            else {
                                translatedTitle[0] = event.getTitle();
                                translatedDesc[0] = event.getDesc();

                                translated = false;
                            }
                        }
                        catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        finally {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    title.setText(translatedTitle[0]);
                                    desc.setText(translatedDesc[0]);
                                }
                            });

                            if(translated)
                                progressCircle.beginFinalAnimation();
                        }
                    }
                };
                trThread.start();
            }
        });
    }
}