package com.careerguide;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.careerguide.adapters.CounsellorAdapter;
import com.careerguide.models.Counsellor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class All_Couns_Fragment extends Fragment {
    private RecyclerView recycler_view;
    private CounsellorAdapter counsellor_adapter;
    private List<Counsellor> counsellorlist;
    LinearLayoutManager mLayoutManager;
    private ArrayList<Counsellor> Counsellors_profile = new ArrayList<>();
    private int Counsellors_profile_size;
    private int size;
    private ProgressBar pb_loading;
    private View view;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public All_Couns_Fragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_all_couns, container, false);
        pb_loading = view.findViewById(R.id.pb_loading1);
        recycler_view = view.findViewById(R.id.recycler_view1);
        counsellorlist = new ArrayList<>();
        counsellor_adapter = new CounsellorAdapter(getActivity(), counsellorlist);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(counsellor_adapter);
        navController= Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container);
        bottomNavigationView= getActivity().findViewById(R.id.bottom_navigation);
        getCounsellor();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK &&  event.getAction() == KeyEvent.ACTION_DOWN )
                {

                    navController.popBackStack();
                    navController.navigate(R.id.nav_to_homeFragment);
                    bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    return true;
                }
                return false;
            }
        } );
        return view;
    }

    private void prepareAlbums() {
        for (int i =0 ; i<Counsellors_profile_size ; i++){
            Log.e("name in prepare" , "-->" +Counsellors_profile.get(i).getUsername());
            com.careerguide.models.Counsellor counsellor_model = new com.careerguide.models.Counsellor(Counsellors_profile.get(i).getUid() , Counsellors_profile.get(i).getUsername() , Counsellors_profile.get(i).getFirst_name(),Counsellors_profile.get(i).getLast_name(),Counsellors_profile.get(i).getAvatar(),23);
            counsellorlist.add(counsellor_model);
        }
        counsellor_adapter.notifyDataSetChanged();
    }

    private void getCounsellor(){
        Counsellors_profile = convert(com.careerguide.universalsearch.Utility.counsellorListForSearch);
        Counsellors_profile_size = Counsellors_profile.size();
        pb_loading.setVisibility(View.GONE);
        prepareAlbums();
    }
    public ArrayList<Counsellor> convert(ArrayList<Object> a) {
        return (ArrayList) a;
    }


 /*   private void getcounsellor() {
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
                        Counsellors_profile.add(new Counsellor(id, email , firstName, lastName, picUrl,27));
                    }
                    Counsellors_profile_size = Counsellors_profile.size();
                    prepareAlbums();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_LONG).show();
                }
                pb_loading.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            pb_loading.setVisibility(View.GONE);
            //Toast.makeText(this, VoleyErrorHelper.getMessage(error, this.getActivity()), Toast.LENGTH_LONG).show();
            Log.e("all_coun_rerror", "error");
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                //params.put("user_education", Utility.getUserEducationUid(this));
                Log.e("all_coun_req", params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }*/



    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }


        @Override
        public void getItemOffsets(@NonNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

