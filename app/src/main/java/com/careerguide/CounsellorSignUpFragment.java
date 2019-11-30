package com.careerguide;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class CounsellorSignUpFragment extends Fragment {


    public CounsellorSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_counsellor_sign_up, container, false);

        final Button button = view.findViewById(R.id.button);
        final Button join_now=view.findViewById(R.id.join_now);



        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_counsellor_sign_up,null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                final EditText nameEditText = dialogView.findViewById(R.id.name);
                final EditText mobileEditText = dialogView.findViewById(R.id.mobileNumber);
                final EditText emailEditText = dialogView.findViewById(R.id.emailId);
                final EditText linkedInLinkEditText = dialogView.findViewById(R.id.linkedInLink);
                final EditText detailEditText = dialogView.findViewById(R.id.detail);

                final TextView nameTextView = dialogView.findViewById(R.id.nameError);
                final TextView mobileTextView = dialogView.findViewById(R.id.mobileNumberError);
                final TextView emailTextView = dialogView.findViewById(R.id.emailError);
                final TextView linkedTextView = dialogView.findViewById(R.id.linkedInLinkError);
                final TextView detailTextView = dialogView.findViewById(R.id.detailError);

                final Button submitButton = dialogView.findViewById(R.id.submitButton);

                nameEditText.setText(Utility.getUserFirstName(getActivity()) + " " + Utility.getUserLastName(getActivity()));
                emailEditText.setText(Utility.getUserEmail(getActivity()));
                String mobile = Utility.getUserMobile(getActivity());
                if (!mobile.isEmpty())
                {
                    mobileEditText.setText(mobile);
                }

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String name = nameEditText.getText().toString().trim();
                        final String mobile = mobileEditText.getText().toString().trim();
                        final String linkedInLink = linkedInLinkEditText.getText().toString().trim();
                        final String detail = detailEditText.getText().toString().trim();
                        final String email = emailEditText.getText().toString().trim();

                        boolean fine = true;
                        if(name.isEmpty())
                        {
                            nameTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            nameTextView.setVisibility(View.INVISIBLE);
                        }
                        if(mobile.isEmpty() || mobile.length() != 10)
                        {
                            mobileTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            mobileTextView.setVisibility(View.INVISIBLE);
                        }
                        if (email.isEmpty())
                        {
                            emailTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            emailTextView.setVisibility(View.INVISIBLE);
                        }
                        if(linkedInLink.isEmpty())
                        {
                            linkedTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            linkedTextView.setVisibility(View.INVISIBLE);

                            //Validating linkedin id pattern
                            if(!isValidid(linkedInLink)){
                                linkedTextView.setText("Enter a valid LinkedIn Id");
                                linkedTextView.setVisibility(View.VISIBLE);
                                /*Toast.makeText(getActivity(), "Enter a Valid Linked Id", Toast.LENGTH_LONG).show();*/
                                fine=false;
                            }
                            else{
                            }



                        }
                        if (detail.isEmpty())
                        {
                            detailTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            detailTextView.setVisibility(View.INVISIBLE);
                        }
                        Log.e("fine",fine + "");
                        if (fine)
                        {
                            alertDialog.dismiss();

                            final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity()).showIt();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "counsellor_registration", new Response.Listener<String>()
                            {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(String response)
                                {
                                    if (isAdded()) {
                                        progressDialog.dismiss();
                                        Log.e("counsellor_response", response);
                                        try
                                        {


                                           Intent intent=new Intent(getActivity(), All_done.class);
                                           startActivity(intent);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }, new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (isAdded()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), VoleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
                                        Log.e("counsellor_error", "error");
                                    }
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String,String> params = new HashMap<>();
                                    params.put("name",name);
                                    params.put("mobile",mobile);
                                    params.put("linkedin_profile_link",linkedInLink);
                                    params.put("desc",detail);
                                    params.put("email",email);
                                    params.put("user_id",Utility.getUserId(getActivity()));
                                    Log.e("request",params.toString());
                                    return params;
                                }
                            };
                            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                        }
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
        join_now.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_counsellor_sign_up,null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

                final EditText nameEditText = dialogView.findViewById(R.id.name);
                final EditText mobileEditText = dialogView.findViewById(R.id.mobileNumber);
                final EditText emailEditText = dialogView.findViewById(R.id.emailId);
                final EditText linkedInLinkEditText = dialogView.findViewById(R.id.linkedInLink);
                final EditText detailEditText = dialogView.findViewById(R.id.detail);

                final TextView nameTextView = dialogView.findViewById(R.id.nameError);
                final TextView mobileTextView = dialogView.findViewById(R.id.mobileNumberError);
                final TextView emailTextView = dialogView.findViewById(R.id.emailError);
                final TextView linkedTextView = dialogView.findViewById(R.id.linkedInLinkError);
                final TextView detailTextView = dialogView.findViewById(R.id.detailError);

                final Button submitButton = dialogView.findViewById(R.id.submitButton);

                nameEditText.setText(Utility.getUserFirstName(getActivity()) + " " + Utility.getUserLastName(getActivity()));
                emailEditText.setText(Utility.getUserEmail(getActivity()));
                String mobile = Utility.getUserMobile(getActivity());
                if (!mobile.isEmpty())
                {
                    mobileEditText.setText(mobile);
                }

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String name = nameEditText.getText().toString().trim();
                        final String mobile = mobileEditText.getText().toString().trim();
                        final String linkedInLink = linkedInLinkEditText.getText().toString().trim();
                        final String detail = detailEditText.getText().toString().trim();
                        final String email = emailEditText.getText().toString().trim();

                        boolean fine = true;
                        if(name.isEmpty())
                        {
                            nameTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            nameTextView.setVisibility(View.INVISIBLE);
                        }
                        if(mobile.isEmpty() || mobile.length() != 10)
                        {
                            mobileTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            mobileTextView.setVisibility(View.INVISIBLE);
                        }
                        if (email.isEmpty())
                        {
                            emailTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            emailTextView.setVisibility(View.INVISIBLE);
                        }
                        if(linkedInLink.isEmpty())
                        {
                            linkedTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            linkedTextView.setVisibility(View.INVISIBLE);

                            //Validating linkedin id pattern
                            if(!isValidid(linkedInLink)){
                                linkedTextView.setText("Enter a valid LinkedIn Id");
                                linkedTextView.setVisibility(View.VISIBLE);
                                /*Toast.makeText(getActivity(), "Enter a Valid Linked Id", Toast.LENGTH_LONG).show();*/
                                fine=false;
                            }
                            else{
                            }
                        }
                        if (detail.isEmpty())
                        {
                            detailTextView.setVisibility(View.VISIBLE);
                            fine = false;
                        }
                        else
                        {
                            detailTextView.setVisibility(View.INVISIBLE);
                        }
                        Log.e("fine",fine + "");
                        if (fine)
                        {
                            alertDialog.dismiss();

                            final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity()).showIt();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "counsellor_registration", new Response.Listener<String>()
                            {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(String response)
                                {
                                    if (isAdded()) {
                                        progressDialog.dismiss();
                                        Log.e("counsellor_response", response);
                                        try
                                        {
                                            Intent intent=new Intent(getActivity(), All_done.class);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            }, new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (isAdded()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), VoleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
                                        Log.e("counsellor_error", "error");
                                    }
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String,String> params = new HashMap<>();
                                    params.put("name",name);
                                    params.put("mobile",mobile);
                                    params.put("linkedin_profile_link",linkedInLink);
                                    params.put("desc",detail);
                                    params.put("email",email);
                                    params.put("user_id",Utility.getUserId(getActivity()));
                                    Log.e("request",params.toString());
                                    return params;
                                }
                            };
                            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                        }
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
        return view;
    }
    // validating linkedin id
    private boolean isValidid(String linkedInLink) {
        String LINKEDIN_PATTERN = "^https?://((www|\\w\\w)\\.)?linkedin.com/((in/[^/]+/?)|(pub/[^/]+/((\\w|\\d)+/?){3}))$";
        Pattern pattern = Pattern.compile(LINKEDIN_PATTERN);
        Matcher matcher = pattern.matcher(linkedInLink);
        return matcher.matches();
    }

}
