package com.careerguide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import com.careerguide.activity.Activity_class;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailActivity extends AppCompatActivity implements LocationListener
{
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private Activity activity = this;
    private String id, firstName, lastName, email, gender, profilePic, dob, city, source, password;
    private EditText firstNameEditText, lastNameEditText, dobEditText, cityEditTex;
    private RadioButton maleRB, femaleRB, otherRB;
    private RadioGroup genderRG;
    private CircleImageView circleImageView;
    private Bitmap bitmap;
    private LocationManager locationManager;
    private String provider;
    private ProgressDialogCustom progressDialogCustom;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        email = intent.getStringExtra("email");
        gender = intent.getStringExtra("gender");
        profilePic = intent.getStringExtra("profilePic");

        Log.e("counsellorpic" , " " +profilePic);
        source = intent.getStringExtra("source");
        password = intent.getStringExtra("password");

        circleImageView = (CircleImageView) findViewById(R.id.profileImage);
        firstNameEditText = (EditText) findViewById(R.id.firstName);
        lastNameEditText = (EditText) findViewById(R.id.lastName);
        dobEditText = (EditText) findViewById(R.id.dob);
        cityEditTex = (EditText) findViewById(R.id.city);


        maleRB = (RadioButton) findViewById(R.id.maleRB);
        femaleRB = (RadioButton) findViewById(R.id.femaleRB);
        otherRB = (RadioButton) findViewById(R.id.otherRB);

        genderRG = (RadioGroup) findViewById(R.id.genderRG);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if(profilePic!= null && profilePic.length()>0)
        {
            // Retrieves an image specified by the URL, displays it in the UI.
            ImageRequest request = new ImageRequest(profilePic,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap)
                        {
                            ProfileDetailActivity.this.bitmap = bitmap;
                            circleImageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            //mImageView.setImageResource(R.drawable.image_load_error);
                        }
                    });
            // Access the RequestQueue through your singleton class.
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        }
        /*else
        {
            circleImageView.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(activity)).charAt(0),100));
            String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
            ((TextView)findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
        }*/

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://github.com/Mariovc/ImagePicker
                //ImagePicker.pickImage(activity, "Select your image:",123,false);

                //https://github.com/ArthurHub/Android-Image-Cropper
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .setAspectRatio(1,1)
                        .start(activity);
            }
        });

        firstNameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        if(gender.equals("male"))
        {
            maleRB.setChecked(true);
        }
        else if(gender.equals("female"))
        {
            femaleRB.setChecked(true);
        }
        else if(gender.length()>0)
        {
            otherRB.setChecked(true);
        }
        else
        {
            genderRG.clearCheck();
        }

        /*final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                Date date = calendar.getTime();
                String dateStr = dateFormat.format(date);
                dobEditText.setText(dateStr);

            }
        },year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());*/
        /*dobEditText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });*/

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dobEditText.setText(current);
                    dobEditText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };

        dobEditText.addTextChangedListener(tw);

        findViewById(R.id.createUserButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                firstName = firstNameEditText.getText().toString().trim();
                lastName = lastNameEditText.getText().toString().trim();
                dob = dobEditText.getText().toString().trim();
                dob = dob.replace("Y","").replace("M","").replace("D","");
                if (dob.length()<10)
                {
                    Toast.makeText(activity,"Please enter valid Date", Toast.LENGTH_LONG).show();
                    return;
                }
                final SimpleDateFormat dateFormatNew = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date date = dateFormat.parse(dob);
                    dob = dateFormatNew.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(activity,"Please enter valid date in DD/MM/YYYY format",Toast.LENGTH_LONG).show();
                }
                city = cityEditTex.getText().toString().trim();
                if(firstName.length() == 0)
                {
                    Toast.makeText(activity,"Please enter First Name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(lastName.length() == 0)
                {
                    Toast.makeText(activity,"Please enter Last Name",Toast.LENGTH_LONG).show();
                    return;
                }
                if(dob.length() == 0)
                {
                    Toast.makeText(activity,"Please enter Date of Birth",Toast.LENGTH_LONG).show();
                    return;
                }
                if(city.length() == 0)
                {
                    Toast.makeText(activity,"Please enter City",Toast.LENGTH_LONG).show();
                    return;
                }
                if(maleRB.isChecked()) {
                    gender = "Male";
                }
                else if(femaleRB.isChecked()) {
                    gender = "Female";
                }
                else if(otherRB.isChecked()) {
                    gender = "Other";
                }
                else
                {
                    Toast.makeText(activity,"Please choose your Gender",Toast.LENGTH_LONG).show();
                    return;
                }
                registerUser();
            }
        });

        findViewById(R.id.fetchLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                //provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        new AlertDialog.Builder(activity)
                                .setTitle("Permission Required")
                                .setMessage("We need location permission to fetch your current city.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Prompt the user once explanation has been shown
                                        ActivityCompat.requestPermissions(activity,
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                MY_PERMISSIONS_REQUEST_LOCATION);
                                    }
                                })
                                .create()
                                .show();
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }
                else
                {
                    fetchLocation();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void fetchLocation()
    {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            progressDialogCustom = new ProgressDialogCustom(activity,"Fetching Location...").showIt();
            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,ProfileDetailActivity.this,null);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if(progressDialogCustom != null && progressDialogCustom.isShowing()) {
                                progressDialogCustom.dismiss();
                            }
                            if (location != null) {
                                // Logic to handle location object

                                Log.e("location with new Logic",location.getLatitude() + " , " + location.getLongitude());
                                try {
                                    Geocoder gcd = new Geocoder(activity, Locale.getDefault());
                                    List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses.size() > 0) {
                                        cityEditTex.setText(addresses.get(0).getLocality());
                                    } else {
                                        //Toast.makeText(activity, "Unable to fetch city.", Toast.LENGTH_LONG).show();
                                        showRetryLocation();
                                    }
                                }
                                catch (Exception ex)
                                {
                                    //Toast.makeText(activity, "Unable to fetch city.", Toast.LENGTH_LONG).show();
                                    showRetryLocation();
                                }
                            }
                            else
                            {
                                Log.e("location with new Logic","null");
                                showRetryLocation();
                            }
                        }
                    });
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if(progressDialogCustom != null && progressDialogCustom.isShowing()) {
                    progressDialogCustom.dismiss();
                    showRetryLocation();
                }
            }, 1000 * 10);
        }
        else
        {
            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(activity).create();
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Please enable GPS from settings.");
            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.show();
        }
    }

    private void showRetryLocation()
    {
        if(progressDialogCustom != null && progressDialogCustom.isShowing()) {
            progressDialogCustom.dismiss();
        }
            new AlertDialog.Builder(activity)
                    .setMessage("Unable to fetch location. Please check your Location settings.")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fetchLocation();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        fetchLocation();
                    }
                    else
                    {
                        Toast.makeText(activity, "Unable to get permission.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(activity, "Unable to get permission.", Toast.LENGTH_LONG).show();
                }
        }
    }
    private void getrewards()
    {
        String id=getIntent().getStringExtra("refid");
        final String[] rp = {"0","0"};
        StringRequest stringRequest2=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "fetch_rewards_point", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rewards_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    /*if(jsonObject.optJSONArray("reward_point").length()!=0)
                    {*/
                        JSONArray userJsonObject = jsonObject.optJSONArray("reward_point");
                        JSONObject jbj = userJsonObject.optJSONObject(0);
                        String rew=jbj.optString("rewards_point");
                        String numref=jbj.optString("reward_number");
                        String name=jbj.optString("name");
                        rp[1]=String.valueOf(Integer.parseInt(numref)+1);
                        Log.e("TAG", "onResponse: "+rew+" "+ numref );
                        if(Integer.parseInt(rp[1])<=20)
                            rp[0]=String.valueOf(Integer.parseInt(rew)+10);
                        else
                            rp[0]=String.valueOf(Integer.parseInt(rew)+15);
                        setrewards(rp[0],rp[1],String.valueOf(name));
                        //Utility.setRewardPoints(activity,String.valueOf(rew));
                    /*}
                    else
                        setrewards("10","1");*/

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
                params.put("userId", id );
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest2);

    }
    private void setrewards(String rew, String numref, String name)
    {
        String id=getIntent().getStringExtra("refid");
        //String id=dl;
        //final int rp=Integer.parseInt(dl.substring(dl.indexOf('/')+1));
        //Log.e("TAG", "rewards: "+id+ (Integer.parseInt(rew) + 1));
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
                params.put("user_id",id);
                params.put("rewards_point", rew);
                params.put("reward_number", numref);
                params.put("name", name);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);

        StringRequest stringRequest1=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "UpdateRewards", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rec_rewards_response", response);
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
                params.put("user_id", Utility.getUserId(activity));
                params.put("rewards_point","10");
                params.put("reward_number", "1");
                params.put("name", Utility.getUserFirstName(activity) + " " + Utility.getUserLastName(activity));
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest1);
    }

    private void registerUser()
    {
        Utility.setUserStreak(activity,"");
        final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                progressDialog.dismiss();
                Log.e("registration_response",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    String msg = jsonObject.optString("msg");
                    if(status && (msg.equals("Registration Successful") || msg.equals("Update Successful")))
                    {
                        JSONObject userJsonObject = jsonObject.optJSONObject("user");
                        String id = userJsonObject.optString("id");
                        String email = userJsonObject.optString("email");
                        String firstName = userJsonObject.optString("first_name");
                        String lastName = userJsonObject.optString("last_name");
                        String profilePic = userJsonObject.optString("profile_pic");
                        String dob = userJsonObject.optString("dob");
                        String gender = userJsonObject.optString("gender");
                        String city = userJsonObject.optString("city");
                        boolean activated = userJsonObject.optString("activated").equals("1");
                        Utility.setUserId(activity,id);
                        Utility.setUserFirstName(activity,firstName);
                        Utility.setUserLasstName(activity,lastName);
                        Utility.setUserPic(activity,profilePic);
                        Utility.setUserCity(activity,city);
                        Utility.setUserDOB(activity,dob);
                        Utility.setUserEmail(activity, email);
                        Utility.setUserGender(activity,gender);
                        Utility.setUserMobile(activity,"");
                        Utility.setUserEducation(activity,"");
                        Utility.setUserActivated(activity,activated);
                        Utility.setRewardPoints(activity,"0");
                        Intent intent = new Intent(activity, Activity_class.class);
                        Log.e("TAG", "onResponse: "+ getIntent().getStringExtra("refid"));
                        setRewData(id, firstName+" "+lastName);
                        if(getIntent().getStringExtra("refid").compareTo("")!=0)
                            getrewards();
                       // Log.e("TAG", "onResponse: "+ getIntent().getStringExtra("refid"));
                        //intent.putExtra("refid",getIntent().getStringExtra("refid"));
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(activity, "Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("registration_error","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("first_name",firstName);
                params.put("last_name",lastName);
                params.put("password",password);
                params.put("facebook_id",source.equals("facebook")?id:"");
                params.put("google_id",source.equals("google")?id:"");
                params.put("dob",dob);
                params.put("gender",gender);
                params.put("city",city);
                String image = getStringImage();
                if(image.length() > 0) {
                    params.put("profile_pic", image);
                }
                Log.e("request",params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public void setRewData(String id, String name)
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "reward_points", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("setdata", response);


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
                        params.put("rewards_point", "0");
                        params.put("reward_number", "0");
                        params.put("name", name);
                        Log.e("request",params.toString());
                        return params;
                    }
                };
                VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest1);
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
                params.put("rewards_point", "0");
                params.put("rewards_number", "0");
                params.put("name", name);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if(requestCode == 123) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);
            if(bitmap != null) {

                circleImageView.setImageBitmap(bitmap);
            }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.e("result uri", resultUri.toString());
                try {
                    this.bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    circleImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public String getStringImage() {
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
        return "";
    }

    @Override
    public void onLocationChanged(Location location) {
        if (progressDialogCustom != null && progressDialogCustom.isShowing())
            progressDialogCustom.dismiss();
        locationManager.removeUpdates(this);

        try {
            Geocoder gcd = new Geocoder(activity, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityEditTex.setText(addresses.get(0).getLocality());
            } else {
                Toast.makeText(activity, "Unable to fetch city.", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(activity, "Unable to fetch city.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void startLocationUpdates()
    {

    }
}
