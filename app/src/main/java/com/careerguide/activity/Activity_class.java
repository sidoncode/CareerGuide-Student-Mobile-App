package com.careerguide.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.HomeActivity;
import com.careerguide.MainActivity;
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
    private final static int SUB_CAT_REQ = 101;

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
        startActivityForResult(intent,SUB_CAT_REQ);
    }

    public void card_click_ten(View view) {
        Utility.setEducationUid(activity , "TEN");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Class 10th");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","TEN");
        intent.putExtra("cat_title","Class 10th");
        startActivityForResult(intent,SUB_CAT_REQ);
    }

    public void card_click_ele(View view) {
        Utility.setEducationUid(activity , "ELEVEN");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Class 11th");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","ELEVEN");
        intent.putExtra("cat_title","Class 11th");
        startActivityForResult(intent,SUB_CAT_REQ);
    }

    public void card_click_twe(View view) {
        Utility.setEducationUid(activity , "TWELVE");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Class 12th");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","TWELVE");
        intent.putExtra("cat_title","Class 12th");
        startActivityForResult(intent,SUB_CAT_REQ);
    }

    public void card_click_gra(View view) {
        Utility.setEducationUid(activity , "GRADUATE");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Graduates");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","GRADUATE");
        intent.putExtra("cat_title","Graduates");
        startActivityForResult(intent,SUB_CAT_REQ);
    }

    public void card_click_postgra(View view) {
        Utility.setEducationUid(activity , "POSTGRA");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Postgraduates");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","POSTGRA");
        intent.putExtra("   cat_title","Postgraduates");
        startActivityForResult(intent,SUB_CAT_REQ);
    }

    public void card_click_working(View view) {
        Utility.setEducationUid(activity , "WORKING");
        Intent intent = new Intent(activity, SubcategoryActivity.class);
        intent.putExtra("parent_cat_title" ,"Working Professional");
        intent.putExtra("type","updatecat");
        intent.putExtra("cat_uid","WORKING");
        intent.putExtra("cat_title","Working Professional");
        startActivityForResult(intent,SUB_CAT_REQ);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SUB_CAT_REQ && resultCode == RESULT_OK && data!=null)
        {

            Utility.setUserEducation(this,data.getStringExtra("parent_cat_title"));
            Utility.setEducationUid(this, data.getStringExtra("subcat_uid"));
            Utility.setIcon_url(this,data.getStringExtra("icon_url"));

            Intent homeIntent = new Intent(Activity_class.this,HomeActivity.class);
            homeIntent.putExtra("parent_cat_title",data.getStringExtra("parent_cat_title"));
            homeIntent.putExtra("subcat_uid",data.getStringExtra("subcat_uid"));
            homeIntent.putExtra("icon_url",data.getStringExtra("icon_url"));
            startActivity(homeIntent);
            finish();
        }
    }
}
