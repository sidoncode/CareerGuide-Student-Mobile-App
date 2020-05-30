package com.careerguide.youtubeVideo;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import java.util.List;

public class YT_recycler_adapter extends RecyclerView.Adapter<YT_recycler_adapter.MyViewHolder> {

    private boolean seeAllMode=false;
    private List<Videos> videoList;
    private String key;
    private Activity activity;
    private int REQ_PLAYER_CODE  = 1;
    private int cornerRadius;
    private int cardColor;
    private int textColor;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name , desc;
        ImageView imageView;
        CardView ll_story;

        MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);

//            CardView cardView = view.findViewById(R.id.card_view);
//            cardView.setCardBackgroundColor(cardColor);
//            cardView.setRadius(cornerRadius);
            view.findViewById(R.id.videoViews).setVisibility(View.GONE);//not need in youtube category
            name = view.findViewById(R.id.counsellorName);
            desc = view.findViewById(R.id.desc);
            ll_story = view.findViewById(R.id.ll_story);
            name.setTextColor(Color.BLACK);
            name.setOnClickListener(v -> {
            });

            if(seeAllMode) {
                ll_story.setPadding(0,32,0,32);
            }else {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                (activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;
                CardView.LayoutParams params = new CardView.LayoutParams((int) (width / 1.25), CardView.LayoutParams.MATCH_PARENT);
                params.setMargins(50, 5, 50, 5);
                ll_story.setLayoutParams(params);
            }
        }
    }

    public YT_recycler_adapter(List<Videos> videoList, String yt_key, Activity activity, int cornerRadius, int textColor) {
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
        Videos video = videoList.get(position);
        Typeface font = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        Typeface font_desc = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-Regular.ttf");
        holder.name.setTypeface(font);
        holder.name.setText(video.getTitle());

        if(video.getDesc().isEmpty()){
            holder.desc.setVisibility(View.GONE);
            Log.e("isempty","-->");
        }
        else {
            Log.e("else","-->");
            holder.desc.setTypeface(font_desc);
            holder.desc.setText(video.getDesc());
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();
        Glide.with(activity).load(video.getThumbnailUrl() ).apply(options).into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext() , youtubeFeedDetail.class);
            intent.putExtra("data_id" , video.getVideoID());
            v.getContext().startActivity(intent);
            //Intent videoIntent = YouTubeStandalonePlayer.createVideoIntent(activity, key, video.getVideoID(), 0, true, false);
           // activity.startActivityForResult(videoIntent, REQ_PLAYER_CODE);
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
