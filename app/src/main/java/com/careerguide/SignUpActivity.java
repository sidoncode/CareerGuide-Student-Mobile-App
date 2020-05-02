package com.careerguide;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class SignUpActivity extends AppCompatActivity
{
    private static final int RC_SIGN_IN = 1;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity activity = this;
    private View signUpLayout, customSignUpLayout, loginLayout;
    private AccessToken accessToken;
    private boolean validEmailSignUp, validPasswordSignUp;
    private boolean validEmailLogin, validPasswordLogin;
    private boolean validCreatePassword, validRetypePassword;
    private View forgotPasswordLayout;
    private View forgotPasswordMessageLayout;
    private LinearLayout resetPasswordLayout;
    private String resetCode;
    private String resetEmail;

    private boolean loginError = false;

    private enum screenType
    {
        SOCIAL,
        COUSTOM,
        LOGIN,
        FORGOT_PASSWORD,
        FORGOT_OTP,
        RESET_PASSWORD
    }

    Stack<screenType> stack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Facebook Start
        /*
        Email:- meracareerguide@gmail.com
        Password:- *PKjf6h4*/
        callbackManager = CallbackManager.Factory.create();


        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        AccessToken.setCurrentAccessToken(null);
        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.e("success", "success");
                    Log.e("permissions", loginResult.getRecentlyGrantedPermissions().toArray().toString());
                    accessToken = loginResult.getAccessToken();
                    parseAccessToken();
                    // App code
                }

                @Override
                public void onCancel() {
                    Log.e("cancel", "cancel");
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.e("error", "error");
                    // App code
                }
            });
        }
        /*else
        {
            //parseAccessToken();
        }*/

        //Facebook Ends

        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.careerguide", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature :info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Google Start

        /*
        Email:- meracareerguide@gmail.com
        Password:- careerguide1*/

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        final View googleSignIn = findViewById(R.id.sign_in_button);
        googleSignIn.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });


        stack.push(screenType.SOCIAL);
        signUpLayout = findViewById(R.id.sign_up_layout);
        customSignUpLayout = findViewById(R.id.coustom_sign_up_layout);
        loginLayout = findViewById(R.id.coustom_log_in_layout);
        forgotPasswordLayout = findViewById(R.id.forgotPassword_layout);
        forgotPasswordMessageLayout = findViewById(R.id.forgotPasswordMessage_layout);
        resetPasswordLayout = findViewById(R.id.reset_password_layout);
        findViewById(R.id.coustom_sign_in_button).setOnClickListener(v -> {
            signUpLayout.setVisibility(View.GONE);
            customSignUpLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            forgotPasswordLayout.setVisibility(View.GONE);
            forgotPasswordMessageLayout.setVisibility(View.GONE);
            resetPasswordLayout.setVisibility(View.GONE);
            stack.push(screenType.COUSTOM);
        });
        findViewById(R.id.loginButton).setOnClickListener(v -> {
            signUpLayout.setVisibility(View.GONE);
            customSignUpLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            forgotPasswordLayout.setVisibility(View.GONE);
            forgotPasswordMessageLayout.setVisibility(View.GONE);
            resetPasswordLayout.setVisibility(View.GONE);
            stack.push(screenType.LOGIN);
        });

        findViewById(R.id.forgotPasswordLink).setOnClickListener(v -> {
            signUpLayout.setVisibility(View.GONE);
            customSignUpLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.GONE);
            forgotPasswordLayout.setVisibility(View.VISIBLE);
            forgotPasswordMessageLayout.setVisibility(View.GONE);
            resetPasswordLayout.setVisibility(View.GONE);
            stack.push(screenType.FORGOT_PASSWORD);

        });

        findViewById(R.id.fbIcon1).setOnClickListener(v -> loginButton.performClick());
        findViewById(R.id.fbIcon2).setOnClickListener(v -> loginButton.performClick());
        findViewById(R.id.fbIcon3).setOnClickListener(v -> loginButton.performClick());
        findViewById(R.id.fbIcon4).setOnClickListener(v -> loginButton.performClick());
        findViewById(R.id.fbSignInBig).setOnClickListener(v -> loginButton.performClick());


        findViewById(R.id.googleIcon1).setOnClickListener(v -> googleSignIn.performClick());

        findViewById(R.id.googleIcon2).setOnClickListener(v -> googleSignIn.performClick());
        findViewById(R.id.googleIcon3).setOnClickListener(v -> googleSignIn.performClick());
        findViewById(R.id.googleIcon4).setOnClickListener(v -> googleSignIn.performClick());
        findViewById(R.id.gSignInBig).setOnClickListener(v -> googleSignIn.performClick());

        initializeCustomSignUp();
        initializeLogIn();
        initializeForgotPassword();

        Uri data = this.getIntent().getData();
        if (data != null && data.isHierarchical())
        {
            String uri = this.getIntent().getDataString();
            //String otp = data.getQueryParameter("otp");
            SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
            resetCode = sharedPreferences.getString("otp","null");
            resetEmail = sharedPreferences.getString("resetEmail","null");
            String otp = uri.substring(uri.lastIndexOf("/") + 1 ,uri.length());
            Log.e("MyApp", "Deep link clicked and otp is: " + uri + "---" + otp);

            if(Utility.getUserId(activity).equals(""))
            {
                if (otp.equals(resetCode))
                {
                    signUpLayout.setVisibility(View.GONE);
                    customSignUpLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    forgotPasswordMessageLayout.setVisibility(View.GONE);
                    resetPasswordLayout.setVisibility(View.VISIBLE);
                    stack.push(screenType.RESET_PASSWORD);
                    initializeResetPassword();
                }
                else
                {
                    Toast.makeText(activity,"Not a valid reset link.",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(activity,"Not a valid reset link.",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,HomeActivity.class));
                finish();
            }
        }
    }

    private void initializeForgotPasswordOTP()
    {
        ((TextView) findViewById(R.id.OTPInstructionsTV)).setText("We have sent an email to your email id " + resetEmail + ".\n\nPlease check it to find the link to reset your password.");
        final EditText OTPEditText = findViewById(R.id.OTPEditText);
        final Button resetPassword = findViewById(R.id.resetPassword);
        OTPEditText.setText("");
        OTPEditText.setEnabled(true);
        resetPassword.setVisibility(View.GONE);
        OTPEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("compare",s + " vs " + resetCode);
                if (s.toString().equals(resetCode))
                {
                    Log.e("compare",s + " vs " + resetCode);
                    OTPEditText.setEnabled(false);
                    OTPEditText.setTextColor(Color.parseColor("#e9397b"));
                    resetPassword.setVisibility(View.VISIBLE);
                    resetPassword.setOnClickListener(v -> {
                        stack.pop();
                        signUpLayout.setVisibility(View.GONE);
                        customSignUpLayout.setVisibility(View.GONE);
                        loginLayout.setVisibility(View.GONE);
                        forgotPasswordLayout.setVisibility(View.GONE);
                        forgotPasswordMessageLayout.setVisibility(View.GONE);
                        resetPasswordLayout.setVisibility(View.VISIBLE);
                        stack.push(screenType.RESET_PASSWORD);
                        initializeResetPassword();
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initializeResetPassword()
    {
        final EditText createPassword = findViewById(R.id.createPassword);
        final EditText retypePassword = findViewById(R.id.retypePassword);
        final Button continueButtonReset = findViewById(R.id.continueButtonReset);


        createPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 6)
                {
                    ((TextView)findViewById(R.id.passwordErrorReset)).setText("Password should contain at least 6 characters.");
                    findViewById(R.id.passwordErrorReset).setVisibility(View.VISIBLE);
                    validCreatePassword = false;
                }
                else
                {
                    findViewById(R.id.passwordErrorReset).setVisibility(View.GONE);
                    validCreatePassword = true;
                }

                if (!createPassword.getText().toString().equals(retypePassword.getText().toString()) && retypePassword.getText().toString().length() > 0)
                {
                    validRetypePassword = false;
                    ((TextView)findViewById(R.id.retypePasswordErrorReset)).setText("Both password should match.");
                    findViewById(R.id.retypePasswordErrorReset).setVisibility(View.VISIBLE);
                }
                else
                {
                    validRetypePassword = true;
                    findViewById(R.id.retypePasswordErrorReset).setVisibility(View.GONE);
                }

                if(validCreatePassword && validRetypePassword)
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

        retypePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {String emailPattern = "[0-9A-Za-z._-]+@[a-z]+\\.+[a-z]+";
                if (s.toString().equals(createPassword.getText().toString()))
                {
                    validRetypePassword = true;
                    findViewById(R.id.retypePasswordErrorReset).setVisibility(View.GONE);
                }
                else
                {
                    findViewById(R.id.retypePasswordErrorReset).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.retypePasswordErrorReset)).setText("Both password should match.");
                    validRetypePassword = false;
                }
                if(validCreatePassword && validRetypePassword)
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
            //final String email = createPassword.getText().toString().trim();
            final String password = createPassword.getText().toString().trim();
            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "change_password", response -> {
                progressDialog.dismiss();
                Log.e("reset_response",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    String msg = jsonObject.optString("msg");
                    if(status && msg.equals("password updated successfully"))
                    {
                        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("otp");
                        editor.remove("resetEmail");
                        editor.apply();
                        JSONObject userJsonObject = jsonObject.optJSONObject("user_detail");
                        String id = userJsonObject.optString("id");
                        String email = userJsonObject.optString("email");
                        String firstName = userJsonObject.optString("first_name");
                        String lastName = userJsonObject.optString("last_name");
                        String profilePic = userJsonObject.optString("profile_pic");
                        String dob = userJsonObject.optString("dob");
                        String gender = userJsonObject.optString("gender");
                        String city = userJsonObject.optString("city");
                        String mobile = userJsonObject.optString("mobile_number");
                        String educationLevel = userJsonObject.optString("education_level");
                        boolean activated = userJsonObject.optString("activated").equals("1");
                        Utility.setUserId(activity,id);
                        Utility.setUserFirstName(activity,firstName);
                        Utility.setUserLasstName(activity,lastName);
                        Utility.setUserPic(activity,profilePic);
                        Utility.setUserCity(activity,city);
                        Utility.setUserDOB(activity,dob);
                        Utility.setUserEmail(activity, email);
                        Utility.setUserGender(activity,gender);
                        Utility.setUserMobile(activity,mobile);
                        Utility.setUserEducation(activity,educationLevel);
                        Utility.setUserActivated(activity,activated);
                        Intent intent = new Intent(activity,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        switch (msg)
                        {
                            case "user does not exist":
                                ((TextView) findViewById(R.id.emailErrorLogin)).setText("Email doesn't exist");
                                findViewById(R.id.emailErrorLogin).setVisibility(View.VISIBLE);
                                break;
                            case "Incorrect password":
                                ((TextView) findViewById(R.id.passwordErrorLogin)).setText("Incorrect password");
                                findViewById(R.id.passwordErrorLogin).setVisibility(View.VISIBLE);
                                break;
                            default:
                                Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("login_error123","error" +error.getMessage());
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("email",resetEmail);
                    params.put("password",password);
                    Log.e("request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        });
    }

    private void initializeForgotPassword() {
        final EditText emailEditText = findViewById(R.id.emailInputFP);
        final Button continueButton = findViewById(R.id.continueButton);
        final boolean validEmail[] = {false};
        emailEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailPattern = "[0-9A-Za-z._-]+@[a-z]+\\.+[a-z]+";
                if (!s.toString().trim().matches(emailPattern))
                {
                    ((TextView)findViewById(R.id.emailErrorFP)).setText("Invalid Email");
                    findViewById(R.id.emailErrorFP).setVisibility(View.VISIBLE);
                    continueButton.setEnabled(false);
                }
                else
                {
                    findViewById(R.id.emailErrorFP).setVisibility(View.GONE);
                    continueButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        continueButton.setOnClickListener(v -> {
            final String email = emailEditText.getText().toString().trim();
            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "forget_password", response -> {
                progressDialog.dismiss();
                Log.e("forget_response",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    String msg = jsonObject.optString("msg");
                    if(status && msg.equals("otp send successfully"))
                    {
                        resetEmail = email;
                        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("resetEmail",email);
                        editor.apply();
                        stack.pop();
                        signUpLayout.setVisibility(View.GONE);
                        customSignUpLayout.setVisibility(View.GONE);
                        loginLayout.setVisibility(View.GONE);
                        forgotPasswordLayout.setVisibility(View.GONE);
                        resetPasswordLayout.setVisibility(View.GONE);
                        forgotPasswordMessageLayout.setVisibility(View.VISIBLE);
                        stack.push(screenType.FORGOT_OTP);
                        initializeForgotPasswordOTP();
                    }
                    else
                    {
                        if (msg.equals("user does not exist"))
                        {
                            ((TextView)findViewById(R.id.emailErrorFP)).setText("Email doesn't exist");
                            findViewById(R.id.emailErrorFP).setVisibility(View.VISIBLE);
                        }
                        else if (msg.equals("user account is not activated yet!"))
                        {
                            ((TextView)findViewById(R.id.emailErrorFP)).setText("Email doesn't exist");
                            findViewById(R.id.emailErrorFP).setVisibility(View.VISIBLE);
                        }
                        else if (msg.equals("user not registered with custom"))
                        {
                            ((TextView)findViewById(R.id.emailErrorFP)).setText("Email doesn't exist");
                            findViewById(R.id.emailErrorFP).setVisibility(View.VISIBLE);
                        }
                        /*  else
                        {
                            Toast.makeText(activity, "Something went wrong.",Toast.LENGTH_LONG).show();
                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("forget_error","error");
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("email",email);
                    params.put("otp",generateOTP());
                    Log.e("request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        });
    }

    private void initializeLogIn() {
        final EditText emailEditText = findViewById(R.id.emailInputLogin);
        final EditText passwordEditText = findViewById(R.id.passwordInputLogin);
        final Button loginButton = findViewById(R.id.login);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String emailPattern = "[0-9A-Za-z._-]+@[a-z]+\\.+[a-z]+";
                if (!s.toString().trim().matches(emailPattern)) {
                    ((TextView)findViewById(R.id.emailErrorLogin)).setText("Invalid Email");
                    findViewById(R.id.emailErrorLogin).setVisibility(View.VISIBLE);
                    findViewById(R.id.signUPLink).setVisibility(View.GONE);
                    validEmailLogin = false;
                }
                else
                {
                    findViewById(R.id.emailErrorLogin).setVisibility(View.GONE);
                    findViewById(R.id.signUPLink).setVisibility(View.GONE);
                    validEmailLogin = true;
                }
                if(validEmailLogin && validPasswordLogin)
                {
                    loginButton.setEnabled(true);
                }
                else
                {
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 6)
                {
                    ((TextView)findViewById(R.id.passwordErrorLogin)).setText("Password should contain at least 6 characters.");
                    findViewById(R.id.passwordErrorLogin).setVisibility(View.VISIBLE);
                    validPasswordLogin = false;
                }
                else
                {
                    findViewById(R.id.passwordErrorLogin).setVisibility(View.GONE);
                    validPasswordLogin = true;
                }

                if(validEmailLogin && validPasswordLogin)
                {
                    loginButton.setEnabled(true);
                }
                else
                {
                    loginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginButton.setOnClickListener(v -> {
            final String email = emailEditText.getText().toString().trim();
            final String password = passwordEditText.getText().toString().trim();
            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "login", response -> {
                progressDialog.dismiss();
                Log.e("login_response",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    String msg = jsonObject.optString("msg");
                    if(status && msg.equals("login successful"))
                    {
                        JSONObject userJsonObject = jsonObject.optJSONObject("user");
                        String id = userJsonObject.optString("id");
                        String email1 = userJsonObject.optString("email");
                        String firstName = userJsonObject.optString("first_name");
                        String lastName = userJsonObject.optString("last_name");
                        String profilePic = userJsonObject.optString("profile_pic");
                        String dob = userJsonObject.optString("dob");
                        String gender = userJsonObject.optString("gender");
                        String city = userJsonObject.optString("city");
                        String mobile = userJsonObject.optString("mobile_number");
                        String educationLevel = userJsonObject.optString("education_level");
                        boolean activated = userJsonObject.optString("activated").equals("1");
                        Utility.setUserId(activity,id);
                        Utility.setUserFirstName(activity,firstName);
                        Utility.setUserLasstName(activity,lastName);
                        Utility.setUserPic(activity,profilePic);
                        Utility.setUserCity(activity,city);
                        Utility.setUserDOB(activity,dob);
                        Utility.setUserEmail(activity, email1);
                        Utility.setUserGender(activity,gender);
                        Utility.setUserMobile(activity,mobile);
                        Utility.setUserEducation(activity,educationLevel);
                        Utility.setUserActivated(activity,activated);
                        Intent intent = new Intent(activity,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        switch (msg) {
                            case "user does not exist":
                                ((TextView) findViewById(R.id.emailErrorLogin)).setText("Email id not registered. Please Sign up");
                                findViewById(R.id.emailErrorLogin).setVisibility(View.VISIBLE);
                                findViewById(R.id.signUPLink).setVisibility(View.VISIBLE);
                                findViewById(R.id.signUPLink).setOnClickListener(v1 -> {
                                    findViewById(R.id.coustom_sign_in_button).performClick();
                                    ((EditText)findViewById(R.id.emailInput)).setText(email);
                                    ((EditText)findViewById(R.id.passwordInput)).setText(password);
                                    findViewById(R.id.loginErrorMessage).setVisibility(View.VISIBLE);
                                    loginError = true;
                                });
                                break;
                            case "Incorrect password":
                                ((TextView) findViewById(R.id.passwordErrorLogin)).setText("Incorrect password");
                                findViewById(R.id.passwordErrorLogin).setVisibility(View.VISIBLE);
                                break;
                            default:
                                Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("login_error","error" +error.getMessage());
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("email",email);
                    params.put("password",password);
                    Log.e("request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        });
    }

    private void initializeCustomSignUp()
    {
        final EditText emailEditText = findViewById(R.id.emailInput);
        final EditText passwordEditText = findViewById(R.id.passwordInput);
        final Button signUpButton = findViewById(R.id.signUpButton);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailPattern = "[0-9A-Za-z._-]+@[a-z]+\\.+[a-z]+";
                if (!s.toString().trim().matches(emailPattern)) {
                    ((TextView)findViewById(R.id.emailError)).setText("Invalid Email");
                    findViewById(R.id.emailError).setVisibility(View.VISIBLE);
                    validEmailSignUp = false;
                }
                else
                {
                    findViewById(R.id.emailError).setVisibility(View.GONE);
                    validEmailSignUp = true;
                }
                if(validEmailSignUp && validPasswordSignUp)
                {
                    signUpButton.setEnabled(true);
                }
                else
                {
                    signUpButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() < 6)
                {
                    ((TextView)findViewById(R.id.passwordError)).setText("Password should contain at least 6 characters.");
                    findViewById(R.id.passwordError).setVisibility(View.VISIBLE);
                    validPasswordSignUp = false;
                }
                else
                {
                    findViewById(R.id.passwordError).setVisibility(View.GONE);
                    validPasswordSignUp = true;
                }

                if(validEmailSignUp && validPasswordSignUp)
                {
                    signUpButton.setEnabled(true);
                }
                else
                {
                    signUpButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        signUpButton.setOnClickListener(v -> {
            final String email = emailEditText.getText().toString().trim();
            final String password = passwordEditText.getText().toString().trim();
            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "user_exist", response -> {
                progressDialog.dismiss();
                Log.e("user_exist_response",response);

                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    if(status)
                    {
                        //user exist
                        ((TextView)findViewById(R.id.emailError)).setText("Email id already registered.");
                        findViewById(R.id.emailError).setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        String msg = jsonObject.optString("msg");
                        switch (msg) {
                            case "db query failed":
                                Toast.makeText(activity, "Something went wrong.\nPlease Retry...", Toast.LENGTH_LONG).show();
                                break;
                            case "user exist":
                                JSONObject userJsonObject = jsonObject.optJSONObject("user");
                                String id = userJsonObject.optString("id");
                                String emailRes = userJsonObject.optString("email");
                                String firstName = userJsonObject.optString("first_name");
                                String lastName = userJsonObject.optString("last_name");
                                String profilePic = userJsonObject.optString("profile_pic");
                                String dob = userJsonObject.optString("dob");
                                String gender = userJsonObject.optString("gender");
                                String city = userJsonObject.optString("city");
                                String mobile = userJsonObject.optString("mobile_number");
                                String educationLevel = userJsonObject.optString("education_level");
                                boolean activated = userJsonObject.optString("activated").equals("1");
                                Utility.setUserId(activity,id);
                                Utility.setUserFirstName(activity,firstName);
                                Utility.setUserLasstName(activity,lastName);
                                Utility.setUserPic(activity,profilePic);
                                Utility.setUserCity(activity,city);
                                Utility.setUserDOB(activity,dob);
                                Utility.setUserEmail(activity, emailRes);
                                Utility.setUserGender(activity,gender);
                                Utility.setUserMobile(activity,mobile);
                                Utility.setUserEducation(activity,educationLevel);
                                Utility.setUserActivated(activity,activated);
                                Intent intent = new Intent(activity,HomeActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case "registered with facebook":
                                proceed("", "", "", email, "", "", "custom", password);
                                break;
                            case "registered with google":
                                proceed("", "", "", email, "", "", "custom", password);
                                break;
                            case "User not found":
                                proceed("", "", "", email, "", "", "custom", password);
                                break;
                            default:
                                Toast.makeText(activity, "Something went wrong.\nPlease Retry...", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("user_exist_error","error");
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("email",email);
                    params.put("password",password);
                    Log.e("request",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();// Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        /*GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);*/
    }

    @Override
    public void onBackPressed() {
        if(stack.size() <2) {
            super.onBackPressed();
        }
        else
        {
            stack.pop();
            screenType topStack = stack.peek();
            switch (topStack)
            {
                case COUSTOM:
                    customSignUpLayout.setVisibility(View.VISIBLE);
                    signUpLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.GONE);
                    forgotPasswordMessageLayout.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    resetPasswordLayout.setVisibility(View.GONE);
                    if (loginError)
                    {
                        /*((EditText)findViewById(R.id.emailInput)).setText("");
                        ((EditText)findViewById(R.id.passwordInput)).setText("");
                        findViewById(R.id.loginErrorMessage).setVisibility(View.VISIBLE);*/
                    }
                    break;
                case LOGIN:
                    customSignUpLayout.setVisibility(View.GONE);
                    signUpLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.VISIBLE);
                    forgotPasswordMessageLayout.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    resetPasswordLayout.setVisibility(View.GONE);
                    break;
                case SOCIAL:
                    customSignUpLayout.setVisibility(View.GONE);
                    signUpLayout.setVisibility(View.VISIBLE);
                    loginLayout.setVisibility(View.GONE);
                    forgotPasswordMessageLayout.setVisibility(View.GONE);
                    forgotPasswordLayout.setVisibility(View.GONE);
                    resetPasswordLayout.setVisibility(View.GONE);
                    break;
                //case FORGOT_OTP:

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
Log.e("result code" , "resultcode:"+resultCode);
        Log.e("result code" , "resultcode:"+requestCode);
            if (requestCode == RC_SIGN_IN)
        {
            Log.d("#code321", "+requestCode");

            //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        Log.d("#code", "+requestCode");
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google SignIn", "signInResult:failed code=" + e.getStatusCode());

            Log.e("Google SignIn", "signInmessage=" + e.getMessage());
            //updateUI(null);
        }

    }

    private void parseAccessToken()
    {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                (jsonObject, response) -> {
                    // Getting FB User Data

                    String id = null;
                    String firstName = "";
                    String lastName = "";
                    String email = "";
                    String gender = "";
                    URL profile_pic = null;
                    try {
                        id = jsonObject.getString("id");
                        if (jsonObject.has("first_name"))
                            firstName = jsonObject.getString("first_name");
                        if (jsonObject.has("last_name"))
                            lastName = jsonObject.getString("last_name");
                        if (jsonObject.has("email"))
                            email = jsonObject.getString("email");
                        if (jsonObject.has("gender"))
                            gender = jsonObject.getString("gender");
                        try {
                            profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                            Log.e("profile_pic", profile_pic + "");
                        }
                        catch (MalformedURLException e)
                        {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (email != null && email.length()>0) {
                        if (id != null) {
                            Log.e("info", id + (" first_name ") + firstName + (" last_name ") + lastName + (" email ") + email + (" gender ") + gender);
                            proceed(id, firstName, lastName, email, gender, profile_pic == null ? "" : profile_pic.toString(), "facebook", "");
                            LoginManager.getInstance().logOut();
                            //finish();
                        }
                    }
                    else
                    {
                        Toast.makeText(activity,"Unable to fetch email id from facebook",Toast.LENGTH_LONG).show();
                        LoginManager.getInstance().logOut();
                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void updateUI(GoogleSignInAccount account)
    {
        if(account != null)
        {
            String email = account.getEmail();
            String name = account.getDisplayName();
            //String idToken = account.getIdToken();
            String id = account.getId();
            Uri image = account.getPhotoUrl();
            Set<Scope> scope = account.getGrantedScopes();

            String[] parts = name.split(" ");
            String firstName = "";
            String lastName = "";
            if (parts.length == 2)
            {
                firstName = parts[0];
                lastName = parts[1];
            }
            else if (parts.length == 3)
            {
                firstName = parts[0];
                lastName = parts[2];
            }
            else
            {
                firstName = name;
            }

            //Toast.makeText(activity,email + ", " + name + ", " + id,Toast.LENGTH_LONG).show();
            Log.e("userDetail Google" , email + ", " + name + ", " + id  + ", " + image);
            Log.e("Scope Google", scope.toString());
            String imageString = "";
            try {
                if (image != null)
                {
                    imageString = image.toString();
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            proceed(id,firstName,lastName,email,"",imageString,"google","" );
            //finish();
        }
    }

    protected String generateOTP()
    {
        Random rnd = new Random();
        String randomDigits = "01234567890";
        resetCode = "";
        for (int i = 0; i < 6; i++) {
            resetCode += randomDigits.charAt(rnd.nextInt(randomDigits.length()));
        }
        Log.e("OTP generated", resetCode);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("otp", resetCode);
        editor.apply();
        return resetCode;
    }


    private void proceed(final String id, final String firstName, final String lastName, final String email, final String gender, final String profilePic, final String source, final String password)
    {
        if (source.equals("custom"))
        {
            Intent intent = new Intent(activity,ProfileDetailActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("firstName",firstName);
            intent.putExtra("lastName",lastName);
            intent.putExtra("email",email);
            intent.putExtra("gender",gender.toLowerCase());
            intent.putExtra("profilePic",profilePic);
            intent.putExtra("source",source);
            intent.putExtra("password",password);
            startActivity(intent);
            finish();
        }
        else
        {
            final ProgressDialog progressDialog = new ProgressDialogCustom(activity);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "social_login", response -> {
                progressDialog.dismiss();
                Log.e("scl_lgn_response",response);
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    String msg = jsonObject.optString("msg");
                    if(status)
                    {
                        JSONObject userJsonObject = jsonObject.optJSONObject("data");
                        String id1 = userJsonObject.optString("id");
                        String email1 = userJsonObject.optString("email");
                        String firstName1 = userJsonObject.optString("first_name");
                        String lastName1 = userJsonObject.optString("last_name");
                        String profilePic1 = userJsonObject.optString("profile_pic");
                        String dob = userJsonObject.optString("dob");
                        String gender1 = userJsonObject.optString("gender");
                        String city = userJsonObject.optString("city");
                        String mobile = userJsonObject.optString("mobile_number");
                        String educationLevel = userJsonObject.optString("education_level");
                        boolean activated = userJsonObject.optString("activated").equals("1");
                        Utility.setUserId(activity, id1);
                        Utility.setUserFirstName(activity, firstName1);
                        Utility.setUserLasstName(activity, lastName1);
                        Utility.setUserPic(activity, profilePic1);
                        Utility.setUserCity(activity,city);
                        Utility.setUserDOB(activity,dob);
                        Utility.setUserEmail(activity, email1);
                        Utility.setUserGender(activity, gender1);
                        Utility.setUserMobile(activity,mobile);
                        Utility.setUserEducation(activity,educationLevel);
                        Utility.setUserActivated(activity,activated);
                        Intent intent = new Intent(activity,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(activity,ProfileDetailActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("firstName",firstName);
                        intent.putExtra("lastName",lastName);
                        intent.putExtra("email",email);
                        intent.putExtra("gender",gender.toLowerCase());
                        intent.putExtra("profilePic",profilePic);
                        intent.putExtra("source",source);
                        intent.putExtra("password",password);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                progressDialog.dismiss();
                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("scl_lgn_error","error");
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("email",email);
                    params.put(source + "_id",id);
                    Log.e("scl_lgn_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        }
    }
}
