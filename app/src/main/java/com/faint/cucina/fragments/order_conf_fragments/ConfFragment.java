package com.faint.cucina.fragments.order_conf_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.faint.cucina.R;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.adapters.OrderConfLVAdapter;
import com.faint.cucina.classes.Cafe;

public class ConfFragment extends Fragment {

    TextView addressTV, clarificationTV;
    ListView orderLV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_conf, container, false);

        addressTV = root.findViewById(R.id.orderAddress);
        clarificationTV = root.findViewById(R.id.orderClar);
        orderLV = root.findViewById(R.id.orderLV);

        OrderConfLVAdapter adapter = new OrderConfLVAdapter(requireActivity(), OrderActivity.order.getOrderList());

        String addressStr = "Адрес заказа: " + OrderActivity.address;
        addressTV.setText(addressStr);

        String clarStr = "Уточнения к заказу: " + OrderActivity.order.getClarifications();
        clarificationTV.setText(clarStr);

        orderLV.setAdapter(adapter);

        return root;
    }
}