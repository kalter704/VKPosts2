package com.example.vasiliy.vkposts2.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vasiliy.vkposts2.R;

public class ShowListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        String message = getIntent().getStringExtra("message");
        ((TextView) findViewById(R.id.tvShowList)).setText(message);
    }
}
