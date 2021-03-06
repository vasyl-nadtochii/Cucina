package com.faint.cucina.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.R;
import com.faint.cucina.activities.AnnouncementActivity;
import com.faint.cucina.activities.MainActivity;
import com.faint.cucina.adapters.NewsLVAdapter;
import com.faint.cucina.adapters.NewsVPAdapter;
import com.faint.cucina.classes.Announcement;
import com.faint.cucina.login_register.URLs;
import com.faint.cucina.login_register.UserDataSP;
import com.faint.cucina.custom.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFragment extends Fragment {

    public static ArrayList<Announcement> eventList;

    private NewsVPAdapter newsVpAdapter;     // ViewPager adapter
    private NewsLVAdapter newsLvAdapter;    // ListView adapter

    private ViewPager viewPager;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private ViewGroup content_layout, msg_layout, err_layout;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_news, container, false);

        // init views
        listView = root.findViewById(R.id.listView);
        viewPager = root.findViewById(R.id.viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
            }
        });

        content_layout = root.findViewById(R.id.content_layout);
        msg_layout = root.findViewById(R.id.message_layout);
        err_layout = root.findViewById(R.id.error_layout);

        progressBar = root.findViewById(R.id.progressBar);

        eventList = new ArrayList<>();
        getNews(false);

        refreshLayout = root.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> getNews(true));

        return root;
    }

    private void enableDisableSwipeRefresh(boolean enable) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(enable);
        }
    }

    public static Intent getNewsIntent(Context c, int pos, ArrayList<Announcement> list) {
        // here we`re creating intent and putting announcement we`ve selected to it
        return new Intent(c, AnnouncementActivity.class).putExtra("ANNOUNCEMENT", list.get(pos));
    }

    private String formatContent(String content) {
        String newContent = content;

        if (newContent.contains("&quot;")) {
            newContent = newContent.replaceAll("&quot;", "\"");
        }

        return newContent;
    }

    private void getNews(boolean refreshing) {
        if (isNetworkAvailable()) {
            if(!eventList.isEmpty()) {
                eventList.clear();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        MainActivity.requestFinished = false;

        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_GET_NEWS,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            int type = object.getInt("type");
                            String header = object.getString("header");
                            String content = formatContent(object.getString("content"));
                            String img_url = object.getString("img_url");
                            String end_date = object.getString("end_date");

                            Announcement announcement = new Announcement(img_url, type,
                                    header, content, end_date);

                            eventList.add(announcement);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(eventList.size() == 0) {
                        msg_layout.setVisibility(View.VISIBLE);
                        content_layout.setVisibility(View.GONE);
                    }
                    else {
                        Collections.reverse(eventList);

                        if(eventList.size() >= 3) {
                            List<Announcement> vpPart = eventList.subList(0, eventList.size() / 2);
                            List<Announcement> lvPart = eventList.subList(eventList.size() / 2, eventList.size());

                            newsLvAdapter = new NewsLVAdapter( new ArrayList<>(lvPart), getActivity() );
                            newsVpAdapter = new NewsVPAdapter( new ArrayList<>(vpPart), getActivity() );
                        }
                        else {
                            newsLvAdapter = new NewsLVAdapter( eventList, getActivity() );
                            newsVpAdapter = new NewsVPAdapter( eventList, getActivity() );
                        }

                        listView.setAdapter(newsLvAdapter);
                        viewPager.setAdapter(newsVpAdapter);

                        content_layout.setVisibility(View.VISIBLE);
                        msg_layout.setVisibility(View.GONE);
                    }

                    err_layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    listView.setOnItemClickListener((adapterView, view, pos, l) -> {
                        // transition with selected announcement
                        if(eventList.size() >= 3) {
                            startActivity( getNewsIntent( getActivity(), pos,
                                    new ArrayList<>(eventList.subList(eventList.size() / 2, eventList.size()))) );
                        }
                        else {
                            startActivity( getNewsIntent( getActivity(), pos, eventList) );
                        }
                    });

                    if(refreshing) {
                        refreshLayout.setRefreshing(false);
                    }

                    MainActivity.requestFinished = true;
                },
                error -> {
                    Toast.makeText(requireActivity(),
                            requireActivity().getString(R.string.network_err), Toast.LENGTH_SHORT).show();

                    if(eventList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        content_layout.setVisibility(View.GONE);
                        msg_layout.setVisibility(View.GONE);
                        err_layout.setVisibility(View.VISIBLE);
                    }

                    if(refreshing) {
                        refreshLayout.setRefreshing(false);
                    }

                    MainActivity.requestFinished = true;
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("city", UserDataSP.getInstance(requireContext()).getUser().getCity());

                return params;
            }
        };

        VolleySingleton.getInstance(requireActivity()).addToRequestQueue(request);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
