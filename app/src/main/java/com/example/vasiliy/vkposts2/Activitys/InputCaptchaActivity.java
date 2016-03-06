package com.example.vasiliy.vkposts2.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vasiliy.vkposts2.Classes.ImageManager;
import com.example.vasiliy.vkposts2.R;

public class InputCaptchaActivity extends AppCompatActivity {

    private boolean isStopPosting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_captcha);

        isStopPosting = false;

        final Intent intent = getIntent();
        int countMadePosts = intent.getIntExtra("countMadePosts", 0);
        String captchaUrl = intent.getStringExtra("captchaUrl");
        String club = intent.getStringExtra("club");

        ((TextView) findViewById(R.id.tvClub)).setText(club);

        ((TextView) findViewById(R.id.textView)).setText("Сделано постов = " + String.valueOf(countMadePosts));

        ((Button) findViewById(R.id.btnStop2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStopPosting = true;
                closeActivity();
            }
        });

        ((Button) findViewById(R.id.btnOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });

        ImageManager im = new ImageManager();
        im.fetchImage(captchaUrl, ((ImageView) findViewById(R.id.imageView)));

    }

    private void closeActivity() {
        Intent intentForReturn = new Intent();
        intentForReturn.putExtra("captchaAnswer", ((EditText) findViewById(R.id.editText)).getText().toString());
        intentForReturn.putExtra("isStopPosting", isStopPosting);
        setResult(RESULT_OK, intentForReturn);
        finish();
    }
}
