package com.example.vasiliy.vkposts2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String accessToken;

    private String[] scopes = new String[] {VKScope.WALL};

    private int countSendedPosts;

    private boolean isDoPost;
    private boolean isStopPosting;

    private String captchaId;
    private String captchaAnswer;

    private String postId;

    private String message = "Добавь меня!!!";

    private String clubs[] = {
            "1"
    };

    TextView tvMadePost;

    BackgroundSendPosts backgroundSendPosts;

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
        postId = "-1";

        VKSdk.login(this, scopes);

        accessToken = VKAccessToken.currentToken().accessToken;

        tvMadePost = (TextView) findViewById(R.id.tvMadePosts);
        tvMadePost.setText("Сделано постов = " + String.valueOf(countSendedPosts));

        backgroundSendPosts = new BackgroundSendPosts();

        ((Button) findViewById(R.id.btnStart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDoPost = true;
                isStopPosting = false;
                captchaId = "-1";
                captchaAnswer = "-1";
                postId = "-1";
                backgroundSendPosts.execute();
            }
        });

        ((Button) findViewById(R.id.btnStop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopPosting = true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundSendPosts != null) {
            backgroundSendPosts.cancel(false);
        }
    }

    class BackgroundSendPosts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            tvMadePost.setText("Сделано постов = " + String.valueOf(countSendedPosts));
        }

        @Override
        protected Void doInBackground(Void... params) {
            int len = clubs.length;
            int i = 0;
            int whereDoPost = VKPosts2Constants.DO_POST_ON_WALL;
            while (!isStopPosting) {
                if(isCancelled()) {
                    return null;
                }
                if(isDoPost) {
                    String response = null;
                    switch (whereDoPost) {
                        case VKPosts2Constants.DO_POST_ON_WALL:
                            response = doPost(clubs[i]);
                            break;
                        case VKPosts2Constants.DO_POST_IN_COMMENT:
                            response = doPostInComment(clubs[i]);
                    }
                    if(response == null) {
                        continue;
                    }
                    switch(whatIsResponse(response)) {
                        case VKPosts2Constants.SUCCESFULL_RESPONSE:
                            captchaId = "-1";
                            captchaAnswer = "-1";
                            postId = "-1";
                            whereDoPost = VKPosts2Constants.DO_POST_ON_WALL;
                            ++i;
                            ++countSendedPosts;
                            publishProgress();
                            break;
                        case VKPosts2Constants.RESPONSE_WITH_CAPTCHA:
                            Map<String, String> captchaMap = getCaptcha(response);
                            captchaId = captchaMap.get("captchaId");
                            Intent intent = new Intent(MainActivity.this, InputCaptchaActivity.class);
                            intent.putExtra("countMadePosts", countSendedPosts);
                            intent.putExtra("captchaUrl", captchaMap.get("captchaUrl"));
                            startActivityForResult(intent, VKPosts2Constants.INPUT_CAPTCHA_ACTIVITY_RESULT_CODE);
                            isDoPost = false;
                            break;
                        case VKPosts2Constants.RESPONSE_WITH_CLOSE_WALL:
                            whereDoPost = VKPosts2Constants.DO_POST_IN_COMMENT;
                            break;
                        case VKPosts2Constants.ERROR_RESPONSE:
                            whereDoPost = VKPosts2Constants.DO_POST_ON_WALL;
                            captchaId = "-1";
                            captchaAnswer = "-1";
                            postId = "-1";
                            ++i;
                            break;
                        default:

                            break;
                    }
                    if(i == len) {
                        i = 0;
                    }
                    long timeWhenEndWait = System.currentTimeMillis() + VKPosts2Constants.VK_TIME_BETWEEN_DO_POST;
                    while(System.currentTimeMillis() < timeWhenEndWait) {}
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            tvMadePost.setText("Сделано постов = " + String.valueOf(countSendedPosts));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            tvMadePost.setText("Сделано постов = " + String.valueOf(countSendedPosts));
        }
    }

    private String doPost(String club) {
        String requestString = null;
        if (captchaId.equals("-1") || captchaAnswer.equals("-1")) {
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
        return request(requestString);
    }

    private String doPostInComment(String club) {
        String requestString = null;
        if(postId.equals("-1")) {
            requestString = "https://api.vk.com/method/wall.get?" +
                    "owner_id=-" + club +
                    "&count=2&filter=all&extended=0&v=5.45&" +
                    "access_token=" + accessToken;
            String responseString = request(requestString);
            if (responseString == null) {
                return null;
            }
            postId = getPostId(responseString);
            if (postId == null) {
                return null;
            }
        }
        if (captchaId.equals("-1") || captchaAnswer.equals("-1")) {
            try {
                requestString = "https://api.vk.com/method/wall.addComment?" +
                        "owner_id=-" + club +
                        "&post_id=" + postId +
                        "&from_group=0&" +
                        "text=" + URLEncoder.encode(message, "UTF-8") +
                        "&v=5.45&" +
                        "access_token=" + accessToken;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                requestString = "https://api.vk.com/method/wall.addComment?" +
                        "owner_id=-" + club +
                        "&post_id=" + postId +
                        "&from_group=0" +
                        "&text=" + URLEncoder.encode(message, "UTF-8") +
                        "&captcha_sid=" + captchaId +
                        "&captcha_key=" + captchaAnswer +
                        "&v=5.45" +
                        "&access_token=" + accessToken;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return request(requestString);
    }

    protected String getPostId(String jsonString) {
        JSONObject dataJsonObject = null;
        if(jsonString == null) {
            return null;
        }
        try {
            dataJsonObject = new JSONObject(jsonString);
            JSONObject response = dataJsonObject.getJSONObject("response");
            JSONArray posts = response.getJSONArray("items");
            return (posts.getJSONObject(0)).getString("id");
        } catch (JSONException e) {
            return null;
        }
    }

    protected Map<String, String> getCaptcha(String jsonString) {
        JSONObject dataJsonObject = null;
        Map<String, String> capMap = new HashMap<String, String>();
        try {
            dataJsonObject = new JSONObject(jsonString);
            JSONObject error = dataJsonObject.getJSONObject("error");
            capMap.put("captchaId", error.getString("captcha_sid"));
            capMap.put("captchaUrl", error.getString("captcha_img").replace("\\", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return capMap;
    }

    private String request(String urlStr) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
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
            Log.d("JSON_RESPONSE", buf.toString());
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
        if(response == null) {
            return VKPosts2Constants.ERROR_RESPONSE;
        }
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
