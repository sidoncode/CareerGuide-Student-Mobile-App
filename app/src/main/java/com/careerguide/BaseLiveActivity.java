package com.careerguide;
import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
    protected RtmClient mRtmClient=null;
    protected RtmChannel mRtmChannel=null;
    private MessageContainer mMsgContainer;
    private RtmEnventCallback mRtmEventListener;
    private boolean mIsMsgChannelEnable;
    private int user_count =0;
    private String Fname;
    private String Lname;

    private AlertDialog alertDialog;

    Map<Integer,BaseLiveCurrentUsersModel> currentUsersList;//holds all the current users


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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreenStausBar();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_living);

        currentUsersList=new HashMap<>();//no users at the start



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /*if (getprivateUID().contentEquals(getRtcUid()+"")){//private session for the user only
                    initView();
                    initRtcEngine();
                    initRtmClient();
                }else{
                    if (getprivateUID().contentEquals("")&&!getchannelid().contains("privatesession")){//free session
                        initView();
                        initRtcEngine();
                        initRtmClient();
                    }
                }*/
                initView();
                initRtcEngine();
                initRtmClient();

            }
        }, 3000);

    }

    protected void initView() {
        ((TextView)findViewById(R.id.live_no_surfaceview_notice)).setText(R.string.living_anchor_offline);
        mMsgContainer = new MessageContainer(findViewById(R.id.live_msg_recycler_view));
        etChatMsg = findViewById(R.id.live_msg_et);
        findViewById(R.id.live_msg_send_btn).setOnClickListener(v -> doSendMsg());
    }
    protected abstract int getRtcUid();
    protected abstract String getchannelid();
    protected abstract String getprivateUID();
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
        if (getchannelid().contentEquals("")){
            showToast("Internet is slow!");
        }
    }

    private void initRtmClient() {

        mRtmClient = RtmClientManager.getInstance().getRtmClient();
        mRtmClient.login("", String.valueOf(getRtcUid()), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "rtmClient login success"+"___uidd:"+getRtcUid());
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "rtmClient login fail : " + errorInfo);
            }
        });
        mRtmEventListener = new RtmEnventCallback(getchannelid(), this);
        if (null == mRtmClient) {
            showToast(R.string.toast_create_channel_error);
        }
    }

    @Override
    public void onRtmConnected() {
        //this method never gets called
    }

    private void checkChannelEnable() {
        if (null == mRtmChannel) {
            mRtmChannel = mRtmClient.createChannel(getchannelid(), mRtmEventListener);

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
        if(!mIsMsgChannelEnable){
            showToast("Trying again,give us a moment");
            //checkChannelEnable();
            return;
        }


        String msg = etChatMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.toast_msg_is_empty);
            return;
        }
        sendMsg(msg);
        etChatMsg.setText("");
    }



    protected void sendMsg(String msg) {
        RtmMessage rtmMessage = mRtmClient.createMessage();
        rtmMessage.setText(msg);



        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    mRtmChannel.sendMessage(rtmMessage,new ResultCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e(TAG, "sendMessage onSuccess");
                            runOnUiThread(()->{
                                hideSoftKeyboard(etChatMsg);
                                showToast("Message sent");
                                if (!TextUtils.isEmpty(msg) && msg.startsWith(MSG_PREFIX_DIRECT_DISPLAY)) {
                                    mMsgContainer.addMessage(new LiveChatMessage(" ", msg.replace(MSG_PREFIX_DIRECT_DISPLAY, " ")));
                                } else {
                                    String finalMsg = msg.replace(MSG_PREFIX_QUESTION, " ").replace(MSG_PREFIX_QUESTION_RESULT, " ");
                                    mMsgContainer.addMessage(new LiveChatMessage(getUserName(getRtcUid()), finalMsg));
                                }
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {
                            runOnUiThread(()-> {
                                showToast("Message was not sent due to some error!");
                            });
                            Log.e(TAG, "sendMessage onFailure : " + errorInfo+"_____:"+errorInfo.getErrorDescription()+"    errorcode:"+errorInfo.getErrorCode()+"  chanel id:"+mRtmChannel.getId());
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();//run the send message on the background thread


    }


    @Override
    public void onUserJoined(final int uid, final int elapsed) {

        //getUserName(Integer.parseInt(uid));
        Log.i("userjoinedid",uid+"");

        Integer[] newUserIdArray={uid};
        new saveNewUserAsyncTask().execute(newUserIdArray);//save new user in the hashmap

    }



    @Override
    public void onUserOffline(final int uid, int reason) {
        Log.i("userjoinedidleft",uid+"");
        runOnUiThread(() -> {
            user_count = user_count-1;
            TextView textView2 = findViewById(R.id.live_user);
            textView2.setText(String.valueOf(user_count) );
            mMsgContainer.addMessage(new LiveChatMessage("", getUserNameRemotelyJoined(uid) + " " + "Left"));
            removeUser(uid);//remove the user from the currentUserList
            if (uid==ANCHOR_UID) {//only when the host leaves we need to finish the activity and release all the resources
                mRtcEngine.leaveChannel();
                mRtcEngine.setupRemoteVideo(null);
                RtcEngine.destroy();
                mRtcEngine = null;
                finish();
            }
        });
    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {}
    @Override
    public final void onMessageReceived(final boolean isChannelMsg, final String uid, final String message) {
        if (isFinishing() || TextUtils.isEmpty(message) || TextUtils.isEmpty(uid)) {
            return;
        }

        handleMessageReceived(isChannelMsg, uid, message);


    }

    protected void handleMessageReceived(boolean isChannelMsg, String uid, String message) {
        if (message.startsWith(MSG_PREFIX_QUESTION_RESULT)) {
            return;
        }
        if (message.startsWith(MSG_PREFIX_DIRECT_DISPLAY)) {
            mMsgContainer.addMessage(new LiveChatMessage("", message.replace(MSG_PREFIX_DIRECT_DISPLAY, "")));
        } else {
            try {
                runOnUiThread(() -> {
                    mMsgContainer.addMessage(new LiveChatMessage(getUserNameRemotelyJoined(Integer.parseInt(uid)) + ": ", message));
                });
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

        alertDialog = new AlertDialog.Builder(activity).create();
        final View dialog = getLayoutInflater().inflate(R.layout.dialog_log_out,null);//using the same logout dialog
        ((TextView)dialog.findViewById(R.id.textViewLogoutTitle)).setText("Do you want to leave the live stream?");

        dialog.findViewById(R.id.no).setOnClickListener(v1 -> alertDialog.dismiss());
        dialog.findViewById(R.id.yes).setOnClickListener(v12 -> {

            if (mRtcEngine != null) {
                mRtcEngine.leaveChannel();
                //mRtcEngine.setupRemoteVideo(null);
                //RtcEngine.destroy();
                //mRtcEngine = null;
            }
            //RtmClientManager.getInstance().setRtmClientListener(null);
            //RtcEngineManager.destory();
            finish();
            super.onBackPressed();
        });

        alertDialog.setView(dialog);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(alertDialog!=null){
            alertDialog.dismiss();
            alertDialog=null;
        }

    }

    protected void onResume() {
        super.onResume();
        Utility.handleOnlineStatus(this, "idle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.handleOnlineStatus(null,"");
    }



    private class saveNewUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        int uid=0;


        @Override
        protected Void doInBackground(Integer... params) {
            uid=params[0];
            checkChannelEnable();

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST, "https://app.careerguide.com/api/main/" + "get_user_name",
                    response ->
                    {

                        JSONObject jobj = null;
                        try {
                            jobj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //currentUsersList is a hashmap which take key as the uid and value as a model of type BaseLiveCurrentUsersModel
                        currentUsersList.put(uid,new BaseLiveCurrentUsersModel(uid,jobj.optString("first_name"),jobj.optString("last_name")));
                        if(jobj.optString("first_name").contains("null")||jobj.optString("last_name").contains("null"))
                            mMsgContainer.addMessage(new LiveChatMessage("",  "You " +"Joined"));
                        else
                            mMsgContainer.addMessage(new LiveChatMessage("", jobj.optString("first_name")+" "+ jobj.optString("last_name") + " " +"Joined"));

                    },error ->
            {
                Log.e("error","could't save new user");
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("id" , Integer.toString(uid));
                    Log.e("#line_status_request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            findViewById(R.id.live_msg).setVisibility(TextView.VISIBLE);
            TextView textView2 = findViewById(R.id.live_user);

            textView2.setText((++user_count)+"");//remove the random counter
            Log.e("#user_count" , "count" +user_count);

            Log.e("uid on join","-->"+uid);

        }
    }

    private String getUserNameRemotelyJoined(int uid){//returns fname and lname of the user

        if (uid == getRtcUid()) {
            return getString(R.string.me);
        } else if (uid == ANCHOR_UID) {
            return getString(R.string.anchor);
        }

        BaseLiveCurrentUsersModel searchedUser=currentUsersList.get(uid);//returns null if not present

        if(searchedUser==null)
            return "User ";
        else
            return searchedUser.getFname()+" "+searchedUser.getLname();
    }

    private void removeUser(int uid){
        currentUsersList.remove(uid);
    }


}