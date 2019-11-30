package com.careerguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careerguide.Album;
import com.careerguide.R;
import com.careerguide.Video_player;
import com.careerguide.adapters.AllTopicsItemAdapter;
import com.careerguide.adapters.feedDetailAdapter;
import com.careerguide.models.topics_model;

import java.util.ArrayList;
import java.util.List;

public class feedDetailActivity extends AppCompatActivity {


    private RecyclerView recycler;
    private List<Album> albumList;
    private ArrayList<topics_model> topics = new ArrayList<>();
    private int topicSize;
    private feedDetailAdapter feed_adapter;
    LinearLayoutManager mLayoutManager;
    private ImageView imageView;
    private TextView textView;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_details);
//
//        recycler = (RecyclerView) findViewById(R.id.rv_ongoing_courses);
//        albumList = new ArrayList<>();
//        feed_adapter = new feedDetailAdapter(this, albumList);
//        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recycler.setLayoutManager(mLayoutManager);
//        recycler.setAdapter(feed_adapter);

        imageView= (ImageView) findViewById(R.id.thumbnail);
        Glide.with(this).load(getIntent().getStringExtra("imgurl")).into(imageView);
        textView = (TextView) findViewById(R.id.count);
        textView.setText(getIntent().getStringExtra("title"));
        title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("Fullname"));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , Video_player.class);
                intent.putExtra("live_video_url" , getIntent().getStringExtra("live_video_url"));
                intent.putExtra("title" , getIntent().getStringExtra("title"));
                intent.putExtra("Fullname" ,getIntent().getStringExtra("Fullname") );
                intent.putExtra("imgurl" , getIntent().getStringExtra("imgurl"));
                intent.putExtra("host_email" , getIntent().getStringExtra("host_email"));
                view.getContext().startActivity(intent);
            }
        });

    }

}
