package com.basic_innovations.kahiyeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentNext = new Intent(Splash_Activity.this, MainActivity.class);
                startActivity(intentNext);
                finish();
            }
        },4000);
    }
}
