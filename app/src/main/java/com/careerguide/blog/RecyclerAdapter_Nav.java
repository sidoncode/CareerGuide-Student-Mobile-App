package com.careerguide.blog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.R;
import com.careerguide.newsfeed.NewsFragment;

import java.util.List;

public class RecyclerAdapter_Nav extends RecyclerView.Adapter<RecyclerAdapter_Nav.Vholder> {

    private Context context;
    private List<DataMembers> dataMemberses;


    public RecyclerAdapter_Nav(Context context, List<DataMembers> dataMemberses) {
        this.context = context;
        this.dataMemberses = dataMemberses;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_item,parent,false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(Vholder holder, int position) {
        final DataMembers dataMembers = dataMemberses.get(position);
        Typeface font = Typeface.createFromAsset(context.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        Typeface font_small = Typeface.createFromAsset(context.getAssets() , "fonts/Montserrat-Regular.ttf");
        holder.text.setTypeface(font);
        holder.post_Desc.setTypeface(font_small);
        holder.text.setText(dataMembers.postTitle);
        holder.post_Desc.setText(dataMembers.getPostDesc());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .dontAnimate()
                .dontTransform();
        Glide.with(context).load(dataMembers.imgurl).apply(options).into(holder.image);
        holder.readMore.setOnClickListener(view -> {
           // Toast.makeText(context, "Read More Clicked", Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(context,DetailActivity.class);
            intent.putExtra("url",dataMembers.postCode);
            intent.putExtra("posttitle",dataMembers.postTitle);
            Log.i("Sending:",dataMembers.posturl);
            BlogActivity.tContent = dataMembers.posturl;
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataMemberses.size();
    }

    class Vholder extends RecyclerView.ViewHolder {

        TextView text , post_Desc;
        ImageView image;
        ConstraintLayout readMore;
        CardView ll_story;

        Vholder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.main_image);
            text = itemView.findViewById(R.id.post_title);
            post_Desc = itemView.findViewById(R.id.post_Desc);
            readMore = itemView.findViewById(R.id.readmorebut);
            ll_story = itemView.findViewById(R.id.ll_story);
//
//            DisplayMetrics displayMetrics = new DisplayMetrics();
//            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            int width = displayMetrics.widthPixels;
//            int height = displayMetrics.heightPixels;
//            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/1.25), RecyclerView.LayoutParams.WRAP_CONTENT);
//            params.setMargins(30, 5, 20, 5);
//            ll_story.setLayoutParams(params);
        }
    }

}
