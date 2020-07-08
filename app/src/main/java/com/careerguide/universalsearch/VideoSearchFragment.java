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

public class VideoSearchFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ContentAdapter mAdapter;
    private Context mContext;



    public VideoSearchFragment() {
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

        mAdapter = new ContentAdapter(mContext, Utility.sessionListForSearch, new ContentAdapter.CardListener() {
            @Override
            public void onCardClick(int position) {

            }
        });




        mRecyclerView.setAdapter(mAdapter);



        return view;
    }
}