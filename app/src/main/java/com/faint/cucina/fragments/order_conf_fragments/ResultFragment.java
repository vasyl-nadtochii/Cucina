package com.faint.cucina.fragments.order_conf_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faint.cucina.R;
import com.faint.cucina.interfaces.OrderFinalMsgUI;

public class ResultFragment extends Fragment {

    public static OrderFinalMsgUI msgUI;
    ViewGroup successLayout, failLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_success, container, false);

        successLayout = root.findViewById(R.id.layout_success);
        failLayout = root.findViewById(R.id.layout_fail);

        msgUI = new OrderFinalMsgUI() {
            @Override
            public void showSuccessUI() {
                successLayout.setVisibility(View.VISIBLE);
                failLayout.setVisibility(View.GONE);
            }

            @Override
            public void showFailUI() {
                successLayout.setVisibility(View.GONE);
                failLayout.setVisibility(View.VISIBLE);
            }
        };

        return root;
    }
}