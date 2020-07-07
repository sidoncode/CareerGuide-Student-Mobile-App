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
import com.careerguide.youtubeVideo.CommonEducationModel;

import java.util.ArrayList;

public class SessionSearchFragment extends Fragment {
    private RecyclerView mRecyclerView;
    public ContentAdapter mAdapter;
    private Context mContext;



    public SessionSearchFragment() {
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




        sessionFilter(UniversalSearchFragment.queryKeyword);


        mRecyclerView.setAdapter(mAdapter);



        return view;
    }


    void sessionFilter(String query){
        ArrayList<Object> temp = new ArrayList();
        for(Object d: Utility.sessionListForSearch){

            if (d instanceof CommonEducationModel) {
                CommonEducationModel session = (CommonEducationModel)d;
                if(session.getTitle().toLowerCase().contains(query.toLowerCase()) || session.getFullName().toLowerCase().contains(query.toLowerCase()) || session.getVideoCategory().toLowerCase().contains(query.toLowerCase())){
                    temp.add(d);
                }
            }

        }

        mAdapter = new ContentAdapter(mContext, temp, new ContentAdapter.CardListener() {
            @Override
            public void onCardClick(int position) {

            }
        });
    }



}