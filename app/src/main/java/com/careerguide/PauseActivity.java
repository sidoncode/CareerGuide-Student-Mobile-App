package com.careerguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PauseActivity extends AppCompatActivity {
    private Activity activity = this;
    private boolean finished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pause);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        TextView headingTextView = (TextView) findViewById(R.id.headingText);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionText);
        LinearLayout parentRelativeLayout = (LinearLayout) findViewById(R.id.parentRL);
        String color = getIntent().getStringExtra("color");
        int imageResource = getIntent().getIntExtra("imageResource",-1);
        String heading = getIntent().getStringExtra("heading");
        String description = getIntent().getStringExtra("description");
        finished = getIntent().getBooleanExtra("finished",false);
        //parentRelativeLayout.setBackgroundColor(Color.parseColor(color));
        imageView.setImageResource(imageResource);
        headingTextView.setText(heading);
        descriptionTextView.setText(description);
        if(finished)
        {
            ((Button)findViewById(R.id.continueButton)).setText("Download Report");
        }

        findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finished)
                {
                    generateReport();

                }
                else {
                    PauseActivity.super.onBackPressed();
                    //finish();
                }

            }
        });
    }

    /*private void generateReport() {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Generating report...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url = "https://devapi.careerguide.com/api/assessment_report";
        try
        {
            final JSONObject jsonBody = new JSONObject("{\"api_key\": \"D7DC21B2-2G71-4CEE-950F-0019675AB74B\", \"test_type\": \"ideal\",\"user_auth\": \"m0bESYfssIk%3d-NDVkZGRhM2QtY2QzZS00ZDQ0LWIzNDctODRiODY1Y2E4NzI1\"}");
            JSONArray jsonArray = new JSONArray();
            for(QuestionAndOptions questionAndOptions : Utility.questionAndOptionses)
            {
                int sno = questionAndOptions.getQuestion().getSrNo();
                String key = questionAndOptions.getAnswerKey();
                JSONObject object1 = new JSONObject();
                object1.put("sno",sno);
                object1.put("key",key);
                jsonArray.put(object1);
            }
            jsonBody.put("questions",jsonArray);
            Log.e("json response", jsonBody.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    progressDialog.dismiss();
                    Log.e("Response",response.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("json response", VoleyErrorHelper.getMessage(error,activity));

                }
            });
            VolleySingleton.getInstance(activity).addToRequestQueue(request);
            progressDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    private void generateReport() {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Generating report...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String url = "https://devapi.careerguide.com/api/assessment_report";
        String auth = getIntent().getStringExtra("auth");
        try
        {
            final JSONObject jsonBody = new JSONObject("{\"api_key\":\"D7DC21B2-2G71-4CEE-950F-0019675AB74B\",\"test_type\":\"ideal\",\"user_auth\":" + auth + /*\"m0bESYfssIk%3d-NDVkZGRhM2QtY2QzZS00ZDQ0LWIzNDctODRiODY1Y2E4NzI1\"*/ ",\"questions\":[{\"sno\":1,\"key\":\"B\"},{\"sno\":2,\"key\":\"B\"},{\"sno\":3,\"key\":\"B\"},{\"sno\":4,\"key\":\"B\"},{\"sno\":5,\"key\":\"B\"},{\"sno\":6,\"key\":\"B\"},{\"sno\":7,\"key\":\"B\"},{\"sno\":8,\"key\":\"B\"},{\"sno\":9,\"key\":\"B\"},{\"sno\":10,\"key\":\"B\"},{\"sno\":11,\"key\":\"B\"},{\"sno\":12,\"key\":\"B\"},{\"sno\":13,\"key\":\"B\"},{\"sno\":14,\"key\":\"B\"},{\"sno\":15,\"key\":\"B\"},{\"sno\":16,\"key\":\"B\"},{\"sno\":17,\"key\":\"B\"},{\"sno\":18,\"key\":\"B\"},{\"sno\":19,\"key\":\"B\"},{\"sno\":20,\"key\":\"B\"},{\"sno\":21,\"key\":\"B\"},{\"sno\":22,\"key\":\"B\"},{\"sno\":23,\"key\":\"B\"},{\"sno\":24,\"key\":\"B\"},{\"sno\":25,\"key\":\"B\"},{\"sno\":26,\"key\":\"B\"},{\"sno\":27,\"key\":\"B\"},{\"sno\":28,\"key\":\"B\"},{\"sno\":29,\"key\":\"B\"},{\"sno\":30,\"key\":\"B\"},{\"sno\":31,\"key\":\"B\"},{\"sno\":32,\"key\":\"B\"},{\"sno\":33,\"key\":\"B\"},{\"sno\":34,\"key\":\"B\"},{\"sno\":35,\"key\":\"B\"},{\"sno\":36,\"key\":\"B\"},{\"sno\":37,\"key\":\"B\"},{\"sno\":38,\"key\":\"B\"},{\"sno\":39,\"key\":\"B\"},{\"sno\":40,\"key\":\"B\"},{\"sno\":41,\"key\":\"B\"},{\"sno\":42,\"key\":\"B\"},{\"sno\":43,\"key\":\"B\"},{\"sno\":44,\"key\":\"B\"},{\"sno\":45,\"key\":\"B\"},{\"sno\":46,\"key\":\"B\"},{\"sno\":47,\"key\":\"B\"},{\"sno\":48,\"key\":\"B\"},{\"sno\":49,\"key\":\"B\"},{\"sno\":50,\"key\":\"B\"},{\"sno\":51,\"key\":\"B\"},{\"sno\":52,\"key\":\"B\"},{\"sno\":53,\"key\":\"B\"},{\"sno\":54,\"key\":\"B\"},{\"sno\":55,\"key\":\"B\"},{\"sno\":56,\"key\":\"B\"},{\"sno\":57,\"key\":\"B\"},{\"sno\":58,\"key\":\"D\"},{\"sno\":59,\"key\":\"B\"},{\"sno\":60,\"key\":\"B\"},{\"sno\":61,\"key\":\"B\"},{\"sno\":62,\"key\":\"B\"},{\"sno\":63,\"key\":\"B\"},{\"sno\":64,\"key\":\"B\"},{\"sno\":65,\"key\":\"B\"},{\"sno\":66,\"key\":\"B\"},{\"sno\":67,\"key\":\"B\"},{\"sno\":68,\"key\":\"B\"},{\"sno\":69,\"key\":\"B\"},{\"sno\":70,\"key\":\"B\"},{\"sno\":71,\"key\":\"D\"},{\"sno\":72,\"key\":\"B\"},{\"sno\":73,\"key\":\"B\"},{\"sno\":74,\"key\":\"B\"},{\"sno\":75,\"key\":\"B\"},{\"sno\":76,\"key\":\"B\"},{\"sno\":77,\"key\":\"B\"},{\"sno\":78,\"key\":\"B\"},{\"sno\":79,\"key\":\"B\"},{\"sno\":80,\"key\":\"B\"},{\"sno\":81,\"key\":\"B\"},{\"sno\":82,\"key\":\"B\"},{\"sno\":83,\"key\":\"B\"},{\"sno\":84,\"key\":\"B\"},{\"sno\":85,\"key\":\"B\"},{\"sno\":86,\"key\":\"B\"},{\"sno\":87,\"key\":\"B\"},{\"sno\":88,\"key\":\"B\"},{\"sno\":89,\"key\":\"D\"},{\"sno\":90,\"key\":\"B\"},{\"sno\":91,\"key\":\"C\"},{\"sno\":92,\"key\":\"B\"},{\"sno\":93,\"key\":\"B\"},{\"sno\":94,\"key\":\"B\"},{\"sno\":95,\"key\":\"B\"},{\"sno\":96,\"key\":\"B\"},{\"sno\":97,\"key\":\"B\"},{\"sno\":98,\"key\":\"C\"},{\"sno\":99,\"key\":\"B\"},{\"sno\":100,\"key\":\"B\"},{\"sno\":101,\"key\":\"B\"},{\"sno\":102,\"key\":\"B\"},{\"sno\":103,\"key\":\"B\"},{\"sno\":104,\"key\":\"B\"},{\"sno\":105,\"key\":\"B\"},{\"sno\":106,\"key\":\"B\"},{\"sno\":107,\"key\":\"B\"},{\"sno\":108,\"key\":\"B\"},{\"sno\":109,\"key\":\"B\"},{\"sno\":110,\"key\":\"B\"},{\"sno\":111,\"key\":\"B\"},{\"sno\":112,\"key\":\"B\"},{\"sno\":113,\"key\":\"B\"},{\"sno\":114,\"key\":\"B\"},{\"sno\":115,\"key\":\"B\"},{\"sno\":116,\"key\":\"B\"},{\"sno\":117,\"key\":\"B\"},{\"sno\":118,\"key\":\"B\"},{\"sno\":119,\"key\":\"B\"},{\"sno\":120,\"key\":\"B\"},{\"sno\":121,\"key\":\"B\"},{\"sno\":122,\"key\":\"B\"},{\"sno\":123,\"key\":\"B\"},{\"sno\":124,\"key\":\"B\"},{\"sno\":125,\"key\":\"B\"},{\"sno\":126,\"key\":\"B\"},{\"sno\":127,\"key\":\"B\"},{\"sno\":128,\"key\":\"B\"},{\"sno\":129,\"key\":\"B\"},{\"sno\":130,\"key\":\"B\"},{\"sno\":131,\"key\":\"B\"},{\"sno\":132,\"key\":\"B\"},{\"sno\":133,\"key\":\"B\"},{\"sno\":134,\"key\":\"B\"},{\"sno\":135,\"key\":\"B\"},{\"sno\":136,\"key\":\"B\"},{\"sno\":137,\"key\":\"B\"},{\"sno\":138,\"key\":\"B\"},{\"sno\":139,\"key\":\"B\"},{\"sno\":140,\"key\":\"B\"},{\"sno\":141,\"key\":\"B\"},{\"sno\":142,\"key\":\"B\"},{\"sno\":143,\"key\":\"B\"},{\"sno\":144,\"key\":\"B\"},{\"sno\":145,\"key\":\"B\"},{\"sno\":146,\"key\":\"B\"},{\"sno\":147,\"key\":\"B\"},{\"sno\":148,\"key\":\"B\"},{\"sno\":149,\"key\":\"B\"},{\"sno\":150,\"key\":\"B\"},{\"sno\":151,\"key\":\"B\"},{\"sno\":152,\"key\":\"B\"},{\"sno\":153,\"key\":\"B\"},{\"sno\":154,\"key\":\"B\"},{\"sno\":155,\"key\":\"B\"},{\"sno\":156,\"key\":\"B\"},{\"sno\":157,\"key\":\"B\"},{\"sno\":158,\"key\":\"B\"},{\"sno\":159,\"key\":\"B\"},{\"sno\":160,\"key\":\"B\"},{\"sno\":161,\"key\":\"B\"},{\"sno\":162,\"key\":\"B\"},{\"sno\":163,\"key\":\"B\"},{\"sno\":164,\"key\":\"B\"},{\"sno\":165,\"key\":\"B\"},{\"sno\":166,\"key\":\"B\"},{\"sno\":167,\"key\":\"B\"},{\"sno\":168,\"key\":\"B\"},{\"sno\":169,\"key\":\"B\"},{\"sno\":170,\"key\":\"B\"},{\"sno\":171,\"key\":\"B\"},{\"sno\":172,\"key\":\"B\"},{\"sno\":173,\"key\":\"B\"},{\"sno\":174,\"key\":\"B\"},{\"sno\":175,\"key\":\"B\"},{\"sno\":176,\"key\":\"B\"},{\"sno\":177,\"key\":\"B\"},{\"sno\":178,\"key\":\"B\"},{\"sno\":179,\"key\":\"B\"},{\"sno\":180,\"key\":\"B\"},{\"sno\":181,\"key\":\"B\"},{\"sno\":182,\"key\":\"B\"}]}");
            JSONArray jsonArray = new JSONArray();
            for(QuestionAndOptions questionAndOptions : Utility.questionAndOptionses)
            {
                int sno = questionAndOptions.getQuestion().getSrNo();
                String key = questionAndOptions.getAnswerKey();
                JSONObject object1 = new JSONObject();
                object1.put("sno",sno);
                object1.put("key",key);
                jsonArray.put(object1);
            }
            jsonBody.remove("questions");
            jsonBody.put("questions",jsonArray);
            Log.e("json query", jsonBody.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String url1 = response.optString("pdf_url");

                  //  report_url = url;
                //    Log.e("#globalreport_url", "urlis " +report_url);
                    Log.e("#usermail", "mail:" +Utility.getUserEmail(activity));
                    progressDialog.dismiss();
                    finish();
                    save_report_url(url1);
                    get_report_url();

                }
            },1500), new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();

                }
            })
            {
                @Override
                protected Response<JSONObject> parseNetworkResponse(final NetworkResponse response) {
                    try {
                        String jsonString =
                                null;
                        jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        //Log.e("String response",jsonString);
                        final JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jsonObject = (jsonArray.getJSONObject(0));
                        return Response.success(jsonObject,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return Response.error(new ParseError(e));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return Response.error(new ParseError(e));
                    }
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(request,120 * 1000,3);
            progressDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        //if(finished)
        {
            //super.onBackPressed();
            get_report_url();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.handleOnlineStatus(this, "idle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.handleOnlineStatus(null,"");
    }

    private void save_report_url(final String report_url){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "save_report_url", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.e("222save_report_url", response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("save_report_url_error","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                Log.e("sendurl" , "url: " +report_url);
                params.put("report_url",report_url);
                params.put("email" , Utility.getUserEmail(activity));
               // String name = "Andorra";
               // params.put("name" , name);
                Log.e("#nline_status_request",params.toString());
                Log.e("userreport" , Utility.getReportUrl(activity));
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }
    private void get_report_url(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "get_report_url", new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {
                                    JSONObject jobj = null;
                                    try {
                                        jobj = new JSONObject(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.e("222save_report_url", response);
                                    String test = "";
                                    String reporturl = jobj.optString("Report_url");
                                    Log.e("#homeurl","report " +reporturl);
                                        Intent intent = new Intent(activity, WebViewActivity.class);
                                        intent.putExtra("url", reporturl);
                                         intent.putExtra("filename", "Report");
                                        Log.e("HomeResponse", reporturl);
                                        startActivity(intent);

                                }
                            }, new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("save_report_url_error","error");
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String,String> params = new HashMap<>();
                                    params.put("email" , Utility.getUserEmail(activity));
                                    Log.e("#line_status_request",params.toString());
                                    return params;
                                }
                            };
                            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

}
