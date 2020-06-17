package com.careerguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.youtubeVideo.CommonEducationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class exoplayerActivity extends AppCompatActivity {
    Activity activity = this;

    private com.careerguide.adapters.LivesessionAdapter allPastLiveSessionAdapter;
    private List<CommonEducationModel> allPastLiveSessionList;//used the same educationmodel from cgplaylist

    private CurrentLiveCounsellorsAdapter currentLiveCounsellorsAdapter;


    ArrayList<CurrentLiveCounsellorsModel> finalList=new ArrayList<>();
    ArrayList<CurrentLiveCounsellorsModel> tempCurrentLiveCounsellorsList = new ArrayList<>();
    ArrayList<CurrentLiveCounsellorsModel> tempPostLiveCounsellorsList = new ArrayList<>();


    LinearLayout currentLiveCounsellorsShimmer,pastLiveSessionsShimmer;

    private int size;
    private int size_new;
    /*
    private ArrayList<live_counsellor_session> counsellors = new ArrayList<>();
    private ArrayList<Counsellor> counsellors_new = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.exoplayer_play_activity);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Live Sessions");

        tempCurrentLiveCounsellorsList.add(new CurrentLiveCounsellorsModel("Checking...","","","",""));

        currentLiveCounsellorsShimmer=findViewById(R.id.currentLiveCounsellorsShimmer);
        pastLiveSessionsShimmer=findViewById(R.id.pastLiveSessionsShimmer);

        RecyclerView recyclerViewCurrentLiveCounsellor = findViewById(R.id.recyclerViewCurrentLiveCounsellor);


        currentLiveCounsellorsAdapter = new CurrentLiveCounsellorsAdapter(getApplicationContext(), finalList);
        recyclerViewCurrentLiveCounsellor.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager_new = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        //mLayoutManager_new.setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewCurrentLiveCounsellor.setLayoutManager(mLayoutManager_new);
        recyclerViewCurrentLiveCounsellor.setAdapter(currentLiveCounsellorsAdapter);


        RecyclerView recyclerViewPastLiveCounsellor = findViewById(R.id.recycler_view);

        allPastLiveSessionList = new ArrayList<CommonEducationModel>();
        allPastLiveSessionAdapter = new com.careerguide.adapters.LivesessionAdapter(this, allPastLiveSessionList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewPastLiveCounsellor.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewPastLiveCounsellor.setAdapter(allPastLiveSessionAdapter);
        getPastLiveSessions();
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


    /**
     * Adding few albums for testing
     */

    /*public void prepareCurrentLiveCousellorsAlbum() {
        Log.e("#name" , "==> " +counsellors_new.get(0).picUrl);
        for(int i=0;i<size_new;i++){
            Log.e("#nameww" , "==> " +counsellors_new.get(0).picUrl);
            CurrentLiveCounsellorsModel dm = new CurrentLiveCounsellorsModel();
            Log.e("#name" , "==> " +counsellors_new.get(i).picUrl);
            dm.setRestaurantName(counsellors_new.get(i).firstName + " "+counsellors_new.get(i).lastName);
            dm.setImgSrc(counsellors_new.get(i).picUrl);
            dm.setchannelname(counsellors_new.get(i).getchannel());
            currentLiveCounsellorsList.add(dm);
        }
        currentLiveCounsellorsAdapter.notifyDataSetChanged();
    }
    private void preparePastLiveCounsellorsAlbum() {
        for(int i = 0; i<size;i++){
            Log.e("url in exo" , "-->" +counsellors.get(i).getVideourl());
            Album a = new Album(counsellors.get(i).getId(),counsellors.get(i).getFullName(), counsellors.get(i).title, counsellors.get(i).getImgurl() , counsellors.get(i).getVideourl() , counsellors , counsellors.get(i).getEmail(),Utility.getUserEducation(activity), counsellors.get(i).getPicUrl(),counsellors.get(i).getVideoviews());
            albumList.add(a);
        }
        allPastLiveSessionAdapter.notifyDataSetChanged();
    }
    */

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


        @Override
        public void getItemOffsets(@NonNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getCurrentLiveCounsellors() {
        //showcurrentLiveCounsellorsShimmer();
        hidecurrentLiveCounsellorsShimmer();
        new TaskFetchLiveCounsellors().execute();

    }

    private void getPastLiveSessions() {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(activity);
        showPastLiveCounsellorsShimmer();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "AllVideos", response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status)
                {
                    JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");

                    Log.e("lengthname--> " , "==> " +counsellorsJsonArray.length() );

                    allPastLiveSessionList.clear();//clear all the old data and fetch new data

                    for (int i = 0; i < counsellorsJsonArray.length(); i++)
                    {

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
                        String profile_pic="https://app.careerguide.com/api/user_dir/"+JsonObject.optString("profile_pic");
                        allPastLiveSessionList.add(new CommonEducationModel(user_id,email, name, img_url, video_url, title, profile_pic,video_views,video_id,video_category));


                    }

                    allPastLiveSessionAdapter.notifyDataSetChanged();

                    //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                } else {
                    Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hidePastLiveCounsellorsShimmer();

        }, error -> {
            hidePastLiveCounsellorsShimmer();
            progressDialog.dismiss();
            Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        });
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    void showcurrentLiveCounsellorsShimmer(){
        currentLiveCounsellorsShimmer.setEnabled(false);
        currentLiveCounsellorsShimmer.setVisibility (View.VISIBLE);

    }
    void hidecurrentLiveCounsellorsShimmer(){

        currentLiveCounsellorsShimmer.setVisibility (View.GONE);

    }

    void showPastLiveCounsellorsShimmer(){
        pastLiveSessionsShimmer.setEnabled(false);
        pastLiveSessionsShimmer.setVisibility (View.VISIBLE);

    }
    void hidePastLiveCounsellorsShimmer(){

        pastLiveSessionsShimmer.setVisibility (View.GONE);

    }
    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLiveCounsellors();
    }

    public class TaskFetchLiveCounsellors extends AsyncTask<String, Void, List<CurrentLiveCounsellorsModel>> {


        @Override
        protected List<CurrentLiveCounsellorsModel> doInBackground(String... params) {

            tempCurrentLiveCounsellorsList.clear();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "all_available_counsellors", response -> {
                Log.e("all_coun_res", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {
                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                        Log.e("name-2->","" +counsellorsJsonArray);

                        for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                        {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("co_id");
                            String firstName = counselorJsonObject.optString("first_name");
                            String lastName = counselorJsonObject.optString("last_name");
                            String picUrl = counselorJsonObject.optString("profile_pic");
                            String channel_name = counselorJsonObject.optString("channel_name");
                            String scheduleDescrpition = "LIVE NOW, Session on "+counselorJsonObject.optString("topic");
                            Log.e("name-1->","" +channel_name);
                            tempCurrentLiveCounsellorsList.add(new CurrentLiveCounsellorsModel(firstName+" "+lastName,"",picUrl,channel_name,scheduleDescrpition));
                            Log.e("#inside" ,"for" +picUrl+"__"+tempCurrentLiveCounsellorsList.get(0).getCounsellorName());

                        }


                        new TaskFetchPostLiveCounsellors().execute();


                        // Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                    //hideProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {

                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    Log.e("all_coun_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);



            return null;

        }

    }

    public class TaskFetchPostLiveCounsellors extends AsyncTask<String, Void, List<CurrentLiveCounsellorsModel>> {


        @Override
        protected List<CurrentLiveCounsellorsModel> doInBackground(String... params) {
            tempPostLiveCounsellorsList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/fetch_session", response -> {
                Log.e("postlive_coun_res", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {

                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellor");
                        Log.e("name-2->","" +counsellorsJsonArray);


                        for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                        {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("userId");
                            String firstName = counselorJsonObject.optString("co_FirstName");
                            String lastName = counselorJsonObject.optString("co_LastName");
                            String picUrl = counselorJsonObject.optString("co_img");
                            String channel_name = counselorJsonObject.optString("channel_name");
                            String scheduleDescrpition = "LIVE AT "+counselorJsonObject.optString("time")+" on "+counselorJsonObject.optString("formatteddate")+". Topic: "+counselorJsonObject.optString("topic")+".";
                            Log.e("name-desc->","" +scheduleDescrpition);
                            Log.e("name-1->","" +channel_name);
                            tempPostLiveCounsellorsList.add(new CurrentLiveCounsellorsModel(firstName+" "+lastName,"",picUrl,channel_name,scheduleDescrpition));//use the same model for postlive sessions
                            Log.e("#inside" ,"for" +picUrl+"__"+tempPostLiveCounsellorsList.get(0).getCounsellorName());

                        }


                        runOnUiThread(()->{
                            finalList.clear();
                            finalList.addAll(tempCurrentLiveCounsellorsList);
                            finalList.addAll(tempPostLiveCounsellorsList);
                            currentLiveCounsellorsAdapter.notifyDataSetChanged();
                        });
                        // Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                    //hideProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    Log.e("all_coun_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);



            return null;

        }

        @Override
        protected void onPostExecute(List<CurrentLiveCounsellorsModel> result) {//is needed don't delete

            //setDisplaylistArrayLiveCounsellors(result);
            //Log.i("sssss",result.get(0).getCounsellorName());
        }
    }


}