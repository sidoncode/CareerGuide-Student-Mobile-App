package com.careerguide.Book_One_To_One.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.Book_One_To_One.customSteppers.BatchSlotStepper;
import com.careerguide.Book_One_To_One.customSteppers.DescriptionStepper;
import com.careerguide.Book_One_To_One.customSteppers.NameEmailStepper;
import com.careerguide.Book_One_To_One.customSteppers.SummaryStepper;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;

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
    private BatchSlotStepper batchSlotStepper;
    private SummaryStepper summaryStepper;


    //description stepper data
    private String packageDescription="";
    private String packageCost="";
    private String packageName="";
    private JSONArray batchSlot=new JSONArray();


    private String selectedDate="20th June";
    private String selectTimeSlot="";
    private String menteeName="";
    private String menteeEmail="";
    private String packageDiscount="";
    private String hostFullName="";
    private String hostId="";
    private String hostImageUrl="";
    private String channelName="";
    private String selectedCategory="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_one_to_one_registeration);

        descriptionStepper =new DescriptionStepper("Package Details");
        nameEmailStepper=new NameEmailStepper("Mentee Info");
        batchSlotStepper=new BatchSlotStepper("Select Batch timing");
        summaryStepper=new SummaryStepper("Summary details","","Book Slot Now");

        verticalStepperForm=findViewById(R.id.stepper_form);

        new TaskFetchPackage().execute();

        verticalStepperForm.setup(this,descriptionStepper,nameEmailStepper,batchSlotStepper,summaryStepper).init();

    }

    @Override
    public void onCompletedForm() {

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


    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }





    private class TaskFetchPackage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/fetchOnToOnePackage", response -> {

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

                        setBatchSlot(jsonObject.optJSONArray("batchSlot"));


                        descriptionStepper.populateData();//after data is fetched populate it

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



}
