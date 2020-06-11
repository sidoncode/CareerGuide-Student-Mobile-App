package com.careerguide.Book_One_To_One.customSteppers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.Book_One_To_One.adapter.OneToOneBatchSlotAdapter;
import com.careerguide.Book_One_To_One.model.OneToOneBatchSlotModel;
import com.careerguide.Book_One_To_One.model.OneToOneTimeSlotModel;
import com.careerguide.R;

import java.util.ArrayList;

import ernestoyaquello.com.verticalstepperform.Step;

public class BatchSlotStepper extends Step<String> {

    private View batchSlotStepperView;

    private ArrayList<OneToOneBatchSlotModel> oneToOneBatchSlotModelArrayList = new ArrayList<>();
    private OneToOneBatchSlotAdapter oneToOneBatchSlotAdapter;
    private RecyclerView recyclerViewBatchSlot;
    private LinearLayoutManager linearLayoutManager;


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
        return new IsDataValid(true);
    }

    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        batchSlotStepperView = inflater.inflate(R.layout.one_to_one_batch_slot_stepper, null, false);

/*

        ArrayList b1=new ArrayList<OneToOneTimeSlotModel>();
        b1.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b1.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b1.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b1.add(new OneToOneTimeSlotModel("09:00 AM",false));
        b1.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b1.add(new OneToOneTimeSlotModel("09:00 AM",true));
        oneToOneBatchSlotModelArrayList.add(new OneToOneBatchSlotModel("1","9-12PM",b1));

        ArrayList b2=new ArrayList<OneToOneTimeSlotModel>();
        b2.add(new OneToOneTimeSlotModel("09:00 AM",false));
        b2.add(new OneToOneTimeSlotModel("09:00 AM",false));
        b2.add(new OneToOneTimeSlotModel("09:00 AM",false));
        b2.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b2.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b2.add(new OneToOneTimeSlotModel("19:00 AM",true));
        b2.add(new OneToOneTimeSlotModel("29:00 AM",true));
        b2.add(new OneToOneTimeSlotModel("09:00 AM",true));
        oneToOneBatchSlotModelArrayList.add(new OneToOneBatchSlotModel("2","12-3PM",b2));


        ArrayList b3=new ArrayList<OneToOneTimeSlotModel>();
        b3.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b3.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b3.add(new OneToOneTimeSlotModel("09:00 AM",false));
        b3.add(new OneToOneTimeSlotModel("09:00 AM",false));
        b3.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b3.add(new OneToOneTimeSlotModel("09:00 AM",true));
        b3.add(new OneToOneTimeSlotModel("09:00 AM",true));


        oneToOneBatchSlotModelArrayList.add(new OneToOneBatchSlotModel("3","3-6PM",b3));
*/

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
}
