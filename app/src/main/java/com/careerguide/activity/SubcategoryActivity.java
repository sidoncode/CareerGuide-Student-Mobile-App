package com.careerguide.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.careerguide.models.Subcategories;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rd.utils.DensityUtils.dpToPx;

public class SubcategoryActivity extends AppCompatActivity {
    private List<Subcategories> submodel;
    private com.careerguide.adapters.subcategoryAdapter adapter;
    Activity activity = this;
    ProgressBar pb_loading;
    TextView tv_title;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);
        pb_loading = findViewById(R.id.pb_loading);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getIntent().getStringExtra("cat_title"));
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        submodel = new ArrayList<>();
        adapter = new com.careerguide.adapters.subcategoryAdapter(this, submodel);
        Utility.setIcon_url(activity , getIntent().getStringExtra("icon_url"));
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SubcategoryActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        fetchContent();
    }

    public void fetchContent(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Fetch_sub_category", response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                pb_loading.setVisibility(View.GONE);
                if (status)
                {
                    JSONArray category = jsonObject.optJSONArray("category");
                    Log.e("lengthname--> " , "==> " +category.length() );

                    for (int i = 0; category != null && i<category.length(); i++)
                    {
                        Log.e("#lengthname--> " , "==> " +category );
                        JSONObject categoryJsonObject = category.optJSONObject(i);
                        String uid = categoryJsonObject.getString("uid");
                        Log.e("#uid" , "==> "+uid);
                        String name = categoryJsonObject.getString("category");
                        String video_count = categoryJsonObject.getString("video_count");
                        Log.e("#categoryJsonObject" , "==> "+categoryJsonObject);
                        submodel.add(new Subcategories(uid, name , getIntent().getStringExtra("cat_title") , video_count , getIntent().getStringExtra("icon_url")));
                        adapter.notifyDataSetChanged();
                    }
                    Log.e("#name" , "==> ");
                    //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                } else {
                    Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(activity, VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                Log.e("#cat-title" , "-->" +getIntent().getStringExtra("cat_title"));
                params.put("cat_uid" ,getIntent().getStringExtra("cat_uid"));
                Log.e("#line_status_request",params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public void backpress(View view){
        finish();
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
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

}
