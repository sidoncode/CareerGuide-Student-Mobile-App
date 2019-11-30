package com.careerguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity_live_counsellor extends AgoraBaseActivity {

    private RecyclerView recyclerView;
    private AlbumsAdapter_live_counsellor adapter;
    private List<Album_live_counsellor> albumList;
    Activity activity = this;
    private String Firstname;
    private String Lastname;
    private String City;
    private String counsellorpic;
    private String Education_level;
    private String Channel_name;
    private ArrayList<Counsellor> counsellors = new ArrayList<>();
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_counsellor);
        Firstname = getIntent().getStringExtra("Firstname");
        Lastname = getIntent().getStringExtra("Lastname");
        Channel_name = getIntent().getStringExtra("channel_name");
        counsellorpic = getIntent().getStringExtra("counsellorpic");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Live Counsellors");

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter_live_counsellor(this, albumList);

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
        getCounsellors();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 1000);

        try {
            Glide.with(this).load(R.drawable.counsellor_join).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void prepareAlbums() {
        for(int i = 0; i<size;i++){
            Log.e("infor" , "--> " +counsellors.get(i).getFirstName() );
            String imgurl = counsellors.get(i).getPicUrl();
            Album_live_counsellor a = new Album_live_counsellor(counsellors.get(i).getFirstName() +" "+ counsellors.get(i).getLastName(), "", counsellors.get(i).getPicUrl() , Channel_name);
            albumList.add(a);

        }
        adapter.notifyDataSetChanged();
    }

    public void cardclick(View view) {
//        Log.e("inside" , "inside");
//        Intent intent = new Intent(activity, ViewerLiveActivity.class);
//        intent.putExtra("Fname" , Firstname);
//        intent.putExtra("Lname" , Lastname);
//        intent.putExtra("Channel_name" , Channel_name);
//        Log.e("#inside_live_counsellor", "test" +Channel_name);
//        startActivity(intent);
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getCounsellors() {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "all_available_counsellors", new Response.Listener<String>()
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
                            for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                            {
                                JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                                String id = counselorJsonObject.optString("co_id");
                                String firstName = counselorJsonObject.optString("first_name");
                                String lastName = counselorJsonObject.optString("last_name");
                                String picUrl = counselorJsonObject.optString("profile_pic");
                                String Videocall_channel_name = counselorJsonObject.optString("videocall_channel_name");
                                counsellors.add(new Counsellor(id,firstName,lastName,picUrl,Videocall_channel_name,"",4.5f,new ArrayList<String>()));
                            }
                            size = counsellors.size();
                            prepareAlbums();
                            Log.e("size " , "==> " +size );
                            Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
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
