package com.careerguide;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.careerguide.Book_Appoinment.BookCounsellor;
import com.careerguide.activity.SeeAllActivity;
import com.careerguide.adapters.AllTopicsItemAdapter;
import com.careerguide.adapters.CounsellorAdapter;
import com.careerguide.blog.DataMembers;
import com.careerguide.blog.adapter.CatDetailAdapter;
import com.careerguide.blog.model.Categories;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.careerguide.models.Counsellor;
import com.careerguide.models.PlayList;
import com.careerguide.models.topics_model;
import com.careerguide.youtubeVideo.CommonEducationAdapter;
import com.careerguide.youtubeVideo.CommonEducationModel;
import com.careerguide.youtubeVideo.Videos;
import com.careerguide.youtubeVideo.YT_recycler_adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;


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
    private CompositeDisposable disposable;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DrawerLayout mDrawer;
    private List<topics_model> topiclist;
    private ArrayList<topics_model> topics = new ArrayList<>();
    private int topicSize;
    private AllTopicsItemAdapter topic_adapter;
    private List<Counsellor> counsellorlist;
    private CounsellorAdapter counsellor_adapter;
    private RecyclerView blogsRecyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private RadioButton class1RadioButton, class10RadioButton, class12RadioButton, graduateRadioButton, postGraduateRadioButton;
    private static boolean eduFetched = false;
    private ArrayList<live_counsellor_session> counsellors = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;
    private int size;
    private LinearLayoutManager mLayoutManager;
    ImageView iv_banner;

    private CommonEducationAdapter allPastLiveSessionAdapter;
    private List<CommonEducationModel> allPastLiveSessionList = new ArrayList<>();
    private RecyclerView recyclerViewPastLiveCounsellor;

    private List<CategoryDetails> categoryDetails;
    private CatDetailAdapter blogAdapter;
    TextView blogsTv;

    String subCat ="";


    RecyclerView playList1Recv,playList2Recv,playList3Recv;
    YT_recycler_adapter adapterP1,adapterP2,adapterP3;
    List<Videos> p1List,p2List,p3List;
    TextView p1Title,p2Title,p3Title;
    TextView p1SeeAll,p2SeeAll,p3SeeAll;


    View shimmer_p1,shimmer_p2,shimmer_p3;

    private List<PlayList> playList;
    //private String browserKey = "AIzaSyC2VcqdBaKakTd7YLn4B9t3dxWat9UHze4";//rachit api key for youtube
    private String browserKey = Utility.browserKey;


    private CGPlayListViewModel viewModel;


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
        setHasOptionsMenu(true);
        progressDialog = new ProgressDialog(getActivity());
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

        mDrawer = getActivity().findViewById(R.id.drawer_layout);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
     /*   toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });*/
        subCat = toolbar.getSubtitle().toString();

        Log.e("inside","-->edu" +Utility.getUserEducation(getActivity()));
        iv_banner = view.findViewById(R.id.iv_banner);
        if(Utility.getUserEducation(getActivity()).contentEquals("Class 9th")){
            Glide.with(getContext()).load("https://ik.imagekit.io/careerguide/Banner-Class9th___5_uLynqGh4v.png").into(iv_banner);
            toolbar.setTitle("Class 9th");
        }

        if(Utility.getUserEducation(getActivity()).contentEquals("Class 10th")){
            Glide.with(getContext()).load("https://ik.imagekit.io/careerguide/Banner-Class10th___8_hHDBZxDZ2.png").into(iv_banner);
            toolbar.setTitle("Class 10th");

        }
        if(Utility.getUserEducation(getActivity()).contentEquals("Class 11th")){
            Glide.with(getContext()).load("https://ik.imagekit.io/careerguide/Banner-Class11th___5_jcQGsViNJr.png").into(iv_banner);
            toolbar.setTitle("Class 11th");
        }

        if(Utility.getUserEducation(getActivity()).contentEquals("Class 12th")){
            Glide.with(getContext()).load("https://ik.imagekit.io/careerguide/Banner_12TH___3_MkTzKle5v.png").into(iv_banner);
            toolbar.setTitle("Class 12th");
        }

        if(Utility.getUserEducation(getActivity()).contentEquals("Graduates")){
            Glide.with(getContext()).load("https://ik.imagekit.io/careerguide/Banner-Working___2_bs5hJSTVAr.png").into(iv_banner);
            toolbar.setTitle("Graduates");
        }
        if(Utility.getUserEducation(getActivity()).contentEquals("Post Graduates")){
            Glide.with(getContext()).load("https://ik.imagekit.io/careerguide/Banner-Masters___1_HdkuGWnJST.png").into(iv_banner);
            toolbar.setTitle("Post Graduates");
        }
        if(Utility.getUserEducation(getActivity()).contentEquals( "Working Professional")){
            Glide.with(getContext()).load("https://ik.imagekit.io/careerguide/Banner-Working___3_iw4be1oq1.png").into(iv_banner);
            toolbar.setTitle("Working Professional");
        }

        RecyclerView recyclerViewPastLiveCounsellor = view.findViewById(R.id.recycler_view);
        allPastLiveSessionList = new ArrayList<CommonEducationModel>();
        allPastLiveSessionAdapter = new CommonEducationAdapter( allPastLiveSessionList,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPastLiveCounsellor.setLayoutManager(mLayoutManager);
        recyclerViewPastLiveCounsellor.setAdapter(allPastLiveSessionAdapter);


        /*RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(getContext(), albumList);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);*/


        RecyclerView recycler_topic = view.findViewById(R.id.recycler_topic);
        topiclist = new ArrayList<>();
        topic_adapter = new AllTopicsItemAdapter(getContext(), topiclist);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_topic.setLayoutManager(mLayoutManager);
        recycler_topic.setAdapter(topic_adapter);


        RecyclerView recycler_counsellor = view.findViewById(R.id.recycler_counsellor);
        counsellorlist = new ArrayList<>();
        counsellor_adapter = new CounsellorAdapter(getContext(), counsellorlist);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_counsellor.setLayoutManager(mLayoutManager);
        recycler_counsellor.setAdapter(counsellor_adapter);



        categoryDetails = new ArrayList<>();
        disposable = new CompositeDisposable();

        blogsRecyclerView = view.findViewById(R.id.recycler_blogs);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        blogsRecyclerView.setLayoutManager(mLayoutManager);
        blogAdapter = new CatDetailAdapter(getActivity(),categoryDetails);
        blogsRecyclerView.setAdapter(blogAdapter);


        playList1Recv = view.findViewById(R.id.p1_recycler_view);
        playList2Recv = view.findViewById(R.id.p2_recycler_view);
        playList3Recv = view.findViewById(R.id.p3_recycler_view);

        p1Title = view.findViewById(R.id.p1_title);
        p2Title = view.findViewById(R.id.p2_title);
        p3Title = view.findViewById(R.id.p3_title);

        p1SeeAll = view.findViewById(R.id.p1_see_all);
        p2SeeAll = view.findViewById(R.id.p2_see_all);
        p3SeeAll = view.findViewById(R.id.p3_see_all);

        shimmer_p1 = view.findViewById(R.id.shimmer_view_p1);
        shimmer_p2 = view.findViewById(R.id.shimmer_view_p2);
        shimmer_p3 = view.findViewById(R.id.shimmer_view_p3);



        playList = new ArrayList<>();
        p1List = new ArrayList<>();
        p2List = new ArrayList<>();
        p3List = new ArrayList<>();

        adapterP1 = new YT_recycler_adapter(p1List,browserKey,getActivity(),0, Color.parseColor("#000000"));
        adapterP2 = new YT_recycler_adapter(p2List,browserKey,getActivity(),0, Color.parseColor("#000000"));
        adapterP3 = new YT_recycler_adapter(p3List,browserKey,getActivity(),0, Color.parseColor("#000000"));

        playList1Recv.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        playList1Recv.setAdapter(adapterP1);

        playList2Recv.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        playList2Recv.setAdapter(adapterP2);

        playList3Recv.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        playList3Recv.setAdapter(adapterP3);



        Log.e("LoadingStatus","JustStarted");
        //progressDialog.show();
        getLiveSession();
        gettopic();
        //getcounsellor();
        getBlogs();
        fetchPlayListIds();


        view.findViewById(R.id.tests).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),PsychometricTestsActivity.class));
            //startActivity(new Intent(getActivity(),PaymentGateway.class));
        });

        view.findViewById(R.id.see_all_tests).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(),PsychometricTestsActivity.class));
        });

        View.OnClickListener onClick = v -> {
            startActivity(new Intent(getActivity(), PlanActivity.class));
        };
        view.findViewById(R.id.tv_subscribe).setOnClickListener(onClick);

        view.findViewById(R.id.counsellor_see_all).setOnClickListener(v->{
            startActivity(new Intent(getActivity(), BookCounsellor.class));
        });

        view.findViewById(R.id.tv_see_all).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SeeAllActivity.class);
            intent.putExtra("mode",0);
            intent.putExtra("EDU_KEY","videos_"+Utility.getUserEducationUid(getActivity()));
            intent.putExtra("TITLE","Classes");
            startActivity(intent);
        });



        p1SeeAll.setOnClickListener(v -> {
            if(playList.size()>0 && p1List.size()>0) {
                Intent intent = new Intent(getActivity(), SeeAllActivity.class);
                intent.putExtra("mode", 1);
                intent.putExtra("KEY", playList.get(0).getId());
                intent.putExtra("TITLE", playList.get(0).getName());
                startActivity(intent);
            }
        });

        p2SeeAll.setOnClickListener(v -> {
            if(playList.size()>1 && p2List.size()>0) {
                Intent intent = new Intent(getActivity(), SeeAllActivity.class);
                intent.putExtra("mode", 1);
                intent.putExtra("KEY", playList.get(1).getId());
                intent.putExtra("TITLE", playList.get(1).getName());
                startActivity(intent);
            }
        });

        p3SeeAll.setOnClickListener(v -> {
            if(playList.size()>2 && p3List.size()>0) {
                Intent intent = new Intent(getActivity(), SeeAllActivity.class);
                intent.putExtra("mode", 1);
                intent.putExtra("KEY", playList.get(2).getId());
                intent.putExtra("TITLE", playList.get(2).getName());
                startActivity(intent);
            }
        });


        blogsTv = view.findViewById(R.id.blogs_title);


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(CGPlayListViewModel.class);

        viewModel.getCounsellorList().observe(getActivity(), counsellors -> {
            counsellorlist.clear();
            counsellorlist.addAll(counsellors);
            counsellor_adapter.notifyDataSetChanged();
        });
    }



    /*
    private void prepareAlbums() {
        for (int i =0 ; i<topicSize ; i++){
            Log.e("name in prepare" , "-->" +topics.get(i).getName());
            topics_model topicModel = new topics_model(topics.get(i).getUid() , topics.get(i).getName() , 27);
            topiclist.add(topicModel);
        }
        topic_adapter.notifyDataSetChanged();
        for (int i =0 ; i<Counsellors_profile_size ; i++){
            Log.e("name in prepare" , "-->" +Counsellors_profile.get(i).getUsername());
            Counsellor counsellor_model = new Counsellor(Counsellors_profile.get(i).getUid() , Counsellors_profile.get(i).getUsername() , Counsellors_profile.get(i).getFirst_name(),Counsellors_profile.get(i).getLast_name(),Counsellors_profile.get(i).getAvatar(),23);
            counsellorlist.add(counsellor_model);
        }
        counsellor_adapter.notifyDataSetChanged();
        for(int i = 0; i<size;i++){
            //Log.e("#profile" , "-->" +Counsellors_profile.get(i).getAvatar());
            Log.e("url in exo" , "-->" +counsellors.get(i).getVideourl());
            Album a = new Album(counsellors.get(i).getFullName(), counsellors.get(i).title, counsellors.get(i).getImgurl() , counsellors.get(i).getVideourl() , counsellors , counsellors.get(i).getId() , Utility.getUserEducation(getActivity()) , counsellors.get(i).getPicUrl());
            albumList.add(a);
        }
        adapter.notifyDataSetChanged();
        Log.e("LoadingStatus","Done");
        progressDialog.dismiss();
    }
*/
//I have shifted all the fucntionality in the respective response. and notify the datasetchanged there itself


    private void initGroup() {
        class1RadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                updateEducation("Class 1 - 9");
            }
        });

        class10RadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                updateEducation("Class 10");
            }
        });

        class12RadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                updateEducation("Class 12");
            }
        });

        graduateRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                updateEducation("Graduate");
            }
        });

        postGraduateRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
            {
                updateEducation("Post Graduate");
            }
        });
    }

    public void updateEducation(final String education) {
        Log.e("onCreate","updateEdu " + eduFetched);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        //progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "profile_update", response -> {
            if (isAdded()) {
                //progressDialog.dismiss();
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
        }, error -> {
            //progressDialog.dismiss();
            if (isAdded())
                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("update_pro_error","error");
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

//    private void fetchEducationLevel()
//    {
//        Log.e("onCreate","fetchEdu " + eduFetched);
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.show();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "get_education_level", new Response.Listener<String>()
//        {
//            @Override
//            public void onResponse(String response)
//            {
//                progressDialog.dismiss();
//                Log.e("get_edu_res",response);
//                try
//                {
//                    eduFetched = true;
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean status = jsonObject.optBoolean("status",false);
//                    if(status)
//                    {
//                        eduLevel = jsonObject.optString("education_level");
//                        switch (eduLevel)
//                        {
//                            case "Class 1 - 9":
//                                class1RadioButton.setOnCheckedChangeListener(null);
//                                class1RadioButton.setChecked(true);
//                                break;
//                            case "Class 10":
//                                class10RadioButton.setOnCheckedChangeListener(null);
//                                class10RadioButton.setChecked(true);
//                                break;
//                            case "Class 12":
//                                class12RadioButton.setOnCheckedChangeListener(null);
//                                class12RadioButton.setChecked(true);
//                                break;
//                            case "Graduate":
//                                graduateRadioButton.setOnCheckedChangeListener(null);
//                                graduateRadioButton.setChecked(true);
//                                break;
//                            case "Post Graduate":
//                                postGraduateRadioButton.setOnCheckedChangeListener(null);
//                                postGraduateRadioButton.setChecked(true);
//                                break;
//                        }
//                        initGroup();
//                    }
//                    else
//                    {
//                        Toast.makeText(getActivity(), "Something went wrong",Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener()
//        {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (isAdded()) {
//                    progressDialog.dismiss();
//                    Toast.makeText(getActivity(), VoleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
//                }
//                Log.e("get_edu_error","error");
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> params = new HashMap<>();
//                params.put("user_id",Utility.getUserId(getActivity()));
//                Log.e("get_edu_req",params.toString());
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
//    }

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
        //progressDialog.show();
        if(Utility.getUserEducationUid(getActivity())==null){

        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Facebook_Live_video", response -> {
            //progressDialog.dismiss();
            if (isAdded()) {
                Log.e("all_coun_res_video", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.optBoolean("status", false);
                    if (status)
                    {
                        JSONArray jsonArray = jsonObject.optJSONArray("counsellors");
                        Log.e("lengthname--> " , "==> " +jsonArray.length() );
                        for (int i = 0; jsonArray != null && i<jsonArray.length(); i++)
                        {
                            JSONObject JsonObject = jsonArray.optJSONObject(i);
                            String user_id = JsonObject.optString("user_id");
                            String email = JsonObject.optString("email");
                            String name = JsonObject.optString("Name");
                            String img_url = JsonObject.optString("img_url");
                            String title = JsonObject.optString("title");
                            String video_url = JsonObject.optString("video_url");
                            String video_views=JsonObject.optString("views");
                            String video_id = JsonObject.optString("id");
                            if(video_views.contains("null")){
                                video_views="1";
                            }
                            allPastLiveSessionList.add(new CommonEducationModel(user_id,email, name, img_url, video_url, title, "",video_views,video_id));
                        }

                        //gettopic();
                        allPastLiveSessionAdapter.notifyDataSetChanged();

                        Log.e("size " , "==> " +counsellors );
                        //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                    }


                    else {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            //progressDialog.dismiss();
//                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        })
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                params.put("uid" ,Utility.getUserEducationUid(getActivity()));
                params.put("email" ,Utility.getUserEmail(getActivity()));
                Log.e("#line_status_request",params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }






    private void gettopic() {
        final ProgressDialog progressDialog = new ProgressDialogCustom(getActivity());
        //progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "Fetch_Topics", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                if (isAdded()) {
                    Log.e("all_coun_res_topic", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.optBoolean("status", false);
                        if (status) {
                            JSONArray counsellorsJsonArray = jsonObject.optJSONArray("topics");
                            Log.e("lengthname--> ", "==> " + counsellorsJsonArray.length());
                            Log.e("TOPICS--> ", "==> " + counsellorsJsonArray);
                            for (int i = 0; counsellorsJsonArray != null && i < counsellorsJsonArray.length(); i++) {
                                JSONObject counselorJsonObject = counsellorsJsonArray.optJSONObject(i);
                                String name = counselorJsonObject.optString("name");
                                Log.e("name--> ", "==> " + name);
                                String uid = counselorJsonObject.optString("uid");
                                String parent_uid = counselorJsonObject.optString("parent_uid");
                                String parent_name = counselorJsonObject.optString("parent_name");
                                topics.add(new topics_model(uid, name, 27));
                            }
                            topicSize = topics.size();
                            //getcounsellor();

                            for (int i =0 ; i<topicSize ; i++){
                                Log.e("name in prepare" , "-->" +topics.get(i).getName());
                                topics_model topicModel = new topics_model(topics.get(i).getUid() , topics.get(i).getName() , 27);
                                topiclist.add(topicModel);
                            }
                            topic_adapter.notifyDataSetChanged();

                            Log.e("size ", "==> " + topics);
                            //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                        } else {
                            //progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong. topics", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }




        }, error -> {
            //progressDialog.dismiss();
//                Toast.makeText(getActivity(),VoleyErrorHelper.getMessage(error,getActivity()),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror", "error");
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                //  params.put("email" ,Utility.getUserEmail(getActivity()));
                params.put("user_education", Utility.getUserEducationUid(getActivity()));
                Log.e("#line_status_request", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }












    private void getBlogs(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/Blog_list",
                response -> {
                    Log.d("#BLOG",response);
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        JSONArray jsonArray = responseObject.getJSONArray("playlist");

                        if(jsonArray.length()<=0)
                        {
                            blogsTv.setVisibility(View.GONE);
                            blogsRecyclerView.setVisibility(View.GONE);
                        }else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject singleBlog = (JSONObject) jsonArray.get(i);
                                String blogId = singleBlog.getString("blog_id");
                                Log.d("#BLOG", blogId);
                                String sub_cat = singleBlog.getString("sub_cat");

                                disposable.add(Utils.get_api().get_specific_cat_detail(blogId, "8", "1")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                                            @Override
                                            public void onSuccess(List<CategoryDetails> cd) {
                                                try {
                                                    if (cd != null) {
                                                        for (CategoryDetails c : cd) {
                                                            c.setTitle(Utils.remove_tags(c.getTitle()));
                                                            c.setDesc(Utils.remove_tags(c.getDesc()));
                                                            //Log.e("dataset","-->" +c.getTitle());
                                                            Log.d("#BLOG", "ITEM: " + c.getTitle() + "------" + c.getPic_url());
                                                            categoryDetails.add(c);
                                                        }
                                                        //Log.e("dataset next","-->" );
                                                        blogAdapter.notifyDataSetChanged();
                                                    } else {
                                                        blogsTv.setVisibility(View.GONE);
                                                        blogsRecyclerView.setVisibility(View.GONE);
                                                    }

                                                } catch (Exception e) {
                                                    Log.e("#BLOG", "onError: " + e.toString());
                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {
//                                            loading = false;
//                                            Utils.hide_loader(id_progress);
//                                            id_sw.setRefreshing(false);
                                                Log.e("#BLOG", "onError: " + e.toString());
                                            }
                                        }));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("#BLOG","error: "+e.toString());
                    }
                },
                error -> Log.e("#BLOG","error"+error.toString()))
        {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();
                String category = Utility.getUserEducationUid(getActivity());
                params.put("category" ,category);

                if(category.equals("NINE") || category.equals("TEN"))
                    params.put("sub_cat", category);
                else
                    params.put("sub_cat" , subCat);

                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }



    void fetchPlayListIds()
    {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app.careerguide.com/api/main/youtube_playlist",
                    response -> {
                        JSONObject json;
                        try {
                            json = new JSONObject(response);
                            Log.d("#PLAYLIST", "response: "+response);
                            JSONArray jsonArray = json.getJSONArray("playlist");
                            for (int i=0; i< jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String playlist_name = jsonObject.getString("playlist_name");
                                String playlist_id = jsonObject.getString("playlist_id");
                                playList.add(new PlayList(playlist_id,playlist_name));
                                Log.e("#play","-->" +playlist_id);
                            }

                            if(playList.size()>0) {
                                p1Title.setText(playList.get(0).getName());
                                fetchPlayListData(1, playList.get(0).getId());

                                if (playList.size() > 1) {
                                    p2Title.setText(playList.get(1).getName());
                                    fetchPlayListData(2, playList.get(1).getId());

                                    if (playList.size() > 2) {
                                        p3Title.setText(playList.get(2).getName());
                                        fetchPlayListData(3, playList.get(2).getId());
                                    } else {
                                        p3Title.setVisibility(View.GONE);
                                        playList3Recv.setVisibility(View.GONE);
                                        p3SeeAll.setVisibility(View.GONE);
                                        shimmer_p3.setVisibility(View.GONE);
                                    }
                                } else {
                                    p2Title.setVisibility(View.GONE);
                                    playList2Recv.setVisibility(View.GONE);
                                    p3Title.setVisibility(View.GONE);
                                    playList3Recv.setVisibility(View.GONE);
                                    p2SeeAll.setVisibility(View.GONE);
                                    p3SeeAll.setVisibility(View.GONE);
                                    shimmer_p2.setVisibility(View.GONE);
                                    shimmer_p3.setVisibility(View.GONE);

                                }

                            }else {
                                p1Title.setVisibility(View.GONE);
                                playList1Recv.setVisibility(View.GONE);
                                p2Title.setVisibility(View.GONE);
                                playList2Recv.setVisibility(View.GONE);
                                p3Title.setVisibility(View.GONE);
                                playList3Recv.setVisibility(View.GONE);
                                p1SeeAll.setVisibility(View.GONE);
                                p2SeeAll.setVisibility(View.GONE);
                                p3SeeAll.setVisibility(View.GONE);
                                shimmer_p1.setVisibility(View.GONE);
                                shimmer_p2.setVisibility(View.GONE);
                                shimmer_p3.setVisibility(View.GONE);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("#PLAYLIST","error"+e.toString());
                        }

                    },
                    error -> Log.e("#PLAYLIST","error"))
            {
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String,String> params = new HashMap<>();
                    String category = Utility.getUserEducationUid(getActivity());
                    params.put("page" ,category);
                    if(category.equals("NINE") || category.equals("TEN"))
                        params.put("sub_cat", "TEN");
                    else
                        params.put("sub_cat" , subCat);
                    return params;
                }
            };
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        }catch(Exception e1)
        {
            e1.printStackTrace();
        }
    }


    private void fetchPlayListData(int mCase, String playlistId) {
        String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+playlistId+"&key=" + browserKey + "&maxResults=6";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                response -> {
                    //Log.d("#PLAYLIST", "fetchPlayListData: "+ response);

                    try {
                        //Log.d("#PLAYLIST", "fetchPlayListData: "+ response);
                        JSONObject json = new JSONObject(response);
                        JSONArray jsonArray = json.getJSONArray("items");
                        int jsonArrayLen = jsonArray.length();

                        for (int i = jsonArrayLen - 1; i >= 0; i--) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject video_json = jsonObject.getJSONObject("snippet").getJSONObject("resourceId");
                            String title = jsonObject.getJSONObject("snippet").getString("title");
                            String Desc = jsonObject.getJSONObject("snippet").getString("description");
                            String id = video_json.getString("videoId");
                            Log.e("inside", "video ID-->" + id);
                            String thumbUrl_two = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("high").getString("url");
                            Videos video = new Videos(title, thumbUrl_two, id, Desc);

                            switch (mCase)
                            {
                                case 1:
                                    p1List.add(video);
                                    break;
                                case 2:
                                    p2List.add(video);
                                    break;
                                case 3:
                                    p3List.add(video);
                                    break;
                            }

                        }
                        switch (mCase)
                        {
                            case 1:
                                if(p1List.size()>0) {
                                    p1SeeAll.setText("See all");
                                    adapterP1.notifyDataSetChanged();
                                    shimmer_p1.setVisibility(View.INVISIBLE);
                                }
                                else
                                {
                                    p1Title.setVisibility(View.GONE);
                                    playList1Recv.setVisibility(View.GONE);
                                    shimmer_p1.setVisibility(View.GONE);
                                }
                                break;

                            case 2:
                                if(p2List.size()>0) {
                                    p2SeeAll.setText("See all");
                                    adapterP2.notifyDataSetChanged();
                                    shimmer_p2.setVisibility(View.INVISIBLE);

                                }
                                else
                                {
                                    p2Title.setVisibility(View.GONE);
                                    playList2Recv.setVisibility(View.GONE);
                                    shimmer_p2.setVisibility(View.GONE);

                                }
                                break;

                            case 3:
                                if(p3List.size()>0) {
                                    p3SeeAll.setText("See all");
                                    adapterP3.notifyDataSetChanged();
                                    shimmer_p3.setVisibility(View.INVISIBLE);

                                }
                                else
                                {
                                    p3Title.setVisibility(View.GONE);
                                    playList3Recv.setVisibility(View.GONE);
                                    shimmer_p3.setVisibility(View.GONE);

                                }
                                break;
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                },error -> {
            Log.d("#PLAYLIST", "error: "+ error.toString());

        });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}







/*
private void getcounsellor() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRIVATE_SERVER + "category_counsellors", response -> {
            Log.e("all_coun_res_counsellor", response);
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
                        Counsellors_profile.add(new Counsellor(id, email, firstName, lastName, picUrl, 27));
                    }
                    Counsellors_profile_size = Counsellors_profile.size();
                    //prepareAlbums();
                    for (int i =0 ; i<Counsellors_profile_size ; i++){
                        Log.e("name in prepare" , "-->" +Counsellors_profile.get(i).getUsername());
                        Counsellor counsellor_model = new Counsellor(Counsellors_profile.get(i).getUid() , Counsellors_profile.get(i).getUsername() , Counsellors_profile.get(i).getFirst_name(),Counsellors_profile.get(i).getLast_name(),Counsellors_profile.get(i).getAvatar(),23);
                        counsellorlist.add(counsellor_model);
                    }
                    counsellor_adapter.notifyDataSetChanged();
                    Log.e("size ", "==> " + size);
                    // Log.e("size1 ", "==> " + counsellors.get(0).getPicUrl());
                } else {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
                //progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            //progressDialog.dismiss();
            Toast.makeText(getActivity(), VoleyErrorHelper.getMessage(error, getActivity()), Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror", "error");
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                //params.put("user_education", Utility.getUserEducationUid(getActivity()));
                Log.e("all_coun_req", params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
 */