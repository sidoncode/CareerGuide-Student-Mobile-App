package com.careerguide.Book_One_To_One.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.Book_One_To_One.activity.OneToOneSessionActivity;
import com.careerguide.Book_One_To_One.model.OneToOneBookingsModel;
import com.careerguide.R;
import com.careerguide.Utility;

import java.util.ArrayList;
import java.util.List;

public class OneToOneBookingsAdapter {

    protected final int ANCHOR_UID = Integer.MAX_VALUE;
    private static final int MAX_MSG_COUNT = 20;
    private Context mContext;
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private OneToOneBookingsAdapter.MyAdapter mAdapter;
    private List<OneToOneBookingsModel> mBookingList;

    public OneToOneBookingsAdapter(RecyclerView listView) {
        mRecyclerView = listView;
        mContext = mRecyclerView.getContext();
        mInflater = LayoutInflater.from(mContext);
        mBookingList = new ArrayList<OneToOneBookingsModel>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mAdapter = new OneToOneBookingsAdapter.MyAdapter(mBookingList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void notifyDataIsChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void addBooking(OneToOneBookingsModel booking) {
        mBookingList.add(booking);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<OneToOneBookingsAdapter.MyViewHolder> {
        private List<OneToOneBookingsModel> mBookingList;

        MyAdapter(List<OneToOneBookingsModel> list) {
            mBookingList = list;
        }

        @Override
        public OneToOneBookingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OneToOneBookingsAdapter.MyViewHolder(mInflater.inflate(R.layout.one_to_one_recycler_view_mybookings, null));
        }

        @Override
        public void onBindViewHolder(OneToOneBookingsAdapter.MyViewHolder holder, int position) {

            OneToOneBookingsModel b = mBookingList.get(position);


            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .dontTransform();
            Glide.with(mContext).load(b.getProfile_pic()).apply(options).into((ImageView) holder.itemView.findViewById(R.id.counselorImage));

            if (b.getSessionHeld().contentEquals("0")){
                ((TextView)holder.itemView.findViewById(R.id.sessionHeld)).setText("Upcoming");
                ((TextView)holder.itemView.findViewById(R.id.sessionHeld)).setTextColor(Color.RED);
            }else{
                ((TextView)holder.itemView.findViewById(R.id.sessionHeld)).setText("Done");
                ((TextView)holder.itemView.findViewById(R.id.sessionHeld)).setTextColor(Color.GREEN);
            }

            ((TextView)holder.itemView.findViewById(R.id.counselorName)).setText(b.getCounselorName());
            ((TextView)holder.itemView.findViewById(R.id.bookingFor)).setText(b.getStudentName());
            ((TextView)holder.itemView.findViewById(R.id.textviewDate)).setText(b.getDateBooked());
            ((TextView)holder.itemView.findViewById(R.id.textviewTime)).setText(b.getTimeSlot());
            ((TextView)holder.itemView.findViewById(R.id.category)).setText(b.getCategory());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent((Activity)mContext, OneToOneSessionActivity.class);
                        Bundle data = new Bundle();


                        data.putString("booking_id", b.getBooking_id());
                        data.putString("channel_name", b.getChannelName());
                        data.putString("host_name", b.getCounselorName());
                        data.putString("host_image", b.getProfile_pic());
                        data.putString("privateUID", Utility.getUserId((Activity)mContext));
                        data.putString("privateUserName", b.getStudentName());
                        data.putString("privateSessionDate" , b.getDateBooked());
                        data.putString("privateSessionTime" , b.getTimeSlot());
                        data.putString("videoUrl" , b.getVideoUrl());
                        data.putString("sessionHeld" , b.getSessionHeld());


                        intent.putExtras(data);

                        mContext.startActivity(intent);
                    }
                });



        }
        @Override
        public int getItemCount() {
            return mBookingList.size();
        }
    }

    public void clearList(){
        mBookingList.clear();
    }
}

