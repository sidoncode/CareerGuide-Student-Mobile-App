package com.careerguide.Book_One_To_One.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.Book_One_To_One.customSteppers.BatchSlotStepper;
import com.careerguide.Book_One_To_One.customSteppers.DescriptionStepper;
import com.careerguide.Book_One_To_One.customSteppers.NameEmailStepper;
import com.careerguide.Book_One_To_One.customSteppers.SummaryStepper;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class NewOneToOneRegisteration extends AppCompatActivity implements StepperFormListener {

    private VerticalStepperFormView verticalStepperForm;

    private DescriptionStepper descriptionStepper;
    private NameEmailStepper nameEmailStepper;
    public BatchSlotStepper batchSlotStepper;
    private SummaryStepper summaryStepper;

    Activity activity=this;


    //description stepper data
    private String packageDescription="";
    private String packageCost="";
    private String packageName="";
    private JSONArray batchSlot=new JSONArray();


    private String selectedDate="";
    private String selectTimeSlot="";
    private String menteeName="";
    private String menteeEmail="";
    private String packageDiscount="";
    private String hostFullName="";
    private String hostId="";
    private String hostImageUrl="";
    private  String hostEmail="";
    private String channelName="";
    private String selectedCategory="";
    private String deepLink="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_one_to_one_registeration);

        descriptionStepper =new DescriptionStepper("Package Details");
        nameEmailStepper=new NameEmailStepper("Mentee Info");
        batchSlotStepper=new BatchSlotStepper("Select Batch timing");
        summaryStepper=new SummaryStepper("Summary details","","Confirm Details");

        verticalStepperForm=findViewById(R.id.stepper_form);

        new TaskFetchPackage().execute();

        verticalStepperForm.setup(this,descriptionStepper,nameEmailStepper,batchSlotStepper,summaryStepper).init();

    }

    @Override
    public void onCompletedForm() {

        createDynamicLink("-1");

    }

    @Override
    public void onCancelledForm() {

    }

    public String getPackageDescription() { return packageDescription; }
    public void setPackageDescription(String packageDescription) { this.packageDescription = packageDescription; }


    public String getPackageNamee() { return packageName; }
    public void setPackageNamee(String packageName) { this.packageName = packageName; }


    public String getPackageCost() {
        return packageCost;
    }
    public void setPackageCost(String packageCost) {
        this.packageCost = packageCost;
    }


    public String getPackageDiscount() {
        return packageDiscount;
    }
    public void setPackageDiscount(String packageDiscount) { this.packageDiscount = packageDiscount; }


    public JSONArray getBatchSlot() { return batchSlot; }
    public void setBatchSlot(JSONArray batchSlot) { this.batchSlot = batchSlot; }


    public String getMenteeName() {
        return menteeName;
    }
    public void setMenteeName(String menteeName) {
        this.menteeName = menteeName;
    }


    public String getMenteeEmail() {
        return menteeEmail;
    }
    public void setMenteeEmail(String menteeEmail) {
        this.menteeEmail = menteeEmail;
    }

    public String getSelectedCategory() { return selectedCategory; }

    public void setSelectedCategory(String selectedCategory) { this.selectedCategory = selectedCategory; }

    public String getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }


    public String getSelectTimeSlot() {
        return selectTimeSlot;
    }
    public void setSelectTimeSlot(String selectTimeSlot) {
        this.selectTimeSlot = selectTimeSlot;
    }


    public String getHostFullName() {
        return hostFullName;
    }
    public void setHostFullName(String hostFullName) { this.hostFullName = hostFullName; }


    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }


    public String getHostImageUrl() {
        return hostImageUrl;
    }
    public void setHostImageUrl(String hostImageUrl) { this.hostImageUrl = hostImageUrl; }


    public String getHostEmail() { return hostEmail; }
    public void setHostEmail(String hostEmail) { this.hostEmail = hostEmail; }


    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }


    public String getDeepLink() { return deepLink; }
    public void setDeepLink(String deepLink) { this.deepLink = deepLink; }



    public void createDynamicLink(String booking_id) {

        String channelNameTemp=getHostEmail() + "_private_" + getSelectedDate() + "_" + getSelectTimeSlot();
        channelNameTemp=channelNameTemp.replace(" ","_");
        channelNameTemp=channelNameTemp.replace(":","_");
        setChannelName(channelNameTemp);


        Log.i("channel__name",getChannelName());

            String host_image_file=getHostImageUrl();
            host_image_file.substring(host_image_file.lastIndexOf('/') + 1);

            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://play.google.com/store/apps/details?id=com.careerguide&hl=en_US&sessionDetails={\"channel_name\":\""+getChannelName()+"\",\"host_name\":\""+getHostFullName()+"\",\"host_image\":\""+host_image_file+"\",\"privateUID\":\""+Utility.getUserId(this)+"\",\"privateUserName\":\""+getMenteeName()+"\",\"privateSessionDate\":\""+getSelectedDate()+"\",\"privateSessionTime\":\""+getSelectTimeSlot()+"\",\"booking_id\":\""+booking_id+"\"}"))
                    .setDynamicLinkDomain("careerguideprivatesession.page.link")
                    // Open links with this app on Android
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.careerguide").build())
                    .setGoogleAnalyticsParameters(
                            new DynamicLink.GoogleAnalyticsParameters.Builder()
                                    .setSource("video")
                                    .setMedium("anyone")
                                    .setCampaign("example-video")
                                    .build())
                    .buildDynamicLink();
            Log.e("main", "Long refer Link"+ dynamicLink.getUri());
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(dynamicLink.getUri())
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("main","short Link" + shortLink);
                            Log.e("main","short Link" + flowchartLink);
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());

                            setDeepLink(shortLink+"");
                            new TaskBookSlotBeforePayment().execute();

                        } else
                        {
                            Log.e("Error","error--> "+task.getException());
                            // Error
                            // ...
                        }
                    });

    }



    private class TaskFetchPackage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, /*"https://app.careerguide.com/api/main/fetchOnToOnePackage"*/"https://f6185d0f8cb4.ngrok.io/FoodRunner-API/foodrunner/v2/careerguide/fetch_one_to_one_package.php",
                    response -> {

                Log.e("all_coun_res_counsellor", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("response->",jsonObject+"");
                    boolean status = jsonObject.optBoolean("success", false);
                    if (status) {

                        setPackageDescription(jsonObject.optString("package_desc"));

                        setPackageCost(jsonObject.optString("packageCost"));

                        //discount left for now

                        setPackageNamee(jsonObject.optString("packageName"));

                        Log.i("aaaa",jsonObject.optJSONArray("batchSlot")+"");

                        setBatchSlot(jsonObject.optJSONArray("batchSlot"));


                        descriptionStepper.populateData();//after data is fetched populate it

                        String today=jsonObject.optJSONArray("batchSlot").getJSONObject(0).getString("day_ame");
                        String tomorrow=jsonObject.optJSONArray("batchSlot").getJSONObject(1).getString("day_ame");
                        String dayafter=jsonObject.optJSONArray("batchSlot").getJSONObject(2).getString("day_ame");

                        batchSlotStepper.setDays(today,tomorrow,dayafter);


                    } else {
                        Log.i("sssss","asasas");
                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                    //pb_loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                // pb_loading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), VoleyErrorHelper.getMessage(error, getApplicationContext()), Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror", "error");
                error.printStackTrace();
            }) {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            return null;

        }
    }


    private class TaskBookSlotBeforePayment extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            try {


                JSONObject jsonBody = new JSONObject();

                jsonBody.put("co_id", getHostId());
                jsonBody.put("student_id", Utility.getUserId(activity));
                jsonBody.put("date_booked", getSelectedDate());
                jsonBody.put("time_slot", getSelectTimeSlot());
                jsonBody.put("price", getPackageCost());
                jsonBody.put("discount_availed", "0");
                jsonBody.put("confirmed_booking", "0");
                jsonBody.put("channel_name", getChannelName());
                jsonBody.put("category", getSelectedCategory());
                jsonBody.put("deep_link", getDeepLink());
//com.careerguide I/jsonbodyy: {"co_id":"43","student_id":"13599","date_booked":"Saturday 13 Jun 20","time_slot":"11:15AM","price":"1999","discount_availed":"0","confirmed_booking":"0","channel_name":"rachit@careerguide.com_privatesession_Saturday 13 Jun 20_11:15AM","category":"B.Tech","deek_link":"https:\/\/careerguidelivestream.page.link\/tGw7qVRWCHGQRzQn9"}

                Log.i("jsonbodyy",jsonBody+"");

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, /*"https://app.careerguide.com/api/main/bookOneToOne"*/"https://f6185d0f8cb4.ngrok.io/FoodRunner-API/foodrunner/v2/careerguide/book_one_to_one.php",jsonBody, response -> {


                try {
                    JSONObject jsonObject = new JSONObject(response+"");
                    Log.i("response->",jsonObject+"");
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status) {

                    String booking_id=jsonObject.getString("booking_id");
                    createDynamicLink(booking_id);
                        ((NewOneToOneRegisteration)activity).runOnUiThread(()->{
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Session Link", getDeepLink());
                            clipboard.setPrimaryClip(clip);

                            Toast.makeText(getApplicationContext(),"Session booked! and copied to clipboard",Toast.LENGTH_SHORT).show();
                            finish();
                        });

                    } else {
                        Log.i("sssss","asasas");
                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    }
                    //pb_loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                // pb_loading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), VoleyErrorHelper.getMessage(error, getApplicationContext()), Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror", "error");
                error.printStackTrace();
            }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Basic ZTg1YWQyZjg3Mzc0NDc5ZWE5ZjZhMTE0MmY5NTRjZjc6YjdiZTUxM2Q4ZDI0NGFiNWFlYWU0ZWQxNWYwZDIyNWM=");
                        return headers;
                    }
                };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }


    }



}

//https://cd904a319c3f.ngrok.io/FoodRunner-API/foodrunner/v2/careerguide/fetch_bookings_for_counselor.php
//https://cd904a319c3f.ngrok.io/FoodRunner-API/foodrunner/v2/careerguide/book_one_to_one.php
//https://cd904a319c3f.ngrok.io/FoodRunner-API/foodrunner/v2/careerguide/fetch_one_to_one_package.php