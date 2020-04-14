package com.careerguide;
import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.RtmEnventCallback.OnRtmMessageListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmMessage;


public abstract class BaseLiveActivity extends AgoraBaseActivity implements OnRtcEnventCallback, OnRtmMessageListener { protected final String MSG_PREFIX_QUESTION = "AgoraQuestion:";
    Activity activity = this;
    protected final String MSG_PREFIX_QUESTION_RESULT = "AgoraQuestionResult:";
    protected final String MSG_PREFIX_DIRECT_DISPLAY = "AgoraDirectDisplay:";
    protected final String RESULT_NO = "yes";
    protected final String RESULT_YES = "no";
    protected final int ANCHOR_UID = Integer.MAX_VALUE;
    protected String TAG = getClass().getSimpleName() + "RtcEngine";
    private EditText etChatMsg;
    protected RtcEngine mRtcEngine;
    protected RtmClient mRtmClient;
    protected RtmChannel mRtmChannel;
    private MessageContainer mMsgContainer;
    private RtmEnventCallback mRtmEventListener;
    private boolean mIsMsgChannelEnable;
    private int user_count =1;
    private String Fname;
    private String Lname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreenStausBar();
        setContentView(R.layout.activity_living);
        initView();
        initRtcEngine();
        initRtmClient();
    }

    protected void initView() {
        mMsgContainer = new MessageContainer(findViewById(R.id.live_msg_recycler_view));
        etChatMsg = findViewById(R.id.live_msg_et);
        findViewById(R.id.live_msg_send_btn).setOnClickListener(v -> doSendMsg());
    }
    protected abstract int getRtcUid();
    protected abstract String getchannelid();
    protected abstract void livePrepare(RtcEngine engine);
    private void initRtcEngine() {
        Log.e("#inside engine","ewkjbewjkfb");
        mRtcEngine = RtcEngineManager.getInstance().getRtcEngine();
        RtcEngineManager.getInstance().setOnRtcEventCallback(this);
        livePrepare(mRtcEngine);
        SurfaceView surface = RtcEngine.CreateRendererView(this);
        ((FrameLayout) findViewById(R.id.live_surfaceview)).addView(surface);

        if (isAnchor()) {
            mRtcEngine.enableLocalAudio(true);
            mRtcEngine.setupLocalVideo(new VideoCanvas(surface, VideoCanvas.RENDER_MODE_HIDDEN, ANCHOR_UID));
            mRtcEngine.startPreview();
        } else {
            mRtcEngine.enableLocalAudio(false);
            mRtcEngine.setupRemoteVideo(new VideoCanvas(surface, VideoCanvas.RENDER_MODE_HIDDEN, ANCHOR_UID));
        }
        Log.e("#cmm engine","ewkjbewjkfb" +getchannelid());
        mRtcEngine.joinChannel("", getchannelid(), "", getRtcUid());
    }

    private void initRtmClient() {
        mRtmClient = RtmClientManager.getInstance().getRtmClient();
        mRtmClient.login("", String.valueOf(getRtcUid()), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "rtmClient login success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "rtmClient login fail : " + errorInfo);
            }
        });
        mRtmEventListener = new RtmEnventCallback(getchannelid(), this);
        //checkChannelEnable();
        if (null == mRtmClient) {
            showToast(R.string.toast_create_channel_error);
        }
    }

    @Override
    public void onRtmConnected() {
        if (isFinishing() || !checkChannelEnable() || mIsMsgChannelEnable) {
            return;
        }
        mRtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mIsMsgChannelEnable = true;
                Log.e(TAG, "rtmClient join channel success");
                showToast(R.string.toast_msg_service_ready);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                mIsMsgChannelEnable = false;
                Log.e(TAG, "rtmClient join channel fail : " + errorInfo);
            }
        });
    }

    private boolean checkChannelEnable() {
//        if (null == mRtmChannel) {
//           // mRtmChannel = mRtmClient.createChannel(getchannelid(), mRtmEventListener);
//            RtmClientManager.getInstance().setRtmClientListener(mRtmEventListener);
//            return false;
//        }
        return true;
    }

    private void doSendMsg() {
        if (!checkChannelEnable()) {
            showToast(R.string.toast_msg_service_error_and_retry);
            return;
        }
        String msg = etChatMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.toast_msg_is_empty);
            return;
        }
        sendMsg(msg, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideSoftKeyboard(etChatMsg);
                Log.e(TAG, "sendMessage onSuccess");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "sendMessage onFailure : " + errorInfo);
            }
        });
        etChatMsg.setText("");
    }
    protected void sendMsg(String msg) {
        sendMsg(msg, null);
    }
    protected void sendMsg(String msg, ResultCallback<Void> callback) {
        RtmMessage rtmMessage = mRtmClient.createMessage();
        rtmMessage.setText(msg);
        if (null == callback) {
            callback = mDefMsgSendCallback;
        }
        mRtmChannel.sendMessage(rtmMessage, callback);
        if (!TextUtils.isEmpty(msg) && msg.startsWith(MSG_PREFIX_DIRECT_DISPLAY)) {
            mMsgContainer.addMessage(new LiveChatMessage(" ", msg.replace(MSG_PREFIX_DIRECT_DISPLAY, " ")));
        } else {
            String finalMsg = msg.replace(MSG_PREFIX_QUESTION, " ").replace(MSG_PREFIX_QUESTION_RESULT, " ");
            mMsgContainer.addMessage(new LiveChatMessage(getUserName(getRtcUid()), finalMsg));
        }
    }

    private ResultCallback<Void> mDefMsgSendCallback = new ResultCallback<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Log.e(TAG, "sendMessage onSuccess");
        }

        @Override
        public void onFailure(ErrorInfo errorInfo) {
            Log.e(TAG, "sendMessage onFailure : " + errorInfo);
        }
    };

    @Override
    public void onUserJoined(final int uid, final int elapsed) {
        runOnUiThread(() -> {
            //getUserName(Integer.parseInt(uid));

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/" + "get_user_name", response -> {
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("user_name","-->" +response);
                Fname = jobj.optString("first_name");
                Lname= jobj.optString("last_name");
                Log.e("user_name","-->" +Fname+" "+Lname);
                runOnUiThread(() -> {
                    Random rand = new Random();
                    user_count++;
                    findViewById(R.id.live_msg).setVisibility(TextView.VISIBLE);
                    TextView textView2 = findViewById(R.id.live_user);
                    textView2.setText(String.valueOf(user_count) );
                    Log.e("uid on join","-->"+uid);
                    // mMsgContainer.addMessage(new LiveChatMessage("", getUserName(uid) + " " +"Joined"));
                });
            }, error -> Log.e("ussr_name_error","error"))
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("id" ,String.valueOf(uid));
                    Log.e("#line_status_request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
            try {
                Thread.sleep(1000); //1000 milliseconds is one second.
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
    }



    @Override
    public void onUserOffline(final int uid, int reason) {
        runOnUiThread(() -> {
            user_count = user_count-1;
            TextView textView2 = findViewById(R.id.live_user);
            textView2.setText(String.valueOf(user_count) );
           // mMsgContainer.addMessage(new LiveChatMessage("", getUserName(uid) + " " + "Left"));
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {}
    @Override
    public final void onMessageReceived(final boolean isChannelMsg, final String uid, final String message) {
        if (isFinishing() || TextUtils.isEmpty(message) || TextUtils.isEmpty(uid)) {
            return;
        }

        runOnUiThread(() -> {
            //getUserName(Integer.parseInt(uid));

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/" + "get_user_name", response -> {
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("user_name","-->" +response);
                Fname = jobj.optString("first_name");
                Lname= jobj.optString("last_name");
                Log.e("user_name","-->" +Fname+" "+Lname);
                handleMessageReceived(isChannelMsg, uid, message);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                handleMessageReceived(isChannelMsg, uid, message);
//                            }
//                        });
            }, error -> Log.e("ussr_name_error","error"))
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("id" ,uid);
                    Log.e("#line_status_request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
            try {
                Thread.sleep(1000); //1000 milliseconds is one second.
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });

    }

    protected void handleMessageReceived(boolean isChannelMsg, String uid, String message) {
        if (message.startsWith(MSG_PREFIX_QUESTION_RESULT)) {
            return;
        }
        if (message.startsWith(MSG_PREFIX_DIRECT_DISPLAY)) {
            mMsgContainer.addMessage(new LiveChatMessage("", message.replace(MSG_PREFIX_DIRECT_DISPLAY, "")));
        } else {
            try {
                mMsgContainer.addMessage(new LiveChatMessage(Fname+ " " +Lname +": ", message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserName(int uid) {
        return Utility.getUserFirstName(activity) + " " + Utility.getUserLastName(activity) + ": ";
    }

    private boolean isAnchor() {
        return ANCHOR_UID == getRtcUid();
    }

    @Override
    public void onBackPressed() {
        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }

        mRtcEngine.setupRemoteVideo(null);
        RtcEngine.destroy();
        mRtcEngine = null;
        finish();
        super.onBackPressed();
    }

}
