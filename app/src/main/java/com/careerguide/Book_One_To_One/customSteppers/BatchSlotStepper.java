package com.careerguide.Book_One_To_One.customSteppers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.Book_One_To_One.activity.NewOneToOneRegisteration;
import com.careerguide.Book_One_To_One.adapter.OneToOneBatchSlotAdapter;
import com.careerguide.Book_One_To_One.model.OneToOneBatchSlotModel;
import com.careerguide.Book_One_To_One.model.OneToOneTimeSlotModel;
import com.careerguide.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ernestoyaquello.com.verticalstepperform.Step;

public class BatchSlotStepper extends Step<String> {

    private View batchSlotStepperView;

    private ArrayList<OneToOneBatchSlotModel> oneToOneBatchSlotModelArrayList = new ArrayList<>();
    private OneToOneBatchSlotAdapter oneToOneBatchSlotAdapter;
    private RecyclerView recyclerViewBatchSlot;
    private LinearLayoutManager linearLayoutManager;

    private TextView today,tomorrow,dayAfter;

    private JSONArray allBatchJSONArray;


    public BatchSlotStepper(String title) {
        super(title);

    }

    protected BatchSlotStepper(String title, String subtitle) {
        super(title, subtitle);
    }

    @Override
    public String getStepData() {
        return null;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    public void restoreStepData(String data) {

    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(false,"Select time slot!");
    }

    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        batchSlotStepperView = inflater.inflate(R.layout.one_to_one_batch_slot_stepper, null, false);



        today=batchSlotStepperView.findViewById(R.id.today);
        tomorrow=batchSlotStepperView.findViewById(R.id.tomorrow);
        dayAfter=batchSlotStepperView.findViewById(R.id.dayAfter);

        ((NewOneToOneRegisteration)getContext()).setSelectedDate(today.getText().toString());

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSlotsForSelectedDate(0);
                today.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_blue));
                tomorrow.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                dayAfter.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                ((NewOneToOneRegisteration)getContext()).setSelectedDate(today.getText().toString());
            }
        });

        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSlotsForSelectedDate(1);
                today.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                tomorrow.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_blue));
                dayAfter.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                ((NewOneToOneRegisteration)getContext()).setSelectedDate(tomorrow.getText().toString());
            }
        });

        dayAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSlotsForSelectedDate(2);
                today.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                tomorrow.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                dayAfter.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_blue));
                ((NewOneToOneRegisteration)getContext()).setSelectedDate(dayAfter.getText().toString());
            }
        });


        recyclerViewBatchSlot=batchSlotStepperView.findViewById(R.id.recyclerViewBatchSlot);
        oneToOneBatchSlotAdapter = new OneToOneBatchSlotAdapter(getContext(), oneToOneBatchSlotModelArrayList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewBatchSlot.setLayoutManager(linearLayoutManager);
        recyclerViewBatchSlot.setAdapter(oneToOneBatchSlotAdapter);


        return batchSlotStepperView;
    }

    @Override
    protected void onStepOpened(boolean animated) {

    }

    @Override
    protected void onStepClosed(boolean animated) {

    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {

    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {

    }

    public void setDays(String todayDay,String tomorrowDay,String dayAfterDay){
        today.setText(todayDay);
        tomorrow.setText(tomorrowDay);
        dayAfter.setText(dayAfterDay);
        today.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_blue));
        tomorrow.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
        dayAfter.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
        ((NewOneToOneRegisteration)getContext()).setSelectedDate(today.getText().toString());
        updateSlotsForSelectedDate(0);
    }

    public void updateSlotsForSelectedDate(int dayCode){

        oneToOneBatchSlotModelArrayList.clear();

        allBatchJSONArray=new JSONArray();
        allBatchJSONArray=((NewOneToOneRegisteration)getContext()).getBatchSlot();

        ArrayList singleBatch;

        try {

            Log.i("batchh",allBatchJSONArray+"");

            //dayCode
            //0 today
            //1 tomorrow
            //2 day after tomorrow

            JSONObject dayJsonObject=allBatchJSONArray.getJSONObject(dayCode);//one day

            JSONArray batch_dataJsonArray=dayJsonObject.getJSONArray("batch_data");//batches under a day

            for (int j=0;j<batch_dataJsonArray.length();j++){//3 batches

                singleBatch=new ArrayList<OneToOneTimeSlotModel>();

                JSONObject batchN=batch_dataJsonArray.getJSONObject(j);//first batch

                JSONArray allBookingSlotArray=batchN.getJSONArray("bookingSlot");

                for (int k=0;k<allBookingSlotArray.length();k++){

                    JSONObject eachBatchJsonObj=allBookingSlotArray.getJSONObject(k);

                    if (eachBatchJsonObj.getBoolean("available")) {
                        JSONArray available_counselor_jsonArray=eachBatchJsonObj.getJSONArray("available_counselor");

                        boolean availableFlag=false;
                        for(int z=0;z<available_counselor_jsonArray.length();z++){

                            if (available_counselor_jsonArray.getJSONObject(z).getString("expertLevel").contains(((NewOneToOneRegisteration)getContext()).getSelectedCategory())){
                                singleBatch.add(new OneToOneTimeSlotModel("" + eachBatchJsonObj.getString("time_slot"), true,available_counselor_jsonArray.getJSONObject(z).getString("co_FullName"),available_counselor_jsonArray.getJSONObject(z).getString("co_id"),available_counselor_jsonArray.getJSONObject(z).getString("email"),"https://app.careerguide.com/api/user_dir/" + available_counselor_jsonArray.getJSONObject(z).getString("profile_pic")));
                                    availableFlag=true;
                                    break;
                            }

                        }
                        if (!availableFlag){
                            singleBatch.add(new OneToOneTimeSlotModel(""+eachBatchJsonObj.getString("time_slot"),false,"","","",""));
                        }

                    }else {
                        singleBatch.add(new OneToOneTimeSlotModel(""+eachBatchJsonObj.getString("time_slot"),false,"","","",""));

                    }




                }
                oneToOneBatchSlotModelArrayList.add(new OneToOneBatchSlotModel(batchN.getString("batchNo"),batchN.getString("batchTiming"),singleBatch));

            }

            oneToOneBatchSlotAdapter.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    public void enableContinueButton(){
        getFormView().markOpenStepAsCompleted(true);
    }

}
