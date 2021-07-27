package com.faint.cucina.fragments.order_conf_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.faint.cucina.R;
import com.faint.cucina.activities.OrderActivity;

public class DescFragment extends Fragment {

    EditText editText;
    TextView counterView;

    public static String descText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_desc, container, false);

        editText = root.findViewById(R.id.edit_text);
        counterView = root.findViewById(R.id.counter);

        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                descText = s.toString();

                String length = s.length() + "/600";
                counterView.setText(length);
            }

            public void afterTextChanged(Editable s) { }
        };

        editText.addTextChangedListener(mTextEditorWatcher);

        return root;
    }
}