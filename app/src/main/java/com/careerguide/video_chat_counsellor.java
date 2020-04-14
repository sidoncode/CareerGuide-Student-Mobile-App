package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class video_chat_counsellor extends AppCompatActivity {
    Activity activity = this;
    private ArrayList<Counsellor> counsellors = new ArrayList<>();
    video_chat_counsellor.CounselorAdapter counselorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_counsellor);
        ListView counsellorListView = findViewById(R.id.counsellorListView);
        counselorAdapter = new CounselorAdapter();
        counsellorListView.setAdapter(counselorAdapter);

        Button payment = findViewById(R.id.payment);
        payment.setOnClickListener(v -> {
            startActivity(new Intent(activity , PlanActivity.class));
        });

        getPaymentDetail();
    }


    private void getPaymentDetail(){
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "GetPayment", response -> {
            progressDialog.dismiss();
            Log.e("all_payment_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status)
                {
                    JSONArray Payments = jsonObject.optJSONArray("payments");
                    Log.e("Payments","-->" +Payments);
                    JSONObject PaymentsJsonObject = Payments.optJSONObject(0);
                    String validate_till = PaymentsJsonObject.optString("validate_till");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    Date date_till= formatter.parse(validate_till);
                    Date currentDate= formatter.parse(formatter.format(date));
                    RelativeLayout paymentArea = findViewById(R.id.paymentArea);
                    if (date_till.before(currentDate)) {
                        Log.e("expired","-->" );
                        paymentArea.setVisibility(View.VISIBLE);
                    }
                    if (date_till.after(currentDate)) {
                        paymentArea.setVisibility(View.GONE);
                        Log.e("valid","-->" );
                    }
                    getCounsellors();
                } else {
                    Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }, error -> {
            progressDialog.dismiss();
            //Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity)),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                Log.e("all_coun_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }


    private void getCounsellors() {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(activity);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "all_available_counsellors", response -> {
                progressDialog.dismiss();
                Log.e("all_coun_res", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {
                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                        for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                        {
                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                            String id = counselorJsonObject.optString("co_id");
                            String firstName = counselorJsonObject.optString("first_name");
                            String lastName = counselorJsonObject.optString("last_name");
                            String picUrl = counselorJsonObject.optString("profile_pic");
                            String Videocall_channel_name = counselorJsonObject.optString("videocall_channel_name");
                            counsellors.add(new Counsellor(id,firstName,lastName,picUrl,Videocall_channel_name,"",4.5f,new ArrayList<String>()));
                        }
                        counselorAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }, error -> {
            progressDialog.dismiss();
                //Toast.makeText(activity,VoleyErrorHelper.getMessage(error,activity)),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(activity));
                Log.e("all_coun_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest);
    }

    public void backpress(View view) {
        finish();
    }

    private class CounselorAdapter extends BaseAdapter
    {
        LayoutInflater layoutInflater;

        CounselorAdapter()
        {
            layoutInflater = activity.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return counsellors.size();
        }

        @Override
        public Object getItem(int position) {
            return counsellors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View counselorView = layoutInflater.inflate(R.layout.counselor_list_item,null);
            final Counsellor counsellor = counsellors.get(position);
            // Log.e("#position" , "position " +counsellor.getvideochannel());
            TextView name = counselorView.findViewById(R.id.name);
            TextView title = counselorView.findViewById(R.id.title);
            ImageView profilePic = counselorView.findViewById(R.id.profilePic);
//            RatingBar ratingBar = counselorView.findViewById(R.id.rat);
            name.setText(counsellor.getName());
            title.setText(counsellor.getTitle());
            //      ratingBar.setRating(counsellor.getRating());
            if (counsellor.picUrl != null && counsellor.picUrl.length()>0)
            {
                Glide.with(activity).load(counsellor.picUrl).into(profilePic);
            }
            counselorView/*.findViewById(R.id.chat)*/.setOnClickListener(v -> {
//                Intent intent = new Intent(activity, ChatActivity.class);
//                intent.putExtra("counsellor",counsellor);
//                startActivity(intent);
                Intent intent = new Intent(activity, VideoChatViewActivity.class);
                intent.putExtra("video_channel_name","");
                 startActivity(intent);
            });
            return counselorView;
        }
    }
}
