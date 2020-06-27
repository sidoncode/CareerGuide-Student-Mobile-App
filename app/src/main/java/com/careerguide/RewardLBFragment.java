package com.careerguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RewardLBFragment extends Fragment {
    private View view;
    TextView[] un=new TextView[6];
    TextView[] rp=new TextView[6];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reward_lb, container,false);

        un[0]=view.findViewById(R.id.lbun1);
        un[1]=view.findViewById(R.id.lbun2);
        un[2]=view.findViewById(R.id.lbun3);
        un[3]=view.findViewById(R.id.lbun4);
        un[4]=view.findViewById(R.id.lbun5);
        //un[5]=view.findViewById(R.id.lbun);

        rp[0]=view.findViewById(R.id.lbrp1);
        rp[1]=view.findViewById(R.id.lbrp2);
        rp[2]=view.findViewById(R.id.lbrp3);
        rp[3]=view.findViewById(R.id.lbrp4);
        rp[4]=view.findViewById(R.id.lbrp5);
        //rp[5]=view.findViewById(R.id.lbrp);

        //un[5].setText(Utility.get);
        StringRequest stringRequest1=new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "fetch_top_rewards", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("rewards_response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    if(jsonObject.optJSONArray("users").length()!=0)
                    {
                        JSONArray userJsonObject = jsonObject.optJSONArray("users");
                        JSONObject jbj1 = userJsonObject.optJSONObject(0);
                        JSONObject jbj2 = userJsonObject.optJSONObject(1);
                        JSONObject jbj3 = userJsonObject.optJSONObject(2);
                        JSONObject jbj4 = userJsonObject.optJSONObject(3);
                        JSONObject jbj5 = userJsonObject.optJSONObject(4);
                        String rew1=jbj1.optString("rewards_point");
                        String rew2=jbj2.optString("rewards_point");
                        String rew3=jbj3.optString("rewards_point");
                        String rew4=jbj4.optString("rewards_point");
                        String rew5=jbj5.optString("rewards_point");

                        String user1=jbj1.optString("name");
                        String user2=jbj2.optString("name");
                        String user3=jbj3.optString("name");
                        String user4=jbj4.optString("name");
                        String user5=jbj5.optString("name");

                        lb(rew1,rew2,rew3,rew4,rew5,user1,user2,user3,user4,user5);

                    }

                } catch (JSONException j) {

                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                Log.e("lb_error","error");

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                //params.put("userId", Utility.getUserId(activity));
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);
        Button refapp=view.findViewById(R.id.refapp);
        Button art=view.findViewById(R.id.shareart);
        //Button blog=view.findViewById(R.id.shareblog);
        refapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Refer_a_friend.class));
            }
        });
        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("RLB","art"));
            }
        });
        /*blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HomeActivity.class).putExtra("RLB","blog"));
            }
        });*/


        setUpCardView();
        return view;
    }

    public void lb(String rew1,String rew2,String rew3,String rew4,String rew5,String user1,String user2,String user3,String user4, String user5)
    {
        un[0].setText(user1);
        un[1].setText(user2);
        un[2].setText(user3);
        un[3].setText(user4);
        un[4].setText(user5);

        rp[0].setText(rew1);
        rp[1].setText(rew2);
        rp[2].setText(rew3);
        rp[3].setText(rew4);
        rp[4].setText(rew5);
    }

    private void setUpCardView() {

        final View rewardDetails = view.findViewById(R.id.rewardsDetails);
        final View LBDetails=view.findViewById(R.id.lbdetails);
        final View redeemDetails=view.findViewById(R.id.redeemLL);

        View rewardRL = view.findViewById(R.id.rewardsRelativeL);
        View lbRL = view.findViewById(R.id.LBRelativeL);
        View redeemRL=view.findViewById(R.id.redeemRelativeL);

        final ImageView arrowRewards = view.findViewById(R.id.arrowRewards);
        final ImageView arrowLB = view.findViewById(R.id.arrowLB);
        final ImageView arrowRedeem = view.findViewById(R.id.arrowredeem);


        rewardRL.setOnClickListener(v -> {
            if (rewardDetails.getVisibility() == View.VISIBLE) {
                rewardDetails.setVisibility(View.GONE);
                arrowRewards.setImageResource(R.drawable.ic_expand);
            } else {
                rewardDetails.setVisibility(View.VISIBLE);
                arrowRewards.setImageResource(R.drawable.ic_collapse);
            }
            redeemDetails.setVisibility(View.GONE);
            LBDetails.setVisibility(View.GONE);

            arrowLB.setImageResource(R.drawable.ic_expand);
            arrowRedeem.setImageResource(R.drawable.ic_expand);
        });
        redeemRL.setOnClickListener(v -> {
            if (redeemDetails.getVisibility() == View.VISIBLE) {
                redeemDetails.setVisibility(View.GONE);
                arrowRedeem.setImageResource(R.drawable.ic_expand);
            } else {
                redeemDetails.setVisibility(View.VISIBLE);
                arrowRedeem.setImageResource(R.drawable.ic_collapse);
            }
            rewardDetails.setVisibility(View.GONE);
            LBDetails.setVisibility(View.GONE);

            arrowLB.setImageResource(R.drawable.ic_expand);
            arrowRewards.setImageResource(R.drawable.ic_expand);
        });
        lbRL.setOnClickListener(v -> {
            if (LBDetails.getVisibility() == View.VISIBLE) {
                LBDetails.setVisibility(View.GONE);
                arrowLB.setImageResource(R.drawable.ic_expand);
            } else {
                LBDetails.setVisibility(View.VISIBLE);
                arrowLB.setImageResource(R.drawable.ic_collapse);
            }
            rewardDetails.setVisibility(View.GONE);
            redeemDetails.setVisibility(View.GONE);

            arrowRedeem.setImageResource(R.drawable.ic_expand);
            arrowRewards.setImageResource(R.drawable.ic_expand);
        });

        TextView rewtxt = view.findViewById(R.id.rewtxt);
        TextView reftxt = view.findViewById(R.id.reftxt);

        rewtxt.setText(Utility.getRewardPoints(getActivity()));
        reftxt.setText(Utility.getNumReferrals(getActivity()));

        TextView fitband=view.findViewById(R.id.fitband);
        TextView blsp=view.findViewById(R.id.blsp);
        TextView tab=view.findViewById(R.id.tab);
        TextView mi=view.findViewById(R.id.mi);
        int refno=Integer.parseInt(Utility.getNumReferrals(getActivity()));
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
    }
}
