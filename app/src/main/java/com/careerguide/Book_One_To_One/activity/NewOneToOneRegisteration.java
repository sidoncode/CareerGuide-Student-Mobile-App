package com.careerguide.Book_One_To_One.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.careerguide.Book_One_To_One.customSteppers.BatchSlotStepper;
import com.careerguide.Book_One_To_One.customSteppers.DescriptionStepper;
import com.careerguide.Book_One_To_One.customSteppers.NameEmailStepper;
import com.careerguide.Book_One_To_One.customSteppers.SummaryStepper;
import com.careerguide.R;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class NewOneToOneRegisteration extends AppCompatActivity implements StepperFormListener {

    private VerticalStepperFormView verticalStepperForm;

    private DescriptionStepper descriptionStepper;
    private NameEmailStepper nameEmailStepper;
    private BatchSlotStepper batchSlotStepper;
    private SummaryStepper summaryStepper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_one_to_one_registeration);

        descriptionStepper =new DescriptionStepper("Package Details");
        nameEmailStepper=new NameEmailStepper("Mentee Info");
        batchSlotStepper=new BatchSlotStepper("Select Batch timing");
        summaryStepper=new SummaryStepper("Summary details","","Proceed to payment");

        verticalStepperForm=findViewById(R.id.stepper_form);

        verticalStepperForm.setup(this,descriptionStepper,nameEmailStepper,batchSlotStepper,summaryStepper).init();

    }

    @Override
    public void onCompletedForm() {

    }

    @Override
    public void onCancelledForm() {

    }
}
