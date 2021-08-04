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
import com.bumptech.glide.request.RequestOptions;
import com.faint.cucina.R;
import com.faint.cucina.classes.async.Translator;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class DishDescActivity extends AppCompatActivity {

    ImageView img;
    TextView nameTV, descTV, priceTV;
    FABProgressCircle progressCircle;
    FloatingActionButton fabTranslate;

    String name, desc, imgUrl, price, currLang;

    boolean translated = false;

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

        fabTranslate = findViewById(R.id.fabTranslate);
        progressCircle = findViewById(R.id.fabProgressCircle);

        currLang = Locale.getDefault().getLanguage();
        if(currLang.equals("ru")) {
            fabTranslate.setVisibility(View.GONE);
        }

        fabTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] translatedName = { name };
                final String[] translatedDesc = { desc };

                Thread trThread = new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        try {
                            if(!translated) {
                                progressCircle.show();

                                translatedName[0] = new Translator().execute("ru", currLang, translatedName[0]).get();
                                translatedDesc[0] = new Translator().execute("ru", currLang, translatedDesc[0]).get();

                                translated = true;
                            }
                            else {
                                translatedName[0] = name;
                                translatedDesc[0] = desc;

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
                                    nameTV.setText(translatedName[0]);
                                    descTV.setText(translatedDesc[0]);
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