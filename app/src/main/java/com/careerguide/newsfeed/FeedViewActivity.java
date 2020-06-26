package com.careerguide.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.HomeActivity;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedViewActivity extends AppCompatActivity {
    private String dlink, dlink1, dlink2, dlink3,url,title;
    String rew,numref,name;
    String androidId;
    WebView webView;
    Activity activity;
    SpinKitView progressBar;
    //@BindView(R.id.id_toolbar)
    Toolbar id_toolbar;
    ImageView share;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_view);
        //ButterKnife.bind(this);
        activity=this;
        id_toolbar=findViewById(R.id.id_toolbar11);
        id_toolbar.setTitle("News");
        id_toolbar.setNavigationIcon(R.drawable.ic_back);
        id_toolbar.setNavigationOnClickListener(v -> onBackPressed());
        share=(ImageView) findViewById(R.id.article_share1);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharee(url);
            }
        });

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.spin_kit_wv);
        webView = (WebView) findViewById(R.id.web_view_1);
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            dlink = deepLink.toString().substring(28);
                            dlink1 = dlink.substring(0, dlink.indexOf('/'));
                            dlink2 = dlink.substring(dlink.indexOf('/')+1,dlink.lastIndexOf('/'));
                            dlink3= dlink.substring(dlink.lastIndexOf('/')+1);
                            Log.e("NewsFeedActivity", "onSuccess: " + deepLink + " " + dlink1 + " " + dlink2 + " " + dlink3);
                            url=dlink2;
                            Log.e("TAG", "onSuccess: "+url);
                            rewd();
                            load();
                            /*Bundle args = new Bundle();
                            args.putString("url",dlink2);
                            navController= Navigation.findNavController(getParent(),R.id.nav_host_fragment_container);
                            navController.popBackStack();
                            navController.navigate(R.id.nav_to_feedViewFragment,args);*/
                            //startActivity(new Intent(HomeActivity.class, FeedViewActivity.class).putExtra("news_url", dlink2));
                        }
                        else
                        {
                            url = getIntent().getStringExtra("news_url");
                            title=getIntent().getStringExtra("title");
                            load();
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MainActivity", "getDynamicLink:onFailure", e);
                    }
                });



    }
    public void sharee(String url) {
        String img;
        androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(Utility.getRefImg(activity).equals("")) {
            imageUri = null;
            try {
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                        BitmapFactory.decodeResource(getResources(), R.drawable.prizesshare), null, null));
            } catch (NullPointerException e) {
            }
            img=imageUri.toString();
            Utility.setRefImg(img,activity);
        }
        else
            img=Utility.getRefImg(activity);
        Toast.makeText(this,"Opening apps...",Toast.LENGTH_LONG).show();
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.careerguide.com/"+ Utility.getUserId(this)+"/"+ url +"/"+androidId))
                .setDomainUriPrefix("https://careerguidestudentarticle.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("articles")
                                .setMedium("anyone")
                                .setCampaign("example-article")
                                .build())
                // Open links with com.example.ios on iOS
                // .setIosParameters(new DynamicLink.IosParameters.Builder("com.careerguide.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("TAG", "sharearticle: " + dynamicLink.getUri());

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(dynamicLinkUri.toString()))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("TAG", "onComplete: " + shortLink);

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(img) );
                            shareIntent.putExtra(Intent.EXTRA_TEXT,
                                    "*"+title+"*\n"+
                                            "Read the Complete Article:\n"+
                                            dynamicLinkUri+"\n\n"+
                                            "It's real\n" +
                                            "  ✅ Register to get ₹ 10 instantly for free!\n" +
                                            "  ✅ Check in Daily to withdraw Cash\n" +
                                            "  ✅ Earn Upto Rs ₹ 1000/day\n" +
                                            "\n" +
                                            "\uD83D\uDC47 Download CareerGuide App now to join now! \uD83D\uDC47\n"
                                            + Utility.getRefId(activity));
                            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                        } else {
                            Log.e("TAG", "onComplete: error" + task.getException());
                            // Error
                            // ...
                        }
                    }
                });
    }
    private void rewd()
    {
        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(!androidId.equals(dlink3) && !dlink1.equals(Utility.getUserId(this)))
        {
            StringRequest stringRequest2=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "fetch_rewards_point", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("rewards_response", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.optBoolean("status",false);
                        JSONArray userJsonObject = jsonObject.optJSONArray("reward_point");
                        JSONObject jbj = userJsonObject.optJSONObject(0);
                        rew=jbj.optString("rewards_point");
                        numref=jbj.optString("reward_number");
                        name=jbj.optString("name");
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "UpdateRewards", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("sender_rewards_response", response);
                            }
                        }, new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                                Log.e("rewards_error","error");

                            }
                        })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> params = new HashMap<>();
                                params.put("user_id",dlink1);
                                params.put("rewards_point", String.valueOf(Integer.parseInt(rew)+5));
                                params.put("reward_number", numref);
                                params.put("name", name);
                                Log.e("request",params.toString());
                                return params;
                            }
                        };
                        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);


                    } catch (JSONException j) {

                    }
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(activity, VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                    Log.e("rewards_error","error");

                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("userId", dlink1 );
                    Log.e("request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest2);
        }
    }

    private void load()
    {


        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(
                url);




        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webView.getProgress() == 100) {
                    progressBar.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
