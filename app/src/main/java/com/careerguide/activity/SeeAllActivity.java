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

import com.careerguide.R;
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

    private List<CommonEducationModel> educationList = new ArrayList<>();
    List<Videos> youtubeList = new ArrayList<>();

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
                URL_EDUCATION+=getIntent().getStringExtra("EDU_KEY");
                adapter = new CommonEducationAdapter(educationList,this);
                adapter.setSeeAllMode(true);
                recyclerView.setAdapter(adapter);
                new TaskEductaion().execute();
                break;
            case 1:
                playlistKEY = getIntent().getStringExtra("KEY");
                URL_YOUTUBE= "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+playlistKEY+"&key=" + browserKey + "&maxResults=50";
                yt_recycler_adapter = new YT_recycler_adapter(youtubeList,browserKey,this,0, Color.parseColor("#000000"));
                yt_recycler_adapter.setSeeAllMode(true);
                recyclerView.setAdapter(yt_recycler_adapter);
                new TaskYoutube().execute();
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

    private class TaskEductaion extends AsyncTask<Void, Void, Void> {

        CommonEducationModel commonEducationModel;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                String response_NINE = getUrlString(URL_EDUCATION);
                JSONObject json_NINE = new JSONObject(response_NINE);
                JSONArray jsonArray_NINE = json_NINE.optJSONArray("videos");
                int jsonArrayLen=jsonArray_NINE.length();
                List<Integer> randomIndexList = new ArrayList<Integer>();
                Random rand = new Random();
                int tempRandom;

                for (int i = 0; i < jsonArrayLen ; i++) {

                    while(true) {//loop until no duplicate is found.
                        tempRandom = rand.nextInt(jsonArrayLen);
                        if (randomIndexList.contains(tempRandom)) {
                            continue;//duplicate number,hence skip
                        } else {
                            randomIndexList.add(tempRandom);
                            break;
                        }
                    }

                    JSONObject JsonObject_NINE = jsonArray_NINE.optJSONObject(tempRandom);
                    String email_NINE = JsonObject_NINE.optString("email");
                    String name_NINE = JsonObject_NINE.optString("Name");
                    String img_url_NINE = JsonObject_NINE.optString("img_url");
                    String title_NINE = JsonObject_NINE.optString("title");
                    String video_url_NINE = JsonObject_NINE.optString("video_url");
                    String video_views = JsonObject_NINE.optString("views");
                    commonEducationModel = new CommonEducationModel(email_NINE, name_NINE, img_url_NINE, video_url_NINE, title_NINE, "",video_views);
                    educationList.add(commonEducationModel);
                }
                Log.e("#Nine","-->");
            }catch(Exception e1)
            {
                e1.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    private class TaskYoutube extends AsyncTask<Void, Void, Void> {

        Videos video;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                String response_three = getUrlString(URL_YOUTUBE);
                JSONObject json_three = new JSONObject(response_three);
                JSONArray jsonArray_three = json_three.getJSONArray("items");
                int jsonArrayLen=jsonArray_three.length();
                List<Integer> randomIndexList = new ArrayList<Integer>();
                Random rand = new Random();
                int tempRandom;

                for (int i = 0; i < jsonArrayLen-1; i++) {

                    while(true) {//loop until no duplicate is found.
                        tempRandom = rand.nextInt(jsonArrayLen);
                        if (randomIndexList.contains(tempRandom)) {
                            continue;//duplicate number,hence skip
                        } else {
                            randomIndexList.add(tempRandom);
                            break;
                        }
                    }

                    JSONObject jsonObject_three = jsonArray_three.getJSONObject(tempRandom);
                    JSONObject video_three = jsonObject_three.getJSONObject("snippet").getJSONObject("resourceId");
                    String title_three = jsonObject_three.getJSONObject("snippet").getString("title");
                    String Desc_three = jsonObject_three.getJSONObject("snippet").getString("description");
                    String id_three = video_three.getString("videoId");
                    String thumbUrl_three = jsonObject_three.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    video = new Videos(title_three, thumbUrl_three, id_three, Desc_three);
                    youtubeList.add(video);
                }
                Log.e("#YT_SEE_ALL","-->");
            }catch(Exception e1)
            {
                e1.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            yt_recycler_adapter.notifyDataSetChanged();
        }
    }


    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


}
