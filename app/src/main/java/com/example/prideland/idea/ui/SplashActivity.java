package com.example.prideland.idea.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.prideland.idea.R;

public class SplashActivity extends AppCompatActivity {
    Connection_Detector connection_detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        connection_detector = new Connection_Detector(this);
        if (connection_detector.isConnected())
        {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_LONG).show();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        },3000);
    }
}
