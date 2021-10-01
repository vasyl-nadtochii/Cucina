package com.faint.cucina.fragments.order_conf_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.faint.cucina.R;
import com.faint.cucina.interfaces.OrderFinalMsgUI;

public class ResultFragment extends Fragment {

    public static OrderFinalMsgUI msgUI;
    private ViewGroup successLayout, failLayout, limitLayout;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_result, container, false);

        successLayout = root.findViewById(R.id.layout_success);
        failLayout = root.findViewById(R.id.layout_fail);
        limitLayout = root.findViewById(R.id.layout_limit);
        progressBar = root.findViewById(R.id.progressBar);

        msgUI = new OrderFinalMsgUI() {
            @Override
            public void showSuccessUI() {
                successLayout.setVisibility(View.VISIBLE);
                failLayout.setVisibility(View.GONE);
                limitLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void showFailUI() {
                successLayout.setVisibility(View.GONE);
                failLayout.setVisibility(View.VISIBLE);
                limitLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void showLimitLayout() {
                successLayout.setVisibility(View.GONE);
                failLayout.setVisibility(View.GONE);
                limitLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };

        return root;
    }
}