package com.careerguide.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.careerguide.*;
import com.careerguide.adapters.GoalAdapter;
import com.careerguide.VolleySingleton;
import com.careerguide.models.Goal;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoalsActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    RequestQueue requestQueue;
    LinearLayoutManager llm;
    List<Goal> goalList;
    GoalAdapter goalAdapter;
    Activity activity = this;
    TextView tv_title;
    MenuItem classimg;
    RecyclerView rv_goals;
    ShimmerFrameLayout shimmer_view_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        shimmer_view_container=findViewById(R.id.shimmer_view_container);

        requestQueue= Volley.newRequestQueue(this);
        goalList=new ArrayList<>();
        goalAdapter=new GoalAdapter(this,goalList);

        tv_title = findViewById(R.id.tv_title);
        rv_goals=findViewById(R.id.rv_goals);
//        iv_banner = findViewById(R.id.iv_banner);
//        Glide.with(this).load("https://ik.imagekit.io/careerguide/Artboard___39__1__hkEZKuebu5.png").into(iv_banner);
        llm=new LinearLayoutManager(this);
        rv_goals.setLayoutManager(llm);
        rv_goals.setAdapter(goalAdapter);
        rv_goals.setNestedScrollingEnabled(false);
        fetchContent();
    }

    public void fetchContent(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Fetch_category", response -> {
                Log.e("all_coun_res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {
                        JSONArray category = jsonObject.optJSONArray("category");
                        Log.e("lengthname--> " , "==> " +category.length() );
                        for (int i = 0; category != null && i<category.length(); i++)
                        {
                            JSONObject categoryJsonObject = category.optJSONObject(i);
                            String uid = categoryJsonObject.getString("uid");
                            String desc = categoryJsonObject.getString("description");
                            String cat_placeholder = categoryJsonObject.getString("cat_placeholder");
                            String name = categoryJsonObject.getString("name");
                            Log.e("#name" , "==> "+name);
                            String icon_url = categoryJsonObject.getString("icon_url");
                            goalList.add(new Goal(uid, name, icon_url,cat_placeholder));
                            goalAdapter.notifyDataSetChanged();

                        }
                        //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            shimmer_view_container.setVisibility(View.INVISIBLE);
            }, error -> {
            Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
            shimmer_view_container.setVisibility(View.INVISIBLE);
        });

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public void cancel(View v){
        finish();
    }

}
