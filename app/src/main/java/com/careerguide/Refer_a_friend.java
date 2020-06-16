package com.careerguide;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class Refer_a_friend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_a_friend);
        setTitle("Refer a Friend");
        TextView rew=findViewById(R.id.rew);
        TextView ref=findViewById(R.id.ref);
        TextView fitband=findViewById(R.id.fitband);
        TextView blsp=findViewById(R.id.blsp);
        TextView tab=findViewById(R.id.tab);
        TextView mi=findViewById(R.id.mi);
        ref.setText(Utility.getNumReferrals(this));
        rew.setText(Utility.getRewardPoints(this));
        int refno=Integer.parseInt(Utility.getNumReferrals(this));
        if(refno<=100) {
            fitband.setText((100 - refno) + " More Successful Referrals Left to Redeem:");
            blsp.setText((500 - refno) + " More Successful Referrals Left to Redeem:");
            tab.setText((1000 - refno) + " More Successful Referrals Left to Redeem:");
            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
        }
        else if(refno<=500) {
            fitband.setText("0 More Successful Referrals Left to Redeem:");
            blsp.setText((500 - refno) + " More Successful Referrals Left to Redeem:");
            tab.setText((1000 - refno) + " More Successful Referrals Left to Redeem:");
            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
        }
        else if(refno<=1000) {
            fitband.setText("0 More Successful Referrals Left to Redeem:");
            blsp.setText("0 More Successful Referrals Left to Redeem:");
            tab.setText((1000 - refno) + " More Successful Referrals Left to Redeem:");
            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
        }
        else if(refno<=5000) {
            fitband.setText("0 More Successful Referrals Left to Redeem:");
            blsp.setText("0 More Successful Referrals Left to Redeem:");
            tab.setText("0 More Successful Referrals Left to Redeem:");
            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton button1= findViewById(R.id.button1);
        Button button2= findViewById(R.id.button2);
        button1.setOnClickListener(view -> {
            share();
            /*Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String sharebody="Hi, I will like you to download amazing CareerGuide app which can help you connect to a career counsellor on chat, video, voice, take psychometric tests and plan your career dreams.\n\nAnd Yes...don't forget to share it with your friends and family- After all Sharing is Caring.\n\n\nhttps://play.google.com/store/apps/details?id=com.careerguide";
            intent.putExtra(Intent.EXTRA_TEXT, sharebody);
            startActivity(intent.createChooser(intent, "Share Body"));*/
        });
        button2.setOnClickListener(view -> {
            startActivity(new Intent(this,HomeActivity.class).putExtra("refer", 1));
        });
    }
    public void share() {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.careerguide.com/"+Utility.getUserId(this)+"/"+androidId))
                .setDomainUriPrefix("https://careerguidestudent.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setGoogleAnalyticsParameters(
                        new DynamicLink.GoogleAnalyticsParameters.Builder()
                                .setSource("app")
                                .setMedium("anyone")
                                .setCampaign("example-app-referral")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("CareerGuide Student App")
                                .setDescription("Install the Career Guide Student App to Get Proper Guidance for your career.")
                                .build())
                // Open links with com.example.ios on iOS
               // .setIosParameters(new DynamicLink.IosParameters.Builder("com.careerguide.ios").build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        Log.e("TAG", "share: " + dynamicLink.getUri());

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(dynamicLinkUri.toString()))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("TAG", "onComplete: " + shortLink);

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.setType("text/plain");
                            //Intent shareI=Intent.createChooser(intent,null);
                            startActivity(intent);
                        } else {
                            Log.e("TAG", "onComplete: error" + task.getException());
                            // Error
                            // ...
                        }
                    }
                });
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
