package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import de.hdodenhof.circleimageview.CircleImageView;
public class LiveRoomsActivity extends AgoraBaseActivity {
    private String PRIVATE_SERVER = "http://app.careerguide.com/api/counsellor/";
    Activity activity = this;
    private String Firstname;
    private String Lastname;
    private String City;
    private String counsellorpic;
    private String Education_level;
    private String Channel_name;
    private CircleImageView circleImageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RtcEngineManager.getInstance().init(this);
        RtmClientManager.getInstance().init(this);
        Firstname = getIntent().getStringExtra("Firstname");
        Lastname = getIntent().getStringExtra("Lastname");
        Channel_name = getIntent().getStringExtra("channel_name");
        counsellorpic = getIntent().getStringExtra("counsellorpic");
        Log.e("viewactivity" ,"show :" +Channel_name);
        findViewById(R.id.Joinlive).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(activity, ViewerLiveActivity.class);
//                intent.putExtra("Fname" , Firstname);
//                intent.putExtra("Lname" , Lastname);
//                intent.putExtra("Channel_name" , Channel_name);
//                Log.e("#inside_live_counsellor", "test" +Channel_name);
//                startActivity(intent);
            }
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
