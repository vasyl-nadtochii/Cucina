package com.faint.cucina.login_register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucina.activities.AuthorizationActivity;
import com.faint.cucina.classes.User;
import com.faint.cucina.custom.VolleySingleton;
import com.faint.cucina.fragments.order_conf_fragments.ResultFragment;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UserDataSP {

    private static final String SHARED_PREF_NAME = "user_account";
    public static final String KEY_USERNAME = "keyusername";
    public static final String KEY_PHONE = "keyphone";
    public static final String KEY_CITY = "keycity";
    private static final String KEY_ID = "keyid";

    @SuppressLint("StaticFieldLeak")
    private static UserDataSP mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context ctx;

    private UserDataSP(Context context) {
        ctx = context;
    }
    public static synchronized UserDataSP getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserDataSP(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_CITY, user.getCity());
        editor.apply();

        FirebaseMessaging.getInstance().subscribeToTopic(user.getCity());
        FirebaseMessaging.getInstance().subscribeToTopic("all");
    }

    public void changeData(String dataToChange, String newData) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(dataToChange, newData);

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_CITY, null)
        );
    }

    public void logout(String city) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(city);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all");

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        ctx.startActivity(new Intent(ctx, AuthorizationActivity.class));
    }
}
