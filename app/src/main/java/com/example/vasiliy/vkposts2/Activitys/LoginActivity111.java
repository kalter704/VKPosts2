package com.example.vasiliy.vkposts2.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.vasiliy.vkposts2.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class LoginActivity111 extends AppCompatActivity {

    private String[] scopes = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VKSdk.login(this, scopes);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(LoginActivity111.this, "Good", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity111.this, MenuActivity.class));
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(LoginActivity111.this, "Bad", Toast.LENGTH_SHORT).show();

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
