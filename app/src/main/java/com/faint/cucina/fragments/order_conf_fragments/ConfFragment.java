package com.faint.cucina.fragments.order_conf_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.faint.cucina.R;
import com.faint.cucina.activities.OrderActivity;
import com.faint.cucina.adapters.OrderConfLVAdapter;

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

        String addressStr = requireActivity().getString(R.string.address) + " " + OrderActivity.address;
        addressTV.setText(addressStr);

        String clarStr = requireActivity().getString(R.string.clarification) + " " + OrderActivity.order.getClarifications();
        clarificationTV.setText(clarStr);

        orderLV.setAdapter(adapter);

        return root;
    }
}