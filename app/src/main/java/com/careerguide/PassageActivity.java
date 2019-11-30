package com.careerguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
