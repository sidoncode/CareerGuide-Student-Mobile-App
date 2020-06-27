package com.careerguide.Book_One_To_One.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.AgoraBaseActivity;
import com.careerguide.Book_One_To_One.adapter.OneToOneMessageContainer;
import com.careerguide.Book_One_To_One.model.OneToOneChatModel;
import com.careerguide.CounsellorProfile;
import com.careerguide.OnRtcEnventCallback;
import com.careerguide.Onboarding;
import com.careerguide.R;
import com.careerguide.RtcEngineManager;
import com.careerguide.RtmClientManager;
import com.careerguide.RtmEnventCallback;
import com.careerguide.Utility;
import com.careerguide.VolleySingleton;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.AgoraImage;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmMessage;

public  class OneToOneSessionActivity extends AgoraBaseActivity implements OnRtcEnventCallback, RtmEnventCallback.OnRtmMessageListener {


    protected RtcEngine mRtcEngine;
    protected RtmClient mRtmClient = null;
    protected RtmChannel mRtmChannel = null;
    private RtmEnventCallback mRtmEventListener;
    private boolean mIsMsgChannelEnable;

    protected final int ANCHOR_UID = Integer.MAX_VALUE;

    private AlertDialog alertDialog;
    private Activity activity = this;

    private TextView live_no_surfaceview_notice, host_name, textLockedMessage, backFromProfile, openHostProfile, zoomHostName, online, timeLeft;
    private RelativeLayout live_no_surfaceview, audio_or_video_background, sessionLocked, zoomHostDetails;
    private FrameLayout live_surfaceview;
    RecyclerView chatRecyclerView;

    private EditText live_msg_et;

    private ImageView endCall, disable_audio, enable_audio, host_image_audio_call, audio_call, video_call, viewHostImageZoom;

    private List<OneToOneChatModel> chatList = new ArrayList<>();
    private OneToOneMessageContainer messageContainer;


    private String bookingId = "";
    private String hostFullName = "";
    private String channelName = "";
    private String hostImage = "";
    private String scheduledesc = "";
    private String fileName = "";
    private String privateUID = "";
    private String privateUserName = "";
    private String privateSessionDate = "";
    private String privateSessionTime = "";

    int minLeft=0;

    private String TAG = "OneToOneSessionActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_session);
        timeLeft = findViewById(R.id.timeLeft);
        enable_audio = findViewById(R.id.enable_audio);
        disable_audio = findViewById(R.id.disable_audio);
        live_no_surfaceview = findViewById(R.id.live_no_surfaceview);
        live_no_surfaceview_notice = findViewById(R.id.live_no_surfaceview_notice);
        sessionLocked = findViewById(R.id.sessionLocked);
        textLockedMessage = findViewById(R.id.textLockedMessage);
        host_name = findViewById(R.id.host_name);
        audio_call = findViewById(R.id.audio_call);
        video_call = findViewById(R.id.video_call);
        audio_or_video_background = findViewById(R.id.audio_or_video_background);
        endCall = findViewById(R.id.endCall);
        live_surfaceview = findViewById(R.id.live_surfaceview);
        host_image_audio_call = findViewById(R.id.host_image_audio_call);
        live_msg_et = findViewById(R.id.live_msg_et);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        zoomHostDetails = findViewById(R.id.zoomHostDetails);
        backFromProfile = findViewById(R.id.backFromProfile);
        openHostProfile = findViewById(R.id.openHostProfile);
        viewHostImageZoom = findViewById(R.id.viewHostImageZoom);
        zoomHostName = findViewById(R.id.zoomHostName);
        online = findViewById(R.id.online);


        RtcEngineManager.getInstance().init(this);
        RtmClientManager.getInstance().init(this);


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        Log.e("deeplink --> ", "" + deepLink);
                        String url = deepLink + "";
                        url = url.substring(url.lastIndexOf("&") + 16);
                        url = url.replaceFirst("%7B", "{");
                        url = url.replaceFirst("%7D", "}");
                        url = url.replace("%0A", "");
                        url = url.replace("%22", "\"");
                        url = url.replace("%0A", "+");
                        url = url.replace("+", " ");
                        Log.i("jjjsson", url);
                        try {
                            JSONObject jsonObject = new JSONObject(url.toString());
                            bookingId = jsonObject.optString("booking_id");
                            channelName = jsonObject.optString("channel_name");
                            hostFullName = jsonObject.optString("host_name");
                            hostImage = jsonObject.optString("host_image");
                            privateUID = jsonObject.optString("privateUID");
                            privateUserName = jsonObject.optString("privateUserName");
                            privateSessionDate = jsonObject.optString("privateSessionDate");
                            privateSessionTime = jsonObject.optString("privateSessionTime");

                            String[] params = {bookingId};
                            messageContainer = new OneToOneMessageContainer(chatRecyclerView);

                            new TaskGetMessageHistory().execute(params);
                            if (privateUID.contentEquals(Utility.getUserId(activity))){
                                textLockedMessage.setText("You will get access at " + privateSessionTime + " on " + privateSessionDate + " \n Session for \n" + privateUserName + ".");

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        initView();
                                        initRtcEngine();
                                        initRtmClient();

                                    }
                                }, 2000);

                             }else{
                                textLockedMessage.setText("This is a private session, you don't have access to view.");
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.loading)
                            .error(R.drawable.loading)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .dontAnimate()
                            .dontTransform();
                    Glide.with(activity).load(hostImage).apply(options).into((ImageView) findViewById(R.id.host_image));
                    host_name.setText(hostFullName);


                })
                .addOnFailureListener(this, e -> Log.e("dynamic links--> ", "getDynamicLink:onFailure", e));


    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

        if (uid == ANCHOR_UID) {
            activity.runOnUiThread(() -> {
                //sessionLocked.setVisibility(View.GONE);
                online.setBackgroundColor(Color.GREEN);
            });

        } else {
            online.setBackgroundColor(Color.RED);
        }

    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        Log.i("stream from:", uid + "");
    }

    @Override
    public void onMessageReceived(boolean isChannelMsg, String uid, String message) {

        Log.i("messsss", uid + "__" + message);
        //messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(uid),message+"",getTimeStamp()+"",hostImage,true));
        if (message.contains("Call-Ended at")) {
            activity.runOnUiThread(() -> {
                audio_call.setVisibility(View.VISIBLE);
                video_call.setVisibility(View.VISIBLE);
                audio_or_video_background.setVisibility(View.GONE);
                mRtcEngine.enableLocalAudio(false);// disable audio

                live_msg_et.setEnabled(true);

                mRtcEngine.setupRemoteVideo(null);

                live_no_surfaceview_notice.setVisibility(View.GONE);

                sendToastMessage("Call ended");
            });

        }
    }

    @Override
    public void onRtmConnected() {

    }

    protected void initView() {


        findViewById(R.id.live_msg_send_btn).setOnClickListener(v -> doSendMsg());

        host_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomHostDetails.setVisibility(View.VISIBLE);

                zoomHostName.setText(hostFullName);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .dontAnimate()
                        .dontTransform();
                Glide.with(activity).load(hostImage).apply(options).into(viewHostImageZoom);


            }
        });


        ((ImageView) findViewById(R.id.host_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomHostDetails.setVisibility(View.VISIBLE);
                zoomHostName.setText(hostFullName);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .dontAnimate()
                        .dontTransform();
                Glide.with(activity).load(hostImage).apply(options).into(viewHostImageZoom);


            }
        });

        backFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomHostDetails.setVisibility(View.GONE);
            }
        });

        openHostProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CounsellorProfile.class);

                String temp = channelName.replace("_private", "*");
                int endIndexOfEmail = temp.indexOf("*");
                String hostEmail = channelName.substring(0, endIndexOfEmail);
                intent.putExtra("host_name", hostFullName);
                intent.putExtra("host_email", hostEmail);
                intent.putExtra("host_img", hostImage);
                startActivity(intent);
            }
        });


        audio_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID), "Voice-call at " + getTimeStamp(), getTimeStamp() + "", "", true));
                audio_call.setVisibility(View.GONE);
                video_call.setVisibility(View.GONE);
                audio_or_video_background.setVisibility(View.VISIBLE);
                host_image_audio_call.setVisibility(View.VISIBLE);
                live_surfaceview.setVisibility(View.GONE);

                live_msg_et.setEnabled(false);

                mRtcEngine.enableLocalAudio(true);//enable audio

                mRtcEngine.setAudioProfile(Constants.AUDIO_ROUTE_EARPIECE, Constants.AUDIO_SCENARIO_EDUCATION);


                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH)
                        .dontAnimate()
                        .dontTransform();
                Glide.with(activity).load(hostImage).apply(options).into((ImageView) findViewById(R.id.host_image_audio_call));

            }
        });

        video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID), "Video-call at " + getTimeStamp(), getTimeStamp() + "", "", true));
                audio_call.setVisibility(View.GONE);
                video_call.setVisibility(View.GONE);
                audio_or_video_background.setVisibility(View.VISIBLE);
                live_surfaceview.setVisibility(View.VISIBLE);
                host_image_audio_call.setVisibility(View.GONE);
                live_msg_et.setEnabled(false);


                mRtcEngine.enableLocalAudio(true);//enable audio
                mRtcEngine.setDefaultAudioRoutetoSpeakerphone(true);
                mRtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_DEFAULT, Constants.AUDIO_SCENARIO_EDUCATION);

                SurfaceView surface = RtcEngine.CreateRendererView(activity);
                ((FrameLayout) findViewById(R.id.live_surfaceview)).addView(surface);
                mRtcEngine.enableLocalAudio(true);
                mRtcEngine.setupRemoteVideo(new VideoCanvas(surface, VideoCanvas.RENDER_MODE_HIDDEN, ANCHOR_UID));
                live_no_surfaceview_notice.setText("Wait for the host to pick the call");
                live_no_surfaceview_notice.setVisibility(View.VISIBLE);
            }
        });

        enable_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtcEngine.enableLocalAudio(false);// disable audio
                enable_audio.setVisibility(View.GONE);
                disable_audio.setVisibility(View.VISIBLE);
                sendToastMessage("Mic is disabled");

            }
        });

        disable_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtcEngine.enableLocalAudio(true);// enable audio
                enable_audio.setVisibility(View.VISIBLE);
                disable_audio.setVisibility(View.GONE);
                sendToastMessage("Mic is enabled");
            }
        });

        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMsg("Call-Ended at " + getTimeStamp());

                audio_call.setVisibility(View.VISIBLE);
                video_call.setVisibility(View.VISIBLE);
                audio_or_video_background.setVisibility(View.GONE);
                mRtcEngine.enableLocalAudio(false);// disable audio

                live_msg_et.setEnabled(true);

                mRtcEngine.setupLocalVideo(null);

                live_no_surfaceview_notice.setVisibility(View.GONE);


                sendToastMessage("Call ended");
            }
        });


    }

    private void initRtcEngine() {
        Log.e("#inside engine", "ewkjbewjkfb");
        mRtcEngine = RtcEngineManager.getInstance().getRtcEngine();
        RtcEngineManager.getInstance().setOnRtcEventCallback(this);

        mRtcEngine.enableLocalAudio(false);//during messaging disable audio
        mRtcEngine.disableVideo();


        Log.e("#cmm engine", "ewkjbewjkfb" + channelName);
        mRtcEngine.joinChannel("", channelName, "", Integer.parseInt(privateUID));
        //mRtcEngine.joinChannel("", channelName, "", ANCHOR_UID);

        //mRtcEngine.setDefaultAudioRoutetoSpeakerphone(false);

    }

    private void initRtmClient() {

        mRtmClient = RtmClientManager.getInstance().getRtmClient();
        mRtmClient.login("", privateUID + "", new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "rtmClient login success" + "___uidd:" + channelName);
                checkChannelEnable();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "rtmClient login fail : " + errorInfo);
            }
        });
        mRtmEventListener = new RtmEnventCallback(channelName, this);
        setClientRoleAudience();
        //setClientRoleBroadcaster();
        if (null == mRtmClient) {
            showToast(R.string.toast_create_channel_error);
        }
    }

    private void checkChannelEnable() {
        if (null == mRtmChannel) {
            mRtmChannel = mRtmClient.createChannel(channelName, mRtmEventListener);
            RtmClientManager.getInstance().setRtmClientListener(mRtmEventListener);
            mRtmChannel.join(new ResultCallback<Void>() {//join the channel to post messages
                @Override
                public void onSuccess(Void aVoid) {
                    mIsMsgChannelEnable = true;
                    Log.e(TAG, "rtmClient join channel success");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    mIsMsgChannelEnable = false;
                    Log.e(TAG, "rtmClient join channel fail : " + errorInfo);
                }
            });
        }
    }


    private void doSendMsg() {
        if (!mIsMsgChannelEnable) {
            showToast("Trying again,give us a moment");
            //checkChannelEnable();
            return;
        }


        String msg = live_msg_et.getText().toString();
        Log.i("mmeessaageee", msg);
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.toast_msg_is_empty);
            return;
        }
        sendMsg(msg);
        live_msg_et.setText("");
    }


    protected void sendMsg(String msg) {
        RtmMessage rtmMessage = mRtmClient.createMessage();
        rtmMessage.setText(msg);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    mRtmChannel.sendMessage(rtmMessage, new ResultCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e(TAG, "sendMessage onSuccess");
                            runOnUiThread(() -> {
                                //hideSoftKeyboard(live_msg_et);
                                //showToast("Message sent");
                                if (!TextUtils.isEmpty(msg)) {
                                    Log.i("mmeessaage", msg);

                                    messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID), msg + "", getTimeStamp() + "", privateUserName.toUpperCase().charAt(0) + "", true));

                                    String[] saveMessageParams = {bookingId, "-1", privateUID, privateUID, msg, getTimeStamp(), privateSessionDate};
                                    new TaskSaveMessage().execute(saveMessageParams);

                                }
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {
                            runOnUiThread(() -> {
                                messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID), msg + "", getTimeStamp() + "", privateUserName.toUpperCase().charAt(0) + "", false));
                            });
                            Log.e(TAG, "sendMessage onFailure : " + errorInfo + "_____:" + errorInfo.getErrorDescription() + "    errorcode:" + errorInfo.getErrorCode() + "  chanel id:" + mRtmChannel.getId());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();//run the send message on the background thread


    }


    private void setClientRoleAudience() {
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
    }

    private void setClientRoleBroadcaster() {
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
    }


    @Override
    public void onBackPressed() {

        alertDialog = new AlertDialog.Builder(activity).create();
        final View dialog = getLayoutInflater().inflate(R.layout.dialog_log_out, null);//using the same logout dialog
        ((TextView) dialog.findViewById(R.id.textViewLogoutTitle)).setText("Do you want to leave the private session?");

        dialog.findViewById(R.id.no).setOnClickListener(v1 -> alertDialog.dismiss());
        dialog.findViewById(R.id.yes).setOnClickListener(v12 -> {

            if (mRtcEngine != null) {
                mRtcEngine.leaveChannel();
                mRtcEngine.setupRemoteVideo(null);
                RtcEngine.destroy();
                mRtcEngine = null;
            }
            RtmClientManager.getInstance().setRtmClientListener(null);
            RtcEngineManager.destory();
            finish();
            super.onBackPressed();
        });

        alertDialog.setView(dialog);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }

        Utility.stopTimer();

    }

    String getTimeStamp() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        minute = minute.length() == 1 ? "0" + minute : minute;
        int hour = calendar.get(Calendar.HOUR);
        return (hour == 0 ? 12 : hour) + ":" + minute + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
    }

    void sendToastMessage(String toastMessage) {
        Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT).show();
    }


    private class TaskGetMessageHistory extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {


                JSONObject jsonBody = new JSONObject();


                jsonBody.put("fk_booking_id", params[0]);


                Log.i("jsonbodyy", jsonBody + "");

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, /*"https://app.careerguide.com/api/main/bookOneToOne"*/Utility.albinoServerIp + "/FoodRunner-API/foodrunner/v2/careerguide/fetch_one_to_one_chat_history.php", jsonBody, response -> {


                    try {
                        JSONObject jsonObject = new JSONObject(response + "");
                        Log.i("response->", jsonObject + "");
                        boolean status = jsonObject.optBoolean("status", false);
                        if (status) {
                            Log.i("message", "saved_to_db");


                            JSONArray jsonArrayMessages = response.getJSONArray("data");
                            for (int i = 0; i < jsonArrayMessages.length(); i++) {
                                JSONObject eachMessage = jsonArrayMessages.getJSONObject(i);

                                runOnUiThread(() -> {
                                    try {
                                        if (!eachMessage.getString("sender_id").contentEquals(Utility.getUserId(activity))) {
                                            messageContainer.addMessage(new OneToOneChatModel(ANCHOR_UID, eachMessage.getString("message") + "", eachMessage.getString("time_stamp") + "", hostImage, true));
                                        } else {
                                            messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(eachMessage.getString("sender_id")), eachMessage.getString("message") + "", eachMessage.getString("time_stamp") + "", privateUserName.toUpperCase().charAt(0) + "", true));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });

                            }


                        } else {
                            Log.i("message", "not_saved_to_db");
                        }
                        //pb_loading.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    // pb_loading.setVisibility(View.GONE);
                    Log.i("message", "not_saved_to_db");
                    error.printStackTrace();
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Basic ZTg1YWQyZjg3Mzc0NDc5ZWE5ZjZhMTE0MmY5NTRjZjc6YjdiZTUxM2Q4ZDI0NGFiNWFlYWU0ZWQxNWYwZDIyNWM=");
                        return headers;
                    }
                };
                VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }


    }

    private class TaskSaveMessage extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {


                JSONObject jsonBody = new JSONObject();


                jsonBody.put("fk_booking_id", params[0]);
                jsonBody.put("host_id", params[1]);
                jsonBody.put("student_id", params[2]);
                jsonBody.put("sender_id", params[3]);
                jsonBody.put("message", params[4]);
                jsonBody.put("time_stamp", params[5]);
                jsonBody.put("date_sent", params[6]);

                Log.i("jsonbodyy", jsonBody + "");

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, /*"https://app.careerguide.com/api/main/bookOneToOne"*/Utility.albinoServerIp + "/FoodRunner-API/foodrunner/v2/careerguide/save_one_to_one_chat_message.php", jsonBody, response -> {


                    try {
                        JSONObject jsonObject = new JSONObject(response + "");
                        Log.i("response->", jsonObject + "");
                        boolean status = jsonObject.optBoolean("status", false);
                        if (status) {
                            Log.i("message", "saved_to_db");
                        } else {
                            Log.i("message", "not_saved_to_db");
                        }
                        //pb_loading.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    // pb_loading.setVisibility(View.GONE);
                    Log.i("message", "not_saved_to_db");
                    error.printStackTrace();
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Basic ZTg1YWQyZjg3Mzc0NDc5ZWE5ZjZhMTE0MmY5NTRjZjc6YjdiZTUxM2Q4ZDI0NGFiNWFlYWU0ZWQxNWYwZDIyNWM=");
                        return headers;
                    }
                };
                VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.keepTrackOfTimeWithServer(this);
    }

    public void updateTimer(String serverDate, String serverTime) {
        Log.i("serverdate", serverDate);
        Log.i("servertime", serverTime);

        int serverMins=Integer.parseInt(serverTime.substring(3, 5));
        int serverHour=Integer.parseInt(serverTime.substring(0, 2));

        int sessionStartMins=Integer.parseInt(privateSessionTime.substring(3, 5));
        int sessionStartHour=Integer.parseInt(privateSessionTime.substring(0, 2));
        int sessionEndMins=0;
        int sessionEndHour=0;


        String warningTime="";
        String endTime="";

        switch (sessionStartMins){
            case 0:{
                sessionEndMins=45;
                sessionEndHour=sessionStartHour;
                break;}
            case 45:{
                sessionEndMins=30;
                sessionEndHour=sessionStartHour+1;
                break;}
            case 30:{
                sessionEndMins=15;
                sessionEndHour=sessionStartHour+1;
                break;}
            case 15:{
                sessionEndMins=0;
                sessionEndHour=sessionStartHour+1;
                break;}

        }

        int tempTime=sessionEndMins-10;
        String formattedTime="";
        if(tempTime/10==0)
        {
            formattedTime="0"+tempTime;
        }else{
            formattedTime=""+tempTime;
        }
        if (sessionEndHour/10==0){




            warningTime="0"+sessionEndHour+":"+formattedTime;
            endTime="0"+sessionEndHour+":"+sessionEndMins;
        }else{
            warningTime=sessionEndHour+":"+formattedTime;
            endTime=sessionEndHour+":"+sessionEndMins;
        }

        if (sessionEndHour>11){
            warningTime=warningTime+"PM";
            endTime=endTime+"PM";
        }else
        {
            warningTime=warningTime+"AM";
            endTime=endTime+"AM";
        }

        Log.i("Endsession",""+endTime);
        Log.i("WarningTime",""+warningTime);

        if (serverHour==sessionEndHour&sessionStartHour==sessionEndHour&privateSessionDate.contentEquals(serverDate))
            minLeft=45-serverMins;
        else {


            if ((sessionStartHour + 1) == sessionEndHour  & privateSessionDate.contentEquals(serverDate)) {
                if (serverHour < sessionEndHour) {
                    minLeft = 75 - serverMins;
                }

                if (serverHour == sessionEndHour) {

                    if (sessionEndMins == 30) {
                        minLeft = 30 - serverMins;
                    }
                    if (sessionEndMins == 15) {
                        minLeft = 15 - serverMins;
                    }
                    if (sessionEndMins == 0) {
                        minLeft = 60 - serverMins;
                    }
                }
            }else{
                runOnUiThread(()->{
                    sessionLocked.setVisibility(View.VISIBLE);
                });
                return;
            }
        }


        Log.i("minLeft",""+minLeft);

        if (minLeft>0&minLeft<46){

            runOnUiThread(()->{
                sessionLocked.setVisibility(View.GONE);
                timeLeft.setText(minLeft + " minutes left");
            });

        }

        if (minLeft==10)
        runOnUiThread(() -> {
            timeLeft.setText(minLeft + " minutes left");
        });

        String hours = privateSessionTime.substring(0, 2);
        String mins = privateSessionTime.substring(3, 5);

        String warningTimer = hours + ":" + (Integer.parseInt(mins) + 35 + privateSessionTime.substring(5, 7));
        String endSessionTime = hours + ":" + (Integer.parseInt(mins) + 45 + privateSessionTime.substring(5, 7));


        if (serverTime.contentEquals(warningTime)&privateSessionDate.contentEquals(serverDate)) {
            runOnUiThread(() -> {
                Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

                // Vibrate for 400 milliseconds
                v.vibrate(400);

                sendToastMessage("Last 10 minutes");
            });

        }
            if (serverTime.contentEquals(endTime)&privateSessionDate.contentEquals(serverDate)) {
                runOnUiThread(() -> {
                    timeLeft.setText("Session is over");

                    Utility.stopTimer();

                    sendToastMessage("Session ended!");
                    if (mRtcEngine != null) {
                        mRtcEngine.leaveChannel();
                        mRtcEngine.setupRemoteVideo(null);
                        RtcEngine.destroy();
                        mRtcEngine = null;
                    }
                    RtmClientManager.getInstance().setRtmClientListener(null);
                    RtcEngineManager.destory();
                    Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);

                    // Vibrate for 400 milliseconds
                    v.vibrate(400);

                    alertDialog = new AlertDialog.Builder(activity).create();
                    final View dialog = getLayoutInflater().inflate(R.layout.one_to_one_feedback_form,null);
                    ((TextView)dialog.findViewById(R.id.menteeName)).setText(privateUserName);

                    alertDialog.setCancelable(false);

                    dialog.findViewById(R.id.cancel).setOnClickListener(v1 -> {
                        alertDialog.dismiss();
                        finish();
                    });
                    dialog.findViewById(R.id.submit).setOnClickListener(v12 -> {
                        alertDialog.dismiss();
                        finish();

                    });

                    alertDialog.setView(dialog);
                    alertDialog.show();


                });


            }

    }
}