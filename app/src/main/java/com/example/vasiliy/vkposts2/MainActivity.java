package com.example.vasiliy.vkposts2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String accessToken;

    private String[] scopes = new String[] {VKScope.WALL};

    private int countSendedPosts;

    private boolean isDoPost;
    private boolean isStopPosting;

    private String captchaId;
    private String captchaAnswer;

    private String message = "Добавь меня!!!";

    private String clubs[] = {
            "1"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println(Arrays.asList(fingerprints));

        countSendedPosts = 0;
        isDoPost = true;
        isStopPosting = false;
        captchaId = "-1";
        captchaAnswer = "-1";

        VKSdk.login(this, scopes);

        accessToken = VKAccessToken.currentToken().accessToken;

        ((Button) findViewById(R.id.btnStart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDoPost = true;
                isStopPosting = false;

            }
        });

        ((Button) findViewById(R.id.btnStop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopPosting = true;
            }
        });
    }

    class BackgroundSendPosts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(Void... params) {
            int len = clubs.length;
            int i = 0;
            while (!isStopPosting) {
                if(isDoPost) {
                    String response = doPost(clubs[i]);
                    switch(whatIsResponse(response)) {
                        case VKPosts2Constants.SUCCESFULL_RESPONSE:

                            break;
                        case VKPosts2Constants.RESPONSE_WITH_CAPTCHA:

                            break;
                        case VKPosts2Constants.RESPONSE_WITH_CLOSE_WALL:

                            break;
                        case VKPosts2Constants.ERROR_RESPONSE:

                            break;
                        default:

                            break;
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }

    private String doPost(String club) {
        String requestString = null;
        if (captchaId.equals("-1") && captchaAnswer.equals("-1")) {
            try {
                requestString = "https://api.vk.com/method/wall.post?" +
                        "owner_id=" + "-" + club +
                        "&friends_only=0&from_group=0&" +
                        "message=" + URLEncoder.encode(message, "UTF-8") +
                        "&v=5.45&" +
                        "access_token=" + accessToken;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                requestString = "https://api.vk.com/method/wall.post?" +
                        "owner_id=" + "-" + club +
                        "&friends_only=0&from_group=0&" +
                        "message=" + URLEncoder.encode(message, "UTF-8") +
                        "&captcha_sid=" + captchaId +
                        "&captcha_key=" + captchaAnswer +
                        "&v=5.45&" +
                        "access_token=" + accessToken;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        BufferedReader reader = null;
        try {
            URL url = new URL(requestString);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoInput(true);
            c.setReadTimeout(10000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
            c.disconnect();
            return (buf.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(MainActivity.this, "Good", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(MainActivity.this, "Bad", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public int whatIsResponse(String response) {
        boolean isParse = false;
        JSONObject dataJsonObject = null;
        try {
            dataJsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject resp = dataJsonObject.getJSONObject("response");
            isParse = true;
        } catch (JSONException e) {
            isParse = false;
        }
        if(isParse) {
            return VKPosts2Constants.SUCCESFULL_RESPONSE;
        }
        try {
            JSONObject resp = dataJsonObject.getJSONObject("error");
            int errorCode = resp.getInt("error_code");
            switch (errorCode) {
                case VKPosts2Constants.VK_ERROR_CODE_CAPTCHA:
                    return VKPosts2Constants.RESPONSE_WITH_CAPTCHA;
                case VKPosts2Constants.VK_ERROR_CODE_CLOSE_WALL:
                    return VKPosts2Constants.RESPONSE_WITH_CLOSE_WALL;
            }
        } catch (JSONException e) {
            return VKPosts2Constants.ERROR_RESPONSE;
        }
        return VKPosts2Constants.ERROR_RESPONSE;
    }

}
