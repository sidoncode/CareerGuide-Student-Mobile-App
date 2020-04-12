package com.careerguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class SettingActivity extends AppCompatActivity {

    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.logOut).setOnClickListener(v -> {
            final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            final View dialog = getLayoutInflater().inflate(R.layout.dialog_log_out,null);

            dialog.findViewById(R.id.no).setOnClickListener(v1 -> alertDialog.dismiss());
            dialog.findViewById(R.id.yes).setOnClickListener(v12 -> {
                if (Utility.logOut(activity))
                {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("hideSplash",true);
                    alertDialog.dismiss();
                    finish();
                    startActivity(i);
                }
                else {
                    Toast.makeText(activity, "Something went wrong. Please Retry", Toast.LENGTH_LONG).show();
                }
            });
            alertDialog.setView(dialog);
            alertDialog.show();
        });

        findViewById(R.id.notiPref).setOnClickListener(v -> {

            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "get_noti_pre", new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    progressDialog.dismiss();
                    Log.e("get_pref_res",response);
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.optBoolean("status",false);
                        if(status)
                        {
                            JSONObject notiPreJsonObject = jsonObject.optJSONObject("noti_pre");
                            boolean email = notiPreJsonObject.optInt("email",1) == 1;
                            boolean message = notiPreJsonObject.optInt("message",1) == 1;
                            boolean call = notiPreJsonObject.optInt("call",1) == 1;
                            showNotiPrefDialog(email, message, call);
                        }
                        else
                        {
                            Toast.makeText(activity, "Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("get_pref_error","error");
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    Log.e("get_pref_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        });

        findViewById(R.id.changePass).setOnClickListener(v -> checkLogin());
    }

    private void checkLogin() {
        final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "user_register_status", response -> {
            progressDialog.dismiss();
            Log.e("reg_stat_res",response);
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status",false);
                if(status) //custom login
                {
                    showResetPass();
                }
                else //social login
                {
                    showCreatePass();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("reg_stat_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                Log.e("reg_stat_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    private void showResetPass() {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        final View dialog = getLayoutInflater().inflate(R.layout.dialog_reset_password,null);
        final EditText oldPassEditText = dialog.findViewById(R.id.oldPassEditText);
        final EditText createPassEditText = dialog.findViewById(R.id.createPassEditText);
        final EditText retypePassEditText = dialog.findViewById(R.id.retypePassEditText);
        final TextView oldPassTextView = dialog.findViewById(R.id.oldPassError);
        final Button continueButtonReset = dialog.findViewById(R.id.confirmButton);
        final boolean valid[] = new boolean[2]; //0-createPass, 1-retypepass

        oldPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPassTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        createPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 6)
                {
                    ((TextView) dialog.findViewById(R.id.passwordErrorReset)).setText("Password should contain atleast 6 characters.");
                    dialog.findViewById(R.id.passwordErrorReset).setVisibility(View.VISIBLE);
                    valid[0] = false;
                }
                else
                {
                    dialog.findViewById(R.id.passwordErrorReset).setVisibility(View.GONE);
                    valid[0] = true;
                }

                if (!createPassEditText.getText().toString().equals(retypePassEditText.getText().toString()) && retypePassEditText.getText().toString().length() > 0)
                {
                    valid[1] = false;
                    ((TextView) dialog.findViewById(R.id.retypePasswordErrorReset)).setText("Both password should match.");
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.VISIBLE);
                }
                else
                {
                    valid[1] = true;
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.GONE);
                }

                if(valid[0] && valid[1])
                {
                    continueButtonReset.setEnabled(true);
                }
                else
                {
                    continueButtonReset.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        retypePassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(createPassEditText.getText().toString()))
                {
                    valid[1] = true;
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.GONE);
                }
                else
                {
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.retypePasswordErrorReset)).setText("Both password should match.");
                    valid[1] = false;
                }
                if(valid[0] && valid[1])
                {
                    continueButtonReset.setEnabled(true);
                }
                else
                {
                    continueButtonReset.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        continueButtonReset.setOnClickListener(v -> checkOldPassword(oldPassEditText.getText().toString().trim(),oldPassTextView, createPassEditText.getText().toString().trim(),alertDialog));
        alertDialog.setView(dialog);
        alertDialog.show();
    }

    private void checkOldPassword(final String oldPass, final TextView oldPassTextView, final String newPass, final AlertDialog alertDialog) {
        if (oldPass.isEmpty())
        {
            oldPassTextView.setVisibility(View.VISIBLE);
            oldPassTextView.setText("Please insert old password.");
            return;
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "check_password ", response -> {
                progressDialog.dismiss();
                Log.e("check_pass_res",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    if(status)
                    {
                        resetPass(newPass,alertDialog);
                    }
                    else
                    {
                        oldPassTextView.setVisibility(View.VISIBLE);
                        oldPassTextView.setText("Incorrect Password");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("check_pass_error","error");
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    params.put("password",oldPass);
                    Log.e("check_pass_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        }
    }

    private void resetPass(final String newPass, final AlertDialog alertDialog) {
        final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "reset_password ", response -> {
            progressDialog.dismiss();
            Log.e("reset_pass_res",response);
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status",false);
                if(status)
                {
                    Toast.makeText(activity, "Password changed successfully.",Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
                else
                {
                    Toast.makeText(activity, "Something went wrong",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            progressDialog.dismiss();
            Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
            Log.e("reset_pass_error","error");
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                params.put("password",newPass);
                Log.e("reset_pass_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    private void showCreatePass() {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        final View dialog = getLayoutInflater().inflate(R.layout.dialog_create_password,null);
        final EditText createPassEditText = dialog.findViewById(R.id.createPassEditText);
        final EditText retypePassEditText = dialog.findViewById(R.id.retypePassEditText);
        final Button continueButtonReset = dialog.findViewById(R.id.confirmButton);
        final boolean valid[] = new boolean[2]; //0-createPass, 1-retypepass


        createPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 6)
                {
                    ((TextView) dialog.findViewById(R.id.passwordErrorReset)).setText("Password should contain atleast 6 characters.");
                    dialog.findViewById(R.id.passwordErrorReset).setVisibility(View.VISIBLE);
                    valid[0] = false;
                }
                else
                {
                    dialog.findViewById(R.id.passwordErrorReset).setVisibility(View.GONE);
                    valid[0] = true;
                }

                if (!createPassEditText.getText().toString().equals(retypePassEditText.getText().toString()) && retypePassEditText.getText().toString().length() > 0)
                {
                    valid[1] = false;
                    ((TextView) dialog.findViewById(R.id.retypePasswordErrorReset)).setText("Both password should match.");
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.VISIBLE);
                }
                else
                {
                    valid[1] = true;
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.GONE);
                }

                if(valid[0] && valid[1])
                {
                    continueButtonReset.setEnabled(true);
                }
                else
                {
                    continueButtonReset.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        retypePassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(createPassEditText.getText().toString()))
                {
                    valid[1] = true;
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.GONE);
                }
                else
                {
                    dialog.findViewById(R.id.retypePasswordErrorReset).setVisibility(View.VISIBLE);
                    ((TextView) dialog.findViewById(R.id.retypePasswordErrorReset)).setText("Both password should match.");
                    valid[1] = false;
                }
                if(valid[0] && valid[1])
                {
                    continueButtonReset.setEnabled(true);
                }
                else
                {
                    continueButtonReset.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        continueButtonReset.setOnClickListener(v -> {

            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "creat_password ", response -> {
                progressDialog.dismiss();
                Log.e("cre_pass_res",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    if(status)
                    {
                        Toast.makeText(activity, "Password created successfully.",Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(activity, "Something went wrong",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("cre_pass_error","error");
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    params.put("password",createPassEditText.getText().toString().trim());
                    Log.e("cre_pass_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        });
        alertDialog.setView(dialog);
        alertDialog.show();
    }

    private void showNotiPrefDialog(boolean email, boolean message, boolean call) {

        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        final View dialog = getLayoutInflater().inflate(R.layout.dialog_noti_pref,null);
        final CheckBox emailCheckBox = dialog.findViewById(R.id.email);
        final CheckBox messageCheckBox = dialog.findViewById(R.id.message);
        final CheckBox callCheckBox = dialog.findViewById(R.id.call);

        emailCheckBox.setChecked(email);
        messageCheckBox.setChecked(message);
        callCheckBox.setChecked(call);


        dialog.findViewById(R.id.confirmButton).setOnClickListener(v -> {
            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "noti_pre", new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                {
                    progressDialog.dismiss();
                    Log.e("noti_pre_res",response);
                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.optBoolean("status",false);
                        if(status)
                        {
                            Toast.makeText(activity, "Preferences saved successfully.",Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(activity, "Something went wrong",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                    Log.e("noti_pre_error","error");
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("user_id",Utility.getUserId(activity));
                    params.put("email",emailCheckBox.isChecked()?"1":"0");
                    params.put("message",messageCheckBox.isChecked()?"1":"0");
                    params.put("call",callCheckBox.isChecked()?"1":"0");
                    Log.e("noti_pre_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        });
        alertDialog.setView(dialog);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.handleOnlineStatus(this, "idle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.handleOnlineStatus(null,"");
    }
}
