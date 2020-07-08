package com.careerguide.universalsearch;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.careerguide.R;
import com.careerguide.models.Counsellor;

import java.util.ArrayList;

public class CounsellorSearchFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ContentAdapter mAdapter;
    private Context mContext;



    public CounsellorSearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_holder, container, false);


        mContext=getContext();
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);



        counsellorFilter(UniversalSearchFragment.queryKeyword);



        mRecyclerView.setAdapter(mAdapter);



        return view;
    }

    void counsellorFilter(String query){

        ArrayList<Object> temp = new ArrayList();
        for(Object d: Utility.counsellorListForSearch) {


            if (d instanceof Counsellor) {
                Counsellor counsellor = (Counsellor) d;
                if ((counsellor.getFirst_name() + counsellor.getLast_name()).toLowerCase().contains(query.toLowerCase()) || (counsellor.getUsername().toLowerCase().contains(query.toLowerCase()))) {
                    temp.add(d);
                }
            }
        }

        //In case no counsellors are found for a keyword, display all of them
        if(temp.size()==0)
            temp=Utility.counsellorListForSearch;
        mAdapter = new ContentAdapter(mContext,temp, new ContentAdapter.CardListener() {
            @Override
            public void onCardClick(int position) {

            }
        });

    }

}