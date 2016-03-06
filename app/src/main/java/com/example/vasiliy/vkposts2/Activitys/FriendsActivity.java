package com.example.vasiliy.vkposts2.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.vasiliy.vkposts2.Classes.VKPosts2Constants;
import com.example.vasiliy.vkposts2.R;
import com.vk.sdk.VKAccessToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class FriendsActivity extends AppCompatActivity {

    TextView tvNumFrieds;

    String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        accessToken = VKAccessToken.currentToken().accessToken;

        tvNumFrieds = (TextView) findViewById(R.id.tvMessFrieds);

    }

    class BackgroundGetNumberOfApplicationFrieds extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            return null;
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }

    }

    private int getNumberOfApplicationFriends() {
        int numAppFriends = 0;
        String executeCode = "";
        String requestString = "https://api.vk.com/method/friends.getRequests";
        String params = "code=" + executeCode +
                "&v=5.45" +
                "&access_token=" + accessToken;
    }


    private String request(String urlStr, String params) {
        URL url = null;
        try {
            url = new URL(urlStr);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");

            OutputStream os = urlConnection.getOutputStream();
            /*
            String jsonParamsString = "owner_id=" + "-" + clubs[0] +
                    "&friends_only=0&from_group=0&" +
                    "message=" + message +
                    "&v=5.45&" +
                    "access_token=" + accessToken;
            */
            //Log.d("QWERTY", "Before: " + jsonParamsString);

            BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            write.write(params);

            write.flush();
            write.close();

            //outputStream.flush();
            //outputStream.close();

// get response
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();

            //InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();

            Log.d("JSON_RESPONSE", sb.toString());

            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
