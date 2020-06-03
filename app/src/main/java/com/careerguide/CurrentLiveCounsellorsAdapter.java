package com.careerguide;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.youtubeVideo.youtubeFeedDetail;

import java.util.List;

public class CurrentLiveCounsellorsAdapter extends RecyclerView.Adapter<CurrentLiveCounsellorsAdapter.MyViewHolder> {

    private Context mContext;
    private List<CurrentLiveCounsellorsModel> listDataModels;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCounsellorName , txtdesc;
        LinearLayout backgroundLayout;
        ImageView imgCounsellor;

        MyViewHolder(View view) {
            super(view);
            imgCounsellor = view.findViewById(R.id.imgCounsellor);
            txtCounsellorName = view.findViewById(R.id.txtCounsellorName);
            txtdesc = view.findViewById(R.id.txtDesc);
            backgroundLayout = view.findViewById(R.id.backgroundLayout);
        }
    }


    public CurrentLiveCounsellorsAdapter(Context mContext, List<CurrentLiveCounsellorsModel> listDataModels) {
        this.mContext = mContext;
        Log.e("#adapter" , "-->" +listDataModels);
        this.listDataModels = listDataModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_live_counsellors_single_item_recyclerview, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        CurrentLiveCounsellorsModel objDataModels = listDataModels.get(position);
        holder.txtCounsellorName.setText(objDataModels.getCounsellorName());
        holder.txtdesc.setText(objDataModels.getscheduleDescription());
        if(objDataModels.getscheduleDescription().contains("LIVE AT")){
            holder.backgroundLayout.setBackgroundColor(Color.GRAY);
            holder.txtCounsellorName.setTextColor(Color.WHITE);
            holder.txtdesc.setTextColor(Color.WHITE);
        }
        else {
            holder.txtCounsellorName.setTextColor(Color.BLACK);
            holder.txtdesc.setTextColor(Color.BLACK);
        }
        Log.i("desssss",holder.txtdesc.getText().toString()+"___"+objDataModels.getscheduleDescription());

        if(objDataModels.getCounsellorName().contains("FaceBook.com")) {//handles facebook live
            Glide.with(mContext).load(objDataModels.getImgSrc()).into(holder.imgCounsellor);
            holder.backgroundLayout.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext() , youtubeFeedDetail.class);
                intent.putExtra("data_id" , objDataModels.getchannelname());//gets the channel id
                view.getContext().startActivity(intent);
            });
        }else {

            if (!objDataModels.getscheduleDescription().equals("")) {//handle if now counsellor is not live
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true);

                holder.imgCounsellor.setImageResource(0);

                Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(objDataModels.getImgSrc()).into(holder.imgCounsellor);
                holder.backgroundLayout.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), ViewerLiveActivity.class);
                    Log.e("name-->", "" + objDataModels.getchannelname());
                    intent.putExtra("Channel_name", objDataModels.getchannelname());
                    intent.putExtra("name", objDataModels.getCounsellorName());
                    intent.putExtra("imgurl" , objDataModels.getImgSrc());
                    intent.putExtra("title" , objDataModels.getTitle());
                    intent.putExtra("scheduledesc" , objDataModels.getscheduleDescription());
                    intent.putExtra("channel_link" , objDataModels.getchannelname());
                    view.getContext().startActivity(intent);

                });
            }
        }

    }

    @Override
    public int getItemCount() {
        Log.e("#adapterdd" , "-->" +listDataModels.size());
        return listDataModels.size();
    }
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView txtRestaurantName, txtDesc;
//        ImageView img;
//
//        public MyViewHolder(View view) {
//            super(view);
//            img = (ImageView) view.findViewById(R.id.imgRestaurant);
//            txtRestaurantName = (TextView) view.findViewById(R.id.txtRestaurantName);
//            txtDesc = (TextView) view.findViewById(R.id.txk[j[]tDesc);
//
//        }
//    }
}