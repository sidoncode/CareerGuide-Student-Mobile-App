package com.careerguide.Book_One_To_One.customSteppers;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.careerguide.Book_One_To_One.activity.NewOneToOneRegisteration;
import com.careerguide.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class DescriptionStepper extends Step<String> {

    private View descriptionStepperView;
    private TextView tv_package_description;

    public DescriptionStepper(String title) {
        super(title);
    }

    protected DescriptionStepper(String title, String subtitle) {
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
        return new IsDataValid(false);
    }

    @Override
    protected View createStepContentLayout() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        descriptionStepperView = inflater.inflate(R.layout.one_to_one_desc_stepper, null, false);

        tv_package_description=descriptionStepperView.findViewById(R.id.tv_package_description);

        return descriptionStepperView;
    }

    @Override
    protected void onStepOpened(boolean animated) {
        if (!tv_package_description.getText().toString().contains("Loading")){
            getFormView().markOpenStepAsCompleted(true);
        }
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

    public void populateData(){
        tv_package_description.setText(((NewOneToOneRegisteration)getContext()).getPackageDescription());
        getFormView().markOpenStepAsCompleted(true);
    }
}
