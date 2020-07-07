package com.careerguide.universalsearch;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.Counsellor;
import com.careerguide.HomeActivity;
import com.careerguide.ProgressDialogCustom;
import com.careerguide.R;
import com.careerguide.VoleyErrorHelper;
import com.careerguide.VolleySingleton;
import com.careerguide.blog.DataMembers;
import com.careerguide.blog.adapter.CatDetailAdapter;
import com.careerguide.blog.model.Categories;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.careerguide.youtubeVideo.CommonEducationModel;
import com.careerguide.youtubeVideo.Videos;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mayank on 7/1/2020.
 * Mayank Develops
 * mayankdevelops@gmail.com
 */
public class UniversalSearchFragment extends Fragment {


    SearchView searchView;
    public static String queryKeyword="";
    ImageView serachBg;

    private List<CommonEducationModel> allPastLiveSessionList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_universal_search,container, false);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);


        serachBg = view.findViewById(R.id.search_bg);
        searchView = view.findViewById(R.id.search_view);
        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black_search));
        searchEditText.setHintTextColor(getResources().getColor(R.color.grey));
        ImageView searchIcon=
                searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);

        // To change color of close button, use:
        // ImageView searchCloseIcon = (ImageView)searchView
        //        .findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(getResources().getColor(R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


               /* if (adapter.getItem(tabs.getSelectedTabPosition()) instanceof SessionSearchFragment)
                {

                    SessionSearchFragment sessionSearchFragment = (SessionSearchFragment)adapter.getItem(tabs.getSelectedTabPosition());
                    sessionSearchFragment.performFilter(query);

                }*/



                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                serachBg.setVisibility(View.GONE);
                queryKeyword = newText;
                tabs.setVisibility(View.VISIBLE);
                setupViewPager(viewPager);
                tabs.setupWithViewPager(viewPager);



                return false;
            }
        });






        if(!(allPastLiveSessionList.size()>1))
            getPastLiveSessions();
        if(!(categoryDetails.size()>1))
            new TaskBlog().execute();



        return view;

    }


    // Add Fragments to Tabs
    Adapter adapter;
    SessionSearchFragment sessionSearchFragment;
    ArticleSearchFragment articleSearchFragment;
    CounsellorSearchFragment counsellorSearchFragment;
    private void setupViewPager(ViewPager viewPager) {

        sessionSearchFragment = new SessionSearchFragment();
        articleSearchFragment = new ArticleSearchFragment();
        counsellorSearchFragment = new CounsellorSearchFragment();

        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(sessionSearchFragment, "Sessions");
    //   adapter.addFragment(new VideoSearchFragment(), "Videos");

        adapter.addFragment(counsellorSearchFragment, "Counsellors");
        adapter.addFragment(articleSearchFragment, "Articles");
        viewPager.setAdapter(adapter);




    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }






    private LinearLayoutManager lm;
    private CompositeDisposable disposable;

    private List<CategoryDetails> categoryDetails=new ArrayList<>();
    private Categories categories;
    private boolean loading = false;
    int page_no = 1, past_visible_count, visible_count, total_Count;




    private void getPastLiveSessions() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, com.careerguide.Utility.PRIVATE_SERVER + "AllVideos", response -> {
            Log.e("all_coun_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean status = jsonObject.optBoolean("status", false);
                if (status)
                {
                    JSONArray counsellorsJsonArray = jsonObject.optJSONArray("counsellors");

                    Log.e("lengthname--> " , "==> " +counsellorsJsonArray.length() );

                    allPastLiveSessionList.clear();//clear all the old data and fetch new data

                    for (int i = 0; i < counsellorsJsonArray.length(); i++)
                    {

                        JSONObject JsonObject = counsellorsJsonArray.optJSONObject(i);
                        String user_id = JsonObject.optString("user_id");
                        String email = JsonObject.optString("email");
                        String name = JsonObject.optString("Name");
                        String img_url = JsonObject.optString("img_url");
                        String title = JsonObject.optString("title");
                        String video_url = JsonObject.optString("video_url");
                        String video_views=JsonObject.optString("views");
                        String video_id = JsonObject.optString("id");
                        String video_category=JsonObject.optString("Video_category");
                        String profile_pic="https://app.careerguide.com/api/user_dir/"+JsonObject.optString("profile_pic");
                        allPastLiveSessionList.add(new CommonEducationModel(user_id,email, name, img_url, video_url, title, profile_pic,video_views,video_id,video_category));


                    }

                    Utility.sessionListForSearch = new ArrayList<>(allPastLiveSessionList);



                    //   Log.e("size1 " , "==> " +counsellors.get(0).getPicUrl());
                } else {
                    Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {


            Toast.makeText(getContext(), VoleyErrorHelper.getMessage(error,getContext()),Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror","error");
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    private class TaskBlog extends AsyncTask<Void, Void, Void> {

        DataMembers displaylist;
        List<DataMembers> displaylistArray = new ArrayList<>();
        CompositeDisposable disposable;
        Categories categories;

        List<CategoryDetails> categoryDetails;

        @Override
        protected Void doInBackground(Void... params) {
            disposable = new CompositeDisposable();

            categoryDetails = new ArrayList<>();
            //   categories = new Gson().fromJson(bundle.getString("data"), Categories.class);
            disposable.add(Utils.get_api().get_cat_detail("50", "1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<List<CategoryDetails>>() {
                        @Override
                        public void onSuccess(List<CategoryDetails> cd) {
                            if (cd != null) {
                                for (CategoryDetails c : cd) {
                                    c.setTitle(Utils.remove_tags(c.getTitle()));
                                    c.setDesc(Utils.remove_tags(c.getDesc()));
                                    Log.e("#c" , "-->" +c);
                                    categoryDetails.add(c);
                                }
                                Utility.articleListForSearch = new ArrayList<>(categoryDetails);
                               // viewModelProvider.setDisplaylistArray_categoryDetails(categoryDetails);
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.e("Error Occured" , "-->");
                        }
                    }));


//            try {
//                String url_Blog = "https://institute.careerguide.com/wp-json/wp/v2/posts";
//                String response_Blog = Utility.getUrlString(url_Blog);
//                JSONArray jsonArray_Blog = new JSONArray(response_Blog);
//                Log.e("Jsonresult" , "--> " +response_Blog);
//                for (int i = 0; i < jsonArray_Blog.length(); i++) {
//                    JSONObject jsonObject;
//                    jsonObject = (JSONObject) jsonArray_Blog.get(i);
//                    JSONObject jsonObject1;
//                    jsonObject1 = (JSONObject) jsonObject.get("title");
//                    JSONObject jsonObject2;
//                    jsonObject2 = (JSONObject) jsonObject.get("content");
//                    JSONObject jsonObject3;
//                    jsonObject3 = (JSONObject) jsonObject.get("excerpt");
//                    String imgUrl = getFetaureImageUrl(jsonObject2.getString("rendered"));
//                    displaylist = new DataMembers(jsonObject.getInt("id"), jsonObject1.getString("rendered") , jsonObject2.getString("rendered") ,imgUrl , jsonObject.getString("link"), jsonObject3.getString("rendered"));
//                    displaylistArray.add(displaylist);
//                }
//                Log.e("#Blog","-->");
//            }catch(Exception e1)
//            {
//                e1.printStackTrace();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //      setDisplaylistArray_Blog(displaylistArray);
          //  viewModelProvider.setDisplaylistArray_categoryDetails(categoryDetails);

        }
    }




}