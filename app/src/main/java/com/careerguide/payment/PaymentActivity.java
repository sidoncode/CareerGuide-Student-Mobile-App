package com.careerguide.payment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.Book_Appoinment.CustomSuccessAlert;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends Activity implements PaymentResultWithDataListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private String order_id;
    Activity activity = this;
    String productinfo = "product1";
    CustomSuccessAlert customSuccessAlert = new CustomSuccessAlert(this , this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        Checkout.preload(getApplicationContext());
        GetOrderId();
        // Payment button created by you in XML layout
//        Button button =  findViewById(R.id.btn_pay);
//        button.setOnClickListener(v -> startPayment());
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


    @Override
    public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "payUhash", response -> {
                Log.e("payuhash_res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
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
            Toast.makeText(this, "Payment Successful: " + paymentData +" "+ razorpayPaymentId , Toast.LENGTH_SHORT).show();
            Log.e("data","" +paymentData.getPaymentId());
            Log.e("data2","" +paymentData.getOrderId());
            Log.e("data3","" +paymentData.getSignature());
            customSuccessAlert.showAlert("Your Booking has been scheduled \n " +
                    "You can manage your booking in Booking Tab");
            //finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String desc, PaymentData paymentData) {
        try {
            Log.e("data3","" +paymentData +" "+ desc);
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }
}