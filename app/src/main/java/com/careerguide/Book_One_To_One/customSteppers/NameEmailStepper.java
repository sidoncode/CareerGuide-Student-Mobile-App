package com.careerguide.Book_One_To_One.customSteppers;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.careerguide.Book_One_To_One.activity.NewOneToOneRegisteration;
import com.careerguide.R;
import com.careerguide.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ernestoyaquello.com.verticalstepperform.Step;

public class NameEmailStepper extends Step<String> {

    private View nameEmailStepperView;

    private TextView tv_bookForYou,tv_bookForOther;
    private EditText menteeName,menteeEmail;

    private int errorCodeName=1;
    private int errorCodeEmail=1;
    private int errorCode=2;

    public NameEmailStepper(String title) {
        super(title);
    }

    protected NameEmailStepper(String title, String subtitle) {
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
        nameEmailStepperView = inflater.inflate(R.layout.one_to_one_name_email_stepper, null, false);

        tv_bookForYou=nameEmailStepperView.findViewById(R.id.tv_bookForYou);
        tv_bookForOther=nameEmailStepperView.findViewById(R.id.tv_bookForOther);

        menteeName=nameEmailStepperView.findViewById(R.id.menteeName);
        menteeEmail=nameEmailStepperView.findViewById(R.id.menteeEmail);


        tv_bookForYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menteeName.setText(Utility.getUserFirstName((NewOneToOneRegisteration)getContext())+" "+Utility.getUserLastName((NewOneToOneRegisteration)getContext()));
                menteeEmail.setText(Utility.getUserEmail((NewOneToOneRegisteration)getContext()));
                menteeName.setEnabled(false);
                menteeEmail.setEnabled(false);

                menteeEmail.setError(null);
                menteeName.setError(null);

                tv_bookForOther.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                tv_bookForYou.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_blue));
                tv_bookForYou.setPadding(12,12,12,12);
                tv_bookForOther.setPadding(12,12,12,12);
            }
        });

        tv_bookForOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menteeName.setEnabled(true);
                menteeEmail.setEnabled(true);
                menteeName.setText("");
                menteeEmail.setText("");
                tv_bookForYou.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_grey));
                tv_bookForOther.setBackground(getContext().getResources().getDrawable(R.drawable.round_corner_blue));
                tv_bookForYou.setPadding(12,12,12,12);
                tv_bookForOther.setPadding(12,12,12,12);
            }
        });


        menteeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count<3){
                    menteeName.setError("Name must have more than 3 chars");
                    errorCodeName=1;
                }else
                    errorCodeName=0;

                if (errorCodeName+errorCodeEmail==0){
                    getFormView().markOpenStepAsCompleted(true);
                }else
                    getFormView().markOpenStepAsUncompleted(true,"Fields need attention");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        menteeEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

                Pattern pattern = Pattern.compile(regex);

                Matcher matcher = pattern.matcher(s.toString());
                matcher.matches();

                if (!matcher.matches()){
                    menteeEmail.setError("Invalid Email");
                    errorCodeEmail=1;
                }else
                    errorCodeEmail=0;

                if (errorCodeName+errorCodeEmail==0){
                    getFormView().markOpenStepAsCompleted(true);
                }else
                    getFormView().markOpenStepAsUncompleted(true,"Fields need attention");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return nameEmailStepperView;

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
