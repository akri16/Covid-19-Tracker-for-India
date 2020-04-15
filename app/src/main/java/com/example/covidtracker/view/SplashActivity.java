package com.example.covidtracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.covidtracker.R;
import com.example.covidtracker.utils.DataUtils;

import java.util.Collection;
import java.util.Collections;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        Runnable runnable = () -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        };
        handler.postDelayed(runnable, 2000);

    }
}
