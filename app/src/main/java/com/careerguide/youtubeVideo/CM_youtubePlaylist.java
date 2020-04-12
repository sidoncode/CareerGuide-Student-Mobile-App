package com.careerguide.youtubeVideo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.careerguide.R;
import com.careerguide.blog.DataMembers;
import com.careerguide.blog.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CM_youtubePlaylist extends AppCompatActivity {

    List<Videos> displaylistArray = new ArrayList<Videos>();
    List<Videos_two> displaylistArray_two = new ArrayList<Videos_two>();
    List<Videos_three> displaylistArray_three = new ArrayList<Videos_three>();
    List<Videos_NINE> displaylistArray_NINE = new ArrayList<Videos_NINE>();
    List<Videos_TEN> displaylistArray_TEN = new ArrayList<Videos_TEN>();
    List<Videos_ELEVEN> displaylistArray_ELEVEN = new ArrayList<Videos_ELEVEN>();
    List<Videos_TWELVE> displaylistArray_TWELVE = new ArrayList<Videos_TWELVE>();
    List<Videos_GRADUATE> displaylistArray_GRADUATE = new ArrayList<Videos_GRADUATE>();
    List<Videos_POSTGRA> displaylistArray_POSTGRA = new ArrayList<Videos_POSTGRA>();
    List<Videos_WORKING> displaylistArray_WORKING = new ArrayList<Videos_WORKING>();
    List<DataMembers> displaylistArray_Blog = new ArrayList<DataMembers>();

    private RecyclerView mVideoRecyclerView , mVideoRecyclerView_two ,mVideoRecyclerView_three , mVideoRecyclerView_NINE, mVideoRecyclerView_TEN, mVideoRecyclerView_ELEVEN, mVideoRecyclerView_TWELVE,mVideoRecyclerView_GRADUATE,mVideoRecyclerView_POSTGRA,mVideoRecyclerView_WORKING ,mVideoRecyclerView_Blog ;
    private YT_recycler_adapter mVideoAdapter;
    private YT_recycler_adapter_two mVideoAdapter_two;
    private YT_recycler_adapter_three mVideoAdapter_three;
    private YT_recycler_adapter_NINE mVideoAdapter_NINE;
    private YT_recycler_adapter_TEN mVideoAdapter_TEN;
    private YT_recycler_adapter_ELEVEN mVideoAdapter_ELEVEN;
    private YT_recycler_adapter_TWELVE mVideoAdapter_TWELVE;
    private YT_recycler_adapter_GRADUATE mVideoAdapter_GRADUATE;
    private YT_recycler_adapter_POSTGRA mVideoAdapter_POSTGRA;
    private YT_recycler_adapter_WORKING mVideoAdapter_WORKING;
    private RecyclerAdapter mVideoAdapter_Blog;

    Context context;
    private String playlist_id;
    Activity activity = this;
    private String browserKey;
    String loadMsg;
    String loadTitle;
    LinearLayoutManager mLayoutManager , mLayoutManager_two, mLayoutManager_three, mLayoutManager_NINE , mLayoutManager_TEN , mLayoutManager_ELEVEN, mLayoutManager_TWELVE , mLayoutManager_GRADUATE ,mLayoutManager_POSTGRA ,mLayoutManager_WORKING, mLayoutManager_Blog;

    TextView Cat_1,Cat_2,Cat_3,Cat_4,Cat_5,Cat_6,Cat_7,Cat_8,Cat_9,Cat_10, Cat_Blog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cm_yt_playlist_recycler);
        Cat_1 = findViewById(R.id.Cat_1);
        Cat_2 = findViewById(R.id.Cat_2);
        Cat_3 = findViewById(R.id.Cat_3);
        Cat_4 = findViewById(R.id.Cat_4);
        Cat_5 = findViewById(R.id.Cat_5);
        Cat_6 = findViewById(R.id.Cat_6);
        Cat_7 = findViewById(R.id.Cat_7);
        Cat_8 = findViewById(R.id.Cat_8);
        Cat_9 = findViewById(R.id.Cat_9);
        Cat_10 = findViewById(R.id.Cat_10);
        Cat_Blog = findViewById(R.id.Cat_Blog);


        Typeface font = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-SemiBold.ttf");

        Cat_1.setTypeface(font);
        Cat_2.setTypeface(font);
        Cat_3.setTypeface(font);
        Cat_4.setTypeface(font);
        Cat_5.setTypeface(font);
        Cat_6.setTypeface(font);
        Cat_7.setTypeface(font);
        Cat_8.setTypeface(font);
        Cat_9.setTypeface(font);
        Cat_10.setTypeface(font);
        Cat_Blog.setTypeface(font);

        Cat_1.setText("CareerGuide Tests");
        Cat_2.setText("CareerGuide Counsellors");
        Cat_Blog.setText("Career Articles");
        Cat_3.setText("Class 9th");
        Cat_4.setText("Class 10th");
        Cat_5.setText("Class 11th");
        Cat_6.setText("Class 12th");
        Cat_7.setText("Graduates");
        Cat_8.setText("PostGraduates");
        Cat_9.setText("Working Professional");
        Cat_10.setText("Counselor Video");

        loadTitle = "Loading...";
        loadMsg = "Loading your videos...";
        browserKey = "AIzaSyC2VcqdBaKakTd7YLn4B9t3dxWat9UHze4";
        playlist_id = getIntent().getStringExtra("playlist_id");
        int cornerRadius = 5;
        int videoTxtColor = Color.parseColor("#000000");

        //TODO: https://www.googleapis.com/youtube/v3/playlists?part=snippet&channelId=UCs6EVBxMpm9S3a2RpbAIp1w&key=AIzaSyC2VcqdBaKakTd7YLn4B9t3dxWat9UHze4

        mVideoRecyclerView = findViewById(R.id.yt_recycler_view_one);
        mVideoRecyclerView_two = findViewById(R.id.yt_recycler_view_two);
        mVideoRecyclerView_three = findViewById(R.id.yt_recycler_view_three);
        mVideoRecyclerView_NINE = findViewById(R.id.yt_recycler_view_NINE);
        mVideoRecyclerView_TEN = findViewById(R.id.yt_recycler_view_TEN);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager_two = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager_three = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager_NINE = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManager_TEN = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        mVideoRecyclerView.setLayoutManager(mLayoutManager);
        mVideoRecyclerView_two.setLayoutManager(mLayoutManager_two);
        mVideoRecyclerView_three.setLayoutManager(mLayoutManager_three);
        mVideoRecyclerView_NINE.setLayoutManager(mLayoutManager_NINE);
        mVideoRecyclerView_TEN.setLayoutManager(mLayoutManager_TEN);

        mVideoAdapter = new YT_recycler_adapter(displaylistArray, browserKey, this, cornerRadius, videoTxtColor);
        mVideoAdapter_two = new YT_recycler_adapter_two(displaylistArray_two, browserKey, this, cornerRadius, videoTxtColor);
        mVideoAdapter_three = new YT_recycler_adapter_three(displaylistArray_three, browserKey, this, cornerRadius, videoTxtColor);
        mVideoAdapter_NINE = new YT_recycler_adapter_NINE(displaylistArray_NINE, browserKey, this, cornerRadius, videoTxtColor);
        mVideoAdapter_TEN = new YT_recycler_adapter_TEN(displaylistArray_TEN, browserKey, this, cornerRadius, videoTxtColor);

        mVideoRecyclerView.setAdapter(mVideoAdapter);
        mVideoRecyclerView_two.setAdapter(mVideoAdapter_two);
        mVideoRecyclerView_three.setAdapter(mVideoAdapter_three);
        mVideoRecyclerView_NINE.setAdapter(mVideoAdapter_NINE);
        mVideoRecyclerView_TEN.setAdapter(mVideoAdapter_TEN);

        mVideoRecyclerView_ELEVEN = findViewById(R.id.yt_recycler_view_ELEVEN);
        mLayoutManager_ELEVEN = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mVideoRecyclerView_ELEVEN.setLayoutManager(mLayoutManager_ELEVEN);
        mVideoAdapter_ELEVEN = new YT_recycler_adapter_ELEVEN(displaylistArray_ELEVEN, browserKey, this, cornerRadius, videoTxtColor);
        mVideoRecyclerView_ELEVEN.setAdapter(mVideoAdapter_ELEVEN);

        mVideoRecyclerView_TWELVE = findViewById(R.id.yt_recycler_view_TWELVE);
        mLayoutManager_TWELVE = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mVideoRecyclerView_TWELVE.setLayoutManager(mLayoutManager_TWELVE);
        mVideoAdapter_TWELVE = new YT_recycler_adapter_TWELVE(displaylistArray_TWELVE, browserKey, this, cornerRadius, videoTxtColor);
        mVideoRecyclerView_TWELVE.setAdapter(mVideoAdapter_TWELVE);

        mVideoRecyclerView_GRADUATE = findViewById(R.id.yt_recycler_view_GRADUATE);
        mLayoutManager_GRADUATE = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mVideoRecyclerView_GRADUATE.setLayoutManager(mLayoutManager_GRADUATE);
        mVideoAdapter_GRADUATE = new YT_recycler_adapter_GRADUATE(displaylistArray_GRADUATE, browserKey, this, cornerRadius, videoTxtColor);
        mVideoRecyclerView_GRADUATE.setAdapter(mVideoAdapter_GRADUATE);

        mVideoRecyclerView_POSTGRA = findViewById(R.id.yt_recycler_view_POSTGRA);
        mLayoutManager_POSTGRA = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mVideoRecyclerView_POSTGRA.setLayoutManager(mLayoutManager_POSTGRA);
        mVideoAdapter_POSTGRA = new YT_recycler_adapter_POSTGRA(displaylistArray_POSTGRA, browserKey, this, cornerRadius, videoTxtColor);
        mVideoRecyclerView_POSTGRA.setAdapter(mVideoAdapter_POSTGRA);

        mVideoRecyclerView_WORKING = findViewById(R.id.yt_recycler_view_WORKING);
        mLayoutManager_WORKING = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mVideoRecyclerView_WORKING.setLayoutManager(mLayoutManager_WORKING);
        mVideoAdapter_WORKING = new YT_recycler_adapter_WORKING(displaylistArray_WORKING, browserKey, this, cornerRadius, videoTxtColor);
        mVideoRecyclerView_WORKING.setAdapter(mVideoAdapter_WORKING);

        mVideoRecyclerView_Blog = findViewById(R.id.yt_recycler_view_Blog);
        mLayoutManager_Blog = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mVideoRecyclerView_Blog.setLayoutManager(mLayoutManager_Blog);
        mVideoAdapter_Blog = new RecyclerAdapter(this , displaylistArray_Blog);
        mVideoRecyclerView_Blog.setAdapter(mVideoAdapter_Blog);

        new TheTask().execute();
    }



    private class TheTask extends AsyncTask<Void, Void, Void>
    {

        Videos displaylist ;
        Videos_two displaylist_two;
        Videos_three displaylist_three;
        Videos_NINE displaylist_NINE;
        Videos_TEN displaylist_TEN;
        Videos_ELEVEN displaylist_ELEVEN;
        Videos_TWELVE displaylist_TWELVE;
        Videos_GRADUATE displaylist_GRADUATE;
        Videos_POSTGRA displaylist_POSTGRA;
        Videos_WORKING displaylist_WORKING;
        DataMembers displaylist_Blog;

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try
            {
                String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLnnMTbSs_SO5Fye2aPKAOmc7FZcsZC9cX&key=" + browserKey + "&maxResults=50";
                String response = getUrlString(url);
                JSONObject json = new JSONObject(response.toString());
                JSONArray jsonArray = json.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject video = jsonObject.getJSONObject("snippet").getJSONObject("resourceId");
                    String title = jsonObject.getJSONObject("snippet").getString("title");
                    String Desc = jsonObject.getJSONObject("snippet").getString("description");
                    String id = video.getString("videoId");
                    Log.e("inside","-->" +id);
                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("standard").getString("url");
                    displaylist = new Videos(title, thumbUrl ,id , Desc);
                    displaylistArray.add(displaylist);
                }

                String url_two = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLnnMTbSs_SO6r8uB8i0COUrTe7L4SLZeK&key=" + browserKey + "&maxResults=50";
                String response_two = getUrlString(url_two);
                JSONObject json_two = new JSONObject(response_two.toString());
                JSONArray jsonArray_two = json_two.getJSONArray("items");
                for (int i = 0; i < jsonArray_two.length(); i++) {
                    JSONObject jsonObject_two = jsonArray_two.getJSONObject(i);
                    JSONObject video_two = jsonObject_two.getJSONObject("snippet").getJSONObject("resourceId");
                    String title_two = jsonObject_two.getJSONObject("snippet").getString("title");
                    String Desc_two = jsonObject_two.getJSONObject("snippet").getString("description");
                    String id_two = video_two.getString("videoId");
                    Log.e("inside","-->" +id_two);
                    String thumbUrl_two = jsonObject_two.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("standard").getString("url");
                    displaylist_two = new Videos_two(title_two, thumbUrl_two ,id_two , Desc_two);
                    displaylistArray_two.add(displaylist_two);
                }

                String url_Blog = "https://www.careerguide.com/career/wp-json/wp/v2/posts";
                String response_Blog = getUrlString(url_Blog);
                JSONArray jsonArray_Blog = new JSONArray(response_Blog);
                Log.e("Jsonresult" , "--> " +response_Blog);
                for (int i = 0; i < 10; i++) {
                    JSONObject jsonObject;
                    jsonObject = (JSONObject) jsonArray_Blog.get(i);
                    JSONObject jsonObject1;
                    jsonObject1 = (JSONObject) jsonObject.get("title");
                    JSONObject jsonObject2;
                    jsonObject2 = (JSONObject) jsonObject.get("content");
                    JSONObject jsonObject3;
                    jsonObject3 = (JSONObject) jsonObject.get("excerpt");
                    String imgUrl = getFetaureImageUrl(jsonObject2.getString("rendered"));
                    displaylist_Blog = new DataMembers(jsonObject.getInt("id"), jsonObject1.getString("rendered") , jsonObject2.getString("rendered") ,imgUrl , jsonObject.getString("link"), jsonObject3.getString("rendered"));
                    displaylistArray_Blog.add(displaylist_Blog);
                }



                String url_three = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLnnMTbSs_SO6xa4LFPEFb1t3ICJ0IQNLc&key=" + browserKey + "&maxResults=50";
                String response_three = getUrlString(url_three);
                JSONObject json_three = new JSONObject(response_three.toString());
                JSONArray jsonArray_three = json_three.getJSONArray("items");
                for (int i = 0; i < jsonArray_three.length(); i++) {
                    JSONObject jsonObject_three = jsonArray_three.getJSONObject(i);
                    JSONObject video_three = jsonObject_three.getJSONObject("snippet").getJSONObject("resourceId");
                    String title_three = jsonObject_three.getJSONObject("snippet").getString("title");
                    String Desc_three = jsonObject_three.getJSONObject("snippet").getString("description");
                    String id_three = video_three.getString("videoId");
                    Log.e("inside","-->" +id_three);
                    String thumbUrl_three = jsonObject_three.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("standard").getString("url");
                    displaylist_three = new Videos_three(title_three, thumbUrl_three ,id_three , Desc_three);
                    displaylistArray_three.add(displaylist_three);
                }

                String url_NINE = "https://app.careerguide.com/api/main/videos_NINE";
                String response_NINE = getUrlString(url_NINE);
                JSONObject json_NINE = new JSONObject(response_NINE.toString());
                JSONArray jsonArray_NINE = json_NINE.optJSONArray("videos");
                for (int i = 0; i < jsonArray_NINE.length(); i++) {
                    JSONObject JsonObject_NINE = jsonArray_NINE.optJSONObject(i);
                    String email_NINE = JsonObject_NINE.optString("email");
                    String name_NINE = JsonObject_NINE.optString("Name");
                    String img_url_NINE = JsonObject_NINE.optString("img_url");
                    String title_NINE = JsonObject_NINE.optString("title");
                    String video_url_NINE = JsonObject_NINE.optString("video_url");
                    displaylist_NINE = new Videos_NINE(email_NINE,name_NINE,img_url_NINE,video_url_NINE,title_NINE,"");
                    displaylistArray_NINE.add(displaylist_NINE);
                }

                String url_TEN = "https://app.careerguide.com/api/main/videos_TEN";
                String response_TEN = getUrlString(url_TEN);
                JSONObject json_TEN = new JSONObject(response_TEN.toString());
                JSONArray jsonArray_TEN = json_TEN.optJSONArray("videos");
                Log.e("jsonArray_TEN","-->" +jsonArray_TEN.length());
                for (int i = 0; i < jsonArray_TEN.length(); i++) {
                    JSONObject JsonObject_TEN = jsonArray_TEN.optJSONObject(i);
                    Log.e("JsonObject_TEN","-->" +jsonArray_TEN);
                    String email_TEN = JsonObject_TEN.optString("email");
                    String name_TEN = JsonObject_TEN.optString("Name");
                    String img_url_TEN = JsonObject_TEN.optString("img_url");
                    String title_TEN = JsonObject_TEN.optString("title");
                    String video_url_TEN = JsonObject_TEN.optString("video_url");
                    displaylist_TEN = new Videos_TEN(email_TEN,name_TEN,img_url_TEN,video_url_TEN,title_TEN,"");
                    displaylistArray_TEN.add(displaylist_TEN);
                }

                String url_ELEVEN = "https://app.careerguide.com/api/main/videos_ELEVEN";
                String response_ELEVEN = getUrlString(url_ELEVEN);
                JSONObject json_ELEVEN = new JSONObject(response_ELEVEN.toString());
                JSONArray jsonArray_ELEVEN = json_ELEVEN.optJSONArray("videos");
                for (int i = 0; i < jsonArray_ELEVEN.length(); i++) {
                    JSONObject JsonObject_ELEVEN = jsonArray_NINE.optJSONObject(i);
                    String email = JsonObject_ELEVEN.optString("email");
                    String name = JsonObject_ELEVEN.optString("Name");
                    String img_url = JsonObject_ELEVEN.optString("img_url");
                    String title = JsonObject_ELEVEN.optString("title");
                    String video_url = JsonObject_ELEVEN.optString("video_url");
                    displaylist_ELEVEN = new Videos_ELEVEN(email,name,img_url,video_url,title,"");
                    displaylistArray_ELEVEN.add(displaylist_ELEVEN);
                }

                String url_TWELVE = "https://app.careerguide.com/api/main/videos_TWELVE";
                String response_TWELVE = getUrlString(url_TWELVE);
                JSONObject json_TWELVE = new JSONObject(response_TWELVE.toString());
                JSONArray jsonArray_TWELVE = json_TWELVE.optJSONArray("videos");
                for (int i = 0; i < jsonArray_TWELVE.length(); i++) {
                    JSONObject JsonObject_TWELVE = jsonArray_TWELVE.optJSONObject(i);
                    String email = JsonObject_TWELVE.optString("email");
                    String name = JsonObject_TWELVE.optString("Name");
                    String img_url = JsonObject_TWELVE.optString("img_url");
                    String title = JsonObject_TWELVE.optString("title");
                    String video_url = JsonObject_TWELVE.optString("video_url");
                    displaylist_TWELVE = new Videos_TWELVE(email,name,img_url,video_url,title,"");
                    displaylistArray_TWELVE.add(displaylist_TWELVE);
                }

                String url_GRADUATE = "https://app.careerguide.com/api/main/videos_GRADUATE";
                String response_GRADUATE = getUrlString(url_GRADUATE);
                JSONObject json_GRADUATE = new JSONObject(response_GRADUATE.toString());
                JSONArray jsonArray_GRADUATE = json_GRADUATE.optJSONArray("videos");
                for (int i = 0; i < jsonArray_GRADUATE.length(); i++) {
                    JSONObject JsonObject_GRADUATE = jsonArray_GRADUATE.optJSONObject(i);
                    String email = JsonObject_GRADUATE.optString("email");
                    String name = JsonObject_GRADUATE.optString("Name");
                    String img_url = JsonObject_GRADUATE.optString("img_url");
                    String title = JsonObject_GRADUATE.optString("title");
                    String video_url = JsonObject_GRADUATE.optString("video_url");
                    displaylist_GRADUATE = new Videos_GRADUATE(email,name,img_url,video_url,title,"");
                    displaylistArray_GRADUATE.add(displaylist_GRADUATE);
                }

                String url_POSTGRA = "https://app.careerguide.com/api/main/videos_POSTGRA";
                String response_POSTGRA = getUrlString(url_POSTGRA);
                JSONObject json_POSTGRA = new JSONObject(response_POSTGRA.toString());
                JSONArray jsonArray_POSTGRA = json_POSTGRA.optJSONArray("videos");
                for (int i = 0; i < jsonArray_POSTGRA.length(); i++) {
                    JSONObject JsonObject_POSTGRA = jsonArray_POSTGRA.optJSONObject(i);
                    String email = JsonObject_POSTGRA.optString("email");
                    String name = JsonObject_POSTGRA.optString("Name");
                    String img_url = JsonObject_POSTGRA.optString("img_url");
                    String title = JsonObject_POSTGRA.optString("title");
                    String video_url = JsonObject_POSTGRA.optString("video_url");
                    displaylist_POSTGRA = new Videos_POSTGRA(email,name,img_url,video_url,title,"");
                    displaylistArray_POSTGRA.add(displaylist_POSTGRA);
                }

                String url_WORKING = "https://app.careerguide.com/api/main/videos_WORKING";
                String response_WORKING = getUrlString(url_WORKING);
                JSONObject json_WORKING = new JSONObject(response_WORKING.toString());
                JSONArray jsonArray_WORKING = json_WORKING.optJSONArray("videos");
                for (int i = 0; i < jsonArray_WORKING.length(); i++) {
                    JSONObject JsonObject_WORKING = jsonArray_WORKING.optJSONObject(i);
                    String email = JsonObject_WORKING.optString("email");
                    String name = JsonObject_WORKING.optString("Name");
                    String img_url = JsonObject_WORKING.optString("img_url");
                    String title = JsonObject_WORKING.optString("title");
                    String video_url = JsonObject_WORKING.optString("video_url");
                    displaylist_WORKING = new Videos_WORKING(email,name,img_url,video_url,title,"");
                    displaylistArray_WORKING.add(displaylist_WORKING);
                }

            }
            catch(Exception e1)
            {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mVideoAdapter.notifyDataSetChanged();
            mVideoAdapter_two.notifyDataSetChanged();
            mVideoAdapter_three.notifyDataSetChanged();
            mVideoAdapter_NINE.notifyDataSetChanged();
            mVideoAdapter_TEN.notifyDataSetChanged();
            mVideoAdapter_TWELVE.notifyDataSetChanged();
            mVideoAdapter_ELEVEN.notifyDataSetChanged();
            mVideoAdapter_GRADUATE.notifyDataSetChanged();
            mVideoAdapter_POSTGRA.notifyDataSetChanged();
            mVideoAdapter_WORKING.notifyDataSetChanged();
            mVideoAdapter_Blog.notifyDataSetChanged();


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

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

    private String getFetaureImageUrl(String rendered) {

        String imgurl;
        Pattern r = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = r.matcher(rendered);
        if (m.find()) {
            Log.i("0",m.group(1));
            imgurl = m.group(1);
        }
        else {
            imgurl = "http://localsplashcdn.wpengine.netdna-cdn.com/wp-content/uploads/2013/04/The-page-wont-load.png";
        }

        return imgurl;
    }
}
