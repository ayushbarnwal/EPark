package com.example.epark;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    final String USER_MODE = "1";
    final String ADMIN_MODE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences("LogIn", MODE_PRIVATE);
                String log_in_mode = sharedPreferences.getString("LogInMode", "");

                if(log_in_mode.equals(USER_MODE)){
                    Intent i = new Intent(SplashActivity.this, UserMainActivity.class);
                    startActivity(i);
                    finish();
                }else if(log_in_mode.equals(ADMIN_MODE)){
                    Intent i = new Intent(SplashActivity.this, OwnerMainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashActivity.this, LogInActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, 2000);



    }
}