package com.careerguide.Book_Appoinment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.HomeActivity;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.careerguide.models.Counsellor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookCounsellor extends AppCompatActivity {
    private ArrayList<Counsellor> Counsellors_profile = new ArrayList<>();
    private int Counsellors_profile_size;
    private BookCounsellorAdapter counsellor_adapter;
    private List<Counsellor> counsellorlist;
    ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_counsellor);
        pb_loading = findViewById(R.id.pb_loading);
        RecyclerView recycler_counsellor = findViewById(R.id.recycler_counsellor);
        counsellorlist = new ArrayList<>();
        counsellor_adapter = new BookCounsellorAdapter(this, counsellorlist);
        recycler_counsellor.setLayoutManager(new LinearLayoutManager(this));
        recycler_counsellor.setAdapter(counsellor_adapter);
        GetCounsellor();
    }

    private void prepareAlbums() {

        for (int i =0 ; i<Counsellors_profile_size ; i++){
            Log.e("name in prepare" , "-->" +Counsellors_profile.get(i).getUsername());
            Counsellor counsellor_model = new Counsellor(Counsellors_profile.get(i).getUid() , Counsellors_profile.get(i).getUsername() , Counsellors_profile.get(i).getFirst_name(),Counsellors_profile.get(i).getLast_name(),Counsellors_profile.get(i).getAvatar(),23);
            counsellorlist.add(counsellor_model);
        }
        counsellor_adapter.notifyDataSetChanged();
    }

    public void GetCounsellor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "category_counsellors", response -> {
            Log.e("all_coun_res_counsellor", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status) {
                    JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                    for (int i = 0; counsellorsJsonArray != null && i < counsellorsJsonArray.length(); i++) {
                        JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                        String id = counselorJsonObject.optString("co_id");
                        String firstName = counselorJsonObject.optString("first_name");
                        String lastName = counselorJsonObject.optString("last_name");
                        String picUrl = counselorJsonObject.optString("profile_pic");
                        String email = counselorJsonObject.optString("email");
                        Counsellors_profile.add(new Counsellor(id, email, firstName, lastName, picUrl, 27));
                    }
                    Counsellors_profile_size = Counsellors_profile.size();
                    prepareAlbums();
                    Log.e("size ", "==> " + Counsellors_profile_size);
                    // Log.e("size1 ", "==> " + counsellors.get(0).getPicUrl());
                } else {


                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
                pb_loading.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            pb_loading.setVisibility(View.GONE);
            Toast.makeText(this, VoleyErrorHelper.getMessage(error, this), Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror", "error");
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                //params.put("user_education", Utility.getUserEducationUid(getActivity()));
                Log.e("all_coun_req", params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



    public void back_onClick(View view) {
        finish();
    }
}
