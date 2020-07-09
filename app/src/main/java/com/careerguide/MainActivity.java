package com.careerguide;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.careerguide.payment.PaymentActivity;
import com.careerguide.youtubeVideo.youtubeFeedDetail;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        if(googleApiAvailability.isGooglePlayServicesAvailable(activity) != ConnectionResult.SUCCESS)
        {
            //testing
            googleApiAvailability.makeGooglePlayServicesAvailable(activity);
        }

        //you can test notifications by sending dummy data by uncommenting
        //sendNotification("Topic: Testing ","Surabhi Dewra is Live now",null,"Exoplayer","4","http://app.careerguide.com/api/user_dir/5f058d1beb8591594199323.jpeg");
    }

    public void sendNotification(String title, String messageBody,  Bitmap image, String activity , String data_id,String imageUrl) {
        PendingIntent pendingIntent;


        if (activity!=null && activity.contains("youtubeFeedDetail")){
            Intent intent = new Intent(this, youtubeFeedDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.e("dataIdnoti" , "--> " +data_id);
            intent.putExtra("data_id", data_id);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        else{
            Intent intent = new Intent(this, exoplayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("data_id", data_id);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        /*Bitmap scaledBitmap;
        try {
            scaledBitmap = Bitmap.createScaledBitmap(image, 500, 200 , true);//if this fails use default image
        }catch (Exception e){
            e.printStackTrace();

            Bitmap temp= BitmapFactory.decodeResource(getResources(), R.mipmap.career_counsellors_in_india);
            scaledBitmap = Bitmap.createScaledBitmap(temp, 500, 200 , true);
        }*/


        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "my_channel_01";
        CharSequence name = "hii2";
        String description = "hiii";

        final RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remoteview_notification);

        remoteViews.setImageViewResource(R.id.remoteview_notification_icon, R.drawable.ic_stat_name);

        remoteViews.setTextViewText(R.id.remoteview_notification_headline, title);
        remoteViews.setTextViewText(R.id.remoteview_notification_short_message, messageBody);
        remoteViews.setTextViewText(R.id.remoteview_notification_time, ""+getTimeStamp());



        int importance = NotificationManager.IMPORTANCE_HIGH;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name,importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setContent(remoteViews)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(id)
                    .setContentIntent(pendingIntent);

            //NotificationManager notificationManager =
            //      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            //notificationManager.notify(10 /* ID of notification */, notificationBuilder.build());

            //     notification.setSmallIcon(R.drawable.icon_transperent);
            //    notification.setColor(getResources().getColor(R.color.notification_color));



            Notification notification = notificationBuilder.build();

// set big content view for newer androids
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                notification.bigContentView = remoteViews;
            }

            NotificationTarget notificationTarget;


            notificationTarget = new NotificationTarget(
                    getApplicationContext(),
                    R.id.remoteview_notification_icon,
                    remoteViews,
                    notification,
                    1);

            Glide
                    .with(getApplicationContext())
                    .asBitmap()
                    .load(imageUrl)
                    .into(notificationTarget);





        } else {
            //    notification.setSmallIcon(R.drawable.icon);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setContent(remoteViews)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(id)
                    .setContentIntent(pendingIntent);

            //NotificationManager notificationManager =
            //      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(10 /* ID of notification */, notificationBuilder.build());

            Notification notification = notificationBuilder.build();

// set big content view for newer androids
            if (android.os.Build.VERSION.SDK_INT >= 16) {
                notification.bigContentView = remoteViews;
            }

            NotificationTarget notificationTarget;


            notificationTarget = new NotificationTarget(
                    getApplicationContext(),
                    R.id.remoteview_notification_icon,
                    remoteViews,
                    notification,
                    1);

            Glide
                    .with(getApplicationContext())
                    .asBitmap()
                    .load(imageUrl)
                    .into(notificationTarget);


        }


    }

    String getTimeStamp() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        minute = minute.length() == 1 ? "0" + minute : minute;
        int hour = calendar.get(Calendar.HOUR);
        return (hour == 0 ? 12 : hour) + ":" + minute + " " + (calendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
    }





}
