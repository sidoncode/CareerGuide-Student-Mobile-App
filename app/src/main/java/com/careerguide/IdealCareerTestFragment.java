package com.careerguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class IdealCareerTestFragment extends TestSuperFragment {
    private View view;
    private String educationAtServer;
    private String mobileAtServer;
    private String authKeyAtServer;
    private String phoneNumberEntered;
    private String enteredCode;
    private String enteredCountryFlag;
    private View alertDialogView;
    private boolean testAvailable = false;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    final private int ASSESMENT_REQ_CODE = 1;


    public IdealCareerTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ideal_career_test, container, false);



        view.findViewById(R.id.startButton).setOnClickListener(v -> {
            //showEducation();
            checkActivationStatus();
            //proceedTest();
        });

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            if (bundle.getBoolean("start",false))
            {
                view.findViewById(R.id.startButton).performClick();
            }
        }
        return view;
    }

    private void proceedTest()
    {
        final Activity activity = getActivity();

        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "user_inventry", response -> {
            if (isAdded()) {
                progressDialog.dismiss();
                Log.e("user_invantory_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    String msg = jsonObject.optString("msg");
                    if (status)
                    {
                        JSONArray dataJsonArray = jsonObject.optJSONArray("data");
                        for (int i = 0; i<dataJsonArray.length(); i++)
                        {
                            JSONObject serviceJsonObject = dataJsonArray.getJSONObject(i);
                            int serviceId = serviceJsonObject.optInt("service_id");
                            try {
                                int quantity = Integer.parseInt(serviceJsonObject.optString("quantity"));
                                if (serviceId == 1 && quantity > 0)
                                {
                                    testAvailable = true;
                                    break;
                                }
                            }
                            catch (NumberFormatException ex)
                            {

                            }
                        }
                        final AlertDialog ad = new AlertDialog.Builder(activity).create();
                        LayoutInflater layoutInflater = activity.getLayoutInflater();
                        alertDialogView = layoutInflater.inflate(R.layout.test_information_dialog, null);
                        ad.setView(alertDialogView);
                        Log.e("test Available",testAvailable + "");
                        testAvailable = true;
                        if (testAvailable)
                        {
                            alertDialogView.findViewById(R.id.start).setVisibility(View.VISIBLE);
                            alertDialogView.findViewById(R.id.payNow).setVisibility(View.GONE);
                            alertDialogView.findViewById(R.id.proceed).setVisibility(View.GONE);
                            if (Utility.isAnswerAvailable(activity))
                            {
                                alertDialogView.findViewById(R.id.resume).setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                alertDialogView.findViewById(R.id.resume).setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            alertDialogView.findViewById(R.id.start).setVisibility(View.GONE);
                            alertDialogView.findViewById(R.id.resume).setVisibility(View.GONE);
                            alertDialogView.findViewById(R.id.payNow).setVisibility(View.VISIBLE);
                            alertDialogView.findViewById(R.id.proceed).setVisibility(View.VISIBLE);
                        }

                        alertDialogView.findViewById(R.id.proceed).setOnClickListener(v -> {
                            ad.dismiss();
                            if (Utility.isAnswerAvailable(activity)) {
                                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(activity);
                                adb.setMessage("One test is in progress. Do you want to resume?");
                                adb.setPositiveButton("Resume", (dialog, which) -> {
                                    Intent intent = new Intent(activity, AssessmentActivity.class);
                                    intent.putExtra("auth", authKeyAtServer);
                                    startActivityForResult(intent,ASSESMENT_REQ_CODE);
                                });
                                adb.setNegativeButton("Restart", (dialog, which) -> {
                                    Utility.clearAnswer(activity);
                                    Intent intent = new Intent(activity, AssessmentActivity.class);
                                    intent.putExtra("auth", authKeyAtServer);
                                    startActivityForResult(intent,ASSESMENT_REQ_CODE);
                                });
                                adb.setNeutralButton("Cancel", (dialog, which) -> {
                                });
                                adb.show();
                            } else {
                                Intent intent = new Intent(activity, AssessmentActivity.class);
                                intent.putExtra("auth", authKeyAtServer);
                                startActivityForResult(intent,ASSESMENT_REQ_CODE);
                            }
                        });
                        alertDialogView.findViewById(R.id.close).setOnClickListener(v -> ad.dismiss());
                        alertDialogView.findViewById(R.id.payNow).setOnClickListener(v -> {
                            Toast.makeText(activity, "Feature not yet supported", Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(activity,PlanActivity.class));
                        });
                        alertDialogView.findViewById(R.id.start).setOnClickListener(v -> {
                            Utility.clearAnswer(activity);
                            /*new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            },1000);
                            alertDialogView.findViewById(R.id.resume).setVisibility(View.GONE);*/
                            Intent intent = new Intent(activity, AssessmentActivity.class);
                            intent.putExtra("auth", authKeyAtServer);
                            startActivityForResult(intent,ASSESMENT_REQ_CODE);
                        });
                        alertDialogView.findViewById(R.id.resume).setOnClickListener(v -> {
                            Intent intent = new Intent(activity, AssessmentActivity.class);
                            intent.putExtra("auth", authKeyAtServer);
                            startActivityForResult(intent,ASSESMENT_REQ_CODE);
                        });

                        alertDialogView.findViewById(R.id.sampleReport).setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                            intent.putExtra("url","https://www.careerguide.com/sample-report/ideal-career-report-sample-report-new.pdf");
                            intent.putExtra("filename", "Report");
                            startActivity(intent);
                        });
                        ad.show();
                    } else {
                        Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            progressDialog.dismiss();
            if (isAdded())
                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("user_invantory_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                Log.e("user_invantory_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

        /*fwrfef;
        fwrfer;*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ASSESMENT_REQ_CODE)
        {
            if (testAvailable)
            {
                alertDialogView.findViewById(R.id.start).setVisibility(View.VISIBLE);
                alertDialogView.findViewById(R.id.payNow).setVisibility(View.GONE);
                alertDialogView.findViewById(R.id.proceed).setVisibility(View.GONE);
                if (Utility.isAnswerAvailable(getActivity()))
                {
                    alertDialogView.findViewById(R.id.resume).setVisibility(View.VISIBLE);
                }
                else
                {
                    alertDialogView.findViewById(R.id.resume).setVisibility(View.GONE);
                }
            }
            else
            {
                alertDialogView.findViewById(R.id.start).setVisibility(View.GONE);
                alertDialogView.findViewById(R.id.resume).setVisibility(View.GONE);
                alertDialogView.findViewById(R.id.payNow).setVisibility(View.VISIBLE);
                alertDialogView.findViewById(R.id.proceed).setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkActivationStatus() {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "user_activation_status", response -> {
            if (isAdded()) {
                progressDialog.dismiss();
                Log.e("activation_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    String msg = jsonObject.optString("msg");
                    if (status)
                    {
                        boolean activated = jsonObject.optString("activated").equals("1");
                        mobileAtServer = jsonObject.optString("mobile_number");
                        educationAtServer = jsonObject.optString("education_level");
                        authKeyAtServer = jsonObject.optString("user_auth");
                        if (activated)
                        {
                            if (mobileAtServer.length() == 0)
                            {
                                final AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_mobile_number, null);
                                Spinner spinner = view.findViewById(R.id.countriesSpinner);
                                final ArrayList<Country> countries = new ArrayList<>();
                                JSONArray countriesJsonArray = jsonObject.optJSONArray("flags");
                                for (int i = 0; i<countriesJsonArray.length(); i++)
                                {
                                    JSONObject countryJsonObject = countriesJsonArray.optJSONObject(i);
                                    boolean current = countryJsonObject.optBoolean("selected");
                                    if (current)
                                    {
                                        Log.e("selected ", countryJsonObject.optString("name"));
                                    }
                                    countries.add(new Country(countryJsonObject.optString("name"), countryJsonObject.optString("flag"),"" + countryJsonObject.optInt("code"),current));
                                }

                                spinner.setAdapter(new CustomAdapter(getActivity(),R.layout.country_spinner_item,countries));
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        Country country = countries.get(position);
                                        Log.e("CODE",country.getCountryCode() + " " + country.getCountryName());
                                        enteredCode = country.getCountryCode();
                                        enteredCountryFlag = country.getImage();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                                for (int i = 0; i< countries.size(); i++)
                                {
                                    if (countries.get(i).isCurrent())
                                    {
                                        spinner.setSelection(i);
                                    }
                                }
                                final EditText editText = view.findViewById(R.id.phoneNumberEdit);

                                view.findViewById(R.id.continueButton).setOnClickListener(v -> {
                                    phoneNumberEntered = editText.getText().toString().trim();
                                    if (phoneNumberEntered.length() > 0)
                                    {
                                        updateMobile(phoneNumberEntered);
                                        //uncomment below method to activate OTP functionalty.
                                        //checkSMSPermission();
                                        ad.dismiss();
                                    } else {
                                        Toast.makeText(getActivity(), "Insert phone number", Toast.LENGTH_LONG).show();
                                    }
                                });
                                ad.setView(view);
                                ad.show();
                            }
                            else if (educationAtServer.length() == 0)
                            {
                                showEducation();
                            }
                            else if (authKeyAtServer.length() == 0)
                            {
                                createAuthKey();
                            }
                            else
                            {
                                proceedTest();
                            }
                        } else {
                            final AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
                            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_email_not_verified,null);
                            view.findViewById(R.id.gotIt).setOnClickListener(v -> ad.dismiss());
                            ad.setView(view);
                            ad.show();
                        }
                    } else {
                        Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Invalid response.\nSomething went wrong.",Toast.LENGTH_LONG).show();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            if (isAdded())
                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("activation_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    /*private void checkSMSPermission() {
        if(Build.VERSION.SDK_INT < 23){
            sendOTP(phoneNumberEntered,createOTP(),enteredCode, true);
        }
        else
            {
            int hasContactPermission = ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.RECEIVE_SMS);

            if(hasContactPermission != PackageManager.PERMISSION_GRANTED )
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECEIVE_SMS))
                {
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle("Permission Required")
                            .setMessage("We need sms permission to auto fetch your verification code.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    requestPermissions(
                                            new String[]{Manifest.permission.RECEIVE_SMS},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            })
                            .create()
                            .show();
                }
                else
                {
                    requestPermissions(
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
            else {
                sendOTP(phoneNumberEntered,createOTP(),enteredCode, true);
            }
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.RECEIVE_SMS)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        sendOTP(phoneNumberEntered,createOTP(),enteredCode, true);
                    }
                    else
                    {
                        sendOTP(phoneNumberEntered,createOTP(),enteredCode, true);
                    }
                }
                else
                {
                    sendOTP(phoneNumberEntered,createOTP(),enteredCode, true);
                }
                break;
        }
    }*/

    /*private void sendOTP(final String phoneNumber, final String otp, final String countryCode, final boolean showOTPWindow)
    {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "sendOtp", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                if (isAdded()) {
                    progressDialog.dismiss();
                    Log.e("update_mobile_response", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.optBoolean("status", false);
                        String msg = jsonObject.optString("msg");
                        if (status)
                        {
                            if (showOTPWindow) {
                                showOTPVerification(phoneNumber, otp, countryCode);
                            }
                        } else {
                            Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (isAdded())
                    Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                Log.e("update_mobile_error","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("country_code",countryCode);
                params.put("mobile",phoneNumber);
                params.put("otp",otp);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }*/

//    private void showOTPVerification(final String phoneNumber, final String otp, final String countryCode) {
//        final AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
//        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_otp_mobile_number, null);
//        final TextView instructionsTV = view.findViewById(R.id.instructions);
//        final ImageView imageView = new ImageView(getActivity());
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(Utility.getPx(24),Utility.getPx(16)));
//        // using the default alignment: ALIGN_BOTTOM
//        Glide.with(getActivity()).load(enteredCountryFlag).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                String prefix = "We have sent you a SMS on ";
//                SpannableString string = new SpannableString(prefix + "" + countryCode + " " + phoneNumber + " with a 6 digit verification code.");
//                //.setSpan(new ImageSpan(getActivity(), bitmap), prefix.length(), prefix.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                instructionsTV.setText(string);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
//                Log.e("size" , bitmap.getWidth() + " X " + bitmap.getHeight());
//                String prefix = "We have sent you a SMS on ";
//                SpannableString string = new SpannableString(prefix + "  " + countryCode + " " + phoneNumber + " with a 6 digit verification code.");
//                string.setSpan(new ImageSpan(getActivity(), bitmap), prefix.length(), prefix.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                instructionsTV.setText(string);
//                return false;
//            }
//        }).into(imageView);
//        final EditText[] editTexts = new EditText[6];
//        editTexts[0] = view.findViewById(R.id.otp1);
//        editTexts[1] = view.findViewById(R.id.otp2);
//        editTexts[2] = view.findViewById(R.id.otp3);
//        editTexts[3] = view.findViewById(R.id.otp4);
//        editTexts[4] = view.findViewById(R.id.otp5);
//        editTexts[5] = view.findViewById(R.id.otp6);
//
//        view.findViewById(R.id.resend).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendOTP(phoneNumber,otp,countryCode,false);
//            }
//        });
//
//        editTexts[0].addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 1)
//                {
//                    editTexts[1].requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        editTexts[1].addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 1)
//                {
//                    editTexts[2].requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        editTexts[2].addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 1)
//                {
//                    editTexts[3].requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        editTexts[3].addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 1)
//                {
//                    editTexts[4].requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        editTexts[4].addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 1)
//                {
//                    editTexts[5].requestFocus();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        /*SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text",messageText);
//                Log.e("msg", messageText);
//                if(messageText.equals(otp + " is the OTP for verification of your mobile number on CareerGuide."))
//                {
//                    SmsReceiver.unBindListener();
//                    view.findViewById(R.id.resend).setOnClickListener(null);
//                    editTexts[0].setText(otp.charAt(0) + "");
//                    editTexts[1].setText(otp.charAt(1) + "");
//                    editTexts[2].setText(otp.charAt(2) + "");
//                    editTexts[3].setText(otp.charAt(3) + "");
//                    editTexts[4].setText(otp.charAt(4) + "");
//                    editTexts[5].setText(otp.charAt(5) + "");
//                    editTexts[5].requestFocus();
//                    editTexts[5].setSelection(1);
//                }
//            }
//        });*/
//        view.findViewById(R.id.continueButton).setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                String otpEntered = editTexts[0].getText().toString() + editTexts[1].getText().toString() +editTexts[2].getText().toString() +editTexts[3].getText().toString() +editTexts[4].getText().toString() + editTexts[5].getText().toString();
//                if (otpEntered.equals(otp))
//                {
//                    updateMobile(phoneNumber);
//                    ad.dismiss();
//                }
//                else
//                {
//                    Toast.makeText(getActivity(),"OTP didn't match",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        ad.setView(view);
//        ad.show();
//    }

    private void updateMobile(final String phoneNumber) {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "update_mobile", response -> {
            if (isAdded()) {
                progressDialog.dismiss();
                Log.e("update_mobile_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    String msg = jsonObject.optString("msg");
                    if (status) {
                        mobileAtServer = phoneNumber;
                        if (educationAtServer.length() == 0)
                        {
                            showEducation();
                        }

                    } else {
                        Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            if (isAdded())
                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("update_mobile_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                params.put("mobile",phoneNumber);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void showEducation()
    {
        final AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_enter_education,null);
        //final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

        final RadioButton class1RadioButton = view.findViewById(R.id.class1);
        final RadioButton class10RadioButton = view.findViewById(R.id.class10);
        final RadioButton class12RadioButton = view.findViewById(R.id.class12);
        final RadioButton graduateRadioButton = view.findViewById(R.id.graduate);
        final RadioButton postGraduateRadioButton = view.findViewById(R.id.postGraduate);

        final String[] selectedEducation = {""};

        class1RadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                selectedEducation[0] = "Class 1 - 9";
            }
        });

        class10RadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                selectedEducation[0] = "Class 10";
            }
        });

        class12RadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                selectedEducation[0] = "Class 12";
            }
        });

        graduateRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                selectedEducation[0] = "Graduate";
            }
        });

        postGraduateRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                selectedEducation[0] = "Post Graduate";
            }
        });

        view.findViewById(R.id.confirmButton).setOnClickListener(v -> {
            switch (selectedEducation[0])
            {
                case "":
                    Toast.makeText(getActivity(),"Please select your highest education",Toast.LENGTH_LONG).show();
                    break;
                default:
                    updateEducation(selectedEducation[0]);
                    ad.dismiss();
            }
        });
        ad.setView(view);
        ad.show();
    }

    private void updateEducation(final String education) {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "update_education_level", response -> {
            if (isAdded()) {
                progressDialog.dismiss();
                Log.e("update_edu_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    String msg = jsonObject.optString("msg");
                    if (status) {
                        educationAtServer = education;
                        if (authKeyAtServer.length()==0)
                        {
                            createAuthKey();
                        }
                        else
                        {
                            proceedTest();
                        }
                    } else {
                        Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            if (isAdded())
                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("update_edu_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                params.put("education_level",education);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void createAuthKey()
    {
        Log.e("pos","createauthkey");
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "creat_auth_key", response -> {
            if (isAdded()) {
                progressDialog.dismiss();
                Log.e("create_auth_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    String msg = jsonObject.optString("msg");
                    if (status)
                    {
                        proceedTest();
                    } else {
                        //Log.e("false message", msg);
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            if (isAdded())
                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("create_auth_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                Log.e("request_create_auth",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest,100000,1);
    }

    void increaseHeight()
    {
        view.findViewById(R.id.parentRL).setPadding(0,Utility.getPx(20),0,Utility.getPx(20));
    }

    void decreaseHeight()
    {
        view.findViewById(R.id.parentRL).setPadding(0,Utility.getPx(40),0,Utility.getPx(40));
    }

    private String createOTP()
    {
        String otp = "" + ((int)(100000 + new Random().nextFloat() * 900000));
        return otp;
    }

    public class Country {

        private  String countryName ="";
        private  String Image="";
        private  String countryCode ="";
        private boolean current = false;

        public Country(String countryName, String image, String countryCode, boolean current) {
            this.countryName = countryName;
            Image = image;
            this.countryCode = "(+" + countryCode + ")";
            this.current = current;
        }

        /*********** Get Methods ****************/
        public String getCountryName()
        {
            return this.countryName;
        }

        public String getImage()
        {
            return this.Image;
        }

        public String getCountryCode()
        {
            return this.countryCode;
        }

        public boolean isCurrent() {
            return current;
        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private Activity activity;
        private ArrayList data;
        Country tempValues=null;
        LayoutInflater inflater;

        public CustomAdapter(
                Activity activitySpinner,
                int textViewResourceId,
                ArrayList objects
        )
        {
            super(activitySpinner, textViewResourceId, objects);
            activity = activitySpinner;
            data     = objects;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view =  getCustomView(position, convertView, parent);
            view.setPadding(0,0,0,0);
            return view;
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.country_spinner_item, parent, false);

            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = (Country) data.get(position);

            TextView label        = row.findViewById(R.id.countryCode);
            ImageView flagLogo = row.findViewById(R.id.flagImage);

            /*if(position==0){
                // Default selected Spinner item
                label.setText("Please select company");
                sub.setText("");
            }
            else*/
            {
                // Set values for spinner each row
                label.setText(tempValues.getCountryCode());
                Glide.with(activity).load(tempValues.getImage()).into(flagLogo);

            }

            return row;
        }
    }
}
