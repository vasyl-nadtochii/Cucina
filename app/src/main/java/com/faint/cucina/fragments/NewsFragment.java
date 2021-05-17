package com.faint.cucina.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.faint.cucina.R;
import com.faint.cucina.activities.AnnouncementActivity;
import com.faint.cucina.adapters.NewsLVAdapter;
import com.faint.cucina.adapters.NewsVPAdapter;

import static com.faint.cucina.activities.MainActivity.eventList;

public class NewsFragment extends Fragment {

    View root;

    NewsVPAdapter newsVpAdapter;    //ViewPager adapter
    NewsLVAdapter newsLvAdapter;    // ListView adapter

    ViewPager viewPager;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_news, container, false);

        // init adapters with ArrayLists we`ve transferred to MainActivity before
        newsLvAdapter = new NewsLVAdapter( eventList, getActivity() );
        newsVpAdapter = new NewsVPAdapter( eventList, getActivity() );

        // init views
        listView = root.findViewById(R.id.listView);
        viewPager = root.findViewById(R.id.viewPager);

        // setting adapters
        listView.setAdapter(newsLvAdapter);
        viewPager.setAdapter(newsVpAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // transition with selected announcement
                startActivity( getNewsIntent( getActivity(), pos) );
            }
        });

        return root;
    }

    public static Intent getNewsIntent(Context c, int pos) {
        // here we`re creating intent and putting announcement we`ve selected to it
        return new Intent(c, AnnouncementActivity.class).putExtra("ANNOUNCEMENT", eventList.get(pos));
    }
}
