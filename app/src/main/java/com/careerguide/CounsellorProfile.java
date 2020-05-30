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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class CounsellorProfile extends AppCompatActivity {

    Activity activity = this;
    private RecyclerView recyclerView;
    private AlbumadapterProfile adapter;
    private List<Album> albumList;
    private int size;
    ProgressBar pb_loading;
    private ArrayList<live_counsellor_session> counsellors = new ArrayList<>();
    private int totalSessionsTaken=0;


    private List<CounsellorProfileExpertLevelModel> student_education_level_list;
    private CounsellorProfileExpertLevelAdapter counsellorProfileExpertLevelAdapter;
    private LinearLayoutManager expertLevelLayoutManager;

    private com.careerguide.adapters.LivesessionAdapter allPastLiveSessionAdapter;
    private List<CommonEducationModel> allPastLiveSessionList;//used the same educationmodel from cgplaylist


    TextView tv_followers_count, tv_feed_title,tv_session_conducted_count,tv_follow;
    ImageView followingTik;
    LinearLayoutManager mLayoutManager;

    String hostPic,hostEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellor_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pb_loading = findViewById(R.id.pb_loading);
        tv_session_conducted_count=findViewById(R.id.tv_session_conducted_count);
        tv_followers_count = findViewById(R.id.tv_followers_count);
        tv_follow = findViewById(R.id.tv_follow);
        followingTik = findViewById(R.id.followingTik);
        tv_feed_title = findViewById(R.id.tv_feed_title);

        RecyclerView recyclerViewExpertLevel = findViewById(R.id.recyclerViewExpertLevel);
        RecyclerView recyclerViewPastLiveCounsellor = findViewById(R.id.recycler_view);

        allPastLiveSessionList = new ArrayList<CommonEducationModel>();
        allPastLiveSessionAdapter = new com.careerguide.adapters.LivesessionAdapter(this, allPastLiveSessionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewPastLiveCounsellor.setLayoutManager(mLayoutManager);
        recyclerViewPastLiveCounsellor.setAdapter(allPastLiveSessionAdapter);

        student_education_level_list=new ArrayList<>();
        counsellorProfileExpertLevelAdapter = new CounsellorProfileExpertLevelAdapter(this, student_education_level_list,allPastLiveSessionAdapter);
        expertLevelLayoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewExpertLevel.setLayoutManager(expertLevelLayoutManager);
        recyclerViewExpertLevel.setAdapter(counsellorProfileExpertLevelAdapter);

        setSupportActionBar(toolbar);
        ((TextView)findViewById(R.id.host_name)).setText(getIntent().getStringExtra("host_name"));
        tv_feed_title.setText(getIntent().getStringExtra("host_name") +"'s Feed");

        hostPic = getIntent().getStringExtra("host_img");
        Glide.with(this).load(hostPic).into((ImageView) findViewById(R.id.profileImage));
        hostEmail = getIntent().getStringExtra("host_email");
        Log.d("COUNSELLOR_PROFILE", "host_img: "+hostPic);

        fetchAndApplyImage();

        getPastLiveSession();

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


    private void getPastLiveSession() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/counsellor/Facebook_Live_video", response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status) {
                    JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                    Log.e("lengthname--> ", "==> " + counsellorsJsonArray.length());
                    pb_loading.setVisibility(View.GONE);

                    totalSessionsTaken=counsellorsJsonArray.length();
                    tv_session_conducted_count.setText(totalSessionsTaken+"");


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
                        String video_category=JsonObject.optString("Video_category");
                        String profile_pic=hostPic;

                        if(video_views.contains("null")){
                            video_views="1";
                        }

                        allPastLiveSessionList.add(new CommonEducationModel(user_id,email, name, img_url, video_url, title, profile_pic,video_views,video_id,video_category));

                    }
                    counsellorProfileExpertLevelAdapter.setListOfPastSession(allPastLiveSessionList);//used for sorting in the CounsellorProfileExpertLevelAdapter on click of the expert level

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
                tv_followers_count.setText("1");
            }
            else{
                tv_follow_layout.setBackgroundResource(R.drawable.save_post_bg);
                tv_follow.setText("Follow");
                tv_follow.setTextColor(Color.parseColor("#ffffff"));
                followingTik.setVisibility(View.GONE);
                tv_followers_count.setText("0");
            }
        }, 300);
    }

    private void fetchAndApplyImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/counsellor/fetch_counsellor_detail",
                response -> {
                    JSONObject responseObject = null;
                    try {
                        responseObject = new JSONObject(response);
                        Log.i("resppp",response);
                        JSONArray jsonArray = responseObject.getJSONArray("counsellors");
                        if(jsonArray.length()>0)
                        {
                            JSONObject counsellor = (JSONObject) jsonArray.get(0);
                            Log.i("respprrp",counsellor+"");
                            if(counsellor.length()>0)
                            {
                                JSONArray student_education_level_array=new JSONArray(counsellor.get("student_education_level")+"");


                                student_education_level_list.add(new CounsellorProfileExpertLevelModel("All"));
                                for(int j=0;j<student_education_level_array.length();j++){
                                    student_education_level_list.add(new CounsellorProfileExpertLevelModel(((JSONObject)student_education_level_array.get(j)).get("expertLevel").toString()));
                                    Log.i("expertLevel"+j,student_education_level_list.get(j).getExpertLevel());
                                }
                                counsellorProfileExpertLevelAdapter.notifyDataSetChanged();

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