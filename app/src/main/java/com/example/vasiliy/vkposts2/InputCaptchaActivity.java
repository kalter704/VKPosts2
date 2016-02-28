package com.example.vasiliy.vkposts2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InputCaptchaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_captcha);

        Intent intent = getIntent();
        int countMadePosts = intent.getIntExtra("countMadePosts", 0);
        String captchaUrl = intent.getStringExtra("captchaUrl");
    }
}
