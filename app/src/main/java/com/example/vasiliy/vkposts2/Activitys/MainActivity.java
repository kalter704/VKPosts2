package com.example.vasiliy.vkposts2.Activitys;

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

        VKSdk.login(this, scopes);

        ((Button) findViewById(R.id.btnGoToPR)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PRActivity.class));
            }
        });

        ((Button) findViewById(R.id.btnGoToFriends)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FriendsActivity.class));
            }
        });


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

}