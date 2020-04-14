package com.careerguide;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class FacebookSignUp extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_sign_up);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
