package com.careerguide.youtubeVideo;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.careerguide.youtubePlayer.DeveloperKey;
import com.careerguide.youtubePlayer.YouTubeFailureRecoveryActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class youtubeFeedDetail extends YouTubeFailureRecoveryActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    Activity activity = this;
    private String browserKey = Utility.browserKey;
    TextView count , desc;
    ImageView downimage;
    private String titleForAnalytics="";
    int flag =0;

    List<Videos> displaylistArray = new ArrayList<>();
    Videos displaylist ;
    private YT_player_recycler_adapter mVideoAdapter;
    LinearLayoutManager mLayoutManager;
    int cornerRadius = 5;
    int videoTxtColor = Color.parseColor("#000000");

    private boolean isFullScreen = false;
    private YouTubePlayer youTubePlayer;
    List<String> videoList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_feed_detail);
        Log.e("dataid","-->" +getIntent().getStringExtra("data_id"));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        YouTubePlayerView youTubeView = findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
        count = findViewById(R.id.count);
        downimage = findViewById(R.id.downimage);
         View arrowDown = findViewById(R.id.arrowDown);
      //  thumbnail = findViewById(R.id.thumbnail);
        desc = findViewById(R.id.desc);
        Typeface font = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        Typeface font_small = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-Regular.ttf");
        count.setTypeface(font);
        desc.setTypeface(font_small);
        arrowDown.setOnClickListener(v->{
            if (desc.getVisibility() == View.VISIBLE)
            {
                desc.setVisibility(View.GONE);
                downimage.setImageResource(R.drawable.arrow_down);
            }
            else
            {
                desc.setVisibility(View.VISIBLE);
                downimage.setImageResource(R.drawable.arrow_up);
            }
        });

        RecyclerView mVideoRecyclerView = findViewById(R.id.rcv_topics);
        mLayoutManager = new LinearLayoutManager(this);
        mVideoRecyclerView.setLayoutManager(mLayoutManager);
        mVideoAdapter = new YT_player_recycler_adapter(displaylistArray, browserKey, this, cornerRadius, videoTxtColor);
        mVideoRecyclerView.setAdapter(mVideoAdapter);

//        thumbnail.setOnClickListener(v->{
//            Intent videoIntent = YouTubeStandalonePlayer.createVideoIntent(activity, browserKey,getIntent().getStringExtra("data_id") , 0, true, false);
//            activity.startActivityForResult(videoIntent, 1);
//        });
        if(flag ==0)
            fetchContent();
      //  new TheTask().execute();
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            Log.e("dataId","-->" +getIntent().getStringExtra("data_id"));
          //   player.cueVideo(getIntent().getStringExtra("data_id"));
          player.loadVideo(getIntent().getStringExtra("data_id"));
          youTubePlayer = player;
          player.setOnFullscreenListener(b -> {
              isFullScreen = b;
          });

          player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
              @Override
              public void onLoading() {

              }

              @Override
              public void onLoaded(String s) {

              }

              @Override
              public void onAdStarted() {

              }

              @Override
              public void onVideoStarted() {

                  //Logic to convert title to valid variable for proper analytics logging
                  String cutString=titleForAnalytics;
                  if(titleForAnalytics.length()>20)
                      cutString = titleForAnalytics.substring(0, 20);
                  StringBuilder sb = new StringBuilder();
                  if(!Character.isJavaIdentifierStart(cutString.charAt(0))) {
                      sb.append("_");
                  }
                  for (char c : cutString.toCharArray()) {
                      if(!Character.isJavaIdentifierPart(c)) {
                          sb.append("_");
                      } else {
                          sb.append(c);
                      }
                  }

                  Bundle bundle = new Bundle();
                  bundle.putBoolean(sb.toString(),true);
                  mFirebaseAnalytics.logEvent("youtube_video_watched_from_app",bundle);


              }

              @Override
              public void onVideoEnded() {

                      if(videoList!=null && videoList.size()>0)
                      {
                          player.loadVideos(videoList);
                          videoList.remove(0);
                      }
              }

              @Override
              public void onError(YouTubePlayer.ErrorReason errorReason) {

              }
          });
        }
    }



    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }



    private void fetchContent(){
        String url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id="+ getIntent().getStringExtra("data_id") +"&key=" + browserKey + "&maxResults=50";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject json = new JSONObject(response);
                JSONArray jsonArray = json.getJSONArray("items");
                    for (int i = 0;  i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getJSONObject("snippet").getString("title");
                        titleForAnalytics = title;
                        String Desc = jsonObject.getJSONObject("snippet").getString("description");
                        count.setText(title);
                        desc.setText(Desc);
                    }
                flag=1;
                fetchAllVideo();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        , error -> {
            Toast.makeText(activity, VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        });

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    private void fetchAllVideo() {
        String url = "https://www.googleapis.com/youtube/v3/search?key="+Utility.browserKey+"&channelId=UCs6EVBxMpm9S3a2RpbAIp1w&part=snippet,id&order=date&maxResults=30";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject json = new JSONObject(response);
                JSONArray jsonArray = json.getJSONArray("items");
                for (int i = 0;  i<jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.e("inside","-->" +jsonObject);
                    String title = jsonObject.getJSONObject("snippet").getString("title");
                    String id = jsonObject.getJSONObject("id").getString("videoId");
                    String Desc = jsonObject.getJSONObject("snippet").getString("description");
                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    displaylist = new Videos(title, thumbUrl ,id , Desc);
                    displaylistArray.add(displaylist);
                }
                mVideoAdapter.notifyDataSetChanged();
                addVideosInVideoList();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } , error -> {
            Toast.makeText(activity, VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        });

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        if (youTubePlayer != null && isFullScreen){
            youTubePlayer.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }


    void addVideosInVideoList()
    {
        if(displaylistArray!=null && displaylistArray.size()>0) {
            for (Videos video : displaylistArray) {
                videoList.add(video.getVideoID());
            }
        }
    }

}
