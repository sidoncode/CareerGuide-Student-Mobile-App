package com.careerguide.Book_Appoinment;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookingTime extends AppCompatActivity implements PaymentResultWithDataListener {
    ProgressBar pb_loading;
    Activity activity = this;
    TextView tv_name;
    ImageView iv_avatar;
    Button book;
    private String order_id;
    String productinfo = "product1";
    CustomSuccessAlert customSuccessAlert = new CustomSuccessAlert(this , this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_time);
        pb_loading = findViewById(R.id.pb_loading);
        String uid = getIntent().getStringExtra("Uid");
        tv_name = findViewById(R.id.tv_name);
        iv_avatar = findViewById(R.id.iv_avatar);
        book = findViewById(R.id.book);
        GetCounsellor(uid);


        book.setOnClickListener(v->{
            GetOrderId();
           // this.startActivity(new Intent(this , PaymentActivity.class));
        });
    }


    public void GetCounsellor(String Uid){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER+"GetCounsellor", response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status) {
                    JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                    pb_loading.setVisibility(View.GONE);
                    for (int i = 0; counsellorsJsonArray != null && i < counsellorsJsonArray.length(); i++) {
                        JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                        String url = counselorJsonObject.optString("profile_pic");
                        String name = counselorJsonObject.optString("first_name")+" "+ counselorJsonObject.optString("last_name");
                        tv_name.setText(name);
                        Glide.with(activity).load(url).into(iv_avatar);
                        Log.e("name" ,"==>" +name);
                    }
                } else {
                    pb_loading.setVisibility(View.GONE);
                    Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            pb_loading.setVisibility(View.GONE);
            Toast.makeText(activity, VoleyErrorHelper.getMessage(error, activity), Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror", "error");
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Uid", Uid);
                Log.e("#line_status_request", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }



    private void GetOrderId() {

        String new_url = "https://api.razorpay.com/v1/orders";
        try
        {
            final JSONObject jsonBody = new JSONObject("{\"amount\":199900,\"currency\":\"INR\"}");
            Log.e("json query", jsonBody.toString());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, new_url, jsonBody,
                    response ->{
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            order_id = jsonObject.optString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startPayment();
                        Log.e("#orderid" , "msg-->" +response.toString());
                    },
                    error ->
                            Log.e("json error22", "msg " +error.toString())
            )
            {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Basic cnpwX3Rlc3RfWmRMV0hxTlVlZlFKVjY6Q01zT1NUMmhwa0NTbEFnWFBPa01sc1h4");
                    return headers;
                }
            };
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "careerguide.com");
            options.put("description", "Career Guidance Plan");
            options.put("image", "https://cdn.razorpay.com/logos/C4V67hNqJ8bXgx_medium.png");
            options.put("currency", "INR");
            options.put("amount", "199900");
            options.put("order_id", order_id);
//            JSONObject preFill = new JSONObject();
//            preFill.put("email", Utility.getUserEmail(this));
//            preFill.put("contact", Utility.getUserMobile(this));
//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("insidr result act","-->");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "payUhash", response -> {
                Log.e("payuhash_res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Toast.makeText(activity, VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
                Log.e("payuhash_error","error");
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    params.put("productInfo",productinfo);
                    params.put("amount",1999 + "");
                    params.put("firstName",Utility.getUserFirstName(activity).trim());
                    params.put("email",Utility.getUserEmail(activity));
                    params.put("user_id",Utility.getUserId(activity));
                    params.put("PaymentId",paymentData.getPaymentId());
                    params.put("OrderId",paymentData.getOrderId());
                    params.put("RazSignature",paymentData.getSignature());
                    params.put("udf1","");
                    params.put("udf2","");
                    params.put("udf3","");
                    params.put("udf4","");
                    params.put("udf5","");
                    Log.e("payuhash_req",params.toString());
                    return params;
                }
            };
            VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
            customSuccessAlert.showAlert("Your Booking has been scheduled \n " +
                    "You can Check your Bookings in Profile");
        } catch (Exception e) {
            Log.e("", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String desc, PaymentData paymentData) {
        try {
            Log.e("data3","" +paymentData +" "+ desc);
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("", "Exception in onPaymentSuccess", e);
        }
    }


    public void back_onClick(View view) {
        finish();
    }
}
