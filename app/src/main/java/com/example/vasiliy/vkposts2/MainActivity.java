package com.example.vasiliy.vkposts2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
*
*
*
* Надо доработать постинг в комменты!!!
*
*
*
*
*
*
 */

public class MainActivity extends AppCompatActivity {

    private String accessToken;

    private String[] scopes = new String[]{VKScope.WALL};

    private int countSendedPosts;

    private boolean isDoPost;
    private boolean isStopPosting;

    private String captchaId;
    private String captchaUrl;
    private String captchaAnswer;

    private String postId;

    //private String message = "Добавь меня!!!";

    private String message = "\uD83D\uDCB0Хочешь по 50-100 заявок в день❓\uD83D\uDCB0 \n" +
            "\uD83D\uDCB0Тогда тебе к нам! Добавь всех из списка \uD83C\uDF1D \n" +
            "○〰 ○〰 ○〰 ○〰 \n" +
            "\uD83D\uDCB2АДМИНЫ\uD83D\uDCB2 \n" +
            "\uD83C\uDF1A @id327708657 (\uD83C\uDF3AАдминка\uD83C\uDF3A) \n" +
            "\uD83C\uDF1D@id295591956 (\uD83C\uDF37Зам.Админа\uD83C\uDF37) \n" +
            "\uD83D\uDC51.::VIP::.\uD83D\uDC51 \n" +
            "\uD83C\uDFAF\uD83D\uDD25@id322157495 (Лиза Драганова)\uD83C\uDF1F \n" +
            "\uD83C\uDFAF\uD83D\uDD25@id350952179 (Артур Великий)\uD83C\uDF1F \n" +
            "\uD83C\uDFAF\uD83D\uDD25@ppfer (Дарья Карпова)\uD83C\uDF1F \n" +
            "\uD83C\uDFAF\uD83D\uDD25@lolkekxaxaxa (Ксюша Цуканова)\uD83C\uDF1F \n" +
            "\uD83C\uDFAF\uD83D\uDD25@id350485363 (Иван Кобра)\uD83C\uDF1F \n" +
            "\uD83C\uDFAF\uD83D\uDD25@id348732714 (Виктория Смирнова)\uD83C\uDF1F \n" +
            "\uD83C\uDF89 УЧАСТНИКИ \uD83C\uDF89 \n" +
            "✨@id256931905 (Катя Яроцинская)✨ \n" +
            "✨@maglee (Маргарита Вартанова)✨\n" +
            "✨@id139163001 (Эдуард Мисько)✨\n" +
            "✨@id339711014 (Ксюша Смирнова)✨ \n" +
            "✨@id353032503 (Виктория Королюк)✨ \n" +
            "✨@id345261648 (Александр Сахаров)✨ \n" +
            "✨@likesuchka (Андрей Самарин)✨ \n" +
            "✨@id327470005 (Nikita Lobkov)✨ \n" +
            "✨@id339711014 (Ксюша Смирнова)✨ \n" +
            "✨@id307278155 (Екатерина Танкова)✨ \n" +
            "✨@id191473719 (Анастасия Зимогляд)✨ \n" +
            "✨@vladdidik (Влад Морозов)✨ \n" +
            "✨@irishakotya (Ира Кот)✨ \n" +
            "✨@id136689482 (Оксана Мантаева)✨ \n" +
            "✨@id351909967 (Нюша Шурочкина)✨ \n" +
            "✨@id195026644 (Лиза Иванова)✨ \n" +
            "✨@id342878456 (Diana Gromovay)✨ \n" +
            "✨@id_avram_0715 (Иван Авраменко)✨ \n" +
            "✨@id185378233 (Женя Пахар)✨ \n" +
            "✨@id314056139 (Vasily Kashirskiy)✨ \n" +
            "✨@id276831637 (Антон Раевский)✨ \n" +
            "✨*id272287187 (Мария Клименко) \n" +
            "✨*id187964827 (Соня Семенова)✨ \n" +
            "✨@id215830542 (Надя Князева)✨ \n" +
            "✨*id269374102 (Диана Доронина)✨ \n" +
            "✨*id325078401 (Матвей Алексеев)✨ \n" +
            "✨*id351481879 (Юля Попадюк)✨ \n" +
            "✨*id312564048 (Марьяна Балковская)✨ \n" +
            "✨@id236005361 (Лера Котова)✨ \n" +
            "✨@arinagertman48 (Арина Гроссман)✨ \n" +
            "✨@maryana_ro_98 (Марьяна Рожкова)✨ \n" +
            "✨*julka242 (Юля Судьина)✨ \n" +
            "✨*id347041471 (Демид Сафин)✨ \n" +
            "✨*id340590069 (Ильяс Ахмедгалиев)✨ \n" +
            "✨*id239678829 (Паша Чёрный)✨ \n" +
            "✨*id261827618 (Настя Кирдяшова)✨ \n" +
            "✨@id317457117 (Ваня Шульман)✨ \n" +
            "✨@id334681505 (Настя Братских)✨ \n" +
            "✨*id152100209 (Артём Московский)✨ \n" +
            "✨*anbreya_krut (Андрей Кубрак)✨ \n" +
            "✨*id300551742 (Марьяна Цой)✨ \n" +
            "✨*id260906258 (Михаил Печерица)✨ \n" +
            "✨*id339851762 (Александр Николаев)✨ \n" +
            "✨@id352828898 ( Милая Котейка)✨ \n" +
            "✨@id345523230 (Иван Рудской)✨\n" +
            "●▬▬▬▬▬▬▬▬ஜ ۩۞۩ ஜ▬▬▬▬▬▬▬▬● \n" +
            "Хочешь в список? Пиши им с пометкой \"Хочу в список\"⬇ \n" +
            "@id327708657(\uD83C\uDF3AАдминке \uD83C\uDF3A) или \n" +
            "@id295591956 (\uD83C\uDF37Лиле\uD83C\uDF37) \n" +
            "●▬▬▬▬▬▬▬▬ஜ ۩۞۩ ஜ▬▬▬▬▬▬▬▬●";

    private String clubs[] = {
            "34985835",
            "59721672",
            "60191872",
            "8337923",
            "46258034",
            "60191872",
            "13295252"
    };

    TextView tvMadePost;
    ImageView img;

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
        captchaUrl = "-1";
        captchaAnswer = "-1";
        postId = "-1";

        VKSdk.login(this, scopes);



        tvMadePost = (TextView) findViewById(R.id.tvMadePosts);
        tvMadePost.setText("Сделано постов = " + String.valueOf(countSendedPosts));

        img = (ImageView) findViewById(R.id.imageView2);

        backgroundSendPosts = new BackgroundSendPosts();

        ((Button) findViewById(R.id.btnStart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDoPost = true;
                isStopPosting = false;
                captchaId = "-1";
                captchaUrl = "-1";
                captchaAnswer = "-1";
                postId = "-1";
                backgroundSendPosts.execute();
                img.setImageResource(R.drawable.galochkacheck);
                accessToken = VKAccessToken.currentToken().accessToken;
            }
        });

        ((Button) findViewById(R.id.btnStop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopPosting = true;
                img.setImageResource(R.drawable.delete);
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
            //int len = clubs.length;
            int i = 0;
            int cc = 0;
            int whereDoPost = VKPosts2Constants.DO_POST_ON_WALL;
            while (!isStopPosting) {
                if (isCancelled()) {
                    return null;
                }
                if (isDoPost) {
                    String response = null;
                    switch (whereDoPost) {
                        case VKPosts2Constants.DO_POST_ON_WALL:
                            response = doPost(clubs[i]);
                            break;
                        case VKPosts2Constants.DO_POST_IN_COMMENT:
                            response = doPostInComment(clubs[i]);
                    }
                    if (response == null) {
                        continue;
                    }
                    switch (whatIsResponse(response)) {
                        case VKPosts2Constants.SUCCESFULL_RESPONSE:
                            captchaId = "-1";
                            captchaUrl = "-1";
                            captchaAnswer = "-1";
                            postId = "-1";
                            whereDoPost = VKPosts2Constants.DO_POST_ON_WALL;
                            ++cc;
                            if (cc == 3) {
                                ++i;
                                cc = 0;
                            }
                            ++countSendedPosts;
                            publishProgress();
                            break;
                        case VKPosts2Constants.RESPONSE_WITH_CAPTCHA:
                            Map<String, String> captchaMap;
                            if (captchaId.equals("-1") || captchaAnswer.equals("-1") || captchaUrl.equals("-1")) {
                                captchaMap = getCaptcha(response);
                                captchaId = captchaMap.get("captchaId");
                                captchaUrl = captchaMap.get("captchaUrl");
                            }
                            isDoPost = false;
                            Intent intent = new Intent(MainActivity.this, InputCaptchaActivity.class);
                            intent.putExtra("countMadePosts", countSendedPosts);
                            intent.putExtra("captchaUrl", captchaUrl);
                            intent.putExtra("club", clubs[i]);
                            startActivityForResult(intent, VKPosts2Constants.INPUT_CAPTCHA_ACTIVITY_RESULT_CODE);
                            break;
                        case VKPosts2Constants.RESPONSE_WITH_CLOSE_WALL:
                            whereDoPost = VKPosts2Constants.DO_POST_IN_COMMENT;
                            break;
                        case VKPosts2Constants.VK_ERROR_CODE_TOO_MACH_REQUEST_PER_SECOND:
                            break;
                        case VKPosts2Constants.ERROR_RESPONSE:
                            whereDoPost = VKPosts2Constants.DO_POST_ON_WALL;
                            captchaId = "-1";
                            captchaUrl = "-1";
                            captchaAnswer = "-1";
                            postId = "-1";
                            ++i;
                            break;
                        default:

                            break;
                    }
                    if (i == clubs.length) {
                        i = 0;
                    }
                    long timeWhenEndWait = System.currentTimeMillis() + VKPosts2Constants.VK_TIME_BETWEEN_DO_POST + (new Random().nextInt(2 * VKPosts2Constants.VK_RANDOM_TIME_RANGE) - VKPosts2Constants.VK_RANDOM_TIME_RANGE);
                    while (System.currentTimeMillis() < timeWhenEndWait) {
                    }
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
        //String requestString = null;
        //String convertedMessage = convertToUTF8(message);
        /*if (captchaId.equals("-1") || captchaAnswer.equals("-1")) {
            requestString = "https://api.vk.com/method/wall.post?" +
                    "owner_id=" + "-" + club +
                    "&friends_only=0&from_group=0&" +
                    "message=" + convertedMessage +
                    "&v=5.45&" +
                    "access_token=" + accessToken;
        } else {
            requestString = "https://api.vk.com/method/wall.post?" +
                    "owner_id=" + "-" + club +
                    "&friends_only=0&from_group=0&" +
                    "message=" + convertedMessage +
                    "&captcha_sid=" + captchaId +
                    "&captcha_key=" + captchaAnswer +
                    "&v=5.45&" +
                    "access_token=" + accessToken;
        }
        */
        String requestString = "https://api.vk.com/method/wall.post";
        String params = "owner_id=" + "-" + club +
                "&friends_only=0&from_group=0&" +
                "message=" + message +
                "&v=5.45&" +
                "access_token=" + accessToken;
        if (!captchaId.equals("-1") || !captchaAnswer.equals("-1")) {
            params += "&captcha_sid=" + captchaId +
                    "&captcha_key=" + captchaAnswer;
        }
        return request(requestString, params);
    }

    //Это можно упростить с помощью запроса execute!!!!!!
    private String doPostInComment(String club) {
        String requestString = null;
        String params = null;
        if (postId.equals("-1")) {
            requestString = "https://api.vk.com/method/wall.get";
            params = "owner_id=-" + club +
                    "&count=2&filter=all&extended=0&v=5.45&" +
                    "access_token=" + accessToken;
            String responseString = request(requestString, params);
            if (responseString == null) {
                return null;
            }
            postId = getPostId(responseString);
            if (postId == null) {
                return null;
            }
        }
        /*
        if (captchaId.equals("-1") || captchaAnswer.equals("-1")) {
            try {
                requestString = "https://api.vk.com/method/wall.addComment?" +

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ?" +
                        "owner_id=-" + club +
                        "&post_id=" + postId +
                        "&from_group=0" +
                        "&text=" + URLEncoder.encode(message, "UTF-8") +
                         +
                        "&v=5.45" +
                        "&access_token=" + accessToken;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        */
        requestString = "https://api.vk.com/method/wall.addComment";
        params = "owner_id=-" + club +
                "&post_id=" + postId +
                "&from_group=0&" +
                "text=" + message +
                "&v=5.45&" +
                "access_token=" + accessToken;
        if (captchaId.equals("-1") || captchaAnswer.equals("-1")) {
            params += "&captcha_sid=" + captchaId +
                    "&captcha_key=" + captchaAnswer;
        }
        return request(requestString, params);
    }

    /*
    private String convertToUTF8(String str) {
        String result = "";
        String temp[] = str.split(" ");
        for (int i = 0; i < temp.length; ++i) {
            try {
                result += URLEncoder.encode(temp[i], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

       */

    protected String getPostId(String jsonString) {
        JSONObject dataJsonObject = null;
        if (jsonString == null) {
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

    private String request(String urlStr, String params) {
        /*
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
        */
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

            String jsonParamsString = "owner_id=" + "-" + clubs[0] +
                    "&friends_only=0&from_group=0&" +
                    "message=" + message +
                    "&v=5.45&" +
                    "access_token=" + accessToken;

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

            Log.d("QWERTY", sb.toString());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == VKPosts2Constants.INPUT_CAPTCHA_ACTIVITY_RESULT_CODE) {
            isStopPosting = data.getBooleanExtra("isStopPosting", true);
            if (!isStopPosting) {
                captchaAnswer = data.getStringExtra("captchaAnswer");
                isDoPost = true;
            } else {
                img.setImageResource(R.drawable.delete);
            }
        }
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
        if (response == null) {
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
        if (isParse) {
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
                case VKPosts2Constants.VK_ERROR_CODE_TOO_MACH_REQUEST_PER_SECOND:
                    return VKPosts2Constants.VK_ERROR_CODE_TOO_MACH_REQUEST_PER_SECOND;
            }
        } catch (JSONException e) {
            return VKPosts2Constants.ERROR_RESPONSE;
        }
        return VKPosts2Constants.ERROR_RESPONSE;
    }

}
