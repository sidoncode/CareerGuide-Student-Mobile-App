package com.careerguide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.adapters.AllTopicsItemAdapter;
import com.careerguide.adapters.CounsellorAdapter;
import com.careerguide.models.Counsellor;
import com.careerguide.models.topics_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private RecyclerView recycler_topic;
    private List<topics_model> topiclist;
    private ArrayList<topics_model> topics = new ArrayList<>();
    private int topicSize;
    private AllTopicsItemAdapter topic_adapter;

    private RecyclerView recycler_counsellor;
    private List<Counsellor> counsellorlist;
    private ArrayList<Counsellor> Counsellors_profile = new ArrayList<>();
    private CounsellorAdapter counsellor_adapter;
    private int Counsellors_profile_size;


    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;

    private List<Album> albumList;
    private RadioButton class1RadioButton, class10RadioButton, class12RadioButton, graduateRadioButton, postGraduateRadioButton;
    private String eduLevel = "";
    private static boolean eduFetched = false;
    private ArrayList<live_counsellor_session> counsellors = new ArrayList<>();


    private OnFragmentInteractionListener mListener;
    private int size;
    LinearLayoutManager mLayoutManager;
    LinearLayout ideal_career;
    NestedScrollView nsv_items;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        final View view =  inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(getContext(), albumList);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);


        recycler_topic = (RecyclerView) view.findViewById(R.id.recycler_topic);
        topiclist = new ArrayList<>();
        topic_adapter = new AllTopicsItemAdapter(getContext(), topiclist);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_topic.setLayoutManager(mLayoutManager);
        recycler_topic.setAdapter(topic_adapter);


        recycler_counsellor = (RecyclerView) view.findViewById(R.id.recycler_counsellor);
        counsellorlist = new ArrayList<>();
        counsellor_adapter = new CounsellorAdapter(getContext(), counsellorlist);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_counsellor.setLayoutManager(mLayoutManager);
        recycler_counsellor.setAdapter(counsellor_adapter);


        getLiveSession();

        view.findViewById(R.id.tests).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),PsychometricTestsActivity.class));
            //startActivity(new Intent(getActivity(),PaymentGateway.class));
        });
        view.findViewById(R.id.tv_chat).setOnClickListener(v -> {
            //ZohoSalesIQ.Chat.open();

        });


//        view.findViewById(R.id.counsellorSignUp).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    MenuItem menuItem = ((HomeActivity) getActivity()).nvDrawer.getMenu().getItem(3);
//                    ((HomeActivity) getActivity()).selectDrawerItem(menuItem);
//                }
//                catch (Exception ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//        });

        /*VideoView videoView = view.findViewById(R.id.vidView);
        videoView.setVideoURI(Uri.parse(""));
        videoView.start();*/

//        class1RadioButton = view.findViewById(R.id.class1);
//        class10RadioButton = view.findViewById(R.id.class10);
//        class12RadioButton = view.findViewById(R.id.class12);
//        graduateRadioButton = view.findViewById(R.id.graduate);
//        postGraduateRadioButton = view.findViewById(R.id.postGraduate);
//
//        Log.e("onCreate","OnCreate " + eduFetched);
//        if (!eduFetched) {
//            Log.e("onCreate","boolTest " + eduFetched);
//            fetchEducationLevel();
//            initGroup();
//        }
//        view.findViewById(R.id.educationalLL).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout educationalDetails = view.findViewById(R.id.educationDetails);
//                if (educationalDetails.getVisibility()==View.GONE)
//                {
//                    educationalDetails.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    educationalDetails.setVisibility(View.GONE);
//                }
//            }
//        });



        return view;
    }

    private void prepareAlbums() {
        for(int i = 0; i<size;i++){
            String imgurl = "";
            Log.e("url in exo" , "-->" +counsellors.get(i).getVideourl());
            Album a = new Album(counsellors.get(i).getFullName(), counsellors.get(i).title, counsellors.get(i).getImgurl() , counsellors.get(i).getVideourl() , counsellors , counsellors.get(i).getId());
            albumList.add(a);

        }

        for (int i =0 ; i<topicSize ; i++){
            Log.e("name in prepare" , "-->" +topics.get(i).getName());
            topics_model topicModel = new topics_model(topics.get(i).getUid() , topics.get(i).getName() , 27);
            topiclist.add(topicModel);
        }

        for (int i =0 ; i<Counsellors_profile_size ; i++){
            Log.e("name in prepare" , "-->" +Counsellors_profile.get(i).getUsername());
            Counsellor counsellor_model = new Counsellor(Counsellors_profile.get(i).getUid() , Counsellors_profile.get(i).getUsername() , Counsellors_profile.get(i).getFirst_name(),Counsellors_profile.get(i).getLast_name(),Counsellors_profile.get(i).getAvatar(),23);
            counsellorlist.add(counsellor_model);
        }
        adapter.notifyDataSetChanged();
        topic_adapter.notifyDataSetChanged();
        counsellor_adapter.notifyDataSetChanged();
    }

    private void initGroup() {
        class1RadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    updateEducation("Class 1 - 9");
                }
            }
        });

        class10RadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    updateEducation("Class 10");
                }
            }
        });

        class12RadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    updateEducation("Class 12");
                }
            }
        });

        graduateRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    updateEducation("Graduate");
                }
            }
        });

        postGraduateRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    updateEducation("Post Graduate");
                }
            }
        });
    }

    public void updateEducation(final String education) {
        Log.e("onCreate","updateEdu " + eduFetched);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "profile_update", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                if (isAdded()) {
                    progressDialog.dismiss();
                    Log.e("update_pro_response", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.optBoolean("status", false);
                        String msg = jsonObject.optString("msg");
                        if (status) {
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
                Log.e("update_pro_error","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                params.put("education_level",education);
                Log.e("request",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void fetchEducationLevel()
    {
        Log.e("onCreate","fetchEdu " + eduFetched);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "get_education_level", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                Log.e("get_edu_res",response);
                try
                {
                    eduFetched = true;
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status",false);
                    if(status)
                    {
                        eduLevel = jsonObject.optString("education_level");
                        switch (eduLevel)
                        {
                            case "Class 1 - 9":
                                class1RadioButton.setOnCheckedChangeListener(null);
                                class1RadioButton.setChecked(true);
                                break;
                            case "Class 10":
                                class10RadioButton.setOnCheckedChangeListener(null);
                                class10RadioButton.setChecked(true);
                                break;
                            case "Class 12":
                                class12RadioButton.setOnCheckedChangeListener(null);
                                class12RadioButton.setChecked(true);
                                break;
                            case "Graduate":
                                graduateRadioButton.setOnCheckedChangeListener(null);
                                graduateRadioButton.setChecked(true);
                                break;
                            case "Post Graduate":
                                postGraduateRadioButton.setOnCheckedChangeListener(null);
                                postGraduateRadioButton.setChecked(true);
                                break;
                        }
                        initGroup();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Something went wrong",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isAdded()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), VoleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
                }
                Log.e("get_edu_error","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("user_id",Utility.getUserId(getActivity()));
                Log.e("get_edu_req",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void getLiveSession() {
        final ProgressDialog progressDialog = new ProgressDialogCustom(getActivity());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Facebook_Live_video", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.dismiss();
                if (isAdded()) {
                    Log.e("all_coun_res", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.optBoolean("status", false);
                        if (status)
                        {
                            JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                            Log.e("lengthname--> " , "==> " +counsellorsJsonArray.length() );
                            for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                            {
                                JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                                String email = counselorJsonObject.optString("email");
                                String name = counselorJsonObject.optString("Name");
                                Log.e("name--> " , "==> " +name );
                                String img_url = counselorJsonObject.optString("img_url");
                                String title = counselorJsonObject.optString("title");
                                String video_url = counselorJsonObject.optString("video_url");
                                counsellors.add(new live_counsellor_session(email,name,img_url,video_url,title,""));
                            }
                            size = counsellors.size();
                            gettopic();
                            Log.e("size " , "==> " +counsellors );
                            //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                        }


                        else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            private void gettopic(){
                final ProgressDialog progressDialog = new ProgressDialogCustom(getActivity());
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Fetch_Topics", new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        progressDialog.dismiss();
                        if (isAdded()) {
                            Log.e("all_coun_res", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean status = jsonObject.optBoolean("status", false);
                                if (status)
                                {
                                    JSONArray counsellorsJsonArray = jsonObject.optJSONArray("topics");
                                    Log.e("lengthname--> " , "==> " +counsellorsJsonArray.length() );
                                    Log.e("TOPICS--> " , "==> " +counsellorsJsonArray );
                                    for (int i = 0; counsellorsJsonArray != null && i<counsellorsJsonArray.length(); i++)
                                    {
                                        JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                                        String name = counselorJsonObject.optString("name");
                                        Log.e("name--> " , "==> " +name );
                                        String uid = counselorJsonObject.optString("uid");
                                        String parent_uid = counselorJsonObject.optString("parent_uid");
                                        String parent_name = counselorJsonObject.optString("parent_name");
                                        topics.add(new topics_model(uid,name,27));
                                    }
                                    topicSize = topics.size();
                                    getcounsellor();
                                    Log.e("size " , "==> " +topics );
                                    //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    private void getcounsellor() {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "category_counsellors", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("all_coun_res", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean status = jsonObject.optBoolean("status", false);
                                    if (status) {
                                        JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");
                                        for (int i = 0; counsellorsJsonArray != null && i < counsellorsJsonArray.length(); i++) {
                                            JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                                            String id = counselorJsonObject.optString("co_id");
                                            String firstName = counselorJsonObject.optString("first_name");
                                            String lastName = counselorJsonObject.optString("last_name");
                                            String picUrl = counselorJsonObject.optString("profile_pic");
                                            String email = counselorJsonObject.optString("email");
                                            Counsellors_profile.add(new Counsellor(id, email , firstName, lastName, picUrl,27));
                                        }
                                        Counsellors_profile_size = Counsellors_profile.size();
                                        prepareAlbums();
                                        Log.e("size ", "==> " + size);
                                        Log.e("size1 ", "==> " + counsellors.get(0).getPicUrl());
                                    } else {
                                        Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                    }
                                    progressDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), VoleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
                                Log.e("all_coun_rerror", "error");
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> params = new HashMap<>();
                                //params.put("user_id", Utility.getUserId(getActivity()));
                                Log.e("all_coun_req", params.toString());
                                return params;
                            }
                        };
                        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
                    }


                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
//                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                        Log.e("all_coun_rerror","error");
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("email" ,Utility.getUserEmail(getActivity()));
                        Log.e("#line_status_request",params.toString());
                        return params;
                    }
                };

                VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }


        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
                Log.e("all_coun_rerror","error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("email" ,Utility.getUserEmail(getActivity()));
                Log.e("#line_status_request",params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }





}
