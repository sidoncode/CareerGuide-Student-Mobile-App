package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.provider.Settings;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.List;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private Activity activity = this;
    private int cnt=0;
    private String dlink="",dlink1,did="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            dlink1=deepLink.toString().substring(28);
                            did=dlink1.substring(dlink1.indexOf('/')+1);
                            dlink=dlink1.substring(0,dlink1.indexOf('/'));
                            Log.e("MainActivity", "onSuccess: "+ deepLink+ " "+dlink+did );
                            cnt=1;
                            rewd();
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

        if(cnt==0)
            rewd();





    }
    private void rewd()
    {
        int interval = 1000;
        if (getIntent().getBooleanExtra("hideSplash",false))
        {
            interval = 1000;
        }
        Log.e("interval",interval + "");
        new Handler().postDelayed(() -> {
            Log.e("TAG", "onCreate: "+Utility.getUserId(activity) );
            if(Utility.getUserId(activity).equals(""))
            {
                //detectDeepLink();
                Log.e("TAG", "onCreate: "+dlink);
                String androidId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                if(did.equals(androidId))
                    startActivity(new Intent(activity, Onboarding.class).putExtra("refid", ""));
                else
                    startActivity(new Intent(activity, Onboarding.class).putExtra("refid", dlink));
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
            //testing
            googleApiAvailability.makeGooglePlayServicesAvailable(activity);
        }
    }
}
