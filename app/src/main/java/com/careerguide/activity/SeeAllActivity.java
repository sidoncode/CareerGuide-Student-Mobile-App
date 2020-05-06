package com.careerguide.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VolleySingleton;
import com.careerguide.youtubeVideo.CommonEducationAdapter;
import com.careerguide.youtubeVideo.CommonEducationModel;
import com.careerguide.youtubeVideo.Videos;
import com.careerguide.youtubeVideo.YT_recycler_adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeeAllActivity extends AppCompatActivity {

    private List<CommonEducationModel> educationList;
    List<Videos> youtubeList;

    RecyclerView recyclerView;
    CommonEducationAdapter adapter;
    YT_recycler_adapter yt_recycler_adapter;

    private String browserKey = "AIzaSyC2VcqdBaKakTd7YLn4B9t3dxWat9UHze4";
    String URL_EDUCATION = "https://app.careerguide.com/api/main/";
    String playlistKEY="";
    String URL_YOUTUBE = "";
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        String title = getIntent().getStringExtra("TITLE");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

        recyclerView = findViewById(R.id.see_all_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        mode = getIntent().getIntExtra("mode",0);

        switch (mode)
        {
            case 0:
                educationList = new ArrayList<>();
                URL_EDUCATION+=getIntent().getStringExtra("EDU_KEY");
                adapter = new CommonEducationAdapter(educationList,this);
                adapter.setSeeAllMode(true);
                recyclerView.setAdapter(adapter);
                fetchEducationVideos();
                break;
            case 1:
                youtubeList = new ArrayList<>();
                playlistKEY = getIntent().getStringExtra("KEY");
                URL_YOUTUBE= "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+playlistKEY+"&key=" + browserKey + "&maxResults=50";
                yt_recycler_adapter = new YT_recycler_adapter(youtubeList,browserKey,this,0, Color.parseColor("#000000"));
                yt_recycler_adapter.setSeeAllMode(true);
                recyclerView.setAdapter(yt_recycler_adapter);
                //new TaskYoutube().execute();
                fetchYoutubeVideos();
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    private void fetchEducationVideos()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL_EDUCATION,
                response -> {

            try {
                JSONObject json = new JSONObject(response);
                JSONArray jsonArray = json.optJSONArray("videos");
                int jsonArrayLen=jsonArray.length();


                for (int i = 0; i < jsonArrayLen ; i++) {

                    JSONObject JsonObject = jsonArray.optJSONObject(i);
                    String user_id = JsonObject.optString("user_id");
                    String email = JsonObject.optString("email");
                    String name = JsonObject.optString("Name");
                    String img_url = JsonObject.optString("img_url");
                    String title = JsonObject.optString("title");
                    String video_url = JsonObject.optString("video_url");
                    String video_views=JsonObject.optString("views");
                    String video_id = JsonObject.optString("id");
                    CommonEducationModel commonEducationModel = new CommonEducationModel(user_id,email, name, img_url, video_url, title, "",video_views,video_id);
                    educationList.add(commonEducationModel);
                }
                adapter.notifyDataSetChanged();

            }catch (Exception e)
            {
                e.printStackTrace();
            }

                },error -> {
            Log.d("#PLAYLIST", "error: "+ error.toString());

        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



    private void fetchYoutubeVideos()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL_YOUTUBE,
                response -> {

                    try {
                        JSONObject json = new JSONObject(response);
                        JSONArray jsonArray = json.getJSONArray("items");
                        int jsonArrayLen = jsonArray.length();

                        for (int i = jsonArrayLen - 1; i >= 0; i--) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject video_json = jsonObject.getJSONObject("snippet").getJSONObject("resourceId");
                            String title = jsonObject.getJSONObject("snippet").getString("title");
                            String Desc = jsonObject.getJSONObject("snippet").getString("description");
                            String id = video_json.getString("videoId");
                            Log.e("inside", "video ID-->" + id);
                            String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                            Videos video = new Videos(title, thumbUrl, id, Desc);
                            youtubeList.add(video);
                        }
                        yt_recycler_adapter.notifyDataSetChanged();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                },error -> {
            Log.d("#PLAYLIST", "error: "+ error.toString());

        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }





}
