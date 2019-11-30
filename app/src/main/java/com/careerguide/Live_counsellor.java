package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Live_counsellor extends AppCompatActivity {
    private String PRIVATE_SERVER = "http://app.careerguide.com/api/counsellor/";
    Activity activity = this;
    private String Firstname;
    private String Lastname;
    private String City;
    private String Profile_pic;
    private String Education_level;
    private String Channel_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_counsellor);
        save_report_url();
        findViewById(R.id.Joinlive).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, VideoChatViewActivity.class);
                intent.putExtra("channel_name" , Channel_name);
                Log.e("#inside_live_counsellor", Channel_name);
                startActivity(intent);
            }
        });
    }

    private void save_report_url(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PRIVATE_SERVER + "get_live_counsellor", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("live_counsellor", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Firstname = jobj.optString("first_name");
                Lastname = jobj.optString("last_name");

                if (Firstname == "null" || Lastname == "null"){
                    Log.e("inside" , "inekme");
                    final android.support.v7.app.AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    final View dialog = getLayoutInflater().inflate(R.layout.dialog_livecounsellor, null);
                    alertDialog.setView(dialog);
                    alertDialog.show();
                    dialog.findViewById(R.id.start_test).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(activity,PsychometricTestsActivity.class));
                            //  alertDialog.dismiss();
                        }
                    });
                }

                City = jobj.optString("city");
                Profile_pic = jobj.optString("profile_pic");
                Channel_name = jobj.optString("channel_name");
                Log.e("#channel_name", Channel_name);
                TextView TextView = (TextView)findViewById(R.id.Counsellor_name);
                TextView.setText(Firstname + " " + Lastname);
//                Intent intent = new Intent(activity, WebViewActivity.class);
//                intent.putExtra("url", reporturl);
//                startActivity(intent);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("live_counsellor_error","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("email" , Utility.getUserEmail(activity));
                // String name = "Andorra";
                // params.put("name" , name);
                Log.e("#nline_status_request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }


}


