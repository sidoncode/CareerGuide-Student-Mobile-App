package com.careerguide;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.os.Environment;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.careerguide.exoplayer.utils.PublicFunctions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

public class ViewerLiveActivity extends BaseLiveActivity {
    private TextView tvNoSurfaceNotice;
    private int unique_id;
    private String UID="";
    private String title="";
    private String Fullname="";
    private String Channel_name="";
    String host_image ="";
    String scheduledesc="";
    String fileName ="";


    Activity activity = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (PublicFunctions.checkAccessStoragePermission ( this )) {
        }

        RtcEngineManager.getInstance().init(this);
        RtmClientManager.getInstance().init(this);
        UID  = Utility.getUserId(activity);
        unique_id = Integer.parseInt(UID);

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
                            Channel_name=jsonObject.optString("channel_name");
                            Fullname = jsonObject.optString("host_name");
                            host_image = "https://app.careerguide.com/api/user_dir/"+jsonObject.optString("host_image");
                            scheduledesc = jsonObject.optString("schedule_desc");
                            fileName=jsonObject.optString("host_image");
                            title=jsonObject.optString("title");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else{


                        Channel_name = getIntent().getStringExtra("Channel_name");

                        Fullname = getIntent().getStringExtra("name");

                        title=getIntent().getStringExtra("title");

                        host_image= getIntent().getStringExtra("imgurl");

                        scheduledesc= "\nGuide "+Fullname+" will be "+getIntent().getStringExtra("scheduledesc")+"\n Let me recommend this LIVE STREAM from CareerGuide- Must watch for you.\n Share with your friends and family too.  ";

                        fileName = host_image.substring(host_image.lastIndexOf('/') + 1);

                        //if (!Utility.ImageStorage.checkifImageExists(fileName))
                        {
                            new GetImages(host_image + "", fileName + "").execute();
                        }

                        }
                })
                .addOnFailureListener(this, e -> Log.e("dynamic links--> ", "getDynamicLink:onFailure", e));




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
        findViewById(R.id.shareWithOthers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                video_sharee();

            }
        });
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

    public void video_sharee() {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://play.google.com/store/apps/details?id=com.careerguide&hl=en_US&sessionDetails={\"channel_name\":\""+Channel_name+"\",\"host_name\":\""+Fullname+"\",\"host_image\":\""+fileName+"\",\"schedule_desc\":\""+scheduledesc+"\",\"title\":\""+title+"\"}"))
                .setDynamicLinkDomain("counsellor.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.careerguide").build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("video")
                                .setMedium("anyone")
                                .setCampaign("example-video")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(title+" by Guide "+Fullname+" from CareerGuide.com")
                                .setDescription(scheduledesc)
                                .setImageUrl(Uri.parse(host_image))
                                .build())
                .buildDynamicLink();
        Log.e("main", "Long refer Link"+ dynamicLink.getUri());
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLink.getUri())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();
                        Log.e("main","short Link" + shortLink);
                        Log.e("main","short Link" + flowchartLink);
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        File imgFile = Utility.ImageStorage.getImage(fileName);

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgFile.toString()) );
                        shareIntent.putExtra(Intent.EXTRA_TEXT, scheduledesc+ shortLink );
                        startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                    } else
                    {
                        Log.e("Error","error--> "+task.getException());
                        // Error
                        // ...
                    }
                });
    }

    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private Bitmap bitmap ;
        private FileOutputStream fos;


        private GetImages(String requestUrl, String _imagename_) {
            this.requestUrl = requestUrl;
            this.imagename_ = _imagename_ ;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                /*URL url = new URL(requestUrl);
                Log.i("uuuuu",url+"");
                URLConnection conn = url.openConnection();
                this.bitmap = BitmapFactory.decodeStream(conn.getInputStream());*/

                DownloadManager downloadmanager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(requestUrl);

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("ssss");
                request.setDescription("ssssss");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setVisibleInDownloadsUi(true);
                File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+imagename_);
                request.setDestinationUri(Uri.fromFile(file));
                return (downloadmanager.enqueue(request));


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            //if(!Utility.ImageStorage.checkifImageExists(imagename_))
            {

                //Utility.ImageStorage.saveToSdCard(bitmap, imagename_);
            }
        }
    }


}
