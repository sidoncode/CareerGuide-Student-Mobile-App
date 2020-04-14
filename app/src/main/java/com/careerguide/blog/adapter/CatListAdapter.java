package com.careerguide.blog.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.careerguide.blog.activity.CatDetailActivity;
import com.careerguide.blog.model.Categories;
import com.google.gson.Gson;

import java.util.List;

public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.ViewHolder> {
    private Context ctx;
    private List<Categories> categories;
    private Intent intent;

    public CatListAdapter(Context ctx, List<Categories> categories) {
        this.ctx = ctx;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_cat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id_title.setText(categories.get(position).getName());
        holder.id_title.setOnClickListener(v -> {
            intent = new Intent(ctx, CatDetailActivity.class);
            intent.putExtra("data", new Gson().toJson(categories.get(position)));
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView id_title;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_title = itemView.findViewById(R.id.id_title);
        }
    }
}