package com.careerguide;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.careerguide.activity.SeeAllActivity;
import com.careerguide.blog.DataMembers;
import com.careerguide.blog.activity.CatDetailActivity;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.careerguide.models.Counsellor;
import com.careerguide.newsfeed.FeedViewActivity;
import com.careerguide.newsfeed.FeedViewFragment;
import com.careerguide.youtubeVideo.CommonEducationModel;
import com.careerguide.youtubeVideo.Videos;
import com.google.android.exoplayer2.C;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavAction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.careerguide.activity.GoalsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.internal.Util;

import com.careerguide.blog.model.Categories;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.JsonArray;


public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener{

    //storage permission code
    private static final int PERMISSION_REQUEST_CODE = 1;

    public static final int REQUEST_CATEGORY_CODE = 201;
    private Activity activity = this;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private static CircleImageView profilePic;
    private static ImageView classimg;
    private static TextView nameTextView , tv_name;
    private static TextView locationTextView;
    private static View headerLayout;

    CGPlayListViewModel viewModelProvider;

    ProgressDialog progressDialog;
    BroadcastReceiver onDownloadComplete;
    long downloadID;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView navigationView;

    NavController navController;
    AppBarConfiguration mAppBarConfiguration;

    //private ActionBarDrawerToggle drawerToggle;
    private String mActivityTitle;
    private int posn=0;
    private String apiKey = "1befd9d8922089646809507c9b51320c61dfa227";


    private String browserKey = Utility.browserKey;


    ArrayList<CurrentLiveCounsellorsModel> finalList=new ArrayList<>();
    ArrayList<CurrentLiveCounsellorsModel> tempCurrentLiveCounsellorsList = new ArrayList<>();
    ArrayList<CurrentLiveCounsellorsModel> tempPostLiveCounsellorsList = new ArrayList<>();


    public void openchat(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        share();

        //Utility.setRewardPoints(activity,"0");
        StringRequest stringRequest1=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "fetch_rewards_point", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rewards_response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    if(jsonObject.optJSONArray("reward_point").length()!=0)
                    {
                        JSONArray userJsonObject = jsonObject.optJSONArray("reward_point");
                        JSONObject jbj = userJsonObject.optJSONObject(0);
                        String rew=jbj.optString("rewards_point");
                        String numref=jbj.optString("reward_number");
                        String name= jbj.optString("name");
                        Log.e("TAG", "onResponse: "+rew+" "+numref);
                        //countstreak(rew);
                        int flag=0;
                        if(numref.equals("null")) {
                            flag=1;
                            numref="0";
                        }
                        if(rew.equals("null")) {
                            flag=1;
                            rew="0";
                        }
                        if(name.equals("null") || !name.equals(Utility.getUserFirstName(activity)+Utility.getUserLastName(activity))) {
                            flag=1;
                            name=Utility.getUserFirstName(activity)+" "+Utility.getUserLastName(activity);
                        }
                        if(flag==1)
                            updatedata(Utility.getUserId(activity),name,rew,numref);
                        else
                        {
                            Utility.setRewardPoints(activity,rew);
                            Utility.setNumReferrals(activity, numref);
                        }
                    }
                    else {
                        // countstreak("0");
                        setdata(Utility.getUserId(activity), Utility.getUserFirstName(activity)+" "+Utility.getUserLastName(activity),"0","0");

                    }

                } catch (JSONException j) {

                }
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
                params.put("userId", Utility.getUserId(activity));
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest1);




        //countstreak();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog ( this);
        progressDialog.setTitle("Downloading...");
        progressDialog.setCancelable(false);

        tempCurrentLiveCounsellorsList.add(new CurrentLiveCounsellorsModel("Checking...","","","",""));

        viewModelProvider = new ViewModelProvider(HomeActivity.this).get(CGPlayListViewModel.class);

        viewModelProvider.setDisplaylistArrayLiveCounsellors(tempCurrentLiveCounsellorsList);

        executeAllTasks();

        /*

         * Tutorial
         * https://guides.codepath.com/android/fragment-navigation-drawer
         *
         * */
        mDrawer = findViewById(R.id.drawer_layout);
        //drawerToggle = setupDrawerToggle();
        //mDrawer.addDrawerListener(drawerToggle);

        navigationView = findViewById(R.id.navView);
        //setupDrawerContent(nvDrawer);

        headerLayout = navigationView.getHeaderView(0);
        profilePic = headerLayout.findViewById(R.id.profilePic);



        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_fragment, R.id.tests, R.id.report_url,
                R.id.counsellorSignUp,R.id.careerbooks,R.id.livecounsellor,
                R.id.videocallcounsellor,R.id.livesessions,R.id.Blog,
                R.id.about_fragment,R.id.contact_us,R.id.refer_a_friend,R.id.rate_us,
                R.id.counsellorCorner)
                .setDrawerLayout(mDrawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        NavigationUI.setupActionBarWithNavController(this, navController,mAppBarConfiguration);
        NavigationUI.setupWithNavController (toolbar, navController, mDrawer);
        NavigationUI.setupWithNavController(navigationView, navController);


        if(getIntent().getIntExtra("refer",0)==1) {
            Log.e("test", "onCreate: " );
            BottomNavigationView bnv=findViewById(R.id.bottom_navigation);
            bnv.setSelectedItemId(R.id.nav_account);
            navController.popBackStack();
            navController.navigate(R.id.nav_to_profileFragment);
        }
        if(getIntent().getStringExtra("RLB")!=null) {
            Log.e("test", "onCreate: " );
            BottomNavigationView bnv=findViewById(R.id.bottom_navigation);
            bnv.setSelectedItemId(R.id.nav_feed);
            navController.popBackStack();
            navController.navigate(R.id.nav_to_feedFragment);
        }
        onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (downloadID == id) {
                    Toast.makeText(HomeActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
                    startWebViewActivity();
                }
            }
        };
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.rate_us:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.careerguide")));
                    mDrawer.closeDrawers();
                    return true;

                case R.id.livecounsellor:
                    liveCounsellorWork();
                    mDrawer.closeDrawers();
                    return true;

                case R.id.report_url:
                    mDrawer.closeDrawers();
                    showReport();
                    return true;

                default:
                    NavigationUI.onNavDestinationSelected(
                            item,navController);
                    mDrawer.closeDrawers();
                    return true;
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() != R.id.class_cat)
                {
                    toolbar.setSubtitle("");
                }
            }
        });



        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.version)).setText(version.equals("")?"":"Version " + version);


        findViewById(R.id.leagal).setOnClickListener(v -> {
            /*FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.flContent, new TermsAndConditionFragment()).addToBackStack("Legal").commit();
            setTitle("Legal");*/
            //todo make changes
            navController.popBackStack(R.id.home_fragment,false);
            navController.navigate(R.id.action_home_fragment_to_termsAndConditionFragment);
            mDrawer.closeDrawers();
        });

        String picUrl = Utility.getUserPic(activity);
        Log.e("Picurl:","url: " + picUrl);
        if ((picUrl != null && picUrl.length() > 0))
        {
            Glide.with(this).load(picUrl).into(profilePic);
        }
        else
        {
            profilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(activity)).charAt(0),60));
            String initial = (Utility.getUserFirstName(activity)).charAt(0) + "" + (Utility.getUserLastName(activity)).charAt(0);
            ((TextView) headerLayout.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
        }

        nameTextView = headerLayout.findViewById(R.id.headerTextView);
        classimg = headerLayout.findViewById(R.id.iv_icon);
        tv_name = headerLayout.findViewById(R.id.tv_name);
        Log.e("###tv_name","-->" +getIntent().getStringExtra("subcat_title"));

        tv_name.setText(Utility.getUserEducation(activity));
        Glide.with(this).load(Utility.getIcon_url(activity)).into(classimg);

       /* if(getIntent().getStringExtra("parent_cat_title") == null){
        }else{
            tv_name.setText(getIntent().getStringExtra("parent_cat_title"));
            Utility.setEducationUid(activity , getIntent().getStringExtra("subcat_uid"));
            Log.e("uidis" , " " + getIntent().getStringExtra("subcat_uid"));
            updateProfile("education_level",getIntent().getStringExtra("parent_cat_title"),null,null,null);

        }
        if(getIntent().getStringExtra("icon_url") == null){
            Glide.with(this).load(Utility.getIcon_url(activity)).into(classimg);
            Log.e("edu" , "" +Utility.getUserEducation(this));
        }
        else{
            Glide.with(this).load(getIntent().getStringExtra("icon_url")).into(classimg);
        }

        */

        headerLayout.findViewById(R.id.class_cat).setOnClickListener(v -> startActivityForResult(new Intent(activity,GoalsActivity.class),REQUEST_CATEGORY_CODE));

        nameTextView.setText(Utility.getUserFirstName(activity) + " " + Utility.getUserLastName(activity) /*+ "   >"*/);
        locationTextView = headerLayout.findViewById(R.id.locationTextView);
        locationTextView.setText(Utility.getUserCity(activity));
        headerLayout.setOnClickListener(v -> {
            navController.popBackStack(R.id.home_fragment,false);
            navController.navigate(R.id.nav_to_profileFragment);
            mDrawer.closeDrawers();
        });

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, new CounsellorCornerFragment()).commit();
//        setTitle("Home");
        if(getIntent().getStringExtra("parent_cat_title") == null){
            //todo revert back bottom 2 lines
            /*FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new CGPlaylist()).commit();
            */
            //navController.navigate(R.id.nav_to_CGPlaylist);

            Log.e("Education_Level" , "-->" +Utility.getUserEducation(activity));
            setTitle("Home");
            mDrawer.closeDrawers();
        }

        /*findViewById(R.id.notification).setOnClickListener(v -> {
         *//*FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.popBackStack();
            fragmentManager1.beginTransaction().replace(R.id.flContent, new NotificationFragment()).addToBackStack("Notifications").commit();
            setTitle("Notifications");*//*
            navController.navigate(R.id.action_home_fragment_to_notificationFragment);
        });
        findViewById(R.id.setting).setOnClickListener(v -> startActivity(new Intent(activity, SettingActivity.class)));
   */
        registerBottomNavBar();
        registerGoogleFeedNotification();
        checkGoogleFeedNotification();

    }
    public int setPosn()
    {
        return posn;
    }
    /*public void countstreak(String rew)
    {
        Calendar calendar=Calendar.getInstance();
        String score;
        int date=calendar.get(Calendar.DATE);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        int day=calendar.get(Calendar.DAY_OF_WEEK)-1;
        Log.e("TAG", "countstreak: "+day );
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int lastdate=sharedPreferences.getInt("lastdate",0);
        if(day==0)
            day=7;
        Log.e("TAG", "countstreak: "+day);
        //String currlogin= date+" "+month+" "+year;
        //Log.e("TAG", "onCreate: "+ currlogin);
        score=Utility.getUserStreak(activity);
        Log.e("TAG", "countstreak: "+score);
        if(score.length()<day)
        {
            while(score.length()+1!=day)
                score=score+"0";
            score=score+"1";
            reward(rew,score);
        }
        else if(score.length()==7 && day!=7)
        {
            score="";
            for(int i=1;i<day;i++)
                score=score+"0";
            score=score+"1";
            reward(rew,score);
        }
        else if(score.length()>day)
        {
            score="";
            for(int i=1;i<day;i++)
                score=score+"0";
            score=score+"1";
            reward(rew,score);
        }
        else if(score.length()==day)
        {
            if(date!=lastdate)
            {
                score="";
                for(int i=1;i<day;i++)
                    score=score+"0";
                score=score+"1";
                reward(rew,score);
            }
        }


        Log.e("TAG", "countstreak: "+score );
        Utility.setUserStreak(activity, score);
        editor.putInt("lastdate",date).apply();
        Log.e("TAG", "lastdate: "+ lastdate );

    }
    private void reward(String rew, String score)
    {
        int cnt = 0;
        for (int i = 0; i < score.length(); i++) {
            if (score.charAt(i) == '1') {
                cnt++;
            } else {
                cnt = 0;
            }
        }
        final int cn=cnt;
        Log.e("TAG", "countstreak: "+rew+" "+cn );
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "reward_points", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rewards_response", response);
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
                params.put("userId",Utility.getUserId(activity));
                params.put("rewards_point", String.valueOf(Integer.parseInt(rew)+cn));
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }*/

    public void updatedata(String id, String name, String rew, String numref)
    {
        Utility.setNumReferrals(activity, numref);
        Utility.setRewardPoints(activity, rew);
        StringRequest stringRequest1=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "UpdateRewards", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("updatesetdata", response);
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
                params.put("user_id",id);
                params.put("rewards_point", rew);
                params.put("reward_number", numref);
                params.put("name", name);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest1);
    }
    public void setdata(String id, String name, String rew, String numref)
    {
        Utility.setNumReferrals(activity, numref);
        Utility.setRewardPoints(activity, rew);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "reward_points", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("setdata", response);
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
                params.put("userId",id);
                params.put("rewards_point", rew);
                params.put("rewards_number", numref);
                params.put("name", name);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);


        StringRequest stringRequest1=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "UpdateRewards", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("updatesetdata", response);
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
                params.put("user_id",id);
                params.put("rewards_point", rew);
                params.put("reward_number", numref);
                params.put("name", name);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest1);
    }

    private void checkGoogleFeedNotification() {

        Intent intent = getIntent();
        String url = intent.getStringExtra("feed_url");
        if(url != null){
            Bundle args = new Bundle();
            args.putString("url1",url);
            navController.popBackStack();
            navController.navigate(R.id.nav_to_feedFragment,args);
        }else{
            Log.d("Google News Feed", "Intent was null");

        }}


    BottomNavigationView bnv;
    private void registerBottomNavBar() {
        bnv=findViewById(R.id.bottom_navigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.nav_counsellor)
                {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_counsellor_profiles);
                }

                if(menuItem.getItemId()==R.id.nav_home)
                {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_homeFragment);
                }
                if(menuItem.getItemId()==R.id.nav_feed)
                {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_feedFragment);
                }
                if(menuItem.getItemId()==R.id.nav_account)
                {
                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_profileFragment);
                }

                return true;
            }
        });

    }

    private void registerGoogleFeedNotification() {

        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();

        dueDate.set(Calendar.HOUR_OF_DAY, 0);
        dueDate.set(Calendar.MINUTE, 10);
        dueDate.set(Calendar.SECOND, 0);

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24);
        }
        long timeDiff = dueDate.getTimeInMillis()-currentDate.getTimeInMillis();

        OneTimeWorkRequest feedSync =
                new OneTimeWorkRequest.Builder(com.careerguide.newsfeed.MyWorker.class)
                        .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                        .addTag("NEWS_WORK")
                        .build();


        WorkManager.getInstance(this).enqueue(feedSync);

    }



    private void showReport() {
        final ProgressDialog progressDialog2 = new ProgressDialog(activity);
        progressDialog2.setMessage("Fetching Counsellors..");
        progressDialog2.setCancelable(false);
        progressDialog2.setCanceledOnTouchOutside(false);
        StringRequest stringRequest_new = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "get_report_url", response -> {
            JSONObject jobj = null;
            try {
                jobj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("222save_report_url", response);
//                        String test = "";
            String reporturl = jobj.optString("Report_url");
            Log.e("#homeurl","report " +reporturl.length());

            if(reporturl.length() > 4) {
                setTitle("Home");
                Log.e("Urltrst", "myurl" + reporturl);
                progressDialog2.dismiss();

                String filename = "Psychometric_Report.pdf";

                if(Utility.getStoragePermissionFromUser(this)) {
                    progressDialog.show();
                    if( Utility.checkFileExist(filename))
                        startWebViewActivity();
                    else
                        downloadID=Utility.downloadPdf(filename,reporturl,"Psychometric Report","Downloading...",this);
                }
            }
            else {
                setTitle("Home");

                final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                final View dialog = getLayoutInflater().inflate(R.layout.dialouge_test_report, null);
                alertDialog.setView(dialog);
                alertDialog.show();

                dialog.findViewById(R.id.start_test).setOnClickListener(v -> {
                    startActivity(new Intent(activity,PsychometricTestsActivity.class));
                    //  alertDialog.dismiss(); //todo ask rachit to remove or not
                });
            }
        }, error -> Log.e("save_report_url_error","error"))
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("email" , Utility.getUserEmail(activity));
                Log.e("#line_status_request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest_new);

    }

    private void liveCounsellorWork() {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Counsellors..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String PRIVATE_SERVER = "https://app.careerguide.com/api/counsellor/";
        StringRequest stringRequests = new StringRequest(Request.Method.POST, PRIVATE_SERVER + "get_live_counsellor", response -> {
            Log.e("live_counsellor", response);
            JSONObject jobj = null;
            try {
                jobj = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String Channel_name = jobj.optString("channel_name");
            String Firstname = jobj.optString("first_name");
            String Lastname = jobj.optString("last_name");
            String counsellorpic = jobj.optString("profile_pic");
            if (Channel_name == "null"){
                Log.e("inside" , "inekme");
                final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                final View dialog = getLayoutInflater().inflate(R.layout.dialog_livecounsellor, null);
                alertDialog.setView(dialog);
                alertDialog.show();
                dialog.findViewById(R.id.start_test).setOnClickListener(v -> {
                    startActivity(new Intent(activity,exoplayerActivity.class));
                    //  alertDialog.dismiss();
                });
            }
            else{
                progressDialog.dismiss();
                Intent intent = new Intent(activity , exoplayerActivity.class);
                intent.putExtra("channel_name" , Channel_name);
                intent.putExtra("Firstname" , Firstname);
                intent.putExtra("Lastname" , Lastname);
                intent.putExtra("counsellorpic" , counsellorpic);
                Log.e("###channel" , "name "+Channel_name);
                startActivity(intent);
            }
        }, error -> Log.e("live_counsellor_error","error"))
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("email" , Utility.getUserEmail(activity));
                Log.e("#nline_status_request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequests);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,   R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    Log.e("#talk", "menuitem:" + menuItem.getItemId());
                    //selectDrawerItem(menuItem);
                    return true;
                });
    }


/*
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        if (menuItem.getItemId() == R.id.tests)
        {
            startActivity(new Intent(activity,PsychometricTestsActivity.class));
            // Close the navigation drawer
            mDrawer.closeDrawers();
            return;
        }
        else if(menuItem.getItemId()==R.id.refer_a_friend)
        {

            startActivity(new Intent(activity, Refer_a_friend.class));
            mDrawer.closeDrawers();

            return;
        }
        else if(menuItem.getItemId()==R.id.rate_us)
        {
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.careerguide"));
            startActivity(intent);
        }
        else if(menuItem.getItemId()==R.id.livesessions)
        {
            Intent intent= new Intent(activity , exoplayerActivity.class);
            startActivity(intent);
            mDrawer.closeDrawers();
            return;
        }
        else if(menuItem.getItemId()==R.id.Blog)
        {
            Intent intent= new Intent(activity , CatDetailActivity.class);
            startActivity(intent);
            mDrawer.closeDrawers();
            return;
        }


        else if(menuItem.getItemId()==R.id.livecounsellor)
        {
            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Fetching Counsellors..");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            String PRIVATE_SERVER = "https://app.careerguide.com/api/counsellor/";
            StringRequest stringRequests = new StringRequest(Request.Method.POST, PRIVATE_SERVER + "get_live_counsellor", response -> {
                Log.e("live_counsellor", response);
                JSONObject jobj = null;
                try {
                    jobj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String Channel_name = jobj.optString("channel_name");
                String Firstname = jobj.optString("first_name");
                String Lastname = jobj.optString("last_name");
                String counsellorpic = jobj.optString("profile_pic");
                if (Channel_name == "null"){
                    Log.e("inside" , "inekme");
                    final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    final View dialog = getLayoutInflater().inflate(R.layout.dialog_livecounsellor, null);
                    alertDialog.setView(dialog);
                    alertDialog.show();
                    dialog.findViewById(R.id.start_test).setOnClickListener(v -> {
                        startActivity(new Intent(activity,exoplayerActivity.class));
                        //  alertDialog.dismiss();
                    });
                }
                else{
                    progressDialog.dismiss();
                    Intent intent = new Intent(activity , exoplayerActivity.class);
                    intent.putExtra("channel_name" , Channel_name);
                    intent.putExtra("Firstname" , Firstname);
                    intent.putExtra("Lastname" , Lastname);
                    intent.putExtra("counsellorpic" , counsellorpic);
                    Log.e("###channel" , "name "+Channel_name);
                    startActivity(intent);
                }
            }, error -> Log.e("live_counsellor_error","error"))
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("email" , Utility.getUserEmail(activity));
                    Log.e("#nline_status_request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequests);
            mDrawer.closeDrawers();
            return;
        }

        Fragment fragment = null;
        Class fragmentClass = HomeFragment.class;
        switch(menuItem.getItemId()) {
            case R.id.home_fragment:
                fragmentClass = CGPlaylist.class;
                break;
            case R.id.about_fragment:
                fragmentClass = AboutUsFragment.class;
                break;
            case R.id.careerbooks:
                fragmentClass = ebookpdf.class;
                break;
            //case R.id.terms_fragment:
                //fragmentClass = TermsAndConditionFragment.class;
                //break;
           case R.id.contact_us:
               fragmentClass = ContactUsFragment.class;
                break;
            case R.id.counsellorSignUp:
                fragmentClass = CounsellorSignUpFragment.class;
                break;

            case R.id.counsellorCorner:
                fragmentClass = CounsellorCornerFragment.class;
                break;
            case R.id.report_url:
                final ProgressDialog progressDialog2 = new ProgressDialog(activity);
                progressDialog2.setMessage("Fetching Counsellors..");
                progressDialog2.setCancelable(false);
                progressDialog2.setCanceledOnTouchOutside(false);
                StringRequest stringRequest_new = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "get_report_url", response -> {
                    JSONObject jobj = null;
                    try {
                        jobj = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("222save_report_url", response);
//                        String test = "";
                    String reporturl = jobj.optString("Report_url");
                    Log.e("#homeurl","report " +reporturl.length());

                    if(reporturl.length() > 4) {
                        setTitle("Home");
                        Log.e("Urltrst", "myurl" + reporturl);
                        progressDialog2.dismiss();
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("url", reporturl);
                        intent.putExtra("filename", "Report");
                        Log.e("HomeResponse", reporturl);
                        startActivity(intent);
                    }
                    else {
                        setTitle("Home");
                        Log.e("insdieif","dialouge");
                        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                        final View dialog = getLayoutInflater().inflate(R.layout.dialouge_test_report, null);
                        alertDialog.setView(dialog);
                        alertDialog.show();
                        //setTitle("Home");

                        dialog.findViewById(R.id.start_test).setOnClickListener(v -> {
                            startActivity(new Intent(activity,PsychometricTestsActivity.class));
                            //  alertDialog.dismiss();
                        });
                    }
                }, error -> Log.e("save_report_url_error","error"))
                {
                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("email" , Utility.getUserEmail(activity));
                        Log.e("#line_status_request",params.toString());
                        return params;
                    }
                };
                VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest_new);
                break;

            case R.id.videocallcounsellor:
//                    openchat();
                Log.e("#talkinswitch" , "menuitem:"+menuItem.getItemId());
                Intent intent = new Intent(activity , video_chat_counsellor.class);
                startActivity(intent);
                break;
            default:
                fragmentClass = CGPlaylist.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(menuItem.getTitle().toString()).commit();

        // Highlight the selected item has been done by NavigationView
        //menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }

    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            /* case R.id.notification:
             *//*FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.flContent, new NotificationFragment()).addToBackStack("Notifications").commit();
                setTitle("Notifications");*//*
                navController.navigate(R.id.action_home_fragment_to_notificationFragment);

                return true;*/
            case R.id.action_settings:
                startActivity(new Intent(activity, SettingActivity.class));
                return true;
        }
        /*if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        return super.onOptionsItemSelected(item) || NavigationUI.onNavDestinationSelected(item,navController);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //drawerToggle.onConfigurationChanged(newConfig);
    }


    public static void updatePic(String picUrl, Context context) {
        if (picUrl != null && picUrl.length() > 0)
        {
            Glide.with(context).load(picUrl).into(profilePic);
            ((TextView) headerLayout.findViewById(R.id.imageInitial)).setText("");
        }
        else
        {
            profilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail((Activity) context)).charAt(0),60));
            String initial = (Utility.getUserFirstName((Activity) context)).charAt(0) + "" + (Utility.getUserLastName((Activity) context)).charAt(0);
            ((TextView) headerLayout.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
        }
    }

    public static void updateUserName(String name,Activity activity) {
        nameTextView.setText(name);
        String picUrl = Utility.getUserPic(activity);
        if (picUrl != null && picUrl.length() > 0)
        {
            Glide.with(activity).load(picUrl).into(profilePic);
            ((TextView) headerLayout.findViewById(R.id.imageInitial)).setText("");
        }
        else
        {
            //profilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(activity).charAt(0)),60));
            String initial = (Utility.getUserFirstName(activity)).charAt(0) + "" + (Utility.getUserLastName(activity)).charAt(0);
            ((TextView) headerLayout.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
        }
    }

    public static void updateUserCity(String city) {
        locationTextView.setText(city);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new TaskFetchLiveCounsellors().execute();
        Utility.handleOnlineStatus(this, "idle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.handleOnlineStatus(null,"");
    }


//  @Override
// public void onBackPressed(){
//        try{
//
//            AlertDialog.Builder builder=new AlertDialog.Builder(this);
//
//            builder.setMessage("Are you sure you want to exit\nCareerGuide?")
//                    .setCancelable(true)
//
//                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            HomeActivity.super.onBackPressed();
//
//                        }
//                    })
//                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            dialogInterface.cancel();
//                        }
//                    })
//
//            ;
//
//            AlertDialog alertDialog=builder.create();
//            alertDialog.show();
//        }
//        catch(Exception e){
//
//        }
    //  }


    @Override
    public void onBackPressed() {
         /*try {
            String menuTitle = "";
            PopupMenu popupMenu = new PopupMenu(activity,null);
            Menu menu = popupMenu.getMenu();
            getMenuInflater().inflate(R.menu.drawer_home,menu);
            for (int i = 0; i<menu.size(); i++)
            {
                menuTitle = menu.getItem(i).getTitle().toString();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(menuTitle);
                if (fragment != null && fragment.isVisible()) {
                    setTitle(menuTitle);
                    break;
                }
            }
            if (menuTitle.isEmpty())
            {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("Notifications");
                if (fragment != null && fragment.isVisible()) {
                    setTitle("Notifications");
                }
                else {
                    fragment = getSupportFragmentManager().findFragmentByTag("Legal");
                    if (fragment != null && fragment.isVisible()) {
                        setTitle("Legal");
                    }
                    else
                    {
                        setTitle("Home");
                    }
                }
            }

            if(getSupportFragmentManager().getFragments().size() <= 1)
                setTitle("Home");

            Log.d("#####",getSupportFragmentManager().getBackStackEntryCount()+" is fragemnt count");


            switch (getSupportFragmentManager().getBackStackEntryCount()){
                case 0:

                    final androidx.appcompat.app.AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    final View dialog = getLayoutInflater().inflate(R.layout.dialog_exit, null);
                    //setTitle("Home");

                    dialog.findViewById(R.id.no).setOnClickListener(v -> alertDialog.dismiss());
                    dialog.findViewById(R.id.yes).setOnClickListener(v -> HomeActivity.super.onBackPressed());
                    alertDialog.setView(dialog);
                    alertDialog.show();
                default:
                    getSupportFragmentManager().popBackStack();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }*/
        super.onBackPressed();
    }

    private void disableEditText(EditText editText)
    {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void updateProfile(final String key, final String category,String subCategory, final View edit, final View done, final View cancel) {

        String value = category;
        if (value.isEmpty())
        {
            Toast.makeText(activity,"Please insert value first", Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialogCustom(activity,"Saving...");
        progressDialog.show();
        final String finalValue = value;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "profile_update", response -> {
            progressDialog.dismiss();
            Log.e("prfl_updt_res",response);
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status",false);
                String msg = jsonObject.optString("msg");
                if(status)
                {
                    switch (key)
                    {
                        case "education_level":
                            Utility.setUserEducation(activity, finalValue);
                            /*FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();*/

                            Menu menu = navigationView.getMenu();
                            MenuItem menuItem = menu.findItem(R.id.nav_to_CategoryHome);
                            NavigationUI.onNavDestinationSelected(menuItem,navController);
                            Log.e("Education Level" , "-->" +Utility.getUserEducation(activity));
                            setTitle(Utility.getUserEducation(activity));
                            toolbar.setSubtitle(subCategory);
                            mDrawer.closeDrawers();
                            break;

                    }
                    if (edit != null)
                        edit.setVisibility(View.VISIBLE);
                    if (done != null)
                        done.setVisibility(View.GONE);
                    if (cancel != null)
                        cancel.setVisibility(View.GONE);

                }
                else
                {
                    Toast.makeText(activity, "Something went wrong.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("prfl_updt_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                params.put(key, finalValue);
                Log.e("prfl_updt_req",params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CATEGORY_CODE && resultCode == RESULT_OK && data != null) {


            tv_name.setText(data.getStringExtra("parent_cat_title"));
            Utility.setEducationUid(this, data.getStringExtra("subcat_uid"));
            Log.e("uidis", " " + data.getStringExtra("subcat_uid"));
            updateProfile("education_level", data.getStringExtra("parent_cat_title"),data.getStringExtra("subcat_title"), null, null, null);

            if (data.getStringExtra("icon_url") == null) {
                Glide.with(this).load(Utility.getIcon_url(this)).into(classimg);
                Log.e("edu", "" + Utility.getUserEducation(this));
            } else {
                Glide.with(this).load(data.getStringExtra("icon_url")).into(classimg);
            }

        }
    }

    public void share() {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.careerguide.com/"+Utility.getUserId(this)+"/"+androidId))
                .setDomainUriPrefix("https://careerguidestudent.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.careerguide")
                        .build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("app")
                                .setMedium("anyone")
                                .setCampaign("example-app-referral")
                                .build())
                // Open links with com.example.ios on iOS
                // .setIosParameters(new DynamicLink.IosParameters.Builder("com.careerguide.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("TAG", "share: " + dynamicLink.getUri());

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
                            Utility.setRefId(dynamicLinkUri.toString(),activity);
                            // File file=new File("android.resource://com.careerguide/" + R.drawable.careerguide_banner);
                        } else {
                            Log.e("TAG", "onComplete: error" + task.getException());
                            // Error
                            // ...
                        }
                    }
                });
    }

    public void executeAllTasks(){

        String url_one[] = {"https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLnnMTbSs_SO6uJ0ID2pCegbt2iXJ_pyFS&key=" + browserKey + "&maxResults=50","1"};
        String url_two[] = {"https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLnnMTbSs_SO7H9GCU_aZbZTK-G064Mcgd&key=" + browserKey + "&maxResults=50","2"};
        String url_three[] = {"https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PLnnMTbSs_SO5Rnt3QlWFTdf-50IUp7bBg&key=" + browserKey + "&maxResults=50","3"};

        String[] url_NINE={  "https://app.careerguide.com/api/main/videos_NINE","1"};//url and fetchCode used in switch
        String[] url_TEN= {  "https://app.careerguide.com/api/main/videos_TEN","2"};
        String[] url_ELEVEN = {"https://app.careerguide.com/api/main/videos_ELEVEN","3"};
        String[] url_TWELVE = {"https://app.careerguide.com/api/main/videos_TWELVE","4"};
        String[] url_GRADUATE ={ "https://app.careerguide.com/api/main/videos_GRADUATE","5"};
        String[] url_POSTGRA = {"https://app.careerguide.com/api/main/videos_POSTGRA","6"};
        String[] url_WORKING = {"https://app.careerguide.com/api/main/videos_WORKING","7"};

        //new TaskFetchLiveCounsellors().execute();
        new TaskFetchLiveFacebookCounsellors().execute();
        new TaskFetch1_2_3().execute(url_one);
        new TaskFetch1_2_3().execute(url_two);
        new TaskFetch1_2_3().execute(url_three);

        new TaskFetch().execute(url_NINE);
        new TaskFetch().execute(url_TEN);
        new TaskFetch().execute(url_ELEVEN);
        new TaskFetch().execute(url_TWELVE);
        new TaskFetch().execute(url_GRADUATE);
        new TaskFetch().execute(url_POSTGRA);
        new TaskFetch().execute(url_WORKING);

        new TaskBlog().execute();
        new TaskFetchAllCounsellors().execute();
    }


    public class TaskFetchLiveCounsellors extends AsyncTask<String, Void, List<CurrentLiveCounsellorsModel>> {


        @Override
        protected List<CurrentLiveCounsellorsModel> doInBackground(String... params) {



            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "all_available_counsellors", response -> {
                Log.e("all_coun_res", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {
                        tempCurrentLiveCounsellorsList.clear();

                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                        Log.e("name-2->","" +counsellorsJsonArray);

                        for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                        {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("co_id");
                            String firstName = counselorJsonObject.optString("first_name");
                            String lastName = counselorJsonObject.optString("last_name");
                            String picUrl = counselorJsonObject.optString("profile_pic");
                            String channel_name = counselorJsonObject.optString("channel_name");
                            String scheduleDescrpition = "LIVE NOW, Session on "+counselorJsonObject.optString("topic");
                            Log.e("name-1->","" +channel_name);
                            tempCurrentLiveCounsellorsList.add(new CurrentLiveCounsellorsModel(firstName+" "+lastName,counselorJsonObject.optString("topic"),picUrl,channel_name,scheduleDescrpition));
                            Log.e("#inside" ,"for" +picUrl+"__"+tempCurrentLiveCounsellorsList.get(0).getCounsellorName());

                        }

                        new TaskFetchPostLiveCounsellors().execute();


                        // Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                    //hideProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {

                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    Log.e("all_coun_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);



            return null;

        }

    }

    public class TaskFetchPostLiveCounsellors extends AsyncTask<String, Void, List<CurrentLiveCounsellorsModel>> {


        @Override
        protected List<CurrentLiveCounsellorsModel> doInBackground(String... params) {
            tempPostLiveCounsellorsList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/fetch_session", response -> {
                Log.e("postlive_coun_res", response);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {

                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellor");
                        Log.e("name-2->","" +counsellorsJsonArray);


                        for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                        {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("userId");
                            String firstName = counselorJsonObject.optString("co_FirstName");
                            String lastName = counselorJsonObject.optString("co_LastName");
                            String picUrl = counselorJsonObject.optString("co_img");
                            String channel_name = counselorJsonObject.optString("channel_name");
                            String scheduleDescrpition = "LIVE AT "+counselorJsonObject.optString("time")+" on "+counselorJsonObject.optString("formatteddate")+". Topic:  "+counselorJsonObject.optString("topic");
                            Log.e("name-1->","" +channel_name);
                            tempPostLiveCounsellorsList.add(new CurrentLiveCounsellorsModel(firstName+" "+lastName,counselorJsonObject.optString("topic"),picUrl,channel_name,scheduleDescrpition));//use the same model for postlive sessions
                            Log.e("#inside" ,"for" +picUrl+"__"+tempPostLiveCounsellorsList.get(0).getCounsellorName());

                        }

                        if(tempPostLiveCounsellorsList.size()==0){
                            tempPostLiveCounsellorsList.add(new CurrentLiveCounsellorsModel("No one has scheduled anything yet","","","",""));
                        }

                        runOnUiThread(()->{
                            finalList.clear();
                            finalList.addAll(tempCurrentLiveCounsellorsList);
                            finalList.addAll(tempPostLiveCounsellorsList);
                            viewModelProvider.setDisplaylistArrayLiveCounsellors(finalList);
                        });
                        // Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                    //hideProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {

                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    Log.e("all_coun_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
            return null;

        }

    }

    private class TaskFetchLiveFacebookCounsellors extends AsyncTask<Void, Void, Void> {

        String LIVE_URL = "https://www.googleapis.com/youtube/v3/search?key="+browserKey+"&channelId=UCs6EVBxMpm9S3a2RpbAIp1w&forUsername=s6EVBxMpm9S3a2RpbAIp1w&part=snippet,id&order=date&maxResults=20";
        ArrayList<CurrentLiveCounsellorsModel> liveVideos = new ArrayList<>();


        @Override
        protected Void doInBackground(Void... params) {


            StringRequest liveRequest = new StringRequest(Request.Method.GET, LIVE_URL, response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean status = json.optBoolean("status", false);
                    if (status) {
                        JSONArray jsonArray = json.getJSONArray("items");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String liveBroadcastContent = jsonObject.getJSONObject("snippet").getString("liveBroadcastContent");
                                if (liveBroadcastContent.equals("live") || liveBroadcastContent.equals("upcoming")) {
                                    JSONObject video = jsonObject.getJSONObject("snippet").getJSONObject("resourceId");
                                    String title = jsonObject.getJSONObject("snippet").getString("title");
                                    String Desc = jsonObject.getJSONObject("snippet").getString("description");
                                    String id = video.getString("videoId");
                                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                                    CurrentLiveCounsellorsModel liveVideo = new CurrentLiveCounsellorsModel("FaceBook.com","", thumbUrl, id,"LIVE Stream, Session on  "+title);//FaceBook is a key word in the CurrentLiveCounsellorsAdapter
                                    liveVideos.add(liveVideo);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("LIVE_VIDEOS", "error: "+e.toString());

                }

            }, error -> {
                Log.e("LIVE_VIDEOS", "error: "+error.toString());
            }){
                @Override
                protected Map<String, String> getParams()
                {
                    return new HashMap<>();
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(liveRequest);
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            viewModelProvider.setLiveVideosList(liveVideos);
        }
    }

    private class TaskFetch1_2_3 extends AsyncTask<String, Void, ArrayList<Videos>> {
        Videos displaylist;
        ArrayList<Videos> displaylistArray = new ArrayList<>();

        int fetchCode=0;//default


        @Override
        protected ArrayList<Videos> doInBackground(String... params) {

            fetchCode=Integer.parseInt(params[1]);

            try {

                String response = Utility.getUrlString(params[0]);
                JSONObject json = new JSONObject(response);
                JSONArray jsonArray = json.getJSONArray("items");
                int jsonArrayLen=jsonArray.length();
                List<Integer> randomIndexList = new ArrayList<Integer>();
                Random rand = new Random();
                int tempRandom;

                int i;//control i depending on the items present default is 6
                if(jsonArrayLen<7){
                    i=7-jsonArrayLen;
                }else {
                    i=0;
                }

                for (; i < 7; i++) {

                    while(true) {//loop until no duplicate is found.
                        tempRandom = rand.nextInt(jsonArrayLen);
                        if (randomIndexList.contains(tempRandom)) {
                            continue;//duplicate number,hence skip
                        } else {
                            randomIndexList.add(tempRandom);
                            break;
                        }
                    }

                    JSONObject jsonObject = jsonArray.getJSONObject(tempRandom);
                    JSONObject video = jsonObject.getJSONObject("snippet").getJSONObject("resourceId");
                    String title = jsonObject.getJSONObject("snippet").getString("title");
                    String Desc = jsonObject.getJSONObject("snippet").getString("description");
                    String id = video.getString("videoId");
                    String thumbUrl = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                    displaylist = new Videos(title, thumbUrl ,id , Desc);
                    displaylistArray.add(displaylist);

                }
                Log.e("#First","--->"+params[1]);
            }catch(Exception e1)
            {
                e1.printStackTrace();
            }
            return displaylistArray;
        }

        @Override
        protected void onPostExecute(ArrayList<Videos> result) {
            viewModelProvider = new ViewModelProvider(HomeActivity.this).get(CGPlayListViewModel.class);

            switch (fetchCode) {
                case 1: {
                    viewModelProvider.setDisplaylistArray(result);
                    break;
                }
                case 2: {
                    viewModelProvider.setDisplaylistArray_two(result);
                    break;
                }
                case 3: {
                    viewModelProvider.setDisplaylistArray_three(result);
                    break;
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

    private class TaskFetch extends AsyncTask<String, Void, List<CommonEducationModel>> {

        CommonEducationModel displaylist;
        List<CommonEducationModel> displaylistArray = new ArrayList<>();

        int fetchCode=0;//default

        @Override
        protected List<CommonEducationModel> doInBackground(String... params) {

            fetchCode=Integer.parseInt(params[1]);

            try {
                String response = Utility.getUrlString(params[0]);
                JSONObject json = new JSONObject(response);
                JSONArray jsonArray= json.optJSONArray("videos");
                int jsonArrayLen=jsonArray.length();
                List<Integer> randomIndexList = new ArrayList<Integer>();
                Random rand = new Random();
                int tempRandom;

                int i;//control i depending on the items present default is 6
                if(jsonArrayLen<7){
                    i=7-jsonArrayLen;
                }else {
                    i=0;
                }

                for (; i < 7; i++) {

                    while(true) {//loop until no duplicate is found.
                        tempRandom = rand.nextInt(jsonArrayLen);
                        if (randomIndexList.contains(tempRandom)) {
                            continue;//duplicate number,hence skip
                        } else {
                            randomIndexList.add(tempRandom);
                            break;
                        }
                    }

                    JSONObject JsonObject = jsonArray.optJSONObject(tempRandom);
                    String user_id = JsonObject.optString("user_id");
                    String email = JsonObject.optString("email");
                    String name = JsonObject.optString("Name");
                    String img_url = JsonObject.optString("img_url");
                    String title = JsonObject.optString("title");
                    String video_url = JsonObject.optString("video_url");
                    String video_views=JsonObject.optString("views");
                    String video_id = JsonObject.optString("id");
                    String video_category=JsonObject.optString("Video_category");
                    String profile_pic="https://app.careerguide.com/api/user_dir/"+JsonObject.optString("profile_pic");
                    displaylist  = new CommonEducationModel(user_id,email, name, img_url, video_url, title,profile_pic ,video_views,video_id,video_category);
                    displaylistArray.add(displaylist);

                }
                Log.e("#First","-->"+params[1]);
            }catch(Exception e1)
            {
                e1.printStackTrace();
            }
            return displaylistArray;

        }

        @Override
        protected void onPostExecute(List<CommonEducationModel> result) {//is needed don't delete

            switch (fetchCode){
                case 1:{
                    viewModelProvider.setDisplaylistArray_NINE(result);
                    break;
                }
                case 2:{
                    viewModelProvider.setDisplaylistArray_TEN(result);
                    break;
                }
                case 3:{
                    viewModelProvider.setDisplaylistArray_ELEVEN(result);
                    break;
                }
                case 4:{
                    viewModelProvider.setDisplaylistArray_TWELVE(result);
                    break;
                }
                case 5:{
                    viewModelProvider.setDisplaylistArray_GRADUATE(result);
                    break;
                }
                case 6:{
                    viewModelProvider.setDisplaylistArray_POSTGRA(result);
                    break;
                }
                case 7:{
                    viewModelProvider.setDisplaylistArray_WORKING(result);
                    break;
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class TaskBlog extends AsyncTask<Void, Void, Void> {

        DataMembers displaylist;
        List<DataMembers> displaylistArray = new ArrayList<>();
        CompositeDisposable disposable;
        Categories categories;
        Bundle bundle = getIntent().getExtras();
        List<CategoryDetails> categoryDetails;

        @Override
        protected Void doInBackground(Void... params) {
            disposable = new CompositeDisposable();

            categoryDetails = new ArrayList<>();
            //   categories = new Gson().fromJson(bundle.getString("data"), Categories.class);
            disposable.add(Utils.get_api().get_cat_detail("10", "1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                        @Override
                        public void onSuccess(List<CategoryDetails> cd) {
                            if (cd != null) {
                                for (CategoryDetails c : cd) {
                                    c.setTitle(Utils.remove_tags(c.getTitle()));
                                    c.setDesc(Utils.remove_tags(c.getDesc()));
                                    Log.e("#c" , "-->" +c);
                                    categoryDetails.add(c);
                                }
                                viewModelProvider.setDisplaylistArray_categoryDetails(categoryDetails);
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            Log.e("Error Occured" , "-->");
                        }
                    }));


//            try {
//                String url_Blog = "https://institute.careerguide.com/wp-json/wp/v2/posts";
//                String response_Blog = Utility.getUrlString(url_Blog);
//                JSONArray jsonArray_Blog = new JSONArray(response_Blog);
//                Log.e("Jsonresult" , "--> " +response_Blog);
//                for (int i = 0; i < jsonArray_Blog.length(); i++) {
//                    JSONObject jsonObject;
//                    jsonObject = (JSONObject) jsonArray_Blog.get(i);
//                    JSONObject jsonObject1;
//                    jsonObject1 = (JSONObject) jsonObject.get("title");
//                    JSONObject jsonObject2;
//                    jsonObject2 = (JSONObject) jsonObject.get("content");
//                    JSONObject jsonObject3;
//                    jsonObject3 = (JSONObject) jsonObject.get("excerpt");
//                    String imgUrl = getFetaureImageUrl(jsonObject2.getString("rendered"));
//                    displaylist = new DataMembers(jsonObject.getInt("id"), jsonObject1.getString("rendered") , jsonObject2.getString("rendered") ,imgUrl , jsonObject.getString("link"), jsonObject3.getString("rendered"));
//                    displaylistArray.add(displaylist);
//                }
//                Log.e("#Blog","-->");
//            }catch(Exception e1)
//            {
//                e1.printStackTrace();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //      setDisplaylistArray_Blog(displaylistArray);
            viewModelProvider.setDisplaylistArray_categoryDetails(categoryDetails);

        }
    }

    private class TaskFetchAllCounsellors extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            List<Counsellor> counsellorList = new ArrayList<>();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "category_counsellors", response -> {
                Log.e("all_coun_res_counsellor", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status) {
                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                        for (int i = 0; counsellorsJsonArray != null && i < counsellorsJsonArray.length(); i++) {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("co_id");
                            String firstName = counselorJsonObject.optString("first_name");
                            String lastName = counselorJsonObject.optString("last_name");
                            String picUrl = counselorJsonObject.optString("profile_pic");
                            String email = counselorJsonObject.optString("email");
                            counsellorList.add(new com.careerguide.models.Counsellor(id, email, firstName, lastName, picUrl, 27));
                        }

                        viewModelProvider.setCounsellorList(counsellorList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.e("all_coun_rerror", "error");
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    //params.put("user_education", Utility.getUserEducationUid(getActivity()));
                    Log.e("all_coun_req", params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            return null;

        }


    }


    public void onSeeAllClick(View v)
    {
        int mode =0;
        Intent intent = new Intent(this, SeeAllActivity.class);

        switch (v.getId())
        {
            case R.id.see_all_cat1:
                mode =1;
                intent.putExtra("KEY","PLnnMTbSs_SO6uJ0ID2pCegbt2iXJ_pyFS");
                intent.putExtra("TITLE","Corona Awareness");

                break;

            case R.id.see_all_cat2:
                mode =1;
                intent.putExtra("KEY","PLnnMTbSs_SO7H9GCU_aZbZTK-G064Mcgd");
                intent.putExtra("TITLE","Career Options");
                break;

            case R.id.see_all_cat10:
                mode =1;
                intent.putExtra("KEY","PLnnMTbSs_SO5Rnt3QlWFTdf-50IUp7bBg");
                intent.putExtra("TITLE","Study Abroad");
                break;

            case R.id.see_all_cat_test:
                startActivity(new Intent(this,PsychometricTestsActivity.class));
                return;

            case R.id.see_all_cat_blogs:
                startActivity(new Intent(this, CatDetailActivity.class));
                return;

            case R.id.see_all_cat3:
                intent.putExtra("EDU_KEY","videos_NINE");
                intent.putExtra("TITLE","Class 9th");
                break;

            case R.id.see_all_cat4:
                intent.putExtra("EDU_KEY","videos_TEN");
                intent.putExtra("TITLE","Class 10th");

                break;

            case R.id.see_all_cat5:
                intent.putExtra("EDU_KEY","videos_ELEVEN");
                intent.putExtra("TITLE","Class 11th");
                break;

            case R.id.see_all_cat6:
                intent.putExtra("EDU_KEY","videos_TWELVE");
                intent.putExtra("TITLE","Class 12th");
                break;

            case R.id.see_all_cat7:
                intent.putExtra("EDU_KEY","videos_GRADUATE");
                intent.putExtra("TITLE","Graduates");
                break;

            case R.id.see_all_cat8:
                intent.putExtra("EDU_KEY","videos_POSTGRA");
                intent.putExtra("TITLE","Post Graduates");
                break;

            case R.id.see_all_cat9:
                intent.putExtra("EDU_KEY","videos_WORKING");
                intent.putExtra("TITLE","Working Professionals");
                break;
        }

        intent.putExtra("mode",mode);
        startActivity(intent);
    }

    void startWebViewActivity(){
        Intent intent1 = new Intent(activity, WebViewActivity.class);
        intent1.putExtra("pdfName", "Psychometric_Report.pdf");
        intent1.putExtra("toolBarTitle","Psychometric Report");
        progressDialog.dismiss();
        startActivity(intent1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted.");
                } else {
                    Log.e("value", "Permission Denied, You cannot save reports/E-Books .");
                }
                break;
        }
    }
}
