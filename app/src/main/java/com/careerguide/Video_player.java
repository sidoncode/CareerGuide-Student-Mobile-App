package com.careerguide;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.exoplayer.AndExoPlayerView;
import com.careerguide.exoplayer.utils.PathUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import com.careerguide.exoplayer.utils.PublicFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video_player extends AppCompatActivity {

    private String hostEmail;

    private FirebaseAnalytics mFirebaseAnalytics;
    private AndExoPlayerView andExoPlayerView;
    // private String TEST_URL_MP3 = "https://host2.rj-mw1.com/media/podcast/mp3-192/Tehranto-41.mp3";
    private int req_code = 129;
    private String videoId = "";
    Context context = this;

    private String title = "";
    private String Fullname = "";
    private String video_url = "";
    String host_image = "";
    String scheduledesc = "";
    String fileName = "";
    String bannerImage = "";
    String bannerImageFilename="";
    String video_views = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_player);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        load_url(deepLink.toString());
                        Log.e("deeplink --> ", "" + deepLink);

                        String url = deepLink + "";
                        url = url.substring(url.lastIndexOf("?") + 16);
                        url = url.replaceFirst("%7B", "{");
                        url = url.replaceFirst("%7D", "}");
                        url = url.replace("%0A", "");
                        url = url.replace("%22", "\"");
                        Log.i("jjjsson", url);
                        try {
                            JSONObject jsonObject = new JSONObject(url.toString());

                            videoId = jsonObject.optString("video_id");
                            Fullname = jsonObject.optString("Fullname");
                            title = jsonObject.optString("title");
                            hostEmail = jsonObject.optString("host_email");
                            video_url = jsonObject.optString("live_video_url");
                            video_url=video_url.replace(" ","+");
                            video_views = jsonObject.optString("video_views");
                            fileName = jsonObject.optString("host_image");
                            bannerImage=jsonObject.optString("bannerImage");
                            bannerImageFilename=bannerImage.substring(bannerImage.lastIndexOf("/")+1);
                            host_image = "https://app.careerguide.com/api/user_dir/" + fileName;
                            new TaskUpdateViewCounter(videoId).execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        videoId = getIntent().getStringExtra("video_id");
                        host_image = getIntent().getStringExtra("host_img");
                        Fullname = getIntent().getStringExtra("Fullname");
                        title = getIntent().getStringExtra("title");
                        hostEmail = getIntent().getStringExtra("host_email");
                        video_url = getIntent().getStringExtra("live_video_url");
                        video_views = getIntent().getStringExtra("video_views");
                        fileName = host_image.substring(host_image.lastIndexOf("/")+1);
                        bannerImage=getIntent().getStringExtra("imgurl");
                        bannerImageFilename=bannerImage.substring(bannerImage.lastIndexOf("/")+1);
                        new TaskUpdateViewCounter(videoId).execute();

                    }

                    Log.i("asasas",videoId+"_"+host_image+"_"+Fullname+"_"+title+"_"+hostEmail+"_"+video_url+"_"+video_views+"_"+fileName);

                    andExoPlayerView.setName(Fullname);
                    andExoPlayerView.sethost_email(hostEmail);

                    if (video_views!= null)//if value is not null set updated value
                    {
                        if (video_views.contains("null"))
                            andExoPlayerView.setVideoViews("1");
                        else
                            andExoPlayerView.setVideoViews(video_views);


                    }


                    logEventForFirebase("v_" + title.trim(), Fullname.trim());

                    if (host_image != null && host_image.length() > 0)
                        andExoPlayerView.setImg(host_image);


                    load_url(video_url);

                })
                .addOnFailureListener(this, e -> Log.e("dynamic links--> ", "getDynamicLink:onFailure", e));


    }


    private void logEventForFirebase(String title, String name) {


        //Logic to convert title into valid variable for proper analytics logging
        String cutString = title;
        if (title.length() > 20)
            cutString = title.substring(0, 20);
        StringBuilder sb = new StringBuilder();
        if (!Character.isJavaIdentifierStart(cutString.charAt(0))) {
            sb.append("_");
        }
        for (char c : cutString.toCharArray()) {
            if (!Character.isJavaIdentifierPart(c)) {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putBoolean(sb.toString(), true);
        bundle.putString("by", name);
        mFirebaseAnalytics.logEvent("live_video_watched_from_app", bundle);

    }


    private void load_url(String s_url) {
        Log.e("url1", "-->" + s_url);
        if (s_url.contains("mp4"))
            loadMP4ServerSide(s_url);
        else {
            loadHls(s_url);
        }
    }

    //    private void loadMp3() {
//        andExoPlayerView.setSource ( TEST_URL_MP3 );
//    }
    private void loadHls(String url) {
        if (url != null) {
            andExoPlayerView.setSource(url);
        }
    }

    public void loadMP4ServerSide(String url) {
        if (url != null) {
            andExoPlayerView.setSource(url);
        }
    }

    //    private void selectLocaleVideo() {
//        if (PublicFunctions.checkAccessStoragePermission ( this )) {
//            Intent intent = new Intent ( );
//            intent.setType ( "video/*" );
//            intent.putExtra ( Intent.EXTRA_LOCAL_ONLY, true );
//            intent.setAction ( Intent.ACTION_GET_CONTENT );
//            startActivityForResult ( Intent.createChooser ( intent, "Select Video" ), req_code );
//        }
//    }
    private void loadMP4Locale(String filePath) {
        andExoPlayerView.setSource(filePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == req_code && resultCode == RESULT_OK) {
            Uri finalVideoUri = data.getData();
            String filePath = null;
            try {
                filePath = PathUtil.getPath(this, finalVideoUri);
                loadMP4Locale(filePath);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap retriveVideoFrameFromVideo(String url)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(url, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(url);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(-1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String url)"
                            + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public void video_share(View view) {

        if (PublicFunctions.checkAccessStoragePermission(this)) {
            if (!Utility.checkFileExist(bannerImageFilename)) {
                Utility.downloadImage(bannerImageFilename + "", host_image + "", this);
            }
            String shareMessagee = "Let me recommend this video from CareerGuide.com- Must watch for you " + title + " by Guide " + Fullname + "\n";

            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(video_url+"?sessionDetails={\"videoId\":\""+videoId+"\",\"Fullname\":\""+Fullname+"\",\"title\":\""+title+"\",\"hostEmail\":\""+hostEmail+"\",\"video_url\":\""+video_url+"\",\"video_views\":\""+video_views+"\",\"fileName\":\""+fileName+"\",\"bannerImage\":\""+bannerImage+"\"}"))
                    .setDynamicLinkDomain("careerguidesharevideo.page.link")
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
                                    .setTitle("Guide " + Fullname + " \n from CareerGuide.com")
                                    .setDescription(shareMessagee)
                                    .setImageUrl(Uri.parse(host_image))
                                    .build())
                    .buildDynamicLink();
            Log.e("main", "Long refer Link" + dynamicLink.getUri());
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(dynamicLink.getUri())
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("main", "short Link" + shortLink);
                            Log.e("main", "short Link" + flowchartLink);
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                            File imgFile = Utility.getFile(bannerImageFilename);
                            Uri path = Uri.fromFile(imgFile);
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            //shareIntent.setAction(Intent.ACTION_SEND); // temp permission for receiving app to read this file
                            shareIntent.setType("image/*");
                            //shareIntent.setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            //shareIntent.addFlags ( Intent.FLAG_GRANT_READ_URI_PERMISSION );
                            String shareMessage = "Let me recommend this video from CareerGuide.com- Must watch for you " + title + " by Guide " + Fullname + "\n";
                            shareMessage = shareMessage + shortLink;
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgFile.toString()));
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                        } else {
                            Log.e("Error", "error--> " + task.getException());
                            // Error
                            // ...
                        }
                    });
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        andExoPlayerView.pausePlayer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        andExoPlayerView.setPlayWhenReady(true);
    }

    class TaskUpdateViewCounter extends AsyncTask<Void, Void, Void> {

        String videoid="";

        TaskUpdateViewCounter(String videoIdd){
            this.videoid=videoIdd;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i("video__id", videoid);
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    "https://app.careerguide.com/api/main/UpdateViews",
                    response -> {
                        Log.i("Updated_video_counter", response);
                    },
                    error -> Log.e("error", error.getMessage())) {
                @Override
                public Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("video_id", videoid);
                    return params;
                }
            };


            VolleySingleton.getInstance(context).addToRequestQueue(request);

            return null;
        }
    }
}









//package com.careerguide;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//import com.careerguide.exoplayer.AndExoPlayerView;
//import com.careerguide.exoplayer.utils.PathUtil;
//import com.careerguide.exoplayer.utils.PublicFunctions;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.dynamiclinks.DynamicLink;
//import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
//import com.google.firebase.dynamiclinks.ShortDynamicLink;
//import java.net.URISyntaxException;
//
//
//public class Video_player extends AppCompatActivity {
//    private AndExoPlayerView andExoPlayerView;
//    private String TEST_URL_MP3 = "https://host2.rj-mw1.com/media/podcast/mp3-192/Tehranto-41.mp3";
//
//    private int req_code = 129;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate ( savedInstanceState );
//
//        setContentView ( R.layout.video_player );
//
//        andExoPlayerView = findViewById ( R.id.andExoPlayerView );
//
//
//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(getIntent())
//                .addOnSuccessListener(this, pendingDynamicLinkData -> {
//                    // Get deep link from result (may be null if no link is found)
//                    Uri deepLink = null;
//                    if (pendingDynamicLinkData != null) {
//                        deepLink = pendingDynamicLinkData.getLink();
//                        load_url( deepLink.toString());
//                        Log.e("deeplink --> " , "" +deepLink);
//                    }
//                    else
//                        load_url( getIntent().getStringExtra("live_video_url"));
//                })
//                .addOnFailureListener(this, e -> Log.e("dynamic links--> ", "getDynamicLink:onFailure", e));
//    }
//
//
//    private void load_url(String s_url) {
//        Log.e ( "url1", "-->" + s_url );
//        if (s_url.contains ( "mp4" ))
//            loadMP4ServerSide (s_url);
//        else {
//            loadHls (s_url );
//        }
//    }
//
//    private void loadMp3() {
//        andExoPlayerView.setSource ( TEST_URL_MP3 );
//    }
//
//    private void loadHls(String url) {
//        if (url != null) {
//            andExoPlayerView.setSource ( url );
//        }
//    }
//
//
//    public void loadMP4ServerSide(String url) {
//        if (url != null) {
//            andExoPlayerView.setSource ( url );
//        }
//
//    }
//
//    private void selectLocaleVideo() {
//        if (PublicFunctions.checkAccessStoragePermission ( this )) {
//            Intent intent = new Intent ( );
//            intent.setType ( "video/*" );
//            intent.putExtra ( Intent.EXTRA_LOCAL_ONLY, true );
//            intent.setAction ( Intent.ACTION_GET_CONTENT );
//            startActivityForResult ( Intent.createChooser ( intent, "Select Video" ), req_code );
//        }
//    }
//
//    private void loadMP4Locale(String filePath) {
//        andExoPlayerView.setSource ( filePath );
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult ( requestCode, resultCode, data );
//        if (requestCode == req_code && resultCode == RESULT_OK) {
//            Uri finalVideoUri = data.getData ( );
//            String filePath = null;
//            try {
//                filePath = PathUtil.getPath ( this, finalVideoUri );
//                loadMP4Locale ( filePath );
//            } catch (URISyntaxException e) {
//                e.printStackTrace ( );
//                Toast.makeText ( this, "Failed: " + e.getMessage ( ), Toast.LENGTH_SHORT ).show ( );
//            }
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId ( ) == android.R.id.home) {
//            finish ( );
//            return true;
//        }
//        return super.onOptionsItemSelected ( item );
//    }
//
//    public void video_share(View view) {
//
//        String img_url= getIntent().getStringExtra("imgurl");
//        Log.e("img",img_url);
//
//        String name= getIntent().getStringExtra("Fullname");
//        Log.e("name",name);
//        String title= getIntent().getStringExtra("title");
//        Log.e("title","--> "+ title);
//        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse(getIntent().getStringExtra("live_video_url")))
//                .setDynamicLinkDomain("counsellor.page.link")
//                // Open links with this app on Android
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.careerguide").build())
//                .setGoogleAnalyticsParameters(
//                        new DynamicLink.GoogleAnalyticsParameters.Builder()
//                                .setSource("video")
//                                .setMedium("anyone")
//                                .setCampaign("example-video")
//                                .build())
//                .setSocialMetaTagParameters(
//                        new DynamicLink.SocialMetaTagParameters.Builder()
//                                .setTitle(name)
//                                .setDescription(title)
//                                .setImageUrl(Uri.parse(getIntent().getStringExtra("imgurl")))
//                                .build())
//                .buildDynamicLink();
//
//        Uri dynamicLinkUri = dynamicLink.getUri();
//        Log.e("main", "Long refer Link"+ dynamicLink.getUri());
//
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLongLink(dynamicLink.getUri())
//                .buildShortDynamicLink()
//
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Short link created
//                        Uri shortLink = task.getResult().getShortLink();
//                        Uri flowchartLink = task.getResult().getPreviewLink();
//                        Log.e("main","short Link" + shortLink);
//                        Log.e("main","short Link" + flowchartLink);
//                        Intent shareIntent = new Intent();
//                        shareIntent.setAction(Intent.ACTION_SEND); // temp permission for receiving app to read this file
//                        shareIntent.setType("text/plain");
//                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        String shareMessage = "\nLet me recommend this video from CareerGuide - Must watch for you\n\n";
//                        shareMessage = shareMessage + shortLink;
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                        startActivity(Intent.createChooser(shareIntent, "Choose an app"));
//
//                    } else {
//
//                        Log.e("Error","error--> "+task.getException());
//
//                        // Error
//                        // ...
//                    }
//                });
//    }
//}
//
//
//
//
////package com.careerguide;
////
////import android.app.Activity;
////import android.content.Intent;
////import android.content.res.Configuration;
////import android.media.MediaMetadata;
////import android.media.MediaMetadataRetriever;
////import android.net.Uri;
////import android.os.Bundle;
////import android.support.constraint.ConstraintLayout;
////import android.support.constraint.ConstraintSet;
////import android.support.constraint.Constraints;
////import android.support.v7.app.AppCompatActivity;
////import android.util.Log;
////import android.view.MenuItem;
////import android.widget.TextView;
////import android.widget.Toast;
////import com.careerguide.exoplayer.AndExoPlayerView;
////import com.careerguide.exoplayer.globalEnums.EnumAspectRatio;
////import com.careerguide.exoplayer.globalInterfaces.ExoPlayerCallBack;
////import com.careerguide.exoplayer.utils.PathUtil;
////import com.careerguide.exoplayer.utils.PublicFunctions;
////import java.net.URISyntaxException;
////import java.util.List;
////
////
////public class Video_player extends AppCompatActivity {
////    private AndExoPlayerView andExoPlayerView;
////
////    private String TEST_URL_MP4 ="https://facebook-live-counsellors.s3.ap-south-1.amazonaws.com/surabhi-Dewra-live.mp4";
////
////    private String TEST_URL_HLS = "https://content.jwplatform.com/manifests/yp34SRmf.m3u8";
////
////    private String TEST_URL_MP3 = "https://host2.rj-mw1.com/media/podcast/mp3-192/Tehranto-41.mp3";
////
////    private int req_code = 129;
////
////    Activity activity = this;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.video_player);
////
////        andExoPlayerView = findViewById(R.id.andExoPlayerView);
////
////        String url = getIntent().getStringExtra("live_video_url");
////        andExoPlayerView.setName(getIntent().getStringExtra("Fullname"));
////        andExoPlayerView.setImg(getIntent().getStringExtra("imgurl"));
////        andExoPlayerView.sethost_email(getIntent().getStringExtra("host_email"));
////
////        FirebaseDynamicLinks.getInstance()
////                .getDynamicLink(getIntent())
////                .addOnSuccessListener(this, pendingDynamicLinkData -> {
////                    // Get deep link from result (may be null if no link is found)
////                    Uri deepLink = null;
////                    if (pendingDynamicLinkData != null) {
////                        deepLink = pendingDynamicLinkData.getLink();
////                        load_url( deepLink.toString());
////                        Log.e("deeplink --> " , "" +deepLink);
////                    }
////                    else
////                        load_url( getIntent().getStringExtra("live_video_url"));
////                })
////                .addOnFailureListener(this, e -> Log.e("dynamic links--> ", "getDynamicLink:onFailure", e));
////
////        Log.e("url" , "-->"+url);
////        if(url.contains("mp4"))
////        loadMP4ServerSide();
////        else{
////            loadHls(url);
////        }
////    }
////
////    private void loadMp3() {
////        andExoPlayerView.setSource(TEST_URL_MP3);
////    }
////
////    private void loadHls(String url) {
////        andExoPlayerView.setSource(url);
////    }
////
////    public void loadMP4ServerSide() {
////        andExoPlayerView.setSource(getIntent().getStringExtra("live_video_url"));
////    }
////
////    private void selectLocaleVideo() {
////        if (PublicFunctions.checkAccessStoragePermission(this)) {
////            Intent intent = new Intent();
////            intent.setType("video/*");
////            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
////            intent.setAction(Intent.ACTION_GET_CONTENT);
////            startActivityForResult(Intent.createChooser(intent, "Select Video"), req_code);
////        }
////    }
////
////    private void loadMP4Locale(String filePath) {
////        andExoPlayerView.setSource(filePath);
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == req_code && resultCode == RESULT_OK) {
////            Uri finalVideoUri = data.getData();
////            String filePath = null;
////            try {
////                filePath = PathUtil.getPath(this, finalVideoUri);
////                loadMP4Locale(filePath);
////            } catch (URISyntaxException e) {
////                e.printStackTrace();
////                Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
////
////
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == android.R.id.home)
////        {
////            finish();
////            return true;
////        }
////        return super.onOptionsItemSelected(item);
////    }
////
////    @Override
////    protected void onPause() {
////        super.onPause();
////        andExoPlayerView.pausePlayer();
////
////    }
////
////    @Override
////    protected void onResume() {
////        super.onResume();
////        andExoPlayerView.setPlayWhenReady(true);
////    }
////
////}
