package com.careerguide.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.careerguide.R;
import com.careerguide.Video_player;
public class feedDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);
        ImageView imageView = findViewById(R.id.thumbnail);
        TextView tv_topic_group_name = findViewById(R.id.tv_topic_group_name);
        Glide.with(this).load(getIntent().getStringExtra("imgurl")).into(imageView);
        TextView textView =  findViewById(R.id.count);
        textView.setText(getIntent().getStringExtra("title"));
        TextView title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("Fullname"));
        tv_topic_group_name.setText(getIntent().getStringExtra("class_cat"));

        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext() , Video_player.class);
            intent.putExtra("live_video_url" , getIntent().getStringExtra("live_video_url"));
            intent.putExtra("title" , getIntent().getStringExtra("title"));
            intent.putExtra("Fullname" ,getIntent().getStringExtra("Fullname") );
            intent.putExtra("imgurl" , getIntent().getStringExtra("imgurl"));
            intent.putExtra("host_email" , getIntent().getStringExtra("host_email"));
            view.getContext().startActivity(intent);
        });
    }
    public void back_onClick(View view) {
        finish();
    }
}
