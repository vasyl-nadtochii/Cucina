package com.faint.cucina.fragments.reg_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.faint.cucina.R;
import com.faint.cucina.activities.RegistrationActivity;

public class InfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        NestedScrollView scrollView = root.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    RegistrationActivity.fragmentInterface.hideBtn();
                } else {
                    RegistrationActivity.fragmentInterface.showBtn();
                }
            }
        });

        return root;
    }
}