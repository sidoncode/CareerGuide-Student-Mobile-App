package com.careerguide.Book_One_To_One.customSteppers;

import android.view.LayoutInflater;
import android.view.View;

import com.careerguide.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class SummaryStepper extends Step<String> {

    private View summaryStepperView;

    protected SummaryStepper(String title) {
        super(title);
    }

    protected SummaryStepper(String title, String subtitle) {
        super(title, subtitle);
    }

    public SummaryStepper(String title, String subtitle, String nextButtonText) {
        super(title, subtitle, nextButtonText);
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
        //return new IsDataValid(false,"Filll");
    }

    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        summaryStepperView = inflater.inflate(R.layout.one_to_one_summary_stepper, null, false);

        return summaryStepperView;
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
