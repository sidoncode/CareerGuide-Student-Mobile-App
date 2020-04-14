package com.careerguide.blog.activity;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

import com.careerguide.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainBlog extends AppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar id_toolbar;
    BottomNavigationView bottomNavigation;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        id_toolbar.setTitle(getString(R.string.app_name));
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(activity, MainBlog.class));
                        return true;
                    case R.id.navigation_sms:
                        startActivity(new Intent(activity, CatListActivity.class));
                        return true;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(activity, CatDetailActivity.class));
                        return true;
                }
                return false;
            };

    @OnClick(R.id.btn_category)
    void open_category() {
        startActivity(new Intent(MainBlog.this, CatListActivity.class));
    }

    @OnClick(R.id.btn_post)
    void open_post() {
        startActivity(new Intent(MainBlog.this, CatDetailActivity.class));
    }

    @OnClick(R.id.btn_post_without)
    void open_post_without() {
        startActivity(new Intent(MainBlog.this, CatDetailWoActivity.class));
    }
}
