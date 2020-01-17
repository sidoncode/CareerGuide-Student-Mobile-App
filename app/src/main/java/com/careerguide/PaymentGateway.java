package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PaymentGateway extends AppCompatActivity {

    private Activity activity = this;

    private String key = "hRZ1gzXY"/*"rjQUPktU"*/ ;
    private String salt = "2Czbr3Kcb6"/*"e5iIg1jwi8"*/;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        //String txnid = (new Random().nextInt(2000000)) + "";
        double amount = 1;
        String productinfo = "product1";
        String firstname =  Utility.getUserFirstName(activity).trim();
        String email =  Utility.getUserEmail(activity);
        phone = Utility.getUserMobile(activity);
        getHash(amount,productinfo,firstname,email);

        /*String hashSequence = key + "|" + txnid + "|" + amount + "|" + productinfo + "|" + firstname + "|" + email + "|||||||||||" + salt;
        String serverCalculatedHash = hashCal("SHA-512", hashSequence);
        Log.e("hash",serverCalculatedHash);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result Code is -1 send from Payumoney activity
//        Log.e("PayUMoney", "request code " + requestCode + " resultcode " + resultCode);
//        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null)
//        {
//            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
//            if (transactionResponse != null && transactionResponse.getPayuResponse() != null)
//            {
//                if(transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL))
//                {
//                    Log.e("PayUMoney", "Success");
//                    //Success Transaction
//                } else{
//                    Log.e("PayUMoney", "Failure");
//                        //Failure Transaction
//                }
//                    // Response from Payumoney
//                String payuResponse = transactionResponse.getPayuResponse();
//                    // Response from SURl and FURL
//                String merchantResponse = transactionResponse.getTransactionDetails();
//            }
//            /*else if (resultModel != null && resultModel.getError() != null)
//            {
//                Log.d("PayUMoney", "Error response : " + resultModel.getError().getTransactionResponse());
//            }*/
//            else
//            {
//                Log.d("PayUMoney", "Both objects are null!");
//            }
//        }
    }

    public static String hashCal(String type, String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }

    public void getHash(final double amount, final String productinfo, final String firstname, final String email)
    {
//        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(activity);
//        progressDialog.show();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "payUhash", new Response.Listener<String>()
//        {
//            @Override
//            public void onResponse(String response)
//            {
//                progressDialog.dismiss();
//                Log.e("payuhash_res", response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean status = jsonObject.optBoolean("status", false);
//                    String msg = jsonObject.optString("msg");
//                    if (status) {
//                        String txnId = jsonObject.optString("txnId");
//                        String hash = jsonObject.optString("hash");
//                        initiatePayment(amount,productinfo,firstname,email,txnId,hash);
//                    } else {
//                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener()
//        {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity),Toast.LENGTH_LONG).show();
//                Log.e("payuhash_error","error");
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                HashMap<String,String> params = new HashMap<>();
//                params.put("productInfo",productinfo);
//                params.put("amount",amount + "");
//                params.put("firstName",firstname);
//                params.put("email",email);
//                params.put("user_id",Utility.getUserId(activity));
//                params.put("udf1","");
//                params.put("udf2","");
//                params.put("udf3","");
//                params.put("udf4","");
//                params.put("udf5","");
//                Log.e("payuhash_req",params.toString());
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    private void initiatePayment(double amount, String productinfo, String firstname, String email, String txnId, String hash) {
//        PayUmoneySdkInitializer.PaymentParam.Builder builder = new
//                PayUmoneySdkInitializer.PaymentParam.Builder();
//        //txnId = (new Random().nextInt(200000)) + "";
//        builder.setAmount(amount)                          // Payment amount
//                .setTxnId(txnId)                                             // Transaction ID
//                .setPhone(phone)                                           // User Phone number
//                .setProductName(productinfo)                   // Product Name or description
//                .setFirstName(firstname)                              // User First name
//                .setEmail(email)                                            // User Email ID
//                .setsUrl("http://app.careerguide.com/api/main/payUsuccess")                    // Success URL (surl)
//                .setfUrl("http://app.careerguide.com/api/main/payUcancel")                     //Failure URL (furl)
//                /*.setsUrl("www.google.com")                    // Success URL (surl)
//                .setfUrl("www.yahoo.com")*/
//                .setUdf1("")
//                .setUdf2("")
//                .setUdf3("")
//                .setUdf4("")
//                .setUdf5("")
//                .setUdf6("")
//                .setUdf7("")
//                .setUdf8("")
//                .setUdf9("")
//                .setUdf10("")
//                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
//                .setKey(key)                        // Merchant key
//                .setMerchantId("6035945");
//
//        /*String hashSequence = key + "|" + txnId + "|" + amount + "|" + productinfo + "|" + firstname + "|" + email + "|||||||||||" + salt;
//        String serverCalculatedHash = hashCal("SHA-512", hashSequence);*/
//
//        //declare paymentParam object
//        PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
//        //set the hash
//        paymentParam.setMerchantHash(hash);
//
//        PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, this,  R.style.AppTheme_NoActionBar, false);
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
