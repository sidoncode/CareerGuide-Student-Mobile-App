package com.careerguide.Book_One_To_One.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.careerguide.Book_One_To_One.adapter.OneToOneBookingsAdapter;
import com.careerguide.Book_One_To_One.model.OneToOneBookingsModel;
import com.careerguide.R;
import com.careerguide.Utility;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyBookingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBookingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    OneToOneBookingsAdapter oneToOneBookingsAdapter;

    public MyBookingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyBookingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyBookingsFragment newInstance(String param1, String param2) {
        MyBookingsFragment fragment = new MyBookingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_my_bookings, container, false);

        RecyclerView recycler_view_bookings=view.findViewById(R.id.recycler_view_bookings);

        oneToOneBookingsAdapter=new OneToOneBookingsAdapter(recycler_view_bookings);


        Log.i("ssss","aaa");

        new TaskFetchBookings().execute();




        return view;
    }


    private class TaskFetchBookings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {


            try {


                JSONObject jsonBody = new JSONObject();


                jsonBody.put("fk_student_id", Utility.getUserId(getActivity()));

                Log.i("jsonbodyy",jsonBody+"");

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, /*"https://app.careerguide.com/api/main/bookOneToOne"*/Utility.albinoServerIp+"/FoodRunner-API/foodrunner/v2/careerguide/fetch_bookings_for_student.php",jsonBody, response -> {


                    try {
                        JSONObject jsonObject = new JSONObject(response+"");
                        Log.i("response->",jsonObject+"");
                        boolean status = jsonObject.optBoolean("status", false);
                        if (status) {

                            JSONArray jsonArray=response.getJSONArray("data");

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject eachBooking=jsonArray.getJSONObject(i);
                                oneToOneBookingsAdapter.addBooking(new OneToOneBookingsModel(eachBooking.getString("booking_id"),eachBooking.getString("sessionHeld"),eachBooking.getString("counselorName"),eachBooking.getString("studentName"),eachBooking.getString("dateBooked"),eachBooking.getString("timeSlot"),eachBooking.getString("channelName"),eachBooking.getString("category"),eachBooking.getString("profile_pic")));
                            }

                            getActivity().runOnUiThread(()->{
                                oneToOneBookingsAdapter.notifyDataIsChanged();
                            });

                        } else {
                            Log.i("sssss","asasas");
                            Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                        //pb_loading.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    // pb_loading.setVisibility(View.GONE);
                    Toast.makeText(getContext(), VoleyErrorHelper.getMessage(error+"", getContext()), Toast.LENGTH_LONG).show();
                    Log.e("all_coun_rerror", "error");
                    error.printStackTrace();
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Basic ZTg1YWQyZjg3Mzc0NDc5ZWE5ZjZhMTE0MmY5NTRjZjc6YjdiZTUxM2Q4ZDI0NGFiNWFlYWU0ZWQxNWYwZDIyNWM=");
                        return headers;
                    }
                };
                VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }


    }
}