package com.careerguide.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.careerguide.*;
import com.careerguide.adapters.GoalAdapter;
import com.careerguide.VolleySingleton;
import com.careerguide.models.Goal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Fetch_category", new Response.Listener<String>()
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
                }
        }, error -> {
            Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        });

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public void cancel(View v){
        finish();
    }

}
