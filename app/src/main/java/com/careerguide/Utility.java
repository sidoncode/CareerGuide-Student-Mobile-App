package com.careerguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.Book_One_To_One.activity.OneToOneSessionActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gaurav Gupta(9910781299) on 30/Dec/17-Saturday.
 */

public class Utility extends Application
{

    public static final String albinoServerIp="https://d67d300c269b.ngrok.io";

    public static final String PRIVATE_SERVER = "https://app.careerguide.com/api/main/";

    //public static final String browserKey = "AIzaSyC2VcqdBaKakTd7YLn4B9t3dxWat9UHze4";//rachit api key for youtube
    public static final String browserKey = "AIzaSyBHsYVOxUjVlLhTbZwA7FgWqjUIjjfOld8";//albino api key for youtube


    //storage permission code
    private static final int PERMISSION_REQUEST_CODE = 1;

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
        editor.putString("id", id).commit();
        Log.i("sharedpreid",sharedPreferences.getString("id","0"));
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
    public static void setRewardPoints(Activity activity, String rp)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("reward", rp).apply();
    }

    public static void setNumReferrals(Activity activity, String rp)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("numref", rp).apply();
    }
    public static void setUserStreak(Activity activity, String score)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("score", score).apply();
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
        return sharedPreferences.getString("education","Select Education Level");
    }

    public static String getUserEducationUid(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("educationUid","");
    }
    public static String getRewardPoints(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("reward","");
    }

    public static String getNumReferrals(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("numref","");
    }

    public static String getUserStreak(Activity activity)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("score","");
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

    public static void keepTrackOfTimeWithServer(final Activity activity)
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
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.albinoServerIp + "/FoodRunner-API/foodrunner/v2/careerguide/fetch_time.php",
                            response -> {

                                try {
                                    JSONObject jsonObject= new JSONObject(response+"");
                                    String serverDate=jsonObject.getString("date");
                                    String serverTime=jsonObject.getString("time");

                                    ((OneToOneSessionActivity)activity).updateTimer(serverDate,serverTime);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            },
                            error -> Log.e("server_time","error"))
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> params = new HashMap<>();

                            return params;
                        }
                    };
                    VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 6000);

    }


    public static void stopTimer(){
        timer.cancel();
    }



    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public static String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public static String getFetaureImageUrl(String rendered) {

        String imgurl;
        Pattern r = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = r.matcher(rendered);
        if (m.find()) {
            Log.i("0",m.group(1));
            imgurl = m.group(1);
        }
        else {
            imgurl = "http://localsplashcdn.wpengine.netdna-cdn.com/wp-content/uploads/2013/04/The-page-wont-load.png";
        }

        return imgurl;
    }

    //check in Downloads folder only
    public static Boolean checkFileExist(String fileName){
        String filename = "/Download/"+fileName;
        File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        if(!f1.exists() && !f1.isDirectory()){
            return  false;
        }else {
            return true;
        }
    }

    //Download file to Downloads folder, returns the id(long) of the file being downloaded
    public static long downloadPdf(String fileName,String url,String downloadTitle,String downloadDescription,Activity currentActivity){
        try {
            DownloadManager downloadmanager = (DownloadManager)currentActivity.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(downloadTitle);
            request.setDescription(downloadDescription);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+fileName);
            request.setDestinationUri(Uri.fromFile(file));
            return (downloadmanager.enqueue(request));

        }catch (Exception e){
            Toast.makeText(currentActivity.getApplicationContext(), "Couldn't download file, Try again!",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return (-1);
        }
    }


        //Download file to Downloads folder, returns the id(long) of the file being downloaded
        public static long downloadImage(String fileName,String url,Activity currentActivity) {
            try {
                DownloadManager downloadmanager = (DownloadManager) currentActivity.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setVisibleInDownloadsUi(false);
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Download/" + fileName);
                request.setDestinationUri(Uri.fromFile(file));
                return (downloadmanager.enqueue(request));

            } catch (Exception e) {
                Toast.makeText(currentActivity.getApplicationContext(), "Couldn't download file, Try again!",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return (-1);
            }
        }

    public static File getFile(String fileName){
        String filename = "/Download/"+fileName;
        File f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        if(f1.exists() && !f1.isDirectory()){
            return  f1;
        }else {
            return null;
        }
    }


    public static boolean getStoragePermissionFromUser(Activity activity){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission(activity))
            {
                return  true;
            } else {
                requestPermission(activity); // Code for permission
                return checkPermission(activity);
            }
        }else
        {
                if(checkPermissionLess22API(activity)){
                    return true;
                }
                else{
                    return false;
                }
        }

    }


    public static boolean checkPermissionLess22API(Activity activity) {
        String permissionToCheck = "android.permission.WRITE_EXTERNAL_STORAGE";

        int permission = PermissionChecker.checkSelfPermission(activity.getApplicationContext(), permissionToCheck);

        if (permission == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());

            builder.setTitle("Permission")
                    .setMessage("Grant storage permission")
                    .setCancelable(false)
                    .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);//open wifi settings
                            activity.startActivity(settingsIntent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            Toast.makeText(activity.getApplicationContext(), "You have not granted permission!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    }


        public static boolean checkPermission (Activity activity){
            int result = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public static void requestPermission (Activity activity){

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "Write External Storage permission allows us to do store reports/E-Books. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }

        public static boolean deleteOldReport(String fileName){

            File oldReport=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Download/"+fileName);
                if (oldReport.delete()) {
                    System.out.println("Report Deleted");
                    return true;
                } else {
                    System.out.println("Report not Deleted");
                    return false;
                }
            }


}



