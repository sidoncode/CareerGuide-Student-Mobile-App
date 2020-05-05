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
    private String hostPicUrl;

    private AndExoPlayerView andExoPlayerView;
   // private String TEST_URL_MP3 = "https://host2.rj-mw1.com/media/podcast/mp3-192/Tehranto-41.mp3";
    private int req_code = 129;
    private String videoId="";
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        if (PublicFunctions.checkAccessStoragePermission ( this )) {
        }

        setContentView ( R.layout.video_player );
        andExoPlayerView = findViewById ( R.id.andExoPlayerView );
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        load_url( deepLink.toString());
                        Log.e("deeplink --> " , "" +deepLink);
                    }
                    else{
                        videoId=getIntent().getStringExtra("id");
                        String img_url= getIntent().getStringExtra("imgurl");
                        String name= getIntent().getStringExtra("Fullname");
                        String title= getIntent().getStringExtra("title");
                        hostEmail = getIntent().getStringExtra("host_email");
                        String url = getIntent().getStringExtra("live_video_url");
                        hostPicUrl = getIntent().getStringExtra("host_img");

                        Log.d("VIDEO_PLAYER","img url: " +img_url);
                        Log.d("VIDEO_PLAYER", "name: " +name);
                        Log.d("VIDEO_PLAYER", "email: " +hostEmail);
                        Log.d("VIDEO_PLAYER","title--> "+ title);
                        Log.d("VIDEO_PLAYER","#views video-->" +getIntent().getStringExtra("video_views"));
                        Log.d("VIDEO_PLAYER","host img url: " +hostPicUrl);
                        Log.d("VIDEO_PLAYER","live video url: " +url);


                        andExoPlayerView.setName(name);
                        andExoPlayerView.sethost_email(hostEmail);

                        if(getIntent().getStringExtra("video_views")!=null)//if value is not null set updated value
                            andExoPlayerView.setVideoViews(getIntent().getStringExtra("video_views"));

                        new TaskUpdateViewCounter().execute();

                        if(hostPicUrl!=null && hostPicUrl.length()>0)
                            andExoPlayerView.setImg(hostPicUrl);
                        else
                        fetchAndApplyImage();

                        new DownloadFile( ).execute (img_url, title);
                        load_url(url);
                    }
                })
                .addOnFailureListener(this, e -> Log.e("dynamic links--> ", "getDynamicLink:onFailure", e));

    }



    private void load_url(String s_url) {
        Log.e ( "url1", "-->" + s_url );
        if (s_url.contains ( "mp4" ))
            loadMP4ServerSide (s_url);
        else {
            loadHls (s_url );
        }
    }
//    private void loadMp3() {
//        andExoPlayerView.setSource ( TEST_URL_MP3 );
//    }
    private void loadHls(String url) {
        if (url != null) {
            andExoPlayerView.setSource ( url );
        }
    }
    public void loadMP4ServerSide(String url) {
        if (url != null) {
            andExoPlayerView.setSource ( url );
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
        andExoPlayerView.setSource ( filePath );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == req_code && resultCode == RESULT_OK) {
            Uri finalVideoUri = data.getData ( );
            String filePath = null;
            try {
                filePath = PathUtil.getPath ( this, finalVideoUri );
                loadMP4Locale ( filePath );
            } catch (URISyntaxException e) {
                e.printStackTrace ( );
                Toast.makeText ( this, "Failed: " + e.getMessage ( ), Toast.LENGTH_SHORT ).show ( );
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId ( ) == android.R.id.home) {
            finish ( );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }
    public static Bitmap retriveVideoFrameFromVideo(String url)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(url, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(url);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(-1,MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String url)"
                            + e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
    public void video_share(View view) {
        String img_url = getIntent().getStringExtra("imgurl");
        Log.e("img", ""+hostPicUrl);
        String name= getIntent().getStringExtra("Fullname");
        Log.e("name",name);
        String title= getIntent().getStringExtra("title");
        Log.e("title","--> "+ title);
        String fileName = img_url.substring(img_url.lastIndexOf('/') + 1);
        Log.e("fileName",fileName);
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(getIntent().getStringExtra("live_video_url")))
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
                                .setTitle(name)
                                .setDescription(title)
                                .setImageUrl(Uri.parse(getIntent().getStringExtra("imgurl")))
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
                        File imgFile = new File ( Environment.getExternalStorageDirectory ( ).getAbsolutePath() + "/com.careerguide/" + fileName );
                        Uri path = Uri.fromFile ( imgFile );
                        Log.e("#path",":" + path);
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        //shareIntent.setAction(Intent.ACTION_SEND); // temp permission for receiving app to read this file
                        shareIntent.setType ( "image/*" );
                        //shareIntent.setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        //shareIntent.addFlags ( Intent.FLAG_GRANT_READ_URI_PERMISSION );
                        String shareMessage = "\nLet me recommend this video from CareerGuide- Must watch for you\n" + title +" by Guide "+name+"\n";
                        shareMessage = shareMessage + shortLink ;
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgFile.toString()) );
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage );
                        startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                    } else
                    {
                        Log.e("Error","error--> "+task.getException());
                        // Error
                        // ...
                    }
                });
    }
    public class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String img_url = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf

            Log.e("img_url","" +img_url);
            String fileName = img_url.substring(img_url.lastIndexOf('/') + 1);
            Log.e("fileName","" +fileName);
            String title = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory ( ).toString ();
            Log.e("storage",extStorageDirectory);
           // File folder = new File ( extStorageDirectory, "/WhatsApp Business/Media/WhatsApp Business Images/Sent" );
            File folder = new File ( extStorageDirectory, "/com.careerguide" );

            folder.mkdir ( );
            File imgFile = new File ( folder, fileName );

            try
            {
                Log.e("File","Downloaded");
                imgFile.createNewFile ( );
            } catch (IOException e)
            {
                e.printStackTrace ( );
            }
            FileDownloader.downloadFile ( img_url, imgFile);
            return null;
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

    private void fetchAndApplyImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/counsellor/fetch_counsellor_detail",
                response -> {
                    Log.d("#HOSTEMAIL Response",response);
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        JSONArray jsonArray = responseObject.getJSONArray("counsellors");
                        JSONObject counsellor = (JSONObject) jsonArray.get(0);
                        hostPicUrl = "https://app.careerguide.com/api/user_dir/"+ counsellor.get("profile_pic");
                        Log.d("#HOSTPIC :", hostPicUrl);
                        andExoPlayerView.setImg(hostPicUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("host_pic__error","error"))
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("email" ,hostEmail);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private class TaskUpdateVideoView extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {

                String update_url = "";
                //Volley request
            }catch(Exception e1)
            {
                e1.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class TaskUpdateViewCounter extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/UpdateViews",
                    response -> {
                        Log.i("Updated_video_counter",response);
                    },
                    error -> Log.e("error","error"))
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("video_id" ,videoId);
                    return params;
                }
            };
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

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
