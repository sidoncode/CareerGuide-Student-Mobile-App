package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.blog.DataMembers;
import com.careerguide.blog.model.Categories;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.careerguide.models.Counsellor;
import com.careerguide.youtubeVideo.CommonEducationModel;
import com.careerguide.youtubeVideo.Videos;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.List;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if(uri != null){
            Log.e("uri" , "--> "+uri);
            String new_uri = uri.getQuery();
            Log.e("#uri" , "--> "+new_uri);
            List<String> param  = uri.getPathSegments();
            Log.e("uri_size" , "--> "+param.size());
            for(int i=0 ; i<param.size() ; i++){
                String id = param.get(i);
                Log.e("###id" , "--> "+id);
            }
        }
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)  // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        if(googleApiAvailability.isGooglePlayServicesAvailable(activity) != ConnectionResult.SUCCESS)
        {
            googleApiAvailability.makeGooglePlayServicesAvailable(activity);
        }
        FirebaseApp.initializeApp(activity);
         //FirebaseMessaging.getInstance().subscribeToTopic("notification");//comment this for local testing
         FirebaseMessaging.getInstance().subscribeToTopic("devtest");//comment this when publishing the app to google
        Log.d("notification", "Subscribed");


        int interval = 800;
        if (getIntent().getBooleanExtra("hideSplash",false))
        {
            interval = 1;
        }
        Log.e("interval",interval + "");

        new Handler().postDelayed(() -> {
            if(Utility.getUserId(activity).equals(""))
            {
                startActivity(new Intent(activity, Onboarding.class));
                finish();
            }
            else
            {
                    startActivity(new Intent(activity, HomeActivity.class));
                    finish();
            }
        },interval);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        if(googleApiAvailability.isGooglePlayServicesAvailable(activity) != ConnectionResult.SUCCESS)
        {
            googleApiAvailability.makeGooglePlayServicesAvailable(activity);
        }
    }
}
