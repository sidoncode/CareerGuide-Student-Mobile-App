package com.careerguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.payment.PaymentDetailAdapter;
import com.careerguide.payment.PaymentDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentProfileFragment extends Fragment {
    private Drawable edittextDrawable = null;
    private View view;
    //private TextView userNameTextView;
    //private TextView userLocationTextView;
    private List<PaymentDetailModel> paymentlist;
    private ArrayList<PaymentDetailModel> payments = new ArrayList<>();
    private int paymentCount;
    private PaymentDetailAdapter payment_adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private LinearLayoutManager mLayoutManager;
    public StudentProfileFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_profile, container, false);
        /*userNameTextView = view.findViewById(R.id.userName);
        userLocationTextView = view.findViewById(R.id.userLocation);
        userNameTextView.setText(Utility.getUserFirstName(getActivity()) + " " + Utility.getUserLastName(getActivity()).trim());
        userLocationTextView.setText(Utility.getUserCity(getActivity()));*/
        RecyclerView recycler_payment = view.findViewById(R.id.recycler_payment);
        paymentlist = new ArrayList<>();
        payment_adapter = new PaymentDetailAdapter(getContext(), paymentlist);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_payment.setLayoutManager(mLayoutManager);
        recycler_payment.setAdapter(payment_adapter);
        setUpCardView();
        getPaymentDetail();
        return view;
    }
    public static StudentProfileFragment newInstance(String param1, String param2) {
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void getPaymentDetail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "GetPayment", response -> {
            Log.e("all_payment_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status)
                {
                    JSONArray Payments = jsonObject.optJSONArray("payments");
                    Log.e("Payments","-->" +Payments);
                    for (int i = 0; Payments != null && i < Payments.length(); i++) {
                        JSONObject PaymentsJsonObject = Payments.optJSONObject(i);
                        String name = PaymentsJsonObject.optString("firstname");
                        String email = PaymentsJsonObject.optString("email");
                        String order_id = PaymentsJsonObject.optString("OrderId");
                        String amount = PaymentsJsonObject.optString("amount");
                        String payment_status = PaymentsJsonObject.optString("status");
                        String datetime = PaymentsJsonObject.optString("datetime");
                        String validate_till = PaymentsJsonObject.optString("validate_till");
                        Log.e("@name" ,"-->" +name);
                        payments.add(new PaymentDetailModel(name, email, order_id, amount, payment_status, datetime,validate_till));
                    }
                    paymentCount = payments.size();
                    for (int i =0 ; i<paymentCount ; i++){
                        Log.e("@paymentCount" ,"-->" +i);
                        PaymentDetailModel paymentModel = new PaymentDetailModel(payments.get(i).getName() , payments.get(i).getemail(), payments.get(i).getOrder_id(), payments.get(i).getAmount(), payments.get(i).getPayment_status(), payments.get(i).getDatetime(), payments.get(i).getValidate_till());
                        paymentlist.add(paymentModel);
                    }
                    payment_adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            //Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity)),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                Log.e("all_coun_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
    private void setUpCardView() {

        final View personalDetails = view.findViewById(R.id.personalDetails);
        final View eduDetails = view.findViewById(R.id.educationDetails);
        final View accDetails = view.findViewById(R.id.accountDetails);
        final View paymentDetails = view.findViewById(R.id.paymentDetails);
        //final View rewardDetails = view.findViewById(R.id.rewardsDetails);

        View personalRL = view.findViewById(R.id.personalRelativeL);
        View eduRL = view.findViewById(R.id.educationalRelativeL);
        View accountRL = view.findViewById(R.id.accountRelativeL);
        View paymentRelativeL = view.findViewById(R.id.paymentRelativeL);
        //View rewardRL = view.findViewById(R.id.rewardsRelativeL);

        final ImageView arrowPersonalImageView = view.findViewById(R.id.arrowPersonal);
        final ImageView arrowEduImageView = view.findViewById(R.id.arrowEducational);
        final ImageView arrowAccountImageView = view.findViewById(R.id.arrowAccount);
        final ImageView arrowpayment = view.findViewById(R.id.arrowpayment);
        //final ImageView arrowRewards = view.findViewById(R.id.arrowRewards);

        personalRL.setOnClickListener(v -> {
            if (personalDetails.getVisibility() == View.VISIBLE)
            {
                personalDetails.setVisibility(View.GONE);
                arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
            }
            else
            {
                personalDetails.setVisibility(View.VISIBLE);
                arrowPersonalImageView.setImageResource(R.drawable.ic_collapse);
            }
            eduDetails.setVisibility(View.GONE);
            accDetails.setVisibility(View.GONE);
            paymentDetails.setVisibility(View.GONE);
            //rewardDetails.setVisibility(View.GONE);

            arrowEduImageView.setImageResource(R.drawable.ic_expand);
            arrowAccountImageView.setImageResource(R.drawable.ic_expand);
            arrowpayment.setImageResource(R.drawable.ic_expand);
            //arrowRewards.setImageResource(R.drawable.ic_expand);
        });


        paymentRelativeL.setOnClickListener(v -> {
            if (paymentDetails.getVisibility() == View.VISIBLE)
            {
                paymentDetails.setVisibility(View.GONE);
                arrowpayment.setImageResource(R.drawable.ic_expand);
            }
            else
            {
                paymentDetails.setVisibility(View.VISIBLE);
                arrowpayment.setImageResource(R.drawable.ic_collapse);
            }
            eduDetails.setVisibility(View.GONE);
            accDetails.setVisibility(View.GONE);
            personalDetails.setVisibility(View.GONE);
            //rewardDetails.setVisibility(View.GONE);

            arrowEduImageView.setImageResource(R.drawable.ic_expand);
            arrowAccountImageView.setImageResource(R.drawable.ic_expand);
            arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
            //arrowRewards.setImageResource(R.drawable.ic_expand);
        });

        eduRL.setOnClickListener(v -> {
            if (eduDetails.getVisibility() == View.VISIBLE)
            {
                eduDetails.setVisibility(View.GONE);
                arrowEduImageView.setImageResource(R.drawable.ic_expand);
            }
            else
            {
                eduDetails.setVisibility(View.VISIBLE);
                arrowEduImageView.setImageResource(R.drawable.ic_collapse);
            }
            personalDetails.setVisibility(View.GONE);
            accDetails.setVisibility(View.GONE);
            paymentDetails.setVisibility(View.GONE);
            //rewardDetails.setVisibility(View.GONE);

            arrowAccountImageView.setImageResource(R.drawable.ic_expand);
            arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
            arrowpayment.setImageResource(R.drawable.ic_expand);
           // arrowRewards.setImageResource(R.drawable.ic_expand);
        });

        accountRL.setOnClickListener(v -> {
            if (accDetails.getVisibility() == View.VISIBLE)
            {
                accDetails.setVisibility(View.GONE);
                arrowAccountImageView.setImageResource(R.drawable.ic_expand);
            }
            else
            {
                accDetails.setVisibility(View.VISIBLE);
                arrowAccountImageView.setImageResource(R.drawable.ic_collapse);
            }
            eduDetails.setVisibility(View.GONE);
            personalDetails.setVisibility(View.GONE);
            paymentDetails.setVisibility(View.GONE);
           // rewardDetails.setVisibility(View.GONE);

            arrowEduImageView.setImageResource(R.drawable.ic_expand);
            //arrowRewards.setImageResource(R.drawable.ic_expand);
            arrowpayment.setImageResource(R.drawable.ic_expand);
            arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
        });
        /*rewardRL.setOnClickListener(v -> {
            if (rewardDetails.getVisibility() == View.VISIBLE)
            {
                rewardDetails.setVisibility(View.GONE);
                arrowRewards.setImageResource(R.drawable.ic_expand);
            }
            else
            {
                rewardDetails.setVisibility(View.VISIBLE);
                arrowRewards.setImageResource(R.drawable.ic_collapse);
            }
            personalDetails.setVisibility(View.GONE);
            accDetails.setVisibility(View.GONE);
            eduDetails.setVisibility(View.GONE);
            paymentDetails.setVisibility(View.GONE);

            arrowEduImageView.setImageResource(R.drawable.ic_expand);
            arrowpayment.setImageResource(R.drawable.ic_expand);
            arrowAccountImageView.setImageResource(R.drawable.ic_expand);
            arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
        });*/

        final EditText firstNameEditText = view.findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = view.findViewById(R.id.lastNameEditText);
        final EditText dobEditText = view.findViewById(R.id.dobEditText);
        //final EditText genderEditText = view.findViewById(R.id.genderEditText);
        final EditText locationEditText = view.findViewById(R.id.locationEditText);
        final EditText mobileEditText = view.findViewById(R.id.mobileEditText);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView edutxt = view.findViewById(R.id.edutxt);
        //TextView rewtxt = view.findViewById(R.id.rewtxt);
        //TextView reftxt = view.findViewById(R.id.reftxt);
        final Spinner eduSpinner = view.findViewById(R.id.eduSpinner);
        final Spinner genderSpinner = view.findViewById(R.id.genderSpinner);

       // rewtxt.setText(Utility.getRewardPoints(getActivity()));
        //reftxt.setText(Utility.getNumReferrals(getActivity()));

        final ImageView editFName = view.findViewById(R.id.editFName);
        final ImageView editLName = view.findViewById(R.id.editLName);
        final ImageView editDOB = view.findViewById(R.id.editDOB);
        //final ImageView editGender = view.findViewById(R.id.editGender);
        final ImageView editLocation = view.findViewById(R.id.editLoc);
        final ImageView editMobile = view.findViewById(R.id.editMob);

        edittextDrawable = firstNameEditText.getBackground();
        disableEditText(firstNameEditText);
        disableEditText(lastNameEditText);
        disableEditText(dobEditText);
        //disableEditText(genderEditText);
        disableEditText(locationEditText);
        disableEditText(mobileEditText);

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dobEditText.setText(current);
                    dobEditText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };

        dobEditText.addTextChangedListener(tw);

        firstNameEditText.setText(Utility.getUserFirstName(getActivity()));
        lastNameEditText.setText(Utility.getUserLastName(getActivity()));
        dobEditText.setText(Utility.getUserDOB(getActivity()));
        //genderEditText.setText(Utility.getUserGender(getActivity()));
        locationEditText.setText(Utility.getUserCity(getActivity()));

        String mobile = Utility.getUserMobile(getActivity());
        mobileEditText.setText(mobile);
        emailTextView.setText(Utility.getUserEmail(getActivity()));
        edutxt.setText(Utility.getUserEducation(getActivity()));

        final String currentEducation[] = {Utility.getUserEducation(getActivity())};

        final List<String> levels = new ArrayList<>();
        levels.add("Class 1st - Class 9th");
        levels.add("Class 10th");
        levels.add("Class 12th");
        levels.add("Graduate");
        levels.add("Post Graduate");


        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_item, levels);
        eduSpinner.setAdapter(dataAdapter);

        int spinnerPosition = dataAdapter.getPosition(currentEducation[0]);
        eduSpinner.setSelection(spinnerPosition);

        eduSpinner.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.app_pink), PorterDuff.Mode.SRC_ATOP);

        if (currentEducation[0].isEmpty() || spinnerPosition == -1)
        {
            levels.add(0,"Select");
        }


        eduSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("Select") || item.equals(currentEducation[0]))
                {
                    return;
                }
                EditText editText = new EditText(getActivity());
                switch (item)
                {
                    case "Class 1st - Class 9th":
                        editText.setText("Class 1 - Class 9");
                        break;
                    case "Class 10th":
                        editText.setText("Class 10");
                        break;
                    case "Class 12th":
                        editText.setText("Class 12");
                        break;
                    default:
                        editText.setText(item);
                        break;
                }
                updateProfile("education_level",editText,null,null,null);
                currentEducation[0] = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        final String gender[] = {Utility.getUserGender(getActivity())};

        final List<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        genders.add("Other");


        final ArrayAdapter<String> dataGenderAdapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_item, genders);
        genderSpinner.setAdapter(dataGenderAdapter);

        int spinnerGenderPosition = dataGenderAdapter.getPosition(gender[0]);
        genderSpinner.setSelection(spinnerGenderPosition);

        genderSpinner.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.app_pink), PorterDuff.Mode.SRC_ATOP);

        if (gender[0].isEmpty() || spinnerGenderPosition == -1)
        {
            genders.add(0,"Select");
        }


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("Select") || item.equals(gender[0]))
                {
                    return;
                }
                EditText editText = new EditText(getActivity());
                editText.setText(item);
                updateProfile("gender",editText,null,null,null);
                gender[0] = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        editFName.setOnClickListener(v -> {
            firstNameEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            firstNameEditText.setBackground(edittextDrawable);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(firstNameEditText, InputMethodManager.SHOW_IMPLICIT);

            final View doneFName = view.findViewById(R.id.doneFName);
            final View cancelFname = view.findViewById(R.id.cancelFName);

            editFName.setVisibility(View.GONE);
            doneFName.setVisibility(View.VISIBLE);
            cancelFname.setVisibility(View.VISIBLE);

            cancelFname.setOnClickListener(v1 -> {
                disableEditText(firstNameEditText);
                firstNameEditText.setText(Utility.getUserFirstName(getActivity()));
                editFName.setVisibility(View.VISIBLE);
                doneFName.setVisibility(View.GONE);
                cancelFname.setVisibility(View.GONE);InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(firstNameEditText.getWindowToken(), 0);
            });
            doneFName.setOnClickListener(v12 -> updateProfile("first_name", firstNameEditText,editFName,doneFName,cancelFname));

        });

        editLName.setOnClickListener(v -> {
            lastNameEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            lastNameEditText.setBackground(edittextDrawable);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(lastNameEditText, InputMethodManager.SHOW_IMPLICIT);

            final View doneLName = view.findViewById(R.id.doneLName);
            final View cancelLname = view.findViewById(R.id.cancelLName);

            editLName.setVisibility(View.GONE);
            doneLName.setVisibility(View.VISIBLE);
            cancelLname.setVisibility(View.VISIBLE);

            cancelLname.setOnClickListener(v110 -> {
                disableEditText(lastNameEditText);
                lastNameEditText.setText(Utility.getUserLastName(getActivity()));
                editLName.setVisibility(View.VISIBLE);
                doneLName.setVisibility(View.GONE);
                cancelLname.setVisibility(View.GONE);InputMethodManager imm15 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm15.hideSoftInputFromWindow(lastNameEditText.getWindowToken(), 0);
            });

            doneLName.setOnClickListener(v13 -> updateProfile("last_name", lastNameEditText,editLName,doneLName,cancelLname));

        });

        editLocation.setOnClickListener(v -> {
            locationEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            locationEditText.setBackground(edittextDrawable);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(locationEditText, InputMethodManager.SHOW_IMPLICIT);

            final View doneLoc = view.findViewById(R.id.doneLoc);
            final View cancelLoc = view.findViewById(R.id.cancelLoc);

            editLocation.setVisibility(View.GONE);
            doneLoc.setVisibility(View.VISIBLE);
            cancelLoc.setVisibility(View.VISIBLE);

            cancelLoc.setOnClickListener(v14 -> {
                disableEditText(locationEditText);
                locationEditText.setText(Utility.getUserCity(getActivity()));
                editLocation.setVisibility(View.VISIBLE);
                doneLoc.setVisibility(View.GONE);
                cancelLoc.setVisibility(View.GONE);InputMethodManager imm12 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm12.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
            });

            doneLoc.setOnClickListener(v15 -> updateProfile("city", locationEditText,editLocation,doneLoc,cancelLoc));

        });

        editMobile.setOnClickListener(v -> {
            mobileEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            mobileEditText.setBackground(edittextDrawable);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mobileEditText, InputMethodManager.SHOW_IMPLICIT);

            final View doneMobile = view.findViewById(R.id.doneMobile);
            final View cancelMobile = view.findViewById(R.id.cancelMobile);

            editMobile.setVisibility(View.GONE);
            doneMobile.setVisibility(View.VISIBLE);
            cancelMobile.setVisibility(View.VISIBLE);

            cancelMobile.setOnClickListener(v18 -> {
                disableEditText(mobileEditText);
                mobileEditText.setText(Utility.getUserFirstName(getActivity()));
                editMobile.setVisibility(View.VISIBLE);
                doneMobile.setVisibility(View.GONE);
                cancelMobile.setVisibility(View.GONE);InputMethodManager imm14 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm14.hideSoftInputFromWindow(mobileEditText.getWindowToken(), 0);
            });

            doneMobile.setOnClickListener(v19 -> updateProfile("mobile_number", mobileEditText,editMobile,doneMobile,cancelMobile));

        });

        editDOB.setOnClickListener(v -> {
            dobEditText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
            dobEditText.setBackground(edittextDrawable);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(dobEditText, InputMethodManager.SHOW_IMPLICIT);

            final View doneDOB = view.findViewById(R.id.doneDob);
            final View cancelDOB = view.findViewById(R.id.cancelDob);

            editDOB.setVisibility(View.GONE);
            doneDOB.setVisibility(View.VISIBLE);
            cancelDOB.setVisibility(View.VISIBLE);

            cancelDOB.setOnClickListener(v16 -> {
                disableEditText(dobEditText);
                dobEditText.setText(Utility.getUserDOB(getActivity()));
                editDOB.setVisibility(View.VISIBLE);
                doneDOB.setVisibility(View.GONE);
                cancelDOB.setVisibility(View.GONE);InputMethodManager imm13 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm13.hideSoftInputFromWindow(dobEditText.getWindowToken(), 0);
            });

            doneDOB.setOnClickListener(v17 -> updateProfile("dob", dobEditText,editDOB,doneDOB,cancelDOB));
        });


    }
    private void disableEditText(EditText editText)
    {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    private void updateProfile(final String key, final EditText editText, final View edit, final View done, final View cancel) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty())
        {
            Toast.makeText(getActivity(),"Please insert value first", Toast.LENGTH_LONG).show();
            return;
        }
        if (key.equals("dob"))
        {
            Log.e("dob",value);
            value = value.replace("Y","").replace("M","").replace("D","");
            if (value.length()<10)
            {
                Toast.makeText(getActivity(),"Please insert valid Date", Toast.LENGTH_LONG).show();
                return;
            }
            final SimpleDateFormat dateFormatNew = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date date = dateFormat.parse(value);
                value = dateFormatNew.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),"Please enter valid date in DD/MM/YYYY format",Toast.LENGTH_LONG).show();
            }
        }
        final ProgressDialog progressDialog = new ProgressDialogCustom(getActivity(),"Saving...");
        progressDialog.show();
        final String finalValue = value;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "profile_update", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                progressDialog.dismiss();
                Log.e("prfl_updt_res",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    String msg = jsonObject.optString("msg");
                    if(status)
                    {
                        disableEditText(editText);
                        String picUrl = Utility.getUserPic(getActivity());
                        switch (key)
                        {
                            case "first_name":
                                Utility.setUserFirstName(getActivity(), finalValue);
                                //userNameTextView.setText(finalValue + " " + Utility.getUserLastName(getActivity()));
                                HomeActivity.updateUserName(finalValue + " " + Utility.getUserLastName(getActivity()),getActivity());
                                if (picUrl.isEmpty())
                                {
                                    //profilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(getActivity())).charAt(0),100));
                                    String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
                                    ((TextView) view.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
                                }
                                break;
                            case "last_name":
                                Utility.setUserLasstName(getActivity(), finalValue);
                                //userNameTextView.setText(Utility.getUserFirstName(getActivity()) + " " + finalValue);
                                HomeActivity.updateUserName(Utility.getUserFirstName(getActivity()) + " " + finalValue,getActivity());
                                if (picUrl.isEmpty())
                                {
                                    //profilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(getActivity())).charAt(0),100));
                                    String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
                                    ((TextView) view.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
                                }
                                break;
                            case "dob":
                                Utility.setUserDOB(getActivity(), finalValue);
                                break;
                            case "education_level":
                                Utility.setUserEducation(getActivity(), finalValue);
                                break;
                            case "mobile_number":
                                Utility.setUserMobile(getActivity(), finalValue);
                                break;
                            case "gender":
                                Utility.setUserGender(getActivity(), finalValue);
                                break;
                            case "city":
                                Utility.setUserCity(getActivity(), finalValue);
                                //userLocationTextView.setText(finalValue);
                                HomeActivity.updateUserCity(finalValue);
                                break;
                        }
                        if (key.equals("dob"))
                        {
                            editText.setText(Utility.getUserDOB(getActivity()));
                        }
                        else {
                            editText.setText(finalValue);
                        }
                        if (edit != null)
                            edit.setVisibility(View.VISIBLE);
                        if (done != null)
                            done.setVisibility(View.GONE);
                        if (cancel != null)
                            cancel.setVisibility(View.GONE);
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Something went wrong.",Toast.LENGTH_LONG).show();
                        if (key.equals("dob"))
                        {
                            editText.setText(Utility.getUserDOB(getActivity()));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("prfl_updt_error","error");
            if (key.equals("dob"))
            {
                editText.setText(Utility.getUserDOB(getActivity()));
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                params.put(key, finalValue);
                Log.e("prfl_updt_req",params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
