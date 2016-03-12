package com.example.vasiliy.vkposts2.Activitys;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vasiliy.vkposts2.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private String[] scopes = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        //System.out.println(Arrays.asList(fingerprints));

        //com.example.vasiliy.vkposts2.Classes.Application applicationM = new com.example.vasiliy.vkposts2.Classes.Application();
        //applicationM.onCreate();

        VKSdk.login(this, scopes);

        /*
        if(VKSdk.wakeUpSession(this)) {
            VKSdk.login(this, scopes);
        } else {
            Toast.makeText(MainActivity.this, "FALSE", Toast.LENGTH_SHORT).show();
        }
        */


        //startActivity(new Intent(MainActivity.this, MenuActivity.class));

        /*
        if(VKAccessToken.currentToken().isExpired()) {
            VKSdk.login(this, scopes);
        } else {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        }
        */

        ((Button) findViewById(R.id.btnLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(MainActivity.this, scopes);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(MainActivity.this, "Good", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(MainActivity.this, "Bad", Toast.LENGTH_SHORT).show();

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}