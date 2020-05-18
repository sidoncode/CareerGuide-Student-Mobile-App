package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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
      //  FirebaseMessaging.getInstance().subscribeToTopic("youtube");
         FirebaseMessaging.getInstance().subscribeToTopic("mytest");
        Log.d("AndroidBash", "Subscribed");
        //Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();
        //https://s3-ap-southeast-1.amazonaws.com/fal-careerguide/id-la/67148.pdf
        /*String url  = "https://s3-ap-southeast-1.amazonaws.com/fal-careerguide/id-la/67148.pdf";
        Intent intent = new Intent(activity,WebViewActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
        finish();*/
        /*Intent intent = new Intent(activity,SplashActivity.class);
        startActivity(intent);
        finish();*/

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
