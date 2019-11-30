package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class video_chat_counsellor extends AppCompatActivity {
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_counsellor);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new CounsellorCornerFragment()).commit();
        setTitle("Our Counsellors");
//        findViewById(R.id.click_whole).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(activity , VideoChatViewActivity.class);
////                startActivity(intent);
//            }
//        });
    }
}
