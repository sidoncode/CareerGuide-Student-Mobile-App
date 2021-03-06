package com.careerguide;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PassageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passage);
        TextView textView = (TextView) findViewById(R.id.passage);
        String paragraph = "";
        for (String passage : Utility.paragraphs)
        {
            paragraph += passage +"\n\n";
        }
        textView.setText(paragraph);

        findViewById(R.id.button).setOnClickListener(v -> onBackPressed());

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
