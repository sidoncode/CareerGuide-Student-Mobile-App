package com.careerguide;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CounsellorCornerFragment extends Fragment {

    private View fragmentView;
    private ArrayList<Counsellor> counsellors = new ArrayList<>();
    CounselorAdapter counselorAdapter;


    public CounsellorCornerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView =  inflater.inflate(R.layout.fragment_counsellor_corner, container, false);
        ListView counsellorListView = fragmentView.findViewById(R.id.counsellorListView);
        counselorAdapter = new CounselorAdapter();
        counsellorListView.setAdapter(counselorAdapter);
        getCounsellors();
        return fragmentView;
    }

    private void getCounsellors() {
        final ProgressDialogCustom progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "all_available_counsellors", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                if (isAdded()) {
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
                            Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (isAdded())
                    Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                Log.e("all_coun_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private class CounselorAdapter extends BaseAdapter
    {
        LayoutInflater layoutInflater;

        public CounselorAdapter()
        {
            layoutInflater = getActivity().getLayoutInflater();
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
                Glide.with(getActivity()).load(counsellor.picUrl).into(profilePic);
            }
            counselorView/*.findViewById(R.id.chat)*/.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), ChatActivity.class);
//                    intent.putExtra("counsellor",counsellor);
//                    startActivity(intent);
                    Intent intent = new Intent(getActivity(), VideoChatViewActivity.class);
                    //intent.putExtra("video_channel_name",counsellor.getvideochannel());
                    startActivity(intent);
                }
            });
            return counselorView;
        }
    }

}
