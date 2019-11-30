package com.careerguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        findViewById(R.id.Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                intent.putExtra("selection",1);
                startActivity(intent);
                onBackPressed();
                finish();
            }
        });

        findViewById(R.id.studentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                intent.putExtra("selection",1);
                startActivity(intent);
                onBackPressed();
                finish();
            }
        });
        findViewById(R.id.counsellorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                intent.putExtra("selection",2);
                startActivity(intent);
                onBackPressed();
                finish();
            }
        });
        findViewById(R.id.parentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                intent.putExtra("selection",3);
                startActivity(intent);
                onBackPressed();
                finish();
            }
        });
    }
}
