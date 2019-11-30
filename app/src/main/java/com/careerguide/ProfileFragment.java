package com.careerguide;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.support.v4.app.Fragment;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                changePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1,1)
                                .setFixAspectRatio(true)
                                .start(getContext(), ProfileFragment.this);
                    }
                });
                removePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogProfilePic.setImageBitmap(Utility.getBackgroundBitmap((Utility.getUserEmail(getActivity())).charAt(0),100));
                        String initial = (Utility.getUserFirstName(getActivity())).charAt(0) + "" + (Utility.getUserLastName(getActivity())).charAt(0);
                        ((TextView) dialogView.findViewById(R.id.imageInitial)).setText(initial.toUpperCase());
                        newImage = "";
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newImage = null;
                        alertDialog.dismiss();
                    }
                });

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        if (newImage != null)
                        {
                            final ProgressDialog progressDialog = new ProgressDialogCustom(getActivity(),"Saving...");
                            progressDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "update_profile_pic", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response){
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
                                }
                            }, new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                                    Log.e("updt_prfl_pic_error","error");
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
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
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        setUpCardView();
        return view;
    }

    private void setUpCardView() {

        final View personalDetails = view.findViewById(R.id.personalDetails);
        final View eduDetails = view.findViewById(R.id.educationDetails);
        final View accDetails = view.findViewById(R.id.accountDetails);

        View personalRL = view.findViewById(R.id.personalRelativeL);
        View eduRL = view.findViewById(R.id.educationalRelativeL);
        View accountRL = view.findViewById(R.id.accountRelativeL);

        final ImageView arrowPersonalImageView = view.findViewById(R.id.arrowPersonal);
        final ImageView arrowEduImageView = view.findViewById(R.id.arrowEducational);
        final ImageView arrowAccountImageView = view.findViewById(R.id.arrowAccount);

        personalRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        eduRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        accountRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
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




        editFName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                firstNameEditText.setBackground(edittextDrawable);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(firstNameEditText, InputMethodManager.SHOW_IMPLICIT);

                final View doneFName = view.findViewById(R.id.doneFName);
                final View cancelFname = view.findViewById(R.id.cancelFName);

                editFName.setVisibility(View.GONE);
                doneFName.setVisibility(View.VISIBLE);
                cancelFname.setVisibility(View.VISIBLE);

                cancelFname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableEditText(firstNameEditText);
                        firstNameEditText.setText(Utility.getUserFirstName(getActivity()));
                        editFName.setVisibility(View.VISIBLE);
                        doneFName.setVisibility(View.GONE);
                        cancelFname.setVisibility(View.GONE);InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(firstNameEditText.getWindowToken(), 0);
                    }
                });

                doneFName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProfile("first_name", firstNameEditText,editFName,doneFName,cancelFname);
                    }
                });

            }
        });

        editLName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastNameEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                lastNameEditText.setBackground(edittextDrawable);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(lastNameEditText, InputMethodManager.SHOW_IMPLICIT);

                final View doneLName = view.findViewById(R.id.doneLName);
                final View cancelLname = view.findViewById(R.id.cancelLName);

                editLName.setVisibility(View.GONE);
                doneLName.setVisibility(View.VISIBLE);
                cancelLname.setVisibility(View.VISIBLE);

                cancelLname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableEditText(lastNameEditText);
                        lastNameEditText.setText(Utility.getUserLastName(getActivity()));
                        editLName.setVisibility(View.VISIBLE);
                        doneLName.setVisibility(View.GONE);
                        cancelLname.setVisibility(View.GONE);InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(lastNameEditText.getWindowToken(), 0);
                    }
                });

                doneLName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProfile("last_name", lastNameEditText,editLName,doneLName,cancelLname);
                    }
                });

            }
        });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                locationEditText.setBackground(edittextDrawable);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(locationEditText, InputMethodManager.SHOW_IMPLICIT);

                final View doneLoc = view.findViewById(R.id.doneLoc);
                final View cancelLoc = view.findViewById(R.id.cancelLoc);

                editLocation.setVisibility(View.GONE);
                doneLoc.setVisibility(View.VISIBLE);
                cancelLoc.setVisibility(View.VISIBLE);

                cancelLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableEditText(locationEditText);
                        locationEditText.setText(Utility.getUserCity(getActivity()));
                        editLocation.setVisibility(View.VISIBLE);
                        doneLoc.setVisibility(View.GONE);
                        cancelLoc.setVisibility(View.GONE);InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(locationEditText.getWindowToken(), 0);
                    }
                });

                doneLoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProfile("city", locationEditText,editLocation,doneLoc,cancelLoc);
                    }
                });

            }
        });

        editMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                mobileEditText.setBackground(edittextDrawable);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mobileEditText, InputMethodManager.SHOW_IMPLICIT);

                final View doneMobile = view.findViewById(R.id.doneMobile);
                final View cancelMobile = view.findViewById(R.id.cancelMobile);

                editMobile.setVisibility(View.GONE);
                doneMobile.setVisibility(View.VISIBLE);
                cancelMobile.setVisibility(View.VISIBLE);

                cancelMobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableEditText(mobileEditText);
                        mobileEditText.setText(Utility.getUserFirstName(getActivity()));
                        editMobile.setVisibility(View.VISIBLE);
                        doneMobile.setVisibility(View.GONE);
                        cancelMobile.setVisibility(View.GONE);InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mobileEditText.getWindowToken(), 0);
                    }
                });

                doneMobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProfile("mobile_number", mobileEditText,editMobile,doneMobile,cancelMobile);
                    }
                });

            }
        });

        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobEditText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                dobEditText.setBackground(edittextDrawable);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(dobEditText, InputMethodManager.SHOW_IMPLICIT);

                final View doneDOB = view.findViewById(R.id.doneDob);
                final View cancelDOB = view.findViewById(R.id.cancelDob);

                editDOB.setVisibility(View.GONE);
                doneDOB.setVisibility(View.VISIBLE);
                cancelDOB.setVisibility(View.VISIBLE);

                cancelDOB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        disableEditText(dobEditText);
                        dobEditText.setText(Utility.getUserDOB(getActivity()));
                        editDOB.setVisibility(View.VISIBLE);
                        doneDOB.setVisibility(View.GONE);
                        cancelDOB.setVisibility(View.GONE);InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(dobEditText.getWindowToken(), 0);
                    }
                });

                doneDOB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProfile("dob", dobEditText,editDOB,doneDOB,cancelDOB);
                    }
                });
            }
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
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                Log.e("prfl_updt_error","error");
                if (key.equals("dob"))
                {
                    editText.setText(Utility.getUserDOB(getActivity()));
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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
