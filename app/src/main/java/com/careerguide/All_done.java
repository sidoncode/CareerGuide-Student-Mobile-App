package com.careerguide;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class All_done extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_done);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(All_done.this, HomeActivity.class);
                startActivity(i);
            }
        });

        TextView tv=(TextView)findViewById(R.id.tv);
        String text= "You are expected to complete the ‘CareerGuide Certification Course for Guiding Students’, post which you will gain a 6 character access code (UAC) to help you log in the cousellor app.";


        SpannableString ss = new SpannableString(text);

        ClickableSpan clickableSpan1= new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Uri uri= Uri.parse("https://www.careerguide.com/certification-course-for-guiding-school-students-online");
                Intent i=new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        };
        ss.setSpan(clickableSpan1, 34, 87, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
        tv.setMovementMethod(LinkMovementMethod.getInstance());




    }
}
