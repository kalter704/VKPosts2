package com.example.vasiliy.vkposts2.Activitys;

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.vasiliy.vkposts2.R;
        import com.example.vasiliy.vkposts2.Classes.VKPosts2Constants;
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

public class PRActivity extends AppCompatActivity {

    private String accessToken;

    //private String[] scopes = new String[]{VKScope.WALL};

    private int countSendedPosts;

    private boolean isDoPost;
    private boolean isStopPosting;

    private String captchaId;
    private String captchaUrl;
    private String captchaAnswer;

    private String postId;

    //private String message = "Добавь меня!!!";

    private String message = "▬▬▬▬▬▬▬ஜ۩۞۩ஜ▬▬▬▬▬▬▬ \n" +
            "░░░░░░░░ДОБАВЛЯЙСЯ░░░░░░░░ \n" +
            "▬▬▬▬▬▬▬ஜ۩۞۩ஜ▬▬▬▬▬▬▬ \n" +
            "\uD83C\uDF89✨Хочешь много заявок в день?\uD83C\uDF89✨ \n" +
            "\uD83D\uDE1B✨Тогда тебе к нам! Добавь всех из списка⤵✨\uD83D\uDE0B \n" +
            "\uD83C\uDF80..:Админы:..\uD83C\uDF80 \n" +
            "\uD83C\uDF1A@id327708657 (Главарь банды) \n" +
            "\uD83C\uDF3A@id295591956 (Помощник) \n" +
            "\uD83D\uDC51..:V.I.P:..\uD83D\uDC51 \n" +
            "⚡\uD83D\uDE3B@id350952179 (Иван Кобра) \n" +
            "⚡\uD83D\uDE3A@id322157495 (Лиза Драганова) \n" +
            "⚡\uD83D\uDE3C@id348732714 (Виктория Смирнова) \n" +
            "⚡\uD83D\uDE40@ppfer (Василиса Карпова) \n" +
            "⚡\uD83D\uDE3D*id210722337 (Лёня Шастин) \n" +
            "\uD83C\uDD92 ..:Участники:..\uD83C\uDD92 \n" +
            "▶\uD83C\uDF86*id.lisa_905236 (Лиза Миронова) \n" +
            "▶\uD83C\uDF86@id342878456 (Diana Gromovay) \n" +
            "▶\uD83C\uDF86*idgromovalex (Александр Громов) \n" +
            "▶\uD83C\uDF86@id350485363 (Артур Великий) \n" +
            "▶\uD83C\uDF86*staffbetsforever (Дмитрий Артюшенко) \n" +
            "▶\uD83C\uDF86@id339711014 (Ксюша Смирнова) \n" +
            "▶\uD83C\uDF86@maglee (Маргарита Вартанова) \n" +
            "▶\uD83C\uDF86@id215830542 (Надя Князева) \n" +
            "▶\uD83C\uDF86@vladdidik (Влад Морозов) \n" +
            "▶\uD83C\uDF86@id314056139 (Vasily Kashirskiy) \n" +
            "▶\uD83C\uDF86*id152100209 (Артём Московский) \n" +
            "▶\uD83C\uDF86*id274157074 (Александра Цветкова) \n" +
            "▶\uD83C\uDF86*likulik95 (Анжелика Волкова) \n" +
            "▶\uD83C\uDF86@id191473719 (Анастасия Зимогляд) \n" +
            "▶\uD83C\uDF86*id206091203 (Снежана Петровская) \n" +
            "▶\uD83C\uDF86*andy_yt (Андрей Смирнов) \n" +
            "▶\uD83C\uDF86*id351909967 (Леван Горозия) \n" +
            "▶\uD83C\uDF86*id353518304 (Анастасия Малышева) \n" +
            "▶\uD83C\uDF86*marshal3000 (Денис Люксембург) \n" +
            "▶\uD83C\uDF86*id323549885 (Костя Якимчук) \n" +
            "▶\uD83C\uDF86@id262425882 (Алёна Малухина) \n" +
            "▶\uD83C\uDF86@id353541071 (Lànà Màlio) \n" +
            "▶\uD83C\uDF86@ivanxxxivan (Ivan Ivanov) \n" +
            "▶\uD83C\uDF86*galhenok99 (Галина Вязович) \n" +
            "▶\uD83C\uDF86*buratosik (Максим Бастраков) \n" +
            "▶\uD83C\uDF86*reishmarina (Марина Хетчикова) \n" +
            "▶\uD83C\uDF86*id190643384 (Владислав Донецкий) \n" +
            "▶\uD83C\uDF86*roleplayer339 (Август Мирный) \n" +
            "▶\uD83C\uDF86*idooooooooooooo0 (Денис Армейцев) \n" +
            "▶\uD83C\uDF86*nasim22 (Nasimdzhon Akhmatdzhanov) \n" +
            "▶\uD83C\uDF86*id313006690 (Юлечка Горюнова) \n" +
            "▶\uD83C\uDF86*id307606780 (Артём Михайлов) \n" +
            "▶\uD83C\uDF86*id237707511 (Александра Майер) \n" +
            "▶\uD83C\uDF86*id290367941 (Леонид Смирнов) \n" +
            "▶\uD83C\uDF86*id236561001 (Кристина Жуванова) \n" +
            "▶\uD83C\uDF86*id350936292 (Антон Петречук) \n" +
            "▶\uD83C\uDF86*vladmatushin (Влад Матюшин) \n" +
            "▶\uD83C\uDF86*id349923770 (Елизавета Новикова) \n" +
            "▶\uD83C\uDF86*idysalimova2016 (Карина Сычёва) \n" +
            "▶\uD83C\uDF86*id353556445 (Саня Некрасов) \n" +
            "▶\uD83C\uDF86*id353580500 (Артём Владимирович) \n" +
            "▶\uD83C\uDF86*inolek (Катерина Котова) \n" +
            "▶\uD83C\uDF86*id246901970 (Александр Васильев) \n" +
            "▶\uD83C\uDF86*id353591749 (Князь Князь) \n" +
            "▶\uD83C\uDF86*id318654472 (Рамзан Лолуев) \n" +
            "▶\uD83C\uDF86*id353020432 (Варя Сотникова) \n" +
            "▶\uD83C\uDF86*ivan16051995 (Иван Булатов)\n" +
            "✨Хочешь попасть в список⁉\uD83D\uDC65 Готов(а) его активно пиарить❓Тогда тебе к нам\uD83D\uDE0F \n" +
            "➡Пиши @id327708657 (Главарю) \"Хочу в список\"";

    private String clubs[] = {
            "34985835",
            "52255475",
            "24261502",
            "53294903",
            "60191872",
            "33764742",
            "59721672",
            "46258034",
            "13295252"
    };

    TextView tvMadePost;
    ImageView img;

    BackgroundSendPosts backgroundSendPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pr);

        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println(Arrays.asList(fingerprints));

        countSendedPosts = 0;
        isDoPost = true;
        isStopPosting = false;
        captchaId = "-1";
        captchaUrl = "-1";
        captchaAnswer = "-1";
        postId = "-1";

        //VKSdk.login(this, scopes);

        tvMadePost = (TextView) findViewById(R.id.tvMadePosts);
        tvMadePost.setText("Сделано постов = " + String.valueOf(countSendedPosts));

        img = (ImageView) findViewById(R.id.imageView2);

        ((Button) findViewById(R.id.btnStart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSending();
                startSending();

            }
        });

        ((Button) findViewById(R.id.btnStop)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSending();
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

    private void startSending() {
        isDoPost = true;
        isStopPosting = false;
        captchaId = "-1";
        captchaUrl = "-1";
        captchaAnswer = "-1";
        postId = "-1";
        backgroundSendPosts = new BackgroundSendPosts();
        backgroundSendPosts.execute();
        img.setImageResource(R.drawable.galochkacheck);
        accessToken = VKAccessToken.currentToken().accessToken;
    }

    private void stopSending() {
        isStopPosting = true;
        img.setImageResource(R.drawable.delete);
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
                            Intent intent = new Intent(PRActivity.this, InputCaptchaActivity.class);
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
            Log.d("QWERTY", "BackgroundSendPosts finish");
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
        /*
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
        */
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
