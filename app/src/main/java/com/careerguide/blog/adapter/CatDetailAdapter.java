package com.careerguide.blog.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.careerguide.blog.activity.CategoryActivity;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.blog.util.Utils;
import com.google.gson.Gson;

import java.util.List;

public class CatDetailAdapter extends RecyclerView.Adapter<CatDetailAdapter.ViewHolder> {
    private Context ctx;
    private List<CategoryDetails> categoryDetails;
    private Intent intent;

    public CatDetailAdapter(Context ctx, List<CategoryDetails> categoryDetails) {
        this.ctx = ctx;
        this.categoryDetails = categoryDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_cat_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Utils.set_image(ctx, categoryDetails.get(position).getPic_url(), holder.id_pic);
        holder.id_title.setText(categoryDetails.get(position).getTitle());
        holder.id_desc.setText(categoryDetails.get(position).getDesc());
        holder.lay_cv.setOnClickListener(v -> {
            intent = new Intent(ctx, CategoryActivity.class);
            intent.putExtra("data", new Gson().toJson(categoryDetails.get(position)));
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryDetails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView lay_cv;
        private AppCompatImageView id_pic;
        private AppCompatTextView id_title, id_desc;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lay_cv = itemView.findViewById(R.id.lay_cv);
            id_pic = itemView.findViewById(R.id.id_pic);
            id_title = itemView.findViewById(R.id.id_title);
            id_desc = itemView.findViewById(R.id.id_desc);
        }
    }
}