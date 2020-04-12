package com.careerguide;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Gaurav Gupta(9910781299) on 30/Dec/17-Saturday.
 */

public class Utility extends Application
{
    public static final String PRIVATE_SERVER = "https://app.careerguide.com/api/main/";


    public static ArrayList<QuestionAndOptions> questionAndOptionses = new ArrayList<>();
    public static ArrayList<String> paragraphs = new ArrayList<>();
    public static ArrayList<String> sectionSet = new ArrayList<>();
    public static String backgroundColors[] = {"#F44336","#E91E63","#9C27B0","#673AB7","#009688"};


    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Timer timer = new Timer();

    public static int getPx(int dp)
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        //return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, displaymetrics );
    }

    public static void saveAnswer(Activity activity)
    {
        ArrayList<Answer> answers = new ArrayList<>();
        for (QuestionAndOptions questionAndOptions : questionAndOptionses)
        {
            if (questionAndOptions.getAnswerKey().equals(""))
            {
                break;
            }
            else
            {
                answers.add(new Answer(questionAndOptions.getQuestion().getSrNo(),questionAndOptions.getAnswerKey()));
            }
        }
        SharedPreferences sharedPreferences = activity.getSharedPreferences("answer",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String test = gson.toJson(answers);
       // Log.e("formed String", test);
        editor.putString("ideal",test);
        editor.apply();

    }

    public static void setSavedAnswer(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("answer",MODE_PRIVATE);
        String test = sharedPreferences.getString("ideal","");
        Log.e("parsed String", test);
        if (test.length() > 0)
        {
            ArrayList<Answer> answers = new Gson().fromJson(test,new TypeToken<List<Answer>>() {}.getType());
            for (Answer answer : answers)
            {
                questionAndOptionses.get(answer.getsNO()-1).setAnswerKey(answer.getKey());
               // Log.e("parsed loop", answer.getsNO() + "");
                //Log.e("test",question.getTitle() + " test");
            }
            if (answers.size() > 0)
            {
               // Log.e("parsed size", answers.size() + "");
            }
        }
        //return false;
    }

    public static boolean isAnswerAvailable(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("answer",MODE_PRIVATE);
        String test = sharedPreferences.getString("ideal","");
        return test.length()>0;
    }

    public static void clearAnswer(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("answer",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("ideal");
        editor.apply();
        for (QuestionAndOptions questionAndOptions : questionAndOptionses)
        {
            questionAndOptions.setAnswerKey("");
        }
    }

    public static void setUserId(Activity activity, String id)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id).apply();
        FirebaseApp.initializeApp(activity);
        FirebaseMessaging.getInstance().subscribeToTopic("cgs" + id);
    }

    public static void setUserFirstName(Activity activity, String name)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name).apply();
    }

    public static void setUserLasstName(Activity activity, String name)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastName", name).apply();
    }

    public static void setUserPic(Activity activity, String pic)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pic", pic).apply();
    }

    public static void setUserAuthKey(Activity activity, String id)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authKey", id).apply();
    }

    public static void setUserEmail(Activity activity, String email)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email).apply();
    }

    public static void setUserDOB(Activity activity, String dob)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dob", dob).apply();
    }

    public static void setUserGender(Activity activity, String gender)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("gender", gender).apply();
    }

    public static void setUserCity(Activity activity, String city)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city", city).apply();
    }

    public static void setUserActivated(Activity activity, boolean activated)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("activated", activated).apply();
    }

    public static void setUserMobile(Activity activity, String city)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", city).apply();
    }

    public static void setUserEducation(Activity activity, String education)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("education", education).apply();
    }

    public static void setEducationUid(Activity activity, String educationUid)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("educationUid", educationUid).apply();
    }

    public static void setIcon_url(Activity activity, String icon_url)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("icon_url", icon_url).apply();
    }

    public static String  getIcon_url(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("icon_url","");
    }

    public static String getUserId(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("id","");
    }

    public static String getUserPic(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("pic","");
    }

    public static String getUserFirstName(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("name","");
    }

    public static String getUserLastName(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("lastName","");
    }

    public static String getUserAuth(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("authKey","");
    }

    public static String getUserEmail(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("email","");
    }

    public static String getUserDOB(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        String dateString = sharedPreferences.getString("dob","");

        if (!dateString.isEmpty()) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            final SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            final Calendar calendar = Calendar.getInstance();
            try {
                Date currentDOB = dateFormat.parse(dateString);
                calendar.setTime(currentDOB);
                dateString = dateFormatNew.format(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateString;
    }

    public static String getUserGender(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("gender","");
    }

    public static String getUserCity(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("city","");
    }

    public static boolean getUserActivated(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getBoolean("activated",false);
    }

    public static String getUserMobile(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("mobile","");
    }

    public static String getReportUrl(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("Report_url","");
    }


    public static String getUserEducation(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("education","");
    }

    public static String getUserEducationUid(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("educationUid","");
    }

    public static boolean logOut(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.edit().clear().commit();
    }

    public static String stringFromBitmap(Bitmap bitmap)
    {
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
        return "";
    }

    public static Bitmap getBackgroundBitmap(char ch,int size)
    {
        int ascii = ch%backgroundColors.length;
        int color = Color.parseColor(backgroundColors[ascii]);
        Bitmap bmp = Bitmap.createBitmap(Utility.getPx(size),Utility.getPx(size),Bitmap.Config.ARGB_8888);
        bmp.eraseColor(color);
        return bmp;
    }

    public static Date getDateFromString(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.getDefault());
        //Log.e("GMT", TimeZone.getTimeZone("GMT").getDisplayName());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance().getTime();
    }

    public static String getStringImage(Bitmap bitmap) {
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
        return "";
    }

    public static void handleOnlineStatus(final Activity activity, final String status)
    {
        timer.cancel();
        if (activity != null)
        {
            timer = new Timer();
        }
        else
        {
            return;
        }
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "online_status", response -> Log.e("online_status_response", response), error -> Log.e("online_status_error","error"))
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> params = new HashMap<>();
                            params.put("user_id",getUserId(activity));
                            params.put("status",status);
                            Log.e("online_status_request",params.toString());
                            return params;
                        }
                    };
                    VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 6000);
    }
}
