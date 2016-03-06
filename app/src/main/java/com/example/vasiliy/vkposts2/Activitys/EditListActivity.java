package com.example.vasiliy.vkposts2.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vasiliy.vkposts2.R;

public class EditListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        ((Button) findViewById(R.id.btnSaveList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForReturn = new Intent();
                intentForReturn.putExtra("message", ((EditText) findViewById(R.id.etList)).getText().toString());
                setResult(RESULT_OK, intentForReturn);
                finish();
            }
        });

        ((Button) findViewById(R.id.btnCancelEditList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
