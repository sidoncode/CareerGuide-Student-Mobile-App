package com.careerguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class exoplayerActivity extends AppCompatActivity {
    Activity activity = this;

    private RecyclerView recyclerView;
    private RecyclerView recyclerView_new;
    private com.careerguide.adapters.LivesessionAdapter adapter;
    private com.careerguide.MyCustomAdapter adapter_new;
    private List<Album> albumList;
    private List<DataModels> albumList_new;
    private int size;
    private int size_new;
    private ArrayList<live_counsellor_session> counsellors = new ArrayList<>();
    private ArrayList<Counsellor> counsellors_new = new ArrayList<>();
    private String Channel_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.exoplayer_play_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Live Sessions");
        recyclerView_new  = (RecyclerView) findViewById(R.id.recyclerView_new);
        Channel_name = getIntent().getStringExtra("channel_name");
        albumList_new = new ArrayList<>();
        adapter_new = new com.careerguide.MyCustomAdapter(this, albumList_new);
        recyclerView_new.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager_new = new LinearLayoutManager(this);
        mLayoutManager_new.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView_new.setLayoutManager(mLayoutManager_new);
        recyclerView_new.setAdapter(adapter_new);



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        adapter = new com.careerguide.adapters.LivesessionAdapter(this, albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getvideoSession();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000);

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

    private void prepareAlbums() {
        for(int i = 0; i<size;i++){
            String imgurl = "";
            Log.e("url in exo" , "-->" +counsellors.get(i).getVideourl());
            Album a = new Album(counsellors.get(i).getFullName(), counsellors.get(i).title, counsellors.get(i).getImgurl() , counsellors.get(i).getVideourl() , counsellors , counsellors.get(i).getId());
            albumList.add(a);

        }
        adapter.notifyDataSetChanged();
    }

    public void listArray() {
        Log.e("#name" , "==> " +counsellors_new.get(0).picUrl);
        for(int i=0;i<size_new;i++){
            Log.e("#nameww" , "==> " +counsellors_new.get(0).picUrl);
            DataModels dm = new DataModels();
            Log.e("#name" , "==> " +counsellors_new.get(i).picUrl);
            dm.setRestaurantName(counsellors_new.get(i).firstName + " "+counsellors_new.get(i).lastName);
            dm.setImgSrc(counsellors_new.get(i).picUrl);
            dm.setchannelname(Channel_name);
            albumList_new.add(dm);
        }
        adapter_new.notifyDataSetChanged();
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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

    private void getLiveSession() {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Facebook_Live_video", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("all_coun_res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {
                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");

                        Log.e("lengthname--> " , "==> " +counsellorsJsonArray.length() );
                        for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                        {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("id");
                            String name = counselorJsonObject.optString("Name");
                            Log.e("name--> " , "==> " +name );
                            String img_url = counselorJsonObject.optString("img_url");
                            String title = counselorJsonObject.optString("title");
                            String video_url = counselorJsonObject.optString("video_url");
                            counsellors.add(new live_counsellor_session(id,name,img_url,video_url,title,""));
                        }
                        size = counsellors.size();
                        prepareAlbums();
                        Log.e("size " , "==> " +counsellors );
                     //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            }
        });
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }
    private void getvideoSession() {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "all_available_counsellors", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("all_coun_res", response);
                try {
                    getLiveSession();
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {
                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                        for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                        {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("co_id");
                            String firstName = counselorJsonObject.optString("first_name");
                            String lastName = counselorJsonObject.optString("last_name");
                            String picUrl = counselorJsonObject.optString("profile_pic");
                            String Videocall_channel_name = counselorJsonObject.optString("videocall_channel_name");
                            counsellors_new.add(new Counsellor(id,firstName,lastName,picUrl,Videocall_channel_name,"",4.5f,new ArrayList<String>()));

                            Log.e("#inside" ,"for" +picUrl);
                            Log.e("size live" , "==> " +counsellors_new.get(0).firstName);
                        }
                        size_new = counsellors_new.size();
                        Log.e("size live" , "==> " +size_new );
                        if(size_new>0){
                            listArray();
                        }
                       // Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            }
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
    }

}
