package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadata;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.careerguide.exoplayer.AndExoPlayerView;
import com.careerguide.exoplayer.globalEnums.EnumAspectRatio;
import com.careerguide.exoplayer.globalInterfaces.ExoPlayerCallBack;
import com.careerguide.exoplayer.utils.PathUtil;
import com.careerguide.exoplayer.utils.PublicFunctions;
import java.net.URISyntaxException;
import java.util.List;


public class Video_player extends AppCompatActivity {
    private AndExoPlayerView andExoPlayerView;

    private String TEST_URL_MP4 ="https://facebook-live-counsellors.s3.ap-south-1.amazonaws.com/surabhi-Dewra-live.mp4";

    private String TEST_URL_HLS = "https://content.jwplatform.com/manifests/yp34SRmf.m3u8";

    private String TEST_URL_MP3 = "https://host2.rj-mw1.com/media/podcast/mp3-192/Tehranto-41.mp3";

    private int req_code = 129;

    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);

        andExoPlayerView = findViewById(R.id.andExoPlayerView);

        String url = getIntent().getStringExtra("live_video_url");
        andExoPlayerView.setName(getIntent().getStringExtra("Fullname"));
        andExoPlayerView.setImg(getIntent().getStringExtra("imgurl"));
        andExoPlayerView.sethost_email(getIntent().getStringExtra("host_email"));

        Uri uri = getIntent().getData();
        if(uri != null){
            List<String> param  = uri.getPathSegments();
            String vurl = param.get(param.size()-1);
            Toast.makeText(activity,vurl,Toast.LENGTH_LONG).show();
        }

        Log.e("url" , "-->"+url);
        if(url.contains("mp4"))
        loadMP4ServerSide();
        else{
            loadHls(url);
        }
    }

    private void loadMp3() {
        andExoPlayerView.setSource(TEST_URL_MP3);
    }

    private void loadHls(String url) {
        andExoPlayerView.setSource(url);
    }

    public void loadMP4ServerSide() {
        andExoPlayerView.setSource(getIntent().getStringExtra("live_video_url"));
    }

    private void selectLocaleVideo() {
        if (PublicFunctions.checkAccessStoragePermission(this)) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), req_code);
        }
    }

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
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}
