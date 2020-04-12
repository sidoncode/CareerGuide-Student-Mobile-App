package com.careerguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AssessmentActivity extends AppCompatActivity {

    private Activity activity = this;
    //private ArrayList<QuestionAndOptions> questionAndOptionses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstnceState)
    {
        super.onCreate(savedInstnceState);
        setContentView(R.layout.activity_assessment);
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        final String auth = getIntent().getStringExtra("auth");
        String url = "https://devapi.careerguide.com/api/assessment_questions";
        try
        {
            final JSONObject jsonBody = new JSONObject("{\"api_key\": \"D7DC21B2-2G71-4CEE-950F-0019675AB74B\", \"test_type\": \"ideal\",\"user_auth\": \"" + auth + /*m0bESYfssIk%3d-NDVkZGRhM2QtY2QzZS00ZDQ0LWIzNDctODRiODY1Y2E4NzI1*/"\"}");
            Log.e("json body", jsonBody.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> {
                progressDialog.dismiss();
                try {
                    ((TextView) findViewById(R.id.textView)).setText(response.getJSONArray("passages").toString());
                    JSONArray questionJsonArray = response.getJSONArray("questions");
                    JSONArray pessageJsonArray = response.getJSONArray("passages");
                    for (int i = 0; i< questionJsonArray.length();i++)
                    {
                        JSONObject questionJsonObject = questionJsonArray.getJSONObject(i);
                        //Log.e("Question", questionJsonObject.toString());
                        String title = questionJsonObject.getString("title");
                        title = title.replace("â\u0080\u0099","'");
                        title = title.replace("Ã·","\u00F7");
                        title = title.replace("â\u0080¦",".");
                        title = title.replace("â\u0080","-");
                        String section = questionJsonObject.getString("section");
                        int srNo = questionJsonObject.getInt("sno");
                        String type = questionJsonObject.optString("type");
                        String passageRef = questionJsonObject.optString("passage_ref");
                        JSONArray optionsJsonArray = questionJsonObject.getJSONArray("options");

                        if(!Utility.sectionSet.contains(section)) {
                            Utility.sectionSet.add(section);
                        }
                        ArrayList<Option> options = new ArrayList<>();
                        for (int j = 0; j<optionsJsonArray.length(); j++)
                        {
                            JSONObject optionJsonObject = optionsJsonArray.getJSONObject(j);
                            String key = optionJsonObject.getString("key");
                            String value = optionJsonObject.getString("value");
                            key = key.replace("â\u0080\u0099", "'");
                            int sno = optionJsonObject.getInt("sno");
                            Option option;
                            if(value.contains("<img src="))
                            {
                                option = new OptionImageBased(sno,key,value);
                            }
                            else
                            {
                                option = new OptionTextBased(sno,key,value);
                            }
                            options.add(option);

                            if (i == 91)
                            {
                                String s = "e of  376Ã·100";
                                //title = title.replace("You ", "").replace(" find difficult to talk about your feeling","");
                                Log.e("question",value);

                            }
                        }
                        Question question;
                        if (i == 97)
                        {
                            String s = "e of  376Ã·100";
                            //title = title.replace("You ", "").replace(" find difficult to talk about your feeling","");
                            Log.e("question",title + " vs \n" + title.replace("â\u0080\u0099","'"));

                        }
                        if(title.contains("<img src="))
                        {
                            question = new QuestionImageBased(section,srNo,type,passageRef,title,null);
                        }
                        else
                        {
                            question = new QuestionTextBased(section,srNo,type,passageRef,title);
                        }
                        Utility.questionAndOptionses.add(new QuestionAndOptions(question,options));
                    }
                    for (int i = 0; i<pessageJsonArray.length(); i++)
                    {
                        JSONObject passageJsonObject = pessageJsonArray.getJSONObject(i);
                        JSONArray paragraphJsonArray = passageJsonObject.getJSONArray("paragraphs");
                        for(int j = 0; j< paragraphJsonArray.length(); j++)
                        {
                            JSONObject paragraphJsonObject = paragraphJsonArray.getJSONObject(j);
                            String paragraph = paragraphJsonObject.getString("paragraph");
                            Utility.paragraphs.add(paragraph);
                            //Log.e("paragraph",paragraph);
                        }
                    }
                    /*String sections = "";
                    for(String string :Utility.sectionSet)
                    {
                        sections += string + "\n";
                    }
                    Log.e("total section", sections);*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    finish();
                }
                if(Utility.questionAndOptionses.size() > 0)
                {
                    Utility.setSavedAnswer(activity);
                    Intent intent = new Intent(activity,QuestionActivity.class);
                    intent.putExtra("auth",auth);
                    startActivity(intent);
                    finish();
                }
            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity, VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                finish();

            })
            {
                @Override
                protected Response<JSONObject> parseNetworkResponse(final NetworkResponse response) {
                    try {
                        String jsonString =
                                null;
                        jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        final JSONArray jsonArray = new JSONArray(jsonString);
                        JSONObject jsonObject = (jsonArray.getJSONObject(0));
                        return Response.success(jsonObject,
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException | JSONException e) {
                        e.printStackTrace();
                        return Response.error(new ParseError(e));
                    }
                }
            };

            if(Utility.questionAndOptionses.size() > 0 )
            {
                Utility.setSavedAnswer(activity);
                Intent intent = new Intent(activity, QuestionActivity.class);
                intent.putExtra("auth",auth);
                startActivity(intent);
                finish();
            }
            else
            {
                VolleySingleton.getInstance(activity).addToRequestQueue(request,10000,3);
                progressDialog.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
}
