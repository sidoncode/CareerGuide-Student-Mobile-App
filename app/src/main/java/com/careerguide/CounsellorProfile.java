package com.careerguide;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.careerguide.adapters.AlbumadapterProfile;
import com.careerguide.youtubeVideo.CommonEducationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CounsellorProfile extends AppCompatActivity {

    Activity activity = this;
    private RecyclerView recyclerView;
    private AlbumadapterProfile adapter;
    private List<Album> albumList;
    private int size;
    ProgressBar pb_loading;
    private ArrayList<live_counsellor_session> counsellors = new ArrayList<>();


    private com.careerguide.adapters.LivesessionAdapter allPastLiveSessionAdapter;
    private List<CommonEducationModel> allPastLiveSessionList;//used the same educationmodel from cgplaylist


    TextView tv_follow, tv_feed_title;
    ImageView followingTik;
    LinearLayoutManager mLayoutManager;

    String hostPic,hostEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pb_loading = findViewById(R.id.pb_loading);
        tv_follow = findViewById(R.id.tv_follow);
        followingTik = findViewById(R.id.followingTik);
        tv_feed_title = findViewById(R.id.tv_feed_title);


        RecyclerView recyclerViewPastLiveCounsellor = findViewById(R.id.recycler_view);

        allPastLiveSessionList = new ArrayList<CommonEducationModel>();
        allPastLiveSessionAdapter = new com.careerguide.adapters.LivesessionAdapter(this, allPastLiveSessionList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPastLiveCounsellor.setLayoutManager(mLayoutManager);
        recyclerViewPastLiveCounsellor.setAdapter(allPastLiveSessionAdapter);


        setSupportActionBar(toolbar);
        ((TextView)findViewById(R.id.host_name)).setText(getIntent().getStringExtra("host_name"));
        tv_feed_title.setText(getIntent().getStringExtra("host_name") +"'s Feed");

        hostPic = getIntent().getStringExtra("host_img");
        hostEmail = getIntent().getStringExtra("host_email");
        Log.d("COUNSELLOR_PROFILE", "host_img: "+hostPic);
        if(hostPic!=null)
            Glide.with(this).load(hostPic).into((ImageView) findViewById(R.id.profileImage));
        else
            fetchAndApplyImage();


        getPassLiveSession();

    }

    /*private void prepareAlbums() {
        allPastLiveSessionList.clear();
        for(int i = 0; i<size;i++){
            String imgurl = "";
            Log.e("url in exo" , "-->" +counsellors.get(i).getVideourl());
            Album a = new Album(counsellors.get(i).getId(),counsellors.get(i).getFullName(), counsellors.get(i).title, counsellors.get(i).getImgurl() , counsellors.get(i).getVideourl() , counsellors , hostEmail , Utility.getUserEducation(activity) , hostPic,getIntent().getStringExtra("video_views"));
            allPastLiveSessionList.add(a);
        }
        adapter.notifyDataSetChanged();
    }*/


    private void getPassLiveSession() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/counsellor/Facebook_Live_video", response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status) {
                    JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                    Log.e("lengthname--> ", "==> " + counsellorsJsonArray.length());
                    pb_loading.setVisibility(View.GONE);
                    for (int i = 0; counsellorsJsonArray != null && i < counsellorsJsonArray.length(); i++) {
                        JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                        //String email = counselorJsonObject.optString("id");

                        JSONObject JsonObject = counsellorsJsonArray.optJSONObject(i);
                        String user_id = JsonObject.optString("user_id");
                        String email = JsonObject.optString("email");
                        String name = JsonObject.optString("Name");
                        String img_url = JsonObject.optString("img_url");
                        String title = JsonObject.optString("title");
                        String video_url = JsonObject.optString("video_url");
                        String video_views=JsonObject.optString("views");
                        String video_id = JsonObject.optString("id");

                        if(video_views.contains("null")){
                            video_views="1";
                        }

                        allPastLiveSessionList.add(new CommonEducationModel(user_id,email, name, img_url, video_url, title, "",video_views,video_id));

                    }
                    size = counsellors.size();
                    allPastLiveSessionAdapter.notifyDataSetChanged();

                } else {
                    pb_loading.setVisibility(View.GONE);
                    Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            pb_loading.setVisibility(View.GONE);
            Toast.makeText(activity, VoleyErrorHelper.getMessage(error, activity), Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror", "error");
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", getIntent().getStringExtra("host_email"));
                Log.e("#line_status_request", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }


    public void back_onClick (View view){
        finish();
    }


    public void followClick (View view){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            LinearLayout tv_follow_layout = findViewById(R.id.tv_follow_layout);
            if(tv_follow.getText() == "Follow"){
                tv_follow_layout.setBackgroundResource(R.drawable.following_post_bg);
                tv_follow.setText("Following");
                tv_follow.setTextColor(Color.parseColor("#4e59ad"));
                followingTik.setVisibility(View.VISIBLE);
            }
            else{
                tv_follow_layout.setBackgroundResource(R.drawable.save_post_bg);
                tv_follow.setText("Follow");
                tv_follow.setTextColor(Color.parseColor("#ffffff"));
                followingTik.setVisibility(View.GONE);
            }
        }, 300);
    }

    private void fetchAndApplyImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/counsellor/fetch_counsellor_detail",
                response -> {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response);
                        JSONArray jsonArray = responseObject.getJSONArray("counsellors");
                        if(jsonArray.length()>0)
                        {
                            JSONObject counsellor = (JSONObject) jsonArray.get(0);
                            if(jsonArray.length()>0)
                            {
                                hostPic = "https://app.careerguide.com/api/user_dir/"+ counsellor.get("profile_pic");
                                Glide.with(this).load(hostPic).into((ImageView) findViewById(R.id.profileImage));
                                //prepareAlbums();
                                Log.d("#HOSTPIC :", hostPic);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("save_report_url_error","error"))
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("email" ,getIntent().getStringExtra("host_email"));
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}