package com.careerguide.blog.adapter;

import android.content.Context;
import android.graphics.Typeface;
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

import static com.careerguide.R.layout.lay_category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context ctx;
    private List<CatFilter> catFilters;
    public Typeface fontSemiBold,fontRegular;


    public CategoryAdapter(Context ctx, List<CatFilter> catFilters) {
        this.ctx = ctx;
        this.catFilters = catFilters;
        fontSemiBold = Typeface.createFromAsset(ctx.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        fontRegular = Typeface.createFromAsset(ctx.getAssets() , "fonts/Montserrat-Regular.ttf");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(lay_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id_pic.setVisibility(View.GONE);
        holder.id_title.setVisibility(View.GONE);
        holder.id_desc.setVisibility(View.GONE);
        holder.id_desc_pic.setVisibility(View.GONE);

        holder.id_title.setTypeface(fontSemiBold);
        holder.id_desc.setTypeface(fontRegular);


        switch (catFilters.get(position).getType()){
            case "title_pic":
                holder.id_pic.setVisibility(View.VISIBLE);
                Utils.set_image(ctx, catFilters.get(position).getData(), holder.id_pic);
                break;

            case "title":
            case "par_heading":
                holder.id_title.setVisibility(View.VISIBLE);
                holder.id_title.setText(Html.fromHtml(catFilters.get(position).getData()).toString());
                break;

            case "paragraph":
                holder.id_desc.setVisibility(View.VISIBLE);
                holder.id_desc.setText(Html.fromHtml(catFilters.get(position).getData()).toString());
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