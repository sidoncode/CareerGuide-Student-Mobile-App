package com.careerguide.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.HomeActivity;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Activity_class extends AppCompatActivity {
    Activity activity =this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
    }

    private void updateEducation(final String education) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "profile_update", response -> {
                progressDialog.dismiss();
                Log.e("update_pro_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    String msg = jsonObject.optString("msg");
                    if (status) {
                        Intent intent = new Intent(activity, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    }, error -> {
            progressDialog.dismiss();
                Toast.makeText(activity, VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("update_pro_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                params.put("education_level",education);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public void card_click_nine(View view) {
        Utility.setEducationUid(activity , "NINE");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"class 9th");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","NINE");
        intent.putExtra("cat_title","Class 9th");
        startActivity(intent);
    }

    public void card_click_ten(View view) {
        Utility.setEducationUid(activity , "TEN");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Class 10th");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","TEN");
        intent.putExtra("cat_title","Class 10th");
        startActivity(intent);
    }

    public void card_click_ele(View view) {
        Utility.setEducationUid(activity , "ELEVEN");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Class 11th");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","ELEVEN");
        intent.putExtra("cat_title","Class 11th");
        startActivity(intent);
    }

    public void card_click_twe(View view) {
        Utility.setEducationUid(activity , "TWELVE");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Class 12th");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","TWELVE");
        intent.putExtra("cat_title","Class 12th");
        startActivity(intent);
    }

    public void card_click_gra(View view) {
        Utility.setEducationUid(activity , "GRADUATE");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Graduates");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","GRADUATE");
        intent.putExtra("cat_title","Graduates");
        startActivity(intent);
    }

    public void card_click_postgra(View view) {
        Utility.setEducationUid(activity , "POSTGRA");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Postgraduates");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","POSTGRA");
        intent.putExtra("   cat_title","Postgraduates");
        startActivity(intent);
    }

    public void card_click_working(View view) {
        Utility.setEducationUid(activity , "WORKING");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Working Professional");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","WORKING");
        intent.putExtra("cat_title","Working Professional");
        startActivity(intent);
    }
}
