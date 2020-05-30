package com.careerguide.youtubeVideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.CounsellorProfile;
import com.careerguide.R;
import com.careerguide.Video_player;

import java.util.List;

public class CommonEducationAdapter extends RecyclerView.Adapter<CommonEducationAdapter.MyViewHolder> {

    private List<CommonEducationModel> videoList;
    private Activity activity;
    boolean seeAllMode = false;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,counsellorName,videoViews,desc;
        ImageView imageView;
        CardView ll_story;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            desc=view.findViewById(R.id.desc);
            desc.setVisibility(View.GONE);
            title = view.findViewById(R.id.title);
            counsellorName=view.findViewById(R.id.counsellorName);
            videoViews=view.findViewById(R.id.videoViews);
            ll_story = view.findViewById(R.id.ll_story);
            if(seeAllMode) {
                ll_story.setPadding(0,32,0,32);
            }else {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                (activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                //int height = displayMetrics.heightPixels;
                CardView.LayoutParams params = new CardView.LayoutParams((int)(width/1.25), CardView.LayoutParams.WRAP_CONTENT);
                params.setMargins(50, 5, 50, 5);
                ll_story.setLayoutParams(params);
            }
        }
    }

    public CommonEducationAdapter(List<CommonEducationModel> videoList, Activity activity) {
        this.activity  = activity;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cm_yt_list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommonEducationModel video = videoList.get(position);
        Typeface font = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        Typeface font_title = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-Regular.ttf");
        holder.counsellorName.setTypeface(font);
        holder.title.setTypeface(font_title);
        holder.title.setText(video.getTitle());
//        holder.desc.setTypeface(font_desc);

        holder.counsellorName.setText("By:"+video.getFullName());
        holder.videoViews.setText("Views:"+video.getVideoViews());
        //holder.desc.setText(video.getDesc());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();
        Glide.with(activity).load(video.getImgurl() ).apply(options).into(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext() , Video_player.class);
            intent.putExtra("video_id" , video.getVideoId());
            intent.putExtra("live_video_url" , video.getVideourl());
            intent.putExtra("Fullname" , video.getFullName());
            intent.putExtra("imgurl" , video.getImgurl());
            intent.putExtra("title" , video.getTitle());
            intent.putExtra("host_email" , video.getEmail());
            intent.putExtra("video_views" , video.getVideoViews());
            intent.putExtra("host_img" , video.getProfilePicUrl());
            v.getContext().startActivity(intent);
        });

        holder.title.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext() , Video_player.class);
            intent.putExtra("video_id" , video.getVideoId());
            intent.putExtra("live_video_url" , video.getVideourl());
            intent.putExtra("Fullname" , video.getFullName());
            intent.putExtra("imgurl" , video.getImgurl());
            intent.putExtra("title" , video.getTitle());
            intent.putExtra("host_email" , video.getEmail());
            intent.putExtra("host_img" , video.getProfilePicUrl());
            intent.putExtra("video_views" , video.getVideoViews());
            v.getContext().startActivity(intent);
        });

        holder.counsellorName.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext() , CounsellorProfile.class);
            intent.putExtra("id"  , video.getUserId());
            intent.putExtra("host_name" , video.getFullName());
            intent.putExtra("host_email" , video.getEmail());
            intent.putExtra("host_img" , video.getProfilePicUrl());
            intent.putExtra("imgurl" , video.getImgurl());
            intent.putExtra("video_views" , video.getVideoViews());
            intent.putExtra("host_img" , video.getProfilePicUrl());
            v.getContext().startActivity(intent);
        });

    }

    private void startVideoPlay(){

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void setSeeAllMode(boolean b)
    {
        this.seeAllMode = b;
    }

}
