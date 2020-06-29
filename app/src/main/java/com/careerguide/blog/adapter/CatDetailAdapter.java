package com.careerguide.blog.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.careerguide.blog.activity.CategoryActivity;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.google.gson.Gson;

import java.util.List;

import static com.careerguide.R.layout.lay_cat_detail;

public class CatDetailAdapter extends RecyclerView.Adapter<CatDetailAdapter.ViewHolder> {
    private Context ctx;
    private List<CategoryDetails> categoryDetails;
    private Intent intent;
    private boolean seeAllMode=false;


    public CatDetailAdapter(Context ctx, List<CategoryDetails> categoryDetails) {
        this.ctx = ctx;
        this.categoryDetails = categoryDetails;
        Log.e("#inside adapter" , "-->" );

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(lay_cat_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Utils.set_image(ctx, categoryDetails.get(position).getPic_url(), (AppCompatImageView) holder.id_pic);
        holder.id_title.setText(categoryDetails.get(position).getTitle());
        holder.id_desc.setText(categoryDetails.get(position).getDesc());
        holder.lay_cv.setOnClickListener(v -> {
            intent = new Intent(ctx, CategoryActivity.class);
            intent.putExtra("data", new Gson().toJson(categoryDetails.get(position)));
            intent.putExtra("pos", position);
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryDetails.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
         CardView lay_cv;
         ImageView id_pic;
         TextView id_title, id_desc;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lay_cv = itemView.findViewById(R.id.lay_cv);
            id_pic = itemView.findViewById(R.id.id_pic);
            id_title = itemView.findViewById(R.id.id_title);
            id_desc = itemView.findViewById(R.id.id_desc);


            if(!seeAllMode) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int) (width / 1.25), RecyclerView.LayoutParams.WRAP_CONTENT);
                params.setMargins(30, 5, 20, 5);
                lay_cv.setLayoutParams(params);
            }
        }
    }


    public void setSeeAllMode(boolean b)
    {
        this.seeAllMode = b;
    }

}