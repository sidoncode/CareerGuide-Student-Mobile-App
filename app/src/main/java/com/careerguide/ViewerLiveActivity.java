package com.careerguide;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

public class ViewerLiveActivity extends BaseLiveActivity {
    private TextView tvNoSurfaceNotice;
    private int unique_id;
    private String UID;
    private String Fullname;
    private String Channel_name;
    Activity activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        RtcEngineManager.getInstance().init(this);
        RtmClientManager.getInstance().init(this);
        UID  = Utility.getUserId(activity);
        unique_id = Integer.parseInt(UID);
//        String Fname = getIntent().getStringExtra("Fname");
//        String Lname = getIntent().getStringExtra("Lname");
        Channel_name = getIntent().getStringExtra("Channel_name");
//        Fullname = Fname + " " + Lname;
        Fullname = getIntent().getStringExtra("name");
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        super.initView();
        TextView textView = findViewById(R.id.live_user_nickname_tv);
        textView.setText(Fullname);
        findViewById(R.id.live_msg).setVisibility(TextView.GONE);
        findViewById(R.id.live_surfaceview).setVisibility(TextView.GONE);
        tvNoSurfaceNotice = findViewById(R.id.live_no_surfaceview_notice);
        tvNoSurfaceNotice.setVisibility(TextView.VISIBLE);
    }

    @Override
    protected int getRtcUid() {
        return unique_id;
    }

    @Override
    protected String getchannelid() {
        return Channel_name;
    }


    @Override
    protected void livePrepare(RtcEngine engine) {
        engine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        super.onUserJoined(uid, elapsed);
        if (ANCHOR_UID == uid) {
            runOnUiThread(() -> tvNoSurfaceNotice.setText(R.string.living_no_surfaceview));
        }
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        super.onUserOffline(uid, reason);
        if (ANCHOR_UID == uid) {
            runOnUiThread(() -> {
                findViewById(R.id.live_surfaceview).setVisibility(TextView.GONE);
                tvNoSurfaceNotice.setVisibility(TextView.VISIBLE);
                tvNoSurfaceNotice.setText(R.string.living_anchor_offline);
                finish();
            });
        }
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
        super.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        runOnUiThread(() -> {
            if (ANCHOR_UID == uid) {
                findViewById(R.id.live_surfaceview).setVisibility(TextView.VISIBLE);
                findViewById(R.id.live_no_surfaceview_notice).setVisibility(TextView.GONE);
            }
        });
    }

    @Override
    protected void handleMessageReceived(boolean isChannelMsg, String uid, String message) {
        if (!TextUtils.isEmpty(message) && message.startsWith(MSG_PREFIX_QUESTION)) {
            message = message.replace(MSG_PREFIX_QUESTION, "");
            handleReceiveQuestion(message);
        }
        super.handleMessageReceived(isChannelMsg, uid, message);
    }


    private void handleReceiveQuestion(String questionInfo) {
        Builder builder = new Builder(this);
        builder.setMessage(questionInfo);
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> sendMsg(MSG_PREFIX_QUESTION_RESULT + RESULT_NO));
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> sendMsg(MSG_PREFIX_QUESTION_RESULT + RESULT_YES));
        builder.show();
    }

    public void Liveuserclick(View view) {
        Log.e("INsise", " eede");
    }


}
