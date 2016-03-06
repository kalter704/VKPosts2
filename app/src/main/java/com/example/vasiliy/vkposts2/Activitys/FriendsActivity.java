package com.example.vasiliy.vkposts2.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vasiliy.vkposts2.Classes.VKPosts2Constants;
import com.example.vasiliy.vkposts2.R;
import com.vk.sdk.VKAccessToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

public class FriendsActivity extends AppCompatActivity {

    TextView tvNumFrieds;

    String accessToken;

    BackgroundGetNumberOfApplicationFrieds backgroundGetNumberOfApplicationFrieds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        accessToken = VKAccessToken.currentToken().accessToken;

        tvNumFrieds = (TextView) findViewById(R.id.tvMessFrieds);

        backgroundGetNumberOfApplicationFrieds = new BackgroundGetNumberOfApplicationFrieds();
        backgroundGetNumberOfApplicationFrieds.execute();

        ((Button) findViewById(R.id.btnUpdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundGetNumberOfApplicationFrieds = new BackgroundGetNumberOfApplicationFrieds();
                backgroundGetNumberOfApplicationFrieds.execute();
            }
        });

        ((Button) findViewById(R.id.btnAddAllFrieds)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new BackgroundAddAllFriends()).execute();
            }
        });

    }

    class BackgroundAddAllFriends extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                String response = addFriends();
                if(howManyLeft(response) <= 0) {
                    return null;
                }
                long timeWhenEndWait = System.currentTimeMillis() + VKPosts2Constants.VK_TIME_BETWEEN_ADD_FRIEND;
                while (System.currentTimeMillis() < timeWhenEndWait) {
                }
            }
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            backgroundGetNumberOfApplicationFrieds = new BackgroundGetNumberOfApplicationFrieds();
            backgroundGetNumberOfApplicationFrieds.execute();
        }

    }

    private String addFriends() {
        String code = "var q = 0;var c = true;var friends = API.friends.getRequests({\"offset\": 0, \"count\": 25, \"extended\": 0, \"need_mutual\": 0, \"out\": 0, \"sort\": 0, \"need_viewed\": 0, \"suggested\": 0});q = q + 1;if(friends.count > 0) {c = true;} else {c = false;}while(c && (q < 20)) {var i = 0;while((i < friends.items.length) && (q < 20)) {API.friends.add({\"user_id\": friends.items[i]});q = q + 1;i = i + 1;}friends = API.friends.getRequests({\"offset\": 0, \"count\": 25, \"extended\": 0, \"need_mutual\": 0, \"out\": 0, \"sort\": 0, \"need_viewed\": 0, \"suggested\": 0});if(friends.count > 0) {c = true;} else {c = false;}}return {\"count\": friends.count};";
        String requestString = "https://api.vk.com/method/execute";
        String params = null;
        try {
            params = "code=" + URLEncoder.encode(code, "UTF-8") +
                    "&v=5.45" +
                    "&access_token=" + accessToken;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return request(requestString, params);
    }

    private int howManyLeft(String responseString) {
        JSONObject dataJsonObject = null;
        try {
            dataJsonObject = new JSONObject(responseString);
            JSONObject response = dataJsonObject.getJSONObject("response");
            return (response.getInt("count"));
        } catch (JSONException e) {
            return -1;
        }
    }

    class BackgroundGetNumberOfApplicationFrieds extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            return getNumberOfApplicationFriends();
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            tvNumFrieds.setText("У вас " + result + " заявок в друзья");
        }

    }

    private int getNumberOfApplicationFriends() {
        String requestString = "https://api.vk.com/method/friends.getRequests";
        String params = "offset=0" +
                "&count=1" +
                "&extended=0" +
                "&need_mutual=0" +
                "&out=0" +
                "&sort=0" +
                "&need_viewed=0" +
                "&suggested=0" +
                "&v=5.45" +
                "&access_token=" + accessToken;
        String response = request(requestString, params);
        return getNumberOfApplicationFriendsFromResponse(response);
    }

    private int getNumberOfApplicationFriendsFromResponse(String responseString) {
        JSONObject dataJsonObject = null;
        try {
            dataJsonObject = new JSONObject(responseString);
            JSONObject response = dataJsonObject.getJSONObject("response");
            return (response.getInt("count"));
        } catch (JSONException e) {
            return -1;
        }
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
