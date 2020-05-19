package com.careerguide;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.careerguide.payment.PaymentDetailAdapter;
import com.careerguide.payment.PaymentDetailModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private String newImage = null;
    private CircleImageView dialogProfilePic = null;
    private CircleImageView profilePic;
    private TextView userNameTextView;
    private TextView userLocationTextView;
    private Drawable edittextDrawable = null;


    private View view;
    private TextView dialogImageInitial;


    public ProfileFragment() {
        // Required empty public constructor
    }

    private List<PaymentDetailModel> paymentlist;
    private ArrayList<PaymentDetailModel> payments = new ArrayList<>();
    private int paymentCount;
    private PaymentDetailAdapter payment_adapter;
    private LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        userNameTextView = view.findViewById(R.id.userName);
        userLocationTextView = view.findViewById(R.id.userLocation);



        userNameTextView.setText(Utility.getUserFirstName(getActivity()) + " " + Utility.getUserLastName(getActivity()).trim());
        userLocationTextView.setText(Utility.getUserCity(getActivity()));

        profilePic = view.findViewById(R.id.profilePic);

        RecyclerView recycler_payment = view.findViewById(R.id.recycler_payment);
        paymentlist = new ArrayList<>();
        payment_adapter = new PaymentDetailAdapter(getContext(), paymentlist);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_payment.setLayoutManager(mLayoutManager);
        recycler_payment.setAdapter(payment_adapter);

        String picUrl = Utility.getUserPic(getActivity());
        if (picUrl.isEmpty())
        {
            profilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(getActivity())).charAt(0),100));
            String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
            ((TextView) view.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
        }
        else  {
            Glide.with(this).load(picUrl).into(profilePic);
            ((TextView) view.findViewById(R.id.imageInitial)).setText("");
        }

        profilePic.setOnClickListener(v -> {
            final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            final View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_profile_pic,null);
            View removePic = dialogView.findViewById(R.id.removePicture);
            View changePic = dialogView.findViewById(R.id.changePicture);
            final Button cancelButton = dialogView.findViewById(R.id.cancelButton);
            Button doneButton = dialogView.findViewById(R.id.doneButton);
            dialogProfilePic = dialogView.findViewById(R.id.dialogProfilePic);
            dialogImageInitial = dialogView.findViewById(R.id.imageInitial);
            if (Utility.getUserPic(getActivity()).isEmpty())
            {
                removePic.setVisibility(View.GONE);
                ((TextView) changePic).setText("Add Profile Picture");
                dialogProfilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(getActivity())).charAt(0),100));
                String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
                dialogImageInitial.setText(initial.toUpperCase());
            }
            else
            {
                Glide.with(ProfileFragment.this).load(Utility.getUserPic(getActivity())).into(dialogProfilePic);
                ((TextView) dialogView.findViewById(R.id.imageInitial)).setText("");
            }

            changePic.setOnClickListener(v14 -> CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .setFixAspectRatio(true)
                    .start(getContext(), ProfileFragment.this));
            removePic.setOnClickListener(v13 -> {
                dialogProfilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(getActivity())).charAt(0),100));
                String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
                ((TextView) dialogView.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
                newImage = "";
            });

            cancelButton.setOnClickListener(v12 -> {
                newImage = null;
                alertDialog.dismiss();
            });

            doneButton.setOnClickListener(v1 -> {
                if (newImage != null)
                {
                    final ProgressDialog progressDialog = new ProgressDialogCustom(getActivity(),"Saving...");
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "update_profile_pic", response -> {
                        progressDialog.dismiss();
                        Log.e("updt_prfl_pic_res",response);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean status = jsonObject.optBoolean("status",false);
                            String msg = jsonObject.optString("msg");
                            if(status)
                            {
                                String profilePic = jsonObject.optString("profile_pic");
                                Utility.setUserPic(getActivity(),profilePic);
                                alertDialog.dismiss();
                                if (!profilePic.isEmpty()){
                                    Glide.with(ProfileFragment.this).load(profilePic).into(ProfileFragment.this.profilePic);
                                    ((TextView) view.findViewById(R.id.imageInitial)).setText("");
                                }
                                else
                                {
                                    ProfileFragment.this.profilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(getActivity())).charAt(0),100));
                                    String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
                                    ((TextView) view.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
                                }
                                HomeActivity.updatePic(profilePic,getActivity());
                                newImage = null;
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Something went wrong.",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                        Log.e("updt_prfl_pic_error","error");
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() {
                            HashMap<String,String> params = new HashMap<>();
                            params.put("user_id",Utility.getUserId(getActivity()));
                            params.put("profile_pic", newImage);
                            Log.e("updt_prfl_pic_req",params.toString());
                            return params;
                        }
                    };

                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                }
                else
                {
                    cancelButton.performClick();
                }
            });
            alertDialog.setView(dialogView);
            alertDialog.show();
        });

        setUpCardView();
        getPaymentDetail();
        return view;
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

        View personalRL = view.findViewById(R.id.personalRelativeL);
        View eduRL = view.findViewById(R.id.educationalRelativeL);
        View accountRL = view.findViewById(R.id.accountRelativeL);
        View paymentRelativeL = view.findViewById(R.id.paymentRelativeL);

        final ImageView arrowPersonalImageView = view.findViewById(R.id.arrowPersonal);
        final ImageView arrowEduImageView = view.findViewById(R.id.arrowEducational);
        final ImageView arrowAccountImageView = view.findViewById(R.id.arrowAccount);
        final ImageView arrowpayment = view.findViewById(R.id.arrowpayment);

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
            arrowEduImageView.setImageResource(R.drawable.ic_expand);
            arrowAccountImageView.setImageResource(R.drawable.ic_expand);
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
            arrowEduImageView.setImageResource(R.drawable.ic_expand);
            arrowAccountImageView.setImageResource(R.drawable.ic_expand);
            arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
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
            arrowAccountImageView.setImageResource(R.drawable.ic_expand);
            arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
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
            arrowEduImageView.setImageResource(R.drawable.ic_expand);
            arrowPersonalImageView.setImageResource(R.drawable.ic_expand);
        });

        final EditText firstNameEditText = view.findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = view.findViewById(R.id.lastNameEditText);
        final EditText dobEditText = view.findViewById(R.id.dobEditText);
        //final EditText genderEditText = view.findViewById(R.id.genderEditText);
        final EditText locationEditText = view.findViewById(R.id.locationEditText);
        final EditText mobileEditText = view.findViewById(R.id.mobileEditText);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        TextView edutxt = view.findViewById(R.id.edutxt);
        final Spinner eduSpinner = view.findViewById(R.id.eduSpinner);
        final Spinner genderSpinner = view.findViewById(R.id.genderSpinner);


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
                                userNameTextView.setText(finalValue + " " + Utility.getUserLastName(getActivity()));
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
                                userNameTextView.setText(Utility.getUserFirstName(getActivity()) + " " + finalValue);
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
                                userLocationTextView.setText(finalValue);
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

    private void disableEditText(EditText editText)
    {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                Log.e("result uri", resultUri.toString());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    dialogProfilePic.setImageBitmap(bitmap);
                    newImage = Utility.stringFromBitmap(bitmap);
                    dialogImageInitial.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                try {
                    Exception error = result.getError();
                    error.printStackTrace();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
