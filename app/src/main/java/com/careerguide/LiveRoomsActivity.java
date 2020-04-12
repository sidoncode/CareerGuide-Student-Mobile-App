package com.careerguide;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
public class LiveRoomsActivity extends AgoraBaseActivity {
    Activity activity = this;
    private String Channel_name;

    public LiveRoomsActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RtcEngineManager.getInstance().init(this);
        RtmClientManager.getInstance().init(this);
        String firstname = getIntent().getStringExtra("Firstname");
        String lastname = getIntent().getStringExtra("Lastname");
        Channel_name = getIntent().getStringExtra("channel_name");
        String counsellorpic = getIntent().getStringExtra("counsellorpic");
        Log.e("viewactivity" ,"show :" +Channel_name);
        findViewById(R.id.Joinlive).setOnClickListener(v -> {
//                Intent intent = new Intent(activity, ViewerLiveActivity.class);
//                intent.putExtra("Fname" , Firstname);
//                intent.putExtra("Lname" , Lastname);
//                intent.putExtra("Channel_name" , Channel_name);
//                Log.e("#inside_live_counsellor", "test" +Channel_name);
//                startActivity(intent);
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
