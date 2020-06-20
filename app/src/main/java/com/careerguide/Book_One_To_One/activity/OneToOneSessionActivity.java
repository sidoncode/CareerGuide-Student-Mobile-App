package com.careerguide.Book_One_To_One.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.AgoraBaseActivity;
import com.careerguide.Book_One_To_One.adapter.OneToOneMessageContainer;
import com.careerguide.Book_One_To_One.model.OneToOneChatModel;
import com.careerguide.OnRtcEnventCallback;
import com.careerguide.R;
import com.careerguide.RtcEngineManager;
import com.careerguide.RtmClientManager;
import com.careerguide.RtmEnventCallback;
import com.careerguide.Utility;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    protected RtmClient mRtmClient=null;
    protected RtmChannel mRtmChannel=null;
    private RtmEnventCallback mRtmEventListener;
    private boolean mIsMsgChannelEnable;

    protected final int ANCHOR_UID = Integer.MAX_VALUE;

    private AlertDialog alertDialog;
    private Activity activity=this;

    private TextView live_no_surfaceview_notice,host_name;
    private RelativeLayout live_no_surfaceview,audio_or_video_background;
    private FrameLayout live_surfaceview;
    RecyclerView chatRecyclerView;

    private EditText live_msg_et;

    private ImageView endCall,disable_audio,enable_audio,host_image_audio_call,audio_call,video_call;

    private List<OneToOneChatModel> chatList=new ArrayList<>();
    private OneToOneMessageContainer messageContainer;


    private String hostFullName="";
    private String channelName="";
    private String hostImage ="";
    private String scheduledesc="";
    private String fileName ="";
    private String privateUID="";
    private String privateUserName="";
    private String privateSessionDate="";
    private String privateSessionTime="";

    private String TAG="OneToOneSessionActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_session);
        enable_audio=findViewById(R.id.enable_audio);
        disable_audio=findViewById(R.id.disable_audio);
        live_no_surfaceview=findViewById(R.id.live_no_surfaceview);
        live_no_surfaceview_notice=findViewById(R.id.live_no_surfaceview_notice);
        host_name=findViewById(R.id.host_name);
        audio_call=findViewById(R.id.audio_call);
        video_call=findViewById(R.id.video_call);
        audio_or_video_background=findViewById(R.id.audio_or_video_background);
        endCall=findViewById(R.id.endCall);
        live_surfaceview=findViewById(R.id.live_surfaceview);
        host_image_audio_call=findViewById(R.id.host_image_audio_call);
        live_msg_et=findViewById(R.id.live_msg_et);
        chatRecyclerView=findViewById(R.id.chatRecyclerView);

        RtcEngineManager.getInstance().init(this);
        RtmClientManager.getInstance().init(this);


        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        Log.e("deeplink --> " , "" +deepLink);
                        String url=deepLink+"";
                        url=url.substring(url.lastIndexOf("&")+16);
                        url=url.replaceFirst("%7B","{");
                        url=url.replaceFirst("%7D","}");
                        url=url.replace("%0A","");
                        url=url.replace("%22","\"");
                        url=url.replace("%0A","+");
                        url=url.replace("+"," ");
                        Log.i("jjjsson",url);
                        try {
                            JSONObject jsonObject=new JSONObject(url.toString());
                            channelName=jsonObject.optString("channel_name");
                            hostFullName = jsonObject.optString("host_name");
                            hostImage = jsonObject.optString("host_image");
                            privateUID=jsonObject.optString("privateUID");
                            privateUserName=jsonObject.optString("privateUserName");
                            privateSessionDate=jsonObject.optString("privateSessionDate");
                            privateSessionTime=jsonObject.optString("privateSessionTime");



                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    initView();
                                    initRtcEngine();
                                    initRtmClient();

                                }
                            }, 2000);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        String url="https://play.google.com/store/apps/details?id=com.careerguide&hl=en_US&sessionDetails=%7B%22channel_name%22:%22surabhi@careerguide.com_privatesession_Friday_19_Jun_20_09_45AM%22,%22host_name%22:%22Surabhi+Surabhi%22,%22host_image%22:%22https://app.careerguide.com/api/user_dir/5dea36c09d66d1575630528.jpeg%22,%22privateUID%22:%2213599%22,%22privateUserName%22:%22Albino+Braganza%22,%22privateSessionDate%22:%22Friday+19+Jun+20%22,%22privateSessionTime%22:%2209:45AM%22%7D";
                        url=url.substring(url.lastIndexOf("&")+16);
                        url=url.replaceFirst("%7B","{");
                        url=url.replaceFirst("%7D","}");
                        url=url.replace("%0A","");
                        url=url.replace("%22","\"");
                        url=url.replace("%0A","+");
                        url=url.replace("+"," ");
                        Log.i("jjjsson",url);
                        try {
                            JSONObject jsonObject=new JSONObject(url.toString());
                            channelName=jsonObject.optString("channel_name");
                            hostFullName = jsonObject.optString("host_name");
                            hostImage = jsonObject.optString("host_image");
                            privateUID=jsonObject.optString("privateUID");
                            privateUserName=jsonObject.optString("privateUserName");
                            privateSessionDate=jsonObject.optString("privateSessionDate");
                            privateSessionTime=jsonObject.optString("privateSessionTime");



                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    initView();
                                    initRtcEngine();
                                    initRtmClient();

                                }
                            }, 2000);



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

    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        Log.i("stream from:",uid+"");
    }

    @Override
    public void onMessageReceived(boolean isChannelMsg, String uid, String message) {

        Log.i("messsss",uid+"__"+message);
            messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(uid),message+"",getTimeStamp()+"","",true));
            if (message.contains("Call-Ended at")){
                activity.runOnUiThread(()->{
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


        messageContainer=new OneToOneMessageContainer(chatRecyclerView);

        findViewById(R.id.live_msg_send_btn).setOnClickListener(v -> doSendMsg());



        audio_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID),"Voice-call at "+getTimeStamp(),getTimeStamp()+"","",true));
                audio_call.setVisibility(View.GONE);
                video_call.setVisibility(View.GONE);
                audio_or_video_background.setVisibility(View.VISIBLE);
                host_image_audio_call.setVisibility(View.VISIBLE);
                live_surfaceview.setVisibility(View.GONE);

                live_msg_et.setEnabled(false);

                mRtcEngine.enableLocalAudio(true);//enable audio

                mRtcEngine.setAudioProfile(Constants.AUDIO_ROUTE_EARPIECE,Constants.AUDIO_SCENARIO_EDUCATION);


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

                messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID),"Video-call at "+getTimeStamp(),getTimeStamp()+"","",true));
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

                sendMsg("Call-Ended at "+getTimeStamp());

                audio_call.setVisibility(View.VISIBLE);
                video_call.setVisibility(View.VISIBLE);
                audio_or_video_background.setVisibility(View.GONE);
                mRtcEngine.enableLocalAudio(false);// disable audio

                live_msg_et.setEnabled(true);

                mRtcEngine.setupRemoteVideo(null);

                live_no_surfaceview_notice.setVisibility(View.GONE);


                sendToastMessage("Call ended");
            }
        });


    }

    private void initRtcEngine() {
        Log.e("#inside engine","ewkjbewjkfb");
        mRtcEngine = RtcEngineManager.getInstance().getRtcEngine();
        RtcEngineManager.getInstance().setOnRtcEventCallback(this);

        mRtcEngine.enableLocalAudio(false);//during messaging disable audio




        Log.e("#cmm engine","ewkjbewjkfb" +channelName);
        mRtcEngine.joinChannel("", channelName, "", Integer.parseInt(privateUID));
        //mRtcEngine.joinChannel("", channelName, "", ANCHOR_UID);

        //mRtcEngine.setDefaultAudioRoutetoSpeakerphone(false);

    }

    private void initRtmClient() {

        mRtmClient = RtmClientManager.getInstance().getRtmClient();
        mRtmClient.login("", privateUID+"", new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "rtmClient login success"+"___uidd:"+channelName);
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
            mRtmChannel = mRtmClient.createChannel("surabhi@careerguide.com_privatesession_Friday_19_Jun_20_09_45AM"/*channelName*/, mRtmEventListener);
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
        if(!mIsMsgChannelEnable){
            showToast("Trying again,give us a moment");
            //checkChannelEnable();
            return;
        }


        String msg = live_msg_et.getText().toString();
        Log.i("mmeessaageee",msg);
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

                try{
                    mRtmChannel.sendMessage(rtmMessage,new ResultCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e(TAG, "sendMessage onSuccess");
                            runOnUiThread(()->{
                                //hideSoftKeyboard(live_msg_et);
                                //showToast("Message sent");
                                if (!TextUtils.isEmpty(msg)) {
                                    Log.i("mmeessaage",msg);

                                    messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID),msg+"",getTimeStamp()+"","",true));
                                }
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {
                            runOnUiThread(()-> {
                                messageContainer.addMessage(new OneToOneChatModel(Integer.parseInt(privateUID),msg+"",getTimeStamp()+"","",false));
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


    private void setClientRoleAudience(){
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
    }

    private void setClientRoleBroadcaster(){
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
    }




    @Override
    public void onBackPressed() {

        alertDialog = new AlertDialog.Builder(activity).create();
        final View dialog = getLayoutInflater().inflate(R.layout.dialog_log_out,null);//using the same logout dialog
        ((TextView)dialog.findViewById(R.id.textViewLogoutTitle)).setText("Do you want to leave the private session?");

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
        if(alertDialog!=null){
            alertDialog.dismiss();
            alertDialog=null;
        }

    }

    String getTimeStamp(){
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        minute = minute.length() == 1 ? "0" + minute : minute;
        int hour = calendar.get(Calendar.HOUR);
        return (hour == 0 ? 12 : hour) + ":" + minute + " " + (calendar.get(Calendar.AM_PM)==0?"AM":"PM");
    }

    void sendToastMessage(String toastMessage ){
        Toast.makeText(activity, toastMessage, Toast.LENGTH_SHORT).show();
    }



}