package com.careerguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.careerguide.payment.PaymentActivity;

public class PlanActivity extends AppCompatActivity
{
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        btn =findViewById(R.id.payment);
        btn.setOnClickListener(v -> {
            startActivity(new Intent(this , PaymentActivity.class));
        });
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

    public void backpress(View view) {
        finish();
    }

    public void back_onClick(View view) {
        finish();
    }
}
