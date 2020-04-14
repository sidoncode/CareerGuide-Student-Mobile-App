package com.careerguide.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careerguide.*;
import com.careerguide.activity.SubcategoryActivity;
import com.careerguide.models.Goal;

import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    Context context;
    List<Goal> goalList;
    //TypefaceUtility tfUtil;

    public class GoalViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_goal;
        ImageView iv_icon;
        TextView tv_name,tv_sub_name;

        public GoalViewHolder(View itemView) {
            super(itemView);
            ll_goal = itemView.findViewById(R.id.ll_goal);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sub_name = itemView.findViewById(R.id.tv_sub_name);
           // tv_name.setTypeface(tfUtil.getTypefaceRegular());
        }
    }

    public GoalAdapter(Context context, List<Goal> goalList) {
        this.context = context;
        this.goalList = goalList;
      //  tfUtil = new TypefaceUtility(context);
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_goal, parent, false);
        return new GoalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {
        final Goal goal=goalList.get(position);

        holder.tv_name.setText(goal.getName());
        holder.tv_sub_name.setText(goal.getCat_placeholder());
        holder.ll_goal.setOnClickListener(v -> {
            Intent bIntent = new Intent(context, SubcategoryActivity.class);
            bIntent.putExtra("type","updatecat");
            bIntent.putExtra("cat_uid",goal.getUid());
            bIntent.putExtra("cat_title",goal.getName());
            bIntent.putExtra("icon_url" , goal.getIcon_url());
            v.getContext().startActivity(bIntent);
        });
        Glide.with(context).load(goal.getIcon_url()).into(holder.iv_icon);

    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }

}
