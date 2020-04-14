package com.careerguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;

import com.careerguide.blog.activity.CatDetailActivity;
import com.careerguide.blog.activity.CatDetailWoActivity;
import com.careerguide.blog.activity.CatListActivity;
import com.careerguide.blog.activity.MainBlog;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.careerguide.activity.GoalsActivity;
import com.careerguide.blog.BlogActivity;
import com.careerguide.youtubeVideo.CGPlaylist;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener{

    private Activity activity = this;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private static CircleImageView profilePic;
    private static ImageView classimg;
    private static TextView nameTextView , tv_name;
    private static TextView locationTextView;
    private static View headerLayout;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    public NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;
    private String mActivityTitle;
    private String apiKey = "1befd9d8922089646809507c9b51320c61dfa227";
    @Override
    protected void onStart() {
        super.onStart();

    }

    public void openchat(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*

        * Tutorial
        * https://guides.codepath.com/android/fragment-navigation-drawer
        *
        * */
        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.navView);
        setupDrawerContent(nvDrawer);

        headerLayout = nvDrawer.getHeaderView(0);
        profilePic = headerLayout.findViewById(R.id.profilePic);



        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.version)).setText(version.equals("")?"":"Version " + version);


        findViewById(R.id.leagal).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.flContent, new TermsAndConditionFragment()).addToBackStack("Legal").commit();
            setTitle("Legal");
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
        if(getIntent().getStringExtra("parent_cat_title") == null){
            tv_name.setText(Utility.getUserEducation(activity));
        }
        else{
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

        headerLayout.findViewById(R.id.class_cat).setOnClickListener(v -> startActivity(new Intent(activity,GoalsActivity.class)));

        nameTextView.setText(Utility.getUserFirstName(activity) + " " + Utility.getUserLastName(activity) /*+ "   >"*/);
        locationTextView = headerLayout.findViewById(R.id.locationTextView);
        locationTextView.setText(Utility.getUserCity(activity));
        headerLayout.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.flContent, new ProfileFragment()).addToBackStack("Profile").commit();
            setTitle("Profile");
            mDrawer.closeDrawers();

        });

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, new CounsellorCornerFragment()).commit();
//        setTitle("Home");
        if(getIntent().getStringExtra("parent_cat_title") == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new CGPlaylist()).commit();
            Log.e("Education_Level" , "-->" +Utility.getUserEducation(activity));
            setTitle("Home");
            mDrawer.closeDrawers();
        }

        findViewById(R.id.notification).setOnClickListener(v -> {
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.popBackStack();
            fragmentManager1.beginTransaction().replace(R.id.flContent, new NotificationFragment()).addToBackStack("Notifications").commit();
            setTitle("Notifications");
        });
        findViewById(R.id.setting).setOnClickListener(v -> startActivity(new Intent(activity, SettingActivity.class)));
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
                    selectDrawerItem(menuItem);
                    return true;
                });
    }



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
           /* case R.id.terms_fragment:
                fragmentClass = TermsAndConditionFragment.class;
                break;*/
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



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home,menu);

        MenuItem item = menu.findItem(R.id.notification);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.notification:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.flContent, new NotificationFragment()).addToBackStack("Notifications").commit();
                setTitle("Notifications");
                return true;
            case R.id.action_settings:
                startActivity(new Intent(activity, SettingActivity.class));
                return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
         try {
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
        }
    }

    private void disableEditText(EditText editText)
    {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void updateProfile(final String key, final String editText, final View edit, final View done, final View cancel) {
        String value = editText;
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
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
                            Log.e("Education Level" , "-->" +Utility.getUserEducation(activity));
                            setTitle(Utility.getUserEducation(activity));
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



}
