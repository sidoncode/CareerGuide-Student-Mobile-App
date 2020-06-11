package com.careerguide.Book_One_To_One.customSteppers;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.Book_One_To_One.activity.NewOneToOneRegisteration;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.careerguide.models.Counsellor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ernestoyaquello.com.verticalstepperform.Step;

public class SummaryStepper extends Step<String> {

    private View summaryStepperView;
    private CircleImageView profileImage;
    private TextView host_name,experience,scheduleDesc,textviewDate,textViewPackageCost,textViewTotal,bookingFor;

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
    }

    @Override
    protected View createStepContentLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        summaryStepperView = inflater.inflate(R.layout.one_to_one_summary_stepper, null, false);

        profileImage=summaryStepperView.findViewById(R.id.profileImage);
        host_name=summaryStepperView.findViewById(R.id.host_name);
        experience=summaryStepperView.findViewById(R.id.experience);
        scheduleDesc=summaryStepperView.findViewById(R.id.scheduleDesc);
        textviewDate=summaryStepperView.findViewById(R.id.textviewDate);
        textViewPackageCost=summaryStepperView.findViewById(R.id.textViewPackageCost);
        textViewTotal=summaryStepperView.findViewById(R.id.textViewTotal);
        bookingFor=summaryStepperView.findViewById(R.id.bookingFor);


        return summaryStepperView;
    }

    @Override
    protected void onStepOpened(boolean animated) {
        new TaskFetchCounsellor().execute();
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


    private class TaskFetchCounsellor extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... params) {

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();
            Glide.with(getContext()).load(((NewOneToOneRegisteration)getContext()).getHostImageUrl()).apply(options).into(profileImage);
            host_name.setText(((NewOneToOneRegisteration)getContext()).getHostFullName());
            experience.setText("10 years Experience");



            scheduleDesc.setText("Personal counseling session \n at "+((NewOneToOneRegisteration)getContext()).getSelectTimeSlot());
            textviewDate.setText(((NewOneToOneRegisteration)getContext()).getSelectedDate());
            textViewPackageCost.setText("Rs."+((NewOneToOneRegisteration)getContext()).getPackageCost());
            bookingFor.setText("For "+((NewOneToOneRegisteration)getContext()).getMenteeName());
            textViewTotal.setText("Rs."+((NewOneToOneRegisteration)getContext()).getPackageCost());
        }
    }


}
