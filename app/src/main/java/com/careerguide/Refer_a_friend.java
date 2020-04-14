package com.careerguide;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

public class Refer_a_friend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_a_friend);
        setTitle("Refer a Friend");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button= findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String sharebody="Hi, I will like you to download amazing CareerGuide app which can help you connect to a career counsellor on chat, video, voice, take psychometric tests and plan your career dreams.\n\nAnd Yes...don't forget to share it with your friends and family- After all Sharing is Caring.\n\n\nhttps://play.google.com/store/apps/details?id=com.careerguide";
            intent.putExtra(Intent.EXTRA_TEXT, sharebody);
            startActivity(intent.createChooser(intent, "Share Body"));
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.handleOnlineStatus(this, "idle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.handleOnlineStatus(null,"");
    }
}
