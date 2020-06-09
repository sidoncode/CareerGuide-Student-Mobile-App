package com.careerguide.Book_One_To_One.customSteppers;

import android.view.LayoutInflater;
import android.view.View;

import com.careerguide.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class DescriptionStepper extends Step<String> {

    private View descriptionStepperView;

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
        return new IsDataValid(true);
    }

    @Override
    protected View createStepContentLayout() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        descriptionStepperView = inflater.inflate(R.layout.one_to_one_desc_stepper, null, false);

        return descriptionStepperView;
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
