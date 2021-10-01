package com.faint.cucina.fragments.reg_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faint.cucina.R;
import com.plattysoft.leonids.ParticleSystem;


public class GreetingFragment extends Fragment {

    public TextView txt;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_greeting,
                container, false);

        txt = root.findViewById(R.id.txt);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            new ParticleSystem(requireActivity(),
                    25, R.drawable.particle_red, 3000)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(txt, 25);

            new ParticleSystem(requireActivity(),
                    25, R.drawable.particle_green, 1500)
                    .setSpeedRange(0.2f, 0.5f)
                    .oneShot(txt, 25);

            new ParticleSystem(requireActivity(),
                    25, R.drawable.particle_blue, 1500)
                    .setSpeedRange(0.2f, 0.4f)
                    .oneShot(txt, 25);
        }, 150);

        return root;
    }
}