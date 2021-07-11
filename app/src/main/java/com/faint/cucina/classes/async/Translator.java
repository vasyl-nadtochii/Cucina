package com.faint.cucina.classes.async;

import android.os.AsyncTask;

import com.faint.cucina.activities.StartActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translator extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            final String urlStr;

            urlStr = "https://script.google.com/macros/s/AKfycbxbALWsM71ovNtLCEoayUpQzc_d9bPXgsPpSkvujhxHz0nPlZNxT527FRlLR7hr3YSi/exec" +
                    "?q=" + URLEncoder.encode(params[2], "UTF-8") +
                    "&target=" + params[1] +
                    "&source=" + params[0];

            URL url = new URL(urlStr);

            StringBuilder response = new StringBuilder();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Error occurred!";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
