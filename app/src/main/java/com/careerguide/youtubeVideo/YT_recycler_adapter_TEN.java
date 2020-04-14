package com.careerguide.youtubeVideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.R;
import com.careerguide.Video_player;

import java.util.List;

public class YT_recycler_adapter_TEN extends RecyclerView.Adapter<YT_recycler_adapter_TEN.MyViewHolder> {

    private List<Videos_TEN> videoList;
    private String key;
    private Activity activity;
    private int REQ_PLAYER_CODE  = 1;
    private int cornerRadius;
    private int cardColor;
    private int textColor;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name , desc;
        ImageView imageView;
        LinearLayout ll_story;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);

//            CardView cardView = view.findViewById(R.id.card_view);
//            cardView.setCardBackgroundColor(cardColor);
//            cardView.setRadius(cornerRadius);
            name = view.findViewById(R.id.name);
            desc = view.findViewById(R.id.desc);
            ll_story = view.findViewById(R.id.ll_story);
            name.setTextColor(textColor);
            name.setOnClickListener(v -> {
            });

            DisplayMetrics displayMetrics = new DisplayMetrics();
            (activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/1.25), RecyclerView.LayoutParams.MATCH_PARENT);
            params.setMargins(50, 5, 50, 5);
            ll_story.setLayoutParams(params);
        }
    }

    YT_recycler_adapter_TEN(List<Videos_TEN> videoList, String yt_key, Activity activity, int cornerRadius, int textColor) {
        this.activity  = activity;
        this.key = yt_key;
        this.videoList = videoList;
        this.cornerRadius = cornerRadius;
        this.textColor = textColor;

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
        Videos_TEN video = videoList.get(position);
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
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext() , Video_player.class);
            intent.putExtra("live_video_url" , video.getVideourl());
            intent.putExtra("Fullname" , video.getFullName());
            intent.putExtra("imgurl" , video.getImgurl());
            intent.putExtra("host_email" , video.getEmail());
            v.getContext().startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
