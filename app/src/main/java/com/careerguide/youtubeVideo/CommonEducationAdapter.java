package com.careerguide.youtubeVideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.R;
import com.careerguide.Video_player;

import java.util.List;

public class CommonEducationAdapter extends RecyclerView.Adapter<CommonEducationAdapter.MyViewHolder> {

    private List<CommonEducationModel> videoList;
    private Activity activity;
    boolean seeAllMode = false;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name , desc;
        ImageView imageView;
        LinearLayout ll_story;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);

            name = view.findViewById(R.id.name);
            desc = view.findViewById(R.id.desc);
            ll_story = view.findViewById(R.id.ll_story);
            name.setOnClickListener(v -> {
            });

            if(seeAllMode) {
                ll_story.setPadding(0,32,0,32);
            }else {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                (activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                //int height = displayMetrics.heightPixels;
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/1.25), RecyclerView.LayoutParams.WRAP_CONTENT);
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
        Typeface font_desc = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-Regular.ttf");
        holder.name.setTypeface(font);
        holder.name.setText(video.getTitle());
        holder.desc.setTypeface(font_desc);
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
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext() , Video_player.class);
            intent.putExtra("id" , video.getId());
            intent.putExtra("live_video_url" , video.getVideourl());
            intent.putExtra("Fullname" , video.getFullName());
            intent.putExtra("imgurl" , video.getImgurl());
            intent.putExtra("host_email" , video.getEmail());
            intent.putExtra("video_views" , video.getVideoViews());
            v.getContext().startActivity(intent);
        });



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
