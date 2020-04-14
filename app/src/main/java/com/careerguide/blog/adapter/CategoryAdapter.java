package com.careerguide.blog.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.careerguide.blog.model.CatFilter;
import com.careerguide.blog.util.Utils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context ctx;
    private List<CatFilter> catFilters;

    public CategoryAdapter(Context ctx, List<CatFilter> catFilters) {
        this.ctx = ctx;
        this.catFilters = catFilters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lay_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id_pic.setVisibility(View.GONE);
        holder.id_title.setVisibility(View.GONE);
        holder.id_desc.setVisibility(View.GONE);
        holder.id_desc_pic.setVisibility(View.GONE);
        switch (catFilters.get(position).getType()){
            case "title_pic":
                holder.id_pic.setVisibility(View.VISIBLE);
                Utils.set_image(ctx, catFilters.get(position).getData(), holder.id_pic);
                break;
            case "title":
                holder.id_title.setVisibility(View.VISIBLE);
                holder.id_title.setText(Html.fromHtml(catFilters.get(position).getData()));
                break;
            case "paragraph":
                holder.id_desc.setVisibility(View.VISIBLE);
                holder.id_desc.setText(Html.fromHtml(catFilters.get(position).getData()));
                break;
            case "par_heading":
                holder.id_title.setVisibility(View.VISIBLE);
                holder.id_title.setText(Html.fromHtml(catFilters.get(position).getData()));
                break;
            case "par_pic":
                holder.id_desc_pic.setVisibility(View.VISIBLE);
                Utils.set_image(ctx, catFilters.get(position).getData(), holder.id_desc_pic);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return catFilters.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView id_pic, id_desc_pic;
        private AppCompatTextView id_title, id_desc;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_pic = itemView.findViewById(R.id.id_pic);
            id_title = itemView.findViewById(R.id.id_title);
            id_desc = itemView.findViewById(R.id.id_desc);
            id_desc_pic = itemView.findViewById(R.id.id_desc_pic);
        }
    }
}