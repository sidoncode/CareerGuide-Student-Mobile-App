package com.careerguide;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.File;

import org.w3c.dom.Text;

public class Refer_a_friend extends AppCompatActivity {
    Uri imageUri;
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
//        if(refno<=100) {
//            fitband.setText((100 - refno) + " More Successful Referrals Left to Redeem:");
//            blsp.setText((500 - refno) + " More Successful Referrals Left to Redeem:");
//            tab.setText((1000 - refno) + " More Successful Referrals Left to Redeem:");
//            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
//        }
//        else if(refno<=500) {
//            fitband.setText("0 More Successful Referrals Left to Redeem:");
//            blsp.setText((500 - refno) + " More Successful Referrals Left to Redeem:");
//            tab.setText((1000 - refno) + " More Successful Referrals Left to Redeem:");
//            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
//        }
//        else if(refno<=1000) {
//            fitband.setText("0 More Successful Referrals Left to Redeem:");
//            blsp.setText("0 More Successful Referrals Left to Redeem:");
//            tab.setText((1000 - refno) + " More Successful Referrals Left to Redeem:");
//            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
//        }
//        else if(refno<=5000) {
//            fitband.setText("0 More Successful Referrals Left to Redeem:");
//            blsp.setText("0 More Successful Referrals Left to Redeem:");
//            tab.setText("0 More Successful Referrals Left to Redeem:");
//            mi.setText((5000 - refno) + " More Successful Referrals Left to Redeem:");
//        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView button1= findViewById(R.id.invite_now_1);
        TextView button2= findViewById(R.id.invite_now_2);
        TextView button3= findViewById(R.id.invite_now_3);

        button1.setOnClickListener(view -> {
            share();

        });

        button2.setOnClickListener(view -> {
            share();
        });

        button3.setOnClickListener(view -> {
            share();
        });

//        button2.setOnClickListener(view -> {
//            startActivity(new Intent(this,HomeActivity.class).putExtra("refer", 1));
//        });
    }
    public void share() {

        String img;
        String androidId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if(Utility.getRefImg(this).equals("")) {
            imageUri = null;
            try {
                imageUri = Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),
                        BitmapFactory.decodeResource(getResources(), R.drawable.prizesshare), null, null));
            } catch (NullPointerException e) {
            }
            img=imageUri.toString();
            Utility.setRefImg(img,this);
        }
        else
            img=Utility.getRefImg(this);
        Toast.makeText(this,"Opening apps...",Toast.LENGTH_LONG).show();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(img) );
        shareIntent.putExtra(Intent.EXTRA_TEXT, "It's real\n" +
                "  ✅ Register to get ₹ 10 instantly for free!\n" +
                "  ✅ Check in Daily to withdraw Cash\n" +
                "  ✅ Earn Upto Rs ₹ 1000/day\n" +
                "\n" +
                "\uD83D\uDC47 Download CareerGuide App now to join now! \uD83D\uDC47\n"
                        + Utility.getRefId(this));
        startActivity(Intent.createChooser(shareIntent, "Choose an app"));

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